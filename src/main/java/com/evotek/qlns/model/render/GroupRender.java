/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.model.render;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Group;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author linhlh2
 */
public class GroupRender implements RowRenderer<Group> {

	public Window winTemp;

	public GroupRender(Window winTemp) {
		this.winTemp = winTemp;
	}

	private Map<String, Object> _createParameterMap(Group group) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Constants.Attr.PARENT_WINDOW, this.winTemp);
		parameters.put(Constants.Attr.TITLE, Labels.getLabel(LanguageKeys.TITLE_EDIT_GROUP));
		parameters.put(Constants.Attr.EDIT_OBJECT, group);

		return parameters;
	}

	private Hlayout _getAction(Group group) {

		Hlayout hlayout = new Hlayout();

		hlayout.setSpacing("0");

		hlayout.appendChild(ComponentUtil.createButton(null, Labels.getLabel(LanguageKeys.EDIT), Constants.Tooltip.EDIT,
				Events.ON_CLICK, Constants.Page.ManagerUser.ADD_EDIT_GROUP, _createParameterMap(group),
				Constants.Zicon.PENCIL, Constants.Sclass.BLUE));

		Long status = group.getStatus();

		if (Values.STATUS_ACTIVE.equals(status)) {
			// Thêm action "Khóa"
			hlayout.appendChild(ComponentUtil.createButton(this.winTemp, Labels.getLabel(LanguageKeys.BUTTON_LOCK),
					Constants.Tooltip.LOCK, Events.ON_CLICK, "onLockGroups", group, Constants.Zicon.LOCK,
					Constants.Sclass.ORANGE));
		} else {
			hlayout.appendChild(ComponentUtil.createButton(this.winTemp, Labels.getLabel(LanguageKeys.BUTTON_UNLOCK),
					Constants.Tooltip.UNLOCK, Events.ON_CLICK, "onUnlockGroups", group, Constants.Zicon.UNLOCK,
					Constants.Sclass.ORANGE));

			// Thêm action "Xóa"
			hlayout.appendChild(ComponentUtil.createButton(this.winTemp, Labels.getLabel(LanguageKeys.BUTTON_DELETE),
					Constants.Tooltip.DEL, Events.ON_CLICK, "onDeleteGroups", group, Constants.Zicon.TRASH_O,
					Constants.Sclass.RED));
		}

		hlayout.appendChild(ComponentUtil.createButton(null, Labels.getLabel(LanguageKeys.ASSIGN_RIGHT),
				Constants.Tooltip.ASSIGN_RIGHT, Events.ON_CLICK, Constants.Page.ManagerUser.ASSIGN_RIGHT,
				_createParameterMap(group), Constants.Zicon.SHARE, Constants.Sclass.PURPLE));

		return hlayout;
	}

	@Override
	public void render(Row row, Group group, int index) throws Exception {
		row.appendChild(ComponentUtil.createCell(Integer.toString(index + 1), Constants.Style.TEXT_ALIGN_CENTER));
		row.appendChild(new Label(group.getGroupName()));
//        row.appendChild(new Label(group.getUserName()));
//        row.appendChild(ComponentUtil.createCell(GetterUtil.getDate(
//                group.getModifiedDate(), DateUtil.SHORT_DATE_PATTERN),
//                Constants.Style.TEXT_ALIGN_CENTER));
		row.appendChild(new Label(group.getDescription()));
		row.appendChild(new Label(Values.getLockStatus(group.getStatus())));
		row.appendChild(this._getAction(group));
	}
}
