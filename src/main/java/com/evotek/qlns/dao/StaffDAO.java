/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.dao;

import java.util.List;

import com.evotek.qlns.model.Department;
import com.evotek.qlns.model.Job;
import com.evotek.qlns.model.Staff;

/**
 *
 * @author PC
 */
public interface StaffDAO {

	public void delete(Staff staff);

	public void deleteAllStaff(List<Staff> staff) throws Exception;

	public List<Staff> getBirthDayNearlyStaff(int dayOfYear);

	public List<Staff> getContractExpiredStaff();

	// public List<HumanResource> getAll();
	public Staff getStaff(Long staffId);

	public List<Staff> getStaff(String keyword, int firstResult, int maxResult, String orderByColumn,
			String orderByType);

	public List<Staff> getStaff(String staffName, Long yearOfBirth, Department dept, String email, Job job,
			String phone, int firstResult, int maxResult, String orderByColumn, String orderByType);

	public List<Staff> getStaffByIdList(List<Long> idList, int firstResult, int maxResult, String orderByColumn,
			String orderByType);

	public int getStaffCount(String keyword);

	public int getStaffCount(String staffName, Long yearOfBirth, Department dept, String email, Job job, String phone);

	public int getStaffCountByIdList(List<Long> idList);

	public void saveOrUpdate(Staff staff);

}
