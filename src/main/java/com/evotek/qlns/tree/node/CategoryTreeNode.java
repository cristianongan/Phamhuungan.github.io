/*
 * Copyright 2013 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.tree.node;

import java.util.List;

import org.zkoss.zul.DefaultTreeNode;

import com.evotek.qlns.model.Category;

/**
 *
 * @author hungnt81
 */
public class CategoryTreeNode extends DefaultTreeNode<Category> {

    private static final long serialVersionUID = 1370486533732L;
   
    private boolean open = false;

    public CategoryTreeNode(Category data, DefaultTreeNode<Category>[] children) {
        super(data, children);
    }

    public CategoryTreeNode(Category data, DefaultTreeNode<Category>[] children,
            boolean open) {
        super(data, children);

        this.setOpen(open);
    }

    public CategoryTreeNode(Category data, List<? extends
            DefaultTreeNode<Category>> children) {
        super(data, children);
    }

    public CategoryTreeNode(Category data, List<? extends
            DefaultTreeNode<Category>> children, boolean open) {
        super(data, children);

        this.setOpen(open);
    }

    public CategoryTreeNode(Category data) {
        super(data);

    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
