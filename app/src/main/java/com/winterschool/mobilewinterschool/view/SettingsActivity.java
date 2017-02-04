package com.winterschool.mobilewinterschool.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.winterschool.mobilewinterschool.R;

public class SettingsActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		Bundle extras = getIntent().getExtras();
		Log.d("token", extras.getString("token"));
	}
}
