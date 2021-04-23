/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.evotek.qlns.dao.DepartmentDAO;
import com.evotek.qlns.model.Department;
import com.evotek.qlns.util.QueryUtil;
import com.evotek.qlns.util.Validator;

/**
 *
 * @author My PC
 */
public class DepartmentDAOImpl extends AbstractDAO<Department> implements 
        DepartmentDAO{
    @Override
	public List<Department> getDepartmentByParentId(Long parentId){
        List<Department> results = new ArrayList<Department>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(Department.class);

            if (Validator.isNull(parentId)) {
                cri.add(Restrictions.isNull("parentId"));
            } else {
                cri.add(Restrictions.eq("parentId", parentId));
            }

            cri.addOrder(Order.asc("ordinal"));

            results = cri.list();

        } catch (HibernateException e) {
            _log.error(e.getMessage(), e);
        }

        return results;
    }
    
    @Override
	public int delete(List<Long> deptIds){
        int result = 0;
        
        try{
            if(!deptIds.isEmpty()){
                //delete Department
                String query = "delete Department where deptId in (:deptIds)";
                
                Query q = currentSession().createQuery(query);
                
                q.setParameterList("deptIds", deptIds);
                
                result = q.executeUpdate();
            }
        } catch (HibernateException e) {
            _log.error(e.getMessage(), e);
        }
        
        return result;
    }
    
    @Override
	public void updateOrdinal(Long parentId, Long deletedIndex){
        try {
            Map params = new HashMap();
            
            StringBuilder sb = new StringBuilder();
            
            sb.append("update Department set ordinal = ordinal - 1 where ");
            
            if(Validator.isNull(parentId)){
                sb.append(" parentId is null ");
            } else {
                sb.append(" parentId=:parentId ");
                
                params.put("parentId", parentId);
            }
            
            sb.append(" and  ordinal>:deletedIndex");
            
            params.put("deletedIndex", deletedIndex);
            
            Query q = currentSession().createQuery(sb.toString());
            
            QueryUtil.setParamerterMap(q, params);
            
            q.executeUpdate();
        } catch (HibernateException e) {
            _log.error(e.getMessage(), e);
        }
    }
    
    @Override
	public Department get(Long deptId){
        return get(Department.class, deptId);
    }
    
    @Override
	public Long getNextOrdinal(Long parentId){
        Long index = 0L;
        
        try {
            StringBuilder sb = new StringBuilder();
            
            sb.append("select max(ordinal) from Department where ");
            
            if(Validator.isNull(parentId)){
                sb.append(" parentId is null ");
            } else {
                sb.append(" parentId=:parentId ");
            }
            
            Query q = currentSession().createQuery(sb.toString());
            
            if(Validator.isNotNull(parentId)){
                q.setParameter("parentId", parentId);
            }
            
            List results = q.list();
            
            if(Validator.isNotNull(results)){
                index = (Long) results.get(0) +1;
            }
        }catch (HibernateException e) {
            _log.error(e.getMessage(), e);
        }
        
        return index;
    }
    
    private static final Logger _log = LogManager.getLogger(DepartmentDAOImpl.class);
}
