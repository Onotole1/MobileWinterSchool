package com.winterschool.mobilewinterschool.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

import android.os.Handler;

import com.winterschool.mobilewinterschool.model.TrainingData;
import com.winterschool.mobilewinterschool.controller.server.Server;

/**
 * Date: 02.02.17
 * Time: 14:20
 *
 * @author anatoliy
 */
public class WriteService implements  Runnable{
	private TrainingData mTrainingData;
	private Handler mHandler;
	private Handler mPulseHandler;
	private int mDelay = 1000;
	private boolean mSendData;

	public WriteService(TrainingData trainingData, Handler pulseHandler) {
		mPulseHandler = pulseHandler;
		mTrainingData = trainingData;
		mSendData = true;
	}

	public void stopSendData(){
		mSendData = false;
	}

	private Runnable sendToServerTask = new Runnable() {
		@Override
		public void run() {
			try {
				if (mSendData == true) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH); //DD/MM/YY HH:MM:SS
							format.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
							Server.getInstance().pulseRequest(mTrainingData.getPulse(), mTrainingData.getSessionId(),
									mTrainingData.getToken(), format.format(new Date()), mPulseHandler);
						}
					}).start();
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			mHandler.postDelayed(sendToServerTask, mDelay);
		}
	};

	@Override
	public void run() {
		mHandler = new Handler();
		mHandler.postDelayed(sendToServerTask, mDelay);
	}
}
