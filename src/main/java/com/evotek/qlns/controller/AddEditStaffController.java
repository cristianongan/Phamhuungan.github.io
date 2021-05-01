/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.A;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.extend.Messagebox;
import com.evotek.qlns.model.ContractType;
import com.evotek.qlns.model.Department;
import com.evotek.qlns.model.Job;
import com.evotek.qlns.model.SimpleModel;
import com.evotek.qlns.model.Staff;
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
 * @author LinhLH
 */
@Controller
@Scope("prototype")
public class AddEditStaffController extends BasicController<Window> implements Serializable {

	private static final long serialVersionUID = 2323128487762215817L;

	private static final Logger _log = LogManager.getLogger(AddEditStaffController.class);

	@Autowired
	private StaffService staffService;

	private static final String ADD_EDIT_CONTRACT_TYPE_URL = "~./pages/manager_human_resource/editContractType.zul";
	private static final String ADD_EDIT_JOB_URL = "~./pages/manager_human_resource/editJob.zul";

	// Bandbox Department
	private Bandbox bbDepartment;
	private A btnClearDept;
	private Combobox cbContractType;
	private Combobox cbJobTitle;
	private Combobox cbStatus;
	private int contractTypeIndex = 0;
	private Datebox dbContractFromDate;
	private Datebox dbContractToDate;
	private Datebox dbDateOfBirth;
	private Datebox dbGrantDate;
	private Datebox dbInsurancePaidDate;
	private Datebox dbWorkDate;
	private Include icDepartment;
	private Integer index;
	private int jobIndex = 0;

	private List<Job> jobs;

	private Longbox lgbSalaryBasic;
	private ListModel model;
	private Staff staff;

	private Long staffId;

	private Textbox tbCollege;
	private Textbox tbContractNumber;
	private Textbox tbCurrentResidence;
	private Textbox tbEmail;
	private Textbox tbGrantPlace;

	private Textbox tbHomePhone;

	private Textbox tbIdentityCard;

	private Textbox tbInsuranceBookNumber;
	private Textbox tbLevels;
	private Textbox tbMajors;
	private Textbox tbMobile;

	private Textbox tbNote;

	private Textbox tbPaidPlace;
	private Textbox tbPermanentResidence;

	private Textbox tbStaffName;

	private Textbox tbTaxCode;

	private Window winEditStaff;

	private Hlayout winParent;

