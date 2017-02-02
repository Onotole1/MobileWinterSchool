package com.winterschool.mobilewinterschool.controller;

/**
 * Date: 02.02.17
 * Time: 14:20
 *
 * @author anatoliy
 */
public class Core {
	private static Core sCore;
	private QueueService mQueueService;
	private ReadService mReadService;
	private WriteService mWriteService;

	private Core() {
	}

	public Core getInstance() {
		if (sCore == null) {
			sCore = new Core();
		}
		return sCore;
	}


}
