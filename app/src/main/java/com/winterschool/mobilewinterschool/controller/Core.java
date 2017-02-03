package com.winterschool.mobilewinterschool.controller;

import com.winterschool.mobilewinterschool.model.TrainingData;

/**
 * Date: 02.02.17
 * Time: 14:20
 *
 * @author anatoliy
 */
public class Core {
	private WriteService mWriteService;
	private TakePhotoThread mTakePhotoThread;
	private TrainingData mTrainingData;

	public Core(TrainingData trainingData) {
		mTrainingData = trainingData;
	}

	public void takePhoto() {
		Thread takePhotoThread = new Thread(mTakePhotoThread);
		takePhotoThread.start();
	}
}
