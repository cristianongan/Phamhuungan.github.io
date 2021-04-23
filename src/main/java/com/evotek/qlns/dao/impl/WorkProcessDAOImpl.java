/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.evotek.qlns.dao.WorkProcessDAO;
import com.evotek.qlns.model.WorkProcess;
import com.evotek.qlns.util.Validator;

/**
 *
 * @author My PC
 */
public class WorkProcessDAOImpl extends BasicDAO<WorkProcess>
        implements WorkProcessDAO{
    private static final Logger _log = LogManager.getLogger(WorkProcessDAOImpl.class);
    
    public List<WorkProcess> getWorkProcessByStaffId(Long staffId){
        List<WorkProcess> results = new ArrayList<WorkProcess>();

        try {
            Criteria cri = currentSession().createCriteria(WorkProcess.class);

            if (Validator.isNotNull(staffId)) {
                cri.add(Restrictions.eq("staffId", staffId));
            }

            cri.addOrder(Order.desc("fromDate"));
            cri.addOrder(Order.desc("toDate"));
            
            results = (List<WorkProcess>) cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return results;
    }
    
    public List<String> getCompanyName(){
        List<String> results = new ArrayList<String>();

        try {
            Criteria cri = currentSession().createCriteria(WorkProcess.class);

            cri.setProjection(Projections.distinct(
                    Projections.property("company")));

            cri.addOrder(Order.asc("company"));
            
            results = (List<String>) cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return results;
    }
    
    public List<String> getJobTitle() {
        List<String> results = new ArrayList<String>();

        try {
            Criteria cri = currentSession().createCriteria(WorkProcess.class);

            cri.setProjection(Projections.distinct(
                    Projections.property("jobTitle")));

            cri.addOrder(Order.asc("jobTitle"));
            
            results = (List<String>) cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return results;
    }
}
