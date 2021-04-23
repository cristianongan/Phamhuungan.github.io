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
import org.zkoss.spring.SpringUtil;
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
 * @author hungnt81
 * LinhLH2 fixed
 */
public class MenuController extends BasicController<Div>
        implements Serializable {

    private Tree treeMenu;
    private Div winMenu;
    private Include parent;

    private TreeBasicModel treeCategoryModelUtil;

    @Override
    public void doBeforeComposeChildren(Div comp) throws Exception {
        super.doBeforeComposeChildren(comp);

        this.winMenu = comp;
    }

    @Override
    public void doAfterCompose(Div comp) throws Exception {
        super.doAfterCompose(comp);
        
        parent = (Include) winMenu.getParent();
        
        this.onCreateTree();
    }

    public void onClick$adminPage(){
        parent.setSrc("/html/pages/admin/default.zul");
    }
    /**
     * Hàm tạo cây menu
     * @throws Exception
     */
    public void onCreateTree() throws Exception {
        try {
            treeCategoryModelUtil = new TreeBasicModel(_buildCategoryTree());

            treeCategoryModelUtil.setMultiple(true);

            treeMenu.setItemRenderer(new TreeCategoryRender(winMenu));
            treeMenu.setModel(treeCategoryModelUtil);
            treeMenu.setCheckmark(true);
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

    }

    /**
     * Hàm tạo cây menu
     * @return
     * @throws Exception
     */
    private CategoryTreeNode _buildCategoryTree()
            throws Exception{
        //tạo cây menu không có gốc
        CategoryTreeNode menu = new CategoryTreeNode(null,
                new CategoryTreeNode[]{});

        menu.setOpen(true);

        try{

            //Lấy danh sách các menu category
            List<Category> roots = categoryService.getCategoryByParentId(null);
            

            for(Category root: roots){

                //Lấy danh sách các menu item ứng với mỗi menu category
                List<Category> childs = categoryService.getCategoryByParentId(
                        root.getCategoryId());
                if (!childs.isEmpty()) {
                    //Tạo cây con với gốc là menu category
                    CategoryTreeNode item = new CategoryTreeNode(root,
                            new CategoryTreeNode[]{});

                    item.setOpen(true);

                    //Gắn các menu item vào cây con vừa tạo
                    for (Category child : childs) {
                        item.add(new CategoryTreeNode(child));
                    }

                    //gắn cấy menu category vào cây menu
                    menu.add(item);
                } else {
                    menu.add(new CategoryTreeNode(root));
                }
            }
        }catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return menu;
    }

    /**
     * Hàm xử lý sự kiện onClick khi click vào nút "Thêm" - có id = btnAdd
     */
    public void onClick$btnAdd() {
        //Tạo map để set các tham số truyền vào khi mở popup cập nhật/thêm mới
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put(Constants.PARENT_WINDOW, winMenu);
        parameters.put(Constants.TITLE, Labels.getLabel(LanguageKeys.ADD));
        parameters.put(Constants.ID, 0L);

        Window win = (Window) Executions.createComponents(
                EDIT_PAGE , winMenu, parameters);

        win.doModal();
    }

    /**
     * Hàm thực hiện reload lại cây menu
     * @param event
     * @throws Exception
     */
    public void onLoadData(Event event) throws Exception {
        this.onCreateTree();
    }

    /**
     * Hàm lấy danh sách id các menu item được chọn
     * @return
     */
    public List<Long> getSelectedItem() {
        List<Long> categoryIds = new ArrayList<Long>();

        if (treeMenu.getItems() != null) {
            for (Object item : treeMenu.getItems()) {
                Treeitem treeitem = (Treeitem) item;

                if (treeitem.isSelected()) {
                    Category categoryTemp =
                            (Category) treeitem.getAttribute("data");

                    categoryIds.add(categoryTemp.getCategoryId());
                }
            }
        } else {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD));
        }

        return categoryIds;
    }

    //event method
    /**
     * Hàm xử lý sự kiện khi click vào nút "Khóa". Hàm sẽ chuyển
     * menu item về trạng thái khóa(không hoạt động), status=0
     * @param event
     * @throws Exception
     */
    public void onLockCategory(Event event) throws Exception {
        final Category category = (Category) event.getData();

        Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_LOCK),
                Labels.getLabel(LanguageKeys.MESSAGE_INFOR_LOCK),
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new EventListener() {

                    public void onEvent(Event e) throws Exception {
                        if (Messagebox.ON_OK.equals(e.getName())) {
                            try {
                                categoryService.lockCategory(category);

                                ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_LOCK_ITEM_SUCCESS);

                                onCreateTree();
                            } catch (Exception ex) {
                                _log.error(ex.getMessage(), ex);

                                Messagebox.show(Labels.getLabel(
                                        LanguageKeys.MESSAGE_LOCK_ITEM_FAIL));
                            }
                        }
                    }
                });
    }

    /**
     * Hàm xử lý sự kiện khi click vào nút "Mở khóa". Hàm sẽ chuyển
     * menu item về trạng thái mở khóa (hoạt động), status=1
     * @param event
     * @throws Exception
     */
    public void onUnlockCategory(Event event) throws Exception {
        final Category category = (Category) event.getData();

        Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_UNLOCK),
                Labels.getLabel(LanguageKeys.MESSAGE_INFOR_UNLOCK),
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new EventListener() {

                    public void onEvent(Event e) throws Exception {
                        if (Messagebox.ON_OK.equals(e.getName())) {
                            try {
                                categoryService.unlockCategory(category);

                                ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_UNLOCK_ITEM_SUCCESS);

                                onCreateTree();
                            } catch (Exception ex) {
                                _log.error(ex.getMessage(), ex);

                                Messagebox.show(Labels.getLabel(
                                        LanguageKeys.MESSAGE_UNLOCK_ITEM_FAIL));
                            }
                        }
                    }
                });
    }

    /**
     * Hàm xóa category khi click chọn nút "Xóa". Category chỉ có thể xóa sau
     * khi Khóa (status=0)
     * @param event
     * @throws Exception
     */
    public void onDeleteCategory(Event event) throws Exception {
        final Category category = (Category) event.getData();

        Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
                Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE),
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new EventListener() {

                    public void onEvent(Event e) throws Exception {
                        if (Messagebox.ON_OK.equals(e.getName())) {
                            try {
                                categoryService.deleteCategory(category);

                                ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_DELETE_SUCCESS);

                                onCreateTree();
                            } catch (Exception ex) {
                                _log.error(ex.getMessage(), ex);

                                Messagebox.show(Labels.getLabel(
                                        LanguageKeys.MESSAGE_DELETE_FAIL));
                            }
                        }
                    }
                });
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

    private static final String EDIT_PAGE =
            "/html/pages/manager_menu/edit.zul";

    private static final Logger _log =
            LogManager.getLogger(MenuController.class);
}
