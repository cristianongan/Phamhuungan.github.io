/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.extend.Messagebox;
import com.evotek.qlns.model.ContractType;
import com.evotek.qlns.model.render.ContractTypeRender;
import com.evotek.qlns.service.StaffService;
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
@Scope("prototype")
public class AddEditContractTypeController extends BasicController<Window> implements Serializable {
	private static final long serialVersionUID = -4468247631962909572L;

	private static final Logger _log = LogManager.getLogger(AddEditContractTypeController.class);

	@Autowired
	private StaffService staffService;

	private Listbox contractTypeListbox;

	private List<ContractType> contractTypes;

	private Spinner spMonthDuration;

	private Textbox tbContractType;

	private Window winEditContractType;
	private Window winParent;

	private boolean _validate(String contractType, Long monthDuration) {
		if (Validator.isNull(contractType)) {
			this.tbContractType
					.setErrorMessage(Values.getRequiredInputMsg(Labels.getLabel(LanguageKeys.CONTRACT_TYPE)));

			this.tbContractType.setFocus(true);

			return false;
		}

		if (contractType.length() > Values.MEDIUM_LENGTH) {
			this.tbContractType.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.CONTRACT_TYPE), Values.MEDIUM_LENGTH));

			this.tbContractType.setFocus(true);

			return false;
		}

		if (monthDuration != null && (monthDuration <= 0 || monthDuration > 3600)) {
			this.spMonthDuration.setErrorMessage(
					Values.getValueMustInRangeMsg(Labels.getLabel(LanguageKeys.CONTRACT_DURATION), 0, 3600));

			this.spMonthDuration.setFocus(true);

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

		this.winEditContractType = comp;
	}

	public void initData() {
		this.winParent = (Window) this.arg.get(Constants.PARENT_WINDOW);

		refreshModel();
	}

	public void onClick$btnAdd() throws Exception {
		try {
			String contractType = GetterUtil.getString(this.tbContractType.getValue());
			Long monthDuration = GetterUtil.getLong(this.spMonthDuration.getValue());

			if (_validate(contractType, monthDuration)) {
				ContractType ct = new ContractType();

				ct.setUserId(getUserId());
				ct.setUserName(getUserName());
				ct.setCreateDate(new Date());
				ct.setModifiedDate(new Date());
				ct.setContractTypeName(contractType);
				ct.setMonthDuration(monthDuration);
				ct.setDescription(contractType);

				this.staffService.saveOrUpdate(ct);

				ComponentUtil.createSuccessMessageBox(ComponentUtil.getSuccessKey(true));

				this.tbContractType.setValue(StringPool.BLANK);

				refreshModel();

				Events.sendEvent("onUpdateCbContractType", this.winParent, null);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage());

			Messagebox.show(Labels.getLabel(ComponentUtil.getFailKey(false)));
		}
	}

	public void onClick$btnCancel() {
		this.winEditContractType.detach();
	}

	public void onDelete(Event event) {
		final ContractType ct = (ContractType) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, Messagebox.OK, new EventListener<Event>() {

					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								AddEditContractTypeController.this.staffService.deleteContractType(ct);

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_DELETE_SUCCESS);

								AddEditContractTypeController.this.contractTypes.remove(ct);

								refreshModel();

							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_DELETE_FAIL));
							}
						}
					}
				});
	}

	public void onUpdateJob(Event event) {
		final ContractType ct = (ContractType) event.getData();

		try {
			this.staffService.saveOrUpdate(ct);

			ComponentUtil.createSuccessMessageBox(ComponentUtil.getSuccessKey(true));

			Events.sendEvent("onUpdateCbContractType", this.winParent, null);
		} catch (Exception ex) {
			_log.error(ex.getMessage());

			Messagebox.show(Labels.getLabel(ComponentUtil.getFailKey(true)));
		}
	}

	public void refreshModel() {
		this.contractTypes = this.staffService.getContract();

		this.contractTypeListbox.setModel(new ListModelList<ContractType>(this.contractTypes));
		this.contractTypeListbox.setItemRenderer(new ContractTypeRender(this.winEditContractType));
	}
}
