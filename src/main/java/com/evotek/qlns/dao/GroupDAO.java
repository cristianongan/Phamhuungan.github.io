/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.evotek.qlns.model.Group;
import com.evotek.qlns.model.User;

/**
 *
 * @author linhlh2
 */
public interface GroupDAO {

    public List<Group> getGroupByUser(User user) throws Exception;

    public List<Group> getGroupByCategoryId(Long categoryId) throws Exception;

    public void deleteByCategoryId(Long categoryId) throws Exception;

    public void saveOrUpdate(Group group) throws DataAccessException;

    public void save(Group group) throws DataAccessException;

    public void delete(Group group) throws DataAccessException;
}
