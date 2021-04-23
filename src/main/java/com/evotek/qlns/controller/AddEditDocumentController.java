/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Document;
import com.evotek.qlns.model.FileEntry;
import com.evotek.qlns.model.User;
import com.evotek.qlns.service.DocumentService;
import com.evotek.qlns.service.DocumentTypeService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;
import com.evotek.qlns.util.key.ZkKeys;

/**
 *
 * @author PC
 */
public class AddEditDocumentController extends BasicController<Window>
        implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5009409376172819411L;

	private Hlayout winParent;
    
    private Window winAddEditDocument;
    
    private Textbox txtDocumentNumber;
    private Textbox txtContent;
    
    //Bandbox documentType
    private Bandbox bbDocumentType;
    
    private Include icDocumentType;
    
    private A btnClearDoc;
    //Bandbox documentType
    
    private Datebox dbDate;
    
    private Combobox cbDepartment;
    
    private Div divUpload;
    
    private Document document;

    private ListModel model;
    private Integer index;
    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp); //To change body of generated methods, choose Tools | Templates.
    
        this.winAddEditDocument = comp;
    }
    
    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        winParent = (Hlayout) arg.get(Constants.PARENT_WINDOW);
        
        document = (Document) arg.get(Constants.OBJECT);
        
        model = (ListModel) arg.get(Constants.MODEL);

        index = (Integer) arg.get(Constants.INDEX);
        
        initData();
    }

    public void initData() {
        if(Validator.isNotNull(document)) {
            winAddEditDocument.setTitle((String) arg.get(Constants.TITLE));
            
            onLoadForm(document);
        } else {
            txtDocumentNumber.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

                public void onEvent(Event event) throws Exception {
                    if(documentService.isExisted(GetterUtil.getString(
                            txtDocumentNumber.getValue()))){
                        Clients.showNotification(
                                Labels.getLabel(LanguageKeys.MESSAGE_CHECK_EXISTED_DATA_BEFORE_SAVE, 
                                        new Object[]{Labels.getLabel(LanguageKeys.DOCUMENT_NUMBER)}),
                                ZkKeys.WARNING, txtDocumentNumber, ZkKeys.END_CENTER,
                                0, true);
                    }
                }
            });
        }
        
        //create File
        this.onCreateFile();
    }

    public void onLoadForm(Document document) {
        txtDocumentNumber.setValue(document.getDocumentNumber());
        
        if (Validator.isNotNull(document.getDocumentTypeId())) {
            bbDocumentType.setValue(document.getTypeName());
            bbDocumentType.setAttribute(Constants.ID, document.getDocumentTypeId());
            btnClearDoc.setVisible(true);
        }
        
        cbDepartment.setValue(document.getPromulgationDept());
        dbDate.setValue(document.getPromulgationDate());
        txtContent.setValue(document.getContent());
    }

    /**
     * Hàm tạo control upload file
     */
    public void onCreateFile() {
        if (divUpload.getChildren().isEmpty()) {
            List<FileEntry> oldFiles = new ArrayList<FileEntry>();

            Component comp = null;

            if (Validator.isNotNull(document)) {
                oldFiles = documentService.getListFileEntrys(document.getDocumentId());
            }

            String config = "true,multiple=true";

            comp = ComponentUtil.createAttachment(config, oldFiles);

            comp.setAttribute(Constants.ID, oldFiles);

            divUpload.appendChild(comp);
        }
    }

    public void onCreate$cbDepartment() {
        ListModel dictModel = new SimpleListModel(documentService.getDepartment());
        cbDepartment.setModel(dictModel);
    }

    public boolean _validate(String documentNumber, Long documentTypeId, 
            String content) {
        Long documentId = Validator.isNull(document) ? null : document.getDocumentId();
        
        if (Validator.isNull(documentNumber)) {
            txtDocumentNumber.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.DOCUMENT_NUMBER)));
            
            txtDocumentNumber.setFocus(true);
            
            return false;
        }

        if (documentNumber.length() > Values.LONG_LENGTH) {
            txtDocumentNumber.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.DOCUMENT_NUMBER),
                    Values.LONG_LENGTH));
            
            txtDocumentNumber.setFocus(true);
            
            return false;
        }
        
        if (Validator.isNull(documentTypeId)) {
            bbDocumentType.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.DOCUMENT_TYPE)));
            
            bbDocumentType.setFocus(true);
            
            return false;
        }

        if(Validator.isNull(content)){
            txtContent.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.CONTENT)));
            
            txtContent.setFocus(true);
            
            return false;
        }

        if (content.length() > Values.GREATE_LONG_LENGTH) {
            txtContent.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.CONTENT),
                    Values.GREATE_LONG_LENGTH));
            
            txtContent.setFocus(true);
            
            return false;
        }
        
        if(documentService.isExisted(documentNumber, content, documentId)){
            txtDocumentNumber.setErrorMessage(Values.getDuplicateMsg(
                    Labels.getLabel(LanguageKeys.DOCUMENT)));
            
            txtDocumentNumber.setFocus(true);
            
            return false;
        }
        
        return true;
    }

    /**
     * Hàm save file upload
     * @param ticket
     * @throws Exception
     */

    public List<FileEntry> updateFileEntry(Document document) {
        User users = getUser();

        List<FileEntry> oldFiles = (List<FileEntry>) divUpload.getFirstChild().
                getAttribute(Constants.ID);

        //Lay list cac file xoa di trong list file cu
        List<FileEntry> deleteFiles = (List<FileEntry>) divUpload.getFirstChild().
                getAttribute(Constants.OBJECT);

        List<FileEntry> deletedFiles = new ArrayList<FileEntry>();
        List<FileEntry> addedFiles = new ArrayList<FileEntry>();

        //xoa file trong truong hop sua doi tuong
        if (deleteFiles != null
                && !deleteFiles.isEmpty()) {
            deletedFiles = documentService.removeFileEntry(deleteFiles);

            oldFiles.removeAll(deletedFiles);
        }

        List<Media> medium = (List<Media>) divUpload.getFirstChild().
                getAttribute(Constants.DATA);

        if (medium != null && !medium.isEmpty()) {
            addedFiles = documentService.saveMedia(users,
                    medium, document);

            oldFiles.addAll(addedFiles);
        }
        
        return oldFiles;
    }

    public void onClick$btnSave() {
        boolean update = true;
        
        try {
            String documentNumber = 
                    GetterUtil.getString(txtDocumentNumber.getValue());
            Long documentTypeId = ComponentUtil.getBandboxValue(bbDocumentType);
            Date publishDate = dbDate.getValue();
            String publisherDept = GetterUtil.getString(cbDepartment.getValue());
            String content = GetterUtil.getString(txtContent.getValue());
            
            if (_validate(documentNumber, documentTypeId, content)) {
                if(Validator.isNull(document)){
                    update = false;
                    
                    document = new Document();
                    
                    document.setUserId(getUserId());
                    document.setUserName(getUserName());
                    document.setCreateDate(new Date());
                }
                
                document.setDocumentNumber(documentNumber);
                document.setDocumentTypeId(documentTypeId);
                document.setModifiedDate(new Date());
                document.setPromulgationDate(publishDate);
                document.setPromulgationDept(publisherDept);
                document.setContent(content);

                documentService.saveOrUpdate(document);

                updateFileEntry(document);
                
                ComponentUtil.createSuccessMessageBox(
                        ComponentUtil.getSuccessKey(update));
                
                winAddEditDocument.detach();
                
                Events.sendEvent("onLoadDataCRUD", winParent, null);
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage());
            
            Messagebox.show(Labels.getLabel(
                    ComponentUtil.getFailKey(update)));
        }

    }

    public void onClick$btnPrevious() throws Exception {
        if (Validator.isNotNull(document)) {
            if (index == 0) {
                index = (model.getSize() - 1);
            } else {
                index--;
            }

            document = (Document) model.getElementAt(index);
            
            divUpload.getChildren().clear();
            
            initData();
            
            onCreate$cbDepartment();
        }
    }
    
    public void onClick$btnNext() throws Exception {
        if (Validator.isNotNull(document)) {
            if (index == (model.getSize() - 1)) {
                index = 0;
            } else {
                index++;
            }

            document = (Document) model.getElementAt(index);
            
            divUpload.getChildren().clear();
            
            initData();
            
            onCreate$cbDepartment();
        }
    }
    
    public void onClick$btnCancel() {
        winAddEditDocument.detach();
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
            
            icDocumentType.setSrc(Constants.TREE_DOCUMENT_TYPE_PAGE);
        }
    }
    //Bandbox documentType
    
    private transient DocumentTypeService documentTypeService;
    private transient DocumentService documentService;

    public DocumentService getDocumentService() {
        if (documentService == null) {
            documentService = (DocumentService) SpringUtil.getBean("documentService");
            setDocumentService(documentService);
        }
        return documentService;
    }

    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

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
    
    private static final Logger _log =
            LogManager.getLogger(AddEditDocumentController.class);
}
