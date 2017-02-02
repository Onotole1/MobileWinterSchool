package com.winterschool.mobilewinterschool;

import java.util.Queue;

/**
 * Date: 02.02.17
 * Time: 14:25
 *
 * @author anatoliy
 */
public class HrQueue {
	private static HrQueue sHrQueue;
	private Queue<Double> mQueue;
	private int mQueueMaxSize;

	private HrQueue() {
	}

	public static HrQueue getInstence() {
		if (sHrQueue == null) {
			sHrQueue = new HrQueue();
		}
		return sHrQueue;
	}

	public int getQueueMaxSize() {
		return mQueueMaxSize;
	}

	public void setQueueMaxSize(int queueMaxSize) {
		mQueueMaxSize = queueMaxSize;
	}

	public Queue<Double> getQueue() {
		return mQueue;
	}

	public void setQueue(Queue<Double> queue) {
		mQueue = queue;
	}
}
