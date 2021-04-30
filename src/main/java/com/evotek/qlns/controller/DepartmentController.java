/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.evotek.qlns.model.Department;
import com.evotek.qlns.model.render.TreeDeptRender;
import com.evotek.qlns.service.DepartmentService;
import com.evotek.qlns.tree.model.TreeBasicModel;
import com.evotek.qlns.tree.node.DepartmentTreeNode;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;

/**
 *
 * @author LinhLH
 */
@Controller
public class DepartmentController extends BasicController<Window> implements Serializable {
	private static final long serialVersionUID = -3705474456101307493L;

	public static final Logger _log = LogManager.getLogger(DepartmentController.class);

	@Autowired
	private DepartmentService departmentService;

	private static final String ADD_EDIT_PAGE = "~./pages/department/edit.zul";

	private Button btnDelete;

	private Button btnDown;
	private Button btnInsert;
	private Button btnReset;
	private Button btnSave;
	private Button btnUp;
	private Set<DepartmentTreeNode> changedNode = new HashSet<DepartmentTreeNode>();

	private Map<Long, List<Department>> deptMaps = new HashMap<Long, List<Department>>();

	private boolean doReload = false;

	private Tree tree;

	private Window winDept;

	private Hlayout winParent;

	public DepartmentTreeNode _buildDeptTree() {
		// tạo cây menu không có gốc
		DepartmentTreeNode rootNode = new DepartmentTreeNode(null, new DepartmentTreeNode[] {});

		rootNode.setOpen(true);

		try {
			// Lấy danh sách các menu category
			List<Department> roots = this.departmentService.getDepartmentByParentId(null);

			this.deptMaps.put(null, roots);

			for (Department root : roots) {
				addChildToParent(root, rootNode);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return rootNode;
	}

	public void addChildToParent(Department parent, DepartmentTreeNode treeNode) {
		List<Department> childs = this.departmentService.getDepartmentByParentId(parent.getDeptId());

		if (Validator.isNotNull(childs)) {
			this.deptMaps.put(parent.getDeptId(), childs);
			// Tạo cây con tu parent
			DepartmentTreeNode rootChilds = new DepartmentTreeNode(parent, new DepartmentTreeNode[] {});

			rootChilds.setOpen(true);

			// Gắn các menu item vào cây con vừa tạo
			for (Department child : childs) {
				addChildToParent(child, rootChilds);
			}

			// gắn cấy menu category vào cây menu
			treeNode.add(rootChilds);
		} else {
			treeNode.add(new DepartmentTreeNode(parent));
		}
	}

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		this.winParent = (Hlayout) this.arg.get(Constants.PARENT_WINDOW);

		onCreateTree();

		this.winDept.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {

				if (DepartmentController.this.doReload) {
					Events.sendEvent("onLoadPage", DepartmentController.this.winParent, null);
				}
			}
		});
	}

	@Override
	public void doBeforeComposeChildren(Window comp) throws Exception {
		super.doBeforeComposeChildren(comp);

		this.winDept = comp;
	}

	private List<Long> getDeptGroup(Long rootId, List<Long> deptIds) {
		List<Department> depts = this.deptMaps.get(rootId);

		if (Validator.isNotNull(depts)) {
			for (Department dept : depts) {
				if (Validator.isNull(dept)) {
					continue;
				}

				deptIds.add(dept.getDeptId());

				getDeptGroup(dept.getDeptId(), deptIds);
			}
		}

		return deptIds;
	}

	private Department getSelectedItem() {
		Treeitem item = this.tree.getSelectedItem();

		return item == null ? null : (Department) ((DepartmentTreeNode) item.getValue()).getData();
	}

	public void onAdd(Event event) {
		Department dept = (Department) event.getData();

		Map map = new HashMap();

		map.put(Constants.TITLE, Labels.getLabel(LanguageKeys.ADD));
		map.put(Constants.PARENT_WINDOW, this.winDept);
		map.put(Constants.SECOND_OBJECT, dept);

		Window win = (Window) Executions.createComponents(ADD_EDIT_PAGE, this.winDept, map);

		win.doModal();
	}

	public void onClick$btnCancel() {
		this.winDept.detach();

		if (this.doReload) {
			Events.sendEvent("onLoadPage", this.winParent, null);
		}
	}

	public void onClick$btnDelete() {
		final Department dept = this.getSelectedItem();

		if (Validator.isNull(dept)) {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD), Labels.getLabel(LanguageKeys.ERROR),
					Messagebox.OK, Messagebox.EXCLAMATION, Messagebox.OK);
		} else {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
					Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE), Messagebox.OK | Messagebox.CANCEL,
					Messagebox.QUESTION, new EventListener() {
						@Override
						public void onEvent(Event e) throws Exception {
							if (Messagebox.ON_OK.equals(e.getName())) {
								try {
									List<Long> deptIds = new ArrayList<Long>();

									deptIds.add(dept.getDeptId());

									getDeptGroup(dept.getDeptId(), deptIds);

									// xoa
									DepartmentController.this.departmentService.delete(deptIds);
									// update ordinal
									DepartmentController.this.departmentService.updateOrdinal(dept.getParentId(),
											dept.getOrdinal());

									ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_DELETE_SUCCESS);
									// refresh lai cay
									onCreateTree();

									DepartmentController.this.doReload = true;
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

			DepartmentTreeNode treeNode = (DepartmentTreeNode) item.getValue();
			DepartmentTreeNode parentTreeNode = (DepartmentTreeNode) treeNode.getParent();

			List<TreeNode<Department>> childNodes = parentTreeNode.getChildren();

			int index = childNodes.indexOf(treeNode);

			if (index < parentTreeNode.getChildren().size() - 1) {
				index++;
			}

			treeModel.remove(treeNode);

			treeModel.insert(parentTreeNode, index, index, new DepartmentTreeNode[] { treeNode });

			treeModel.addToSelection(treeNode);

//            this.updateOrdinal(childNodes);

			this.changedNode.add(parentTreeNode);

			this.btnReset.setDisabled(this.changedNode.isEmpty());
			this.btnSave.setDisabled(this.changedNode.isEmpty());
		}
	}

	public void onClick$btnReset() {
		this.changedNode.clear();

		onCreateTree();
	}

	public void onClick$btnSave() {
		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_SAVE_CHANGE),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_SAVE_CHANGE), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener() {
					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								updateOrdinal();

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_UPDATE_SUCCESS);
								// refresh lai danh muc tai lieu
