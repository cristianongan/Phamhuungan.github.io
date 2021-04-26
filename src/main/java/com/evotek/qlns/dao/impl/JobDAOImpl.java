/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.evotek.qlns.dao.JobDAO;
import com.evotek.qlns.model.Job;

/**
 *
 * @author My PC
 */
public class JobDAOImpl extends AbstractDAO<Job> implements JobDAO {
	@Override
	public List<Job> getJobTitle() {
		List<Job> results = new ArrayList<Job>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Job> criteria = builder.createQuery(Job.class);

			Root<Job> root = criteria.from(Job.class);

			criteria.select(root);

			criteria.orderBy(builder.asc(root.get("jobTitle")));

			results = session.createQuery(criteria).getResultList();

		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return results;
	}

	@Override
	public List<String> getJobTitleOnly() {
		List<String> results = new ArrayList<String>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<String> criteria = builder.createQuery(String.class);

			Root<Job> root = criteria.from(Job.class);
			
			criteria.distinct(true).select(root.get("jobTitle"));
			
			criteria.orderBy(builder.asc(root.get("jobTitle")));
			
			results = session.createQuery(criteria).getResultList();

		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return results;
	}

	private static final Logger _log = LogManager.getLogger(JobDAOImpl.class);
}
