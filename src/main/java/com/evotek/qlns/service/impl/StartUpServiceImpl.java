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
public class StartUpServiceImpl implements StartUpService{
    private transient NotificationDAO notificationDAO;
    private transient StaffDAO staffDAO;

    public NotificationDAO getNotificationDAO() {
        return notificationDAO;
    }

    public void setNotificationDAO(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    public StaffDAO getStaffDAO() {
        return staffDAO;
    }

    public void setStaffDAO(StaffDAO staffDAO) {
        this.staffDAO = staffDAO;
    }
    
    public int updateNotificationStatus(){
        return notificationDAO.updateNotificationStatus();
    }
    
    public int insertNotification(){
        return insertCtExpiredNotification() + insertBirthDayNotification();
    }
    
    public int insertCtExpiredNotification() {
        List<Staff> staffs = staffDAO.getContractExpiredStaff();
        
        List<Long> notiStaffIds = notificationDAO.getStaffIdByT_S(
                Values.NOTI_CONTRACT_EXPIRED, Values.STATUS_ACTIVE);
        
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
        
        notificationDAO.saveOrUpdateAll(notifives);
        
        return notifives.size();
    }
    
    public int insertBirthDayNotification() {
        Calendar cal = Calendar.getInstance();
        
        int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        int year = cal.get(Calendar.YEAR);
        
        if((year%400==0)||(year%4==0)) {
            dayOfYear--;
        }
        
        List<Staff> staffs = staffDAO.getBirthDayNearlyStaff(dayOfYear);
        
        List<Notification> notifives = new ArrayList<Notification>();
        
        List<Long> notiStaffIds = notificationDAO.getStaffIdByT_S_E(
                Values.NOTI_BIRTHDAY, null, false);
        
        for (Staff staff : staffs) {
            if (!notiStaffIds.contains(staff.getStaffId())) {
                Notification notify = new Notification();

                notify.setCreateDate(new Date());
                notify.setEventDate(DateUtil.getDateAfter(
                        DateUtil.getNextBirthDay(staff.getDateOfBirth()), 0));
                notify.setExpiredDate(DateUtil.getDateAfter(
                        DateUtil.getNextBirthDay(staff.getDateOfBirth()), 1));
                notify.setNotificationType(Values.NOTI_BIRTHDAY);
                notify.setStatus(Values.STATUS_ACTIVE);
                notify.setMessage(staff.getStaffName());
                notify.setClassName(Staff.class.getName());
                notify.setClassPk(staff.getStaffId());

                notifives.add(notify);
            }
        }
        
        notificationDAO.saveOrUpdateAll(notifives);
        
        return notifives.size();
    }
    
    public List<Notification> getNotifies(){
        return notificationDAO.getNotifies(Values.STATUS_ACTIVE, false);
    }
    
    public void expired(Notification notify){
        notify.setStatus(Values.STATUS_DEACTIVE);
        
        notificationDAO.saveOrUpdate(notify);
    }
}
