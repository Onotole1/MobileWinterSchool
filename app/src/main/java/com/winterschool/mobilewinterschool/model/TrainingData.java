package com.winterschool.mobilewinterschool.model;

import java.util.ResourceBundle;

/**
 * Date: 02.02.17
 * Time: 16:35
 *
 * @author anatoliy
 */
public class TrainingData {
	private Integer mPulse;
	private ResourceBundle mPhoto;
	private String mToken;
	private int mSessionId;
	private boolean mIsPolarConnected;

	public TrainingData(String token) {
		mToken = token;
		mPulse = 0;
		mSessionId = 0;
		mIsPolarConnected = false;
	}

	public Integer getPulse() {
		return mPulse;
	}

	public void setPulse(Integer pulse) {
		mPulse = pulse;
	}

	public ResourceBundle getPhoto() {
		return mPhoto;
	}

	public void setPhoto(ResourceBundle photo) {
		mPhoto = photo;
	}

	public String getToken() {
		return mToken;
	}

	public void setToken(String token) {
		mToken = token;
	}

	public int getSessionId() {
		return mSessionId;
	}

	public void setSessionId(int sessionId) {
		mSessionId = sessionId;
	}

	public boolean isPolarConnected() {
		return mIsPolarConnected;
	}

	public void setPolarConnected(boolean polarConnected) {
		mIsPolarConnected = polarConnected;
	}
}
