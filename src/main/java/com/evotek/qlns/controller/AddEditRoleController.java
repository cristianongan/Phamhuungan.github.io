/*
 * Copyright 2013 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.controller;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Role;
import com.evotek.qlns.service.RoleService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;


/**
 *
 * @author LinhLH2
 */
public class AddEditRoleController extends BasicController<Window> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6903205783062391225L;
	private Window winAddRole;
    private Div winTemp;

    private Textbox tbRoleName;
    private Textbox tbDescription;

    private Checkbox chbShareable;

    private Role role;

    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp);

        this.winAddRole = comp;
    }

    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);
        
        //init data
        this.initData();
    }

    private void initData() throws Exception {
        try {
            this.winTemp = (Div) this.arg.get(Constants.PARENT_WINDOW);

            this.role = (Role) this.arg.get(Constants.EDIT_OBJECT);

            if(Validator.isNotNull(this.role)){
                this.winAddRole.setTitle((String) this.arg.get(Constants.TITLE));

                this._setEditForm();
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    private void _setEditForm(){
        this.tbRoleName.setValue(this.role.getRoleName());
        this.tbDescription.setValue(this.role.getDescription());

        this.chbShareable.setChecked(this.role.getShareable());
    }

    //event method
    public void onClick$btnCancel(){
        this.winAddRole.detach();
    }

    public void onClick$btnSave() {
        boolean update = true;

        try {
            String roleName = GetterUtil.getString(this.tbRoleName.getValue());
            String description = GetterUtil.getString(this.tbDescription.getValue());

            if(_validate(roleName, description)){
                if(Validator.isNull(this.role)){
                    update = false;

                    this.role = new Role();

                    this.role.setUserId(getUserId());
                    this.role.setUserName(getUserName());
                    this.role.setCreateDate(new Date());
                    this.role.setStatus(Values.STATUS_ACTIVE);
                    this.role.setImmune(Values.NOT_IMMUNE);
                }

                this.role.setRoleName(roleName);
                this.role.setDescription(description);
                this.role.setModifiedDate(new Date());
                this.role.setShareable(this.chbShareable.isChecked());

                this.roleService.saveOrUpdateRole(this.role);

                ComponentUtil.createSuccessMessageBox(
                        ComponentUtil.getSuccessKey(update));

                this.winAddRole.detach();

                Events.sendEvent("onLoadRole", this.winTemp, null);
            }

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);

            Messagebox.show(Labels.getLabel(
                    ComponentUtil.getFailKey(update)));
        }
    }
    //event method

    //validate
    private boolean _validate(String roleName, String description)
            throws Exception{
        if(Validator.isNull(roleName)){
            this.tbRoleName.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.ROLE_NAME)));

            return false;
        }
        
        if(!Validator.isCodeString(roleName)){
            this.tbRoleName.setErrorMessage(Values.getFormatInvalidMsg(
                    Labels.getLabel(LanguageKeys.ROLE_NAME)));

            return false;
        }

        if(this.roleService.isRoleExist(roleName, this.role)){
            this.tbRoleName.setErrorMessage(Values.getDuplicateMsg(
                    Labels.getLabel(LanguageKeys.ROLE_NAME)));

            return false;
        }
        
        if(roleName.length() > Values.MEDIUM_LENGTH){
            this.tbRoleName.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.ROLE_NAME),
                    Values.MEDIUM_LENGTH));

            return false;
        }

        if (Validator.isNotNull(description)
                && description.length() > Values.GREATE_LONG_LENGTH) {
            this.tbDescription.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.DESCRIPTION),
                    Values.GREATE_LONG_LENGTH));

            return false;
        }

        return true;
    }


    //service
    public RoleService getRoleService() {
        if (this.roleService == null) {
            this.roleService = (RoleService)
                    SpringUtil.getBean("roleService");
            
            setRoleService(this.roleService);
        }

        return this.roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    private transient RoleService roleService;

    private static final Logger _log =
            LogManager.getLogger(AddEditRoleController.class);
}
