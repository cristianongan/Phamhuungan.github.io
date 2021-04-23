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

        role = (Role) arg.get(Constants.EDIT_OBJECT);

        groups = role.getGroups();
        
        searchResult.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
            public void onEvent(Event t) throws Exception {
                btnSave.setVisible(true);
            }
        });
    }

    public void onCreate$treeMenu() throws Exception {
        try {
            treeModel = new TreeBasicModel(_buildCategoryTree());

            treeModel.setMultiple(true);

            treeMenu.setItemRenderer(new TreeitemRenderer<CategoryTreeNode>() {

                public void render(Treeitem item, CategoryTreeNode t, int i) throws Exception {
                    final Category category = (Category) t.getData();

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
            
            searchResult.setItemRenderer(new AssignPermissionRender(groups));
            searchResult.setModel(new ListModelList<Group>());
            searchResult.setMultiple(true);
            
            treeMenu.setModel(treeModel);
            
            treeMenu.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    categorySelected = ((CategoryTreeNode) 
                            treeMenu.getSelectedItem().getValue()).getData();
                    
                    searchResult.setModel(
                            new SimpleModelList<Group>(
                                    mapGroups.get(categorySelected.getCategoryId())));
                    
                    searchResult.invalidate();
                    
                    btnSave.setVisible(false);
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
            List<Category> roots = categoryService.getCategoryByParentId(null);
            

            for(Category root: roots){
                categoryId = root.getCategoryId();
                //Lấy danh sách các menu item ứng với mỗi menu category
                List<Category> childs = categoryService.getCategoryByParentId(
                        categoryId);
                if (!childs.isEmpty()) {
                    //Tạo cây con với gốc là menu category
                    CategoryTreeNode item = new CategoryTreeNode(root,
                            new CategoryTreeNode[]{});

                    item.setOpen(true);

                    //Gắn các menu item vào cây con vừa tạo
                    for (Category child : childs) {
                        item.add(new CategoryTreeNode(child));

                        mapGroups.put(child.getCategoryId(), roleService.getGroupByCategoryId(
                                child.getCategoryId()));
                    }

                    //gắn cấy menu category vào cây menu
                    menu.add(item);
                } else {
                    menu.add(new CategoryTreeNode(root));
                }
                
                mapGroups.put(categoryId, roleService.getGroupByCategoryId(
                        categoryId));
            }
        }catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return menu;
    }

    public void onClick$btnCancel() {
        winPermission.detach();
    }

    public void onClick$btnSave() {
        try{
            List<Group> allGroups = mapGroups.get(categorySelected.getCategoryId());

            Set<Listitem> listitems = searchResult.getSelectedItems();

            for(Listitem item: listitems){
                if(item instanceof Listitem){
                    Group group = (Group) item.getValue();

                    groups.add(group);
                    
                    allGroups.remove(group);
                }
            }

            groups.removeAll(allGroups);
            
            role.setGroups(groups);

            roleService.saveOrUpdateRole(role);

            btnSave.setVisible(false);
            
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
