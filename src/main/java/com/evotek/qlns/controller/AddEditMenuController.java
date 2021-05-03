package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Category;
import com.evotek.qlns.model.SimpleModel;
import com.evotek.qlns.model.render.BandboxCategoryRender;
import com.evotek.qlns.service.CategoryService;
import com.evotek.qlns.tree.model.TreeBasicModel;
import com.evotek.qlns.tree.node.CategoryTreeNode;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author LinhLH
 */
@Controller
@Scope("prototype")
public class AddEditMenuController extends BasicController<Window> implements Serializable {

	private static final long serialVersionUID = -5083458946646218726L;

	private static final Logger _log = LogManager.getLogger(AddEditMenuController.class);

	private static final Long MENU_CATEGORY = 0L;

	private static final Long MENU_ITEM = 1L;

	@Autowired
	private CategoryService categoryService;

	// private Row rowNameFolder;
	private Bandbox bbMenuParent;
	
	private Category category;
	private Combobox cbMenuType;
	
	private Div winTemp;
	
	private Row rowIconUrl;
	private Row rowMenu;
	
	private Textbox tbDescription;
	private Textbox tbFolderName;
	private Textbox tbIcon;
	private Textbox tbLanguageKey;
	private Textbox tbViewPage;
	private Textbox tbWeight;
	
	private Tree treeMenu;
	
	private TreeBasicModel treeCategoryModelUtil;
	
	private Window winAddMenu;
//    private Row rowViewPage;

	private CategoryTreeNode _buildCategoryTree() throws Exception {
		CategoryTreeNode menu = new CategoryTreeNode(null, new CategoryTreeNode[] {});

		try {
			List<Category> roots = this.categoryService.getCategoryByParentId(null);

			for (Category root : roots) {
				CategoryTreeNode item = new CategoryTreeNode(root);

				menu.add(item);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return menu;
	}

	private void _setEditForm() throws Exception {
		try {
			if (MENU_CATEGORY.equals(this.category.getType())) {
				_showHideControlItem(false);
			} else if (MENU_ITEM.equals(this.category.getType())) {
				_showHideControlItem(true);

				this.onCreateTree();

				if (Validator.isNotNull(this.category.getParentId())) {
					Category parentCategory = this.categoryService.getParentCategory(this.category.getParentId());

					this.bbMenuParent.setAttribute("categoryId", parentCategory.getCategoryId());

					this.bbMenuParent.setValue(Labels.getLabel(parentCategory.getLanguageKey()));
				}

				this.tbIcon.setValue(this.category.getIcon());
			}

			this.tbFolderName.setValue(this.category.getFolderName());
			this.tbViewPage.setValue(this.category.getViewPage());
			this.tbLanguageKey.setValue(this.category.getLanguageKey());
			this.tbWeight.setValue(GetterUtil.getString(this.category.getWeight()));
			this.tbDescription.setValue(this.category.getDescription());
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

	}

	private void _showHideControlItem(boolean check) {

		this.rowIconUrl.setVisible(check);

//        rowNameFolder.setVisible(check);

//        rowViewPage.setVisible(check);

		this.rowMenu.setVisible(check);
	}

	private boolean _validate(Long categoryType, Long parentId, String languageKey, String folderName, String viewPage,
			String icon, String weight, String description) {
//        boolean isMenuItem = categoryType.equals(MENU_ITEM);

//        if(isMenuItem && Validator.isNull(parentId)){
//            bbMenuParent.setErrorMessage(Values.getRequiredSelectMsg(
//                    Labels.getLabel(LanguageKeys.PARENT_MENU)));
//
//            return false;
//        }

		if (Validator.isNull(languageKey)) {
			this.tbLanguageKey.setErrorMessage(Values.getRequiredInputMsg(Labels.getLabel(LanguageKeys.LANGUAGE_KEY)));

			return false;
		}

		if (languageKey.length() > Values.MEDIUM_LENGTH) {
			this.tbLanguageKey.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.LANGUAGE_KEY), Values.MEDIUM_LENGTH));

			return false;
		}

//        if (isMenuItem) {
		if (Validator.isNull(folderName)) {
			this.tbFolderName.setErrorMessage(Values.getRequiredInputMsg(Labels.getLabel(LanguageKeys.FOLDER_NAME)));

			return false;
		}

		if (folderName.length() > Values.MEDIUM_LENGTH) {
			this.tbFolderName.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.FOLDER_NAME), Values.MEDIUM_LENGTH));

			return false;
		}

		if (Validator.isNull(viewPage)) {
			this.tbViewPage.setErrorMessage(Values.getRequiredInputMsg(Labels.getLabel(LanguageKeys.VIEW_PAGE)));

			return false;
		}

		if (viewPage.length() > Values.MEDIUM_LENGTH) {
			this.tbViewPage.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.VIEW_PAGE), Values.MEDIUM_LENGTH));

			return false;
		}
