/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.extend.Messagebox;
import com.evotek.qlns.model.SalaryLandmark;
import com.evotek.qlns.model.Staff;
import com.evotek.qlns.model.WorkProcess;
import com.evotek.qlns.model.render.SalaryLandmarkRender;
import com.evotek.qlns.model.render.WorkProcessRender;
import com.evotek.qlns.service.StaffService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.DateUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author LinhLH
 */
@Controller
@Scope("prototype")
public class DetailStaffController extends BasicController<Window> implements Serializable {

	private static final long serialVersionUID = 9166160847764008289L;

	private static final Logger _log = LogManager.getLogger(DetailStaffController.class);

	@Autowired
	private StaffService staffService;

	private Integer index;

	private Label lbCollege;
	private Label lbContractFromDate;
	private Label lbContractNumber;
	private Label lbContractToDate;
	private Label lbContractType;
	private Label lbCurrentResidence;
	private Label lbDateOfBirth;
	private Label lbDeptName;
	private Label lbEmail;
	private Label lbGrantDate;
	private Label lbGrantPlace;
	private Label lbHomePhone;
	private Label lbIdentityCard;
	private Label lbInsuranceBookNumber;
	private Label lbInsurancePaidDate;
	private Label lbJobTitle;
	private Label lbLevels;
	private Label lbMajors;
	private Label lbMobile;
	private Label lbNote;
	private Label lbPaidPlace;
	private Label lbPermanentResidence;
	private Label lbSalaryBasic;
	private Label lbStaffName;
	private Label lbStatus;
	private Label lbTaxCode;
	private Label lbWorkDate;

	private ListModel model;

	private Listbox listSalaryLm;
	private Listbox listWorkProcess;

	private Staff staff;

	private Window winDetailStaff;

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		this.staff = (Staff) this.arg.get(Constants.Attr.OBJECT);

		this.model = (ListModel) this.arg.get(Constants.Attr.MODEL);

		this.index = (Integer) this.arg.get(Constants.Attr.INDEX);

