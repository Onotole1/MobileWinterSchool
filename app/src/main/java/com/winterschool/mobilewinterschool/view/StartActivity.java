package com.winterschool.mobilewinterschool.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

import com.winterschool.mobilewinterschool.R;

/**
 * Created by vlad- on 06.02.2017.
 */

public class StartActivity extends Activity {

    private Handler mHandler;
    private static final int STOP_DELAY = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopActivity();
            }
        }, STOP_DELAY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        stopActivity();
        return super.onTouchEvent(event);
    }

    private void stopActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
