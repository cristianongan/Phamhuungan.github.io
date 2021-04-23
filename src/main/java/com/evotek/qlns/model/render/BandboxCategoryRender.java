/*
 * Copyright 2013 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.model.render;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

import com.evotek.qlns.model.Category;
import com.evotek.qlns.tree.node.CategoryTreeNode;

/**
 *
 * @author hungnt81
 */
public class BandboxCategoryRender<Component> implements TreeitemRenderer<CategoryTreeNode> {

    private Bandbox bbTemp;

    public BandboxCategoryRender(Bandbox bb) {
        this.bbTemp = bb;
    }

    @Override
    public void render(Treeitem trtm, CategoryTreeNode t, int i) throws Exception {
        CategoryTreeNode dtn = t;

        final Category category = dtn.getData();

        trtm.setValue(dtn);
        trtm.setOpen(dtn.isOpen());

        Treerow dataRow = new Treerow();

        Treecell treeCell = new Treecell();
        treeCell.appendChild(new Label(Labels.getLabel(category.getLanguageKey())));

        dataRow.appendChild(treeCell);
        
        trtm.appendChild(dataRow);

        trtm.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

            @Override
            public void onEvent(Event t) throws Exception {
                CategoryTreeNode clickedNodeValue = ((Treeitem) 
                        t.getTarget()).getValue();

                BandboxCategoryRender.this.bbTemp.setValue(Labels.getLabel(clickedNodeValue.getData().
                        getLanguageKey()));
                
                BandboxCategoryRender.this.bbTemp.setAttribute("categoryId", category.getCategoryId());

                BandboxCategoryRender.this.bbTemp.setOpen(true);
                BandboxCategoryRender.this.bbTemp.setOpen(false);
            }
        });
    }
}
