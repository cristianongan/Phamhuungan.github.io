/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Role;
import com.evotek.qlns.model.render.RoleRender;
import com.evotek.qlns.service.RoleService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;


/**
 *
 * @author linhlh2
 */
public class RoleController extends BasicController<Div>
        implements Serializable {

    private static final long serialVersionUID = 1371435022018L;
    
    private Div winRole;

//    private Textbox roleName;

//    private Combobox status;
    
    private Listbox searchResultGrid;

    private Include parent;
    
    private Map<String, Object> paramMap = new HashMap<String, Object>();

    @Override
    public void doBeforeComposeChildren(Div comp) throws Exception {
        super.doBeforeComposeChildren(comp);

        this.winRole = comp;
    }

    @Override
    public void doAfterCompose(Div comp) throws Exception {
        super.doAfterCompose(comp);
        //Init data
        this.initData();

        this.search();
    }

    public void initData() throws Exception {
        try {
            parent = (Include) winRole.getParent();
            
//            List<SimpleModel> statusList = ComponentUtil.getComboboxStatusValue(
//                    new String[]{Labels.getLabel(LanguageKeys.STATUS_ACTIVE),
//                    Labels.getLabel(LanguageKeys.STATUS_NOT_ACTIVE)}, true);
            
//            status.setModel(new ListModelList<SimpleModel>(statusList));
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    //event method

//    public void onAfterRender$status(){
//        status.setSelectedIndex(Values.FIRST_INDEX);
//    }

    public void onClick$btnSearch() throws Exception{
        this.search();
    }

    public void onClick$btnCancel(){
        winRole.detach();
    }

    public void onLockRole(Event event) throws Exception {
        final Role role = (Role) event.getData();

        Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_LOCK),
                Labels.getLabel(LanguageKeys.MESSAGE_INFOR_LOCK),
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new EventListener<Event>() {

                    public void onEvent(Event e) throws Exception {
                        if (Messagebox.ON_OK.equals(e.getName())) {
                            try {
                                roleService.lockRole(role);

                               ComponentUtil.createSuccessMessageBox(
                                       LanguageKeys.MESSAGE_LOCK_ITEM_SUCCESS);

                                search();
                            } catch (Exception ex) {
                                _log.error(ex.getMessage(), ex);

                                Messagebox.show(Labels.getLabel(
                                        LanguageKeys.MESSAGE_LOCK_ITEM_FAIL));
                            }
                        }
                    }
                });
    }

    public void onUnlockRole(Event event) throws Exception {
        final Role role = (Role) event.getData();

        Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_UNLOCK),
                Labels.getLabel(LanguageKeys.MESSAGE_INFOR_UNLOCK),
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new EventListener() {

                    public void onEvent(Event e) throws Exception {
                        if (Messagebox.ON_OK.equals(e.getName())) {
                            try {
                                roleService.unlockRole(role);

                                ComponentUtil.createSuccessMessageBox(
                                        LanguageKeys.MESSAGE_UNLOCK_ITEM_SUCCESS);

                                search();
                            } catch (Exception ex) {
                                _log.error(ex.getMessage(), ex);

                                Messagebox.show(Labels.getLabel(
                                        LanguageKeys.MESSAGE_UNLOCK_ITEM_FAIL));
                            }
                        }
                    }
                });
    }

    public void onDeleteRole(Event event) throws Exception {
        final Role role = (Role) event.getData();

        Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
                Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE),
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new EventListener() {

                    public void onEvent(Event e) throws Exception {
                        if (Messagebox.ON_OK.equals(e.getName())) {
                            try {
                                roleService.deleteRole(role);

                                ComponentUtil.createSuccessMessageBox(
                                        LanguageKeys.MESSAGE_DELETE_SUCCESS);

                                search();
                            } catch (Exception ex) {
                                _log.error(ex.getMessage(), ex);

                                Messagebox.show(Labels.getLabel(
                                        LanguageKeys.MESSAGE_DELETE_FAIL));
                            }
                        }
                    }
                });
    }

    public void onClick$btnAdd() {
        try {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put(Constants.PARENT_WINDOW, winRole);
            parameters.put(Constants.ID, 0L);

            Window win = (Window) Executions.createComponents(EDIT_PAGE,
                    winRole, parameters);
            win.doModal();
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public void onLoadRole(Event event) throws Exception{
        this.search();
    }
    //event method

    private void search() throws Exception {
        try {
            List<Role> roles = this.getRoles();

            searchResultGrid.setItemRenderer(new RoleRender(winRole));
            searchResultGrid.setModel(new ListModelList<Role>(roles));
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public List<Role> getRoles() throws Exception {
        List<Role> _roles = new ArrayList<Role>();

        try {
//            String _roleName = GetterUtil.getString(roleName.getValue());
//            Long _status = ComponentUtil.getComboboxValue(status);

            //create param map
//            paramMap.put("roleName", _roleName);
//            paramMap.put("status", _status);

            _roles = roleService.getRoles(null, null);
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return _roles;
    }

    public void onClick$adminPage(){
        parent.setSrc("/html/pages/admin/default.zul");
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

    private static final String EDIT_PAGE =
            "/html/pages/manager_role/edit.zul";

    private static final Logger _log =
            LogManager.getLogger(RoleController.class);
}