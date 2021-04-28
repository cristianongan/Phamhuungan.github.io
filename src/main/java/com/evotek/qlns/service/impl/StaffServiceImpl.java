/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evotek.qlns.dao.ContractTypeDAO;
import com.evotek.qlns.dao.JobDAO;
import com.evotek.qlns.dao.SalaryLandmarkDAO;
import com.evotek.qlns.dao.StaffDAO;
import com.evotek.qlns.dao.WorkProcessDAO;
import com.evotek.qlns.model.ContractType;
import com.evotek.qlns.model.Department;
import com.evotek.qlns.model.Job;
import com.evotek.qlns.model.SalaryLandmark;
import com.evotek.qlns.model.Staff;
import com.evotek.qlns.model.WorkProcess;
import com.evotek.qlns.service.StaffService;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author LinhLH
 */
@Service
@Transactional
public class StaffServiceImpl implements StaffService {

	private static final Logger _log = LogManager.getLogger(StaffServiceImpl.class);

	@Autowired
	private ContractTypeDAO contractTypeDAO;

	@Autowired
	private JobDAO jobDAO;

	@Autowired
	private SalaryLandmarkDAO salaryLandmarkDAO;

	@Autowired
	private StaffDAO staffDAO;

	@Autowired
	private WorkProcessDAO workProcessDAO;

	@Override
	public void deleteAll(List<Staff> staff) throws Exception {
		this.staffDAO.deleteAllStaff(staff);
	}

	@Override
	public void deleteContractType(ContractType ct) throws Exception {
		this.contractTypeDAO.delete(ct);
	}

	@Override
	public void deleteJob(Job job) throws Exception {
		this.jobDAO.delete(job);
	}

	@Override
	public void deleteSalaryLm(SalaryLandmark salaryLm) {
		this.salaryLandmarkDAO.delete(salaryLm);
	}

	@Override
	public void deleteStaff(Staff staff) throws Exception {
		try {
			Long status = staff.getStatus();

			if (!Values.STATUS_ACTIVE.equals(status)) {
				this.staffDAO.delete(staff);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public void deleteWorkProcess(WorkProcess wp) {
		this.workProcessDAO.delete(wp);
	}

	@Override
	public List<String> getCompanyName() {
		return this.workProcessDAO.getCompanyName();
	}

	@Override
	public List<ContractType> getContract() {
		return this.contractTypeDAO.getContract();
	}

	@Override
	public List<Job> getJobTitle() {
		return this.jobDAO.getJobTitle();
	}

	@Override
	public List<SalaryLandmark> getSalaryLandmarkByStaffId(Long staffId) {
		return this.salaryLandmarkDAO.getSalaryLandmarkByStaffId(staffId);
	}

	@Override
	public Staff getStaff(Long staffId) {
		return this.staffDAO.getStaff(staffId);
	}

	@Override
	public List<Staff> getStaff(String keyword, int firstResult, int maxResult, String orderByColumn,
			String orderByType) {
		return this.staffDAO.getStaff(keyword, firstResult, maxResult, orderByColumn, orderByType);
	}

	@Override
	public List<Staff> getStaff(String staffName, Long yearOfBirth, Department dept, String email, Job job,
			String phone, int firstResult, int maxResult, String orderByColumn, String orderByType) {
		return this.staffDAO.getStaff(staffName, yearOfBirth, dept, email, job, phone, firstResult, maxResult,
				orderByColumn, orderByType);
	}

	@Override
	public List<Staff> getStaffByIdList(List<Long> idList, int firstResult, int maxResult, String orderByColumn,
			String orderByType) {
		return this.staffDAO.getStaffByIdList(idList, firstResult, maxResult, orderByColumn, orderByType);
	}

	@Override
	public int getStaffCount(String keyword) {
		return this.staffDAO.getStaffCount(keyword);
	}

	@Override
	public int getStaffCount(String staffName, Long yearOfBirth, Department dept, String email, Job job, String phone) {
		return this.staffDAO.getStaffCount(staffName, yearOfBirth, dept, email, job, phone);
	}

	@Override
	public int getStaffCountByIdList(List<Long> idList) {
		return this.staffDAO.getStaffCountByIdList(idList);
	}

	@Override
	public List<String> getTotalJobTitle() {
		List<String> _l1 = this.jobDAO.getJobTitleOnly();
		List<String> _l2 = this.workProcessDAO.getJobTitle();

		Set<String> _s = new HashSet<String>(_l1);

		_s.addAll(_l2);

		return new ArrayList<String>(_s);
	}

	@Override
	public List<WorkProcess> getWorkProcessByStaffId(Long staffId) {
		return this.workProcessDAO.getWorkProcessByStaffId(staffId);
	}

	@Override
	public void lockStaff(Staff staff) throws Exception {
		try {
			staff.setStatus(Values.STATUS_DEACTIVE);

			this.staffDAO.saveOrUpdate(staff);
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public void saveOrUpdate(ContractType ct) throws Exception {
		this.contractTypeDAO.saveOrUpdate(ct);
	}

	@Override
	public void saveOrUpdate(Job job) throws Exception {
		this.jobDAO.saveOrUpdate(job);
	}

	@Override
	public void saveOrUpdate(SalaryLandmark salaryLm) throws Exception {
		this.salaryLandmarkDAO.saveOrUpdate(salaryLm);
	}

	@Override
	public void saveOrUpdate(Staff staff) throws Exception {
		this.staffDAO.saveOrUpdate(staff);
	}

	@Override
	public void saveOrUpdate(WorkProcess wp) throws Exception {
		this.workProcessDAO.saveOrUpdate(wp);
	}

	@Override
	public void unlockStaff(Staff staff) throws Exception {
		try {
			staff.setStatus(Values.STATUS_ACTIVE);

			this.staffDAO.saveOrUpdate(staff);
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

}
