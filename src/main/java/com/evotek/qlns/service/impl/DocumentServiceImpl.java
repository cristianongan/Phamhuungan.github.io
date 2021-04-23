/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.util.media.Media;

import com.evotek.qlns.dao.DocumentDAO;
import com.evotek.qlns.dao.FileEntryDAO;
import com.evotek.qlns.dao.FolderEntryDAO;
import com.evotek.qlns.model.Document;
import com.evotek.qlns.model.FileEntry;
import com.evotek.qlns.model.FolderEntry;
import com.evotek.qlns.model.User;
import com.evotek.qlns.service.DocumentService;
import com.evotek.qlns.util.FileUtil;
import com.evotek.qlns.util.StaticUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;

/**
 *
 * @author LinhLH2
 */
public class DocumentServiceImpl implements DocumentService {

    private transient DocumentDAO documentDAO;

    public DocumentDAO getDocumentDAO() {
        return documentDAO;
    }

    public void setDocumentDAO(DocumentDAO documentDAO) {
        this.documentDAO = documentDAO;
    }
    private transient FolderEntryDAO folderEntryDAO;

    public FolderEntryDAO getFolderEntryDAO() {
        return folderEntryDAO;
    }

    public void setFolderEntryDAO(FolderEntryDAO folderEntryDAO) {
        this.folderEntryDAO = folderEntryDAO;
    }
    private transient FileEntryDAO fileEntryDAO;

    public FileEntryDAO getFileEntryDAO() {
        return fileEntryDAO;
    }

    public void setFileEntryDAO(FileEntryDAO fileEntryDAO) {
        this.fileEntryDAO = fileEntryDAO;
    }
    private static final Logger _log =
            LogManager.getLogger(DocumentServiceImpl.class);

    public List<String> getDepartment() {
        List<Document> documents = documentDAO.getAll();
        List<String> strList = new ArrayList<String>();
        for (Document document : documents) {
            if (document.getPromulgationDept() != null && !document.getPromulgationDept().equals("")) {
                strList.add(document.getPromulgationDept().toLowerCase());
            }
        }
        return strList;
    }

    public int getDocumentCountAdv(String documentContent, String documentNumber, Long documentType, String department, Date fromDate, Date toDate) {
        return documentDAO.getDocumentCountAdv(documentContent, documentNumber, documentType, department, fromDate, toDate);
    }

    public List<Document> getDocumentListAdv(String documentContent, String documentNumber, Long documentType, String department, Date fromDate, Date toDate, int firstResult, int maxResult, String orderByColumn, String orderByType) {
        return documentDAO.getDocumentListAdv(documentContent, documentNumber, documentType, department, fromDate, toDate, firstResult, maxResult, orderByColumn, orderByType);
    }

    public int getDocumentCountBasic(String textSearch) {
        return documentDAO.getDocumentCountBasic(textSearch);
    }

    public List<Document> getDocumentListBasic(String textSearch, int firstResult, int maxResult, String orderByColumn, String orderByType) {
        return documentDAO.getDocumentListBasic(textSearch, firstResult, maxResult, orderByColumn, orderByType);
    }

    public void delete(Document document) {
        documentDAO.delete(document);
    }

    public void deleteAll(List<Document> documents) {
        documentDAO.delete(documents);
    }

    public List<FileEntry> saveMedia(User user, List<Media> medium, Document document) {
        List<FileEntry> addFiles = new ArrayList<FileEntry>();

        try {
            String instanceFolder = StaticUtil.FILE_UPLOAD_DIR;
            //getLastest folder
            FolderEntry folderEntry = folderEntryDAO.getLastedFolderEntry();

            //neu ko co folder nao hoac fileCount > 100 thi tao moi folder
            if (Validator.isNull(folderEntry)
                    || folderEntry.getFileCount() >= 100) {
                folderEntry = createFolderEntry(user);
            }

            Long folderId = folderEntry.getFolderId();

            if (Validator.isNull(folderId)) {
                return null;
            }

            String folderPath = FileUtil.getOrCreateFolder(instanceFolder,
                    String.valueOf(folderId));

            if (Validator.isNull(folderPath)) {
                folderEntryDAO.rollback();

                return addFiles;
            }

            for (Media media : medium) {
                FileEntry fileEntry = createFileEntry(user, media, folderId, document);

                Long fileId = fileEntry.getFileId();

                if (Validator.isNull(fileId)) {
                    continue;
                }

                //ghi file
                if (FileUtil.write(media, folderPath,
                        String.valueOf(fileId))) {
                    addFiles.add(fileEntry);

                    folderEntry.setFileCount(folderEntry.getFileCount() + 1);
                } else {
                    fileEntryDAO.delete(fileEntry);
                }
            }

            //update lai folder entry
            folderEntryDAO.saveOrUpdate(folderEntry);
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return addFiles;
    }

    public FolderEntry createFolderEntry(User users) {
        FolderEntry folder = new FolderEntry();

        folder.setUserId(users.getUserId());
        folder.setUserName(users.getUserName());
        folder.setCreateDate(new Date());
        folder.setModifiedDate(new Date());
        folder.setFileCount(Constants.START_COUNT);

        folderEntryDAO.saveOrUpdate(folder);

        return folder;
    }

    public FileEntry createFileEntry(User users, Media media, Long folderId, Document document) {
        FileEntry file = new FileEntry();


        file.setUserId(users.getUserId());
        file.setUserName(users.getUserName());
        file.setCreateDate(new Date());
        file.setModifiedDate(new Date());
        file.setName(media.getName());
        file.setFolderId(folderId);
        file.setDocument(document);

        fileEntryDAO.saveOrUpdate(file);

        return file;
    }

    public List<FileEntry> removeFileEntry(List<FileEntry> deleteFiles) {
        List<FileEntry> delelteFiles = new ArrayList<FileEntry>();

        try {
            String instanceFolder = StaticUtil.FILE_UPLOAD_DIR;

            for (FileEntry fileEntry : deleteFiles) {
                if (FileUtil.remove(instanceFolder,
                        String.valueOf(fileEntry.getFolderId()),
                        String.valueOf(fileEntry.getFileId()))) {
                    fileEntryDAO.delete(fileEntry);

                    delelteFiles.add(fileEntry);
                }
            }

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return delelteFiles;
    }

    public void saveOrUpdate(Document document) {
        documentDAO.saveOrUpdate(document);
    }

    public List<FileEntry> getListFileEntrys(Long documentId) {
        return fileEntryDAO.getFileListByDocumentId(documentId);
    }

//    public List<FileEntry> getListFileEntryById(List<Long> listId) {
//        return fileEntryDAO.getFileEntryByIds(listId);
//    }

    public int getDocumentByIdListCount(List<Long> idList) {
        return documentDAO.getDocumentByIdListCount(idList);
    }

    public List<Document> getDocumentListByIdList(List<Long> idList, 
            int firstResult, int maxResult, String orderByColumn, String orderByType) {
        return  documentDAO.getDocumentListByIdList(idList, firstResult, 
                maxResult, orderByColumn, orderByType);
    }
    
    public boolean isExisted(String documentNumber, String content, 
            Long documentId){
        List<Document> results = new ArrayList<Document>();

        try {
            results = documentDAO.getDocumentByI_N_C(documentId, documentNumber, 
                    content);
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return !results.isEmpty();
    }
    
    public boolean isExisted(String documentNumber){
        List<Document> results = new ArrayList<Document>();

        try {
            results = documentDAO.getDocumentByN(documentNumber);
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return !results.isEmpty();
    }
}
