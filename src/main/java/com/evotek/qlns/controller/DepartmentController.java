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
import org.zkoss.spring.SpringUtil;
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
 * @author My PC
 */
public class DepartmentController extends BasicController<Window>
        implements Serializable{
    private Window winDept;

    private Hlayout winParent;
    
    private Tree tree;

    private Button btnInsert;
    private Button btnDelete;
    private Button btnSave;
    private Button btnReset;
    private Button btnUp;
    private Button btnDown;
    
    private boolean doReload = false;
    
    private Set<DepartmentTreeNode> changedNode = 
            new HashSet<DepartmentTreeNode>();
    
    private Map<Long, List<Department>> deptMaps = 
            new HashMap<Long, List<Department>>();
    
    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp);
        
        this.winDept = comp;
    }

    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);
        
        winParent = (Hlayout) arg.get(Constants.PARENT_WINDOW);
        
        onCreateTree();
        
        winDept.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

            public void onEvent(Event event) throws Exception {
                
                if(doReload){
                    Events.sendEvent("onLoadPage", winParent, null);
                }
            }
        });
    }

    public void onCreateTree() {
        btnDelete.setDisabled(true);
        btnUp.setDisabled(true);
        btnDown.setDisabled(true);
        
        TreeBasicModel treeConfigModel = 
                new TreeBasicModel(_buildDeptTree(), false);

        tree.setModel(treeConfigModel);
        
        tree.setItemRenderer(new TreeDeptRender(
                winDept, departmentService)); 
        
        tree.addEventListener(Events.ON_SELECT, new EventListener<Event>() {

            public void onEvent(Event t) throws Exception {
                boolean disable = tree.getSelectedCount()<0;
                
                btnDelete.setDisabled(disable);
                btnUp.setDisabled(disable);
                btnDown.setDisabled(disable);
            }
        });
        
        tree.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {

            public void onEvent(Event t) throws Exception {
                tree.clearSelection();
                
                btnDelete.setDisabled(true);
                btnUp.setDisabled(true);
                btnDown.setDisabled(true);
            }
        });
        
        btnInsert.addForward(Events.ON_CLICK, winDept, "onAdd", null);
    }
    
    public DepartmentTreeNode _buildDeptTree() {
        //tạo cây menu không có gốc
        DepartmentTreeNode rootNode = new DepartmentTreeNode(null,
                new DepartmentTreeNode[]{});

        rootNode.setOpen(true);

        try {
            //Lấy danh sách các menu category
            List<Department> roots = departmentService.getDepartmentByParentId(null);

            deptMaps.put(null, roots);
            
            for (Department root : roots) {
                addChildToParent(root, rootNode);
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return rootNode;
    }

    public void addChildToParent(Department parent, DepartmentTreeNode treeNode) {
        List<Department> childs
                = departmentService.getDepartmentByParentId(parent.getDeptId());

        if (Validator.isNotNull(childs)) {
            deptMaps.put(parent.getDeptId(), childs);
            //Tạo cây con tu parent
            DepartmentTreeNode rootChilds = new DepartmentTreeNode(parent,
                    new DepartmentTreeNode[]{});

            rootChilds.setOpen(true);

            //Gắn các menu item vào cây con vừa tạo
            for (Department child : childs) {
                addChildToParent(child, rootChilds);
            }

            //gắn cấy menu category vào cây menu
            treeNode.add(rootChilds);
        } else {
            treeNode.add(new DepartmentTreeNode(parent));
        }
    }
    
    public void onClick$btnDelete() {
        final Department dept = this.getSelectedItem();

        if (Validator.isNull(dept)) {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD),
                    Labels.getLabel(LanguageKeys.ERROR), Messagebox.OK,
                    Messagebox.EXCLAMATION, Messagebox.OK);
        } else {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
                    Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE),
                    Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new EventListener() {
                        public void onEvent(Event e) throws Exception {
                            if (Messagebox.ON_OK.equals(e.getName())) {
                                try {
                                    List<Long> deptIds = 
                                            new ArrayList<Long>();
                                    
                                    deptIds.add(dept.getDeptId());
                                    
                                    getDeptGroup(dept.getDeptId(), 
                                            deptIds);
                                    
                                    //xoa
                                    departmentService.delete(deptIds);
                                    //update ordinal
                                    departmentService.updateOrdinal(dept.getParentId(), 
                                            dept.getOrdinal());
                                    
                                    ComponentUtil.createSuccessMessageBox(
                                            LanguageKeys.MESSAGE_DELETE_SUCCESS);
                                    //refresh lai cay
                                    onCreateTree();
                                    
                                    doReload = true;
                                    //refresh lai danh muc tai lieu
//                                    Events.sendEvent("onLoadPage", winParent, null);
                                } catch (Exception ex) {
                                    _log.error(ex.getMessage(), ex);

                                    Messagebox.show(Labels.getLabel(
                                                    LanguageKeys.MESSAGE_DELETE_FAIL));
                                }
                            }
                        }
                    });
        }
    }
    
    public void onClick$btnUp(){
        Treeitem item = tree.getSelectedItem();
        
        if (Validator.isNull(item)) {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD),
                    Labels.getLabel(LanguageKeys.ERROR), Messagebox.OK,
                    Messagebox.EXCLAMATION, Messagebox.OK);
        } else {   
            TreeBasicModel treeModel = (TreeBasicModel) tree.getModel();
            
            DepartmentTreeNode treeNode = (DepartmentTreeNode) item.getValue();
            DepartmentTreeNode parentTreeNode = (DepartmentTreeNode) treeNode.getParent();
            
            List<TreeNode<Department>> childNodes = parentTreeNode.getChildren();
            
            int index = childNodes.indexOf(treeNode);
            
            if(index>0){
                index--;
            }
            
            treeModel.remove(treeNode);
            
            treeModel.insert(parentTreeNode, index, index, 
                    new DepartmentTreeNode[]{treeNode});
            
            treeModel.addToSelection(treeNode);
            
//            this.updateOrdinal(childNodes);
            
            changedNode.add(parentTreeNode);
            
            btnReset.setDisabled(changedNode.isEmpty());
            btnSave.setDisabled(changedNode.isEmpty());
        }
    }
    
    public void onClick$btnDown(){
        Treeitem item = tree.getSelectedItem();
        
        if (Validator.isNull(item)) {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD),
                    Labels.getLabel(LanguageKeys.ERROR), Messagebox.OK,
                    Messagebox.EXCLAMATION, Messagebox.OK);
        } else {
            TreeBasicModel treeModel = (TreeBasicModel) tree.getModel();
            
            DepartmentTreeNode treeNode = (DepartmentTreeNode) item.getValue();
            DepartmentTreeNode parentTreeNode = (DepartmentTreeNode) treeNode.getParent();
            
            List<TreeNode<Department>> childNodes = parentTreeNode.getChildren();
            
            int index = childNodes.indexOf(treeNode);
            
            if(index<parentTreeNode.getChildren().size()-1){
                index++;
            }
            
            treeModel.remove(treeNode);
            
            treeModel.insert(parentTreeNode, index, index, 
                    new DepartmentTreeNode[]{treeNode});
            
            treeModel.addToSelection(treeNode);
            
//            this.updateOrdinal(childNodes);
            
            changedNode.add(parentTreeNode);
            
            btnReset.setDisabled(changedNode.isEmpty());
            btnSave.setDisabled(changedNode.isEmpty());
        }
    }
    
    public void onClick$btnReset(){
        changedNode.clear();
        
        onCreateTree();
    }
    
    public void onClick$btnSave(){
        Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_SAVE_CHANGE),
                Labels.getLabel(LanguageKeys.MESSAGE_INFOR_SAVE_CHANGE),
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new EventListener() {
                    public void onEvent(Event e) throws Exception {
                        if (Messagebox.ON_OK.equals(e.getName())) {
                            try {
                                updateOrdinal();

                                ComponentUtil.createSuccessMessageBox(
                                        LanguageKeys.MESSAGE_UPDATE_SUCCESS);
                                //refresh lai danh muc tai lieu
//                                Events.sendEvent("onLoadPage", winParent, null);
                                //disable button
                                btnSave.setDisabled(true);
                                btnReset.setDisabled(true);
                                
                                doReload = true;
                            } catch (Exception ex) {
                                _log.error(ex.getMessage(), ex);

                                Messagebox.show(Labels.getLabel(
                                                LanguageKeys.MESSAGE_UPDATE_FAIL));
                            }
                        }
                    }
                });
    }
    
    private void updateOrdinal() {
        for (DepartmentTreeNode deptTreeNode : changedNode) {
            List<TreeNode<Department>> childNodes = deptTreeNode.getChildren();
            
            for (TreeNode<Department> treeNode : childNodes) {
                Department dept = treeNode.getData();

                dept.setOrdinal(Long.valueOf(childNodes.indexOf(treeNode)));
            }
            
            departmentService.saveOrUpdate(deptTreeNode.getData());
        }

        changedNode.clear();
    }
    
    private List<Long> getDeptGroup(Long rootId, 
            List<Long> deptIds){
        List<Department> depts = deptMaps.get(rootId);
        
        if(Validator.isNotNull(depts)){
            for(Department dept: depts){
                if(Validator.isNull(dept)){
                    continue;
                }
                
                deptIds.add(dept.getDeptId());
                
                getDeptGroup(dept.getDeptId(), 
                        deptIds);
            }
        }
        
        return deptIds;
    }
    
    public void onAdd(Event event) {
        Department dept = (Department) event.getData();

        Map map = new HashMap();

        map.put(Constants.TITLE, Labels.getLabel(LanguageKeys.ADD));
        map.put(Constants.PARENT_WINDOW, winDept);
        map.put(Constants.SECOND_OBJECT, dept);

        Window win = (Window) Executions.createComponents(ADD_EDIT_PAGE, 
                winDept, map);

        win.doModal();
    }

    public void onEdit(Event event) {
        Department dept = (Department) event.getData();

        Map map = new HashMap();

        map.put(Constants.TITLE, Labels.getLabel(LanguageKeys.TITLE_EDIT_DEPARTMENT));
        map.put(Constants.PARENT_WINDOW, winDept);
        map.put(Constants.OBJECT, dept);

        Window win = (Window) Executions.createComponents(ADD_EDIT_PAGE, 
                winDept, map);
        
        win.doModal();
    }

    private Department getSelectedItem(){
        Treeitem item = tree.getSelectedItem();
        
        return item == null ? null:
                (Department) ((DepartmentTreeNode) item.getValue()).getData();
    }
    
    public void onLoadData(Event event) {
        onCreateTree();

        doReload = true;
    }
    
    public void onClick$btnCancel() {
        winDept.detach();

        if (doReload) {
            Events.sendEvent("onLoadPage", winParent, null);
        }
    }
    
    public DepartmentService getDepartmentService() {
        if (departmentService == null) {
            departmentService = (DepartmentService) SpringUtil.getBean("departmentService");
            
            setDepartmentService(departmentService);
        }
        
        return departmentService;
    }

    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    private transient DepartmentService departmentService;
    
    private static final String ADD_EDIT_PAGE =
            "/html/pages/department/edit.zul";
    
    public static final Logger _log =
            LogManager.getLogger(DepartmentController.class);
}
