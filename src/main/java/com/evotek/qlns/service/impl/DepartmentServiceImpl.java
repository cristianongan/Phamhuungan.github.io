/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evotek.qlns.dao.DepartmentDAO;
import com.evotek.qlns.model.Department;
import com.evotek.qlns.service.DepartmentService;

/**
 *
 * @author LinhLH2
 */
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
	@Autowired
	private DepartmentDAO departmentDAO;

	@Override
	public List<Department> getDepartmentByParentId(Long parentId) {
		return this.departmentDAO.getDepartmentByParentId(parentId);
	}

	@Override
	public void saveOrUpdate(Department dept) {
		this.departmentDAO.saveOrUpdate(dept);
	}

	@Override
	public int delete(List<Long> deptIds) {
		return this.departmentDAO.delete(deptIds);
	}

	@Override
	public void updateOrdinal(Long parentId, Long deletedIndex) {
		this.departmentDAO.updateOrdinal(parentId, deletedIndex);
	}

	@Override
	public Department getDeparment(Long deptId) {
		return this.departmentDAO.get(deptId);
	}

	@Override
	public Long getNextOrdinal(Long parentId) {
		return this.departmentDAO.getNextOrdinal(parentId);
	}
}
