/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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

import com.evotek.qlns.model.Department;
import com.evotek.qlns.tree.node.DepartmentTreeNode;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.key.Constants;

/**
 *
 * @author My PC
 */
public class TreeDeptSearchRender implements
        TreeitemRenderer<DepartmentTreeNode> {

    private Bandbox bbTemp;
    private A btnClear;

    public TreeDeptSearchRender(Bandbox bandbox, A btnClear) {
        this.bbTemp = bandbox;
        this.btnClear = btnClear;
    }

    public void render(Treeitem treeItem, DepartmentTreeNode t, int i) throws Exception {
        Department dept = (Department) t.getData();

        Treerow dataRow = new Treerow();

        dataRow.appendChild(ComponentUtil.createTreeCell(dept.getDeptName()));

        treeItem.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

            @Override
            public void onEvent(Event t) throws Exception {
                DepartmentTreeNode clickedNodeValue = ((Treeitem) t.getTarget()).getValue();

                bbTemp.setValue(clickedNodeValue.getData().getDeptName());
                
                bbTemp.setAttribute(Constants.OBJECT,
                        clickedNodeValue.getData());
                bbTemp.setAttribute(Constants.ID,
                        clickedNodeValue.getData().getDeptId());
               
                bbTemp.close();
                
                btnClear.setVisible(true);
                btnClear.setDisabled(false);
            }
        });

        treeItem.appendChild(dataRow);

        treeItem.setAttribute(Constants.DATA, dept);
        treeItem.setValue(t);
        treeItem.setOpen(t.isOpen());
    }
    
}
