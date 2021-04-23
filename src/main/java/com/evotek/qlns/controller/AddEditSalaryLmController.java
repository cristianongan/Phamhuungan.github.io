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
            winTemp = (Window) arg.get(Constants.PARENT_WINDOW);

            staff = (Staff) arg.get(Constants.OBJECT);
            salaryLm = (SalaryLandmark) arg.get(Constants.EDIT_OBJECT);

            if(Validator.isNotNull(salaryLm)){
                winEditSalaryLm.setTitle((String) arg.get(Constants.TITLE));

                this._setEditForm();
            } 
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }
    
    private void _setEditForm() {
        dbFromDate.setValue(salaryLm.getFromDate());
        dbToDate.setValue(salaryLm.getToDate());
        lgbSalary.setValue(salaryLm.getSalary());
    }
    
    public void onClick$btnSave() throws Exception {
        boolean update = true;

        try {
            Date fromDate = dbFromDate.getValue();
            Date toDate = dbToDate.getValue();
            Long salary = lgbSalary.getValue();
            
            if(_validate(salary)){
                if(Validator.isNull(salaryLm)){
                    update = false;
                    
                    salaryLm = new SalaryLandmark();
                    
                    salaryLm.setUserId(getUserId());
                    salaryLm.setUserName(getUserName());
                    salaryLm.setCreateDate(new Date());
                }
                
                salaryLm.setFromDate(fromDate);
                salaryLm.setToDate(toDate);
                salaryLm.setSalary(salary);
                salaryLm.setModifiedDate(new Date());
                
                staffService.saveOrUpdate(salaryLm);
                
                ComponentUtil.createSuccessMessageBox(
                        ComponentUtil.getSuccessKey(update));

                winEditSalaryLm.detach();

                Events.sendEvent("onLoadReloadSalaryLm", winTemp, null);
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);

            Messagebox.show(Labels.getLabel(
                    ComponentUtil.getFailKey(update)));
        }
    }
    
    private boolean _validate(Long salary){
        if (Validator.isNull(salary)) {
            lgbSalary.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.SALARY)));
            
            lgbSalary.setFocus(true);
            
            return false;
        }
        
        return true;
    }
    
    public void onClick$btnCancel(){
        winEditSalaryLm.detach();
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