//                                Events.sendEvent("onLoadPage", winParent, null);
								// disable button
								DepartmentController.this.btnSave.setDisabled(true);
								DepartmentController.this.btnReset.setDisabled(true);

								DepartmentController.this.doReload = true;
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

			DepartmentTreeNode treeNode = (DepartmentTreeNode) item.getValue();
			DepartmentTreeNode parentTreeNode = (DepartmentTreeNode) treeNode.getParent();

			List<TreeNode<Department>> childNodes = parentTreeNode.getChildren();

			int index = childNodes.indexOf(treeNode);

			if (index > 0) {
				index--;
			}

			treeModel.remove(treeNode);

			treeModel.insert(parentTreeNode, index, index, new DepartmentTreeNode[] { treeNode });

			treeModel.addToSelection(treeNode);

//            this.updateOrdinal(childNodes);

			this.changedNode.add(parentTreeNode);

			this.btnReset.setDisabled(this.changedNode.isEmpty());
			this.btnSave.setDisabled(this.changedNode.isEmpty());
		}
	}

	public void onCreateTree() {
		this.btnDelete.setDisabled(true);
		this.btnUp.setDisabled(true);
		this.btnDown.setDisabled(true);

		TreeBasicModel treeConfigModel = new TreeBasicModel(_buildDeptTree(), false);

		this.tree.setModel(treeConfigModel);

		this.tree.setItemRenderer(new TreeDeptRender(this.winDept, this.departmentService));

		this.tree.addEventListener(Events.ON_SELECT, new EventListener<Event>() {

			@Override
			public void onEvent(Event t) throws Exception {
				boolean disable = DepartmentController.this.tree.getSelectedCount() < 0;

				DepartmentController.this.btnDelete.setDisabled(disable);
				DepartmentController.this.btnUp.setDisabled(disable);
				DepartmentController.this.btnDown.setDisabled(disable);
			}
		});

		this.tree.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {

			@Override
			public void onEvent(Event t) throws Exception {
				DepartmentController.this.tree.clearSelection();

				DepartmentController.this.btnDelete.setDisabled(true);
				DepartmentController.this.btnUp.setDisabled(true);
				DepartmentController.this.btnDown.setDisabled(true);
			}
		});

		this.btnInsert.addForward(Events.ON_CLICK, this.winDept, "onAdd", null);
	}

	public void onEdit(Event event) {
		Department dept = (Department) event.getData();

		Map map = new HashMap();

		map.put(Constants.TITLE, Labels.getLabel(LanguageKeys.TITLE_EDIT_DEPARTMENT));
		map.put(Constants.PARENT_WINDOW, this.winDept);
		map.put(Constants.OBJECT, dept);

		Window win = (Window) Executions.createComponents(ADD_EDIT_PAGE, this.winDept, map);

		win.doModal();
	}

	public void onLoadData(Event event) {
		onCreateTree();

		this.doReload = true;
	}

	private void updateOrdinal() {
		for (DepartmentTreeNode deptTreeNode : this.changedNode) {
			List<TreeNode<Department>> childNodes = deptTreeNode.getChildren();

			for (TreeNode<Department> treeNode : childNodes) {
				Department dept = treeNode.getData();

				dept.setOrdinal(Long.valueOf(childNodes.indexOf(treeNode)));
			}

			this.departmentService.saveOrUpdate(deptTreeNode.getData());
		}

		this.changedNode.clear();
	}
}
