/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
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
import com.evotek.qlns.model.DocumentType;
import com.evotek.qlns.model.render.TreeDocumentTypeRender;
import com.evotek.qlns.service.DocumentTypeService;
import com.evotek.qlns.tree.model.TreeBasicModel;
import com.evotek.qlns.tree.node.DocumentTypeTreeNode;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;

/**
 *
 * @author MRHOT
 */
public class DocumentTypeController extends BasicController<Window> {
    private Window windowDocumentType;

    private Hlayout winParent;
    
    private Tree tree;

    private Button btnInsert;
    private Button btnDelete;
    private Button btnSave;
    private Button btnReset;
    private Button btnUp;
    private Button btnDown;
    
    private Map<Long, List<DocumentType>> docTypeMap;
    private Set<DocumentTypeTreeNode> changedTypeNode = 
            new HashSet<DocumentTypeTreeNode>();
    
    private boolean doReload = false;
    
    private ServletContext context;
    
    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp);
        
        this.windowDocumentType = comp;
        
        context = Sessions.getCurrent().getWebApp().getServletContext();
        
        docTypeMap = documentTypeService.getDocTypeMap(context);
    }

    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);
        
        winParent = (Hlayout) arg.get(Constants.PARENT_WINDOW);
        
        onCreateTree();
        
        windowDocumentType.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

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
                new TreeBasicModel(_buildCategoryTree(), false);

        tree.setModel(treeConfigModel);
        
        tree.setItemRenderer(new TreeDocumentTypeRender(
                windowDocumentType, documentTypeService)); 
        
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
        
        btnInsert.addForward(Events.ON_CLICK, windowDocumentType, "onAdd", null);
    }

    private DocumentTypeTreeNode _buildCategoryTree() {
        //tạo cây menu không có gốc
        DocumentTypeTreeNode rootNode = new DocumentTypeTreeNode(null,
                new DocumentTypeTreeNode[]{});

        rootNode.setOpen(true);

        try {
            //Lấy danh sách các menu category
            List<DocumentType> roots = docTypeMap.get(null);

            for (DocumentType root : roots) {

                if (Validator.isNull(root) 
                        || Validator.isNull(root.getDocumentTypeId()) ) {
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
                = docTypeMap.get(parent.getDocumentTypeId());

        if (Validator.isNotNull(childs)) {
            //Tạo cây con tu parent
            DocumentTypeTreeNode rootChilds = new DocumentTypeTreeNode(parent,
                    new DocumentTypeTreeNode[]{});

            rootChilds.setOpen(true);

            //Gắn các menu item vào cây con vừa tạo
            for (DocumentType child : childs) {
                if (Validator.isNull(child)
                        || Validator.isNull(child.getDocumentTypeId())) {
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

    public void onClick$btnDelete() {
        final DocumentType docType = this.getSelectedItem();

        if (Validator.isNull(docType)) {
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
                                    List<Long> docTypeGroupIds = 
                                            new ArrayList<Long>();
                                    
                                    docTypeGroupIds.add(docType.getDocumentTypeId());
                                    
                                    getDocumentTypeGroup(docType.getDocumentTypeId(), 
                                            docTypeGroupIds);
                                    
                                    //xoa
                                    documentTypeService.delete(docType, 
                                            docTypeGroupIds, context);

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
            
            DocumentTypeTreeNode treeNode = (DocumentTypeTreeNode) item.getValue();
            DocumentTypeTreeNode parentTreeNode = (DocumentTypeTreeNode) treeNode.getParent();
            
            List<TreeNode<DocumentType>> childNodes = parentTreeNode.getChildren();
            
            int index = childNodes.indexOf(treeNode);
            
            if(index>0){
                index--;
            }
            
            treeModel.remove(treeNode);
            
            treeModel.insert(parentTreeNode, index, index, 
                    new DocumentTypeTreeNode[]{treeNode});
            
            treeModel.addToSelection(treeNode);
            
//            this.updateOrdinal(childNodes);
            
            changedTypeNode.add(parentTreeNode);
            
            btnReset.setDisabled(changedTypeNode.isEmpty());
            btnSave.setDisabled(changedTypeNode.isEmpty());
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
            
            DocumentTypeTreeNode treeNode = (DocumentTypeTreeNode) item.getValue();
            DocumentTypeTreeNode parentTreeNode = (DocumentTypeTreeNode) treeNode.getParent();
            
            List<TreeNode<DocumentType>> childNodes = parentTreeNode.getChildren();
            
            int index = childNodes.indexOf(treeNode);
            
            if(index<parentTreeNode.getChildren().size()-1){
                index++;
            }
            
            treeModel.remove(treeNode);
            
            treeModel.insert(parentTreeNode, index, index, 
                    new DocumentTypeTreeNode[]{treeNode});
            
            treeModel.addToSelection(treeNode);
            
//            this.updateOrdinal(childNodes);
            
            changedTypeNode.add(parentTreeNode);
            
            btnReset.setDisabled(changedTypeNode.isEmpty());
            btnSave.setDisabled(changedTypeNode.isEmpty());
        }
    }
    
    private void updateOrdinal() {
        for (DocumentTypeTreeNode docTreeNode : changedTypeNode) {
            List<TreeNode<DocumentType>> childNodes = docTreeNode.getChildren();
            
            for (TreeNode<DocumentType> treeNode : childNodes) {
                DocumentType docType = treeNode.getData();

                docType.setOrdinal(Long.valueOf(childNodes.indexOf(treeNode)));
            }
            
            documentTypeService.saveOrUpdate(docTreeNode.getData());
        }

        changedTypeNode.clear();
    }
    
    public void onClick$btnReset(){
        changedTypeNode.clear();
        
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
    
    public void onClick$btnCancel() {
        windowDocumentType.detach();

        if (doReload) {
            Events.sendEvent("onLoadPage", winParent, null);
        }
    }
    
    private DocumentType getSelectedItem(){
        Treeitem item = tree.getSelectedItem();
        
        return item == null ? null:
                (DocumentType) ((DocumentTypeTreeNode) item.getValue()).getData();
    }
    
    private List<Long> getDocumentTypeGroup(Long rootId, 
            List<Long> docTypeGroupIds){
        List<DocumentType> docTypes = docTypeMap.get(rootId);
        
        if(Validator.isNotNull(docTypes)){
            for(DocumentType docType: docTypes){
                if(Validator.isNull(docType)){
                    continue;
                }
                
                docTypeGroupIds.add(docType.getDocumentTypeId());
                
                getDocumentTypeGroup(docType.getDocumentTypeId(), 
                        docTypeGroupIds);
            }
        }
        
        return docTypeGroupIds;
    }
    //

    public void onAdd(Event event) {
        DocumentType documentType = (DocumentType) event.getData();

        Map map = new HashMap();

        map.put(Constants.TITLE, Labels.getLabel(LanguageKeys.ADD));
        map.put(Constants.PARENT_WINDOW, windowDocumentType);
        map.put(Constants.SECOND_OBJECT, documentType);

        Window win = (Window) Executions.createComponents(ADD_EDIT_PAGE, 
                windowDocumentType, map);

        win.doModal();
    }

    public void onEdit(Event event) {
        DocumentType documentType = (DocumentType) event.getData();

        Map map = new HashMap();

        map.put(Constants.TITLE, Labels.getLabel(LanguageKeys.TITLE_EDIT_DOCUMENT_TYPE));
        map.put(Constants.PARENT_WINDOW, windowDocumentType);
        map.put(Constants.OBJECT, documentType);

        Window win = (Window) Executions.createComponents(ADD_EDIT_PAGE, 
                windowDocumentType, map);
        
        win.doModal();
    }

    public void onLoadData(Event event) {
        onCreateTree();

        doReload = true;
        //refresh lai danh muc tai lieu
//        Events.sendEvent("onLoadPage", winParent, null);
    }
    
    private transient DocumentTypeService documentTypeService;

    public DocumentTypeService getDocumentTypeService() {
        if (documentTypeService == null) {
            documentTypeService = (DocumentTypeService)
                    SpringUtil.getBean("documentTypeService");
            setDocumentTypeService(documentTypeService);
        }
        return documentTypeService;
    }

    public void setDocumentTypeService(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }
    
    private static final String ADD_EDIT_PAGE =
            "/html/pages/document_type/edit.zul";

    public static final Logger _log =
            LogManager.getLogger(DocumentTypeController.class);
}
