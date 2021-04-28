/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import com.evotek.qlns.extend.Messagebox;
import com.evotek.qlns.model.Category;
import com.evotek.qlns.model.Group;
import com.evotek.qlns.model.Right;
import com.evotek.qlns.model.render.GroupRender;
import com.evotek.qlns.model.render.RightRender;
import com.evotek.qlns.service.CategoryService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;

/**
 *
 * @author linhlh2
 */
@Controller
public class RightAssignmentController extends BasicController<Window> implements Serializable {

	private static final long serialVersionUID = 1370926408457L;

	private static final Logger _log = LogManager.getLogger(RightAssignmentController.class);

	private static final String EDIT_GROUP_PAGE = "/html/pages/manager_menu/edit_group.zul";

	private static final String EDIT_RIGHT_PAGE = "/html/pages/manager_menu/edit_right.zul";

	@Autowired
	private CategoryService categoryService;

	private Category category;
	private Long categoryId;

	// get set service

	private Grid groupsSearchResult;

	private Grid rightSearchResult;

	private Window winRight;
//    private Window winTemp;

	private Map<String, Object> _createParameterMap() {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Constants.PARENT_WINDOW, this.winRight);
		parameters.put(Constants.OBJECT, this.category);

		return parameters;
	}

	private String _createTitle(String title, String categoryKey) {
		StringBuilder sb = new StringBuilder(title);

		sb.append(StringPool.SPACE);
		sb.append(StringPool.MINUS);
		sb.append(StringPool.SPACE);
		sb.append(Labels.getLabel(categoryKey));

		return sb.toString();
	}

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		// init data
		this.initData();
	}

	@Override
	public void doBeforeComposeChildren(Window comp) throws Exception {
		super.doBeforeComposeChildren(comp);

		this.winRight = comp;
	}

	public void initData() throws Exception {
		try {
//            winTemp = (Window) arg.get(Constants.PARENT_WINDOW);

			this.category = (Category) this.arg.get(Constants.OBJECT);

			this.categoryId = this.category.getCategoryId();

			this.winRight.setTitle(Labels.getLabel(LanguageKeys.TITLE_MANAGER_RIGHT,
					new String[] { Labels.getLabel(this.category.getLanguageKey()) }));

			// seach
			this.searchGroups();

			this.searchRight();
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	// event method
	public void onClick$btnAddGroups() {
		Window win = (Window) Executions.createComponents(EDIT_GROUP_PAGE, this.winRight, _createParameterMap());

		win.doModal();
	}

	public void onClick$btnAddRight() {
		Window win = (Window) Executions.createComponents(EDIT_RIGHT_PAGE, this.winRight, _createParameterMap());

		win.doModal();
	}

	public void onClick$btnCancel() {
		this.winRight.detach();
	}

	public void onDeleteGroups(Event event) throws Exception {
		final Group group = (Group) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener() {

					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								RightAssignmentController.this.categoryService.deleteGroup(group);

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_DELETE_SUCCESS);

								searchGroups();
							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_DELETE_FAIL));
							}
						}
					}
				});
	}

	public void onDeleteRight(Event event) throws Exception {
		final Right right = (Right) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener() {

					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								RightAssignmentController.this.categoryService.deleteRight(right);

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_DELETE_SUCCESS);

								searchRight();
							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_DELETE_FAIL));
							}
						}
					}
				});
	}
	// event method

	public void onLoadGroups(Event event) throws Exception {
		this.searchGroups();
	}

	public void onLoadRight(Event event) throws Exception {
		this.searchRight();
	}

	public void onLockGroups(Event event) throws Exception {
		final Group group = (Group) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_LOCK),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_LOCK), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener<Event>() {

					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								RightAssignmentController.this.categoryService.lockGroup(group);

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_LOCK_ITEM_SUCCESS);

								searchGroups();
							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_LOCK_ITEM_FAIL));
							}
						}
					}
				});
	}

	public void onLockRight(Event event) throws Exception {
		final Right right = (Right) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_LOCK),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_LOCK), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener<Event>() {

					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								RightAssignmentController.this.categoryService.lockRight(right);

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_LOCK_ITEM_SUCCESS);

								searchRight();
							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_LOCK_ITEM_FAIL));
							}
						}
					}
				});
	}

	public void onUnlockGroups(Event event) throws Exception {
		final Group group = (Group) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_UNLOCK),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_UNLOCK), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener() {

					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								RightAssignmentController.this.categoryService.unlockGroup(group);

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_UNLOCK_ITEM_SUCCESS);

								searchGroups();
							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_UNLOCK_ITEM_FAIL));
							}
						}
					}
				});
	}

	public void onUnlockRight(Event event) throws Exception {
		final Right right = (Right) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_UNLOCK),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_UNLOCK), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener() {

					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								RightAssignmentController.this.categoryService.unlockRight(right);

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_UNLOCK_ITEM_SUCCESS);

								searchRight();
							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_UNLOCK_ITEM_FAIL));
							}
						}
					}
				});
	}

	// search data
	public void searchGroups() throws Exception {
		try {
			List<Group> groups = this.categoryService.getGroupByCategoryId(this.categoryId);

			this.groupsSearchResult.setRowRenderer(new GroupRender(this.winRight));
			this.groupsSearchResult.setModel(new ListModelList<Group>(groups));

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	public void searchRight() throws Exception {
		try {
			List<Right> rights = this.categoryService.getRightByCategoryId(this.categoryId);

			this.rightSearchResult.setRowRenderer(new RightRender(this.winRight));
			this.rightSearchResult.setModel(new ListModelList<Right>(rights));
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}
}
