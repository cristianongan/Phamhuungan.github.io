/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.evotek.qlns.dao.ContractTypeDAO;
import com.evotek.qlns.model.ContractType;

/**
 *
 * @author LinhLH
 */
@Repository
public class ContractTypeDAOImpl extends AbstractDAO<ContractType> implements 
        ContractTypeDAO{

    @Override
	public List<ContractType> getContract() {
        List<ContractType> results = new ArrayList<ContractType>();

        try {
            Session session = getCurrentSession();
            
            CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ContractType> criteria = builder.createQuery(ContractType.class);

			Root<ContractType> root = criteria.from(ContractType.class);

			criteria.select(root);
			
			criteria.orderBy(builder.asc(root.get("contractTypeName")));

            results = session.createQuery(criteria).getResultList();

        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return results;
    }
    
    private static final Logger _log = LogManager.getLogger(ContractTypeDAOImpl.class);
}
