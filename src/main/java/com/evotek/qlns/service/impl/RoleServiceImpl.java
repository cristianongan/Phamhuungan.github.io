
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evotek.qlns.dao.GroupDAO;
import com.evotek.qlns.dao.RoleDAO;
import com.evotek.qlns.model.Group;
import com.evotek.qlns.model.Role;
import com.evotek.qlns.service.RoleService;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author LinhLH
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
	private static final Logger _log = LogManager.getLogger(RoleServiceImpl.class);

	@Autowired
	private GroupDAO groupDAO;

	@Autowired
	private RoleDAO roleDAO;

	@Override
	public void deleteRole(Role role) throws Exception {
		try {
			Long status = role.getStatus();

			if (!Values.STATUS_ACTIVE.equals(status)) {
				this.roleDAO.delete(role);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public List<Group> getGroupByCategoryId(Long categoryId) throws Exception {
		return this.groupDAO.getGroupByCategoryId(categoryId);
	}

	@Override
	public List<Role> getRoles(String roleName, Long status) throws Exception {
		return this.roleDAO.getRoles(roleName, status);
	}

	@Override
	public boolean isRoleExist(String roleName, Role role) throws Exception {
		Long roleId = null;

		if (role != null) {
			roleId = role.getRoleId();
		}

		List<Role> roles = this.roleDAO.getRoleByRN(roleName, roleId);

		return !roles.isEmpty();
	}

	@Override
	public void lockRole(Role role) throws Exception {
		try {
			role.setStatus(Values.STATUS_DEACTIVE);

			this.roleDAO.saveOrUpdate(role);
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public void saveOrUpdateRole(Role role) throws Exception {
		this.roleDAO.saveOrUpdate(role);
	}

	@Override
	public void unlockRole(Role role) throws Exception {
		try {
			role.setStatus(Values.STATUS_ACTIVE);

			this.roleDAO.saveOrUpdate(role);
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

}
