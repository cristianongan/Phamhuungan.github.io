/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.evotek.qlns.dao.RightDAO;
import com.evotek.qlns.model.Right;
import com.evotek.qlns.model.RightView;
import com.evotek.qlns.model.User;
import com.evotek.qlns.util.CharPool;
import com.evotek.qlns.util.QueryUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author linhlh2
 */
public class RightDAOImpl extends AbstractDAO<Right> implements RightDAO {

	private static final Logger _log = LogManager.getLogger(RightDAOImpl.class);

	@Override
	public void deleteByCategoryId(Long categoryId) throws Exception {
		try {
			List<Right> rights = getRightByCategoryId(categoryId);

			deleteAll(rights);
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}
	}

	@Override
	public List<Right> getAllRights() throws Exception {
		List<Right> rights = new ArrayList<Right>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Right> criteria = builder.createQuery(Right.class);

			Root<Right> root = criteria.from(Right.class);

			criteria.select(root).where(builder.notEqual(root.get("status"), Values.STATUS_DEACTIVE));

			criteria.orderBy(builder.asc(root.get("rightName")));

			rights = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return rights;
	}

	@Override
	public List<Right> getAllRights(List<Long> types) throws Exception {
		List<Right> rights = new ArrayList<Right>();

		try {

			if (types.contains(QueryUtil.ALL)) {
				return getAllRights(QueryUtil.ALL);
			}

			if (types.size() == 1) {
				return getAllRights(types.get(QueryUtil.FIRST_INDEX));
			}

			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Right> criteria = builder.createQuery(Right.class);

			Root<Right> root = criteria.from(Right.class);

			criteria.select(root).where(root.get("type").in(types),
					builder.notEqual(root.get("status"), Values.STATUS_DEACTIVE));

			criteria.orderBy(builder.asc(root.get("rightName")));

			rights = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return rights;
	}

	@Override
	public List<Right> getAllRights(Long type) throws Exception {
		List<Right> rights = new ArrayList<Right>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Right> criteria = builder.createQuery(Right.class);

			Root<Right> root = criteria.from(Right.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (!type.equals(QueryUtil.ALL)) {
				predicates.add(builder.equal(root.get("type"), type));
			}

			predicates.add(builder.notEqual(root.get("status"), Values.STATUS_DEACTIVE));

			criteria.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

			criteria.orderBy(builder.asc(root.get("rightName")));

			rights = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return rights;
	}

	@Override
	public int getCountAllRights() throws Exception {
		int result = 0;

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

			Root<Right> root = criteria.from(Right.class);

			criteria.select(builder.count(root)).where(builder.notEqual(root.get("status"), Values.STATUS_DEACTIVE));

			Long count = (Long) session.createQuery(criteria).uniqueResult();

			if (count != null) {
				result = count.intValue();
			}
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return result;
	}

	@Override
	public Right getNewRight() {
		return new Right();
	}

	@Override
	public List<Right> getRightByCategoryId(Long categoryId) throws Exception {
		List<Right> rights = new ArrayList<Right>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Right> criteria = builder.createQuery(Right.class);

			Root<Right> root = criteria.from(Right.class);

			criteria.select(root).where(builder.equal(root.get("categoryId"), categoryId));

			rights = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return rights;
	}

	@Override
	public Right getRightByCI_RN(Long categoryId, String folderName) {
		Right right = null;

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Right> criteria = builder.createQuery(Right.class);

			Root<Right> root = criteria.from(Right.class);

			criteria.select(root).where(builder.equal(root.get("categoryId"), categoryId),
					builder.equal(root.get("rightName"), folderName));

			right = (Right) session.createQuery(criteria).uniqueResult();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return right;
	}

	@Override
	public Right getRightById(Long rightId) throws Exception {
		Right right = null;

		try {
			right = findById(Right.class, rightId);

			if (Validator.isNull(right) || !Values.STATUS_ACTIVE.equals(right.getStatus())) {
				return null;
			}
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return right;
	}

	@Override
	public List<Right> getRightByName(String rightName, Long rightId) throws Exception {
		List<Right> rights = new ArrayList<Right>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Right> criteria = builder.createQuery(Right.class);

			Root<Right> root = criteria.from(Right.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			predicates.add(builder.equal(root.get("rightName"), rightName));

			if (Validator.isNotNull(rightId)) {
				predicates.add(builder.notEqual(root.get("rightId"), rightId));
			}

			criteria.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

			criteria.orderBy(builder.asc(builder.lower(root.get("rightName"))));

			rights = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return rights;
	}

	@Override
	public List<Right> getRightsByRN(String rightName) throws Exception {
		List<Right> rights = new ArrayList<Right>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Right> criteria = builder.createQuery(Right.class);

			Root<Right> root = criteria.from(Right.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (Validator.isNotNull(rightName)) {
				predicates.add(builder.like(builder.lower(root.get("rightName")),
						QueryUtil.getFullStringParam(rightName, true), CharPool.BACK_SLASH));

			}

			predicates.add(builder.notEqual(root.get("status"), Values.STATUS_DEACTIVE));

			criteria.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

			criteria.orderBy(builder.asc(builder.lower(root.get("rightName"))));

			rights = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return rights;
	}

	@Override
	public List<Right> getRightsByRN_T(String rightName, List<Long> types) throws Exception {
		List<Right> rights = new ArrayList<Right>();

		try {
			if (types.contains(QueryUtil.ALL)) {
				return getRightsByRN(rightName);
			}

			if (types.size() == 1) {
				return getRightsByRN_T(rightName, types.get(0));
			}

			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Right> criteria = builder.createQuery(Right.class);

			Root<Right> root = criteria.from(Right.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (Validator.isNotNull(rightName)) {
				predicates.add(builder.like(builder.lower(root.get("rightName")),
						QueryUtil.getFullStringParam(rightName, true), CharPool.BACK_SLASH));

			}

			predicates.add(root.get("type").in(types));

			predicates.add(builder.notEqual(root.get("status"), Values.STATUS_DEACTIVE));

			criteria.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

			criteria.orderBy(builder.asc(builder.lower(root.get("rightName"))));

			rights = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return rights;
	}

	@Override
	public List<Right> getRightsByRN_T(String rightName, Long type) throws Exception {
		List<Right> rights = new ArrayList<Right>();

		try {
			if (type.equals(QueryUtil.ALL)) {
				return getRightsByRN(rightName);
			}

			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Right> criteria = builder.createQuery(Right.class);

			Root<Right> root = criteria.from(Right.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (Validator.isNotNull(rightName)) {
				predicates.add(builder.like(builder.lower(root.get("rightName")),
						QueryUtil.getFullStringParam(rightName, true), CharPool.BACK_SLASH));

			}

			predicates.add(builder.equal(root.get("type"), type));

			predicates.add(builder.notEqual(root.get("status"), Values.STATUS_DEACTIVE));

			criteria.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

			criteria.orderBy(builder.asc(builder.lower(root.get("rightName"))));

			rights = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return rights;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Right> getRightsByUser(User user) throws Exception {
		List<Right> rights = new ArrayList<Right>();

		try {
			Session session = getCurrentSession();

			StringBuilder sb = new StringBuilder();

			sb.append(" SELECT distinct r FROM Right r JOIN r.groupsRights ");
			sb.append(" AS gr JOIN gr.groups.roleGroups AS rg JOIN ");
			sb.append(" rg.role.userRoles AS ur JOIN ur.user AS u ");
			sb.append(" WHERE u.userId = :userId AND r.status = 1");

			Query q = session.createQuery(sb.toString());

			q.setParameter("userId", user.getUserId());

			rights = (List<Right>) q.getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return rights;
	}

	@Override
	public List<RightView> getRightViewByUserId(Long userId) throws Exception {
		List<RightView> rights = new ArrayList<RightView>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<RightView> criteria = builder.createQuery(RightView.class);

			Root<RightView> root = criteria.from(RightView.class);

			criteria.select(root).where(builder.notEqual(root.get("status"), Values.STATUS_DEACTIVE),
					builder.equal(root.get("userId"), userId));

			criteria.orderBy(builder.asc(builder.lower(root.get("rightName"))));

			rights = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return rights;
	}
}
