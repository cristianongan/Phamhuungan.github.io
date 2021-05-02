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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
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
@Controller
@Scope("prototype")
public class RoleController extends BasicController<Div> implements Serializable {

	private static final long serialVersionUID = 1371435022018L;

	private static final Logger _log = LogManager.getLogger(RoleController.class);

	private static final String EDIT_PAGE = "~./pages/manager_role/edit.zul";

	@Autowired
	private RoleService roleService;

//    private Textbox roleName;

//    private Combobox status;

	private Div winRole;

	private Include parent;

	private Listbox searchResultGrid;

	private Map<String, Object> paramMap = new HashMap<String, Object>();

	// event method

//    public void onAfterRender$status(){
//        status.setSelectedIndex(Values.FIRST_INDEX);
//    }

	@Override
	public void doAfterCompose(Div comp) throws Exception {
		super.doAfterCompose(comp);
		// Init data
		this.initData();

		this.search();
	}

	@Override
	public void doBeforeComposeChildren(Div comp) throws Exception {
		super.doBeforeComposeChildren(comp);

		this.winRole = comp;
	}

	public List<Role> getRoles() throws Exception {
		List<Role> _roles = new ArrayList<Role>();

		try {
//            String _roleName = GetterUtil.getString(roleName.getValue());
//            Long _status = ComponentUtil.getComboboxValue(status);

			// create param map
//            paramMap.put("roleName", _roleName);
//            paramMap.put("status", _status);

			_roles = this.roleService.getRoles(null, null);
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return _roles;
	}

	public void initData() throws Exception {
		try {
			this.parent = (Include) this.winRole.getParent();

//            List<SimpleModel> statusList = ComponentUtil.getComboboxStatusValue(
//                    new String[]{Labels.getLabel(LanguageKeys.STATUS_ACTIVE),
//                    Labels.getLabel(LanguageKeys.STATUS_NOT_ACTIVE)}, true);

//            status.setModel(new ListModelList<SimpleModel>(statusList));
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	public void onClick$adminPage() {
		this.parent.setSrc("~./pages/admin/default.zul");
	}

	public void onClick$btnAdd() {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(Constants.PARENT_WINDOW, this.winRole);
			parameters.put(Constants.ID, 0L);

			Window win = (Window) Executions.createComponents(EDIT_PAGE, this.winRole, parameters);
			win.doModal();
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	public void onClick$btnCancel() {
		this.winRole.detach();
	}

	public void onClick$btnSearch() throws Exception {
		this.search();
	}

	public void onDeleteRole(Event event) throws Exception {
		final Role role = (Role) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener<Event>() {

					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								RoleController.this.roleService.deleteRole(role);

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_DELETE_SUCCESS);

								search();
							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_DELETE_FAIL));
							}
						}
					}
				});
	}

	public void onLoadRole(Event event) throws Exception {
		this.search();
	}
	// event method

	public void onLockRole(Event event) throws Exception {
		final Role role = (Role) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_LOCK),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_LOCK), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener<Event>() {

					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								RoleController.this.roleService.lockRole(role);

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_LOCK_ITEM_SUCCESS);

								search();
							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_LOCK_ITEM_FAIL));
							}
						}
					}
				});
	}

	public void onUnlockRole(Event event) throws Exception {
		final Role role = (Role) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_UNLOCK),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_UNLOCK), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener<Event>() {

					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								RoleController.this.roleService.unlockRole(role);

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_UNLOCK_ITEM_SUCCESS);

								search();
							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_UNLOCK_ITEM_FAIL));
							}
						}
					}
				});
	}

	private void search() throws Exception {
		try {
			List<Role> roles = this.getRoles();

			this.searchResultGrid.setItemRenderer(new RoleRender(this.winRole));
			this.searchResultGrid.setModel(new ListModelList<Role>(roles));
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}
}
