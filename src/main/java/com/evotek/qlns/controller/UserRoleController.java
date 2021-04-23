/*
 * Copyright 2013 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Role;
import com.evotek.qlns.model.User;
import com.evotek.qlns.model.list.SimpleModelList;
import com.evotek.qlns.model.render.UserRoleRender;
import com.evotek.qlns.service.UserService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;


/**
 *
 * @author hungnt81
 */
public class UserRoleController extends BasicController<Window>
        implements Serializable {

    private Window winUserRole;
    private Div winParent;

    private Listbox gridRole;

    private User user;

    private boolean isAdmin;

    private Set<Role> _roles;

    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp);

        this.winUserRole = comp;
    }


    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        //init data
        this.initData();
        
    }

    public void initData() throws Exception {
        try {
            winParent = (Div) arg.get(Constants.PARENT_WINDOW);

            user = (User) arg.get(Constants.OBJECT);

            winUserRole.setTitle(Labels.getLabel(
                    LanguageKeys.ROLE_ASSIGNED_TO_USER,
                    new Object[]{user.getUserName()}));

            isAdmin = this.getUserWorkspace().isAdministrator();

            this.onSearch();
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public void onClick$btnCancel() {
        winUserRole.detach();
    }

    public void onSearch() {
        if (user != null) {
            _roles = user.getRoles();

            gridRole.setItemRenderer(new UserRoleRender());
            gridRole.setModel(new SimpleModelList<Role>(_roles));
            gridRole.setMultiple(true);
        }
    }

    public void onClick$btnAdd() {
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put(Constants.PARENT_WINDOW, winUserRole);
        parameters.put(Constants.OBJECT, user);
        parameters.put(Constants.SECOND_OBJECT, _roles);

        Window win = (Window) Executions.createComponents(ADD_ROLE_PAGE,
                winUserRole, parameters);

        win.doModal();
    }

    public void onClick$btnDelete() {
        final List<Role> roles = this.getRoleSelected();

        if (Validator.isNull(roles)) {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD),
                    Labels.getLabel(LanguageKeys.ERROR), Messagebox.OK,
                    Messagebox.EXCLAMATION);
        } else {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
                    Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE),
                    Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new EventListener<Event>() {

                        public void onEvent(Event e) throws Exception {
                            if (Messagebox.ON_OK.equals(e.getName())) {
                                try {
                                    userService.delete(roles, user);

                                    ComponentUtil.createSuccessMessageBox(
                                                 LanguageKeys.MESSAGE_DELETE_SUCCESS);

                                    onSearch();
                                } catch (Exception ex) {
                                    _log.error(ex.getMessage(), ex);

                                    Messagebox.show(Labels.getLabel(
                                            LanguageKeys.MESSAGE_DELETE_FAIL));
                                }
                            }
                        }
                    });
        }
    }

    public void onLoadData(Event event) throws Exception{
        this.onSearch();
    }

    private List<Role> getRoleSelected(){
        List<Role> roles = new ArrayList<Role>();

        for (Listitem item : gridRole.getSelectedItems()) {
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

    private static final String ADD_ROLE_PAGE =
            "/html/pages/manager_user/add_role.zul";

    private static final Logger _log = LogManager.getLogger(UserRoleController.class);
}
