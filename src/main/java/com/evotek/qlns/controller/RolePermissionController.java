/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Category;
import com.evotek.qlns.model.Group;
import com.evotek.qlns.model.Role;
import com.evotek.qlns.model.list.SimpleModelList;
import com.evotek.qlns.model.render.AssignPermissionRender;
import com.evotek.qlns.service.CategoryService;
import com.evotek.qlns.service.RoleService;
import com.evotek.qlns.tree.model.TreeBasicModel;
import com.evotek.qlns.tree.node.CategoryTreeNode;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;

/**
 *
 * @author manhnn1
 */
public class RolePermissionController extends BasicController<Window> {

    private Window winPermission;

    private Button btnSave;
    
    private Role role;
    
    private Set<Group> groups;
    
    private Tree treeMenu;
    private TreeBasicModel treeModel;
    
    private Listbox searchResult;
    private Category categorySelected;
    
    private Map<Long, List<Group>> mapGroups = new HashMap<Long, List<Group>>();

    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp);

        this.winPermission = comp;
    }

    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        //init data
        this.initData();
    }

    public void initData() throws Exception {
//        winTemp = (Window) arg.get(Constants.PARENT_WINDOW);

        this.role = (Role) this.arg.get(Constants.EDIT_OBJECT);

        this.groups = this.role.getGroups();
        
        this.searchResult.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
            @Override
			public void onEvent(Event t) throws Exception {
                RolePermissionController.this.btnSave.setVisible(true);
            }
        });
    }

    public void onCreate$treeMenu() throws Exception {
        try {
            this.treeModel = new TreeBasicModel(_buildCategoryTree());

            this.treeModel.setMultiple(true);

            this.treeMenu.setItemRenderer(new TreeitemRenderer<CategoryTreeNode>() {

                @Override
				public void render(Treeitem item, CategoryTreeNode t, int i) throws Exception {
                    final Category category = t.getData();

                    //tree cell
                    Treerow dataRow = new Treerow();

                    dataRow.setParent(item);

                    item.setValue(t);
                    item.setOpen(t.isOpen());
                    item.setAttribute("data", category);

                    dataRow.appendChild(ComponentUtil.createTreeCell(
                            Labels.getLabel(category.getLanguageKey())));//Ten menu
                }
            });
            
            this.searchResult.setItemRenderer(new AssignPermissionRender(this.groups));
            this.searchResult.setModel(new ListModelList<Group>());
            this.searchResult.setMultiple(true);
            
            this.treeMenu.setModel(this.treeModel);
            
            this.treeMenu.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
                @Override
				public void onEvent(Event event) throws Exception {
                    RolePermissionController.this.categorySelected = ((CategoryTreeNode) 
                            RolePermissionController.this.treeMenu.getSelectedItem().getValue()).getData();
                    
                    RolePermissionController.this.searchResult.setModel(
                            new SimpleModelList<Group>(
                                    RolePermissionController.this.mapGroups.get(RolePermissionController.this.categorySelected.getCategoryId())));
                    
                    RolePermissionController.this.searchResult.invalidate();
                    
                    RolePermissionController.this.btnSave.setVisible(false);
                }
            });
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
            Long categoryId = null;
            //Lấy danh sách các menu category
            List<Category> roots = this.categoryService.getCategoryByParentId(null);
            

            for(Category root: roots){
                categoryId = root.getCategoryId();
                //Lấy danh sách các menu item ứng với mỗi menu category
                List<Category> childs = this.categoryService.getCategoryByParentId(
                        categoryId);
                if (!childs.isEmpty()) {
                    //Tạo cây con với gốc là menu category
                    CategoryTreeNode item = new CategoryTreeNode(root,
                            new CategoryTreeNode[]{});

                    item.setOpen(true);

                    //Gắn các menu item vào cây con vừa tạo
                    for (Category child : childs) {
                        item.add(new CategoryTreeNode(child));

                        this.mapGroups.put(child.getCategoryId(), this.roleService.getGroupByCategoryId(
                                child.getCategoryId()));
                    }

                    //gắn cấy menu category vào cây menu
                    menu.add(item);
                } else {
                    menu.add(new CategoryTreeNode(root));
                }
                
                this.mapGroups.put(categoryId, this.roleService.getGroupByCategoryId(
                        categoryId));
            }
        }catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return menu;
    }

    public void onClick$btnCancel() {
        this.winPermission.detach();
    }

    public void onClick$btnSave() {
        try{
            List<Group> allGroups = this.mapGroups.get(this.categorySelected.getCategoryId());

            Set<Listitem> listitems = this.searchResult.getSelectedItems();

            for(Listitem item: listitems){
                if(item instanceof Listitem){
                    Group group = (Group) item.getValue();

                    this.groups.add(group);
                    
                    allGroups.remove(group);
                }
            }

            this.groups.removeAll(allGroups);
            
            this.role.setGroups(this.groups);

            this.roleService.saveOrUpdateRole(this.role);

            this.btnSave.setVisible(false);
            
            ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_UPDATE_SUCCESS);
        }catch (Exception ex) {
            _log.error(ex.getMessage(), ex);

            Messagebox.show(Labels.getLabel(
                    LanguageKeys.MESSAGE_UPDATE_FAIL));
        }
    }

    //service
    public RoleService getRoleService() {
        if (this.roleService == null) {
            this.roleService = (RoleService)
                    SpringUtil.getBean("roleService");
            
            setRoleService(this.roleService);
        }

        return this.roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

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
    private transient RoleService roleService;

    private static final Logger _log =
            LogManager.getLogger(RolePermissionController.class);
}
