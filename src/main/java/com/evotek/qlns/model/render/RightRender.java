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

import com.evotek.qlns.model.Right;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.StaticUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.PermissionConstants;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author linhlh2
 */
public class RightRender implements RowRenderer<Right> {

	private static final String[] _type = StaticUtil.MENU_RIGHT_TYPE;

	private static final String EDIT_RIGHT_PAGE = "~./pages/manager_menu/edit_right.zul";

	public Window winTemp;

	public RightRender(Window winTemp) {
		this.winTemp = winTemp;
	}

	private Map<String, Object> _createParameterMap(Right right) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Constants.PARENT_WINDOW, this.winTemp);
		parameters.put(Constants.TITLE, Labels.getLabel(LanguageKeys.TITLE_EDIT_RIGHT));
		parameters.put(Constants.EDIT_OBJECT, right);

		return parameters;
	}

	private Hlayout _getAction(Right right) {
		Hlayout hlayout = new Hlayout();

		hlayout.setSpacing("0");

		hlayout.appendChild(ComponentUtil.createButton(null, Labels.getLabel(LanguageKeys.EDIT), Constants.Tooltip.EDIT,
				Events.ON_CLICK, EDIT_RIGHT_PAGE, _createParameterMap(right), Constants.Zicon.PENCIL, Constants.BLUE));

		Long status = right.getStatus();

		if (Values.STATUS_ACTIVE.equals(status)) {
			// Thêm action "Khóa"
			hlayout.appendChild(ComponentUtil.createButton(this.winTemp, Labels.getLabel(LanguageKeys.BUTTON_LOCK),
					Constants.Tooltip.LOCK, Events.ON_CLICK, "onLockRight", right, Constants.Zicon.LOCK,
					Constants.ORANGE));
		} else {
			hlayout.appendChild(ComponentUtil.createButton(this.winTemp, Labels.getLabel(LanguageKeys.BUTTON_UNLOCK),
					Constants.Tooltip.UNLOCK, Events.ON_CLICK, "onUnlockRight", right, Constants.Zicon.UNLOCK,
					Constants.ORANGE));

			// Thêm action "Xóa"
			hlayout.appendChild(ComponentUtil.createButton(this.winTemp, Labels.getLabel(LanguageKeys.BUTTON_DELETE),
					Constants.Tooltip.DEL, Events.ON_CLICK, "onDeleteRight", right, Constants.Zicon.TRASH_O,
					Constants.RED));
		}

		return hlayout;
	}

	private String getRightTypeName(Long type) {
		String statusName = StringPool.BLANK;

		if (Validator.isNotNull(type) && type < _type.length) {
			statusName = _type[type.intValue()];
		}

		return statusName;
	}

	@Override
	public void render(Row row, Right right, int index) throws Exception {
		row.appendChild(ComponentUtil.createCell(Integer.toString(index + 1), Constants.STYLE_TEXT_ALIGN_CENTER));
		row.appendChild(new Label(right.getRightName()));
		row.appendChild(new Label(getRightTypeName(right.getRightType())));
//        row.appendChild(new Label(right.getUserName()));
//        row.appendChild(ComponentUtil.createCell(GetterUtil.getDate(
//                right.getModifiedDate(), DateUtil.SHORT_DATE_PATTERN),
//                Constants.STYLE_TEXT_ALIGN_CENTER));
		row.appendChild(new Label(right.getDescription()));
		row.appendChild(new Label(Values.getLockStatus(right.getStatus())));

		if (!PermissionConstants.TYPE_MENU_ITEM.equals(right.getRightType())) {
			row.appendChild(this._getAction(right));
		} else {
			row.appendChild(new Label(StringPool.BLANK));
		}
	}
}
