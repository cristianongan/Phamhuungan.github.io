/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.zkoss.spring.SpringUtil;

import com.evotek.qlns.service.StartUpService;

/**
 *
 * @author linhlh2
 */
public class NotificationUpdate {

	private static final Logger _log = LogManager.getLogger(NotificationUpdate.class);

	private StartUpService startUpService;

	public StartUpService getStartUpService() {
		if (this.startUpService == null) {
			this.startUpService = (StartUpService) SpringUtil.getBean("startUpService");
			setStartUpService(this.startUpService);
		}

		return this.startUpService;
	}

	// @Scheduled(cron="*/5 * * * * ?")
	@Scheduled(fixedDelay = 3600000) // run each 1 hour
	public void run() {

		try {
			System.out.println("NotificationUpdate thread is running");
			// set all expired notification to status 0
			int updated = this.startUpService.updateNotificationStatus();

			_log.info(updated + " notifications have been updated!");

			int inserted = this.startUpService.insertNotification();

			_log.info(inserted + " notifications have been inserted!");

		} catch (Exception ex) {
			_log.error(ex.getMessage());
		}
	}

	public void setStartUpService(StartUpService startUpService) {
		this.startUpService = startUpService;
	}
}
