/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.A;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.extend.Messagebox;
import com.evotek.qlns.model.Department;
import com.evotek.qlns.service.DepartmentService;
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
public class AddEditDeptController extends BasicController<Window>
        implements Serializable{
    private Window winEditDept;
    private Window winParent;
    
    private Textbox tbDeptName;
    private Textbox tbDescription;
    
    //Bandbox documentType
    private Bandbox bbDept;
    
    private Include icDepartment;
    
    private A btnClearDoc;
    //Bandbox documentType
    
    private Department dept;
    private Department parentDept;
    private Department oldParentDept;
    
    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp); 
        
        this.winEditDept = comp;
    }
    
    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        initData();
    }
    
    //init data
    public void initData() throws Exception {
        try {
            winParent = (Window) arg.get(Constants.PARENT_WINDOW);

            dept = (Department) arg.get(Constants.OBJECT);
            parentDept = (Department) arg.get(Constants.SECOND_OBJECT);

            if (Validator.isNotNull(parentDept)) {
                bbDept.setValue(parentDept.getDeptName());
                bbDept.setAttribute(Constants.ID,
                        parentDept.getDeptId());
                bbDept.setAttribute(Constants.OBJECT,
                        parentDept);
            }
            
            if(Validator.isNotNull(dept)){
                winEditDept.setTitle((String) arg.get(Constants.TITLE));
                
                if(Validator.isNotNull(dept.getParentId())){
                    oldParentDept = departmentService.getDeparment(dept.getParentId());
                }
                
                this._setEditForm();
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }
    
    private void _setEditForm() throws Exception {
        tbDeptName.setValue(dept.getDeptName());
        tbDescription.setValue(dept.getDescription());
        
        if (Validator.isNotNull(oldParentDept)) {
            bbDept.setValue(oldParentDept.getDeptName());
            bbDept.setAttribute(Constants.ID,
                    oldParentDept.getDeptId());
            bbDept.setAttribute(Constants.OBJECT,
                        oldParentDept);
            btnClearDoc.setVisible(true);
        }
    }
    
    public void onClick$btnSave() throws Exception{
        boolean update = true;
        
        try {
            String deptName = GetterUtil.getString(tbDeptName.getValue());
            String description = GetterUtil.getString(tbDescription.getValue());
            parentDept = (Department) bbDept.getAttribute(Constants.OBJECT);
            
            if (this._validate(deptName, description)) {
                if(Validator.isNull(dept)){
                    update = false;
                    
                    dept = new Department();
                    
                    dept.setUserId(getUserId());
                    dept.setUserName(getUserName());
                    dept.setCreateDate(new Date());
                    dept.setOrdinal(getOrdinal());
                }
                
                dept.setDeptName(deptName);
                dept.setDescription(description);
                dept.setModifiedDate(new Date());
                dept.setParentId(Validator.isNull(parentDept)?null:parentDept.getDeptId());
                
                departmentService.saveOrUpdate(dept);
                
                ComponentUtil.createSuccessMessageBox(
                        ComponentUtil.getSuccessKey(update));
                
                winEditDept.detach();

                Events.sendEvent("onLoadData", winParent, null);
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
            
            Messagebox.show(Labels.getLabel(
                    ComponentUtil.getFailKey(update)));
        }
    }

    public boolean _validate(String deptName, String description) {
        if (Validator.isNull(deptName)) {
            tbDeptName.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.DEPARMENT_NAME)));
            
            tbDeptName.setFocus(true);
            
            return false;
        }

        if (deptName.length() > Values.LONG_LENGTH) {
            tbDeptName.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.DOCUMENT_TYPE_NAME),
                    Values.LONG_LENGTH));
            
            tbDeptName.setFocus(true);
            
            return false;
        }
        
        if (Validator.isNotNull(description)
                && description.length() > Values.GREATE_LONG_LENGTH) {
            tbDescription.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.DESCRIPTION),
                    Values.GREATE_LONG_LENGTH));

            return false;
        }
        
        return true;
    }
    
    private Long getOrdinal(){        
        if(Validator.isNull(parentDept)){
            return departmentService.getNextOrdinal(null);
        } else {
            return departmentService.getNextOrdinal(parentDept.getDeptId());
        }
    }
    
    public void onClick$btnCancel() {
        winEditDept.detach();
    }

    //Bandbox documentType
    public void onClick$btnClearDoc() {
        bbDept.setValue(StringPool.BLANK);
        bbDept.setAttribute(Constants.ID, null);
        
        btnClearDoc.setDisabled(true);
        btnClearDoc.setVisible(false);
    }
    
    public void onOpen$bbDept(){
        if(bbDept.isOpen() 
                && Validator.isNull(icDepartment.getSrc())) {
            icDepartment.setAttribute("bandbox", bbDept);
            icDepartment.setAttribute("btnclear", btnClearDoc);
            
            if(Validator.isNotNull(dept)){
                icDepartment.setAttribute("exclude", dept);
            }
            
            icDepartment.setSrc(Constants.TREE_DEPARTMENT_PAGE);
        }
    }
    //Bandbox documentType
    
    public DepartmentService getDepartmentService() {
        if (departmentService == null) {
            departmentService = (DepartmentService) SpringUtil.getBean("departmentService");
            
            setDepartmentService(departmentService);
        }
        
        return departmentService;
    }

    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    private transient DepartmentService departmentService;
    
    private static final Logger _log =	
            LogManager.getLogger(AddEditDeptController.class);
}
