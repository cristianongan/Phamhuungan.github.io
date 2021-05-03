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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
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
 * @author LinhLH
 */
@Controller
@Scope("prototype")
public class UserRoleController extends BasicController<Window> implements Serializable {

	private static final long serialVersionUID = -8558183729803388458L;

	private static final Logger _log = LogManager.getLogger(UserRoleController.class);

	@Autowired
	private UserService userService;

	private Div winParent;

	private Listbox gridRole;

	private Set<Role> _roles;

	private User user;

	private Window winUserRole;

	private boolean isAdmin;

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		// init data
		this.initData();

	}

	@Override
	public void doBeforeComposeChildren(Window comp) throws Exception {
		super.doBeforeComposeChildren(comp);

		this.winUserRole = comp;
	}

	private List<Role> getRoleSelected() {
		List<Role> roles = new ArrayList<Role>();

		for (Listitem item : this.gridRole.getSelectedItems()) {
			Role role = (Role) item.getAttribute("data");

			if (Validator.isNotNull(role)) {
				roles.add(role);
			}
		}

		return roles;
	}

	public void initData() throws Exception {
		try {
			this.winParent = (Div) this.arg.get(Constants.Attr.PARENT_WINDOW);

			this.user = (User) this.arg.get(Constants.Attr.OBJECT);

			this.winUserRole.setTitle(
					Labels.getLabel(LanguageKeys.ROLE_ASSIGNED_TO_USER, new Object[] { this.user.getUserName() }));

			this.isAdmin = this.getUserWorkspace().isAdministrator();

			this.onSearch();
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	public void onClick$btnAdd() {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Constants.Attr.PARENT_WINDOW, this.winUserRole);
		parameters.put(Constants.Attr.OBJECT, this.user);
		parameters.put(Constants.Attr.SECOND_OBJECT, this._roles);

		Window win = (Window) Executions.createComponents(Constants.Page.ManagerUser.ADD_ROLE, this.winUserRole,
				parameters);

		win.doModal();
	}

	public void onClick$btnCancel() {
		this.winUserRole.detach();
	}

	public void onClick$btnDelete() {
		final List<Role> roles = this.getRoleSelected();

		if (Validator.isNull(roles)) {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD), Labels.getLabel(LanguageKeys.ERROR),
					Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
					Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE), Messagebox.OK | Messagebox.CANCEL,
					Messagebox.QUESTION, new EventListener<Event>() {

						@Override
						public void onEvent(Event e) throws Exception {
							if (Messagebox.ON_OK.equals(e.getName())) {
								try {
									UserRoleController.this.userService.delete(roles, UserRoleController.this.user);

									ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_DELETE_SUCCESS);

									onSearch();
								} catch (Exception ex) {
									_log.error(ex.getMessage(), ex);

									Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_DELETE_FAIL));
								}
							}
						}
					});
		}
	}

	public void onLoadData(Event event) throws Exception {
		this.onSearch();
	}

	public void onSearch() {
		if (this.user != null) {
			this._roles = this.user.getRoles();

			this.gridRole.setItemRenderer(new UserRoleRender());
			this.gridRole.setModel(new SimpleModelList<Role>(this._roles));
			this.gridRole.setMultiple(true);
		}
	}
}
