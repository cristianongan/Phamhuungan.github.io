/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import com.evotek.qlns.dao.UsersLoginDAO;
import com.evotek.qlns.model.UserLogin;
import com.evotek.qlns.util.LikeCriterionMaker;

/**
 *
 * @author linhlh2
 */
public class UsersLoginDAOImpl extends AbstractDAO<UserLogin>
        implements UsersLoginDAO {

    @Override
	public int countByIp(String ip) {
        int count = 0;

        try {
            Criteria cri = currentSession().createCriteria(UserLogin.class);

            cri.add(LikeCriterionMaker.ilike("ip", ip, MatchMode.EXACT));

            cri.setProjection(Projections.rowCount());

            count = (Integer) cri.uniqueResult();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return count;
    }

    @Override
	public List<UserLogin> getUsersLogin(String ip){
        List<UserLogin> results = new ArrayList<UserLogin>();

        try {
            Criteria cri = currentSession().createCriteria(UserLogin.class);

            cri.add(LikeCriterionMaker.ilike("ip", ip, MatchMode.EXACT));

            results = cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return results;
    }

    @Override
	public void delete(List<UserLogin> userLogins){
        deleteAll(userLogins);
    }

    private static final Logger _log =
            LogManager.getLogger(UsersLoginDAOImpl.class);
}
