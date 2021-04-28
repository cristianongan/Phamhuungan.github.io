/*
 * Copyright 2013 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.model.list;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evotek.qlns.service.UserService;

/**
 *
 * @author hungnt81
 */
public class UserListModel<User> extends AbstractModelList {
	private static final Logger _log = LogManager.getLogger(UserListModel.class);
	private String _account;
	private Date _birthdayFrom;
	private Date _birthdayTo;
	private String _birthPlace;
	private String _email;
	private Long _gender;
	private boolean _isAdvance;
	private String _keyword;
	private String _mobile;

	private String _phone;
	private Long _status;

	private String _userName;

	private UserService _userService;

	public UserListModel(int pageSize, String keyword, boolean isAdvance, UserService userService) {
		this._pageSize = pageSize;
		this._keyword = keyword;
		this._isAdvance = isAdvance;
		this._userService = userService;

		setMultiple(true);
	}

	public UserListModel(int pageSize, String userName, String email, Long gender, String birthPlace, Date birthdayFrom,
			Date birthdayTo, String phone, String mobile, String account, Long status, boolean isAdvance,
			UserService userService) {
		this._pageSize = pageSize;
		this._userName = userName;
		this._email = email;
		this._gender = gender;
		this._birthPlace = birthPlace;
		this._birthdayFrom = birthdayFrom;
		this._birthdayTo = birthdayTo;
		this._phone = phone;
		this._mobile = mobile;
		this._account = account;
		this._status = status;
		this._isAdvance = isAdvance;

		this._userService = userService;

		setMultiple(true);
	}

	@Override
	public int getSize() {
		if (this._cachedSize < 0) {
			try {
				if (this._isAdvance) {
					this._cachedSize = this._userService.getUsersCount(this._userName, this._email, this._gender,
							this._birthPlace, this._birthdayFrom, this._birthdayTo, this._phone, this._mobile,
							this._account, this._status);
				} else {
					this._cachedSize = this._userService.getUsersCount(this._keyword);
				}

			} catch (Exception ex) {
				_log.error(ex.getMessage(), ex);
			}
		}
		return this._cachedSize;
	}

	@Override
	public void loadToCache(int itemStartNumber, int pageSize) {
		try {
			if (this._isAdvance) {
				this._cache = this._userService.getUsers(this._userName, this._email, this._gender, this._birthPlace,
						this._birthdayFrom, this._birthdayTo, this._phone, this._mobile, this._account, this._status,
						itemStartNumber, pageSize, this._orderByColumn, this._orderByType);
			} else {
				this._cache = this._userService.getUsers(this._keyword, itemStartNumber, pageSize, this._orderByColumn,
						this._orderByType);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}
}
