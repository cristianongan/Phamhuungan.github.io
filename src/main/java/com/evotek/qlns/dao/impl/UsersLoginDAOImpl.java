/*
 * To change this template, choose Tools | Templates
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

import com.evotek.qlns.dao.UsersLoginDAO;
import com.evotek.qlns.model.UserLogin;
import com.evotek.qlns.util.CharPool;
import com.evotek.qlns.util.QueryUtil;

/**
 *
 * @author linhlh2
 */
public class UsersLoginDAOImpl extends AbstractDAO<UserLogin> implements UsersLoginDAO {

	private static final Logger _log = LogManager.getLogger(UsersLoginDAOImpl.class);

	@Override
	public int countByIp(String ip) {
		int result = 0;

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

			Root<UserLogin> root = criteria.from(UserLogin.class);

			criteria.select(builder.count(root)).where(builder.like(builder.lower(root.get("ip")),
					QueryUtil.getFullStringParam(ip, true), CharPool.BACK_SLASH));

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
	public void delete(List<UserLogin> userLogins) {
		deleteAll(userLogins);
	}

	@Override
	public List<UserLogin> getUsersLogin(String ip) {
		List<UserLogin> results = new ArrayList<UserLogin>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<UserLogin> criteria = builder.createQuery(UserLogin.class);

			Root<UserLogin> root = criteria.from(UserLogin.class);

			criteria.select(root).where(builder.like(builder.lower(root.get("ip")),
					QueryUtil.getFullStringParam(ip, true), CharPool.BACK_SLASH));

			results = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return results;
	}
}
