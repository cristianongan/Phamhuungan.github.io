/*
 * To change this template, choose Tools | Templates
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

import com.evotek.qlns.model.DocumentType;
import com.evotek.qlns.service.DocumentTypeService;
import com.evotek.qlns.tree.node.DocumentTypeTreeNode;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;

/**
 *
 * @author MRHOT
 */
public class TreeDocumentTypeRender implements TreeitemRenderer<DocumentTypeTreeNode> {

	private DocumentTypeService documentTypeService;
	private Window winparent;

	public TreeDocumentTypeRender(Window win, DocumentTypeService _doDocumentTypeService) {
		this.winparent = win;
		this.documentTypeService = _doDocumentTypeService;
	}

	private Menupopup _createContextMenu(final Treerow treeRow, DocumentType documentType) {
		// context menu
		Menupopup popup = new Menupopup();

		popup.setPage(treeRow.getPage());

		popup.appendChild(ComponentUtil.createMenuitem(this.winparent, Labels.getLabel(LanguageKeys.ADD),
				Events.ON_CLICK, "onAdd", documentType, Constants.Zicon.PLUS, Constants.BLUE));

//        if (documentType.getParentDocumentType() != null) { //neu khong phai la root
		popup.appendChild(ComponentUtil.createMenuitem(this.winparent, Labels.getLabel(LanguageKeys.EDIT),
				Events.ON_CLICK, "onEdit", documentType, Constants.Zicon.PENCIL, Constants.BLUE));
//        popup.appendChild(ComponentUtil.createMenuitem(winparent,
//                Labels.getLabel(LanguageKeys.BUTTON_DELETE),
//                Events.ON_CLICK, "onDelete", documentType,
//                ComponentUtil.DELETE_ICON));
//        }

		return popup;
	}

	@Override
	public void render(final Treeitem item, DocumentTypeTreeNode node, int index) throws Exception {
		DocumentType documentType = node.getData();
		// tree cell
		Treerow treeRow = new Treerow();

		item.appendChild(treeRow);

		item.setValue(node);
		item.setOpen(node.isOpen());
		item.setAttribute(Constants.DATA, documentType);
		// name
		treeRow.appendChild(ComponentUtil.createTreeCell(documentType.getTypeName()));
		// status
//        treeRow.appendChild(ComponentUtil.createTreeCell(
//                Values.getLockStatus(documentType.getStatus())));

		treeRow.setContext(_createContextMenu(treeRow, documentType));

		treeRow.addForward(Events.ON_DOUBLE_CLICK, this.winparent, "onEdit", documentType);
		// cho phep keo doi tuong
//        if (documentType.getChildDocumentTypes().isEmpty()) {
//            item.setDraggable(String.valueOf(true));
//        }
//
//        item.setDroppable(String.valueOf(true));
//        item.addEventListener(Events.ON_DROP, new EventListener<Event>() {
//
//            @Override
//            public void onEvent(Event event) throws Exception {
//                    // The dragged target is a TreeRow belongs to an
//                // Treechildren of TreeItem.
//                Treeitem draggedItem = (Treeitem) ((DropEvent) event).getDragged();
//                Treeitem parentDropItem = item.getParentItem();
//
//                DocumentTypeTreeNode draggedValue
//                        = (DocumentTypeTreeNode) draggedItem.getValue();
//
//                DocumentType draggedDocType
//                        = (DocumentType) draggedValue.getData();
//                DocumentType dropDocumentType = (DocumentType) ((DocumentTypeTreeNode) item.getValue()).getData();
//                DocumentType draggedParent
//                        = draggedDocType.getParentDocumentType();
//
//                //xoa khoi noi ban dau
//                _remove(draggedDocType);
//                //kiem tra xem dat gia tri vao nhánh khác hay trong cùng nhánh
//                if (_isOtherBranch(dropDocumentType, draggedParent)) {
//                    _add(dropDocumentType, draggedDocType);
//                } else {
//                    int indexTo = parentDropItem.getTreechildren().
//                            getChildren().indexOf(item);
//                    int indexFrom = parentDropItem.getTreechildren().
//                            getChildren().indexOf(draggedItem);
//
//                    draggedDocType.setParentDocumentType(draggedParent);
//
//                    if (parentDropItem.getValue() instanceof DocumentTypeTreeNode) {
//                        _insert((DocumentTypeTreeNode) parentDropItem.getValue(),
//                                indexFrom, indexTo, draggedDocType, draggedValue);
//                    }
//
//                }
//            }
//        });
	}

//    private void _remove(DocumentType draggedDocType) {
//        draggedDocType.setOrdinal(null);
//        draggedDocType.setParentDocumentType(null);
//        documentTypeService.saveOrUpdate(draggedDocType);
//    }
//
//    private boolean _isOtherBranch(DocumentType documentTypeDrop,
//            DocumentType documentTypeDragParent) {
//        if (!documentTypeDragParent.getDocumentTypeId()
//                .equals(documentTypeDrop.getParentDocumentType().getDocumentTypeId())) {
//            return true;
//        }
//        
//        return false;
//    }
//
//    private void _add(DocumentType documentTypeDrop, DocumentType draggedDocType) {
//        draggedDocType.setParentDocumentType(documentTypeDrop);
//        //vi tri o cuoi
//        int ordinal = documentTypeService.count(documentTypeDrop.getDocumentTypeId());
//        
//        draggedDocType.setOrdinal(Long.valueOf(ordinal));
//        
//        documentTypeService.saveOrUpdate(draggedDocType);
//    }
//
//    //chen vao danh sach
//    private void _insert(DocumentTypeTreeNode parent, int indexFrom, int indexTo,
//            DocumentType draggedDocType, DocumentTypeTreeNode draggedValue)
//            throws IndexOutOfBoundsException {
//        for (int index = 0; index < parent.getChildCount(); index++) {
//            //kiemm tra xem day co phai doi tuong drag ko ?
//            if (!parent.getChildAt(index).equals(draggedValue)) {
//                //ko phai dragged
//                DocumentType docTypeOther =
//                        (DocumentType) parent.getChildAt(index).getData();
//                //kiem tra xem co phai vi tri nay la vi tri drag toi ko?
//                if (index == indexTo) {
//                    draggedDocType.setOrdinal(Long.valueOf(String.valueOf(index)));
//                    if (indexFrom > indexTo) {
//                        //neu keo len tren
//                        docTypeOther.setOrdinal(Long.valueOf(index + 1));
//                    } else {
//                        docTypeOther.setOrdinal(Long.valueOf(index - 1));
//                    }
//                } else {
//                    docTypeOther.setOrdinal(Long.valueOf(String.valueOf(index)));
//                }
//
//                documentTypeService.saveOrUpdate(docTypeOther);
//            }
//        }
//
//    }
}
