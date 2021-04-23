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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.extend.Messagebox;
import com.evotek.qlns.model.SalaryLandmark;
import com.evotek.qlns.model.Staff;
import com.evotek.qlns.service.StaffService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author My PC
 */
public class AddEditSalaryLmController extends BasicController<Window>{
    /**
	 * 
	 */
	private static final long serialVersionUID = -420472383138292995L;
	
	private Window winEditSalaryLm;
    private Window winTemp;

    private Datebox dbFromDate;
    private Datebox dbToDate;
    
    private Longbox lgbSalary;
    
    private SalaryLandmark salaryLm;
    private Staff staff;
    
    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp);
        
        this.winEditSalaryLm = comp;
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
            this.salaryLm = (SalaryLandmark) this.arg.get(Constants.EDIT_OBJECT);

            if(Validator.isNotNull(this.salaryLm)){
                this.winEditSalaryLm.setTitle((String) this.arg.get(Constants.TITLE));

                this._setEditForm();
            } 
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }
    
    private void _setEditForm() {
        this.dbFromDate.setValue(this.salaryLm.getFromDate());
        this.dbToDate.setValue(this.salaryLm.getToDate());
        this.lgbSalary.setValue(this.salaryLm.getSalary());
    }
    
    public void onClick$btnSave() throws Exception {
        boolean update = true;

        try {
            Date fromDate = this.dbFromDate.getValue();
            Date toDate = this.dbToDate.getValue();
            Long salary = this.lgbSalary.getValue();
            
            if(_validate(salary)){
                if(Validator.isNull(this.salaryLm)){
                    update = false;
                    
                    this.salaryLm = new SalaryLandmark();
                    
                    this.salaryLm.setUserId(getUserId());
                    this.salaryLm.setUserName(getUserName());
                    this.salaryLm.setCreateDate(new Date());
                }
                
                this.salaryLm.setFromDate(fromDate);
                this.salaryLm.setToDate(toDate);
                this.salaryLm.setSalary(salary);
                this.salaryLm.setModifiedDate(new Date());
                
                this.staffService.saveOrUpdate(this.salaryLm);
                
                ComponentUtil.createSuccessMessageBox(
                        ComponentUtil.getSuccessKey(update));

                this.winEditSalaryLm.detach();

                Events.sendEvent("onLoadReloadSalaryLm", this.winTemp, null);
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);

            Messagebox.show(Labels.getLabel(
                    ComponentUtil.getFailKey(update)));
        }
    }
    
    private boolean _validate(Long salary){
        if (Validator.isNull(salary)) {
            this.lgbSalary.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.SALARY)));
            
            this.lgbSalary.setFocus(true);
            
            return false;
        }
        
        return true;
    }
    
    public void onClick$btnCancel(){
        this.winEditSalaryLm.detach();
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
