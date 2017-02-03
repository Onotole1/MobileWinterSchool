package com.winterschool.mobilewinterschool.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.winterschool.mobilewinterschool.R;
import com.winterschool.mobilewinterschool.controller.Core;
import com.winterschool.mobilewinterschool.model.TrainingData;

public class TrainingActivity extends AppCompatActivity {
	private Core mCore;
	private TrainingData mTrainingData;

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
	}

	private String getToken() {
		Bundle extras = getIntent().getExtras();
		return extras.getString(Intent.EXTRA_TEXT);
	}
}
