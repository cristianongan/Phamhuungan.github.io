package com.evotek.qlns.model.render;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.ListModel;
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
 * @author LinhLH
 */
public class UserRender implements ListitemRenderer<User> {

	private boolean isAdmin;
	
	private ListModel model;

	private Div winParent;

	public UserRender(Div win, ListModel model, boolean isAdmin) {
		this.winParent = win;
		
		this.model = model;

		this.isAdmin = isAdmin;
	}

	private Map<String, Object> _createParameterMap(User user, int index) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Constants.Attr.PARENT_WINDOW, this.winParent);
		parameters.put(Constants.Attr.TITLE, Labels.getLabel(LanguageKeys.TITLE_EDIT_USER));
		parameters.put(Constants.Attr.OBJECT, user);
		parameters.put(Constants.Attr.SECOND_OBJECT, true);
		parameters.put(Constants.Attr.MODEL, this.model);
		parameters.put(Constants.Attr.INDEX, index);

		return parameters;
	}

	private Listcell _getAction(User user, int index) {
		if (!this.isAdmin) {
			return new Listcell();
		}

		Listcell action = new Listcell();

		Hlayout hlayout = new Hlayout();

		hlayout.setSpacing("0");

		hlayout.appendChild(ComponentUtil.createButton(null, Labels.getLabel(LanguageKeys.EDIT), Constants.Tooltip.EDIT,
				Events.ON_CLICK, Constants.Page.ManagerUser.ADD_EDIT, _createParameterMap(user, index), Constants.Zicon.PENCIL,
				Constants.Sclass.BLUE));

		Long status = user.getStatus();

		if (Values.STATUS_ACTIVE.equals(status)) {
			hlayout.appendChild(ComponentUtil.createButton(this.winParent, Labels.getLabel(LanguageKeys.BUTTON_LOCK),
					Constants.Tooltip.LOCK, Events.ON_CLICK, "onLockUser", user, Constants.Zicon.LOCK,
					Constants.Sclass.ORANGE));
		} else if (Values.STATUS_DEACTIVE.equals(status)) {
			hlayout.appendChild(ComponentUtil.createButton(this.winParent, Labels.getLabel(LanguageKeys.BUTTON_UNLOCK),
					Constants.Tooltip.UNLOCK, Events.ON_CLICK, "onUnlockUser", user, Constants.Zicon.UNLOCK,
					Constants.Sclass.ORANGE));
		}

		if (Values.STATUS_NOT_READY.equals(status)) {
			hlayout.appendChild(ComponentUtil.createButton(this.winParent, Labels.getLabel(LanguageKeys.BUTTON_DELETE),
					Constants.Tooltip.DEL, Events.ON_CLICK, "onDeleteUser", user, Constants.Zicon.TRASH_O,
					Constants.Sclass.RED));
		}

		hlayout.appendChild(ComponentUtil.createButton(null, Labels.getLabel(LanguageKeys.ASSIGN_ROLE),
				Constants.Tooltip.ASSIGN_ROLE, Events.ON_CLICK, Constants.Page.ManagerUser.ASSIGNED_ROLE,
				_createParameterMap(user, index), Constants.Zicon.SHARE, Constants.Sclass.PURPLE));

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

		items.appendChild(ComponentUtil.createListcell(StringPool.BLANK, Constants.Style.TEXT_ALIGN_CENTER));
		items.appendChild(ComponentUtil.createListcell(Integer.toString(index + 1), Constants.Style.TEXT_ALIGN_CENTER));
		items.appendChild(new Listcell(user.getUserName()));
		items.appendChild(new Listcell(user.getEmail()));
		items.appendChild(_getGender(user.getGender()));
		items.appendChild(new Listcell(_getPhoneNumber(user.getPhone(), user.getMobile())));
		items.appendChild(new Listcell(user.getFullName()));
		items.appendChild(new Listcell(Values.getLockStatus(user.getStatus())));
//        items.appendChild(ComponentUtil.createListcell(GetterUtil.getDate(
//                user.getModifiedDate(), DateUtil.LONG_DATE_PATTERN),
//                Constants.Style.TEXT_ALIGN_CENTER));
		items.appendChild(_getAction(user, index));
	}
}
