package com.winterschool.mobilewinterschool.controller;

import android.os.Handler;

import com.winterschool.mobilewinterschool.model.TrainingData;

/**
 * Date: 02.02.17
 * Time: 14:20
 *
 * @author anatoliy
 */
public class Core {
	private TrainingData mTrainingData;

	public Core(TrainingData trainingData) {
		mTrainingData = trainingData;
	}

	public void stopTraining() {

	}

	public void startTraining(Handler pulseHandler) {
		new WriteService(mTrainingData, pulseHandler);
	}
}
