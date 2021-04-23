/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.service.impl;

import java.util.List;

import com.evotek.qlns.dao.DepartmentDAO;
import com.evotek.qlns.model.Department;
import com.evotek.qlns.service.DepartmentService;

/**
 *
 * @author My PC
 */
public class DepartmentServiceImpl implements DepartmentService{
    private transient DepartmentDAO departmentDAO;

    public DepartmentDAO getDepartmentDAO() {
        return departmentDAO;
    }

    public void setDepartmentDAO(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }
    
    public List<Department> getDepartmentByParentId(Long parentId){
        return departmentDAO.getDepartmentByParentId(parentId);
    }
    
    public void saveOrUpdate(Department dept){
        departmentDAO.saveOrUpdate(dept);
    }
    
    public int delete(List<Long> deptIds){
        return departmentDAO.delete(deptIds);
    }
    
    public void updateOrdinal(Long parentId, Long deletedIndex){
        departmentDAO.updateOrdinal(parentId, deletedIndex);
    }
    
    public Department getDeparment(Long deptId){
        return departmentDAO.get(deptId);
    }
    
    public Long getNextOrdinal(Long parentId){
        return departmentDAO.getNextOrdinal(parentId);
    }
}
