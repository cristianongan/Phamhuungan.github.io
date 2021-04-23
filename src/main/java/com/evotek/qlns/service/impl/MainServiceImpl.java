/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evotek.qlns.dao.CategoryDAO;
import com.evotek.qlns.dao.NotificationDAO;
import com.evotek.qlns.model.Category;
import com.evotek.qlns.model.User;
import com.evotek.qlns.service.MainService;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author linhlh2
 */
public class MainServiceImpl implements MainService{

    private transient CategoryDAO categoryDAO;
    private transient NotificationDAO notificationDAO;
    
    public CategoryDAO getCategoryDAO() {
        return categoryDAO;
    }

    public void setCategoryDAO(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public NotificationDAO getNotificationDAO() {
        return notificationDAO;
    }

    public void setNotificationDAO(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }
    
    public List<Category> getAllCategory() throws Exception {
        return categoryDAO.getAllCategory();
    }

    public List<Category> getCategoryByUser(User user) throws Exception{
        return categoryDAO.getCategoryByUser(user);
    }

    public Category getCategoryById(Long categoryId) throws Exception{
        return categoryDAO.getCategoryById(categoryId);
    }

    
    public int getContractExpiredCount(){
        return notificationDAO.getNotificationCountByT_S(Values.NOTI_CONTRACT_EXPIRED, 
                Values.STATUS_ACTIVE);
    }

    public int getBirthDayCount(){
        return notificationDAO.getNotificationCountByT_S_E(Values.NOTI_BIRTHDAY, 
                Values.STATUS_ACTIVE, false);
    }
    
    

    private static final Logger _log =
            LogManager.getLogger(MainServiceImpl.class);
}
