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
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import com.evotek.qlns.dao.JobDAO;
import com.evotek.qlns.model.Job;

/**
 *
 * @author My PC
 */
public class JobDAOImpl extends AbstractDAO<Job> implements 
        JobDAO{
    @Override
	public List<Job> getJobTitle(){
        List<Job> results = new ArrayList<Job>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(Job.class);

            cri.addOrder(Order.asc("jobTitle"));

            results = cri.list();

        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return results;
    }
    
    @Override
	public List<String> getJobTitleOnly() {
        List<String> results = new ArrayList<String>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(Job.class);

            cri.setProjection(Projections.distinct(
                    Projections.property("jobTitle")));
            
            cri.addOrder(Order.asc("jobTitle"));

            results = cri.list();

        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return results;
    }
    
    private static final Logger _log = LogManager.getLogger(JobDAOImpl.class);
}
