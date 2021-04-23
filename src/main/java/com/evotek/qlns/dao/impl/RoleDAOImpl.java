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
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.evotek.qlns.dao.RoleDAO;
import com.evotek.qlns.model.Role;
import com.evotek.qlns.util.LikeCriterionMaker;
import com.evotek.qlns.util.QueryUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author linhlh2
 */
public class RoleDAOImpl extends BasicDAO<Role> implements RoleDAO {

    private static final Logger _log = LogManager.getLogger(RoleDAOImpl.class);

    public Role getNewRole() {
        return new Role();
    }

    public Role getRoleById(Long roleId) throws Exception {
        Role role = null;

        try {
            role = get(Role.class, roleId);

            if (Validator.isNull(role)
                    || !QueryUtil.STATUS_ACTIVE.equals(role.getStatus())) {
                return null;
            }
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return role;
    }

    public List<Role> getRoleByRN(String roleName) throws Exception {
        List<Role> roles = new ArrayList<Role>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(Role.class);

            if (Validator.isNotNull(roleName)) {
                cri.add(LikeCriterionMaker.ilike("roleName", roleName,
                        MatchMode.ANYWHERE));
            }

            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));

            cri.addOrder(Order.asc("roleName"));

            roles = (List<Role>) cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return roles;
    }

    public List<Role> getRoleByRN(String roleName, Long roleId) throws Exception {
        List<Role> roles = new ArrayList<Role>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(Role.class);

            cri.add(Restrictions.eq("roleName", roleName));

            if(Validator.isNotNull(roleId)){
                cri.add(Restrictions.ne("roleId", roleId));
            }

            roles = (List<Role>) cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return roles;
    }

    public int getCountAllRoles() throws Exception {
        int count = 0;

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(Role.class);

            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));

            cri.setProjection(Projections.rowCount());

            count = (Integer) cri.uniqueResult();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return count;
    }

//    public List<String> getRolesNameByUser(User user) throws Exception {
//        List<String> roleNames = new ArrayList<String>();
//
//        try {
//            Session session = currentSession();
//
//            Criteria cri = session.createCriteria(Role.class);
//
//            cri.createAlias("user", "ur");
//
//            cri.setFetchMode("ur.roles", FetchMode.JOIN);
//
//            cri.setProjection(Projections.property("roleName"));
//
//            roleNames = (List<String>) cri.list();
//        } catch (Exception e) {
//            _log.error(e.getMessage(), e);
//        }
//
//        return roleNames;
//    }
//
//    public List<String> getRolesNameByUserId(Long userId) {
//        List<String> roleNames = new ArrayList<String>();
//
//        try {
//            Session session = currentSession();
//
//            Criteria cri = session.createCriteria(Role.class);
//
//            cri.createAlias("userRoles", "ur");
//
//            cri.add(Restrictions.eq("ur.user.userId", userId));
//
//            cri.setProjection(Projections.property("roleName"));
//
//            roleNames = (List<String>) cri.list();
//        } catch (Exception e) {
//            _log.error(e.getMessage(), e);
//        }
//
//        return roleNames;
//    }

    public List<Role> getRoles(boolean isAdmin) {
        List<Role> roles = new ArrayList<Role>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(Role.class);

            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));

            if(!isAdmin){
                cri.add(Restrictions.eq("shareable", Values.SHAREABLE));
            }

            cri.addOrder(Order.asc("roleName"));

            roles = (List<Role>) cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return roles;
    }

    public List<Role> getRoles(String roleName, Long status) throws Exception {
        List<Role> results = new ArrayList<Role>();

        try {
            Criteria cri = currentSession().createCriteria(Role.class);

            if(Validator.isNotNull(roleName)){
                cri.add(LikeCriterionMaker.ilike("roleName", roleName,
                        MatchMode.ANYWHERE));
            }

            if(Validator.isNotNull(status)){
                cri.add(Restrictions.eq("status", status));
            }

            cri.addOrder(Order.asc("roleName").ignoreCase());

            results = (List<Role>) cri.list();
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return results;
    }
    
    //New
    public List<Role> searchRole(String roleName)
    {
        List<Role> roles = new ArrayList<Role>();
        try {
            Session session = currentSession();
            Criteria criteria  = session.createCriteria(Role.class);
            criteria.add(LikeCriterionMaker.ilike("roleName", roleName, MatchMode.ANYWHERE));
            roles = (List<Role>) criteria.list();
        } catch (Exception ex) {
             _log.error(ex.getMessage(), ex);
        }
        finally
        {
            return roles;
        }
    }

    public Role getRoleByName(String roleName) throws Exception {
        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(Role.class);

            if (Validator.isNotNull(roleName)) {
                cri.add(LikeCriterionMaker.ilike("roleName", roleName,
                        MatchMode.ANYWHERE));
            }

            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));

            cri.addOrder(Order.asc("roleName"));

            List<Role> roles = (List<Role>) cri.list();

            if(roles.isEmpty()){
                return null;
            } else {
                return roles.get(0);
            }
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
            return null;
        }

    }
}
