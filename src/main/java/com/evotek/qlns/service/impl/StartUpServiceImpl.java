/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evotek.qlns.dao.NotificationDAO;
import com.evotek.qlns.dao.StaffDAO;
import com.evotek.qlns.model.Notification;
import com.evotek.qlns.model.Staff;
import com.evotek.qlns.service.StartUpService;
import com.evotek.qlns.util.DateUtil;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author linhlh2
 */
@Service
@Transactional
public class StartUpServiceImpl implements StartUpService {

	@Autowired
	private NotificationDAO notificationDAO;

	@Autowired
	private StaffDAO staffDAO;

	@Override
	public void expired(Notification notify) {
		notify.setStatus(Values.STATUS_DEACTIVE);

		this.notificationDAO.saveOrUpdate(notify);
	}

	@Override
	public List<Notification> getNotifies() {
		return this.notificationDAO.getNotifies(Values.STATUS_ACTIVE, false);
	}

	@Override
	public int insertBirthDayNotification() {
		Calendar cal = Calendar.getInstance();

		int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
		int year = cal.get(Calendar.YEAR);

		if ((year % 400 == 0) || (year % 4 == 0)) {
			dayOfYear--;
		}

		List<Staff> staffs = this.staffDAO.getBirthDayNearlyStaff(dayOfYear);

		List<Notification> notifives = new ArrayList<Notification>();

		List<Long> notiStaffIds = this.notificationDAO.getStaffIdByT_S_E(Values.NOTI_BIRTHDAY, null, false);

		for (Staff staff : staffs) {
			if (!notiStaffIds.contains(staff.getStaffId())) {
				Notification notify = new Notification();

				notify.setCreateDate(new Date());
				notify.setEventDate(DateUtil.getDateAfter(DateUtil.getNextBirthDay(staff.getDateOfBirth()), 0));
				notify.setExpiredDate(DateUtil.getDateAfter(DateUtil.getNextBirthDay(staff.getDateOfBirth()), 1));
				notify.setNotificationType(Values.NOTI_BIRTHDAY);
				notify.setStatus(Values.STATUS_ACTIVE);
				notify.setMessage(staff.getStaffName());
				notify.setClassName(Staff.class.getName());
				notify.setClassPk(staff.getStaffId());

				notifives.add(notify);
			}
		}

		this.notificationDAO.saveOrUpdateAll(notifives);

		return notifives.size();
	}

	@Override
	public int insertCtExpiredNotification() {
		List<Staff> staffs = this.staffDAO.getContractExpiredStaff();

		List<Long> notiStaffIds = this.notificationDAO.getStaffIdByT_S(Values.NOTI_CONTRACT_EXPIRED, Values.STATUS_ACTIVE);

		List<Notification> notifives = new ArrayList<Notification>();

		for (Staff staff : staffs) {
			if (!notiStaffIds.contains(staff.getStaffId())) {
				Notification notify = new Notification();

				notify.setCreateDate(new Date());
				notify.setEventDate(staff.getContractToDate());
				notify.setNotificationType(Values.NOTI_CONTRACT_EXPIRED);
				notify.setStatus(Values.STATUS_ACTIVE);
				notify.setMessage(staff.getStaffName());
				notify.setClassName(Staff.class.getName());
				notify.setClassPk(staff.getStaffId());

				notifives.add(notify);
			}

		}

		this.notificationDAO.saveOrUpdateAll(notifives);

		return notifives.size();
	}

	@Override
	public int insertNotification() {
		return insertCtExpiredNotification() + insertBirthDayNotification();
	}

	@Override
	public int updateNotificationStatus() {
		return this.notificationDAO.updateNotificationStatus();
	}
}
