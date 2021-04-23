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
            winTemp = (Div) arg.get(Constants.PARENT_WINDOW);

            role = (Role) arg.get(Constants.EDIT_OBJECT);

            if(Validator.isNotNull(role)){
                winAddRole.setTitle((String) arg.get(Constants.TITLE));

                this._setEditForm();
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    private void _setEditForm(){
        tbRoleName.setValue(role.getRoleName());
        tbDescription.setValue(role.getDescription());

        chbShareable.setChecked(role.getShareable());
    }

    //event method
    public void onClick$btnCancel(){
        winAddRole.detach();
    }

    public void onClick$btnSave() {
        boolean update = true;

        try {
            String roleName = GetterUtil.getString(tbRoleName.getValue());
            String description = GetterUtil.getString(tbDescription.getValue());

            if(_validate(roleName, description)){
                if(Validator.isNull(role)){
                    update = false;

                    role = new Role();

                    role.setUserId(getUserId());
                    role.setUserName(getUserName());
                    role.setCreateDate(new Date());
                    role.setStatus(Values.STATUS_ACTIVE);
                    role.setImmune(Values.NOT_IMMUNE);
                }

                role.setRoleName(roleName);
                role.setDescription(description);
                role.setModifiedDate(new Date());
                role.setShareable(chbShareable.isChecked());

                roleService.saveOrUpdateRole(role);

                ComponentUtil.createSuccessMessageBox(
                        ComponentUtil.getSuccessKey(update));

                winAddRole.detach();

                Events.sendEvent("onLoadRole", winTemp, null);
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
            tbRoleName.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.ROLE_NAME)));

            return false;
        }
        
        if(!Validator.isCodeString(roleName)){
            tbRoleName.setErrorMessage(Values.getFormatInvalidMsg(
                    Labels.getLabel(LanguageKeys.ROLE_NAME)));

            return false;
        }

        if(roleService.isRoleExist(roleName, role)){
            tbRoleName.setErrorMessage(Values.getDuplicateMsg(
                    Labels.getLabel(LanguageKeys.ROLE_NAME)));

            return false;
        }
        
        if(roleName.length() > Values.MEDIUM_LENGTH){
            tbRoleName.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.ROLE_NAME),
                    Values.MEDIUM_LENGTH));

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
