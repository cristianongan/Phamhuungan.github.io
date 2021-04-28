/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.evotek.qlns.model.Right;
import com.evotek.qlns.model.RightView;
import com.evotek.qlns.model.User;

/**
 *
 * @author linhlh2
 */
public interface RightDAO {

	public void delete(Right right) throws DataAccessException;

	public void deleteByCategoryId(Long categoryId) throws Exception;

	public List<Right> getAllRights() throws Exception;

	public List<Right> getAllRights(List<Long> types) throws Exception;

	public List<Right> getAllRights(Long type) throws Exception;

//    public List<Right> getRightsByGroupsRight(GroupsRight groupsRight)
//            throws Exception;

	public int getCountAllRights() throws Exception;

	public Right getNewRight();

	public List<Right> getRightByCategoryId(Long categoryId) throws Exception;

	public Right getRightByCI_RN(Long categoryId, String folderName);

	public Right getRightById(Long rightId) throws Exception;

	public List<Right> getRightByName(String rightName, Long rightId) throws Exception;

	public List<Right> getRightsByRN(String rightName) throws Exception;

	public List<Right> getRightsByRN_T(String rightName, List<Long> types) throws Exception;

	public List<Right> getRightsByRN_T(String rightName, Long type) throws Exception;

	public List<Right> getRightsByUser(User user) throws Exception;

	public List<RightView> getRightViewByUserId(Long userId) throws Exception;

	public void saveOrUpdate(Right right) throws DataAccessException;
}
