/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.evotek.qlns.dao.NotificationDAO;
import com.evotek.qlns.model.Notification;
import com.evotek.qlns.model.Staff;
import com.evotek.qlns.util.Validator;

/**
 *
 * @author linhlh2
 */
@Repository
@Transactional
public class NotificationDAOImpl extends AbstractDAO<Notification> implements NotificationDAO {
	private static final Logger _log = LogManager.getLogger(NotificationDAOImpl.class);

	@Override
	public int getNotificationCountByT_S(Long notificationType, Long status) {
		int result = 0;

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

			Root<Notification> root = criteria.from(Notification.class);

			criteria.select(builder.count(root));

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (Validator.isNotNull(notificationType)) {
				predicates.add(builder.equal(root.get("notificationType"), notificationType));
			}

			if (Validator.isNotNull(status)) {
				predicates.add(builder.equal(root.get("status"), status));
			}

			criteria.where(predicates.toArray(new Predicate[predicates.size()]));

			Long count = (Long) session.createQuery(criteria).uniqueResult();

			if (count != null) {
				result = count.intValue();
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return result;
	}

	@Override
	public int getNotificationCountByT_S_E(Long notificationType, Long status, boolean expired) {
		int result = 0;

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

			Root<Notification> root = criteria.from(Notification.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (Validator.isNotNull(notificationType)) {
				predicates.add(builder.equal(root.get("notificationType"), notificationType));
			}

			if (Validator.isNotNull(status)) {
				predicates.add(builder.equal(root.get("status"), status));
			}

			if (!expired) {
				predicates.add(builder.greaterThanOrEqualTo(root.get("expiredDate"), new Date()));
			} else {
				predicates.add(builder.lessThanOrEqualTo(root.get("expiredDate"), new Date()));
			}

			criteria.select(builder.count(root)).where(predicates.toArray(new Predicate[predicates.size()]));

			Long count = (Long) session.createQuery(criteria).uniqueResult();

			if (count != null) {
				result = count.intValue();
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return result;
	}

	@Override
	public List<Notification> getNotifies(Long status, boolean expired) {
		List<Notification> results = new ArrayList<Notification>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Notification> criteria = builder.createQuery(Notification.class);

			Root<Notification> root = criteria.from(Notification.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (Validator.isNotNull(status)) {
				predicates.add(builder.equal(root.get("status"), status));
			}

			Predicate expiredDateNullPre = builder.isNull(root.get("expiredDate"));

			Predicate expiredDatePre = !expired ? builder.greaterThanOrEqualTo(root.get("expiredDate"), new Date())
					: builder.lessThanOrEqualTo(root.get("expiredDate"), new Date());

			predicates.add(builder.or(expiredDateNullPre, expiredDatePre));

			criteria.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

			criteria.orderBy(builder.asc(root.get("notificationType")), builder.desc(root.get("eventDate")));

			results = session.createQuery(criteria).getResultList();
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

	@Override
	public List<Long> getStaffIdByT_S(Long notificationType, Long status) {
		List<Long> results = new ArrayList<Long>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

			Root<Notification> root = criteria.from(Notification.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (Validator.isNotNull(notificationType)) {
				predicates.add(builder.equal(root.get("notificationType"), notificationType));
			}

			if (Validator.isNotNull(status)) {
				predicates.add(builder.equal(root.get("status"), status));
			}

			predicates.add(builder.equal(root.get("className"), Staff.class.getName()));

			criteria.select(root.get("classPk")).where(predicates.toArray(new Predicate[predicates.size()]));

			results = session.createQuery(criteria).getResultList();
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

	@Override
	public List<Long> getStaffIdByT_S_E(Long notificationType, Long status, boolean expired) {
		List<Long> results = new ArrayList<Long>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

			Root<Notification> root = criteria.from(Notification.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (Validator.isNotNull(notificationType)) {
				predicates.add(builder.equal(root.get("notificationType"), notificationType));
			}

			if (Validator.isNotNull(status)) {
				predicates.add(builder.equal(root.get("status"), status));
			}

			predicates.add(builder.equal(root.get("className"), Staff.class.getName()));

			if (!expired) {
				predicates.add(builder.greaterThanOrEqualTo(root.get("expiredDate"), new Date()));
			} else {
				predicates.add(builder.lessThanOrEqualTo(root.get("expiredDate"), new Date()));
			}

			criteria.select(root.get("classPk")).where(predicates.toArray(new Predicate[predicates.size()]));

			results = session.createQuery(criteria).getResultList();

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

	@Override
	public int updateNotificationStatus() {
		int result = 0;

		try {
			StringBuilder sb = new StringBuilder();

			sb.append("update Notification set status = 0 ");
			sb.append("where expiredDate<CURRENT_TIMESTAMP()");

			Query query = getCurrentSession().createQuery(sb.toString());

			result = query.executeUpdate();
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return result;
	}
}
