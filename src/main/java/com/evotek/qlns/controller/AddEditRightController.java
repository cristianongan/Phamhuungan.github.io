/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Category;
import com.evotek.qlns.model.Right;
import com.evotek.qlns.model.SimpleModel;
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
@Scope("prototype")
public class AddEditRightController extends BasicController<Window> implements Serializable {

	private static final long serialVersionUID = 1371006808897L;

	private static final Logger _log = LogManager.getLogger(AddEditRightController.class);

	@Autowired
	private CategoryService categoryService;

	private Category category;

	// get set service

	private Combobox cbRightType;

	private Right right;
	private Textbox tbDescription;

	private Textbox tbRightName;

	private Window winEditRight;

	private Window winTemp;

	private void _setEditForm() {
		this.tbRightName.setValue(this.right.getRightName());
		this.tbDescription.setValue(this.right.getDescription());
	}

	private boolean _validate(String rightName, String description) throws Exception {
		if (Validator.isNull(rightName)) {
			this.tbRightName.setErrorMessage(Values.getRequiredInputMsg(Labels.getLabel(LanguageKeys.RIGHT_NAME)));

			return false;
		}

		if (rightName.length() > Values.MEDIUM_LENGTH) {
			this.tbRightName.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.RIGHT_NAME), Values.MEDIUM_LENGTH));

			return false;
		}

		if (this.categoryService.isRightExist(rightName, this.right)) {
			this.tbRightName.setErrorMessage(Values.getDuplicateMsg(Labels.getLabel(LanguageKeys.RIGHT_NAME)));

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

		this.winEditRight = comp;
	}

	// init data
	public void initData() throws Exception {
		try {
			this.winTemp = (Window) this.arg.get(Constants.PARENT_WINDOW);

			this.category = (Category) this.arg.get(Constants.OBJECT);

			if (Validator.isNull(this.category)) {
				this.category = new Category();
			}

			this.right = (Right) this.arg.get(Constants.EDIT_OBJECT);

			if (Validator.isNotNull(this.right)) {
				this.winEditRight.setTitle((String) this.arg.get(Constants.TITLE));

				this._setEditForm();
			} else {
				this.tbRightName.setValue(this.category.getFolderName());
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	public void onAfterRender$cbRightType() {
		if (Validator.isNotNull(this.right) && Validator.isNotNull(this.right.getRightType())) {
			this.cbRightType.setSelectedIndex(this.right.getRightType().intValue());
		} else {
			this.cbRightType.setSelectedIndex(Values.FIRST_INDEX);
		}
	}

	public void onClick$btnCancel() {
		this.winEditRight.detach();
	}

	public void onClick$btnSave() throws Exception {
		boolean update = true;

		try {
			String rightName = GetterUtil.getString(this.tbRightName.getValue());
			Long rightType = ComponentUtil.getComboboxValue(this.cbRightType);
			String description = GetterUtil.getString(this.tbDescription.getValue());

			if (_validate(rightName, description)) {
				if (Validator.isNull(this.right)) {
					update = false;

					this.right = new Right();

					this.right.setUserId(getUserId());
					this.right.setUserName(getUserName());
					this.right.setCreateDate(new Date());
					this.right.setCategoryId(this.category.getCategoryId());
					this.right.setStatus(Values.STATUS_ACTIVE);
				}

				this.right.setRightName(rightName);
				this.right.setRightType(rightType);
				this.right.setDescription(description);
				this.right.setModifiedDate(new Date());

				this.categoryService.saveOrUpdateRight(this.right);

				ComponentUtil.createSuccessMessageBox(ComponentUtil.getSuccessKey(update));

				this.winEditRight.detach();

				Events.sendEvent("onLoadRight", this.winTemp, null);
			}

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);

			Messagebox.show(Labels.getLabel(ComponentUtil.getFailKey(update)));
		}
	}
	// even method

	// even method
	public void onCreate$cbRightType() {
		List<SimpleModel> rightTypes = this.categoryService.getRightType();

		this.cbRightType.setModel(new ListModelList<SimpleModel>(rightTypes));
	}
}
