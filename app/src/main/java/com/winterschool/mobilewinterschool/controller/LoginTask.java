package com.winterschool.mobilewinterschool.controller;

/**
 * Date: 03.02.17
 * Time: 19:51
 *
 * @author anatoliy
 */
public class LoginTask implements Runnable {
	private String mLogin;
	private String mPassword;

	public LoginTask(String login, String password) {
		mLogin = login;
		mPassword = password;
	}

	@Override
	public void run() {
		//send
	}
}
