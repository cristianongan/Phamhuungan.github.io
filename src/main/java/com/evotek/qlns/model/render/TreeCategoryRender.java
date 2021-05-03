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
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

import com.evotek.qlns.model.Category;
import com.evotek.qlns.tree.node.CategoryTreeNode;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.DateUtil;
import com.evotek.qlns.util.StaticUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author hungnt81
 */
public class TreeCategoryRender<Component> implements TreeitemRenderer<CategoryTreeNode> {

	// Mảng các loại menu lấy từ file cấu hình vsm.config.properties
	private static final String[] _type = StaticUtil.MENU_TYPE;

	public Div winTemp;

	public TreeCategoryRender(Component win) {
		this.winTemp = (Div) win;
	}

	/**
	 * Hàm tạo map các tham số truyền vào khi mở lên popup
	 * 
	 * @param category
	 * @return
	 */
	private Map<String, Object> _createParameterMap(Category category) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Constants.Attr.PARENT_WINDOW, this.winTemp);
		parameters.put(Constants.Attr.TITLE, Labels.getLabel(LanguageKeys.TITLE_EDIT_MENU));
		parameters.put(Constants.Attr.OBJECT, category);

		return parameters;
	}

	/**
	 * Hàm tạo menu action
	 * 
	 * @param category
	 * @return
	 */
	private Treecell _getAction(Category category) {
		Treecell action = new Treecell();

		Hlayout hlayout = new Hlayout();

		hlayout.setSpacing("0");

		hlayout.appendChild(ComponentUtil.createButton(null, Labels.getLabel(LanguageKeys.EDIT), Constants.Tooltip.EDIT,
				Events.ON_CLICK, Constants.Page.ManagerMenu.ADD_EDIT, _createParameterMap(category),
				Constants.Zicon.PENCIL, Constants.Sclass.BLUE));

		Long status = category.getStatus();
		Long immune = category.getImmune();

		if (Validator.isNotNull(immune) && !immune.equals(Values.IMMUNE)) {
			if (Validator.isNotNull(status) && status.equals(Values.STATUS_ACTIVE)) {
				// Thêm action "Khóa"
				hlayout.appendChild(ComponentUtil.createButton(this.winTemp, Labels.getLabel(LanguageKeys.BUTTON_LOCK),
						Constants.Tooltip.LOCK, Events.ON_CLICK, "onLockCategory", category, Constants.Zicon.LOCK,
						Constants.Sclass.ORANGE));

			} else {
				// Thêm action "Mở khóa"
				hlayout.appendChild(ComponentUtil.createButton(this.winTemp,
						Labels.getLabel(LanguageKeys.BUTTON_UNLOCK), Constants.Tooltip.UNLOCK, Events.ON_CLICK,
						"onUnlockCategory", category, Constants.Zicon.UNLOCK, Constants.Sclass.ORANGE));

				// Thêm action "Xóa"
				hlayout.appendChild(ComponentUtil.createButton(this.winTemp,
						Labels.getLabel(LanguageKeys.BUTTON_DELETE), Constants.Tooltip.DEL, Events.ON_CLICK,
						"onDeleteCategory", category, Constants.Zicon.TRASH_O, Constants.Sclass.RED));
			}
		}

		// Thêm action "Quyền" và "Resource" nếu là menu item
		if (Values.MENU_TYPE_ITEM.equals(category.getType())) {
			hlayout.appendChild(ComponentUtil.createButton(null, Labels.getLabel(LanguageKeys.BUTTON_RIGHT),
					Constants.Tooltip.RIGHT, Events.ON_CLICK, Constants.Page.ManagerMenu.RIGHT,
					_createParameterMap(category), Constants.Zicon.KEY, Constants.Sclass.ORANGE));

//            hlayout.appendChild(ComponentUtil.createButton(null,
//                Labels.getLabel(LanguageKeys.BUTTON_RESOURCE), Events.ON_CLICK, 
//                RESOURCE_ACTION_PAGE, _createParameterMap(category), 
//                Constants.ZICON.Z_ICON_KEY, Constants.ORANGE));
		}

		action.appendChild(hlayout);

		return action;
	}

	/**
	 * Hàm lấy tên loại menu tương ứng với giá trị categoryType
	 * 
	 * @param categoryType
	 * @return
	 */
	private String _getType(Long categoryType) {
		String type = StringPool.BLANK;

		if (Validator.isNotNull(categoryType) && categoryType.intValue() < _type.length) {
			type = _type[categoryType.intValue()];
		}

		return type;
	}

	@Override
	public void render(Treeitem trtm, CategoryTreeNode t, int i) throws Exception {
		final Category category = t.getData();

		// tree cell
		Treerow dataRow = new Treerow();

		dataRow.setParent(trtm);

		trtm.setValue(t);
		trtm.setOpen(t.isOpen());
		trtm.setAttribute("data", category);

//        dataRow.appendChild(new Treecell());
		dataRow.appendChild(ComponentUtil.createTreeCell(Labels.getLabel(category.getLanguageKey())));// Ten menu
		dataRow.appendChild(ComponentUtil.createTreeCell(_getType(category.getType())));// Loai menu
		dataRow.appendChild(ComponentUtil.createTreeCell(category.getUserName()));// Nguoi tao
		dataRow.appendChild(ComponentUtil.createTreeCell(DateUtil.formatLongDate(category.getModifiedDate())));
		dataRow.appendChild(ComponentUtil.createTreeCell(Values.getLockStatus(category.getStatus())));
		dataRow.appendChild(_getAction(category));
	}
}
