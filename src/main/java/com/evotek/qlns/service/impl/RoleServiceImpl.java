
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evotek.qlns.dao.CategoryDAO;
import com.evotek.qlns.dao.GroupDAO;
import com.evotek.qlns.dao.RoleDAO;
import com.evotek.qlns.model.Group;
import com.evotek.qlns.model.Role;
import com.evotek.qlns.service.RoleService;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author hungnt78
 */
public class RoleServiceImpl implements RoleService {
    private static final Logger _log =
            LogManager.getLogger(RoleServiceImpl.class);

    private transient RoleDAO roleDAO;
    private transient GroupDAO groupDAO;
    private transient CategoryDAO categoryDAO;

    public RoleDAO getRoleDAO() {
        return roleDAO;
    }

    public void setRoleDAO(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public GroupDAO getGroupDAO() {
        return groupDAO;
    }

    public void setGroupDAO(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    public CategoryDAO getCategoryDAO() {
        return categoryDAO;
    }

    public void setCategoryDAO(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public List<Role> getRoles(String roleName, Long status)
            throws Exception {
        return roleDAO.getRoles(roleName, status);
    }

    public void lockRole(Role role) throws Exception {
        try{
            role.setStatus(Values.STATUS_DEACTIVE);

            roleDAO.saveOrUpdate(role);
        }catch(Exception ex){
            _log.error(ex.getMessage(), ex);
        }
    }

    public void unlockRole(Role role) throws Exception {
        try{
            role.setStatus(Values.STATUS_ACTIVE);

            roleDAO.saveOrUpdate(role);
        }catch(Exception ex){
            _log.error(ex.getMessage(), ex);
        }
    }

    public void deleteRole(Role role) throws Exception {
        try{
            Long status = role.getStatus();

            if(!Values.STATUS_ACTIVE.equals(status)){
                roleDAO.delete(role);
            }
        }catch(Exception ex){
            _log.error(ex.getMessage(), ex);
        }
    }

    public List<Group> getGroupByCategoryId(Long categoryId) throws Exception{
        return groupDAO.getGroupByCategoryId(categoryId);
    }

    public boolean isRoleExist(String roleName, Role role) throws Exception{
        Long roleId = null;

        if(role!=null){
            roleId = role.getRoleId();
        }

        List<Role> roles = roleDAO.getRoleByRN(roleName, roleId);

        return !roles.isEmpty();
    }

    public void saveOrUpdateRole(Role role) throws Exception{
        roleDAO.saveOrUpdate(role);
    }
    
}
