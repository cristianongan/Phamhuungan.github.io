/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.schedule.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.evotek.qlns.schedule.Worker;
import com.evotek.qlns.service.StartUpService;

/**
 *
 * @author linhlh2
 */
@EnableAsync
@Component
public class NotificationUpdater implements Worker{

	private static final Logger _log = LogManager.getLogger(NotificationUpdater.class);

	@Autowired
	private StartUpService startUpService;

	@Override
    @Scheduled(cron = "${cron.notification.updater}")
	@Async("qlnsScheduleExecutor")
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
}
