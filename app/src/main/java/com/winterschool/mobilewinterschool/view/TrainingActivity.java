package com.winterschool.mobilewinterschool.view;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.winterschool.mobilewinterschool.R;
import com.winterschool.mobilewinterschool.controller.ConnectTask;
import com.winterschool.mobilewinterschool.controller.WriteService;
import com.winterschool.mobilewinterschool.model.TrainingData;
import com.winterschool.mobilewinterschool.controller.server.Server;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class TrainingActivity extends AppCompatActivity {
	private WriteService  mWriteService;
	public TrainingData mTrainingData;
	private ConnectTask mConnectTask;
	private Thread connectThread;
	private BluetoothDevice mDevice;
	private Context context = this;
	private Thread mTrainingThread;

	static final int REQUEST_IMAGE_CAPTURE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_training);
		dispatchTakePictureIntent();

		try {
			mTrainingData = new TrainingData(getToken());
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		connectToDevice();

		Button button = (Button) findViewById(R.id.stop_training_button);
		button.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Server.getInstance().stopSignalRequest(mTrainingData.getSessionId(), mTrainingData.getToken(), stopSignalHandler);
				stopTraining();
			}
		});

	}

	private void stopTraining(){
		mTrainingThread.interrupt();
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			galleryAddPic();
			setPic();
			//CRYPT
			mTrainingThread = new Thread(new Runnable() {
				@Override
				public void run() {
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH); //DD/MM/YY HH:MM:SS
					format.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
					Server.getInstance().photoRequest("imagePath", format.format(new Date()), mTrainingData.getToken(), photoHandler);
				}
			});
			mTrainingThread.start();
		}
	}

	String mCurrentPhotoPath;

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}

	static final int REQUEST_TAKE_PHOTO = 1;

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File
				String errorMessage = "Whoops - your device doesn't support the crop action!";
				Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
				toast.show();
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				Uri photoURI = FileProvider.getUriForFile(this,
						"com.winterschool.mobilewinterschool.fileprovider",
						photoFile);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
				startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
			}
		}
	}

	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

	ImageView mImageView;

	private void setPic() {
		// Get the dimensions of the View
		mImageView = (ImageView) findViewById(R.id.mImageView);
		int targetW = 500;/*mImageView.getWidth();*/
		int targetH = 1000;/*mImageView.getHeight();*/

		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		mImageView.setImageBitmap(bitmap);
	}

	private String getToken() {
		Bundle extras = getIntent().getExtras();
		return extras.getString(Intent.EXTRA_TEXT);
	}

	public void connectToDevice(){
		Intent intent = getIntent();
		mDevice = intent.getParcelableExtra("device");
		mConnectTask = new ConnectTask(mDevice, mTrainingData, this.getApplicationContext(), mConnectErrorHandler);
		connectThread = new Thread(mConnectTask);
		connectThread.start();
	}

	private Handler mConnectErrorHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(final Message msg) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getBaseContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
				}
			});
			return false;
		}
	});

	Handler photoHandler = new Handler() {
		public void handleMessage(final Message message) {
			mTrainingData.setSessionId(1234567);
			mWriteService = new WriteService(mTrainingData, pulseHandler);
			runOnUiThread(mWriteService);
			/*if(message.arg1 == Server.ACK_PHOTO) {
				mTrainingData.setSessionId((int) message.obj);
			}
			else {
				if (message.arg1 == Server.ERR_CONNECTION)
					createToast("Нет связи с сервером");
				else if (message.arg1 == Server.ERR_PHOTO_PATH)
					createToast("Ошибка при обработке фотографии");
				else if (message.arg1 == Server.ERR_PHOTO)
					createToast("Ошибка при отправке фотографии");
				//stopTraining();
			}*/
		}
	};

	Handler pulseHandler = new Handler() {
		public void handleMessage(final Message message) {
			if(message.arg1 != Server.ACK_PULSE) {
				if (message.arg1 == Server.ERR_CONNECTION)
					createToast("Нет связи с сервером");
				else if (message.arg1 == Server.ERR_PULSE)
					createToast("Ошибка при отправке пульса");
				//stopTraining();
			}
		}
	};

	Handler stopSignalHandler = new Handler() {
		public void handleMessage(final Message message) {
			if(message.arg1 != Server.ACK_STOP) {
				if (message.arg1 == Server.ERR_CONNECTION)
					createToast("Нет связи с сервером");
				else if (message.arg1 == Server.ERR_INVALID_SESSION)
					createToast("Сессия пуста или отсутствует на сервере");
				else if (message.arg1 == Server.ERR_END_SESSION)
					createToast("Сессия уже завершена");
				//stopTraining();
			}
		}
	};

	public void createToast(final String toastMessage) {
		runOnUiThread(new Runnable() {
			public void run() {
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, toastMessage, duration);
				toast.show();
			}
		});
	}
}
