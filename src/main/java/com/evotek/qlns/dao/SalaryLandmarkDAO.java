/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao;

import java.util.List;

import com.evotek.qlns.model.SalaryLandmark;

/**
 *
 * @author linhlh2
 */
public interface SalaryLandmarkDAO {

    public List<SalaryLandmark> getSalaryLandmarkByStaffId(Long staffId);

    public void delete(SalaryLandmark salaryLm);

    public void saveOrUpdate(SalaryLandmark salaryLm);
    
}
