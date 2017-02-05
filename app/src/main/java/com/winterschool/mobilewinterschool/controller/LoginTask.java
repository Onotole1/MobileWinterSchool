package com.winterschool.mobilewinterschool.controller;

import android.os.Handler;
import android.os.Message;

/**
 * Date: 03.02.17
 * Time: 19:51
 *
 * @author anatoliy
 */
public class LoginTask implements Runnable {
	private String mLogin;
	private String mPassword;
	private Handler.Callback mCallback;

	public LoginTask(String login, String password, Handler.Callback callback) {
		mLogin = login;
		mPassword = password;
		mCallback = callback;
	}

	@Override
	public void run() {
		//send
		//Server.login(mLogin, mPassword, loginCallback);
		Message message = new Message();
		message.obj = "toooken";
		message.arg1 = 1;
		mCallback.handleMessage(message);
	}
}
