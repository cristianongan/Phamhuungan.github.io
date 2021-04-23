/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.A;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.DocumentType;
import com.evotek.qlns.service.DocumentTypeService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author MRHOT
 */
public class AddEditDocumentTypeController extends BasicController<Window>
        implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6515082727563393176L;
	
	private Window winTemp;
    private Window winDocType;
    
    private Textbox tbTypeName;
    private Textbox tbDescription;
    
    //Bandbox documentType
    private Bandbox bbDocumentType;
    
    private Include icDocumentType;
    
    private A btnClearDoc;
    //Bandbox documentType
    
    private DocumentType docType;
    private DocumentType parentDocType;
    private DocumentType oldParentDocType;

    private Map<Long, List<DocumentType>> docTypeMap;
    
    private ServletContext context;
    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp); 
        
        this.winDocType= comp;
        
        context = Sessions.getCurrent().getWebApp().getServletContext();
        
        docTypeMap = documentTypeService.getDocTypeMap(context);
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
            winTemp = (Window) arg.get(Constants.PARENT_WINDOW);

            docType = (DocumentType) arg.get(Constants.OBJECT);
            parentDocType = (DocumentType) arg.get(Constants.SECOND_OBJECT);

            if (Validator.isNotNull(parentDocType)) {
                bbDocumentType.setValue(parentDocType.getTypeName());
                bbDocumentType.setAttribute(Constants.ID,
                        parentDocType.getDocumentTypeId());
                bbDocumentType.setAttribute(Constants.OBJECT,
                        parentDocType);
            }
            
            if(Validator.isNotNull(docType)){
                winDocType.setTitle((String) arg.get(Constants.TITLE));

                oldParentDocType = docType.getParentDocumentType();
                
                this._setEditForm();
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }
    
    private void _setEditForm() throws Exception {
        tbTypeName.setValue(docType.getTypeName());
        tbDescription.setValue(docType.getDescription());
        
        if (Validator.isNotNull(oldParentDocType)) {
            bbDocumentType.setValue(oldParentDocType.getTypeName());
            bbDocumentType.setAttribute(Constants.ID,
                    oldParentDocType.getDocumentTypeId());
            bbDocumentType.setAttribute(Constants.OBJECT,
                        oldParentDocType);
            btnClearDoc.setVisible(true);
        }
    }
    
    public void onClick$btnSave() throws Exception{
        boolean update = true;
        
        try {
            String typeName = GetterUtil.getString(tbTypeName.getValue());
            String description = GetterUtil.getString(tbDescription.getValue());
            parentDocType = (DocumentType) bbDocumentType.getAttribute(Constants.OBJECT);
            
            if (this._validate(typeName, description)) {
                if(Validator.isNull(docType)){
                    update = false;
                    
                    docType = new DocumentType();
                    
                    docType.setUserId(getUserId());
                    docType.setUserName(getUserName());
                    docType.setCreateDate(new Date());
                    docType.setStatus(Values.ENABLE);
                    docType.setOrdinal(getOrdinal());
                }
                
                docType.setTypeName(typeName);
                docType.setDescription(description);
                docType.setModifiedDate(new Date());
                docType.setParentDocumentType(parentDocType);
                
                if(!update){
                    documentTypeService.updateDocTypeMap(context, docType, 
                            parentDocType);
                } else {
                    documentTypeService.updateDocTypeMap(context, docType, 
                            parentDocType, oldParentDocType);
                }
                
                ComponentUtil.createSuccessMessageBox(
                        ComponentUtil.getSuccessKey(update));
                
                winDocType.detach();

                Events.sendEvent("onLoadData", winTemp, null);
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
            
            Messagebox.show(Labels.getLabel(
                    ComponentUtil.getFailKey(update)));
        }
    }

    public boolean _validate(String typeName, String description) {
        if (Validator.isNull(typeName)) {
            tbTypeName.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.DOCUMENT_TYPE_NAME)));
            
            tbTypeName.setFocus(true);
            
            return false;
        }

        if (typeName.length() > Values.LONG_LENGTH) {
            tbTypeName.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.DOCUMENT_TYPE_NAME),
                    Values.LONG_LENGTH));
            
            tbTypeName.setFocus(true);
            
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
    
    private Long getOrdinal(){
        List<DocumentType> docTypes;
        
        if(Validator.isNull(parentDocType)){
            docTypes = docTypeMap.get(null);
        } else {
            docTypes = docTypeMap.get(parentDocType.getDocumentTypeId());
        }
        
        if(Validator.isNull(docTypes)){
            return 0L;
        }
        
        Long ordinal = docTypes.get(docTypes.size()-1).getOrdinal();
        
        return Validator.isNull(ordinal)?docTypes.size():ordinal+1;
    }
    
    public void onClick$btnCancel() {
        winDocType.detach();
    }

    //Bandbox documentType
    public void onClick$btnClearDoc() {
        bbDocumentType.setValue(StringPool.BLANK);
        bbDocumentType.setAttribute(Constants.ID, null);
        
        btnClearDoc.setDisabled(true);
        btnClearDoc.setVisible(false);
    }
    
    public void onOpen$bbDocumentType(){
        if(bbDocumentType.isOpen() 
                && Validator.isNull(icDocumentType.getSrc())) {
            icDocumentType.setAttribute("bandbox", bbDocumentType);
            icDocumentType.setAttribute("btnclear", btnClearDoc);
            
            if(Validator.isNotNull(docType)){
                icDocumentType.setAttribute("exclude", docType);
            }
            
            icDocumentType.setSrc(Constants.TREE_DOCUMENT_TYPE_PAGE);
        }
    }
    //Bandbox documentType
    
    public DocumentTypeService getDocumentTypeService() {
        if (documentTypeService == null) {
            documentTypeService = (DocumentTypeService) SpringUtil.getBean("documentTypeService");
            setDocumentTypeService(documentTypeService);
        }
        return documentTypeService;
    }

    public void setDocumentTypeService(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }
    
    private transient DocumentTypeService documentTypeService;
    
    private static final Logger _log =
            LogManager.getLogger(AddEditDocumentTypeController.class);
}
