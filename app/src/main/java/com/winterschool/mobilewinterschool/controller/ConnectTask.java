package com.winterschool.mobilewinterschool.controller;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.winterschool.mobilewinterschool.view.TrainingActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by vlad- on 05.02.2017.
 */

public class ConnectTask implements Runnable {

    private BluetoothDevice mDevice;
    private BluetoothGatt mGatt;
    private BluetoothGattCharacteristic mCharacteristic;
    private BluetoothGattDescriptor mDescriptor;
    private TrainingActivity mTrainingActivity;

    private static final String HRUUID = "0000180d-0000-1000-8000-00805f9b34fb";

    public ConnectTask(TrainingActivity trainingActivity, BluetoothDevice device) {
        this.mTrainingActivity = trainingActivity;
        this.mDevice = device;
    }

    @Override
    public void run() {
        mGatt = mDevice.connectGatt(mTrainingActivity, false, mBluetoothGattCallback);
    }

    private void disconnect(){
        mGatt.setCharacteristicNotification(mCharacteristic, false);
        mDescriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        mGatt.writeDescriptor(mDescriptor);
        mGatt.disconnect();
        mGatt.close();
        Log.i("GattService", "Device disconnected finally");
    }

    private BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

            byte[] data = characteristic.getValue();
            int heartRate = data[1] & 0xFF;

            Log.i("GattService", "Data receive from device: " + heartRate);
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                Log.i("GattService", "Device disconnected");
            } else {
                gatt.discoverServices();
                Log.i("GattService", "Connected and discover service");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            BluetoothGattService mService = gatt.getService(UUID.fromString(HRUUID));

            List<BluetoothGattCharacteristic> characteristicList = mService.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : characteristicList) {
                List<BluetoothGattDescriptor> descriptorList = characteristic.getDescriptors();
                for (BluetoothGattDescriptor descriptor : descriptorList) {
                    mCharacteristic = characteristic;
                    mDescriptor = descriptor;
                    gatt.setCharacteristicNotification(mCharacteristic, true);
                    mDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    gatt.writeDescriptor(mDescriptor);
                }
            }
            Log.i("GattService", "Connected to device");
        }
    };
}
