/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.model.render;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Department;
import com.evotek.qlns.service.DepartmentService;
import com.evotek.qlns.tree.node.DepartmentTreeNode;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;

/**
 *
 * @author linhlh2
 */
public class TreeDeptRender implements TreeitemRenderer<DepartmentTreeNode> {
	private DepartmentService departmentService;
	private Window winparent;

	public TreeDeptRender(Window winparent, DepartmentService departmentService) {
		this.winparent = winparent;
		this.departmentService = departmentService;
	}

	private Menupopup _createContextMenu(final Treerow treeRow, Department dept) {
		// context menu
		Menupopup popup = new Menupopup();

		popup.setPage(treeRow.getPage());

		popup.appendChild(ComponentUtil.createMenuitem(this.winparent, Labels.getLabel(LanguageKeys.ADD),
				Events.ON_CLICK, "onAdd", dept, Constants.Zicon.PLUS, Constants.BLUE));

//        if (documentType.getParentDocumentType() != null) { //neu khong phai la root
		popup.appendChild(ComponentUtil.createMenuitem(this.winparent, Labels.getLabel(LanguageKeys.EDIT),
				Events.ON_CLICK, "onEdit", dept, Constants.Zicon.PENCIL, Constants.BLUE));

		return popup;
	}

	@Override
	public void render(final Treeitem item, DepartmentTreeNode node, int index) throws Exception {
		Department dept = node.getData();
		// tree cell
		Treerow treeRow = new Treerow();

		item.appendChild(treeRow);

		item.setValue(node);
		item.setOpen(node.isOpen());
		item.setAttribute(Constants.DATA, dept);
		// name
		treeRow.appendChild(ComponentUtil.createTreeCell(dept.getDeptName()));

		treeRow.setContext(_createContextMenu(treeRow, dept));

		treeRow.addForward(Events.ON_DOUBLE_CLICK, this.winparent, "onEdit", dept);
	}
}
