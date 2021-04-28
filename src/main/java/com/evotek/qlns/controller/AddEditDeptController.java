/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.A;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.extend.Messagebox;
import com.evotek.qlns.model.Department;
import com.evotek.qlns.service.DepartmentService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author linhlh2
 */
@Controller
public class AddEditDeptController extends BasicController<Window> implements Serializable {

	private static final long serialVersionUID = -163604506317766020L;

	private static final Logger _log = LogManager.getLogger(AddEditDeptController.class);

	@Autowired
	private DepartmentService departmentService;
	// Bandbox documentType
	private Bandbox bbDept;

	private A btnClearDoc;
	// Bandbox documentType

	private Department dept;

	private Include icDepartment;

	private Department oldParentDept;

	private Department parentDept;
	private Textbox tbDeptName;
	private Textbox tbDescription;

	private Window winEditDept;

	private Window winParent;

	private void _setEditForm() throws Exception {
		this.tbDeptName.setValue(this.dept.getDeptName());
		this.tbDescription.setValue(this.dept.getDescription());

		if (Validator.isNotNull(this.oldParentDept)) {
			this.bbDept.setValue(this.oldParentDept.getDeptName());
			this.bbDept.setAttribute(Constants.ID, this.oldParentDept.getDeptId());
			this.bbDept.setAttribute(Constants.OBJECT, this.oldParentDept);
			this.btnClearDoc.setVisible(true);
		}
	}

	public boolean _validate(String deptName, String description) {
		if (Validator.isNull(deptName)) {
			this.tbDeptName.setErrorMessage(Values.getRequiredInputMsg(Labels.getLabel(LanguageKeys.DEPARMENT_NAME)));

			this.tbDeptName.setFocus(true);

			return false;
		}

		if (deptName.length() > Values.LONG_LENGTH) {
			this.tbDeptName.setErrorMessage(Values
					.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.DOCUMENT_TYPE_NAME), Values.LONG_LENGTH));

			this.tbDeptName.setFocus(true);

			return false;
		}

		if (Validator.isNotNull(description) && description.length() > Values.GREATE_LONG_LENGTH) {
			this.tbDescription.setErrorMessage(Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.DESCRIPTION),
					Values.GREATE_LONG_LENGTH));

			return false;
		}

		return true;
	}

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		initData();
	}

	@Override
	public void doBeforeComposeChildren(Window comp) throws Exception {
		super.doBeforeComposeChildren(comp);

		this.winEditDept = comp;
	}

	private Long getOrdinal() {
		if (Validator.isNull(this.parentDept)) {
			return this.departmentService.getNextOrdinal(null);
		} else {
			return this.departmentService.getNextOrdinal(this.parentDept.getDeptId());
		}
	}

	// init data
	public void initData() throws Exception {
		try {
			this.winParent = (Window) this.arg.get(Constants.PARENT_WINDOW);

			this.dept = (Department) this.arg.get(Constants.OBJECT);
			this.parentDept = (Department) this.arg.get(Constants.SECOND_OBJECT);

			if (Validator.isNotNull(this.parentDept)) {
				this.bbDept.setValue(this.parentDept.getDeptName());
				this.bbDept.setAttribute(Constants.ID, this.parentDept.getDeptId());
				this.bbDept.setAttribute(Constants.OBJECT, this.parentDept);
			}

			if (Validator.isNotNull(this.dept)) {
				this.winEditDept.setTitle((String) this.arg.get(Constants.TITLE));

				if (Validator.isNotNull(this.dept.getParentId())) {
					this.oldParentDept = this.departmentService.getDeparment(this.dept.getParentId());
				}

				this._setEditForm();
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	public void onClick$btnCancel() {
		this.winEditDept.detach();
	}

	// Bandbox documentType
	public void onClick$btnClearDoc() {
		this.bbDept.setValue(StringPool.BLANK);
		this.bbDept.setAttribute(Constants.ID, null);

		this.btnClearDoc.setDisabled(true);
		this.btnClearDoc.setVisible(false);
	}

	public void onClick$btnSave() throws Exception {
		boolean update = true;

		try {
			String deptName = GetterUtil.getString(this.tbDeptName.getValue());
			String description = GetterUtil.getString(this.tbDescription.getValue());
			this.parentDept = (Department) this.bbDept.getAttribute(Constants.OBJECT);

			if (this._validate(deptName, description)) {
				if (Validator.isNull(this.dept)) {
					update = false;

					this.dept = new Department();

					this.dept.setUserId(getUserId());
					this.dept.setUserName(getUserName());
					this.dept.setCreateDate(new Date());
					this.dept.setOrdinal(getOrdinal());
				}

				this.dept.setDeptName(deptName);
				this.dept.setDescription(description);
				this.dept.setModifiedDate(new Date());
				this.dept.setParentId(Validator.isNull(this.parentDept) ? null : this.parentDept.getDeptId());

				this.departmentService.saveOrUpdate(this.dept);

				ComponentUtil.createSuccessMessageBox(ComponentUtil.getSuccessKey(update));

				this.winEditDept.detach();

				Events.sendEvent("onLoadData", this.winParent, null);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);

			Messagebox.show(Labels.getLabel(ComponentUtil.getFailKey(update)));
		}
	}

	public void onOpen$bbDept() {
		if (this.bbDept.isOpen() && Validator.isNull(this.icDepartment.getSrc())) {
			this.icDepartment.setAttribute("bandbox", this.bbDept);
			this.icDepartment.setAttribute("btnclear", this.btnClearDoc);

			if (Validator.isNotNull(this.dept)) {
				this.icDepartment.setAttribute("exclude", this.dept);
			}

			this.icDepartment.setSrc(Constants.TREE_DEPARTMENT_PAGE);
		}
	}
	// Bandbox documentType
}
