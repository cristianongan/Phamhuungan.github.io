/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evotek.qlns.dao.CategoryDAO;
import com.evotek.qlns.dao.NotificationDAO;
import com.evotek.qlns.model.Category;
import com.evotek.qlns.model.User;
import com.evotek.qlns.service.MainService;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author linhlh2
 */
@Service
@Transactional
public class MainServiceImpl implements MainService {

	private static final Logger _log = LogManager.getLogger(MainServiceImpl.class);

	@Autowired
	private transient CategoryDAO categoryDAO;

	@Autowired
	private transient NotificationDAO notificationDAO;

	@Override
	@Cacheable(value="allCategory", keyGenerator="customKeyGenerator", sync=true)
	public List<Category> getAllCategory() throws Exception {
		return this.categoryDAO.getAllCategory();
	}

	@Override
	public int getBirthDayCount() {
		return this.notificationDAO.getNotificationCountByT_S_E(Values.NOTI_BIRTHDAY, Values.STATUS_ACTIVE, false);
	}

	@Override
	public Category getCategoryById(Long categoryId) throws Exception {
		return this.categoryDAO.getCategoryById(categoryId);
	}

	@Override
	public List<Category> getCategoryByUser(User user) throws Exception {
		return this.categoryDAO.getCategoryByUser(user);
	}

	@Override
	public int getContractExpiredCount() {
		return this.notificationDAO.getNotificationCountByT_S(Values.NOTI_CONTRACT_EXPIRED, Values.STATUS_ACTIVE);
	}
}