//        }

		if (Validator.isNotNull(icon) && icon.length() > Values.LONG_LENGTH) {
			this.tbIcon.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.ICON), Values.MEDIUM_LENGTH));

			return false;
		}

		if (Validator.isNotNull(weight) && !Validator.isNumberic(weight)) {
			this.tbWeight.setErrorMessage(Values.getInputNumberOnlyMsg(Labels.getLabel(LanguageKeys.WEIGHT)));

			return false;
		}

		if (Validator.isNotNull(description) && description.length() > Values.GREATE_LONG_LENGTH) {
			this.tbDescription.setErrorMessage(Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.DESCRIPTION),
					Values.GREATE_LONG_LENGTH));

			return false;
		}

		return true;
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

		this.winAddMenu = comp;
	}

	// init data
	public void initData() throws Exception {
		try {
			this.winTemp = (Div) this.arg.get(Constants.Attr.PARENT_WINDOW);

			this.category = (Category) this.arg.get(Constants.Attr.OBJECT);

			if (Validator.isNotNull(this.category)) {
				this.winAddMenu.setTitle((String) this.arg.get(Constants.Attr.TITLE));

				this._setEditForm();
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	// event
	public void onAfterRender$cbMenuType() {
		if (Validator.isNotNull(this.category) && Validator.isNotNull(this.category.getType())) {
			this.cbMenuType.setSelectedIndex(this.category.getType().intValue());

			_showHideControlItem(MENU_ITEM.equals(this.category.getType()));

			this.cbMenuType.setDisabled(true);
		} else {
			this.cbMenuType.setSelectedIndex(MENU_CATEGORY.intValue());

			_showHideControlItem(false);
		}
	}

	public void onChange$cbMenuType() throws Exception {
		try {
			Long categoryType = ComponentUtil.getComboboxValue(this.cbMenuType);

			if (categoryType.equals(Values.MENU_TYPE_CATEGORY)) {
				_showHideControlItem(false);
			} else if (categoryType.equals(Values.MENU_TYPE_ITEM)) {
				_showHideControlItem(true);

				this.onCreateTree();
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

	}

	public void onClick$btnCancel() {
		this.winAddMenu.detach();
	}

	public void onClick$btnSave() throws Exception {
		boolean update = true;

		try {
			Long categoryType = ComponentUtil.getComboboxValue(this.cbMenuType);
			Long parentId = GetterUtil.getLong(this.bbMenuParent.getAttribute("categoryId"));
			String languageKey = GetterUtil.getString(this.tbLanguageKey.getValue());
			String folderName = GetterUtil.getString(this.tbFolderName.getValue());
			String viewPage = GetterUtil.getString(this.tbViewPage.getValue());
			String icon = GetterUtil.getString(this.tbIcon.getValue());
			String weight = GetterUtil.getString(this.tbWeight.getValue());
			String description = GetterUtil.getString(this.tbDescription.getValue());

			if (_validate(categoryType, parentId, languageKey, folderName, viewPage, icon, weight, description)) {
				if (Validator.isNull(this.category)) {
					update = false;

					this.category = new Category();

					this.category.setType(categoryType);
					this.category.setCreateDate(new Date());
					this.category.setUserId(getUserId());
					this.category.setUserName(getUserName());
					this.category.setStatus(Values.STATUS_ACTIVE);
				}

				String rightName = this.category.getFolderName();

				this.category.setModifiedDate(new Date());
				this.category.setLanguageKey(languageKey);
				this.category.setWeight(GetterUtil.getDouble(weight));
				this.category.setDescription(description);
				this.category.setImmune(Values.NOT_IMMUNE);
				this.category.setFolderName(folderName);
				this.category.setViewPage(viewPage);

				if (categoryType.equals(Values.MENU_TYPE_ITEM)) {
					this.category.setParentId(parentId);
//                    category.setFolderName(folderName);
//                    category.setViewPage(viewPage);
					this.category.setIcon(icon);
				}

				this.categoryService.saveOrUpdateCategory(this.category, rightName, !update);

				ComponentUtil.createSuccessMessageBox(ComponentUtil.getSuccessKey(update));

				this.winAddMenu.detach();

				Events.sendEvent("onLoadData", this.winTemp, null);
			}

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);

			Messagebox.show(Labels.getLabel(ComponentUtil.getFailKey(update)));
		}
	}

	public void onCreate$cbMenuType() {
		List<SimpleModel> menuTypes = this.categoryService.getMenuType();

		this.cbMenuType.setModel(new ListModelList<SimpleModel>(menuTypes));
	}
	// event

	public void onCreateTree() throws Exception {
		try {
			this.treeCategoryModelUtil = new TreeBasicModel(_buildCategoryTree());

			this.treeMenu.setItemRenderer(new BandboxCategoryRender(this.bbMenuParent));
			this.treeMenu.setModel(this.treeCategoryModelUtil);
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

	}

}
