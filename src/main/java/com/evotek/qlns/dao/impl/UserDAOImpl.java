/*
 * Copyright 2013 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import com.evotek.qlns.dao.UserDAO;
import com.evotek.qlns.model.User;
import com.evotek.qlns.util.LikeCriterionMaker;
import com.evotek.qlns.util.QueryUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author hungnt1 LinhLH2 fixed
 */
public class UserDAOImpl extends BasicDAO<User> implements UserDAO {

    public List<User> getListUsers() {
        List<User> results = new ArrayList<User>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(User.class);

            cri.add(Restrictions.eq("status", 1L));

            results = (List<User>) cri.list();
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return results;
    }

    public User getNewUser() {
        return new User();
    }

    public int getCountAllUsers() throws Exception {
        int count = 0;

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(User.class);

            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));

            cri.setProjection(Projections.rowCount());

            count = (Integer) cri.uniqueResult();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return count;
    }

    public List<User> getAllUsers() throws Exception {
        List<User> users = new ArrayList<User>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(User.class);

            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));

            users = (List<User>) cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return users;
    }

    public User getUserById(Long userId) {
        User user = null;

        try {
            user = get(User.class, userId);

            if (QueryUtil.STATUS_DEACTIVE.equals(user.getStatus())) {
                return null;
            }
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return user;
    }

    public User getUserByUserName(String userName) throws Exception {
        User user = null;

        try {
            if (Validator.isNull(userName)) {
                return user;
            }

            Session session = currentSession();

            Criteria cri = session.createCriteria(User.class);

            cri.add(Restrictions.eq("userName", userName).ignoreCase());
            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));

            List<User> results = (List<User>) cri.list();

            if (!results.isEmpty()) {
                user = results.get(0);
            }

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return user;
    }

    public List<User> getUsersLikeUserName(String value) throws Exception {
        List<User> users = new ArrayList<User>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(User.class);

            if (Validator.isNotNull(value)) {
                cri.add(LikeCriterionMaker.ilike("userName", value, MatchMode.ANYWHERE));
            }

            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));

            users = (List<User>) cri.list();

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return users;
    }

    public List<User> getUsersLikeLastname(String value) throws Exception {
        List<User> users = new ArrayList<User>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(User.class);

            if (Validator.isNotNull(value)) {
                cri.add(LikeCriterionMaker.ilike("lastName", value, MatchMode.ANYWHERE));
            }

            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));

            users = (List<User>) cri.list();

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return users;
    }

    public List<User> getUsersLikeEmail(String value) throws Exception {
        List<User> users = new ArrayList<User>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(User.class);

            if (Validator.isNotNull(value)) {
                cri.add(LikeCriterionMaker.ilike("email", value, MatchMode.ANYWHERE));
            }

            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));

            users = (List<User>) cri.list();

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return users;
    }

