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
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.evotek.qlns.dao.WorkProcessDAO;
import com.evotek.qlns.model.WorkProcess;
import com.evotek.qlns.util.Validator;

/**
 *
 * @author LinhLH
 */
@Repository
@Transactional
public class WorkProcessDAOImpl extends AbstractDAO<WorkProcess> implements WorkProcessDAO {
	private static final Logger _log = LogManager.getLogger(WorkProcessDAOImpl.class);

	@Override
	public List<String> getCompanyName() {
		List<String> results = new ArrayList<String>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<String> criteria = builder.createQuery(String.class);

			Root<WorkProcess> root = criteria.from(WorkProcess.class);

			criteria.distinct(true).select(root.get("company"));

			criteria.orderBy(builder.asc(root.get("company")));

			results = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return results;
	}

	@Override
	public List<String> getJobTitle() {
		List<String> results = new ArrayList<String>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<String> criteria = builder.createQuery(String.class);

			Root<WorkProcess> root = criteria.from(WorkProcess.class);

			criteria.distinct(true).select(root.get("jobTitle"));

			criteria.orderBy(builder.asc(root.get("jobTitle")));

			results = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return results;
	}

	@Override
	public List<WorkProcess> getWorkProcessByStaffId(Long staffId) {
		List<WorkProcess> results = new ArrayList<WorkProcess>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<WorkProcess> criteria = builder.createQuery(WorkProcess.class);

			Root<WorkProcess> root = criteria.from(WorkProcess.class);

			criteria.select(root);

			if (Validator.isNotNull(staffId)) {
				criteria.where(builder.equal(root.get("staffId"), staffId));

			}

			criteria.orderBy(builder.desc(root.get("fromDate")), builder.desc(root.get("toDate")));

			results = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return results;
	}
}
