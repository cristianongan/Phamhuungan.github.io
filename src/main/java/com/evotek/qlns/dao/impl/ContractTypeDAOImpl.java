/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import com.evotek.qlns.dao.ContractTypeDAO;
import com.evotek.qlns.model.ContractType;

/**
 *
 * @author My PC
 */
public class ContractTypeDAOImpl extends AbstractDAO<ContractType> implements 
        ContractTypeDAO{

    @Override
	public List<ContractType> getContract() {
        List<ContractType> results = new ArrayList<ContractType>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(ContractType.class);

            cri.addOrder(Order.asc("contractTypeName"));

            results = cri.list();

        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return results;
    }
    
    private static final Logger _log = LogManager.getLogger(ContractTypeDAOImpl.class);
}
