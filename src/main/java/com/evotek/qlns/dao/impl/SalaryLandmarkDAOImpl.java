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

import com.evotek.qlns.dao.SalaryLandmarkDAO;
import com.evotek.qlns.model.SalaryLandmark;
import com.evotek.qlns.util.Validator;

/**
 *
 * @author linhlh2
 */
@Repository
@Transactional
public class SalaryLandmarkDAOImpl extends AbstractDAO<SalaryLandmark> implements SalaryLandmarkDAO {

	private static final Logger _log = LogManager.getLogger(SalaryLandmarkDAOImpl.class);

	@Override
	public List<SalaryLandmark> getSalaryLandmarkByStaffId(Long staffId) {
		List<SalaryLandmark> results = new ArrayList<SalaryLandmark>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<SalaryLandmark> criteria = builder.createQuery(SalaryLandmark.class);

			Root<SalaryLandmark> root = criteria.from(SalaryLandmark.class);

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
