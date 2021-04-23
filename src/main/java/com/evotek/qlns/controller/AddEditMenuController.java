package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
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
public class AddEditMenuController extends BasicController<Window>
        implements Serializable {

    private Category category;

    private Combobox cbMenuType;

    private Bandbox bbMenuParent;

    private Window winAddMenu;
    private Div winTemp;

    private Textbox tbLanguageKey;
    private Textbox tbDescription;
    private Textbox tbFolderName;
    private Textbox tbViewPage;
    private Textbox tbWeight;
    private Textbox tbIcon;

    private Row rowMenu;
//    private Row rowNameFolder;
//    private Row rowViewPage;
    private Row rowIconUrl;
    
    private Tree treeMenu;

    private TreeBasicModel treeCategoryModelUtil;

    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp);

        this.winAddMenu = comp;
    }

    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        //init data
        this.initData();
    }

    //init data
    public void initData() throws Exception {
        try {
            winTemp = (Div) arg.get(Constants.PARENT_WINDOW);

            category = (Category) arg.get(Constants.OBJECT);

            if(Validator.isNotNull(category)){
                winAddMenu.setTitle((String) arg.get(Constants.TITLE));

                this._setEditForm();
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    private void _setEditForm() throws Exception {
        try {
            if (MENU_CATEGORY.equals(category.getType())) {
                _showHideControlItem(false);
            } else if (MENU_ITEM.equals(category.getType())) {
                _showHideControlItem(true);

                this.onCreateTree();

                if (Validator.isNotNull(category.getParentId())) {
                    Category parentCategory = categoryService.getParentCategory(
                            category.getParentId());

                    bbMenuParent.setAttribute("categoryId",
                            parentCategory.getCategoryId());

                    bbMenuParent.setValue(Labels.getLabel(
                            parentCategory.getLanguageKey()));
                }

                
                tbIcon.setValue(category.getIcon());
            }

            tbFolderName.setValue(category.getFolderName());
            tbViewPage.setValue(category.getViewPage());
            tbLanguageKey.setValue(category.getLanguageKey());
            tbWeight.setValue(GetterUtil.getString(category.getWeight()));
            tbDescription.setValue(category.getDescription());
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

    }

    private void _showHideControlItem(boolean check) {

        rowIconUrl.setVisible(check);

//        rowNameFolder.setVisible(check);

//        rowViewPage.setVisible(check);

        rowMenu.setVisible(check);
    }

    //event
    public void onAfterRender$cbMenuType() {
        if (Validator.isNotNull(category)
                && Validator.isNotNull(category.getType())) {
            cbMenuType.setSelectedIndex(category.getType().intValue());
            
            _showHideControlItem(MENU_ITEM.equals(category.getType()));

            cbMenuType.setDisabled(true);
        } else {
            cbMenuType.setSelectedIndex(MENU_CATEGORY.intValue());

            _showHideControlItem(false);
        }
    }

    public void onChange$cbMenuType() throws Exception {
        try {
            Long categoryType = ComponentUtil.getComboboxValue(cbMenuType);

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

    public void onCreate$cbMenuType(){
        List<SimpleModel> menuTypes = categoryService.getMenuType();

        cbMenuType.setModel(new ListModelList<SimpleModel>(menuTypes));
    }
    //event

    public void onCreateTree() throws Exception {
        try {
            treeCategoryModelUtil = new TreeBasicModel(_buildCategoryTree());

            treeMenu.setItemRenderer(new BandboxCategoryRender(bbMenuParent));
            treeMenu.setModel(treeCategoryModelUtil);
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

    }

    private CategoryTreeNode _buildCategoryTree()
            throws Exception{
        CategoryTreeNode menu = new CategoryTreeNode(null,
                new CategoryTreeNode[]{});

        try{
            List<Category> roots = categoryService.getCategoryByParentId(null);

            for(Category root: roots){
                CategoryTreeNode item = new CategoryTreeNode(root);

                menu.add(item);
            }
        }catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return menu;
    }

    public void onClick$btnCancel(){
        winAddMenu.detach();
    }

    public void onClick$btnSave() throws Exception {
        boolean update = true;

        try{
            Long categoryType = ComponentUtil.getComboboxValue(cbMenuType);
            Long parentId = GetterUtil.getLong(
                    bbMenuParent.getAttribute("categoryId"));
            String languageKey = GetterUtil.getString(tbLanguageKey.getValue());
            String folderName = GetterUtil.getString(tbFolderName.getValue());
            String viewPage = GetterUtil.getString(tbViewPage.getValue());
            String icon = GetterUtil.getString(tbIcon.getValue());
            String weight = GetterUtil.getString(tbWeight.getValue());
            String description = GetterUtil.getString(tbDescription.getValue());
            
            if (_validate(categoryType, parentId, languageKey, folderName,
                    viewPage, icon, weight, description)) {
                if(Validator.isNull(category)){
                    update = false;

                    category = new Category();

                    category.setType(categoryType);
                    category.setCreateDate(new Date());
                    category.setUserId(getUserId());
                    category.setUserName(getUserName());
                    category.setStatus(Values.STATUS_ACTIVE);
                }

                String rightName = category.getFolderName();

                category.setModifiedDate(new Date());
                category.setLanguageKey(languageKey);
                category.setWeight(GetterUtil.getDouble(weight));
                category.setDescription(description);
                category.setImmune(Values.NOT_IMMUNE);
                category.setFolderName(folderName);
                category.setViewPage(viewPage);
                
                if (categoryType.equals(Values.MENU_TYPE_ITEM)) {
                    category.setParentId(parentId);
//                    category.setFolderName(folderName);
//                    category.setViewPage(viewPage);
                    category.setIcon(icon);
                }

                categoryService.saveOrUpdateCategory(category, rightName, !update);

                ComponentUtil.createSuccessMessageBox(
                        ComponentUtil.getSuccessKey(update));

                winAddMenu.detach();

                Events.sendEvent("onLoadData", winTemp, null);
            }

        }catch (Exception ex) {
            _log.error(ex.getMessage(), ex);

            Messagebox.show(Labels.getLabel(
                    ComponentUtil.getFailKey(update)));
        }
    }

    private boolean _validate(Long categoryType, Long parentId,
            String languageKey, String folderName, String viewPage,
            String icon, String weight, String description) {
//        boolean isMenuItem = categoryType.equals(MENU_ITEM);

//        if(isMenuItem && Validator.isNull(parentId)){
//            bbMenuParent.setErrorMessage(Values.getRequiredSelectMsg(
//                    Labels.getLabel(LanguageKeys.PARENT_MENU)));
//
//            return false;
//        }

        if (Validator.isNull(languageKey)) {
            tbLanguageKey.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.LANGUAGE_KEY)));

            return false;
        }

        if (languageKey.length() > Values.MEDIUM_LENGTH) {
            tbLanguageKey.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.LANGUAGE_KEY),
                    Values.MEDIUM_LENGTH));

            return false;
        }

//        if (isMenuItem) {
        if (Validator.isNull(folderName)) {
            tbFolderName.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.FOLDER_NAME)));

            return false;
        }

        if (folderName.length() > Values.MEDIUM_LENGTH) {
            tbFolderName.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.FOLDER_NAME),
                    Values.MEDIUM_LENGTH));

            return false;
        }

        if (Validator.isNull(viewPage)) {
            tbViewPage.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.VIEW_PAGE)));

            return false;
        }

        if (viewPage.length() > Values.MEDIUM_LENGTH) {
            tbViewPage.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.VIEW_PAGE),
                    Values.MEDIUM_LENGTH));

            return false;
        }
//        }

        if (Validator.isNotNull(icon)
                && icon.length() > Values.LONG_LENGTH) {
            tbIcon.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.ICON),
                    Values.MEDIUM_LENGTH));

            return false;
        }

        if(Validator.isNotNull(weight)
                && !Validator.isNumberic(weight)){
            tbWeight.setErrorMessage(Values.getInputNumberOnlyMsg(
                        Labels.getLabel(LanguageKeys.WEIGHT)));

            return false;
        }

        if (Validator.isNotNull(description)
                && description.length() > Values.GREATE_LONG_LENGTH) {
            tbDescription.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.DESCRIPTION),
                    Values.GREATE_LONG_LENGTH));

            return false;
        }

        return true;
    }

    //get set service
    public CategoryService getCategoryService() {
        if (this.categoryService == null) {
            this.categoryService = (CategoryService)
                    SpringUtil.getBean("categoryService");
            setCategoryService(this.categoryService);
        }

        return this.categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    private transient CategoryService categoryService;
    //get set service
    private static final Long MENU_CATEGORY = 0L;

    private static final Long MENU_ITEM = 1L;

    private static final Logger _log =
            LogManager.getLogger(AddEditMenuController.class);
}
