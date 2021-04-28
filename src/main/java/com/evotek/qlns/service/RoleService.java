/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.service;

import java.util.List;

import com.evotek.qlns.model.Group;
import com.evotek.qlns.model.Role;

/**
 *
 * @author hungnt78
 */
public interface RoleService {
	public void deleteRole(Role role) throws Exception;

	public List<Group> getGroupByCategoryId(Long categoryId) throws Exception;

	public List<Role> getRoles(String roleName, Long status) throws Exception;

	public boolean isRoleExist(String roleName, Role role) throws Exception;

	public void lockRole(Role role) throws Exception;

	public void saveOrUpdateRole(Role role) throws Exception;

	public void unlockRole(Role role) throws Exception;

}
