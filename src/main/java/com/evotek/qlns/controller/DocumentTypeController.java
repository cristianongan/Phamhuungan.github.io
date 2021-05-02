/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import com.evotek.qlns.extend.Messagebox;
import com.evotek.qlns.model.DocumentType;
import com.evotek.qlns.model.render.TreeDocumentTypeRender;
import com.evotek.qlns.service.DocumentTypeService;
import com.evotek.qlns.tree.model.TreeBasicModel;
import com.evotek.qlns.tree.node.DocumentTypeTreeNode;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;

/**
 *
 * @author LinhLH
 */
@Controller
@Scope("prototype")
public class DocumentTypeController extends BasicController<Window> {
	private static final long serialVersionUID = -6936773820341890421L;

	public static final Logger _log = LogManager.getLogger(DocumentTypeController.class);

	private static final String ADD_EDIT_PAGE = "~./pages/document_type/edit.zul";

	@Autowired
	private DocumentTypeService documentTypeService;

	private Button btnDelete;
	private Button btnDown;
	private Button btnInsert;
	private Button btnReset;
	private Button btnSave;
	private Button btnUp;

	private Hlayout winParent;

	private Map<Long, List<DocumentType>> docTypeMap;

	private Set<DocumentTypeTreeNode> changedTypeNode = new HashSet<DocumentTypeTreeNode>();

	private Tree tree;

	private Window windowDocumentType;

	private boolean doReload = false;

