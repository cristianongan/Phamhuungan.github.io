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

import com.evotek.qlns.dao.ContractTypeDAO;
import com.evotek.qlns.dao.DepartmentDAO;
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
 * @author PC
 */
public class StaffServiceImpl implements StaffService {

    private transient StaffDAO staffDAO;
    private transient DepartmentDAO departmentDAO;
    private transient JobDAO jobDAO;
    private transient ContractTypeDAO contractTypeDAO;
    private transient SalaryLandmarkDAO salaryLandmarkDAO;
    private transient WorkProcessDAO workProcessDAO;

    public StaffDAO getStaffDAO() {
        return staffDAO;
    }

    public void setStaffDAO(StaffDAO staffDAO) {
        this.staffDAO = staffDAO;
    }

    public DepartmentDAO getDepartmentDAO() {
        return departmentDAO;
    }

    public void setDepartmentDAO(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    public JobDAO getJobDAO() {
        return jobDAO;
    }

    public void setJobDAO(JobDAO jobDAO) {
        this.jobDAO = jobDAO;
    }

    public ContractTypeDAO getContractTypeDAO() {
        return contractTypeDAO;
    }

    public void setContractTypeDAO(ContractTypeDAO contractTypeDAO) {
        this.contractTypeDAO = contractTypeDAO;
    }

    public SalaryLandmarkDAO getSalaryLandmarkDAO() {
        return salaryLandmarkDAO;
    }

    public void setSalaryLandmarkDAO(SalaryLandmarkDAO salaryLandmarkDAO) {
        this.salaryLandmarkDAO = salaryLandmarkDAO;
    }

    public WorkProcessDAO getWorkProcessDAO() {
        return workProcessDAO;
    }

    public void setWorkProcessDAO(WorkProcessDAO workProcessDAO) {
        this.workProcessDAO = workProcessDAO;
    }

    public Staff getStaff(Long staffId){
        return staffDAO.getStaff(staffId);
    }
    
    public List<Staff> getStaff(String keyword, int firstResult, 
            int maxResult, String orderByColumn, String orderByType) {
        return staffDAO.getStaff(keyword, firstResult, 
                maxResult, orderByColumn, orderByType);
    }
    
    public int getStaffCount(String keyword) {
        return staffDAO.getStaffCount(keyword);
    }

    public List<Staff> getStaff(String staffName, Long yearOfBirth, 
            Department dept, String email, Job job, String phone, 
            int firstResult, int maxResult, String orderByColumn, 
            String orderByType){
        return staffDAO.getStaff(staffName, yearOfBirth, dept, email, job, phone, 
                firstResult, maxResult, orderByColumn, orderByType);
    }
    
    public int getStaffCount(String staffName, Long yearOfBirth, Department dept, 
            String email, Job job, String phone){
        return staffDAO.getStaffCount(staffName, yearOfBirth, dept, email, job, phone);
    }
    
    public List<Staff> getStaffByIdList(List<Long> idList, int firstResult, 
            int maxResult, String orderByColumn, String orderByType){
        return staffDAO.getStaffByIdList(idList, firstResult, maxResult, 
                orderByColumn, orderByType);
    }
    
    public int getStaffCountByIdList(List<Long> idList){
        return staffDAO.getStaffCountByIdList(idList);
    }
    
    public void lockStaff(Staff staff) throws Exception {
        try{
            staff.setStatus(Values.STATUS_DEACTIVE);

            staffDAO.saveOrUpdate(staff);
        }catch(Exception ex){
            _log.error(ex.getMessage(), ex);
        }
    }

    public void unlockStaff(Staff staff) throws Exception {
        try{
            staff.setStatus(Values.STATUS_ACTIVE);

            staffDAO.saveOrUpdate(staff);
        }catch(Exception ex){
            _log.error(ex.getMessage(), ex);
        }
    }

    public void deleteStaff(Staff staff) throws Exception {
        try{
            Long status = staff.getStatus();

            if(!Values.STATUS_ACTIVE.equals(status)){
                staffDAO.delete(staff);
            }
        }catch(Exception ex){
            _log.error(ex.getMessage(), ex);
        }
    }
    
    public void deleteJob(Job job) throws Exception {
        jobDAO.delete(job);
    }
    
    public void deleteContractType(ContractType ct) throws Exception {
        contractTypeDAO.delete(ct);
    }
    
    public void saveOrUpdate(Staff staff) throws Exception {
        staffDAO.saveOrUpdate(staff);
    }

    public void saveOrUpdate(Job job) throws Exception {
        jobDAO.saveOrUpdate(job);
    }
    
    public void saveOrUpdate(ContractType ct) throws Exception {
        contractTypeDAO.saveOrUpdate(ct);
    }
    
    public void saveOrUpdate(SalaryLandmark salaryLm) throws Exception{
        salaryLandmarkDAO.saveOrUpdate(salaryLm);
    }
    
    public void saveOrUpdate(WorkProcess wp) throws Exception {
        workProcessDAO.saveOrUpdate(wp);
    }
    
    public void deleteAll(List<Staff> staff) throws Exception {
        staffDAO.deleteAllStaff(staff);
    }
    
    public List<Job> getJobTitle() {
        return jobDAO.getJobTitle();
    }
    
    public List<ContractType> getContract(){
        return contractTypeDAO.getContract();
    }
    
    public List<SalaryLandmark> getSalaryLandmarkByStaffId(Long staffId){
        return salaryLandmarkDAO.getSalaryLandmarkByStaffId(staffId);
    }
    
    public void deleteSalaryLm(SalaryLandmark salaryLm){
        salaryLandmarkDAO.delete(salaryLm);
    }
    
    public List<WorkProcess> getWorkProcessByStaffId(Long staffId){
        return workProcessDAO.getWorkProcessByStaffId(staffId);
    }
    
    public void deleteWorkProcess(WorkProcess wp){
        workProcessDAO.delete(wp);
    }
    
    public List<String> getCompanyName(){
        return workProcessDAO.getCompanyName();
    }
    
    public List<String> getTotalJobTitle() {
        List<String> _l1 = jobDAO.getJobTitleOnly();
        List<String> _l2 = workProcessDAO.getJobTitle();
        
        Set<String> _s = new HashSet<String>(_l1);
        
        _s.addAll(_l2);
        
        return new ArrayList<String>(_s);
    }
    
    private static final Logger _log =
            LogManager.getLogger(StaffServiceImpl.class);

}
