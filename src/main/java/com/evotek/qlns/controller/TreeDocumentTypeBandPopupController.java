/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.A;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Tree;

import com.evotek.qlns.model.DocumentType;
import com.evotek.qlns.model.render.TreeDocumentTypeSearchRender;
import com.evotek.qlns.service.DocumentTypeService;
import com.evotek.qlns.tree.model.TreeBasicModel;
import com.evotek.qlns.tree.node.DocumentTypeTreeNode;
import com.evotek.qlns.util.Validator;

/**
 *
 * @author linhlh2
 */
public class TreeDocumentTypeBandPopupController extends BasicController<Tree>
        implements Serializable{
    private Tree treeDocType;

    private A btnClear;
    private Bandbox bbDocumentType;
    private DocumentType exclude;
    
    private Map<Long, List<DocumentType>> docTypeMap;
    @Override
    public void doBeforeComposeChildren(Tree comp) throws Exception {
        super.doBeforeComposeChildren(comp); 
    
        this.treeDocType = comp;
    }

    @Override
    public void doAfterCompose(Tree comp) throws Exception {
        super.doAfterCompose(comp);
        
        ServletContext context = Sessions.getCurrent().getWebApp().getServletContext();
        
        this.docTypeMap = this.documentTypeService.getDocTypeMap(context);
        
        this.init();
    }
    
    public void init(){
        Include include = (Include) this.treeDocType.getParent();
        
        this.bbDocumentType = (Bandbox) include.getAttribute(BANDBOX);
        this.btnClear = (A) include.getAttribute(BTN_CLEAR);
        this.exclude = (DocumentType) include.getAttribute(EXCLUDE);
        
        this.onCreate();
    }
    
    public void onCreate() {
        TreeBasicModel treeConfigModel = 
                new TreeBasicModel(_buildCategoryTree(), false);

        this.treeDocType.setModel(treeConfigModel);
        
        this.treeDocType.setItemRenderer(new TreeDocumentTypeSearchRender(
                this.bbDocumentType, this.btnClear));
    }

    public DocumentTypeTreeNode _buildCategoryTree() {
        //tạo cây menu không có gốc
        DocumentTypeTreeNode rootNode = new DocumentTypeTreeNode(null,
                new DocumentTypeTreeNode[]{});

        rootNode.setOpen(true);

        try {
            //Lấy danh sách các menu category
            List<DocumentType> roots = this.docTypeMap.get(null);

            for (DocumentType root : roots) {

                if (Validator.isNull(root) 
                        || Validator.isNull(root.getDocumentTypeId()) 
                        || root.equals(this.exclude)) {
                    continue;
                }
                
                addChildToParent(root, rootNode);
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return rootNode;
    }

    public void addChildToParent(DocumentType parent, DocumentTypeTreeNode treeNode) {
        List<DocumentType> childs
                = this.docTypeMap.get(parent.getDocumentTypeId());

        if (Validator.isNotNull(childs)) {
            //Tạo cây con tu parent
            DocumentTypeTreeNode rootChilds = new DocumentTypeTreeNode(parent,
                    new DocumentTypeTreeNode[]{});

            rootChilds.setOpen(true);

            //Gắn các menu item vào cây con vừa tạo
            for (DocumentType child : childs) {
                if (Validator.isNull(child)
                        || Validator.isNull(child.getDocumentTypeId())
                        || child.equals(this.exclude)) {
                    continue;
                }
                
                addChildToParent(child, rootChilds);
            }

            //gắn cấy menu category vào cây menu
            treeNode.add(rootChilds);
        } else {
            treeNode.add(new DocumentTypeTreeNode(parent));
        }
    }

    public DocumentTypeService getDocumentTypeService() {
        if (this.documentTypeService == null) {
            this.documentTypeService = (DocumentTypeService)
                    SpringUtil.getBean("documentTypeService");
            setDocumentTypeService(this.documentTypeService);
        }
        return this.documentTypeService;
    }

    public void setDocumentTypeService(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }
    
    private transient DocumentTypeService documentTypeService;
    
    private static final String BANDBOX = "bandbox";
    
    private static final String BTN_CLEAR = "btnclear";
    
    private static final String EXCLUDE = "exclude";
    
    private static final Logger _log =
            LogManager.getLogger(TreeDocumentTypeBandPopupController.class);
}
