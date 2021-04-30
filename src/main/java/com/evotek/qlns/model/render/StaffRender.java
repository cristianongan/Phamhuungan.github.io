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
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.evotek.qlns.model.Staff;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.DateUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author PC
 */
public class StaffRender implements ListitemRenderer<Staff> {

	private static final String DETAIL_PAGE = "~./pages/manager_human_resource/detail.zul";
	private static final String EDIT_PAGE = "~./pages/manager_human_resource/edit.zul";

	private ListModel _model;

	private Hlayout _window;

	public StaffRender(Hlayout window, ListModel model) {
		this._window = window;
		this._model = model;
	}

	private Map<String, Object> _createParameterMap(Staff staff, String title, int index) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Constants.PARENT_WINDOW, this._window);
		parameters.put(Constants.TITLE, title);
		parameters.put(Constants.OBJECT, staff);
		parameters.put(Constants.MODEL, this._model);
		parameters.put(Constants.INDEX, index);

		return parameters;
	}

	private Listcell _getAction(Staff staff, int index) {
		Listcell action = new Listcell();

		Hlayout hlayout = new Hlayout();

		hlayout.setSpacing("0");

		hlayout.appendChild(ComponentUtil.createButton(null, Labels.getLabel(LanguageKeys.EDIT),
				ComponentUtil.EDIT_TOOLTIP, Events.ON_CLICK, EDIT_PAGE,
				_createParameterMap(staff, Labels.getLabel(LanguageKeys.TITLE_EDIT_STAFF), index),
				Constants.Z_ICON_PENCIL, Constants.BLUE));

		Long status = staff.getStatus();

		if (Values.STATUS_ACTIVE.equals(status)) {
			hlayout.appendChild(ComponentUtil.createButton(this._window, Labels.getLabel(LanguageKeys.BUTTON_LOCK),
					ComponentUtil.LOCK_TOOLTIP, Events.ON_CLICK, "onLockStaff", staff, Constants.Z_ICON_LOCK,
					Constants.ORANGE));
		} else {
			hlayout.appendChild(ComponentUtil.createButton(this._window, Labels.getLabel(LanguageKeys.BUTTON_UNLOCK),
					ComponentUtil.UNLOCK_TOOLTIP, Events.ON_CLICK, "onUnlockStaff", staff, Constants.Z_ICON_UNLOCK,
					Constants.ORANGE));

			// Thêm action "Xóa"
			hlayout.appendChild(ComponentUtil.createButton(this._window, Labels.getLabel(LanguageKeys.BUTTON_DELETE),
					ComponentUtil.DEL_TOOLTIP, Events.ON_CLICK, "onDeleteStaff", staff, Constants.Z_ICON_TRASH_O,
					Constants.RED));
		}

		hlayout.appendChild(ComponentUtil.createButton(null, Labels.getLabel(LanguageKeys.DETAIL),
				ComponentUtil.DETAIL_TOOLTIP, Events.ON_CLICK, DETAIL_PAGE,
				_createParameterMap(staff, Labels.getLabel(LanguageKeys.TITLE_STAFF_DETAIL), index),
				Constants.Z_ICON_SEARCH_PLUS, Constants.PURPLE));

		action.appendChild(hlayout);

		return action;
	}

	private String _getPhoneNumber(String phone, String mobile) {
		StringBuilder sb = new StringBuilder();

		sb.append(phone);

		if (sb.length() > 0 && Validator.isNotNull(mobile)) {
			sb.append(StringPool.SLASH);
			sb.append(StringPool.SPACE);
			sb.append(mobile);
		} else if (Validator.isNotNull(mobile)) {
			sb.append(mobile);
		}

		return sb.toString();
	}

	@Override
	public void render(Listitem item, Staff staff, int index) throws Exception {
		item.setAttribute("data", staff);

		item.appendChild(ComponentUtil.createListcell(StringPool.BLANK, Constants.STYLE_TEXT_ALIGN_CENTER));
		item.appendChild(ComponentUtil.createListcell(Integer.toString(index + 1), Constants.STYLE_TEXT_ALIGN_CENTER));
		item.appendChild(_getAction(staff, index));
		item.appendChild(ComponentUtil.createListcell(staff.getStaffName()));
		item.appendChild(ComponentUtil.createListcell(staff.getDepartment().getDeptName()));
		item.appendChild(ComponentUtil.createListcell(staff.getJob().getJobTitle()));
		item.appendChild(
				ComponentUtil.createListcell(GetterUtil.getDate(staff.getWorkDate(), DateUtil.SHORT_DATE_PATTERN),
						Constants.STYLE_TEXT_ALIGN_CENTER));
		item.appendChild(
				ComponentUtil.createListcell(GetterUtil.getDate(staff.getDateOfBirth(), DateUtil.SHORT_DATE_PATTERN),
						Constants.STYLE_TEXT_ALIGN_CENTER));
		item.appendChild(ComponentUtil.createListcell(staff.getPermanentResidence()));
		item.appendChild(ComponentUtil.createListcell(staff.getCurrentResidence()));
		item.appendChild(ComponentUtil.createListcell(Values.getStaffStatus(staff.getStatus())));
		item.appendChild(ComponentUtil.createListcell(staff.getNote()));
		item.appendChild(ComponentUtil.createListcell(staff.getContractType().getContractTypeName()));
		item.appendChild(ComponentUtil.createListcell(
				GetterUtil.getDate(staff.getContractFromDate(), DateUtil.SHORT_DATE_PATTERN),
				Constants.STYLE_TEXT_ALIGN_CENTER));
		item.appendChild(
				ComponentUtil.createListcell(GetterUtil.getDate(staff.getContractToDate(), DateUtil.SHORT_DATE_PATTERN),
						Constants.STYLE_TEXT_ALIGN_CENTER));
		item.appendChild(ComponentUtil.createListcell(staff.getContractNumber()));
		item.appendChild(ComponentUtil.createListcell(staff.getTaxCode()));
		item.appendChild(ComponentUtil.createListcell(GetterUtil.getFormat(staff.getSalaryBasic()),
				Constants.STYLE_TEXT_ALIGN_CENTER));
		item.appendChild(ComponentUtil.createListcell(
				GetterUtil.getDate(staff.getInsurancePaidDate(), DateUtil.SHORT_DATE_PATTERN),
				Constants.STYLE_TEXT_ALIGN_CENTER));
		item.appendChild(ComponentUtil.createListcell(staff.getInsuranceBookNumber()));
		item.appendChild(ComponentUtil.createListcell(staff.getPaidPlace()));
		item.appendChild(ComponentUtil.createListcell(staff.getLevels()));
		item.appendChild(ComponentUtil.createListcell(staff.getMajors()));
		item.appendChild(ComponentUtil.createListcell(staff.getCollege()));
		item.appendChild(ComponentUtil.createListcell(staff.getIdentityCard()));
		item.appendChild(
				ComponentUtil.createListcell(GetterUtil.getDate(staff.getGrantDate(), DateUtil.SHORT_DATE_PATTERN),
						Constants.STYLE_TEXT_ALIGN_CENTER));
		item.appendChild(ComponentUtil.createListcell(staff.getGrantPlace()));

		item.appendChild(new Listcell(_getPhoneNumber(staff.getMobile(), staff.getHomePhone())));
		item.appendChild(ComponentUtil.createListcell(staff.getEmail()));
//        item.appendChild(ComponentUtil.createListcell(staff.getGender()));
//        item.appendChild(ComponentUtil.createListcell(staff.getMarried()));
//        item.appendChild(ComponentUtil.createListcell(staff.getChildNumber() == null
//                ? "" : staff.getChildNumber().toString()));

	}
}
