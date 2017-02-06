package com.winterschool.mobilewinterschool.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.winterschool.mobilewinterschool.R;

import java.util.ArrayList;

public class SettingsActivity extends Activity {

	private Button startTrainingButton;
	private Button restartScanButton;
	private ListView deviceListView;
	private TextView textViewInfo;

	private Bundle extrasIn;
	private BluetoothAdapter bluetoothAdapter;
	private Handler mHandler;

	private ArrayList<String> deviceNameList;
	private ArrayList<BluetoothDevice> deviceList;
	private ArrayAdapter<String> adapterList;
	private BluetoothDevice neededDevice = null;

	private static final int bluetoothRequestCode = 1;
	private static final long SCAN_PERIOD = 5000;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		initialization();
		setClickListener();

		if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
			Log.i("Bluetooth", "Bluetooth isn`t enable");
			Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBT, bluetoothRequestCode);
		} else {
			Log.i("Bluetooth", "Bluetooth enable");
			scanDevice(true);
		}
	}

	private void initialization(){

		extrasIn = getIntent().getExtras();
		Log.d("token", extrasIn.getString("token"));

		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		mHandler = new Handler();

		startTrainingButton = (Button)findViewById(R.id.startTraining);
		restartScanButton = (Button) findViewById(R.id.restartScan);
		deviceListView = (ListView) findViewById(R.id.deviceList);
		textViewInfo = (TextView) findViewById(R.id.textInfo);

		startTrainingButton.setEnabled(false);

		deviceNameList = new ArrayList<>();
		deviceList = new ArrayList<>();
		adapterList = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceNameList);
		deviceListView.setAdapter(adapterList);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			this.requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, bluetoothRequestCode);
		}
		Log.i("Initialization", "Initialization complete");
	}

	private void setClickListener(){
		View.OnClickListener onClickListenerStartTraining = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), TrainingActivity.class);
				intent.putExtra("token", extrasIn.getString("token"));
				intent.putExtra("device", neededDevice);
				Log.i("Button", "Pressed training activity button");
				startActivity(intent);
			}
		};
		View.OnClickListener onClickListenerRestartScan = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				restartScan();
				if(startTrainingButton.isEnabled()){
					startTrainingButton.setEnabled(false);
					textViewInfo.setText(R.string.settings_notification);
				}
			}
		};
		startTrainingButton.setOnClickListener(onClickListenerStartTraining);
		restartScanButton.setOnClickListener(onClickListenerRestartScan);
		AdapterView.OnItemClickListener onItemClickListenerDeviceList = new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				scanDevice(false);
				neededDevice = deviceList.get(position);
				deviceList.clear();
				deviceNameList.clear();
				updateDeviceListView();
				deviceNameList.add(neededDevice.getName());
				updateDeviceListView();
				startTrainingButton.setEnabled(true);
				textViewInfo.setText(R.string.settings_start_training);
				Log.i("ItemSelected", "Needed device selected");
			}
		};
		deviceListView.setOnItemClickListener(onItemClickListenerDeviceList);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == bluetoothRequestCode){
			if(bluetoothAdapter.isEnabled()){
				Log.i("Bluetooth", "Bluetooth on");
				scanDevice(true);
			}else{
				Log.i("Bluetooth", "Bluetooth still off");
			}
		}
	}

	private void scanDevice(boolean scanning){
		if (scanning) {
			Log.i("BluetoothScan", "Start Scanning");
			mHandler.postDelayed(stopLeScanRunnable, SCAN_PERIOD);
			bluetoothAdapter.startLeScan(leScanCallback);
		} else {
			Log.i("BluetoothScan", "Bluetooth scan stops");
			bluetoothAdapter.stopLeScan(leScanCallback);
		}
	}

	private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(!deviceList.contains(device)){
						Log.i("DeviceList", "Device add to deviceList");
						deviceList.add(device);
						deviceNameList.add(device.getName());
						updateDeviceListView();
					}
				}
			});
		}
	};

	private Runnable stopLeScanRunnable = new Runnable() {
		@Override
		public void run() {
			scanDevice(false);
		}
	};

	private void updateDeviceListView(){
		adapterList.notifyDataSetInvalidated();
	}

	private void restartScan(){
		mHandler.removeCallbacks(stopLeScanRunnable);
		scanDevice(false);
		deviceList.clear();
		deviceNameList.clear();
		updateDeviceListView();
		scanDevice(true);
	}
}
