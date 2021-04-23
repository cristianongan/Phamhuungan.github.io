/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zul.A;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Tree;

import com.evotek.qlns.model.Department;
import com.evotek.qlns.model.render.TreeDeptSearchRender;
import com.evotek.qlns.service.DepartmentService;
import com.evotek.qlns.tree.model.TreeBasicModel;
import com.evotek.qlns.tree.node.DepartmentTreeNode;
import com.evotek.qlns.util.Validator;

/**
 *
 * @author My PC
 */
public class TreeDeptBandPopupController extends BasicController<Tree>
        implements Serializable{
    private Tree treeDocType;

    private A btnClear;
    private Bandbox bbDepartment;
    private Department exclude;

    @Override
    public void doBeforeComposeChildren(Tree comp) throws Exception {
        super.doBeforeComposeChildren(comp); 
    
        this.treeDocType = comp;
    }

    @Override
    public void doAfterCompose(Tree comp) throws Exception {
        super.doAfterCompose(comp);
        
        this.init();
    }
    
    public void init(){
        Include include = (Include) treeDocType.getParent();
        
        bbDepartment = (Bandbox) include.getAttribute(BANDBOX);
        btnClear = (A) include.getAttribute(BTN_CLEAR);
        exclude = (Department) include.getAttribute(EXCLUDE);
        
        this.onCreate();
    }
    
    public void onCreate() {
        TreeBasicModel treeConfigModel = 
                new TreeBasicModel(_buildDeptTree(), false);

        treeDocType.setModel(treeConfigModel);
        
        treeDocType.setItemRenderer(new TreeDeptSearchRender(
                bbDepartment, btnClear));
    }

    public DepartmentTreeNode _buildDeptTree() {
        //tạo cây menu không có gốc
        DepartmentTreeNode rootNode = new DepartmentTreeNode(null,
                new DepartmentTreeNode[]{});

        rootNode.setOpen(true);

        try {
            //Lấy danh sách các menu category
            List<Department> roots = departmentService.getDepartmentByParentId(null);

            for (Department root : roots) {

                if (Validator.isNull(root) 
                        || Validator.isNull(root.getDeptId()) 
                        || root.equals(exclude)) {
                    continue;
                }
                
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
            //Tạo cây con tu parent
            DepartmentTreeNode rootChilds = new DepartmentTreeNode(parent,
                    new DepartmentTreeNode[]{});

            rootChilds.setOpen(true);

            //Gắn các menu item vào cây con vừa tạo
            for (Department child : childs) {
                if (Validator.isNull(child)
                        || Validator.isNull(child.getDeptId())
                        || child.equals(exclude)) {
                    continue;
                }
                
                addChildToParent(child, rootChilds);
            }

            //gắn cấy menu category vào cây menu
            treeNode.add(rootChilds);
        } else {
            treeNode.add(new DepartmentTreeNode(parent));
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
    
    private static final String BANDBOX = "bandbox";
    
    private static final String BTN_CLEAR = "btnclear";
    
    private static final String EXCLUDE = "exclude";
    
    private static final Logger _log =
            LogManager.getLogger(TreeDeptBandPopupController.class);
    
}
