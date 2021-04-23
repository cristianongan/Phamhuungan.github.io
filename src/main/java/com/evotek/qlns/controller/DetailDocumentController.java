/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Div;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Document;
import com.evotek.qlns.model.FileEntry;
import com.evotek.qlns.service.DocumentService;
import com.evotek.qlns.service.DocumentTypeService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.DateUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.StaticUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;

/**
 *
 * @author PC
 */
public class DetailDocumentController extends BasicController<Component>
        implements Serializable {

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        
        initData();
    }

    public void initData() {
        winParent = (Window) arg.get(Constants.PARENT_WINDOW);
        title = (String) arg.get(Constants.TITLE);
        documentTemp = (Document) arg.get(Constants.OBJECT);

        winDetailDocument.setTitle(title);

        if (Validator.isNotNull(documentTemp)) {
            onLoadForm(documentTemp);
        }
    }

    public void onLoadForm(Document document) {
        lbDocumentNumber.setValue(document.getDocumentNumber() == null ? "" : document.getDocumentNumber());
        lbDocumentType.setValue(document.getTypeName() == null ? "" : document.getTypeName());
        lbDepartment.setValue(document.getPromulgationDept() == null ? "" : document.getPromulgationDept());
        lbDate.setValue(document.getPromulgationDate() == null ? "" : GetterUtil.getDate(document.getPromulgationDate(), DateUtil.SHORT_DATE_PATTERN));
        lbContent.setValue(document.getContent() == null ? "" : document.getContent());

        List<FileEntry> oldFiles = documentService.getListFileEntrys(documentTemp.getDocumentId());

        Component comp = ComponentUtil.createDownloadFileGrid(oldFiles, winDetailDocument);
        divFile.appendChild(comp);
    }

    public void onClick$btnClose() {
        winDetailDocument.detach();
    }

    public void onPreviewFile(Event event) throws FileNotFoundException, IOException {
        FileEntry fileEntry = (FileEntry) event.getData();
        iFrameDocPreview.setVisible(true);
        StringBuilder sb = new StringBuilder();

        sb.append(StaticUtil.SYSTEM_STORE_FILE_DIR);
        sb.append(StringPool.SLASH);
        sb.append(StaticUtil.FILE_UPLOAD_DIR);
        sb.append(StringPool.SLASH);
        sb.append(fileEntry.getFolderId());
        sb.append(StringPool.SLASH);
        sb.append(fileEntry.getFileId());

        File file = new File(sb.toString());

        byte[] buffer = new byte[(int) file.length()];
        FileInputStream fs = new FileInputStream(file);
        fs.read(buffer);
        fs.close();

        ByteArrayInputStream is = new ByteArrayInputStream(buffer);
        AMedia amedia = null;
        if (fileEntry.getName().endsWith(".doc")) {
            amedia = new AMedia(fileEntry.getName(), "doc", "application/msword", is);
        } else if (fileEntry.getName().endsWith(".docx")) {
            amedia = new AMedia(fileEntry.getName(), "docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", is);
        } else if (fileEntry.getName().endsWith(".pdf")) {
            amedia = new AMedia(fileEntry.getName(), "pdf", "application/pdf", is);
        } else if (fileEntry.getName().endsWith(".jpg")) {
            amedia = new AMedia(fileEntry.getName(), "jpg", "image/jpeg", is);
        } else if (fileEntry.getName().endsWith(".jpeg")) {
            amedia = new AMedia(fileEntry.getName(), "jpeg", "image/jpeg", is);
        } else if (fileEntry.getName().endsWith(".xls")) {
            amedia = new AMedia(fileEntry.getName(), "xls", "application/vnd.ms-excel", is);
        } else if (fileEntry.getName().endsWith(".xlsx")) {
            amedia = new AMedia(fileEntry.getName(), "xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", is);
        } else if (fileEntry.getName().endsWith(".zip")) {
            amedia = new AMedia(fileEntry.getName(), "zip", "application/x-zip-compressed", is);
        } else if (fileEntry.getName().endsWith(".rar")) {
            amedia = new AMedia(fileEntry.getName(), "rar", "application/x-rar-compressed", is);
        } else if (fileEntry.getName().endsWith(".png")) {
            amedia = new AMedia(fileEntry.getName(), "png", "image/png", is);
        } else if (fileEntry.getName().endsWith(".ppt")) {
            amedia = new AMedia(fileEntry.getName(), "ppt", "application/vnd.ms-powerpoint", is);
        } else if (fileEntry.getName().endsWith(".pptx")) {
            amedia = new AMedia(fileEntry.getName(), "pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation", is);
        } else if (fileEntry.getName().endsWith(".wav")) {
            amedia = new AMedia(fileEntry.getName(), "wav", "audio/wav", is);
        }
        iFrameDocPreview.setContent(amedia);
    }
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
            LogManager.getLogger(DetailDocumentController.class);
    private String title;
    private Window winParent;
    private Document documentTemp;
    private Window winDetailDocument;
    private Label lbDocumentNumber;
    private Label lbDocumentType;
    private Label lbDate;
    private Label lbDepartment;
    private Label lbContent;
    private Div divFile;
    private Iframe iFrameDocPreview;
}
