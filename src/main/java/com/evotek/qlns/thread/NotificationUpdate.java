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
public class NotificationUpdate{

    private StartUpService startUpService;

    public StartUpService getStartUpService() {
        if (startUpService == null) {
            startUpService = (StartUpService) SpringUtil.getBean("startUpService");
            setStartUpService(startUpService);
        }

        return startUpService;
    }

    public void setStartUpService(StartUpService startUpService) {
        this.startUpService = startUpService;
    }
    
    //@Scheduled(cron="*/5 * * * * ?")
    @Scheduled(fixedDelay = 3600000)//run each 1 hour
    public void run() {

        try {
            System.out.println("NotificationUpdate thread is running");
            //set all expired notification to status 0
            int updated = startUpService.updateNotificationStatus();

            _log.info(updated + " notifications have been updated!");

            int inserted = startUpService.insertNotification();

            _log.info(inserted + " notifications have been inserted!");

        } catch (Exception ex) {
            _log.error(ex.getMessage());
        }
    }
    
    private static final Logger _log = LogManager.getLogger(NotificationUpdate.class);
}
