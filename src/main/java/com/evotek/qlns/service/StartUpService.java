/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.service;

import java.util.List;

import com.evotek.qlns.model.Notification;

/**
 *
 * @author linhlh2
 */
public interface StartUpService {

	public void expired(Notification notify);

	public List<Notification> getNotifies();

	public int insertBirthDayNotification();

	public int insertCtExpiredNotification();

	public int insertNotification();

	public int updateNotificationStatus();

}
