/*
 * To change this template, choose Tools | Templates
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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Category;
import com.evotek.qlns.model.Group;
import com.evotek.qlns.service.CategoryService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author linhlh2
 */
@Controller
public class AddEditGroupController extends BasicController<Window> implements Serializable {

	private static final long serialVersionUID = 1371000290846L;

	private static final Logger _log = LogManager.getLogger(AddEditGroupController.class);

	@Autowired
	private CategoryService categoryService;

	private Category category;

	// get set service

	private Group group;

	private Textbox tbDescription;
	private Textbox tbGroupsName;

	private Window winEditGroups;

	private Window winTemp;

	private void _setEditForm() {
		this.tbGroupsName.setValue(this.group.getGroupName());
		this.tbDescription.setValue(this.group.getDescription());
	}

	private boolean _validate(String groupName, String description) {
		if (Validator.isNull(groupName)) {
			this.tbGroupsName.setErrorMessage(Values.getRequiredInputMsg(Labels.getLabel(LanguageKeys.GROUP_NAME)));

			return false;
		}

		if (groupName.length() > Values.MEDIUM_LENGTH) {
			this.tbGroupsName.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.GROUP_NAME), Values.MEDIUM_LENGTH));

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

		// init data
		this.initData();
	}

	@Override
	public void doBeforeComposeChildren(Window comp) throws Exception {
		super.doBeforeComposeChildren(comp);

		this.winEditGroups = comp;
	}

	// init data
	public void initData() throws Exception {
		try {
			this.winTemp = (Window) this.arg.get(Constants.PARENT_WINDOW);

			this.category = (Category) this.arg.get(Constants.OBJECT);

			if (Validator.isNull(this.category)) {
				this.category = new Category();
			}

			this.group = (Group) this.arg.get(Constants.EDIT_OBJECT);

			if (Validator.isNotNull(this.group)) {
				this.winEditGroups.setTitle((String) this.arg.get(Constants.TITLE));

				this._setEditForm();
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	// even method
	public void onClick$btnCancel() {
		this.winEditGroups.detach();
	}

	public void onClick$btnSave() throws Exception {
		boolean update = true;

		try {
			String groupName = GetterUtil.getString(this.tbGroupsName.getValue());
			String description = GetterUtil.getString(this.tbDescription.getValue());

			if (_validate(groupName, description)) {
				if (Validator.isNull(this.group)) {
					update = false;

					this.group = new Group();

					this.group.setUserId(getUserId());
					this.group.setUserName(getUserName());
					this.group.setCreateDate(new Date());
					this.group.setCategoryId(this.category.getCategoryId());
					this.group.setStatus(Values.STATUS_ACTIVE);
				}

				this.group.setGroupName(groupName);
				this.group.setDescription(description);
				this.group.setModifiedDate(new Date());

				this.categoryService.saveOrUpdateGroup(this.group);

				ComponentUtil.createSuccessMessageBox(ComponentUtil.getSuccessKey(update));

				this.winEditGroups.detach();

				Events.sendEvent("onLoadGroups", this.winTemp, null);
			}

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);

			Messagebox.show(Labels.getLabel(ComponentUtil.getFailKey(update)));
		}
	}
	// even method
}
