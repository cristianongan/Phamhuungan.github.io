/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.evotek.qlns.model.Role;

/**
 *
 * @author linhlh2
 */
public interface RoleDAO {

    public Role getNewRole();

    public Role getRoleById(Long roleId) throws Exception;

    public List<Role> getRoleByRN(String roleName) throws Exception;

    public List<Role> getRoleByRN(String roleName, Long roleId) throws Exception;

    public List<Role> getRoles(boolean isAdmin);

    public int getCountAllRoles() throws Exception;

    public List<Role> getRoles(String roleName, Long status) throws Exception;

    public void saveOrUpdate(Role role) throws DataAccessException;

    public void delete(Role role) throws DataAccessException;

    public List<Role> searchRole(String roleName);

    public Role getRoleByName(String roleName) throws Exception;
}
