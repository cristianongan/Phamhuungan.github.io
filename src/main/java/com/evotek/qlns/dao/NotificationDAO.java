/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao;

import java.util.Collection;
import java.util.List;

import com.evotek.qlns.model.Notification;

/**
 *
 * @author linhlh2
 */
public interface NotificationDAO {

	public int getNotificationCountByT_S(Long notificationType, Long status);

	public int getNotificationCountByT_S_E(Long notificationType, Long status, boolean expired);

	public List<Notification> getNotifies(Long status, boolean expired);

	public List<Long> getStaffIdByT_S(Long notificationType, Long status);

	public List<Long> getStaffIdByT_S_E(Long notificationType, Long status, boolean expired);

	public void saveOrUpdate(Notification notify);

	public void saveOrUpdateAll(Collection<Notification> notifives);

	public int updateNotificationStatus();
}