	private boolean _validate(String staffName, String permanentResidence, String currentResidence, String note,
			String contractNumber, String taxCode, String insuranceBookNumber, String paidPlace, String levels,
			String majors, String college, String identityCard, String grantPlace, String mobile, String homePhone,
			String email) {
		if (Validator.isNull(staffName)) {
			this.tbStaffName.setErrorMessage(Values.getRequiredInputMsg(Labels.getLabel(LanguageKeys.FULL_NAME)));

			this.tbStaffName.setFocus(true);

			return false;
		}

		if (staffName.length() > Values.MEDIUM_LENGTH) {
			this.tbStaffName.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.FULL_NAME), Values.MEDIUM_LENGTH));

			this.tbStaffName.setFocus(true);

			return false;
		}

		if (permanentResidence.length() > Values.LONG_LENGTH) {
			this.tbPermanentResidence.setErrorMessage(Values
					.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.PERMANENT_RESIDENCE), Values.LONG_LENGTH));

			this.tbPermanentResidence.setFocus(true);

			return false;
		}

		if (currentResidence.length() > Values.LONG_LENGTH) {
			this.tbCurrentResidence.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.CURRENT_RESIDENCE), Values.LONG_LENGTH));

			this.tbCurrentResidence.setFocus(true);

			return false;
		}

		if (note.length() > Values.GREATE_LONG_LENGTH) {
			this.tbNote.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.NOTE), Values.GREATE_LONG_LENGTH));

			this.tbNote.setFocus(true);

			return false;
		}

		if (contractNumber.length() > Values.SHORT_LENGTH) {
			this.tbContractNumber.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.CONTRACT_NUMBER), Values.SHORT_LENGTH));

			this.tbContractNumber.setFocus(true);

			return false;
		}

		if (taxCode.length() > Values.SHORT_LENGTH) {
			this.tbTaxCode.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.TAX_CODE), Values.SHORT_LENGTH));

			this.tbTaxCode.setFocus(true);

			return false;
		}

		if (insuranceBookNumber.length() > Values.SHORT_LENGTH) {
			this.tbInsuranceBookNumber.setErrorMessage(Values
					.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.INSURANCE_BOOK_NUMBER), Values.SHORT_LENGTH));

			this.tbInsuranceBookNumber.setFocus(true);

			return false;
		}

		if (paidPlace.length() > Values.LONG_LENGTH) {
			this.tbPaidPlace.setErrorMessage(Values
					.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.PAID_INSURANCE_PLACE), Values.LONG_LENGTH));

			this.tbPaidPlace.setFocus(true);

			return false;
		}

		if (levels.length() > Values.MEDIUM_LENGTH) {
			this.tbLevels.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.DEGREE), Values.MEDIUM_LENGTH));

			this.tbLevels.setFocus(true);

			return false;
		}

		if (majors.length() > Values.MEDIUM_LENGTH) {
			this.tbMajors.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.MAJORS), Values.MEDIUM_LENGTH));

			this.tbMajors.setFocus(true);

			return false;
		}

		if (college.length() > Values.MEDIUM_LENGTH) {
			this.tbCollege.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.COLLEGE), Values.MEDIUM_LENGTH));

			this.tbCollege.setFocus(true);

			return false;
		}

		if (identityCard.length() > Values.SHORT_LENGTH) {
			this.tbIdentityCard.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.IDENTITY_CARD), Values.SHORT_LENGTH));

			this.tbIdentityCard.setFocus(true);

			return false;
		}

		if (grantPlace.length() > Values.LONG_LENGTH) {
			this.tbGrantPlace.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.GRANT_PLACE), Values.LONG_LENGTH));

			this.tbGrantPlace.setFocus(true);

			return false;
		}

		if (mobile.length() > Values.VERY_SHORT_LENGTH) {
			this.tbMobile.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.MOBILE), Values.VERY_SHORT_LENGTH));

			this.tbMobile.setFocus(true);

			return false;
		}

		if (homePhone.length() > Values.VERY_SHORT_LENGTH) {
			this.tbHomePhone.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.HOME_PHONE), Values.VERY_SHORT_LENGTH));

			this.tbHomePhone.setFocus(true);

			return false;
		}

		if (email.length() > Values.SHORT_LENGTH) {
			this.tbEmail.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.EMAIL), Values.SHORT_LENGTH));

			this.tbEmail.setFocus(true);

			return false;
		}

		if (Validator.isNotNull(email) && !Validator.isEmailAddress(email)) {
			this.tbEmail.setErrorMessage(Values.getFormatInvalidMsg(Labels.getLabel(LanguageKeys.EMAIL)));

			this.tbEmail.setFocus(true);

			return false;
		}

		return true;
	}

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		this.staffId = (Long) this.arg.get(Constants.OBJECT_ID);

		if (Validator.isNotNull(this.staffId)) {
			this.staff = this.staffService.getStaff(this.staffId);
		} else {
			this.staff = (Staff) this.arg.get(Constants.OBJECT);
		}

		this.model = (ListModel) this.arg.get(Constants.MODEL);

		this.index = (Integer) this.arg.get(Constants.INDEX);

		initData();
	}

	@Override
	public void doBeforeComposeChildren(Window comp) throws Exception {
		super.doBeforeComposeChildren(comp);

		this.winEditStaff = comp;
	}

	public void initData() {
		if ((Validator.isNull(this.index) || Validator.isNull(this.model)) && Validator.isNotNull(this.staff)) {
			this.dbContractFromDate.setFocus(true);
		} else {
			this.winParent = (Hlayout) this.arg.get(Constants.PARENT_WINDOW);
		}

		if (Validator.isNotNull(this.staff)) {
			this.winEditStaff.setTitle((String) this.arg.get(Constants.TITLE));

			this.onLoadEditForm();
		}
	}

	public void onAfterRender$cbContractType() {
		this.cbContractType.setSelectedIndex(this.contractTypeIndex);
	}

	public void onAfterRender$cbJobTitle() {
		this.cbJobTitle.setSelectedIndex(this.jobIndex);
	}

	public void onAfterRender$cbStatus() {
		if (Validator.isNotNull(this.staff) && Validator.isNotNull(this.staff.getStatus())) {
			this.cbStatus.setSelectedIndex(this.staff.getStatus().intValue() + 1);
		} else {
			this.cbStatus.setSelectedIndex(Values.FIRST_INDEX);
		}
	}

	public void onChange$dbContractFromDate() {
		if (this.dbContractFromDate.getValue() != null
				&& this.cbContractType.getSelectedItem().getAttribute("data") != null) {
			Date fromDate = this.dbContractFromDate.getValue();

			Calendar cal = Calendar.getInstance();

			cal.setTime(fromDate);

			ContractType ct = (ContractType) this.cbContractType.getSelectedItem().getAttribute("data");

			if (Validator.isNotNull(ct.getMonthDuration())) {
				cal.add(Calendar.MONTH, ct.getMonthDuration().intValue());

				this.dbContractToDate.setValue(cal.getTime());
			}
		}
	}

	public void onClick$btnAddContractType() {
		Map map = new HashMap();

		map.put(Constants.PARENT_WINDOW, this.winEditStaff);
		map.put(Constants.OBJECT, null);

		Window win = (Window) Executions.createComponents(ADD_EDIT_CONTRACT_TYPE_URL, null, map);

		win.doModal();
	}

	public void onClick$btnAddJob() {
		Map map = new HashMap();

		map.put(Constants.PARENT_WINDOW, this.winEditStaff);
		map.put(Constants.OBJECT, null);

		Window win = (Window) Executions.createComponents(ADD_EDIT_JOB_URL, null, map);

		win.doModal();
	}

	public void onClick$btnCancel() {
		this.winEditStaff.detach();
	}

	// Bandbox documentType
	public void onClick$btnClearDept() {
		this.bbDepartment.setValue(StringPool.BLANK);
		this.bbDepartment.setAttribute(Constants.ID, null);

		this.btnClearDept.setDisabled(true);
		this.btnClearDept.setVisible(false);
	}

	public void onClick$btnNext() throws Exception {
		if (Validator.isNotNull(this.staff)) {
			if (this.index == (this.model.getSize() - 1)) {
				this.index = 0;
			} else {
				this.index++;
			}

			this.staff = (Staff) this.model.getElementAt(this.index);

			initData();

			onCreate$cbContractType();
			onCreate$cbJobTitle();
			onCreate$cbStatus();
		}
	}

	public void onClick$btnPrevious() throws Exception {
		if (Validator.isNotNull(this.staff)) {
			if (this.index == 0) {
				this.index = (this.model.getSize() - 1);
			} else {
				this.index--;
			}

			this.staff = (Staff) this.model.getElementAt(this.index);

			initData();

			onCreate$cbContractType();
			onCreate$cbJobTitle();
			onCreate$cbStatus();
		}
	}

	public void onClick$btnSave() {
		boolean update = true;

		try {
			String staffName = GetterUtil.getString(this.tbStaffName.getValue());
			// thong tin chung
			Department dept = (Department) this.bbDepartment.getAttribute(Constants.OBJECT);
			Job job = (Job) this.cbJobTitle.getSelectedItem().getAttribute("data");
			Date workDate = this.dbWorkDate.getValue();
			Date dateOfBirth = this.dbDateOfBirth.getValue();
			String permanentResidence = GetterUtil.getString(this.tbPermanentResidence.getValue());
			String currentResidence = GetterUtil.getString(this.tbCurrentResidence.getValue());
			Long status = ComponentUtil.getComboboxValue(this.cbStatus);
			String note = GetterUtil.getString(this.tbNote.getValue());
			// Hop dong lao dong
			ContractType contractType = (ContractType) this.cbContractType.getSelectedItem().getAttribute("data");
			Date contractFromDate = this.dbContractFromDate.getValue();
			Date contractToDate = this.dbContractToDate.getValue();
			String contractNumber = GetterUtil.getString(this.tbContractNumber.getValue());
			String taxCode = GetterUtil.getString(this.tbTaxCode.getValue());

			// Bao hiem xa hoi
			Long salaryBasic = this.lgbSalaryBasic.getValue();
			Date insurancePaidDate = this.dbInsurancePaidDate.getValue();
			String insuranceBookNumber = GetterUtil.getString(this.tbInsuranceBookNumber.getValue());
			String paidPlace = GetterUtil.getString(this.tbPaidPlace.getValue());
			// Trinh do chuyen mon
			String levels = GetterUtil.getString(this.tbLevels.getValue());
			String majors = GetterUtil.getString(this.tbMajors.getValue());
			String college = GetterUtil.getString(this.tbCollege.getValue());
			// Chung minh nhan dan
			String identityCard = GetterUtil.getString(this.tbIdentityCard.getValue());
			Date grantDate = this.dbGrantDate.getValue();
			String grantPlace = GetterUtil.getString(this.tbGrantPlace.getValue());
			// Lien he
			String mobile = GetterUtil.getString(this.tbMobile.getValue());
			String homePhone = GetterUtil.getString(this.tbHomePhone.getValue());
			String email = GetterUtil.getString(this.tbEmail.getValue());

			if (_validate(staffName, permanentResidence, currentResidence, note, contractNumber, taxCode,
					insuranceBookNumber, paidPlace, levels, majors, college, identityCard, grantPlace, mobile,
					homePhone, email)) {
				if (Validator.isNull(this.staff)) {
					update = false;

					this.staff = new Staff();

					this.staff.setUserId(getUserId());
					this.staff.setUserName(getUserName());
					this.staff.setCreateDate(new Date());
					this.staff.setStatus(status);
				}

				this.staff.setStaffName(staffName);
				this.staff.setDepartment(Validator.isNotNull(dept) ? dept : null);
				this.staff.setJob(Validator.isNotNull(job.getJobId()) ? job : null);
				this.staff.setWorkDate(workDate);
				this.staff.setDateOfBirth(dateOfBirth);
				this.staff.setPermanentResidence(permanentResidence);
				this.staff.setCurrentResidence(currentResidence);
				this.staff.setNote(note);
				this.staff.setContractType(Validator.isNotNull(contractType.getContractTypeId()) ? contractType : null);
				this.staff.setContractFromDate(contractFromDate);
				this.staff.setContractToDate(contractToDate);
				this.staff.setContractNumber(contractNumber);
				this.staff.setTaxCode(taxCode);
				this.staff.setSalaryBasic(salaryBasic);
				this.staff.setInsurancePaidDate(insurancePaidDate);
				this.staff.setInsuranceBookNumber(insuranceBookNumber);
				this.staff.setPaidPlace(paidPlace);
				this.staff.setLevels(levels);
				this.staff.setMajors(majors);
				this.staff.setCollege(college);
				this.staff.setIdentityCard(identityCard);
				this.staff.setGrantDate(grantDate);
				this.staff.setGrantPlace(grantPlace);
				this.staff.setMobile(mobile);
				this.staff.setHomePhone(homePhone);
				this.staff.setEmail(email);

				this.staff.setModifiedDate(new Date());

				this.staffService.saveOrUpdate(this.staff);

				ComponentUtil.createSuccessMessageBox(ComponentUtil.getSuccessKey(update));

				this.winEditStaff.detach();

				if (Validator.isNotNull(this.winParent)) {
					Events.sendEvent("onLoadData", this.winParent, null);
				}
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage());

			Messagebox.show(Labels.getLabel(ComponentUtil.getFailKey(update)));
		}
	}

	// combobox contract type
	public void onCreate$cbContractType() throws Exception {
		List<ContractType> contracts = this.staffService.getContract();

		contracts.add(0, new ContractType(Labels.getLabel(LanguageKeys.OPTION)));

		this.cbContractType.setModel(new ListModelList<ContractType>(contracts));

		if (Validator.isNotNull(this.staff) && Validator.isNotNull(this.staff.getContractType())) {
			this.contractTypeIndex = contracts.indexOf(this.staff.getContractType());
		}
	}

	// Bandbox documentType
	// combobox jobtitle
	public void onCreate$cbJobTitle() throws Exception {
		this.jobs = this.staffService.getJobTitle();

		this.jobs.add(0, new Job(Labels.getLabel(LanguageKeys.OPTION)));

		this.cbJobTitle.setModel(new ListModelList<Job>(this.jobs));

		if (Validator.isNotNull(this.staff) && Validator.isNotNull(this.staff.getJob())) {
			this.jobIndex = this.jobs.indexOf(this.staff.getJob());
		}
	}

	// combobox status
	public void onCreate$cbStatus() {
		List<SimpleModel> statusList = new ArrayList<SimpleModel>();

		statusList.add(new SimpleModel(Values.DEFAULT_OPTION_VALUE_INT, Labels.getLabel(LanguageKeys.OPTION)));

		statusList.add(new SimpleModel(Values.STATUS_DEACTIVE, Labels.getLabel(LanguageKeys.STATUS_NOT_WORKING)));

		statusList.add(new SimpleModel(Values.STATUS_ACTIVE, Labels.getLabel(LanguageKeys.STATUS_WORKING)));

		this.cbStatus.setModel(new ListModelList<SimpleModel>(statusList));
	}

	private void onLoadEditForm() {
		Department dept = this.staff.getDepartment();

		this.tbStaffName.setValue(this.staff.getStaffName());

		// thong tin chung
		if (dept.getDeptId() != null) {
			this.bbDepartment.setValue(dept.getDeptName());
			this.bbDepartment.setAttribute(Constants.ID, dept.getDeptId());

			this.bbDepartment.setAttribute(Constants.OBJECT, dept);
			this.btnClearDept.setVisible(true);
		}

		this.dbWorkDate.setValue(this.staff.getWorkDate());
		this.dbDateOfBirth.setValue(this.staff.getDateOfBirth());
		this.tbPermanentResidence.setValue(this.staff.getPermanentResidence());
		this.tbCurrentResidence.setValue(this.staff.getCurrentResidence());
		this.tbNote.setValue(this.staff.getNote());
		// Hop dong lao dong
		this.dbContractFromDate.setValue(this.staff.getContractFromDate());
		this.dbContractToDate.setValue(this.staff.getContractToDate());
		this.tbContractNumber.setValue(this.staff.getContractNumber());
		this.tbTaxCode.setValue(this.staff.getTaxCode());
		// Bao hiem xa hoi
		this.lgbSalaryBasic.setValue(this.staff.getSalaryBasic());
		this.dbInsurancePaidDate.setValue(this.staff.getInsurancePaidDate());
		this.tbInsuranceBookNumber.setValue(this.staff.getInsuranceBookNumber());
		this.tbPaidPlace.setValue(this.staff.getPaidPlace());
		// Trinh do chuyen mon
		this.tbLevels.setValue(this.staff.getLevels());
		this.tbMajors.setValue(this.staff.getMajors());
		this.tbCollege.setValue(this.staff.getCollege());
		// Chung minh nhan dan
		this.tbIdentityCard.setValue(this.staff.getIdentityCard());
		this.dbGrantDate.setValue(this.staff.getGrantDate());
		this.tbGrantPlace.setValue(this.staff.getGrantPlace());
		// Lien he
		this.tbMobile.setValue(this.staff.getMobile());
		this.tbHomePhone.setValue(this.staff.getHomePhone());
		this.tbEmail.setValue(this.staff.getEmail());

	}

	public void onOpen$bbDepartment() {
		if (this.bbDepartment.isOpen() && Validator.isNull(this.icDepartment.getSrc())) {
			this.icDepartment.setAttribute("bandbox", this.bbDepartment);
			this.icDepartment.setAttribute("btnclear", this.btnClearDept);

			this.icDepartment.setSrc(Constants.TREE_DEPARTMENT_PAGE);
		}
	}

	public void onUpdateCbContractType(Event event) throws Exception {
		onCreate$cbContractType();
	}

	public void onUpdateCbJob(Event event) throws Exception {
		onCreate$cbJobTitle();
	}
}
