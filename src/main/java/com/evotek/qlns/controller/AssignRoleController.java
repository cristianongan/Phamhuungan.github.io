package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import com.evotek.qlns.extend.Messagebox;
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
 * @author LinhLH
 */

@Controller
@Scope("prototype")
public class AssignRoleController extends BasicController<Window> implements Serializable {

	private static final long serialVersionUID = -3001693200983070558L;

	private static final Logger _log = LogManager.getLogger(AssignRoleController.class);

	@Autowired
	private UserService userService;

	private Listbox gridRole;

	private Set<Role> excludeRoles;

	private User user;

	private Window winAssignRole;
	private Window winParent;

	private boolean isAdmin;

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		// init
		initData();
	}

	@Override
	public void doBeforeComposeChildren(Window comp) throws Exception {
		super.doBeforeComposeChildren(comp);

		this.winAssignRole = comp;
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

	public void initData() {
		try {
			this.winParent = (Window) this.arg.get(Constants.Attr.PARENT_WINDOW);

			this.user = (User) this.arg.get(Constants.Attr.OBJECT);

			this.excludeRoles = (Set<Role>) this.arg.get(Constants.Attr.SECOND_OBJECT);

			this.isAdmin = this.getUserWorkspace().isAdministrator();

			this.onSearch();

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

	}

	public void onClick$btnAdd() {
		final List<Role> roles = this.getRoleSelected();

		if (Validator.isNull(roles)) {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD), Labels.getLabel(LanguageKeys.ERROR),
					Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_ADD),
					Labels.getLabel(LanguageKeys.MESSAGE_INFOR_ADD), Messagebox.OK | Messagebox.CANCEL,
					Messagebox.QUESTION, new EventListener<Event>() {

						@Override
						public void onEvent(Event e) throws Exception {
							if (Messagebox.ON_OK.equals(e.getName())) {
								try {
									AssignRoleController.this.userService.assignRoleToUser(
											AssignRoleController.this.user, roles, AssignRoleController.this.isAdmin);

									ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_UPDATE_SUCCESS);

									AssignRoleController.this.winAssignRole.detach();

									Events.sendEvent(ZkKeys.ON_LOAD_DATA, AssignRoleController.this.winParent, null);
								} catch (Exception ex) {
									_log.error(ex.getMessage(), ex);

									Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_ACTIVATE_FAIL));
								}
							}
						}
					});
		}
	}

	public void onClick$btnCancel() {
		this.winAssignRole.detach();
	}

	public void onSearch() {
		List<Role> roles = this.userService.getRoles(this.isAdmin);

		roles = (List<Role>) CollectionUtil.subtract(roles, this.excludeRoles);

		this.gridRole.setModel(new SimpleListModel<Role>(roles));
		this.gridRole.setItemRenderer(new UserRoleRender());

		this.gridRole.setMultiple(true);
	}
}
