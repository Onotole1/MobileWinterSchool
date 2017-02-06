package com.winterschool.mobilewinterschool.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.winterschool.mobilewinterschool.R;
import com.winterschool.mobilewinterschool.controller.ConnectTack;
import com.winterschool.mobilewinterschool.controller.Core;
import com.winterschool.mobilewinterschool.model.TrainingData;

public class TrainingActivity extends AppCompatActivity {
	private Core mCore;
	private TrainingData mTrainingData;
	private ConnectTack mCconnectTack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_training);

		try {
			mTrainingData = new TrainingData(getToken());
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		mCore = new Core(mTrainingData);
		//mCore.takePhoto();
		mCore.startTraining();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//super.onActivityResult(requestCode, resultCode, data);
		mCore.startTraining();
	}

	private String getToken() {
		Bundle extras = getIntent().getExtras();
		return extras.getString(Intent.EXTRA_TEXT);
	}

	private void connectionError(){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getBaseContext(), getString(R.string.connection_error), Toast.LENGTH_SHORT);
			}
		});
	}
}
