/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.tree.node;

import java.util.List;

import org.zkoss.zul.DefaultTreeNode;

import com.evotek.qlns.model.DocumentType;

/**
 *
 * @author MRHOT
 */
public class DocumentTypeTreeNode extends DefaultTreeNode<DocumentType> {

	private boolean open = false;

	public DocumentTypeTreeNode(DocumentType data) {
		super(data);

	}

	public DocumentTypeTreeNode(DocumentType data, DefaultTreeNode<DocumentType>[] children) {
		super(data, children);
	}

	public DocumentTypeTreeNode(DocumentType data, DefaultTreeNode<DocumentType>[] children, boolean _open) {
		super(data, children);

		this.setOpen(_open);
	}

	public DocumentTypeTreeNode(DocumentType data, List<? extends DefaultTreeNode<DocumentType>> children) {
		super(data, children);
	}

	public DocumentTypeTreeNode(DocumentType data, List<? extends DefaultTreeNode<DocumentType>> children,
			boolean _open) {
		super(data, children);

		this.setOpen(_open);
	}

	public boolean isOpen() {
		return this.open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
}
