package com.winterschool.mobilewinterschool.controller;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.SimpleTimeZone;

import android.os.Handler;

import com.winterschool.mobilewinterschool.model.TrainingData;

/**
 * Date: 02.02.17
 * Time: 14:20
 *
 * @author anatoliy
 */
public class WriteService {
	private TrainingData mTrainingData;
	private Handler mHandler;
	private int mDelay = 1000;

	public WriteService(TrainingData trainingData) {
		mTrainingData = trainingData;
		mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH); //DD/MM/YY HH:MM:SS
				format.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
				try {
					//Тут вызывается сервер. Возможно, пустое поле int mTrainingData.mPulse
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				//Do something after 1000ms
			}
		}, mDelay);
	}
}
