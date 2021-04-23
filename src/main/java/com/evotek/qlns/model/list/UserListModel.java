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
public class UserListModel<User> extends AbstractModelList{
    private String _userName;
    private String _email;
    private Long _gender;
    private String _birthPlace;
    private Date _birthdayFrom;
    private Date _birthdayTo;
    private String _phone;
    private String _mobile;
    private String _account;
    private Long _status;

    private String _keyword;
    private boolean _isAdvance;

    private UserService _userService;

    public UserListModel(int pageSize, String userName, String email,
            Long gender, String birthPlace, Date birthdayFrom, Date birthdayTo,
            String phone, String mobile, String account, Long status,
            boolean isAdvance, UserService userService) {
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

    public UserListModel(int pageSize, String keyword, boolean isAdvance,
            UserService userService) {
        this._pageSize = pageSize;
        this._keyword = keyword;
        this._isAdvance = isAdvance;
        this._userService = userService;

        setMultiple(true);
    }

    public void loadToCache(int itemStartNumber, int pageSize) {
        try {
            if (_isAdvance) {
            _cache = _userService.getUsers(_userName, _email, _gender,
                    _birthPlace, _birthdayFrom, _birthdayTo, _phone, _mobile,
                    _account, _status, itemStartNumber, pageSize,
                    _orderByColumn, _orderByType);
            } else{
                _cache = _userService.getUsers(_keyword,
                        itemStartNumber, pageSize, _orderByColumn, _orderByType);
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public int getSize() {
        if (_cachedSize < 0) {
            try {
                if (_isAdvance) {
                     _cachedSize = _userService.getUsersCount(_userName,
                             _email, _gender, _birthPlace, _birthdayFrom,
                             _birthdayTo, _phone, _mobile, _account, _status);
                } else{
                    _cachedSize = _userService.getUsersCount(_keyword);
                }
               
            } catch (Exception ex) {
                _log.error(ex.getMessage(), ex);
            }
        }
        return _cachedSize;
    }

    private static final Logger _log =
            LogManager.getLogger(UserListModel.class);
}
