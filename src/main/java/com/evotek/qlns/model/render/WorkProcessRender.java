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

import com.evotek.qlns.model.Staff;
import com.evotek.qlns.model.WorkProcess;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.DateUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;

/**
 *
 * @author My PC
 */
public class WorkProcessRender implements ListitemRenderer<WorkProcess> {

	private Staff _staff;

	private Window _winParent;

	public WorkProcessRender(Window winParent, Staff staff) {
		this._winParent = winParent;
		this._staff = staff;
	}

	private Map<String, Object> _createParameterMap(WorkProcess wp, String title) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Constants.Attr.PARENT_WINDOW, this._winParent);
		parameters.put(Constants.Attr.TITLE, title);
		parameters.put(Constants.Attr.OBJECT, this._staff);
		parameters.put(Constants.Attr.EDIT_OBJECT, wp);

		return parameters;
	}

	private Listcell _getAction(WorkProcess wp) {
		Listcell action = new Listcell();

		Hlayout hlayout = new Hlayout();

		hlayout.setSpacing("0");

		hlayout.appendChild(ComponentUtil.createButton(null, Labels.getLabel(LanguageKeys.EDIT), Constants.Tooltip.EDIT,
				Events.ON_CLICK, Constants.Page.HumanResourceManagement.ADD_EDIT_WORK_PROCESS,
				_createParameterMap(wp, Labels.getLabel(LanguageKeys.TITLE_EDIT_WORK_PROCESS)), Constants.Zicon.PENCIL,
				Constants.Sclass.BLUE));

		// Thêm action "Xóa"
		hlayout.appendChild(ComponentUtil.createButton(this._winParent, Labels.getLabel(LanguageKeys.BUTTON_DELETE),
				Constants.Tooltip.DEL, Events.ON_CLICK, "onDeleteWorkProcess", wp, Constants.Zicon.TRASH_O,
				Constants.Sclass.RED));

		action.appendChild(hlayout);

		action.setStyle(Constants.Style.TEXT_ALIGN_CENTER);

		return action;
	}

	@Override
	public void render(Listitem item, WorkProcess wp, int index) throws Exception {
		item.setAttribute("data", wp);

		item.appendChild(ComponentUtil.createListcell(GetterUtil.getDate(wp.getFromDate(), DateUtil.SHORT_DATE_PATTERN),
				Constants.Style.TEXT_ALIGN_CENTER));
		item.appendChild(ComponentUtil.createListcell(GetterUtil.getDate(wp.getToDate(), DateUtil.SHORT_DATE_PATTERN),
				Constants.Style.TEXT_ALIGN_CENTER));
		item.appendChild(ComponentUtil.createListcell(wp.getCompany()));
		item.appendChild(ComponentUtil.createListcell(wp.getJobTitle()));

		item.appendChild(_getAction(wp));
	}
}
