/*
 * Copyright 2013 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.evotek.qlns.model.User;

/**
 *
 * @author hungnt1 LinhLH2 fixed
 */
public interface UserDAO {

	/**
	 * EN: Deletes an User.<br>
	 */
	public void delete(User user) throws DataAccessException;

	/**
	 * EN: Get a list of all Users.<br>
	 *
	 * @return List of Users / Liste aus Usern
	 */
	public List<User> getAllUsers() throws Exception;

	/**
	 * EN: Get the count of all Users.<br>
	 *
	 * @return int
	 */
	public int getCountAllUsers() throws Exception;

	public List<User> getListUsers() throws Exception;

	public User getNewUser();

	/**
	 * EN: Get an User by its ID.<br>
	 *
	 * @param id / the persistence identifier / der PrimaerKey
	 * @return User/ User
	 */
	public User getUserById(Long userId);

	/**
	 * EN: Get an User by its LoginName.<br>
	 *
	 * @param userName UserName / User Name
	 * @return User/ User
	 */
	public User getUserByUserName(final String userName) throws Exception;

	public List<User> getUsers(String keyword, int itemStartNumber, int pageSize, String orderByColumn,
			String orderByType);

	public List<User> getUsers(String userName, String email, Long gender, String birthPlace, Date birthdayFrom,
			Date birthdayTo, String phone, String mobile, String account, Long status, int itemStartNumber,
			int pageSize, String orderByColumn, String orderByType);

	public List<User> getUsersByI_E(Long userId, String email);

	public List<User> getUsersByI_UN(Long userId, String userName);

	public int getUsersCount(String keyword);

	public int getUsersCount(String userName, String email, Long gender, String birthPlace, Date birthdayFrom,
			Date birthdayTo, String phone, String mobile, String account, Long status);

	/**
	 * EN: Get a list of Users by its emailaddress with the like SQL operator.<br>
	 *
	 * @param email Email Address / Email Adresse
	 * @return List of Users
	 */
	public List<User> getUsersLikeEmail(String value) throws Exception;

	/**
	 * EN: Gets a list of Users where the LastName name contains the %string% .<br>
	 *
	 * @param string LastName / LastName
	 * @return List of Users / Liste of Users
	 */
	public List<User> getUsersLikeLastname(String value) throws Exception;

	/**
	 * EN: Gets a list of Users where the LoginName contains the %string% .<br>
	 *
	 * @param string LoginName / LoginName
	 * @return List of Users / Liste of Users
	 */
	public List<User> getUsersLikeUserName(String value) throws Exception;

	/**
	 * EN: Saves new or updates an User.<br>
	 */
	public void saveOrUpdate(User user) throws DataAccessException;

	public void saveOrUpdateAll(Collection<User> users) throws DataAccessException;
}