//    public List<User> getUsers(String userName, String email, Long gender,
//            String birthPlace, Date birthdayFrom, Date birthdayTo, String phone,
//            String mobile, String account, Long status, int firstResult,
//            int maxResult, String orderByColumn, String orderByType) {
//        List<User> users = new ArrayList<User>();
//
//        try {
//            SQLQuery sql = getUsersQuery(userName, email, gender, birthPlace,
//                    birthdayFrom, birthdayTo, phone, mobile, account, status,
//                    orderByColumn, orderByType, false);
//
//            if (firstResult >= 0 && maxResult > 0) {
//                sql.setFirstResult(firstResult);
//                sql.setMaxResults(maxResult);
//            }
//
//            sql.setResultTransformer(Transformers.aliasToBean(User.class));
//
//            users = (List<User>) sql.list();
//        } catch (Exception ex) {
//            _log.error(ex.getMessage(), ex);
//        }
//
//        return users;
//    }
//
//    public int getUsersCount(String userName, String email, Long gender,
//            String birthPlace, Date birthdayFrom, Date birthdayTo, String phone,
//            String mobile, String account, Long status) {
//
//        int result = 0;
//
//        try {
//            SQLQuery sql = getUsersQuery(userName, email, gender, birthPlace,
//                    birthdayFrom, birthdayTo, phone, mobile, account, status,
//                    null, null, true);
//
//            result = ((BigInteger) sql.uniqueResult()).intValue();
//        } catch (Exception ex) {
//            _log.error(ex.getMessage(), ex);
//        }
//
//        return result;
//    }
//
//    public List<User> getUsers(String keyword, int firstResult, int maxResult,
//            String orderByColumn, String orderByType) {
//        List<User> users = new ArrayList<User>();
//
//        try {
//            SQLQuery sql = getUsersQuery(keyword, orderByColumn, orderByType,
//                    false);
//
//            if (firstResult >= 0 && maxResult > 0) {
//                sql.setFirstResult(firstResult);
//                sql.setMaxResults(maxResult);
//            }
//
//            sql.setResultTransformer(Transformers.aliasToBean(User.class));
//
//            users = (List<User>) sql.list();
//        } catch (Exception ex) {
//            _log.error(ex.getMessage(), ex);
//        }
//
//        return users;
//    }
//
//    public int getUsersCount(String keyword) {
//        int result = 0;
//
//        try {
//            SQLQuery sql = getUsersQuery(keyword, null, null, true);
//
//            result = ((BigInteger) sql.uniqueResult()).intValue();
//        } catch (Exception ex) {
//            _log.error(ex.getMessage(), ex);
//        }
//
//        return result;
//    }

    public List<User> getUsers(String userName, String email, Long gender,
            String birthPlace, Date birthdayFrom, Date birthdayTo, String phone,
            String mobile, String account, Long status, int firstResult,
            int maxResult, String orderByColumn, String orderByType) {
        List<User> users = new ArrayList<User>();

        try {
            Criteria cri = getUsersCriteria(userName, email, gender, birthPlace,
                    birthdayFrom, birthdayTo, phone, mobile, account, status,
                    orderByColumn, orderByType);

            if (firstResult >= 0 && maxResult > 0) {
                cri.setFirstResult(firstResult);
                cri.setMaxResults(maxResult);
            }

            users = (List<User>) cri.list();
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return users;
    }

    public int getUsersCount(String userName, String email, Long gender,
            String birthPlace, Date birthdayFrom, Date birthdayTo, String phone,
            String mobile, String account, Long status) {

        int result = 0;

        try {
            Criteria cri = getUsersCriteria(userName, email, gender, birthPlace,
                    birthdayFrom, birthdayTo, phone, mobile, account, status,
                    null, null);

            cri.setProjection(Projections.rowCount());

            result = ((Integer) cri.uniqueResult()).intValue();
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return result;
    }

    public List<User> getUsers(String keyword, int firstResult, int maxResult,
            String orderByColumn, String orderByType) {
        List<User> users = new ArrayList<User>();

        try {
            Criteria cri = getUsersCriteria(keyword, orderByColumn, orderByType);

            if (firstResult >= 0 && maxResult > 0) {
                cri.setFirstResult(firstResult);
                cri.setMaxResults(maxResult);
            }

            users = (List<User>) cri.list();
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return users;
    }

    public int getUsersCount(String keyword) {
        int result = 0;

        try {
            Criteria cri = getUsersCriteria(keyword, null, null);

            cri.setProjection(Projections.rowCount());

            result = (Integer) cri.uniqueResult();
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return result;
    }

    private Criteria getUsersCriteria(String keyword, String orderByColumn,
            String orderByType) throws Exception{
        try {
            Criteria cri = currentSession().createCriteria(User.class);

            if (Validator.isNotNull(keyword)) {
               Disjunction disjunction = Restrictions.disjunction();

               disjunction.add(LikeCriterionMaker.ilike("firstName",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("middleName",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("lastName",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("email",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("birthPlace",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("phone",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("mobile",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("userName",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("address",
                        keyword, MatchMode.ANYWHERE));

               cri.add(disjunction);
            }

            cri.add(Restrictions.ne("status",
                        Values.STATUS_DEACTIVE));
            
            if (Validator.isNotNull(orderByColumn)) {
                if(StringPool.ASC.equalsIgnoreCase(orderByType)){
                    cri.addOrder(Order.asc(orderByColumn));
                } else {
                    cri.addOrder(Order.desc(orderByColumn));
                }

            } else {
                cri.addOrder(Order.desc("modifiedDate"));
            }

            return cri;
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);

            throw ex;
        }
    }

    private Criteria getUsersCriteria(String userName, String email, Long gender,
            String birthPlace, Date birthdayFrom, Date birthdayTo, String phone,
            String mobile, String account, Long status, String orderByColumn,
            String orderByType) throws Exception{
        try {
            Criteria cri = currentSession().createCriteria(User.class);

            Conjunction conjunction = Restrictions.conjunction();

            if (Validator.isNotNull(userName)) {
                Disjunction disjunction = Restrictions.disjunction();

                disjunction.add(LikeCriterionMaker.ilike("firstName",
                        userName, MatchMode.ANYWHERE));
                disjunction.add(LikeCriterionMaker.ilike("middleName",
                        userName, MatchMode.ANYWHERE));
                disjunction.add(LikeCriterionMaker.ilike("lastName",
                        userName, MatchMode.ANYWHERE));

                conjunction.add(disjunction);
            }

            if (Validator.isNotNull(email)) {
                conjunction.add(LikeCriterionMaker.ilike("email",
                        email, MatchMode.ANYWHERE));
            }

            if (Validator.isNotNull(gender)) {
                conjunction.add(Restrictions.eq("gender", gender));
            }

            if (Validator.isNotNull(birthPlace)) {
                conjunction.add(LikeCriterionMaker.ilike("birthPlace",
                        birthPlace, MatchMode.ANYWHERE));
            }

            if (Validator.isNotNull(birthdayFrom)) {
                conjunction.add(Restrictions.ge("dateOfBirth", birthdayFrom));
            }

            if (Validator.isNotNull(birthdayTo)) {
                conjunction.add(Restrictions.le("dateOfBirth", birthdayTo));
            }

            if (Validator.isNotNull(phone)) {
                conjunction.add(LikeCriterionMaker.ilike("phone",
                        phone, MatchMode.ANYWHERE));
            }

            if (Validator.isNotNull(mobile)) {
                conjunction.add(LikeCriterionMaker.ilike("mobile",
                        mobile, MatchMode.ANYWHERE));
            }

            if (Validator.isNotNull(account)) {
                conjunction.add(LikeCriterionMaker.ilike("account",
                        account, MatchMode.ANYWHERE));
            }

            if (Validator.isNotNull(status)) {
                conjunction.add(Restrictions.eq("status", status));
            } else {
                conjunction.add(Restrictions.ne("status",
                        Values.STATUS_DEACTIVE));
            }

            cri.add(conjunction);

            if (Validator.isNotNull(orderByColumn)) {
                if(StringPool.ASC.equalsIgnoreCase(orderByType)){
                    cri.addOrder(Order.asc(orderByColumn));
                } else {
                    cri.addOrder(Order.desc(orderByColumn));
                }

            } else {
                cri.addOrder(Order.desc("modifiedDate"));
            }

            return cri;
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);

            throw ex;
        }
    }

    public SQLQuery getUsersQuery(String userName, String email, Long gender,
            String birthPlace, Date birthdayFrom, Date birthdayTo, String phone,
            String mobile, String account, Long status, String orderByColumn,
            String orderByType, boolean count)
            throws Exception {
        try {
            Session session = currentSession();

            StringBuilder sb = new StringBuilder();

            List params = new ArrayList();

            if (count) {
                sb.append("select count(*)");
            } else {
                sb.append(" SELECT a.user_id as userId, a.user_name as userName, ");
                sb.append(" a.password as password, a.email as email, a.phone ");
                sb.append(" as phone, a.mobile as mobile, a.gender as ");
                sb.append(" gender, a.date_of_birth as dateOfBirth, a.birth_place ");
                sb.append(" as birthPlace, a.address as address, ");
                sb.append(" a.create_date as createDate, a.modified_date as ");
                sb.append(" modifiedDate, a.first_name as firstName, a.last_name as ");
                sb.append(" lastName, a.status as status, a.description as ");
                sb.append(" description");

            }

            sb.append(" FROM user_ a");
            sb.append(" where 1 = 1 ");

            if (Validator.isNotNull(userName)) {
                sb.append(" and lower(a.first_name) like ? escape '\\' or lower(a.last_name) like ? escape '\\'");

                params.add(QueryUtil.getFullStringParam(userName.toLowerCase()));
                params.add(QueryUtil.getFullStringParam(userName.toLowerCase()));
            }

            if (Validator.isNotNull(email)) {
                sb.append(" and lower(a.email) like ? escape '\\'");

                params.add(QueryUtil.getFullStringParam(email.toLowerCase()));
            }

            if (Validator.isNotNull(gender)) {
                sb.append(" and a.gender = ? ");

                params.add(gender);
            }

            if (Validator.isNotNull(birthPlace)) {
                sb.append(" and lower(a.birth_place) like ? escape '\\'");

                params.add(QueryUtil.getFullStringParam(birthPlace.toLowerCase()));
            }

            if (Validator.isNotNull(birthdayFrom)) {
                sb.append(" and a.date_of_birth >= ?");

                params.add(birthdayFrom);
            }

            if (Validator.isNotNull(birthdayTo)) {
                sb.append(" and a.date_of_birth <= ?");

                params.add(birthdayTo);
            }

            if (Validator.isNotNull(phone)) {
                sb.append(" and lower(a.phone) like ? escape '\\'");

                params.add(QueryUtil.getFullStringParam(mobile.toLowerCase()));
                params.add(QueryUtil.getFullStringParam(mobile.toLowerCase()));
            }

            if (Validator.isNotNull(mobile)) {
                sb.append(" and lower(a.mobile) like ? escape '\\'");

                params.add(QueryUtil.getFullStringParam(mobile.toLowerCase()));
                params.add(QueryUtil.getFullStringParam(mobile.toLowerCase()));
            }

            if (Validator.isNotNull(account)) {
                sb.append(" and lower(a.user_name) like ? escape '\\'");

                params.add(QueryUtil.getFullStringParam(account.toLowerCase()));
            }

            if (Validator.isNotNull(status)) {
                sb.append(" and a.status = ? ");

                params.add(status);
            } else {
                sb.append(" and a.status != 0 ");
            }

            if (Validator.isNotNull(orderByColumn)
                    && Validator.isNotNull(orderByType)) {
                sb.append(QueryUtil.addOrder(User.class,
                        orderByColumn, orderByType));
            } else {
                sb.append(" order by a.modified_date desc");
            }

            SQLQuery sql = session.createSQLQuery(sb.toString());

            if (!count) {
                sql.addScalar("userId", LongType.INSTANCE);
                sql.addScalar("userName", StringType.INSTANCE);
                sql.addScalar("password", StringType.INSTANCE);
                sql.addScalar("email", StringType.INSTANCE);
                sql.addScalar("phone", StringType.INSTANCE);
                sql.addScalar("mobile", StringType.INSTANCE);
                sql.addScalar("gender", LongType.INSTANCE);
                sql.addScalar("dateOfBirth", DateType.INSTANCE);
                sql.addScalar("birthPlace", StringType.INSTANCE);
                sql.addScalar("address", StringType.INSTANCE);
                sql.addScalar("createDate", DateType.INSTANCE);
                sql.addScalar("modifiedDate", DateType.INSTANCE);
                sql.addScalar("firstName", StringType.INSTANCE);
                sql.addScalar("lastName", StringType.INSTANCE);
                sql.addScalar("status", LongType.INSTANCE);
                sql.addScalar("description", StringType.INSTANCE);
            }

            for (int i = 0; i < params.size(); i++) {
                sql.setParameter(i, params.get(i));
            }

            return sql;

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);

            throw ex;
        }
    }

    public SQLQuery getUsersQuery(String keyword, String orderByColumn,
            String orderByType, boolean count)
            throws Exception {
        try {
            Session session = currentSession();

            StringBuilder sb = new StringBuilder();

            List params = new ArrayList();

            if (count) {
                sb.append("select count(*)");
            } else {
                sb.append(" SELECT a.user_id as userId, a.user_name as userName, ");
                sb.append(" a.password as password, a.email as email, a.phone ");
                sb.append(" as phone, a.mobile as mobile, a.gender as ");
                sb.append(" gender, a.date_of_birth as dateOfBirth, a.birth_place ");
                sb.append(" as birthPlace, a.address as address, ");
                sb.append(" a.create_date as createDate, a.modified_date as ");
                sb.append(" modifiedDate, a.first_name as firstName, a.last_name as ");
                sb.append(" lastName, a.status as status, a.description as ");
                sb.append(" description");
            }

            sb.append(" FROM user_ a");
            sb.append(" where 1 = 1 and a.status != 0");

            if (Validator.isNotNull(keyword)) {
                String _keyword = QueryUtil.getFullStringParam(keyword.toLowerCase());

                sb.append(" and (lower(a.first_name) like ? escape '\\'");
                sb.append(" or lower(a.last_name) like ? escape '\\'");
                sb.append(" or lower(a.email) like ? escape '\\'");
                sb.append(" or lower(a.birth_place) like ? escape '\\'");
                sb.append(" or lower(a.mobile) like ? escape '\\'");
                sb.append(" or lower(a.phone) like ? escape '\\'");
                sb.append(" or lower(a.user_name) like ? escape '\\'");
                sb.append(")");

                params.add(_keyword);
                params.add(_keyword);
                params.add(_keyword);
                params.add(_keyword);
                params.add(_keyword);
                params.add(_keyword);
                params.add(_keyword);
            }

            if (Validator.isNotNull(orderByColumn)
                    && Validator.isNotNull(orderByType)) {
                sb.append(QueryUtil.addOrder(User.class,
                        orderByColumn, orderByType));
            } else {
                sb.append(" order by a.modified_date desc");
            }

            SQLQuery sql = session.createSQLQuery(sb.toString());

            if (!count) {
                sql.addScalar("userId", LongType.INSTANCE);
                sql.addScalar("userName", StringType.INSTANCE);
                sql.addScalar("password", StringType.INSTANCE);
                sql.addScalar("email", StringType.INSTANCE);
                sql.addScalar("phone", StringType.INSTANCE);
                sql.addScalar("mobile", StringType.INSTANCE);
                sql.addScalar("gender", LongType.INSTANCE);
                sql.addScalar("dateOfBirth", DateType.INSTANCE);
                sql.addScalar("birthPlace", StringType.INSTANCE);
                sql.addScalar("address", StringType.INSTANCE);
                sql.addScalar("createDate", DateType.INSTANCE);
                sql.addScalar("modifiedDate", DateType.INSTANCE);
                sql.addScalar("firstName", StringType.INSTANCE);
                sql.addScalar("lastName", StringType.INSTANCE);
                sql.addScalar("status", LongType.INSTANCE);
                sql.addScalar("description", StringType.INSTANCE);
            }

            for (int i = 0; i < params.size(); i++) {
                sql.setParameter(i, params.get(i));
            }

            return sql;

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);

            throw ex;
        }
    }

    public List<User> getUsersByI_UN(Long userId, String userName) {
        List<User> results = new ArrayList<User>();

        try {
            Criteria cri = currentSession().createCriteria(User.class);

            cri.add(LikeCriterionMaker.ilike("userName", userName, MatchMode.EXACT));

            if (Validator.isNotNull(userId)) {
                cri.add(Restrictions.ne("userId", userId));
            }

            results = (List<User>) cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return results;
    }

    public List<User> getUsersByI_E(Long userId, String email) {
        List<User> results = new ArrayList<User>();

        try {
            Criteria cri = currentSession().createCriteria(User.class);

            cri.add(LikeCriterionMaker.ilike("email", email, MatchMode.EXACT));

            if (Validator.isNotNull(userId)) {
                cri.add(Restrictions.ne("userId", userId));
            }

            results = (List<User>) cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return results;
    }
    
    private static final Logger _log = LogManager.getLogger(UserDAOImpl.class);
    

    public Map<String, Long> createMapUser() {
        Map<String, Long> map = new HashMap<String, Long>();
        Session session = null;
        try {
            session = currentSession();
            Criteria criteria = session.createCriteria(User.class);
            List<User> results = (List<User>) criteria.list();
            for (User user : results) {
                map.put(user.getUserName().toLowerCase(), user.getUserId());
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        } finally {
            return map;
        }
    }
}
