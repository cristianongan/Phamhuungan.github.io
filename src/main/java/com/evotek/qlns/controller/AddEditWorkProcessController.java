/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.controller;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Window;

import com.evotek.qlns.extend.Messagebox;
import com.evotek.qlns.model.Staff;
import com.evotek.qlns.model.WorkProcess;
import com.evotek.qlns.service.StaffService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author LinhLH
 */
public class AddEditWorkProcessController extends BasicController<Window>{
    private Window winEditWp;
    private Window winTemp;

    private Datebox dbFromDate;
    private Datebox dbToDate;
    
    private Combobox cbCompany;
    private Combobox cbJobTitle;
    
    private WorkProcess wp;
    private Staff staff;
    
    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp);
        
        this.winEditWp = comp;
    }

    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);
        
        this.initData();
    }
    
    //init data
    public void initData() throws Exception {
        try {
            winTemp = (Window) arg.get(Constants.PARENT_WINDOW);

            staff = (Staff) arg.get(Constants.OBJECT);
            wp = (WorkProcess) arg.get(Constants.EDIT_OBJECT);

            if(Validator.isNotNull(wp)){
                winEditWp.setTitle((String) arg.get(Constants.TITLE));

                this._setEditForm();
            } 
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }
    
    private void _setEditForm() {
        dbFromDate.setValue(wp.getFromDate());
        dbToDate.setValue(wp.getToDate());
        cbCompany.setValue(wp.getCompany());
        cbJobTitle.setValue(wp.getJobTitle());
    }
    
    public void onCreate$cbDepartment() {
        ListModel model = new SimpleListModel(staffService.getCompanyName());
        
        cbCompany.setModel(model);
    }
    
    public void onCreate$cbJobTitle() {
        ListModel model = new SimpleListModel(staffService.getTotalJobTitle());
        
        cbJobTitle.setModel(model);
    }
    
    public void onClick$btnSave() throws Exception {
        boolean update = true;

        try {
            Date fromDate = dbFromDate.getValue();
            Date toDate = dbToDate.getValue();
            String company = GetterUtil.getString(cbCompany.getValue());
            String jobTitle = GetterUtil.getString(cbJobTitle.getValue());
            
            if(_validate(company)){
                if(Validator.isNull(wp)){
                    update = false;
                    
                    wp = new WorkProcess();
                    
                    wp.setUserId(getUserId());
                    wp.setUserName(getUserName());
                    wp.setCreateDate(new Date());
                    wp.setStaffId(staff.getStaffId());
                }
                
                wp.setFromDate(fromDate);
                wp.setToDate(toDate);
                wp.setCompany(company);
                wp.setJobTitle(jobTitle);
                wp.setModifiedDate(new Date());
                
                staffService.saveOrUpdate(wp);
                
                ComponentUtil.createSuccessMessageBox(
                        ComponentUtil.getSuccessKey(update));

                winEditWp.detach();

                Events.sendEvent("onLoadReloadWp", winTemp, null);
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);

            Messagebox.show(Labels.getLabel(
                    ComponentUtil.getFailKey(update)));
        }
    }
    
    private boolean _validate(String company){
        if (Validator.isNull(company)) {
            cbCompany.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.WORK_AT)));
            
            cbCompany.setFocus(true);
            
            return false;
        }
        
        return true;
    }
    
    public void onClick$btnCancel(){
        winEditWp.detach();
    }
    //get set service
    public StaffService getStaffService() {
        if (staffService == null) {
            staffService = (StaffService) SpringUtil.getBean("staffService");
            setStaffService(staffService);
        }
        return staffService;
    }

    public void setStaffService(StaffService StaffService) {
        this.staffService = StaffService;
    }

    private transient StaffService staffService;
    
    private static final Logger _log
            = LogManager.getLogger(AddEditSalaryLmController.class);
}