	private DocumentTypeTreeNode _buildCategoryTree() {
		// tạo cây menu không có gốc
		DocumentTypeTreeNode rootNode = new DocumentTypeTreeNode(null, new DocumentTypeTreeNode[] {});

		rootNode.setOpen(true);

		try {
			// Lấy danh sách các menu category
			List<DocumentType> roots = this.docTypeMap.get(null);

			for (DocumentType root : roots) {

				if (Validator.isNull(root) || Validator.isNull(root.getDocumentTypeId())) {
					continue;
				}

				addChildToParent(root, rootNode);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return rootNode;
	}

	public void addChildToParent(DocumentType parent, DocumentTypeTreeNode treeNode) {
		List<DocumentType> childs = this.docTypeMap.get(parent.getDocumentTypeId());

		if (Validator.isNotNull(childs)) {
			// Tạo cây con tu parent
			DocumentTypeTreeNode rootChilds = new DocumentTypeTreeNode(parent, new DocumentTypeTreeNode[] {});

			rootChilds.setOpen(true);

			// Gắn các menu item vào cây con vừa tạo
			for (DocumentType child : childs) {
				if (Validator.isNull(child) || Validator.isNull(child.getDocumentTypeId())) {
					continue;
				}

				addChildToParent(child, rootChilds);
			}

			// gắn cấy menu category vào cây menu
			treeNode.add(rootChilds);
		} else {
			treeNode.add(new DocumentTypeTreeNode(parent));
		}
	}

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		this.winParent = (Hlayout) this.arg.get(Constants.PARENT_WINDOW);

		onCreateTree();

		this.windowDocumentType.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {

				if (DocumentTypeController.this.doReload) {
					Events.sendEvent("onLoadPage", DocumentTypeController.this.winParent, null);
				}
			}
		});
	}

	@Override
	public void doBeforeComposeChildren(Window comp) throws Exception {
		super.doBeforeComposeChildren(comp);

		this.windowDocumentType = comp;

		this.docTypeMap = this.documentTypeService.getDocTypeMap();
	}

	private List<Long> getDocumentTypeGroup(Long rootId, List<Long> docTypeGroupIds) {
		List<DocumentType> docTypes = this.docTypeMap.get(rootId);

		if (Validator.isNotNull(docTypes)) {
			for (DocumentType docType : docTypes) {
				if (Validator.isNull(docType)) {
					continue;
				}

				docTypeGroupIds.add(docType.getDocumentTypeId());

				getDocumentTypeGroup(docType.getDocumentTypeId(), docTypeGroupIds);
			}
		}

		return docTypeGroupIds;
	}

	private DocumentType getSelectedItem() {
		Treeitem item = this.tree.getSelectedItem();

		return item == null ? null : (DocumentType) ((DocumentTypeTreeNode) item.getValue()).getData();
	}

	public void onAdd(Event event) {
		DocumentType documentType = (DocumentType) event.getData();

		Map map = new HashMap();

		map.put(Constants.TITLE, Labels.getLabel(LanguageKeys.ADD));
		map.put(Constants.PARENT_WINDOW, this.windowDocumentType);
		map.put(Constants.SECOND_OBJECT, documentType);

		Window win = (Window) Executions.createComponents(ADD_EDIT_PAGE, this.windowDocumentType, map);

		win.doModal();
	}

	public void onClick$btnCancel() {
		this.windowDocumentType.detach();

		if (this.doReload) {
			Events.sendEvent("onLoadPage", this.winParent, null);
		}
	}

	public void onClick$btnDelete() {
		final DocumentType docType = this.getSelectedItem();

		if (Validator.isNull(docType)) {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD), Labels.getLabel(LanguageKeys.ERROR),
					Messagebox.OK, Messagebox.EXCLAMATION, Messagebox.OK);
		} else {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
					Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE), Messagebox.OK | Messagebox.CANCEL,
					Messagebox.QUESTION, new EventListener<Event>() {
						@Override
						public void onEvent(Event e) throws Exception {
							if (Messagebox.ON_OK.equals(e.getName())) {
								try {
									List<Long> docTypeGroupIds = new ArrayList<Long>();

									docTypeGroupIds.add(docType.getDocumentTypeId());

									getDocumentTypeGroup(docType.getDocumentTypeId(), docTypeGroupIds);

									// xoa
									DocumentTypeController.this.documentTypeService.delete(docType, docTypeGroupIds);

									ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_DELETE_SUCCESS);
									// refresh lai cay
									onCreateTree();

									DocumentTypeController.this.doReload = true;
									// refresh lai danh muc tai lieu
//                                    Events.sendEvent("onLoadPage", winParent, null);
								} catch (Exception ex) {
									_log.error(ex.getMessage(), ex);

									Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_DELETE_FAIL));
								}
							}
						}
					});
		}
	}

	public void onClick$btnDown() {
		Treeitem item = this.tree.getSelectedItem();

		if (Validator.isNull(item)) {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD), Labels.getLabel(LanguageKeys.ERROR),
					Messagebox.OK, Messagebox.EXCLAMATION, Messagebox.OK);
		} else {
			TreeBasicModel treeModel = (TreeBasicModel) this.tree.getModel();

			DocumentTypeTreeNode treeNode = (DocumentTypeTreeNode) item.getValue();
			DocumentTypeTreeNode parentTreeNode = (DocumentTypeTreeNode) treeNode.getParent();

			List<TreeNode<DocumentType>> childNodes = parentTreeNode.getChildren();

			int index = childNodes.indexOf(treeNode);

			if (index < parentTreeNode.getChildren().size() - 1) {
				index++;
			}

			treeModel.remove(treeNode);

			treeModel.insert(parentTreeNode, index, index, new DocumentTypeTreeNode[] { treeNode });

			treeModel.addToSelection(treeNode);

//            this.updateOrdinal(childNodes);

			this.changedTypeNode.add(parentTreeNode);

			this.btnReset.setDisabled(this.changedTypeNode.isEmpty());
			this.btnSave.setDisabled(this.changedTypeNode.isEmpty());
		}
	}

	public void onClick$btnReset() {
		this.changedTypeNode.clear();

		onCreateTree();
	}

	public void onClick$btnSave() {
		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_SAVE_CHANGE),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_SAVE_CHANGE), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener<Event>() {
					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								updateOrdinal();

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_UPDATE_SUCCESS);
								// refresh lai danh muc tai lieu
