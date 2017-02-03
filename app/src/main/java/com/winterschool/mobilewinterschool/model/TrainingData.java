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
}
