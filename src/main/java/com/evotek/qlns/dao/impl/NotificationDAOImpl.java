/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.evotek.qlns.dao.NotificationDAO;
import com.evotek.qlns.model.Notification;
import com.evotek.qlns.model.Staff;
import com.evotek.qlns.util.Validator;

/**
 *
 * @author linhlh2
 */
public class NotificationDAOImpl extends AbstractDAO<Notification>
        implements NotificationDAO{
    @Override
	public int getNotificationCountByT_S(Long notificationType, Long status){
        int result = 0;

        try {
            Criteria cri = currentSession().createCriteria(Notification.class);
            
            if(Validator.isNotNull(notificationType)){
                cri.add(Restrictions.eq("notificationType", notificationType));
            }
            
            if(Validator.isNotNull(status)){
               cri.add(Restrictions.eq("status", status)); 
            }
            
            cri.setProjection(Projections.rowCount());

            result = (Integer) cri.uniqueResult();
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return result;
    }
    
    @Override
	public int getNotificationCountByT_S_E(Long notificationType, Long status, 
            boolean expired){
        int result = 0;

        try {
            Criteria cri = currentSession().createCriteria(Notification.class);
            
            if(Validator.isNotNull(notificationType)){
                cri.add(Restrictions.eq("notificationType", notificationType));
            }
            
            if(Validator.isNotNull(status)){
               cri.add(Restrictions.eq("status", status)); 
            }
            
            if(!expired){
                cri.add(Restrictions.ge("expiredDate", new Date())); 
            } else {
                cri.add(Restrictions.lt("expiredDate", new Date())); 
            }
            
            cri.setProjection(Projections.rowCount());

            result = (Integer) cri.uniqueResult();
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return result;
    }
    
    @Override
	public List<Long> getStaffIdByT_S(Long notificationType, Long status){
        List<Long> results = new ArrayList<Long>();

        try {
            Criteria cri = currentSession().createCriteria(Notification.class);
            
            if(Validator.isNotNull(notificationType)){
                cri.add(Restrictions.eq("notificationType", notificationType));
            }
            
            if(Validator.isNotNull(status)){
               cri.add(Restrictions.eq("status", status)); 
            }
            
            cri.add(Restrictions.eq("className", Staff.class.getName()));
            
            cri.setProjection(Projections.property("classPk"));

            results =  cri.list();
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return results;
    }
    
    @Override
	public List<Long> getStaffIdByT_S_E(Long notificationType, Long status, 
            boolean expired){
        List<Long> results = new ArrayList<Long>();

        try {
            Criteria cri = currentSession().createCriteria(Notification.class);
            
            if(Validator.isNotNull(notificationType)){
                cri.add(Restrictions.eq("notificationType", notificationType));
            }
            
            if(Validator.isNotNull(status)){
               cri.add(Restrictions.eq("status", status)); 
            }
            
            cri.add(Restrictions.eq("className", Staff.class.getName()));
            
            if(!expired){
                cri.add(Restrictions.ge("expiredDate", new Date())); 
            } else {
                cri.add(Restrictions.lt("expiredDate", new Date())); 
            }
            
            cri.setProjection(Projections.property("classPk"));

            results =  cri.list();
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return results;
    }
    
    @Override
	public int updateNotificationStatus(){
        int result = 0;
        
        try {
            StringBuilder sb = new StringBuilder();
            
            sb.append("update Notification set status = 0 ");
            sb.append("where expiredDate<CURRENT_TIMESTAMP()");
            
            Query query = currentSession().createQuery(sb.toString());
            
            result = query.executeUpdate();
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
        
        return result;
    }
    
    @Override
	public List<Notification> getNotifies(Long status, boolean expired){
        List<Notification> results = new ArrayList<Notification>();
        
        try {
            Criteria cri = currentSession().createCriteria(Notification.class);
            
            if(Validator.isNotNull(status)){
               cri.add(Restrictions.eq("status", status)); 
            }
            
            Disjunction disjunction = Restrictions.disjunction();
            
            disjunction.add(Restrictions.isNull("expiredDate"));
            
            if(!expired){
                disjunction.add(Restrictions.ge("expiredDate", new Date())); 
            } else {
                disjunction.add(Restrictions.lt("expiredDate", new Date())); 
            }
            
            cri.add(disjunction);
            
            cri.addOrder(Order.asc("notificationType"));
            cri.addOrder(Order.desc("eventDate"));
            
            results = cri.list();
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
        
        return results;
    }
    
    private static final Logger _log = LogManager.getLogger(NotificationDAOImpl.class);
}
