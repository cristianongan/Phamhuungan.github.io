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

import com.evotek.qlns.dao.GroupDAO;
import com.evotek.qlns.model.Group;

/**
 *
 * @author linhlh2
 */
public class GroupDAOImpl extends AbstractDAO<Group> implements GroupDAO {

    private static final Logger _log = LogManager.getLogger(GroupDAOImpl.class);

    @Override
	public void deleteByCategoryId(Long categoryId) throws Exception{
        try {
            List<Group> groups = getGroupByCategoryId(categoryId);

            deleteAll(groups);
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }
    }

    @Override
	public List<Group> getGroupByCategoryId(Long categoryId) throws Exception{
        List<Group> groups = new ArrayList<Group>();

        try {
        	Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Group> criteria = builder.createQuery(Group.class);
			
			Root<Group> root = criteria.from(Group.class);
			
			criteria.select(root).where(builder.equal(root.get("categoryId"), categoryId));

            groups = session.createQuery(criteria).getResultList();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return groups;
    }
}
