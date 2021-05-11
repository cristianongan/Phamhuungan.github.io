/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.model.render;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Notification;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.DateUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author linhlh2
 */
public class NotificationRender implements ListitemRenderer<Notification> {

	private Window _window;

	public NotificationRender(Window window) {
		this._window = window;
	}

	private Map<String, Object> _createParameterMap(Notification notify, String title) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Constants.Attr.PARENT_WINDOW, this._window);
		parameters.put(Constants.Attr.TITLE, title);
		parameters.put(Constants.Attr.OBJECT_ID, notify.getClassPk());

		return parameters;
	}

	private Listcell _getAction(Notification notify, Long type) {
		Listcell action = new Listcell();

		Hlayout hlayout = new Hlayout();

		hlayout.setSpacing("0");

		if (Values.NOTI_CONTRACT_EXPIRED.equals(type)) {
			hlayout.appendChild(ComponentUtil.createButton(null, Labels.getLabel(LanguageKeys.EXTEND_CONTRACT),
					"extendContractTooltip", Events.ON_CLICK, Constants.Page.HumanResourceManagement.ADD_EDIT,
					_createParameterMap(notify, Labels.getLabel(LanguageKeys.TITLE_EDIT_STAFF)),
					Constants.Zicon.LEVEL_UP, Constants.Sclass.BLUE));
		} else {
			hlayout.appendChild(ComponentUtil.createButton(this._window, Labels.getLabel(LanguageKeys.DELETE),
					Constants.Tooltip.DEL, Events.ON_CLICK, "onDeleteNotify", notify, Constants.Zicon.TRASH_O,
					Constants.Sclass.RED));
		}

		action.appendChild(hlayout);

		return action;
	}

	@Override
	public void render(Listitem item, Notification notify, int index) throws Exception {
		item.setAttribute("data", notify);

		Long type = notify.getNotificationType();

		StringBuilder sb = new StringBuilder();

		if (Values.NOTI_CONTRACT_EXPIRED.equals(type)) {
			item.setSclass(Constants.Class.TASK_PINK);

			sb.append(Labels.getLabel(LanguageKeys.CONTRACT_EXPIRED));
			sb.append(StringPool.SPACE);
			sb.append(StringPool.DASH);
			sb.append(StringPool.SPACE);

			item.appendChild(ComponentUtil.createListcell(ComponentUtil.createAIcon(Constants.Sclass.CONTRACT)));

		} else if (Values.NOTI_BIRTHDAY.equals(type)) {
			item.setSclass(Constants.Class.TASK_GREEN);

			sb.append(Labels.getLabel(LanguageKeys.BIRTH_DAY));
			sb.append(StringPool.SPACE);
			sb.append(StringPool.DASH);
			sb.append(StringPool.SPACE);

			item.appendChild(ComponentUtil.createListcell(ComponentUtil.createAIcon(Constants.Sclass.DATE)));
		} else {
			item.setSclass(Constants.Class.TASK_DEFAULT);
		}

		sb.append(notify.getMessage());

		item.appendChild(ComponentUtil.createListcell(sb.toString()));
		item.appendChild(
				ComponentUtil.createListcell(GetterUtil.getDate(notify.getEventDate(), DateUtil.SHORT_DATE_PATTERN),
						Constants.Style.TEXT_ALIGN_CENTER));

		item.appendChild(_getAction(notify, type));
	}
}
