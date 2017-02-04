package com.winterschool.mobilewinterschool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.winterschool.mobilewinterschool.controller.LoginTask;
import com.winterschool.mobilewinterschool.view.SettingsActivity;

public class LoginActivity extends AppCompatActivity  {
    private Button mLoginButton;
    private ImageButton mLoginDeleteButtton;
    private ImageButton mPasswordDeleteButton;
    private EditText mLoginEditText;
    private EditText mPasswordEditText;
    private Context context = this;
    private String token;

    private LoginTask mLoginTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindViews();
        setClickListeners();

    // Example of a call to a native method
    /*TextView tv = (TextView) findViewById(R.id.sample_text);
    tv.setText(stringFromJNI());*/
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private void bindViews() {
        mLoginButton = (Button)findViewById(R.id.activity_login_button);
        mLoginDeleteButtton = (ImageButton)findViewById(R.id.activivty_login_image_button_delete_login);
        mPasswordDeleteButton = (ImageButton)findViewById(R.id.activivty_login_image_button_delete_pass);
        mLoginEditText = (EditText)findViewById(R.id.activity_login_edittext_login);
        mPasswordEditText = (EditText)findViewById(R.id.activity_login_edittext_password);
    }

    private void setClickListeners() {
        View.OnClickListener onClickListenerLogin = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginTask = new LoginTask(mLoginEditText.getText().toString()
                        , mPasswordEditText.getText().toString(), loginCallback);
                Thread thread = new Thread(mLoginTask);
                thread.start();
            }
        };
        View.OnClickListener onClickListenerLoginDelete = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginEditText.setText("");
            }
        };
        View.OnClickListener onClickListenerPasswordDelete = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPasswordEditText.setText("");
            }
        };
        mLoginButton.setOnClickListener(onClickListenerLogin);
        mLoginDeleteButtton.setOnClickListener(onClickListenerLoginDelete);
        mPasswordDeleteButton.setOnClickListener(onClickListenerPasswordDelete);
    }

    Handler.Callback loginCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            try {
                token = (String) message.obj;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, SettingsActivity.class);
                        intent.putExtra("token", token);
                        context.startActivity(intent);
                    }
                });
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return false;
        }
    };
}
