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

	private static final String ASSIGN_RIGHT_PAGE = "~./pages/manager_menu/assign_right.zul";

	private static final String EDIT_GROUP_PAGE = "~./pages/manager_menu/edit_group.zul";

	public Window winTemp;

	public GroupRender(Window winTemp) {
		this.winTemp = winTemp;
	}

	private Map<String, Object> _createParameterMap(Group group) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Constants.PARENT_WINDOW, this.winTemp);
		parameters.put(Constants.TITLE, Labels.getLabel(LanguageKeys.TITLE_EDIT_GROUP));
		parameters.put(Constants.EDIT_OBJECT, group);

		return parameters;
	}

	private Hlayout _getAction(Group group) {

		Hlayout hlayout = new Hlayout();

		hlayout.setSpacing("0");

		hlayout.appendChild(ComponentUtil.createButton(null, Labels.getLabel(LanguageKeys.EDIT),
				ComponentUtil.EDIT_TOOLTIP, Events.ON_CLICK, EDIT_GROUP_PAGE, _createParameterMap(group),
				Constants.Z_ICON_PENCIL, Constants.BLUE));

		Long status = group.getStatus();

		if (Values.STATUS_ACTIVE.equals(status)) {
			// Thêm action "Khóa"
			hlayout.appendChild(ComponentUtil.createButton(this.winTemp, Labels.getLabel(LanguageKeys.BUTTON_LOCK),
					ComponentUtil.LOCK_TOOLTIP, Events.ON_CLICK, "onLockGroups", group, Constants.Z_ICON_LOCK,
					Constants.ORANGE));
		} else {
			hlayout.appendChild(ComponentUtil.createButton(this.winTemp, Labels.getLabel(LanguageKeys.BUTTON_UNLOCK),
					ComponentUtil.UNLOCK_TOOLTIP, Events.ON_CLICK, "onUnlockGroups", group, Constants.Z_ICON_UNLOCK,
					Constants.ORANGE));

			// Thêm action "Xóa"
			hlayout.appendChild(ComponentUtil.createButton(this.winTemp, Labels.getLabel(LanguageKeys.BUTTON_DELETE),
					ComponentUtil.DEL_TOOLTIP, Events.ON_CLICK, "onDeleteGroups", group, Constants.Z_ICON_TRASH_O,
					Constants.RED));
		}

		hlayout.appendChild(ComponentUtil.createButton(null, Labels.getLabel(LanguageKeys.ASSIGN_RIGHT),
				ComponentUtil.ASSIGN_RIGHT_TOOLTIP, Events.ON_CLICK, ASSIGN_RIGHT_PAGE, _createParameterMap(group),
				Constants.Z_ICON_SHARE, Constants.PURPLE));

		return hlayout;
	}

	@Override
	public void render(Row row, Group group, int index) throws Exception {
		row.appendChild(ComponentUtil.createCell(Integer.toString(index + 1), Constants.STYLE_TEXT_ALIGN_CENTER));
		row.appendChild(new Label(group.getGroupName()));
//        row.appendChild(new Label(group.getUserName()));
//        row.appendChild(ComponentUtil.createCell(GetterUtil.getDate(
//                group.getModifiedDate(), DateUtil.SHORT_DATE_PATTERN),
//                Constants.STYLE_TEXT_ALIGN_CENTER));
		row.appendChild(new Label(group.getDescription()));
		row.appendChild(new Label(Values.getLockStatus(group.getStatus())));
		row.appendChild(this._getAction(group));
	}
}
