/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.tree.node;

import java.util.List;

import org.zkoss.zul.DefaultTreeNode;

import com.evotek.qlns.model.Department;

/**
 *
 * @author My PC
 */
public class DepartmentTreeNode extends DefaultTreeNode<Department> {

    private boolean open = false;

    public DepartmentTreeNode(Department data, 
            DefaultTreeNode<Department>[] children) {
        super(data, children);
    }

    public DepartmentTreeNode(Department data, 
            DefaultTreeNode<Department>[] children,
            boolean _open) {
        super(data, children);

        this.setOpen(_open);
    }

    public DepartmentTreeNode(Department data, 
            List<? extends DefaultTreeNode<Department>> children) {
        super(data, children);
    }

    public DepartmentTreeNode(Department data, 
            List<? extends DefaultTreeNode<Department>> children, boolean _open) {
        super(data, children);

        this.setOpen(_open);
    }

    public DepartmentTreeNode(Department data) {
        super(data);

    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
    
}
