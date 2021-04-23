/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.service;

import java.util.List;

import com.evotek.qlns.model.Department;

/**
 *
 * @author My PC
 */
public interface DepartmentService {
    public List<Department> getDepartmentByParentId(Long parentId);
    
    public void saveOrUpdate(Department dept);

    public int delete(List<Long> deptIds);
    
    public void updateOrdinal(Long parentId, Long deletedIndex);

    public Department getDeparment(Long deptId);
    
    public Long getNextOrdinal(Long parentId);
}
