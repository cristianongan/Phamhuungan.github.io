
/*
 * Copyright 2013 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.evotek.qlns.dao.CategoryDAO;
import com.evotek.qlns.model.Category;
import com.evotek.qlns.model.User;
import com.evotek.qlns.util.QueryUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author LinhLH2
 */
public class CategoryDAOImpl extends BasicDAO<Category> implements CategoryDAO {

    public List<Category> getAllCategory() throws Exception {
        List<Category> categories = new ArrayList<Category>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(Category.class);

            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));

            cri.addOrder(Order.asc("type"));
            cri.addOrder(Order.asc("weight"));
            cri.addOrder(Order.asc("createDate"));

            categories = (List<Category>) cri.list();

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return categories;
    }

    public List<Category> getCategoryItems() throws Exception{
        List<Category> categories = new ArrayList<Category>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(Category.class);

            cri.add(Restrictions.eq("type", Values.MENU_TYPE_ITEM));
            cri.add(Restrictions.ne("status", QueryUtil.STATUS_DEACTIVE));

            cri.addOrder(Order.asc("type"));
            cri.addOrder(Order.asc("weight"));
            cri.addOrder(Order.asc("createDate"));

            categories = (List<Category>) cri.list();

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return categories;
    }

    public List<Category> getCategoryByUser(User user) throws Exception {
        List<Category> categorys = new ArrayList<Category>();

        try {
            Session session = currentSession();

            StringBuilder sb = new StringBuilder();

            sb.append("SELECT distinct c FROM Category c, RightView rv");
            sb.append(" WHERE rv.userId = :userId AND rv.rightType in (1, 2)");
            sb.append(" AND c.folderName = rv.rightName AND rv.status = 1");
            sb.append(" AND c.status = 1");
            sb.append(" ORDER BY c.type ASC, c.weight ASC, c.createDate ASC");

            Query q = session.createQuery(sb.toString());

            q.setParameter("userId", user.getUserId());

            categorys = (List<Category>) q.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return categorys;
    }

    public List<Category> getCategoryByParentId(Long parentId) throws Exception {
        List<Category> results = new ArrayList<Category>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(Category.class);

            if(Validator.isNull(parentId)){
                cri.add(Restrictions.isNull("parentId"));
            }else{
                cri.add(Restrictions.eq("parentId", parentId));
            }

            cri.addOrder(Order.asc("weight"));
            cri.addOrder(Order.asc("createDate"));

            results = (List<Category>) cri.list();
            
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return results;
    }

    public List<Category> getCategoryMenuByParentId(Long parentId) throws Exception {
        List<Category> results = new ArrayList<Category>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(Category.class);

            if(Validator.isNull(parentId)){
                cri.add(Restrictions.isNull("parentId"));
            }else{
                cri.add(Restrictions.eq("parentId", parentId));
            }

            cri.add(Restrictions.eq("type", Values.MENU_TYPE_CATEGORY));

            cri.addOrder(Order.asc("weight"));
            cri.addOrder(Order.asc("createDate"));

            results = (List<Category>) cri.list();

        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return results;
    }

    public Category getCategoryById(Long categoryId) throws Exception{
        return get(Category.class, categoryId);
    }

    private static final Logger _log =
            LogManager.getLogger(CategoryDAOImpl.class);
}
