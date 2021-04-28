/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao;

import java.util.List;

import com.evotek.qlns.model.WorkProcess;

/**
 *
 * @author My PC
 */
public interface WorkProcessDAO {

	public void delete(WorkProcess wp);

	public List<String> getCompanyName();

	public List<String> getJobTitle();

	public List<WorkProcess> getWorkProcessByStaffId(Long staffId);

	public void saveOrUpdate(WorkProcess wp);

}
