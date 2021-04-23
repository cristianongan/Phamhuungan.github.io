/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.model.render;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.A;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

import com.evotek.qlns.model.DocumentType;
import com.evotek.qlns.tree.node.DocumentTypeTreeNode;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.key.Constants;

/**
 *
 * @author PC
 */
public class TreeDocumentTypeSearchRender implements
        TreeitemRenderer<DocumentTypeTreeNode> {

    private Bandbox bbTemp;
    private A btnClear;

    public TreeDocumentTypeSearchRender(Bandbox bandbox, A btnClear) {
        this.bbTemp = bandbox;
        this.btnClear = btnClear;
    }

    @Override
	public void render(Treeitem treeItem, DocumentTypeTreeNode t, int i) throws Exception {
        DocumentType documentType = t.getData();

        Treerow dataRow = new Treerow();

        dataRow.appendChild(ComponentUtil.createTreeCell(documentType.getTypeName()));

        treeItem.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

            @Override
            public void onEvent(Event t) throws Exception {
                DocumentTypeTreeNode clickedNodeValue = ((Treeitem) t.getTarget()).getValue();

                TreeDocumentTypeSearchRender.this.bbTemp.setValue(clickedNodeValue.getData().getTypeName());
                
                TreeDocumentTypeSearchRender.this.bbTemp.setAttribute(Constants.OBJECT,
                        clickedNodeValue.getData());
                TreeDocumentTypeSearchRender.this.bbTemp.setAttribute(Constants.ID,
                        clickedNodeValue.getData().getDocumentTypeId());
               
                TreeDocumentTypeSearchRender.this.bbTemp.close();
                
                TreeDocumentTypeSearchRender.this.btnClear.setVisible(true);
                TreeDocumentTypeSearchRender.this.btnClear.setDisabled(false);
            }
        });

        treeItem.appendChild(dataRow);

        treeItem.setAttribute(Constants.DATA, documentType);
        treeItem.setValue(t);
        treeItem.setOpen(t.isOpen());
    }

}
