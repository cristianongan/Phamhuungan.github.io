/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.evotek.qlns.dao.RoleDAO;
import com.evotek.qlns.model.Role;
import com.evotek.qlns.util.CharPool;
import com.evotek.qlns.util.QueryUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author linhlh2
 */
@Repository
public class RoleDAOImpl extends AbstractDAO<Role> implements RoleDAO {

	private static final Logger _log = LogManager.getLogger(RoleDAOImpl.class);

	@Override
	public int getCountAllRoles() throws Exception {
		int result = 0;

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

			Root<Role> root = criteria.from(Role.class);

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
	public Role getNewRole() {
		return new Role();
	}

	@Override
	public Role getRoleById(Long roleId) throws Exception {
		Role role = null;

		try {
			role = findById(Role.class, roleId);

			if (Validator.isNull(role) || !Values.STATUS_ACTIVE.equals(role.getStatus())) {
				return null;
			}
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return role;
	}

	@Override
	public Role getRoleByName(String roleName) throws Exception {
		Role result = null;

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Role> criteria = builder.createQuery(Role.class);

			Root<Role> root = criteria.from(Role.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (Validator.isNotNull(roleName)) {
				predicates.add(builder.like(builder.lower(root.get("roleName")),
						QueryUtil.getFullStringParam(roleName, true), CharPool.BACK_SLASH));
			}

			predicates.add(builder.notEqual(root.get("status"), Values.STATUS_DEACTIVE));

			criteria.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

			criteria.orderBy(builder.asc(builder.lower(root.get("roleName"))));

			List<Role> roles = session.createQuery(criteria).getResultList();

			if (!roles.isEmpty()) {
				result = roles.get(0);
			}
		} catch (Exception e) {
			_log.error(e.getMessage(), e);

		}

		return result;
	}

	@Override
	public List<Role> getRoleByRN(String roleName) throws Exception {
		List<Role> roles = new ArrayList<Role>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Role> criteria = builder.createQuery(Role.class);

			Root<Role> root = criteria.from(Role.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (Validator.isNotNull(roleName)) {
				predicates.add(builder.like(builder.lower(root.get("roleName")),
						QueryUtil.getFullStringParam(roleName, true), CharPool.BACK_SLASH));
			}

			predicates.add(builder.notEqual(root.get("status"), Values.STATUS_DEACTIVE));

			criteria.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

			criteria.orderBy(builder.asc(builder.lower(root.get("roleName"))));

			roles = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return roles;
	}

	@Override
	public List<Role> getRoleByRN(String roleName, Long roleId) throws Exception {
		List<Role> roles = new ArrayList<Role>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Role> criteria = builder.createQuery(Role.class);

			Root<Role> root = criteria.from(Role.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			predicates.add(builder.equal(root.get("roleName"), roleName));

			if (Validator.isNotNull(roleId)) {
				predicates.add(builder.notEqual(root.get("roleId"), roleId));
			}

			criteria.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

			criteria.orderBy(builder.asc(builder.lower(root.get("roleName"))));

			roles = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return roles;
	}

	@Override
	public List<Role> getRoles(boolean isAdmin) {
		List<Role> roles = new ArrayList<Role>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Role> criteria = builder.createQuery(Role.class);

			Root<Role> root = criteria.from(Role.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			predicates.add(builder.notEqual(root.get("status"), Values.STATUS_DEACTIVE));

			if (!isAdmin) {
				predicates.add(builder.equal(root.get("shareable"), Values.SHAREABLE));
			}

			criteria.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

			criteria.orderBy(builder.asc(builder.lower(root.get("roleName"))));

			roles = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return roles;
	}

	@Override
	public List<Role> getRoles(String roleName, Long status) throws Exception {
		List<Role> roles = new ArrayList<Role>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Role> criteria = builder.createQuery(Role.class);

			Root<Role> root = criteria.from(Role.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (Validator.isNotNull(roleName)) {
				predicates.add(builder.like(builder.lower(root.get("roleName")),
						QueryUtil.getFullStringParam(roleName, true), CharPool.BACK_SLASH));
			}

			if (Validator.isNotNull(status)) {
				predicates.add(builder.equal(root.get("status"), status));

			}

			criteria.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

			criteria.orderBy(builder.asc(builder.lower(root.get("roleName"))));

			roles = session.createQuery(criteria).getResultList();
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return roles;
	}

	// New
	@Override
	public List<Role> searchRole(String roleName) {
		List<Role> roles = new ArrayList<Role>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Role> criteria = builder.createQuery(Role.class);

			Root<Role> root = criteria.from(Role.class);

			criteria.select(root).where(builder.like(builder.lower(root.get("roleName")),
					QueryUtil.getFullStringParam(roleName, true), CharPool.BACK_SLASH));

			roles = session.createQuery(criteria).getResultList();
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return roles;
	}
}
