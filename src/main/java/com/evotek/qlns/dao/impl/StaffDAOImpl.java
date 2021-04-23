/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

import com.evotek.qlns.dao.StaffDAO;
import com.evotek.qlns.model.Department;
import com.evotek.qlns.model.Job;
import com.evotek.qlns.model.Staff;
import com.evotek.qlns.util.DateUtil;
import com.evotek.qlns.util.LikeCriterionMaker;
import com.evotek.qlns.util.StaticUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author PC
 */
public class StaffDAOImpl extends AbstractDAO<Staff> implements StaffDAO {

    private static final Logger _log = LogManager.getLogger(StaffDAOImpl.class);

    @Override
	public Staff getStaff(Long staffId) {
        Staff result = null;

        try {
            Criteria cri = currentSession().createCriteria(Staff.class, "s");

            cri.createAlias("department", "d", CriteriaSpecification.LEFT_JOIN);
            cri.createAlias("job", "j", CriteriaSpecification.LEFT_JOIN);
            cri.createAlias("contractType", "ct", CriteriaSpecification.LEFT_JOIN);
            
            cri.add(Restrictions.eq("staffId", staffId));
            
            result = (Staff) cri.uniqueResult();
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return result;
    }
    
    @Override
	public List<Staff> getStaff(String keyword, int firstResult, int maxResult, 
            String orderByColumn, String orderByType) {
        List<Staff> results = new ArrayList<Staff>();

        try {
            Criteria cri = getStaffQuery(keyword, orderByColumn, 
                    orderByType);

            if (firstResult >= 0 && maxResult > 0) {
                cri.setFirstResult(firstResult);
                cri.setMaxResults(maxResult);
            }

            results = cri.list();

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return results;
    }

    @Override
	public int getStaffCount(String keyword) {
        int result = 0;

        try {
            Criteria cri = getStaffQuery(keyword, null, null);

            cri.setProjection(Projections.rowCount());

            result = (Integer) cri.uniqueResult();
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return result;
    }
    
    @Override
	public List<Staff> getStaff(String staffName, Long yearOfBirth, Department dept, 
            String email, Job job, String phone, int firstResult, int maxResult, 
            String orderByColumn, String orderByType){
        List<Staff> results = new ArrayList<Staff>();

        try {
            Criteria cri = getStaffQuery(staffName, yearOfBirth, dept, email, 
                    job, phone, orderByColumn, orderByType);

            if (firstResult >= 0 && maxResult > 0) {
                cri.setFirstResult(firstResult);
                cri.setMaxResults(maxResult);
            }

            results = cri.list();

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return results;
    }

    @Override
	public int getStaffCount(String staffName, Long yearOfBirth, Department dept, 
            String email, Job job, String phone){
        int result = 0;

        try {
            Criteria cri = getStaffQuery(staffName, yearOfBirth, dept, email, 
                    job, phone, null, null);

            cri.setProjection(Projections.rowCount());

            result = (Integer) cri.uniqueResult();
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return result;
    }
    
    @Override
	public List<Staff> getStaffByIdList(List<Long> idList, int firstResult, 
            int maxResult, String orderByColumn, String orderByType){
        List<Staff> results = new ArrayList<Staff>();

        try {
            Criteria cri = getStaffByIdListQuery(idList, orderByColumn, 
                    orderByType);

            if (firstResult >= 0 && maxResult > 0) {
                cri.setFirstResult(firstResult);
                cri.setMaxResults(maxResult);
            }

            results = cri.list();

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return results;
    }

    @Override
	public int getStaffCountByIdList(List<Long> idList){
        int result = 0;

        try {
            Criteria cri = getStaffByIdListQuery(idList, null, null);

            cri.setProjection(Projections.rowCount());

            result = (Integer) cri.uniqueResult();
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return result;
    }
    
    private Criteria getStaffQuery(String keyword, String orderByColumn,
            String orderByType) throws Exception {
        try {
            Criteria cri = currentSession().createCriteria(Staff.class);
            
            cri.createAlias("department", "d", CriteriaSpecification.LEFT_JOIN);
            cri.createAlias("job", "j", CriteriaSpecification.LEFT_JOIN);
            cri.createAlias("contractType", "ct", CriteriaSpecification.LEFT_JOIN);
            
            if (Validator.isNotNull(keyword)) {
               Disjunction disjunction = Restrictions.disjunction();

               disjunction.add(LikeCriterionMaker.ilike("staffName",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("d.deptName",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("j.jobTitle",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("permanentResidence",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("currentResidence",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("note",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("ct.contractTypeName",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("contractNumber",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("taxCode",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("insuranceBookNumber",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("paidPlace",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("levels",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("majors",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("college",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("identityCard",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("grantPlace",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("mobile",
                        keyword, MatchMode.ANYWHERE));
               disjunction.add(LikeCriterionMaker.ilike("email",
                        keyword, MatchMode.ANYWHERE));
               
               cri.add(disjunction);
            }

            if (Validator.isNotNull(orderByColumn)) {
                if(StringPool.ASC.equalsIgnoreCase(orderByType)){
                    cri.addOrder(Order.asc(orderByColumn));
                } else {
                    cri.addOrder(Order.desc(orderByColumn));
                }

            } else {
                cri.addOrder(Order.asc("staffName"));
            }
            
            return cri;
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);

            throw ex;
        }
    }
    
    private Criteria getStaffQuery(String staffName, Long yearOfBirth, 
            Department dept, String email, Job job, String phone, 
            String orderByColumn, String orderByType) throws Exception{
        try {
            Criteria cri = currentSession().createCriteria(Staff.class, "s");
            
            cri.createAlias("department", "d", CriteriaSpecification.LEFT_JOIN);
            cri.createAlias("job", "j", CriteriaSpecification.LEFT_JOIN);
            cri.createAlias("contractType", "ct", CriteriaSpecification.LEFT_JOIN);
            
            if (Validator.isNotNull(staffName)) {
                cri.add(LikeCriterionMaker.ilike("staffName",
                        staffName, MatchMode.ANYWHERE));
            }
            
            if (Validator.isNotNull(yearOfBirth)) {
                cri.add(Restrictions.sqlRestriction(" YEAR(date_of_birth) = ? ", 
                        yearOfBirth, LongType.INSTANCE));
            }
            
            if (Validator.isNotNull(dept) 
                    && Validator.isNotNull(dept.getDeptId())){
                cri.add(Restrictions.eq("d.deptId", dept.getDeptId()));
            }
            
            if (Validator.isNotNull(staffName)) {
                cri.add(LikeCriterionMaker.ilike("email",
                        email, MatchMode.ANYWHERE));
            }
            
            if (Validator.isNotNull(job) 
                    && Validator.isNotNull(job.getJobId())){
                cri.add(Restrictions.eq("j.jobId", job.getJobId()));
            }
            
            if (Validator.isNotNull(phone)) {
                Disjunction disjunction = Restrictions.disjunction();
                
                disjunction.add(LikeCriterionMaker.ilike("mobile",
                        phone, MatchMode.ANYWHERE));
                disjunction.add(LikeCriterionMaker.ilike("homePhone",
                        phone, MatchMode.ANYWHERE));
                
                cri.add(disjunction);
            }
            
            if (Validator.isNotNull(orderByColumn)) {
                if(StringPool.ASC.equalsIgnoreCase(orderByType)){
                    cri.addOrder(Order.asc(orderByColumn));
                } else {
                    cri.addOrder(Order.desc(orderByColumn));
                }

            } else {
                cri.addOrder(Order.asc("staffName"));
            }
            
            return cri;
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);

            throw ex;
        }
    }
    
    private Criteria getStaffByIdListQuery(List<Long> idList, String orderByColumn,
            String orderByType) throws Exception {
        try {
            Criteria cri = currentSession().createCriteria(Staff.class);
            
            cri.createAlias("department", "d", CriteriaSpecification.LEFT_JOIN);
            cri.createAlias("job", "j", CriteriaSpecification.LEFT_JOIN);
            cri.createAlias("contractType", "ct", CriteriaSpecification.LEFT_JOIN);
            
            if (Validator.isNotNull(idList)) {
                cri.add(Restrictions.in("d.deptId", idList));
            }

            if (Validator.isNotNull(orderByColumn)) {
                if(StringPool.ASC.equalsIgnoreCase(orderByType)){
                    cri.addOrder(Order.asc(orderByColumn));
                } else {
                    cri.addOrder(Order.desc(orderByColumn));
                }

            } else {
                cri.addOrder(Order.asc("staffName"));
            }
            
            return cri;
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);

            throw ex;
        }
    }

    @Override
	public void deleteAllStaff(List<Staff> staffs) throws Exception {
        deleteAll(staffs);
    }
    
    @Override
	public List<Staff> getContractExpiredStaff(){
        List<Staff> results = new ArrayList<Staff>();

        try {
            Criteria cri = currentSession().createCriteria(Staff.class);

            cri.add(Restrictions.isNotNull("contractToDate"));
            cri.add(Restrictions.le("contractToDate", 
                    DateUtil.getDateAfter(StaticUtil.NOTIFY_CONTRACT_EXPIRED_BEFORE_DAY)));
            cri.add(Restrictions.eq("status", Values.STATUS_ACTIVE));

            results = cri.list();

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return results;
    }
    
    @Override
	public List<Staff> getBirthDayNearlyStaff(int dayOfYear){
        List<Staff> results = new ArrayList<Staff>();

        try {
            int _day = StaticUtil.NOTIFY_BIRTHDAY_BEFORE_DAY;
            
            Criteria cri = currentSession().createCriteria(Staff.class);
            
            cri.add(Restrictions.isNotNull("dateOfBirth"));
            cri.add(Restrictions.sqlRestriction(_buildBirthDayNearlyQuery(),
                    new Object[]{
                        dayOfYear + _day - 1, dayOfYear + _day + 1,
                        dayOfYear, dayOfYear + _day},
                    new Type[]{
                        IntegerType.INSTANCE, IntegerType.INSTANCE,
                        IntegerType.INSTANCE, IntegerType.INSTANCE
                    }));
            cri.add(Restrictions.eq("status", Values.STATUS_ACTIVE));

            results = cri.list();

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return results;
    }
    
    private String _buildBirthDayNearlyQuery(){
        StringBuilder sb = new StringBuilder();
        
        sb.append("((MOD(YEAR(date_of_birth),4)=0 or MOD(YEAR(date_of_birth),400)=0)");
        sb.append(" and DAYOFYEAR(date_of_birth)>=? and DAYOFYEAR(date_of_birth)<=?)");
        sb.append(" or ((MOD(YEAR(date_of_birth),4)>0 and MOD(YEAR(date_of_birth),400)>0)");
        sb.append(" and DAYOFYEAR(date_of_birth)>=? and DAYOFYEAR(date_of_birth)<=?)");
        
        return sb.toString();
    }
}
