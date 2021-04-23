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

    public List<Job> getJobTitle();

    public void delete(Job job);

    public void saveOrUpdate(Job job);

    public List<String> getJobTitleOnly();
    
}
