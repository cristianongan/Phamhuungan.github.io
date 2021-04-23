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
import org.zkoss.spring.SpringUtil;
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
 * @author PC
 */
public class DetailStaffController extends BasicController<Window>
        implements Serializable {

    private Window winDetailStaff;
    
    private Label lbStaffName;
    private Label lbDeptName;
    private Label lbJobTitle;
    private Label lbWorkDate;
    private Label lbDateOfBirth;
    private Label lbPermanentResidence;
    private Label lbCurrentResidence;
    private Label lbStatus;
    private Label lbNote;
    private Label lbContractType;
    private Label lbContractFromDate;
    private Label lbContractToDate;
    private Label lbContractNumber;
    private Label lbTaxCode;
    private Label lbSalaryBasic;
    private Label lbInsurancePaidDate;
    private Label lbInsuranceBookNumber;
    private Label lbPaidPlace;
    private Label lbLevels;
    private Label lbMajors;
    private Label lbCollege;
    private Label lbIdentityCard;
    private Label lbGrantDate;
    private Label lbGrantPlace;
    private Label lbMobile;
    private Label lbHomePhone;
    private Label lbEmail;

    private Listbox listSalaryLm;
    private Listbox listWorkProcess;
    
    private Staff staff;
    private ListModel model;
    private Integer index;
    
    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp);

        this.winDetailStaff = comp;
    }

    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        staff = (Staff) arg.get(Constants.OBJECT);

        model = (ListModel) arg.get(Constants.MODEL);
        
        index = (Integer) arg.get(Constants.INDEX);
        
        initData();
    }

    public void initData() {
        if (Validator.isNotNull(staff)) {
            winDetailStaff.setTitle(Labels.getLabel(LanguageKeys.TITLE_STAFF_DETAIL,
                    new Object[]{staff.getStaffName()}));

            this.onLoadData();

            this.onLoadSalaryLandmark();

            this.onLoadWorkProcess();
        }
    }

    public void onLoadData() {
        lbStaffName.setValue(staff.getStaffName());
        lbDeptName.setValue(staff.getDepartment()!=null?
                staff.getDepartment().getDeptName():StringPool.BLANK);
        lbJobTitle.setValue(staff.getJob()!=null?
                staff.getJob().getJobTitle():StringPool.BLANK);
        lbWorkDate.setValue(GetterUtil.getDate(
                staff.getWorkDate(), DateUtil.SHORT_DATE_PATTERN));
        lbDateOfBirth.setValue(GetterUtil.getDate(
                staff.getDateOfBirth(), DateUtil.SHORT_DATE_PATTERN));
        lbPermanentResidence.setValue(staff.getPermanentResidence());
        lbCurrentResidence.setValue(staff.getCurrentResidence());
        lbStatus.setValue(Values.getStaffStatus(staff.getStatus()));
        lbNote.setValue(staff.getNote());
        lbContractType.setValue(staff.getContractType()!=null?
                staff.getContractType().getContractTypeName():StringPool.BLANK);
        lbContractFromDate.setValue(GetterUtil.getDate(
                staff.getContractFromDate(), DateUtil.SHORT_DATE_PATTERN));
        lbContractToDate.setValue(GetterUtil.getDate(
                staff.getContractToDate(), DateUtil.SHORT_DATE_PATTERN));
        lbContractNumber.setValue(staff.getContractNumber());
        lbTaxCode.setValue(staff.getTaxCode());
        lbSalaryBasic.setValue(GetterUtil.getFormat(staff.getSalaryBasic()));
        lbInsurancePaidDate.setValue(GetterUtil.getDate(
                staff.getInsurancePaidDate(), DateUtil.SHORT_DATE_PATTERN));
        lbInsuranceBookNumber.setValue(staff.getInsuranceBookNumber());
        lbPaidPlace.setValue(staff.getPaidPlace());
        lbLevels.setValue(staff.getLevels());
        lbMajors.setValue(staff.getMajors());
        lbCollege.setValue(staff.getCollege());
        lbIdentityCard.setValue(staff.getIdentityCard());
        lbGrantDate.setValue(GetterUtil.getDate(
                staff.getGrantDate(), DateUtil.SHORT_DATE_PATTERN));
        lbGrantPlace.setValue(staff.getGrantPlace());
        lbMobile.setValue(staff.getMobile());
        lbHomePhone.setValue(staff.getHomePhone());
        lbEmail.setValue(staff.getEmail());
    }

    public void onLoadSalaryLandmark() {
        List<SalaryLandmark> salaryLandmarks = 
                staffService.getSalaryLandmarkByStaffId(staff.getStaffId());

        listSalaryLm.setItemRenderer(new SalaryLandmarkRender(winDetailStaff, staff));
        listSalaryLm.setModel(new ListModelList<SalaryLandmark>(salaryLandmarks));
    }

    public void onLoadWorkProcess() {
        List<WorkProcess> workProcesses = 
                staffService.getWorkProcessByStaffId(staff.getStaffId());
        
        listWorkProcess.setItemRenderer(new WorkProcessRender(winDetailStaff, staff));
        listWorkProcess.setModel(new ListModelList<WorkProcess>(workProcesses));
    }

    public void onClick$btnCancel() {
        winDetailStaff.detach();
    }

    public void onDeleteStaff(Event event) throws Exception {
        final SalaryLandmark salaryLm = (SalaryLandmark) event.getData();

        Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
                Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE),
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new EventListener() {

                    public void onEvent(Event e) throws Exception {
                        if (Messagebox.ON_OK.equals(e.getName())) {
                            try {
                                staffService.deleteSalaryLm(salaryLm);

                                ComponentUtil.createSuccessMessageBox(
                                        LanguageKeys.MESSAGE_DELETE_SUCCESS);

                                onLoadSalaryLandmark();
                            } catch (Exception ex) {
                                _log.error(ex.getMessage(), ex);

                                Messagebox.show(Labels.getLabel(
                                                LanguageKeys.MESSAGE_DELETE_FAIL));
                            }
                        }
                    }
                });
    }
    
    public void onDeleteWorkProcess(Event event) throws Exception {
        final WorkProcess wp = (WorkProcess) event.getData();

        Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
                Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE),
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new EventListener() {

                    public void onEvent(Event e) throws Exception {
                        if (Messagebox.ON_OK.equals(e.getName())) {
                            try {
                                staffService.deleteWorkProcess(wp);

                                ComponentUtil.createSuccessMessageBox(
                                        LanguageKeys.MESSAGE_DELETE_SUCCESS);

                                onLoadWorkProcess();
                            } catch (Exception ex) {
                                _log.error(ex.getMessage(), ex);

                                Messagebox.show(Labels.getLabel(
                                                LanguageKeys.MESSAGE_DELETE_FAIL));
                            }
                        }
                    }
                });
    }

    public void onClick$btnAddSalaryLm() {
        Map map = new HashMap();

        map.put(Constants.PARENT_WINDOW, winDetailStaff);
        map.put(Constants.OBJECT, staff);

        Window win = (Window) Executions.createComponents(
                ADD_EDIT_SALARY_LANDMARK_PAGE, null, map);

        win.doModal();
    }
    
    public void onClick$btnAddWorkProcess() {
        Map map = new HashMap();

        map.put(Constants.PARENT_WINDOW, winDetailStaff);
        map.put(Constants.OBJECT, staff);

        Window win = (Window) Executions.createComponents(
                ADD_EDIT_WORK_PROCESS_PAGE, null, map);

        win.doModal();
    }
    
    public void onLoadReloadSalaryLm(Event event) {        
        this.onLoadSalaryLandmark();
    }
    
    public void onLoadReloadWp(Event event) {        
        this.onLoadWorkProcess();
    }
    
    public void onClick$btnPrevious() throws Exception{
        if(index == 0){
            index = (model.getSize() - 1);
        } else {
            index--;
        }
        
        staff = (Staff) model.getElementAt(index);
        
        initData();
    }
    
    public void onClick$btnNext() throws Exception{
        if(index == (model.getSize() - 1)){
            index = 0;
        } else {
            index++;
        }
        
        staff = (Staff) model.getElementAt(index);
        
        initData();
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

    private static final String ADD_EDIT_SALARY_LANDMARK_PAGE =
            "/html/pages/manager_human_resource/editSalaryLandmark.zul";
    private static final String ADD_EDIT_WORK_PROCESS_PAGE =
            "/html/pages/manager_human_resource/editWorkProcess.zul";
    
    private static final Logger _log
            = LogManager.getLogger(DetailStaffController.class);
}
