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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.evotek.qlns.dao.RightDAO;
import com.evotek.qlns.model.Right;
import com.evotek.qlns.model.RightView;
import com.evotek.qlns.model.User;
import com.evotek.qlns.util.LikeCriterionMaker;
import com.evotek.qlns.util.QueryUtil;
import com.evotek.qlns.util.Validator;

/**
 *
 * @author linhlh2
 */
public class RightDAOImpl extends BasicDAO<Right> implements RightDAO{

    public List<Right> getRightsByUser(User user) throws Exception {
        List<Right> rights = new ArrayList<Right>();

        try {
            Session session = currentSession();

            StringBuilder sb = new StringBuilder();

            sb.append(" SELECT distinct r FROM Right r JOIN r.groupsRights ");
            sb.append(" AS gr JOIN gr.groups.roleGroups AS rg JOIN ");
            sb.append(" rg.role.userRoles AS ur JOIN ur.user AS u ");
            sb.append(" WHERE u.userId = :userId AND r.status = 1");

            Query q = session.createQuery(sb.toString());

            q.setParameter("userId", user.getUserId());

            rights = (List<Right>) q.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return rights;
    }

    public List<RightView> getRightViewByUserId(Long userId) throws Exception {
        List<RightView> rights = new ArrayList<RightView>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(RightView.class);

            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));
            cri.add(Restrictions.eq("userId", userId));

            cri.addOrder(Order.asc("rightName"));

            rights = (List<RightView>) cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return rights;
    }

    public Right getNewRight() {
        return new Right();
    }

    public List<Right> getAllRights() throws Exception {
        List<Right> rights = new ArrayList<Right>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(Right.class);

            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));
            
            cri.addOrder(Order.asc("rightName"));

            rights = (List<Right>) cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return rights;
    }

    public List<Right> getAllRights(Long type) throws Exception {
        List<Right> rights = new ArrayList<Right>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(Right.class);

            if(!type.equals(QueryUtil.ALL)){
                cri.add(Restrictions.eq("type", type));
            }

            cri.addOrder(Order.asc("rightName"));

            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));

            rights = (List<Right>) cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return rights;
    }

    public List<Right> getAllRights(List<Long> types) throws Exception {
        List<Right> rights = new ArrayList<Right>();

        try {

            if(types.contains(QueryUtil.ALL)){
                return getAllRights(QueryUtil.ALL);
            }

            if(types.size()==1){
                return getAllRights(types.get(QueryUtil.FIRST_INDEX));
            }

            Session session = currentSession();

            Criteria cri = session.createCriteria(Right.class);

            cri.add(Restrictions.in("type", types));
            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));
            
            cri.addOrder(Order.asc("rightName"));

            rights = (List<Right>) cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return rights;
    }

    public Right getRightById(Long rightId) throws Exception {
        Right right = null;

        try {
            right = get(Right.class, rightId);

            if(Validator.isNull(right) ||
                    !QueryUtil.STATUS_ACTIVE.equals(right.getStatus())){
                return null;
            }
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return right;
    }

//    public List<Right> getRightsByGroupsRight(GroupsRight groupsRight) throws Exception {
//        List<Right> rights = new ArrayList<Right>();
//
//        try {
//            Session session = currentSession();
//
//            Criteria cri = session.createCriteria(Right.class);
//
//            cri.createAlias("groupsRights", "gr");
//
//            cri.add(Restrictions.eq("gr.groupsRightId",
//                    groupsRight.getGroupsRightId()));
//            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));
//
//            cri.addOrder(Order.asc("rightName"));
//
//            rights = (List<Right>) cri.list();
//        } catch (Exception e) {
//            _log.error(e.getMessage(), e);
//        }
//
//        return rights;
//    }

    public List<Right> getRightsByRN(String rightName) throws Exception {
        List<Right> rights = new ArrayList<Right>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(Right.class);

            if(Validator.isNotNull(rightName)){
                cri.add(LikeCriterionMaker.ilike("rightName", rightName,
                        MatchMode.ANYWHERE));
            }

            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));

            cri.addOrder(Order.asc("rightName"));
            
            rights = (List<Right>) cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return rights;
    }

    public List<Right> getRightsByRN_T(String rightName, Long type) throws Exception {
        List<Right> rights = new ArrayList<Right>();

        try {
            if(type.equals(QueryUtil.ALL)){
                return getRightsByRN(rightName);
            }

            Session session = currentSession();

            Criteria cri = session.createCriteria(Right.class);

            if(Validator.isNotNull(rightName)){
                cri.add(LikeCriterionMaker.ilike("rightName", rightName,
                        MatchMode.ANYWHERE));
            }

            cri.add(Restrictions.eq("type", type));
            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));

            cri.addOrder(Order.asc("rightName"));
            
            rights = (List<Right>) cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return rights;
    }

    public List<Right> getRightsByRN_T(String rightName, List<Long> types) throws Exception {
        List<Right> rights = new ArrayList<Right>();

        try {
            if(types.contains(QueryUtil.ALL)){
                return getRightsByRN(rightName);
            }

            if(types.size() == 1){
                return getRightsByRN_T(rightName, types.get(0));
            }

            Session session = currentSession();

            Criteria cri = session.createCriteria(Right.class);

            if(Validator.isNotNull(rightName)){
                cri.add(LikeCriterionMaker.ilike("rightName", rightName,
                        MatchMode.ANYWHERE));
            }

            cri.add(Restrictions.in("type", types));
            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));

            cri.addOrder(Order.asc("rightName"));
            
            rights = (List<Right>) cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return rights;
    }

    public int getCountAllRights() throws Exception {
        int count = 0;

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(Right.class);

            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));

            cri.setProjection(Projections.rowCount());

            count = (Integer) cri.uniqueResult();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return count;
    }

    public List<Right> getRightByCategoryId(Long categoryId) throws Exception{
        List<Right> rights = new ArrayList<Right>();

        try {
            Criteria cri = currentSession().createCriteria(Right.class);

            cri.add(Restrictions.eq("categoryId", categoryId));

            rights = (List<Right>) cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return rights;
    }

    public void deleteByCategoryId(Long categoryId) throws Exception{
        try {
            List<Right> rights = getRightByCategoryId(categoryId);

            deleteAll(rights);
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }
    }

    private static final Logger _log = LogManager.getLogger(RightDAOImpl.class);

    public List<Right> getRightByName(String rightName, Long rightId) throws Exception {
        List<Right> rights = new ArrayList<Right>();

        try {
            Criteria cri = currentSession().createCriteria(Right.class);

            cri.add(Restrictions.eq("rightName", rightName));

            if(Validator.isNotNull(rightId)){
                cri.add(Restrictions.ne("rightId", rightId));
            }

            rights = (List<Right>) cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return rights;
    }

    public Right getRightByCI_RN(Long categoryId, String folderName) {
        Right right = null;

        try {
            Criteria cri = currentSession().createCriteria(Right.class);

            cri.add(Restrictions.eq("categoryId", categoryId));
            cri.add(Restrictions.eq("rightName", folderName));

            right = (Right) cri.uniqueResult();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return right;
    }
}
