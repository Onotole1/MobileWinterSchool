package com.winterschool.mobilewinterschool.controller;

import com.winterschool.mobilewinterschool.model.TrainingData;

/**
 * Date: 02.02.17
 * Time: 14:20
 *
 * @author anatoliy
 */
public class Core {
	private static Core sCore;
	private WriteService mWriteService;
	private TakePhotoThread mTakePhotoThread;
	private TrainingData mTrainingData;

	private Core(TrainingData trainingData) {
		mTrainingData = trainingData;
	}

	public Core getInstance(TrainingData trainingData) {
		if (sCore == null) {
			sCore = new Core(trainingData);
		}
		return sCore;
	}

	public void takePhoto() {
		Thread takePhotoThread = new Thread(mTakePhotoThread);
		takePhotoThread.start();
	}
}
