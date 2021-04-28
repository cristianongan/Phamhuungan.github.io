/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao;

import java.util.List;

import com.evotek.qlns.model.Job;

/**
 *
 * @author My PC
 */
public interface JobDAO {

	public void delete(Job job);

	public List<Job> getJobTitle();

	public List<String> getJobTitleOnly();

	public void saveOrUpdate(Job job);

}
