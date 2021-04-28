/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.tree.model;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;

/**
 *
 * @author linhlh2
 */
public class TreeBasicModel<T> extends DefaultTreeModel<T> {
	private static final Logger _log = LogManager.getLogger(TreeBasicModel.class);
	private DefaultTreeNode<T> _root;

	private Boolean multiple;

	public TreeBasicModel(DefaultTreeNode<T> root) {
		super(root);

		this._root = root;

		setMultiple(true);
	}

	public TreeBasicModel(DefaultTreeNode<T> root, Boolean multiple) {
		super(root);

		this._root = root;
		this.multiple = multiple;

		setMultiple(multiple);
	}

	public void add(DefaultTreeNode<T> parent, DefaultTreeNode<T>[] newNodes) {
		DefaultTreeNode<T> stn = parent;

		stn.getChildren().addAll(Arrays.asList(newNodes));
	}

	private DefaultTreeNode<T> dfSearchParent(DefaultTreeNode<T> node, DefaultTreeNode<T> target) {
		if (node.getChildren() != null && node.getChildren().contains(target)) {
			return node;
		} else {
			int size = getChildCount(node);

			for (int i = 0; i < size; i++) {
				DefaultTreeNode<T> parent = dfSearchParent((DefaultTreeNode<T>) getChild(node, i), target);

				if (parent != null) {
					return parent;
				}
			}
		}

		return null;
	}

	public void insert(DefaultTreeNode<T> parent, int indexFrom, int indexTo, DefaultTreeNode<T>[] newNodes)
			throws IndexOutOfBoundsException {
		DefaultTreeNode<T> stn = parent;

		for (int i = indexFrom; i <= indexTo; i++) {
			try {
				stn.getChildren().add(i, newNodes[i - indexFrom]);
			} catch (Exception exp) {
				throw new IndexOutOfBoundsException("Out of bound: " + i + " while size=" + stn.getChildren().size());
			}
		}
	}

	public void remove(DefaultTreeNode<T> target) throws IndexOutOfBoundsException {
		int index = 0;

		try {
			DefaultTreeNode<T> parent = null;
			// find the parent and index of target
			parent = dfSearchParent(this._root, target);
			for (index = 0; index < parent.getChildCount(); index++) {
				if (parent.getChildAt(index).equals(target)) {
					break;
				}
			}
			remove(parent, index, index);
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	public void remove(DefaultTreeNode<T> parent, int indexFrom, int indexTo) throws IndexOutOfBoundsException {
		DefaultTreeNode<T> stn = parent;

		try {
			for (int i = indexTo; i >= indexFrom; i--) {
				stn.getChildren().remove(i);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}
}