		initData();
	}

	@Override
	public void doBeforeComposeChildren(Window comp) throws Exception {
		super.doBeforeComposeChildren(comp);

		this.winDetailStaff = comp;
	}

	public void initData() {
		if (Validator.isNotNull(this.staff)) {
			this.winDetailStaff.setTitle(
					Labels.getLabel(LanguageKeys.TITLE_STAFF_DETAIL, new Object[] { this.staff.getStaffName() }));

			this.onLoadData();

			this.onLoadSalaryLandmark();

			this.onLoadWorkProcess();
		}
	}

	public void onClick$btnAddSalaryLm() {
		Map<String, Object> map = new HashMap<>();

		map.put(Constants.Attr.PARENT_WINDOW, this.winDetailStaff);
		map.put(Constants.Attr.OBJECT, this.staff);

		Window win = (Window) Executions
				.createComponents(Constants.Page.ManagerHumanResource.ADD_EDIT_SALARY_LANDMARK, null, map);

		win.doModal();
	}

	public void onClick$btnAddWorkProcess() {
		Map<String, Object> map = new HashMap<>();

		map.put(Constants.Attr.PARENT_WINDOW, this.winDetailStaff);
		map.put(Constants.Attr.OBJECT, this.staff);

		Window win = (Window) Executions.createComponents(Constants.Page.ManagerHumanResource.ADD_EDIT_WORK_PROCESS,
				null, map);

		win.doModal();
	}

	public void onClick$btnCancel() {
		this.winDetailStaff.detach();
	}

	public void onClick$btnNext() throws Exception {
		if (this.index == (this.model.getSize() - 1)) {
			this.index = 0;
		} else {
			this.index++;
		}

		this.staff = (Staff) this.model.getElementAt(this.index);

		initData();
	}

	public void onClick$btnPrevious() throws Exception {
		if (this.index == 0) {
			this.index = (this.model.getSize() - 1);
		} else {
			this.index--;
		}

		this.staff = (Staff) this.model.getElementAt(this.index);

		initData();
	}

	public void onDeleteStaff(Event event) throws Exception {
		final SalaryLandmark salaryLm = (SalaryLandmark) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener<Event>() {

					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								DetailStaffController.this.staffService.deleteSalaryLm(salaryLm);

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_DELETE_SUCCESS);

								onLoadSalaryLandmark();
							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_DELETE_FAIL));
							}
						}
					}
				});
	}

	public void onDeleteWorkProcess(Event event) throws Exception {
		final WorkProcess wp = (WorkProcess) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener<Event>() {

					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								DetailStaffController.this.staffService.deleteWorkProcess(wp);

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_DELETE_SUCCESS);

								onLoadWorkProcess();
							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_DELETE_FAIL));
							}
						}
					}
				});
	}

	public void onLoadData() {
		this.lbStaffName.setValue(this.staff.getStaffName());
		this.lbDeptName.setValue(
				this.staff.getDepartment() != null ? this.staff.getDepartment().getDeptName() : StringPool.BLANK);
		this.lbJobTitle.setValue(this.staff.getJob() != null ? this.staff.getJob().getJobTitle() : StringPool.BLANK);
		this.lbWorkDate.setValue(GetterUtil.getDate(this.staff.getWorkDate(), DateUtil.SHORT_DATE_PATTERN));
		this.lbDateOfBirth.setValue(GetterUtil.getDate(this.staff.getDateOfBirth(), DateUtil.SHORT_DATE_PATTERN));
		this.lbPermanentResidence.setValue(this.staff.getPermanentResidence());
		this.lbCurrentResidence.setValue(this.staff.getCurrentResidence());
		this.lbStatus.setValue(Values.getStaffStatus(this.staff.getStatus()));
		this.lbNote.setValue(this.staff.getNote());
		this.lbContractType
				.setValue(this.staff.getContractType() != null ? this.staff.getContractType().getContractTypeName()
						: StringPool.BLANK);
		this.lbContractFromDate
				.setValue(GetterUtil.getDate(this.staff.getContractFromDate(), DateUtil.SHORT_DATE_PATTERN));
		this.lbContractToDate.setValue(GetterUtil.getDate(this.staff.getContractToDate(), DateUtil.SHORT_DATE_PATTERN));
		this.lbContractNumber.setValue(this.staff.getContractNumber());
		this.lbTaxCode.setValue(this.staff.getTaxCode());
		this.lbSalaryBasic.setValue(GetterUtil.getFormat(this.staff.getSalaryBasic()));
		this.lbInsurancePaidDate
				.setValue(GetterUtil.getDate(this.staff.getInsurancePaidDate(), DateUtil.SHORT_DATE_PATTERN));
		this.lbInsuranceBookNumber.setValue(this.staff.getInsuranceBookNumber());
		this.lbPaidPlace.setValue(this.staff.getPaidPlace());
		this.lbLevels.setValue(this.staff.getLevels());
		this.lbMajors.setValue(this.staff.getMajors());
		this.lbCollege.setValue(this.staff.getCollege());
		this.lbIdentityCard.setValue(this.staff.getIdentityCard());
		this.lbGrantDate.setValue(GetterUtil.getDate(this.staff.getGrantDate(), DateUtil.SHORT_DATE_PATTERN));
		this.lbGrantPlace.setValue(this.staff.getGrantPlace());
		this.lbMobile.setValue(this.staff.getMobile());
		this.lbHomePhone.setValue(this.staff.getHomePhone());
		this.lbEmail.setValue(this.staff.getEmail());
	}

	public void onLoadReloadSalaryLm(Event event) {
		this.onLoadSalaryLandmark();
	}

	public void onLoadReloadWp(Event event) {
		this.onLoadWorkProcess();
	}

	public void onLoadSalaryLandmark() {
		List<SalaryLandmark> salaryLandmarks = this.staffService.getSalaryLandmarkByStaffId(this.staff.getStaffId());

		this.listSalaryLm.setItemRenderer(new SalaryLandmarkRender(this.winDetailStaff, this.staff));
		this.listSalaryLm.setModel(new ListModelList<SalaryLandmark>(salaryLandmarks));
	}

	public void onLoadWorkProcess() {
		List<WorkProcess> workProcesses = this.staffService.getWorkProcessByStaffId(this.staff.getStaffId());

		this.listWorkProcess.setItemRenderer(new WorkProcessRender(this.winDetailStaff, this.staff));
		this.listWorkProcess.setModel(new ListModelList<WorkProcess>(workProcesses));
	}
}
