package com.winterschool.mobilewinterschool.controller;

import com.winterschool.mobilewinterschool.HrQueue;

/**
 * Date: 02.02.17
 * Time: 14:23
 *
 * @author anatoliy
 */
public class QueueService implements Runnable {
	private HrQueue mHrQueue;

	public QueueService(HrQueue hrQueue) {
		mHrQueue = hrQueue;
	}

	@Override
	public void run() {

	}

	public boolean isReadyToSend() {
		return mHrQueue.getQueueMaxSize() == mHrQueue.getQueue().size();
	}

	public void sendToServer() {}
}
