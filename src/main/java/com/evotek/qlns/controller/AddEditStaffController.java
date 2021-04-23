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
import org.zkoss.spring.SpringUtil;
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
 * @author hungnt81
 */
public class AddEditStaffController extends BasicController<Window>
        implements Serializable {

	private static final long serialVersionUID = 2323128487762215817L;
	private Window winEditStaff;
    private Hlayout winParent;
    
    private Textbox tbStaffName;
    private Textbox tbPermanentResidence;
    private Textbox tbCurrentResidence;
    private Textbox tbNote;
    private Textbox tbContractNumber;
    private Textbox tbTaxCode;
    private Textbox tbInsuranceBookNumber;
    private Textbox tbPaidPlace;
    private Textbox tbLevels;
    private Textbox tbMajors;
    private Textbox tbCollege;
    private Textbox tbIdentityCard;
    private Textbox tbGrantPlace;
    private Textbox tbMobile;
    private Textbox tbHomePhone;
    private Textbox tbEmail;
    
    private Longbox lgbSalaryBasic;
    
    private Combobox cbJobTitle;
    private Combobox cbStatus;
    private Combobox cbContractType;
    
    private Datebox dbWorkDate;
    private Datebox dbDateOfBirth;
    private Datebox dbContractFromDate;
    private Datebox dbContractToDate;
    private Datebox dbInsurancePaidDate;
    private Datebox dbGrantDate;
    //Bandbox Department
    private Bandbox bbDepartment;
    
    private Include icDepartment;
    
    private A btnClearDept;
    
    private Long staffId;
    private Staff staff;
    private ListModel model;
    private Integer index;
    
    private List<Job> jobs;
    
    private int jobIndex = 0;
    private int contractTypeIndex = 0;
    
    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp); 
        
        this.winEditStaff = comp;
    }
    
    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        staffId = (Long) arg.get(Constants.OBJECT_ID);
        
        if(Validator.isNotNull(staffId)){
            staff = staffService.getStaff(staffId);
        } else {
            staff = (Staff) arg.get(Constants.OBJECT);
        }
        
        model = (ListModel) arg.get(Constants.MODEL);

        index = (Integer) arg.get(Constants.INDEX);

        initData();
    }

    public void initData() {
        if((Validator.isNull(index) 
                || Validator.isNull(model)) && Validator.isNotNull(staff)){            
            dbContractFromDate.setFocus(true);
        } else {
            winParent = (Hlayout) arg.get(Constants.PARENT_WINDOW);
        }
        
        if (Validator.isNotNull(staff)) {
            winEditStaff.setTitle((String) arg.get(Constants.TITLE));
            
            this.onLoadEditForm();
        }
    }

    private void onLoadEditForm() {
        Department dept = staff.getDepartment();
        
        tbStaffName.setValue(staff.getStaffName());
        
        //thong tin chung
        if(dept.getDeptId()!=null){
            bbDepartment.setValue(dept.getDeptName());
            bbDepartment.setAttribute(Constants.ID, dept.getDeptId());
            
            bbDepartment.setAttribute(Constants.OBJECT, dept);
            btnClearDept.setVisible(true);
        }
        
        dbWorkDate.setValue(staff.getWorkDate());
        dbDateOfBirth.setValue(staff.getDateOfBirth());
        tbPermanentResidence.setValue(staff.getPermanentResidence());
        tbCurrentResidence.setValue(staff.getCurrentResidence());
        tbNote.setValue(staff.getNote());
        //Hop dong lao dong
        dbContractFromDate.setValue(staff.getContractFromDate());
        dbContractToDate.setValue(staff.getContractToDate());
        tbContractNumber.setValue(staff.getContractNumber());
        tbTaxCode.setValue(staff.getTaxCode());
        //Bao hiem xa hoi
        lgbSalaryBasic.setValue(staff.getSalaryBasic());
        dbInsurancePaidDate.setValue(staff.getInsurancePaidDate());
        tbInsuranceBookNumber.setValue(staff.getInsuranceBookNumber());
        tbPaidPlace.setValue(staff.getPaidPlace());
        //Trinh do chuyen mon
        tbLevels.setValue(staff.getLevels());
        tbMajors.setValue(staff.getMajors());
        tbCollege.setValue(staff.getCollege());
        //Chung minh nhan dan
        tbIdentityCard.setValue(staff.getIdentityCard());
        dbGrantDate.setValue(staff.getGrantDate());
        tbGrantPlace.setValue(staff.getGrantPlace());
        //Lien he
        tbMobile.setValue(staff.getMobile());
        tbHomePhone.setValue(staff.getHomePhone());
        tbEmail.setValue(staff.getEmail());
                
    }
    
    public void onClick$btnSave() {
        boolean update = true;

        try {
            String staffName            = GetterUtil.getString(
                    tbStaffName.getValue());
            //thong tin chung
            Department dept             = (Department) bbDepartment.
                    getAttribute(Constants.OBJECT);
            Job job                     = (Job) cbJobTitle.getSelectedItem().
                    getAttribute("data");
            Date workDate               = dbWorkDate.getValue();
            Date dateOfBirth            = dbDateOfBirth.getValue();
            String permanentResidence   = GetterUtil.getString(
                    tbPermanentResidence.getValue());
            String currentResidence     = GetterUtil.getString(
                    tbCurrentResidence.getValue());
            Long status                 = ComponentUtil.getComboboxValue(
                    cbStatus);
            String note                 = GetterUtil.getString(
                    tbNote.getValue());
            //Hop dong lao dong
            ContractType contractType   = (ContractType) 
                    cbContractType.getSelectedItem().getAttribute("data");
            Date contractFromDate       = dbContractFromDate.getValue();
            Date contractToDate         = dbContractToDate.getValue();
            String contractNumber       = GetterUtil.getString(
                    tbContractNumber.getValue());
            String taxCode              = GetterUtil.getString(
                    tbTaxCode.getValue());
            
            //Bao hiem xa hoi
            Long salaryBasic            = lgbSalaryBasic.getValue();
            Date insurancePaidDate      = dbInsurancePaidDate.getValue();
            String insuranceBookNumber  = GetterUtil.getString(
                    tbInsuranceBookNumber.getValue());
            String paidPlace            = GetterUtil.getString(
                    tbPaidPlace.getValue());
            //Trinh do chuyen mon
            String levels               = GetterUtil.getString(
                    tbLevels.getValue());
            String majors               = GetterUtil.getString(
                    tbMajors.getValue());
            String college              = GetterUtil.getString(
                    tbCollege.getValue());
            //Chung minh nhan dan
            String identityCard         = GetterUtil.getString(
                    tbIdentityCard.getValue());
            Date grantDate              = dbGrantDate.getValue();
            String grantPlace           = GetterUtil.getString(
                    tbGrantPlace.getValue());
            //Lien he
            String mobile               = GetterUtil.getString(
                    tbMobile.getValue());
            String homePhone            = GetterUtil.getString(
                    tbHomePhone.getValue());
            String email                = GetterUtil.getString(
                    tbEmail.getValue());
            
            if (_validate(staffName, permanentResidence, currentResidence, note, 
                    contractNumber, taxCode, insuranceBookNumber, paidPlace, 
                    levels, majors, college, identityCard, grantPlace, mobile, 
                    homePhone, email)) {
                if(Validator.isNull(staff)){
                    update = false;
                    
                    staff = new Staff();
                    
                    staff.setUserId(getUserId());
                    staff.setUserName(getUserName());
                    staff.setCreateDate(new Date());
                    staff.setStatus(status);
                }
                
                staff.setStaffName(staffName);
                staff.setDepartment(Validator.isNotNull(dept)?dept:null);
                staff.setJob(Validator.isNotNull(job.getJobId())?job:null);
                staff.setWorkDate(workDate);
                staff.setDateOfBirth(dateOfBirth);
                staff.setPermanentResidence(permanentResidence);
                staff.setCurrentResidence(currentResidence);
                staff.setNote(note);
                staff.setContractType(Validator.isNotNull(
                        contractType.getContractTypeId())?contractType:null);
                staff.setContractFromDate(contractFromDate);
                staff.setContractToDate(contractToDate);
                staff.setContractNumber(contractNumber);
                staff.setTaxCode(taxCode);
                staff.setSalaryBasic(salaryBasic);
                staff.setInsurancePaidDate(insurancePaidDate);
                staff.setInsuranceBookNumber(insuranceBookNumber);
                staff.setPaidPlace(paidPlace);
                staff.setLevels(levels);
                staff.setMajors(majors);
                staff.setCollege(college);
                staff.setIdentityCard(identityCard);
                staff.setGrantDate(grantDate);
                staff.setGrantPlace(grantPlace);
                staff.setMobile(mobile);
                staff.setHomePhone(homePhone);
                staff.setEmail(email);
                
                staff.setModifiedDate(new Date());
                
                staffService.saveOrUpdate(staff);
                
                ComponentUtil.createSuccessMessageBox(
                        ComponentUtil.getSuccessKey(update));

                winEditStaff.detach();
                
                if(Validator.isNotNull(winParent)){
                    Events.sendEvent("onLoadData", winParent, null);
                }
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage());

            Messagebox.show(Labels.getLabel(
                    ComponentUtil.getFailKey(update)));
        }
    }

    private boolean _validate(String staffName, String permanentResidence, 
            String currentResidence, String note, String contractNumber, 
            String taxCode, String insuranceBookNumber, String paidPlace, 
            String levels, String majors, String college, String identityCard, 
            String grantPlace, String mobile, String homePhone, String email){
        if (Validator.isNull(staffName)) {
            tbStaffName.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.FULL_NAME)));
            
            tbStaffName.setFocus(true);
            
            return false;
        }
        
        if (staffName.length() > Values.MEDIUM_LENGTH) {
            tbStaffName.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.FULL_NAME),
                    Values.MEDIUM_LENGTH));
            
            tbStaffName.setFocus(true);
            
            return false;
        }
        
        if (permanentResidence.length() > Values.LONG_LENGTH) {
            tbPermanentResidence.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.PERMANENT_RESIDENCE),
                    Values.LONG_LENGTH));
            
            tbPermanentResidence.setFocus(true);
            
            return false;
        }
        
        if (currentResidence.length() > Values.LONG_LENGTH) {
            tbCurrentResidence.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.CURRENT_RESIDENCE),
                    Values.LONG_LENGTH));
            
            tbCurrentResidence.setFocus(true);
            
            return false;
        }
        
        if (note.length() > Values.GREATE_LONG_LENGTH) {
            tbNote.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.NOTE),
                    Values.GREATE_LONG_LENGTH));
            
            tbNote.setFocus(true);
            
            return false;
        }
        
        if (contractNumber.length() > Values.SHORT_LENGTH) {
            tbContractNumber.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.CONTRACT_NUMBER),
                    Values.SHORT_LENGTH));
            
            tbContractNumber.setFocus(true);
            
            return false;
        }
        
        if (taxCode.length() > Values.SHORT_LENGTH) {
            tbTaxCode.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.TAX_CODE),
                    Values.SHORT_LENGTH));
            
            tbTaxCode.setFocus(true);
            
            return false;
        }
        
        if (insuranceBookNumber.length() > Values.SHORT_LENGTH) {
            tbInsuranceBookNumber.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.INSURANCE_BOOK_NUMBER),
                    Values.SHORT_LENGTH));
            
            tbInsuranceBookNumber.setFocus(true);
            
            return false;
        }
        
        if (paidPlace.length() > Values.LONG_LENGTH) {
            tbPaidPlace.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.PAID_INSURANCE_PLACE),
                    Values.LONG_LENGTH));
            
            tbPaidPlace.setFocus(true);
            
            return false;
        }
        
        if (levels.length() > Values.MEDIUM_LENGTH) {
            tbLevels.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.DEGREE),
                    Values.MEDIUM_LENGTH));
            
            tbLevels.setFocus(true);
            
            return false;
        }
        
        if (majors.length() > Values.MEDIUM_LENGTH) {
            tbMajors.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.MAJORS),
                    Values.MEDIUM_LENGTH));
            
            tbMajors.setFocus(true);
            
            return false;
        }
        
        if (college.length() > Values.MEDIUM_LENGTH) {
            tbCollege.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.COLLEGE),
                    Values.MEDIUM_LENGTH));
            
            tbCollege.setFocus(true);
            
            return false;
        }
        
        if (identityCard.length() > Values.SHORT_LENGTH) {
            tbIdentityCard.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.IDENTITY_CARD),
                    Values.SHORT_LENGTH));
            
            tbIdentityCard.setFocus(true);
            
            return false;
        }
        
        if (grantPlace.length() > Values.LONG_LENGTH) {
            tbGrantPlace.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.GRANT_PLACE),
                    Values.LONG_LENGTH));
            
            tbGrantPlace.setFocus(true);
            
            return false;
        }
        
        if (mobile.length() > Values.VERY_SHORT_LENGTH) {
            tbMobile.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.MOBILE),
                    Values.VERY_SHORT_LENGTH));
            
            tbMobile.setFocus(true);
            
            return false;
        }
        
        if (homePhone.length() > Values.VERY_SHORT_LENGTH) {
            tbHomePhone.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.HOME_PHONE),
                    Values.VERY_SHORT_LENGTH));
            
            tbHomePhone.setFocus(true);
            
            return false;
        }
        
        if (email.length() > Values.SHORT_LENGTH) {
            tbEmail.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.EMAIL),
                    Values.SHORT_LENGTH));
            
            tbEmail.setFocus(true);
            
            return false;
        }
        
        if(Validator.isNotNull(email) 
                && !Validator.isEmailAddress(email)){
            tbEmail.setErrorMessage(Values.getFormatInvalidMsg(
                    Labels.getLabel(LanguageKeys.EMAIL)));
            
            tbEmail.setFocus(true);
            
            return false;
        }
        
        return true;
    }
    
    public void onClick$btnAddJob() {
        Map map = new HashMap();

        map.put(Constants.PARENT_WINDOW, winEditStaff);
        map.put(Constants.OBJECT, null);

        Window win = (Window) Executions.createComponents(
                ADD_EDIT_JOB_URL, null, map);

        win.doModal();
    }
    
    public void onClick$btnAddContractType() {
        Map map = new HashMap();

        map.put(Constants.PARENT_WINDOW, winEditStaff);
        map.put(Constants.OBJECT, null);

        Window win = (Window) Executions.createComponents(
                ADD_EDIT_CONTRACT_TYPE_URL, null, map);

        win.doModal();
    }
    
    
    
    public void onClick$btnCancel(){
        winEditStaff.detach();
    }
    //Bandbox documentType
    public void onClick$btnClearDept() {
        bbDepartment.setValue(StringPool.BLANK);
        bbDepartment.setAttribute(Constants.ID, null);
        
        btnClearDept.setDisabled(true);
        btnClearDept.setVisible(false);
    }
    
    public void onOpen$bbDepartment(){
        if(bbDepartment.isOpen() 
                && Validator.isNull(icDepartment.getSrc())) {
            icDepartment.setAttribute("bandbox", bbDepartment);
            icDepartment.setAttribute("btnclear", btnClearDept);
            
            icDepartment.setSrc(Constants.TREE_DEPARTMENT_PAGE);
        }
    }

    //Bandbox documentType
    //combobox jobtitle
    public void onCreate$cbJobTitle() throws Exception {
        jobs = staffService.getJobTitle();
        
        jobs.add(0, new Job(Labels.getLabel(LanguageKeys.OPTION)));

        cbJobTitle.setModel(new ListModelList<Job>(jobs));
        
        if (Validator.isNotNull(staff)
                && Validator.isNotNull(staff.getJob())) {
            jobIndex = jobs.indexOf(staff.getJob());
        }
    }
    
    public void onAfterRender$cbJobTitle() {
        cbJobTitle.setSelectedIndex(jobIndex);
    }
    
    public void onUpdateCbJob(Event event) throws Exception{
        onCreate$cbJobTitle();
    }
    
    public void onUpdateCbContractType(Event event) throws Exception{
        onCreate$cbContractType();
    }
    //combobox status
    public void onCreate$cbStatus() {
        List<SimpleModel> statusList = new ArrayList<SimpleModel>();

        statusList.add(new SimpleModel(Values.DEFAULT_OPTION_VALUE_INT,
                Labels.getLabel(LanguageKeys.OPTION)));
        
        statusList.add(new SimpleModel(Values.STATUS_DEACTIVE,
                Labels.getLabel(LanguageKeys.STATUS_NOT_WORKING)));
        
        statusList.add(new SimpleModel(Values.STATUS_ACTIVE,
                Labels.getLabel(LanguageKeys.STATUS_WORKING)));

        cbStatus.setModel(new ListModelList<SimpleModel>(statusList));
    }

    public void onAfterRender$cbStatus() {
        if (Validator.isNotNull(staff)
                && Validator.isNotNull(staff.getStatus())) {
            cbStatus.setSelectedIndex(staff.getStatus().intValue()+1);
        } else {
            cbStatus.setSelectedIndex(Values.FIRST_INDEX);
        }
    }
    //combobox contract type
    public void onCreate$cbContractType() throws Exception {
        List<ContractType> contracts = staffService.getContract();
        
        contracts.add(0, new ContractType(Labels.getLabel(LanguageKeys.OPTION)));

        cbContractType.setModel(new ListModelList<ContractType>(contracts));
        
        if (Validator.isNotNull(staff)
                && Validator.isNotNull(staff.getContractType())) {
            contractTypeIndex = contracts.indexOf(staff.getContractType());
        }
    }
    
    public void onAfterRender$cbContractType() {
        cbContractType.setSelectedIndex(contractTypeIndex);
    }
    
    public void onChange$dbContractFromDate(){
        if(dbContractFromDate.getValue()!=null 
                && cbContractType.getSelectedItem().getAttribute("data")!=null) {
            Date fromDate = dbContractFromDate.getValue();
            
            Calendar cal = Calendar.getInstance();
            
            cal.setTime(fromDate);
            
            ContractType ct = (ContractType) 
                    cbContractType.getSelectedItem().getAttribute("data");
            
            if(Validator.isNotNull(ct.getMonthDuration())){
                cal.add(Calendar.MONTH, ct.getMonthDuration().intValue());
                
                dbContractToDate.setValue(cal.getTime());
            }
        }
    }
    
    public void onClick$btnPrevious() throws Exception {
        if (Validator.isNotNull(staff)) {
            if (index == 0) {
                index = (model.getSize() - 1);
            } else {
                index--;
            }

            staff = (Staff) model.getElementAt(index);

            initData();
            
            onCreate$cbContractType();
            onCreate$cbJobTitle();
            onCreate$cbStatus();
        }
    }
    
    public void onClick$btnNext() throws Exception {
        if (Validator.isNotNull(staff)) {
            if (index == (model.getSize() - 1)) {
                index = 0;
            } else {
                index++;
            }

            staff = (Staff) model.getElementAt(index);

            initData();
            
            onCreate$cbContractType();
            onCreate$cbJobTitle();
            onCreate$cbStatus();
        }
    }
    //get set service
    public StaffService getStaffService() {
        if (staffService == null) {
            staffService = (StaffService) SpringUtil.getBean("staffService");
            setStaffService(staffService);
        }
        return staffService;
    }

    public void setStaffService(StaffService staffService) {
        this.staffService = staffService;
    }
    
    private transient StaffService staffService;
    
    private static final String ADD_EDIT_JOB_URL
            = "/html/pages/manager_human_resource/editJob.zul";
    private static final String ADD_EDIT_CONTRACT_TYPE_URL
            = "/html/pages/manager_human_resource/editContractType.zul";
    
    private static final Logger _log =
            LogManager.getLogger(AddEditStaffController.class);
}
