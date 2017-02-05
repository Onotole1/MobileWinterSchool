package com.winterschool.mobilewinterschool.controller;

import com.winterschool.mobilewinterschool.model.TrainingData;

/**
 * Date: 02.02.17
 * Time: 14:20
 *
 * @author anatoliy
 */
public class Core {
	private TakePhotoThread mTakePhotoThread;
	private TrainingData mTrainingData;

	public Core(TrainingData trainingData) {
		mTrainingData = trainingData;
	}

	public void takePhoto() {
		new Thread(mTakePhotoThread).start();
	}

	public void startTraining() {
		new WriteService(mTrainingData);
	}
}
