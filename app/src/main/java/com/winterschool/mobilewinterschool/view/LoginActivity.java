package com.winterschool.mobilewinterschool.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.winterschool.mobilewinterschool.R;
import com.winterschool.mobilewinterschool.controller.server.Server;
import com.winterschool.mobilewinterschool.view.SettingsActivity;

public class LoginActivity extends AppCompatActivity  {
    private Button mLoginButton;
    private ImageButton mLoginDeleteButton;
    private ImageButton mPasswordDeleteButton;
    private EditText mLoginEditText;
    private EditText mPasswordEditText;
    private Context context = this;
    private String mLogin;
    private String mPassword;
    private  final static String APP_PREFERENCES = "MobileWinterSchool";
    private  final static String LOGIN_PREFERENCES = "login";
    private  final static String PASSWORD_PREFERENCES = "password";
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
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
    //public static native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public static void main(String[] args) {
        //new LoginActivity();
        //   System.out.println(stringFromJNI());
    }

    private void bindViews() {
        mLoginButton = (Button)findViewById(R.id.activity_login_button);
        mLoginDeleteButton = (ImageButton)findViewById(R.id.activivty_login_image_button_delete_login);
        mPasswordDeleteButton = (ImageButton)findViewById(R.id.activivty_login_image_button_delete_pass);
        mLoginEditText = (EditText)findViewById(R.id.activity_login_edittext_login);
        mPasswordEditText = (EditText)findViewById(R.id.activity_login_edittext_password);
        mLoginEditText.setText( preferences.getString(LOGIN_PREFERENCES, ""));
        mPasswordEditText.setText( preferences.getString(LOGIN_PREFERENCES, ""));
    }

    private void setClickListeners() {
        View.OnClickListener onClickListenerLogin = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mLogin = mLoginEditText.getText().toString();
                        mPassword = mPasswordEditText.getText().toString();
                        Server.getInstance().authRequest(mLogin, mPassword, loginHandler);
                    }
                }).start();
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
        mLoginDeleteButton.setOnClickListener(onClickListenerLoginDelete);
        mPasswordDeleteButton.setOnClickListener(onClickListenerPasswordDelete);
    }

    Handler loginHandler = new Handler() {
        public void handleMessage(Message message) {
            /*token = (String) message.obj;
            if (message.arg1 == Server.ERR_CONNECTION)
                createToast("Нет связи с сервером");
            else if (message.arg1 == Server.ERR_LOGIN) {
                createToast("Неправильный логин / пароль");
            } else if (message.arg1 == Server.ACK_LOGIN){*/
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(context, SettingsActivity.class);
                    intent.putExtra("token", "abcdef123");
                    context.startActivity(intent);
                }
            });
        }
    };
    // };

    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LOGIN_PREFERENCES, mLogin);
        editor.putString(PASSWORD_PREFERENCES, mPassword);
        editor.apply();
        super.onPause();
    }

    public void createToast(final String toastMessage) {
        runOnUiThread(new Runnable() {
            public void run() {
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, toastMessage, duration);
                toast.show();
            }
        });
    }
}