//                                Events.sendEvent("onLoadPage", winParent, null);
								// disable button
								DocumentTypeController.this.btnSave.setDisabled(true);
								DocumentTypeController.this.btnReset.setDisabled(true);

								DocumentTypeController.this.doReload = true;
							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_UPDATE_FAIL));
							}
						}
					}
				});
	}

	public void onClick$btnUp() {
		Treeitem item = this.tree.getSelectedItem();

		if (Validator.isNull(item)) {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD), Labels.getLabel(LanguageKeys.ERROR),
					Messagebox.OK, Messagebox.EXCLAMATION, Messagebox.OK);
		} else {
			TreeBasicModel treeModel = (TreeBasicModel) this.tree.getModel();

			DocumentTypeTreeNode treeNode = (DocumentTypeTreeNode) item.getValue();
			DocumentTypeTreeNode parentTreeNode = (DocumentTypeTreeNode) treeNode.getParent();

			List<TreeNode<DocumentType>> childNodes = parentTreeNode.getChildren();

			int index = childNodes.indexOf(treeNode);

			if (index > 0) {
				index--;
			}

			treeModel.remove(treeNode);

			treeModel.insert(parentTreeNode, index, index, new DocumentTypeTreeNode[] { treeNode });

			treeModel.addToSelection(treeNode);

//            this.updateOrdinal(childNodes);

			this.changedTypeNode.add(parentTreeNode);

			this.btnReset.setDisabled(this.changedTypeNode.isEmpty());
			this.btnSave.setDisabled(this.changedTypeNode.isEmpty());
		}
	}

	public void onCreateTree() {
		this.btnDelete.setDisabled(true);
		this.btnUp.setDisabled(true);
		this.btnDown.setDisabled(true);

		TreeBasicModel treeConfigModel = new TreeBasicModel(_buildCategoryTree(), false);

		this.tree.setModel(treeConfigModel);

		this.tree.setItemRenderer(new TreeDocumentTypeRender(this.windowDocumentType, this.documentTypeService));

		this.tree.addEventListener(Events.ON_SELECT, new EventListener<Event>() {

			@Override
			public void onEvent(Event t) throws Exception {
				boolean disable = DocumentTypeController.this.tree.getSelectedCount() < 0;

				DocumentTypeController.this.btnDelete.setDisabled(disable);
				DocumentTypeController.this.btnUp.setDisabled(disable);
				DocumentTypeController.this.btnDown.setDisabled(disable);
			}
		});

		this.tree.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {

			@Override
			public void onEvent(Event t) throws Exception {
				DocumentTypeController.this.tree.clearSelection();

				DocumentTypeController.this.btnDelete.setDisabled(true);
				DocumentTypeController.this.btnUp.setDisabled(true);
				DocumentTypeController.this.btnDown.setDisabled(true);
			}
		});

		this.btnInsert.addForward(Events.ON_CLICK, this.windowDocumentType, "onAdd", null);
	}

	public void onEdit(Event event) {
		DocumentType documentType = (DocumentType) event.getData();

		Map map = new HashMap();

		map.put(Constants.TITLE, Labels.getLabel(LanguageKeys.TITLE_EDIT_DOCUMENT_TYPE));
		map.put(Constants.PARENT_WINDOW, this.windowDocumentType);
		map.put(Constants.OBJECT, documentType);

		Window win = (Window) Executions.createComponents(ADD_EDIT_PAGE, this.windowDocumentType, map);

		win.doModal();
	}

	public void onLoadData(Event event) {
		onCreateTree();

		this.doReload = true;
		// refresh lai danh muc tai lieu
//        Events.sendEvent("onLoadPage", winParent, null);
	}

	private void updateOrdinal() {
		for (DocumentTypeTreeNode docTreeNode : this.changedTypeNode) {
			List<TreeNode<DocumentType>> childNodes = docTreeNode.getChildren();

			for (TreeNode<DocumentType> treeNode : childNodes) {
				DocumentType docType = treeNode.getData();

				docType.setOrdinal(Long.valueOf(childNodes.indexOf(treeNode)));
			}

			this.documentTypeService.saveOrUpdate(docTreeNode.getData());
		}

		this.changedTypeNode.clear();
	}
}
