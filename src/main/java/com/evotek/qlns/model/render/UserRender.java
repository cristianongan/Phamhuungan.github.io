/*
 * Copyright 2013 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.model.render;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.evotek.qlns.model.User;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author hungnt81
 */
public class UserRender implements ListitemRenderer<User> {

	private static final String ASSIGNED_ROLE_PAGE = "~./pages/manager_user/assigned_role.zul";
	private static final String EDIT_PAGE = "~./pages/manager_user/edit.zul";

	private boolean isAdmin;

	private Div winParent;

	public UserRender(Div win, boolean isAdmin) {
		this.winParent = win;

		this.isAdmin = isAdmin;
	}

	private Map<String, Object> _createParameterMap(User user) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Constants.PARENT_WINDOW, this.winParent);
		parameters.put(Constants.TITLE, Labels.getLabel(LanguageKeys.TITLE_EDIT_USER));
		parameters.put(Constants.OBJECT, user);
		parameters.put(Constants.SECOND_OBJECT, true);

		return parameters;
	}

	private Listcell _getAction(User user) {
		if (!this.isAdmin) {
			return new Listcell();
		}

		Listcell action = new Listcell();

		Hlayout hlayout = new Hlayout();

		hlayout.setSpacing("0");

		hlayout.appendChild(ComponentUtil.createButton(null, Labels.getLabel(LanguageKeys.EDIT),
				ComponentUtil.EDIT_TOOLTIP, Events.ON_CLICK, EDIT_PAGE, _createParameterMap(user),
				Constants.Z_ICON_PENCIL, Constants.BLUE));

		Long status = user.getStatus();

		if (Values.STATUS_ACTIVE.equals(status)) {
			hlayout.appendChild(ComponentUtil.createButton(this.winParent, Labels.getLabel(LanguageKeys.BUTTON_LOCK),
					ComponentUtil.LOCK_TOOLTIP, Events.ON_CLICK, "onLockUser", user, Constants.Z_ICON_LOCK,
					Constants.ORANGE));
		} else if (Values.STATUS_DEACTIVE.equals(status)) {
			hlayout.appendChild(ComponentUtil.createButton(this.winParent, Labels.getLabel(LanguageKeys.BUTTON_UNLOCK),
					ComponentUtil.UNLOCK_TOOLTIP, Events.ON_CLICK, "onUnlockUser", user, Constants.Z_ICON_UNLOCK,
					Constants.ORANGE));
		}

		if (Values.STATUS_NOT_READY.equals(status)) {
			hlayout.appendChild(ComponentUtil.createButton(this.winParent, Labels.getLabel(LanguageKeys.BUTTON_DELETE),
					ComponentUtil.DEL_TOOLTIP, Events.ON_CLICK, "onDeleteUser", user, Constants.Z_ICON_TRASH_O,
					Constants.RED));
		}

		hlayout.appendChild(ComponentUtil.createButton(null, Labels.getLabel(LanguageKeys.ASSIGN_ROLE),
				ComponentUtil.ASSIGN_ROLE_TOOLTIP, Events.ON_CLICK, ASSIGNED_ROLE_PAGE, _createParameterMap(user),
				Constants.Z_ICON_SHARE, Constants.PURPLE));

		action.appendChild(hlayout);

		return action;
	}

	private Listcell _getGender(Long gender) {
		String[] genderTypeArray = new String[] { Labels.getLabel(LanguageKeys.MALE),
				Labels.getLabel(LanguageKeys.FEMALE) };

		Listcell genderName = new Listcell(GetterUtil.getStatusName(gender, genderTypeArray));

		return genderName;
	}

	private String _getPhoneNumber(String phone, String mobile) {
		StringBuilder sb = new StringBuilder();

		sb.append(phone);

		if (sb.length() > 0 && Validator.isNotNull(mobile)) {
			sb.append(StringPool.SLASH);
			sb.append(StringPool.SPACE);
			sb.append(mobile);
		} else {
			sb.append(mobile);
		}

		return sb.toString();
	}

	@Override
	public void render(Listitem items, User user, int index) throws Exception {
		items.setAttribute("data", user);

		items.appendChild(ComponentUtil.createListcell(StringPool.BLANK, Constants.STYLE_TEXT_ALIGN_CENTER));
		items.appendChild(ComponentUtil.createListcell(Integer.toString(index + 1), Constants.STYLE_TEXT_ALIGN_CENTER));
		items.appendChild(new Listcell(user.getUserName()));
		items.appendChild(new Listcell(user.getEmail()));
		items.appendChild(_getGender(user.getGender()));
		items.appendChild(new Listcell(_getPhoneNumber(user.getPhone(), user.getMobile())));
		items.appendChild(new Listcell(user.getFullName()));
		items.appendChild(new Listcell(Values.getLockStatus(user.getStatus())));
//        items.appendChild(ComponentUtil.createListcell(GetterUtil.getDate(
//                user.getModifiedDate(), DateUtil.LONG_DATE_PATTERN),
//                Constants.STYLE_TEXT_ALIGN_CENTER));
		items.appendChild(_getAction(user));
	}
}
