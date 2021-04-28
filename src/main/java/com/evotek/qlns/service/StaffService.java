/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.service;

import java.util.List;

import com.evotek.qlns.model.ContractType;
import com.evotek.qlns.model.Department;
import com.evotek.qlns.model.Job;
import com.evotek.qlns.model.SalaryLandmark;
import com.evotek.qlns.model.Staff;
import com.evotek.qlns.model.WorkProcess;

/**
 *
 * @author PC
 */
public interface StaffService {

	public void deleteAll(List<Staff> staff) throws Exception;

	public void deleteContractType(ContractType ct) throws Exception;

	public void deleteJob(Job job) throws Exception;

	public void deleteSalaryLm(SalaryLandmark salaryLm);

	public void deleteStaff(Staff staff) throws Exception;

	public void deleteWorkProcess(WorkProcess wp);

	public List<String> getCompanyName();

	public List<ContractType> getContract();

	public List<Job> getJobTitle();

	public List<SalaryLandmark> getSalaryLandmarkByStaffId(Long staffId);

	public Staff getStaff(Long staffId);

	public List<Staff> getStaff(String keyword, int firstResult, int maxResult, String orderByColumn,
			String orderByType);

	public List<Staff> getStaff(String staffName, Long yearOfBirth, Department dept, String email, Job job,
			String phone, int itemStartNumber, int pageSize, String orderByColumn, String orderByType);

	public List<Staff> getStaffByIdList(List<Long> idList, int firstResult, int maxResult, String orderByColumn,
			String orderByType);

	public int getStaffCount(String keyword);

	public int getStaffCount(String staffName, Long yearOfBirth, Department dept, String email, Job job, String phone);

	public int getStaffCountByIdList(List<Long> idList);

	public List<String> getTotalJobTitle();

	public List<WorkProcess> getWorkProcessByStaffId(Long staffId);

	public void lockStaff(Staff staff) throws Exception;

	public void saveOrUpdate(ContractType ct) throws Exception;

	public void saveOrUpdate(Job job) throws Exception;

	public void saveOrUpdate(SalaryLandmark salaryLm) throws Exception;

	public void saveOrUpdate(Staff staff) throws Exception;

	public void saveOrUpdate(WorkProcess wp) throws Exception;

	public void unlockStaff(Staff staff) throws Exception;
}
