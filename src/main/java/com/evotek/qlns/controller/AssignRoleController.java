/*
 * Copyright 2013 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Role;
import com.evotek.qlns.model.User;
import com.evotek.qlns.model.render.UserRoleRender;
import com.evotek.qlns.service.UserService;
import com.evotek.qlns.util.CollectionUtil;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.ZkKeys;

/**
 *
 * @author hungnt81
 */
public class AssignRoleController extends BasicController<Window>
        implements Serializable {

    private Window winAssignRole;
    private Window winParent;

    private Listbox gridRole;

    private boolean isAdmin;

    private User user;

    private Set<Role> excludeRoles;

    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp);

        this.winAssignRole = comp;
    }

    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        //init
        initData();
    }

    public void initData() {
        try {
            this.winParent = (Window) this.arg.get(Constants.PARENT_WINDOW);

            this.user = (User) this.arg.get(Constants.OBJECT);

            this.excludeRoles = (Set<Role>) this.arg.get(Constants.SECOND_OBJECT);

            this.isAdmin = this.getUserWorkspace().isAdministrator();

            this.onSearch();

            
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

    }

    public void onSearch() {
        List<Role> roles = this.userService.getRoles(this.isAdmin);

        roles = (List<Role>) CollectionUtil.subtract(roles, this.excludeRoles);
        
        this.gridRole.setModel(new SimpleListModel<Role>(roles));
        this.gridRole.setItemRenderer(new UserRoleRender());
        
        this.gridRole.setMultiple(true);
    }

    public void onClick$btnCancel() {
        this.winAssignRole.detach();
    }

    public void onClick$btnAdd() {
        final List<Role> roles = this.getRoleSelected();

        if (Validator.isNull(roles)) {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD),
                    Labels.getLabel(LanguageKeys.ERROR), Messagebox.OK,
                    Messagebox.EXCLAMATION);
        } else {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_ADD),
                    Labels.getLabel(LanguageKeys.MESSAGE_INFOR_ADD),
                    Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new EventListener<Event>() {

                        @Override
						public void onEvent(Event e) throws Exception {
                            if (Messagebox.ON_OK.equals(e.getName())) {
                                try {
                                    AssignRoleController.this.userService.assignRoleToUser(AssignRoleController.this.user, roles,
                                            AssignRoleController.this.isAdmin);

                                    ComponentUtil.createSuccessMessageBox(
                                            LanguageKeys.MESSAGE_UPDATE_SUCCESS);

                                    AssignRoleController.this.winAssignRole.detach();

                                    Events.sendEvent(ZkKeys.ON_LOAD_DATA,
                                            AssignRoleController.this.winParent, null);
                                } catch (Exception ex) {
                                    _log.error(ex.getMessage(), ex);

                                    Messagebox.show(Labels.getLabel(
                                            LanguageKeys.MESSAGE_ACTIVATE_FAIL));
                                }
                            }
                        }
                    });
        }
    }


    private List<Role> getRoleSelected(){
        List<Role> roles = new ArrayList<Role>();

        for (Listitem item : this.gridRole.getSelectedItems()) {
            Role role = (Role) item.getAttribute("data");

            if(Validator.isNotNull(role)){
                roles.add(role);
            }
        }

        return roles;
    }
    //get set service

    public UserService getUserService() {
        if (this.userService == null) {
            this.userService = (UserService)
                    SpringUtil.getBean("userService");
            setUserService(this.userService);
        }
        return this.userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private transient UserService userService;
    
    private static final Logger _log =
            LogManager.getLogger(AssignRoleController.class);
}
