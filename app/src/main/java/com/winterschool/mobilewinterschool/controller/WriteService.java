package com.winterschool.mobilewinterschool.controller;

import com.winterschool.mobilewinterschool.model.TrainingData;

/**
 * Date: 02.02.17
 * Time: 14:20
 *
 * @author anatoliy
 */
public class WriteService implements Runnable {
	private TrainingData mTrainingData;

	public WriteService(TrainingData trainingData) {
		mTrainingData = trainingData;
	}

	@Override
	public void run() {

	}
}
