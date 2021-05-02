/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.model.list;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evotek.qlns.model.Department;
import com.evotek.qlns.model.Job;
import com.evotek.qlns.model.Staff;
import com.evotek.qlns.service.StaffService;

/**
 *
 * @author LinhLH
 */
public class StaffListModel extends AbstractModelList<Staff> {
	private static final long serialVersionUID = -1291502995857939565L;

	private static final Logger _log = LogManager.getLogger(StaffListModel.class);

	private Department _dept;
	private String _email;
	private List<Long> _idList;
	boolean _isAdvance;
	private Job _job;
	private String _keyword;

	private String _phone;
	private String _staffName;
	private StaffService _staffService;

	private boolean _view;

	private Long _yearOfBirth;

	public StaffListModel(int pageSize, String keyword, boolean isAdvance, boolean view, List<Long> idList,
			StaffService staffService) {
		this._pageSize = pageSize;
		this._keyword = keyword;
		this._isAdvance = isAdvance;
		this._idList = idList;
		this._view = view;
		this._staffService = staffService;

		setMultiple(true);
	}

	public StaffListModel(int pageSize, String staffName, Long yearOfBirth, Department dept, String email, Job job,
			String phone, boolean isAdvance, boolean view, List<Long> idList, StaffService staffService) {
		this._pageSize = pageSize;
		this._staffName = staffName;
		this._yearOfBirth = yearOfBirth;
		this._dept = dept;
		this._email = email;
		this._job = job;
		this._phone = phone;
		this._isAdvance = isAdvance;
		this._view = view;
		this._idList = idList;
		this._staffService = staffService;
	}

	@Override
	public int getSize() {
		if (this._cachedSize < 0) {
			try {
				if (!this._view) {
					if (this._isAdvance) {
						this._cachedSize = this._staffService.getStaffCount(this._staffName, this._yearOfBirth,
								this._dept, this._email, this._job, this._phone);
					} else {
						this._cachedSize = this._staffService.getStaffCount(this._keyword);
					}
				} else {
					this._cachedSize = this._staffService.getStaffCountByIdList(this._idList);
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
			if (!this._view) {
				if (this._isAdvance) {
					this._cache = this._staffService.getStaff(this._staffName, this._yearOfBirth, this._dept,
							this._email, this._job, this._phone, itemStartNumber, pageSize, this._orderByColumn,
							this._orderByType);
				} else {
					this._cache = this._staffService.getStaff(this._keyword, itemStartNumber, pageSize,
							this._orderByColumn, this._orderByType);
				}
			} else {
				this._cache = this._staffService.getStaffByIdList(this._idList, itemStartNumber, pageSize,
						this._orderByColumn, this._orderByType);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}
}
