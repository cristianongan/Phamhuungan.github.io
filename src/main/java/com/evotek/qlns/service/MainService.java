/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.service;

import java.util.List;

import com.evotek.qlns.model.Category;
import com.evotek.qlns.model.User;

/**
 *
 * @author linhlh2
 */
public interface MainService {
	public List<Category> getAllCategory() throws Exception;

	public int getBirthDayCount();

	public Category getCategoryById(Long categoryId) throws Exception;

	public List<Category> getCategoryByUser(User user) throws Exception;

	public int getContractExpiredCount();
}
