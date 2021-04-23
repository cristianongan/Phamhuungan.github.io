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
import org.hibernate.criterion.Restrictions;

import com.evotek.qlns.dao.GroupDAO;
import com.evotek.qlns.model.Group;
import com.evotek.qlns.model.User;

/**
 *
 * @author linhlh2
 */
public class GroupDAOImpl extends BasicDAO<Group> implements GroupDAO {

    private static final Logger _log = LogManager.getLogger(GroupDAOImpl.class);

    public List<Group> getGroupByUser(User user) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Group> getGroupByCategoryId(Long categoryId) throws Exception{
        List<Group> groups = new ArrayList<Group>();

        try {
            Criteria cri = currentSession().createCriteria(Group.class);

            cri.add(Restrictions.eq("categoryId", categoryId));

            groups = (List<Group>) cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return groups;
    }

    public void deleteByCategoryId(Long categoryId) throws Exception{
        try {
            List<Group> groups = getGroupByCategoryId(categoryId);

            deleteAll(groups);
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }
    }
}
