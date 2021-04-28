/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao;

import java.util.List;

import com.evotek.qlns.model.Department;

/**
 *
 * @author My PC
 */
public interface DepartmentDAO {

	public int delete(List<Long> deptIds);

	public Department get(Long deptId);

	public List<Department> getDepartmentByParentId(Long parentId);

	public Long getNextOrdinal(Long parentId);

	public Long getNextOrdinalSql(Long parentId);

	public void saveOrUpdate(Department dept);

	public void updateOrdinal(Long parentId, Long deletedIndex);
}
