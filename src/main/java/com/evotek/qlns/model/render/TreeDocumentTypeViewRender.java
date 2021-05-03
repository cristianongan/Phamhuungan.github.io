/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.model.render;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.DocumentType;
import com.evotek.qlns.tree.node.DocumentTypeTreeNode;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.ZkKeys;

/**
 *
 * @author PC
 */
public class TreeDocumentTypeViewRender implements TreeitemRenderer<DocumentTypeTreeNode> {

	private Window win;

	public TreeDocumentTypeViewRender(Window window) {
		this.win = window;
	}

	@Override
	public void render(Treeitem treeItem, DocumentTypeTreeNode t, int i) throws Exception {
		DocumentType documentType = t.getData();

		Treerow dataRow = new Treerow();

		dataRow.appendChild(ComponentUtil.createTreeCell(documentType.getTypeName()));

		treeItem.appendChild(dataRow);

		treeItem.setAttribute(Constants.Attr.DATA, documentType);
		treeItem.setValue(t);
		treeItem.setOpen(t.isOpen());

		treeItem.addForward(Events.ON_CLICK, this.win, ZkKeys.ON_LOAD_DATA, documentType);
	}

}
