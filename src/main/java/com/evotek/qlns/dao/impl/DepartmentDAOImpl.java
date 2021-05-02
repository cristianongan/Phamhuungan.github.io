/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.evotek.qlns.dao.DepartmentDAO;
import com.evotek.qlns.model.Department;
import com.evotek.qlns.util.Validator;

/**
 *
 * @author LinhLH
 */
@Repository
@Transactional
public class DepartmentDAOImpl extends AbstractDAO<Department> implements DepartmentDAO {
	private static final Logger _log = LogManager.getLogger(DepartmentDAOImpl.class);

	@Override
	public int delete(List<Long> deptIds) {
		int result = 0;

		try {
			if (!deptIds.isEmpty()) {
				// delete Department
				String query = "delete Department where deptId in :deptIds";

				Query q = getCurrentSession().createQuery(query);

				q.setParameter("deptIds", deptIds);

				result = q.executeUpdate();
			}
		} catch (HibernateException e) {
			_log.error(e.getMessage(), e);
		}

		return result;
	}

	@Override
	public Department get(Long deptId) {
		return findById(Department.class, deptId);
	}

	@Override
	public List<Department> getDepartmentByParentId(Long parentId) {
		List<Department> results = new ArrayList<Department>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Department> criteria = builder.createQuery(Department.class);

			Root<Department> root = criteria.from(Department.class);

			Predicate pre = Validator.isNull(parentId) ? builder.isNull(root.get("parentId"))
					: builder.equal(root.get("parentId"), parentId);

			criteria.select(root).where(pre);

			criteria.orderBy(builder.asc(root.get("ordinal")));

			results = session.createQuery(criteria).getResultList();

		} catch (HibernateException e) {
			_log.error(e.getMessage(), e);
		}

		return results;
	}

	@Override
	public Long getNextOrdinal(Long parentId) {
		Long index = 0L;

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<BigDecimal> criteria = builder.createQuery(BigDecimal.class);

			Root<Department> root = criteria.from(Department.class);

			criteria.select(builder.max(root.get("ordinal")));

			Predicate pre = Validator.isNull(parentId) ? builder.isNull(root.get("parentId"))
					: builder.equal(root.get("parentId"), parentId);

			criteria.where(pre);

			BigDecimal result = (BigDecimal) session.createQuery(criteria).getSingleResult();

			if (Validator.isNotNull(result)) {
				index = (Long) result.longValue() + 1;
			}
		} catch (HibernateException e) {
			_log.error(e.getMessage(), e);
		}

		return index;
	}

	@Override
	public Long getNextOrdinalSql(Long parentId) {
		Long index = 0L;

		try {
			StringBuilder sb = new StringBuilder();

			sb.append("select max(ordinal) from Department where ");

			if (Validator.isNull(parentId)) {
				sb.append(" parentId is null ");
			} else {
				sb.append(" parentId=:parentId ");
			}

			Query q = getCurrentSession().createQuery(sb.toString());

			if (Validator.isNotNull(parentId)) {
				q.setParameter("parentId", parentId);
			}

			@SuppressWarnings("rawtypes")
			List results = q.getResultList();

			if (Validator.isNotNull(results)) {
				index = (Long) results.get(0) + 1;
			}
		} catch (HibernateException e) {
			_log.error(e.getMessage(), e);
		}

		return index;
	}

	@Override
	public void updateOrdinal(Long parentId, Long deletedIndex) {
		try {
			StringBuilder sb = new StringBuilder();

			sb.append("update Department set ordinal = ordinal - 1 where ");

			if (Validator.isNull(parentId)) {
				sb.append(" parentId is null ");
			} else {
				sb.append(" parentId=:parentId ");
			}

			sb.append(" and  ordinal>:deletedIndex");

			Query q = getCurrentSession().createQuery(sb.toString());

			if (Validator.isNotNull(parentId)) {
				q.setParameter("parentId", parentId);
			}

			q.setParameter("deletedIndex", deletedIndex);

			int result = q.executeUpdate();

			_log.info(String.format("DepartmentDAOImpl.updateOrdinal update successful %d item", result));
		} catch (HibernateException e) {
			_log.error(e.getMessage(), e);
		}
	}
}
