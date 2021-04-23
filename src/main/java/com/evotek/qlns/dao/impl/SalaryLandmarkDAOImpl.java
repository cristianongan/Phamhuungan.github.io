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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.evotek.qlns.dao.SalaryLandmarkDAO;
import com.evotek.qlns.model.SalaryLandmark;
import com.evotek.qlns.util.Validator;

/**
 *
 * @author linhlh2
 */
public class SalaryLandmarkDAOImpl extends AbstractDAO<SalaryLandmark>
        implements SalaryLandmarkDAO {

    private static final Logger _log = LogManager.getLogger(SalaryLandmarkDAOImpl.class);

    @Override
	public List<SalaryLandmark> getSalaryLandmarkByStaffId(Long staffId) {
        List<SalaryLandmark> results = new ArrayList<SalaryLandmark>();

        try {
            Criteria cri = currentSession().createCriteria(SalaryLandmark.class);

            if (Validator.isNotNull(staffId)) {
                cri.add(Restrictions.eq("staffId", staffId));
            }

            cri.addOrder(Order.desc("fromDate"));
            cri.addOrder(Order.desc("toDate"));
            
            results = cri.list();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return results;
    }
}
