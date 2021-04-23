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
            this.winTemp = (Window) this.arg.get(Constants.PARENT_WINDOW);

            this.staff = (Staff) this.arg.get(Constants.OBJECT);
            this.wp = (WorkProcess) this.arg.get(Constants.EDIT_OBJECT);

            if(Validator.isNotNull(this.wp)){
                this.winEditWp.setTitle((String) this.arg.get(Constants.TITLE));

                this._setEditForm();
            } 
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }
    
    private void _setEditForm() {
        this.dbFromDate.setValue(this.wp.getFromDate());
        this.dbToDate.setValue(this.wp.getToDate());
        this.cbCompany.setValue(this.wp.getCompany());
        this.cbJobTitle.setValue(this.wp.getJobTitle());
    }
    
    public void onCreate$cbDepartment() {
        ListModel model = new SimpleListModel(this.staffService.getCompanyName());
        
        this.cbCompany.setModel(model);
    }
    
    public void onCreate$cbJobTitle() {
        ListModel model = new SimpleListModel(this.staffService.getTotalJobTitle());
        
        this.cbJobTitle.setModel(model);
    }
    
    public void onClick$btnSave() throws Exception {
        boolean update = true;

        try {
            Date fromDate = this.dbFromDate.getValue();
            Date toDate = this.dbToDate.getValue();
            String company = GetterUtil.getString(this.cbCompany.getValue());
            String jobTitle = GetterUtil.getString(this.cbJobTitle.getValue());
            
            if(_validate(company)){
                if(Validator.isNull(this.wp)){
                    update = false;
                    
                    this.wp = new WorkProcess();
                    
                    this.wp.setUserId(getUserId());
                    this.wp.setUserName(getUserName());
                    this.wp.setCreateDate(new Date());
                    this.wp.setStaffId(this.staff.getStaffId());
                }
                
                this.wp.setFromDate(fromDate);
                this.wp.setToDate(toDate);
                this.wp.setCompany(company);
                this.wp.setJobTitle(jobTitle);
                this.wp.setModifiedDate(new Date());
                
                this.staffService.saveOrUpdate(this.wp);
                
                ComponentUtil.createSuccessMessageBox(
                        ComponentUtil.getSuccessKey(update));

                this.winEditWp.detach();

                Events.sendEvent("onLoadReloadWp", this.winTemp, null);
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);

            Messagebox.show(Labels.getLabel(
                    ComponentUtil.getFailKey(update)));
        }
    }
    
    private boolean _validate(String company){
        if (Validator.isNull(company)) {
            this.cbCompany.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.WORK_AT)));
            
            this.cbCompany.setFocus(true);
            
            return false;
        }
        
        return true;
    }
    
    public void onClick$btnCancel(){
        this.winEditWp.detach();
    }
    //get set service
    public StaffService getStaffService() {
        if (this.staffService == null) {
            this.staffService = (StaffService) SpringUtil.getBean("staffService");
            setStaffService(this.staffService);
        }
        return this.staffService;
    }

    public void setStaffService(StaffService StaffService) {
        this.staffService = StaffService;
    }

    private transient StaffService staffService;
    
    private static final Logger _log
            = LogManager.getLogger(AddEditSalaryLmController.class);
}
