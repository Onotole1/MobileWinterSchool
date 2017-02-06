package com.winterschool.mobilewinterschool.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.winterschool.mobilewinterschool.R;

public class SettingsActivity extends AppCompatActivity {

	private Button mSettingsButton;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		mContext = this.getApplicationContext();

		Bundle extras = getIntent().getExtras();
		final String token = extras.getString("token");
		Log.d("token", token);


		mSettingsButton = (Button)findViewById(R.id.activity_settings_button);
		mSettingsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, TrainingActivity.class);
				intent.putExtra("token", token);
				startActivity(intent);
			}
		});
	}


}
