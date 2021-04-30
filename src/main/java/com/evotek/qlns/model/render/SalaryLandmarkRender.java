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

import com.evotek.qlns.model.SalaryLandmark;
import com.evotek.qlns.model.Staff;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.DateUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;

/**
 *
 * @author linhlh2
 */
public class SalaryLandmarkRender implements ListitemRenderer<SalaryLandmark> {

	private static final String ADD_EDIT_SALARY_LANDMARK_PAGE = "~./pages/manager_human_resource/editSalaryLandmark.zul";
	private Staff _staff;

	private Window _winParent;

	public SalaryLandmarkRender(Window winParent, Staff staff) {
		this._winParent = winParent;
		this._staff = staff;
	}

	private Map<String, Object> _createParameterMap(SalaryLandmark salaryLm, String title) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Constants.PARENT_WINDOW, this._winParent);
		parameters.put(Constants.TITLE, title);
		parameters.put(Constants.OBJECT, this._staff);
		parameters.put(Constants.EDIT_OBJECT, salaryLm);

		return parameters;
	}

	private Listcell _getAction(SalaryLandmark salaryLm) {
		Listcell action = new Listcell();

		Hlayout hlayout = new Hlayout();

		hlayout.setSpacing("0");

		hlayout.appendChild(ComponentUtil.createButton(null, Labels.getLabel(LanguageKeys.EDIT),
				ComponentUtil.EDIT_TOOLTIP, Events.ON_CLICK, ADD_EDIT_SALARY_LANDMARK_PAGE,
				_createParameterMap(salaryLm, Labels.getLabel(LanguageKeys.TITLE_EDIT_SALARY_LANDMARK)),
				Constants.Z_ICON_PENCIL, Constants.BLUE));

		// Thêm action "Xóa"
		hlayout.appendChild(ComponentUtil.createButton(this._winParent, Labels.getLabel(LanguageKeys.BUTTON_DELETE),
				ComponentUtil.DEL_TOOLTIP, Events.ON_CLICK, "onDeleteSalaryLm", salaryLm, Constants.Z_ICON_TRASH_O,
				Constants.RED));

		action.appendChild(hlayout);

		action.setStyle(Constants.STYLE_TEXT_ALIGN_CENTER);

		return action;
	}

	@Override
	public void render(Listitem item, SalaryLandmark salaryLm, int index) throws Exception {
		item.setAttribute("data", salaryLm);

//        item.appendChild(ComponentUtil.createListcell(Integer.toString(index + 1),
//                Constants.STYLE_TEXT_ALIGN_CENTER));
		item.appendChild(
				ComponentUtil.createListcell(GetterUtil.getDate(salaryLm.getFromDate(), DateUtil.SHORT_DATE_PATTERN),
						Constants.STYLE_TEXT_ALIGN_CENTER));
		item.appendChild(
				ComponentUtil.createListcell(GetterUtil.getDate(salaryLm.getToDate(), DateUtil.SHORT_DATE_PATTERN),
						Constants.STYLE_TEXT_ALIGN_CENTER));
		item.appendChild(ComponentUtil.createListcell(GetterUtil.getFormat(salaryLm.getSalary()),
				Constants.STYLE_TEXT_ALIGN_CENTER));

		item.appendChild(_getAction(salaryLm));
	}
}
