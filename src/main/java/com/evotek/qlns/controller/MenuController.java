/*
 * Copyright 2013 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import com.evotek.qlns.extend.Messagebox;
import com.evotek.qlns.model.Category;
import com.evotek.qlns.model.render.TreeCategoryRender;
import com.evotek.qlns.service.CategoryService;
import com.evotek.qlns.tree.model.TreeBasicModel;
import com.evotek.qlns.tree.node.CategoryTreeNode;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;

/**
 *
 * @author LinhLH2
 */
@Controller
@Scope("prototype")
public class MenuController extends BasicController<Div> implements Serializable {

	private static final long serialVersionUID = 882799078126995809L;

	private static final Logger _log = LogManager.getLogger(MenuController.class);

	private static final String EDIT_PAGE = "~./pages/manager_menu/edit.zul";

	@Autowired
	private CategoryService categoryService;

	private Include parent;

	private TreeBasicModel treeCategoryModelUtil;

	private Tree treeMenu;

	private Div winMenu;

	/**
	 * Hàm tạo cây menu
	 * 
	 * @return
	 * @throws Exception
	 */
	private CategoryTreeNode _buildCategoryTree() throws Exception {
		// tạo cây menu không có gốc
		CategoryTreeNode menu = new CategoryTreeNode(null, new CategoryTreeNode[] {});

		menu.setOpen(true);

		try {

			// Lấy danh sách các menu category
			List<Category> roots = this.categoryService.getCategoryByParentId(null);

			for (Category root : roots) {

				// Lấy danh sách các menu item ứng với mỗi menu category
				List<Category> childs = this.categoryService.getCategoryByParentId(root.getCategoryId());
				if (!childs.isEmpty()) {
					// Tạo cây con với gốc là menu category
					CategoryTreeNode item = new CategoryTreeNode(root, new CategoryTreeNode[] {});

					item.setOpen(true);

					// Gắn các menu item vào cây con vừa tạo
					for (Category child : childs) {
						item.add(new CategoryTreeNode(child));
					}

					// gắn cấy menu category vào cây menu
					menu.add(item);
				} else {
					menu.add(new CategoryTreeNode(root));
				}
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return menu;
	}

	@Override
	public void doAfterCompose(Div comp) throws Exception {
		super.doAfterCompose(comp);

		this.parent = (Include) this.winMenu.getParent();

		this.onCreateTree();
	}

	@Override
	public void doBeforeComposeChildren(Div comp) throws Exception {
		super.doBeforeComposeChildren(comp);

		this.winMenu = comp;
	}

	/**
	 * Hàm lấy danh sách id các menu item được chọn
	 * 
	 * @return
	 */
	public List<Long> getSelectedItem() {
		List<Long> categoryIds = new ArrayList<Long>();

		if (this.treeMenu.getItems() != null) {
			for (Object item : this.treeMenu.getItems()) {
				Treeitem treeitem = (Treeitem) item;

				if (treeitem.isSelected()) {
					Category categoryTemp = (Category) treeitem.getAttribute("data");

					categoryIds.add(categoryTemp.getCategoryId());
				}
			}
		} else {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD));
		}

		return categoryIds;
	}

	public void onClick$adminPage() {
		this.parent.setSrc("~./pages/admin/default.zul");
	}

	/**
	 * Hàm xử lý sự kiện onClick khi click vào nút "Thêm" - có id = btnAdd
	 */
	public void onClick$btnAdd() {
		// Tạo map để set các tham số truyền vào khi mở popup cập nhật/thêm mới
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Constants.PARENT_WINDOW, this.winMenu);
		parameters.put(Constants.TITLE, Labels.getLabel(LanguageKeys.ADD));
		parameters.put(Constants.ID, 0L);

		Window win = (Window) Executions.createComponents(EDIT_PAGE, this.winMenu, parameters);

		win.doModal();
	}

	/**
	 * Hàm tạo cây menu
	 * 
	 * @throws Exception
	 */
	public void onCreateTree() throws Exception {
		try {
			this.treeCategoryModelUtil = new TreeBasicModel(_buildCategoryTree());

			this.treeCategoryModelUtil.setMultiple(true);

			this.treeMenu.setItemRenderer(new TreeCategoryRender(this.winMenu));
			this.treeMenu.setModel(this.treeCategoryModelUtil);
			this.treeMenu.setCheckmark(true);
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

	}

	// get set service

	/**
	 * Hàm xóa category khi click chọn nút "Xóa". Category chỉ có thể xóa sau khi
	 * Khóa (status=0)
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onDeleteCategory(Event event) throws Exception {
		final Category category = (Category) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener() {

					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								MenuController.this.categoryService.deleteCategory(category);

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_DELETE_SUCCESS);

								onCreateTree();
							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_DELETE_FAIL));
							}
						}
					}
				});
	}

	/**
	 * Hàm thực hiện reload lại cây menu
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onLoadData(Event event) throws Exception {
		this.onCreateTree();
	}

	// event method
	/**
	 * Hàm xử lý sự kiện khi click vào nút "Khóa". Hàm sẽ chuyển menu item về trạng
	 * thái khóa(không hoạt động), status=0
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onLockCategory(Event event) throws Exception {
		final Category category = (Category) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_LOCK),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_LOCK), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener() {

					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								MenuController.this.categoryService.lockCategory(category);

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_LOCK_ITEM_SUCCESS);

								onCreateTree();
							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_LOCK_ITEM_FAIL));
							}
						}
					}
				});
	}

	/**
	 * Hàm xử lý sự kiện khi click vào nút "Mở khóa". Hàm sẽ chuyển menu item về
	 * trạng thái mở khóa (hoạt động), status=1
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onUnlockCategory(Event event) throws Exception {
		final Category category = (Category) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_UNLOCK),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_UNLOCK), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener() {

					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								MenuController.this.categoryService.unlockCategory(category);

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_UNLOCK_ITEM_SUCCESS);

								onCreateTree();
							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_UNLOCK_ITEM_FAIL));
							}
						}
					}
				});
	}
}
