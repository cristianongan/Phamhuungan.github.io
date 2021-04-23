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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

	private static final Logger _log = LogManager.getLogger(DocumentServiceImpl.class);

	@Autowired
	private DocumentDAO documentDAO;

	@Autowired
	private transient FileEntryDAO fileEntryDAO;

	@Autowired
	private FolderEntryDAO folderEntryDAO;

	public FileEntry createFileEntry(User users, Media media, Long folderId, Document document) {
		FileEntry file = new FileEntry();

		file.setUserId(users.getUserId());
		file.setUserName(users.getUserName());
		file.setCreateDate(new Date());
		file.setModifiedDate(new Date());
		file.setName(media.getName());
		file.setFolderId(folderId);
		file.setDocument(document);

		this.fileEntryDAO.saveOrUpdate(file);

		return file;
	}

	public FolderEntry createFolderEntry(User users) {
		FolderEntry folder = new FolderEntry();

		folder.setUserId(users.getUserId());
		folder.setUserName(users.getUserName());
		folder.setCreateDate(new Date());
		folder.setModifiedDate(new Date());
		folder.setFileCount(Constants.START_COUNT);

		this.folderEntryDAO.saveOrUpdate(folder);

		return folder;
	}

	@Override
	public void delete(Document document) {
		this.documentDAO.delete(document);
	}

	@Override
	public void deleteAll(List<Document> documents) {
		this.documentDAO.delete(documents);
	}

	@Override
	public List<String> getDepartment() {
		List<Document> documents = this.documentDAO.getAll();
		List<String> strList = new ArrayList<String>();
		for (Document document : documents) {
			if (document.getPromulgationDept() != null && !document.getPromulgationDept().equals("")) {
				strList.add(document.getPromulgationDept().toLowerCase());
			}
		}
		return strList;
	}

	@Override
	public int getDocumentByIdListCount(List<Long> idList) {
		return this.documentDAO.getDocumentByIdListCount(idList);
	}

	@Override
	public int getDocumentCountAdv(String documentContent, String documentNumber, Long documentType, String department,
			Date fromDate, Date toDate) {
		return this.documentDAO.getDocumentCountAdv(documentContent, documentNumber, documentType, department, fromDate,
				toDate);
	}

	@Override
	public int getDocumentCountBasic(String textSearch) {
		return this.documentDAO.getDocumentCountBasic(textSearch);
	}

	@Override
	public List<Document> getDocumentListAdv(String documentContent, String documentNumber, Long documentType,
			String department, Date fromDate, Date toDate, int firstResult, int maxResult, String orderByColumn,
			String orderByType) {
		return this.documentDAO.getDocumentListAdv(documentContent, documentNumber, documentType, department, fromDate,
				toDate, firstResult, maxResult, orderByColumn, orderByType);
	}

	@Override
	public List<Document> getDocumentListBasic(String textSearch, int firstResult, int maxResult, String orderByColumn,
			String orderByType) {
		return this.documentDAO.getDocumentListBasic(textSearch, firstResult, maxResult, orderByColumn, orderByType);
	}

	@Override
	public List<Document> getDocumentListByIdList(List<Long> idList, int firstResult, int maxResult,
			String orderByColumn, String orderByType) {
		return this.documentDAO.getDocumentListByIdList(idList, firstResult, maxResult, orderByColumn, orderByType);
	}

	@Override
	public List<FileEntry> getListFileEntrys(Long documentId) {
		return this.fileEntryDAO.getFileListByDocumentId(documentId);
	}

	@Override
	public boolean isExisted(String documentNumber) {
		List<Document> results = new ArrayList<Document>();

		try {
			results = this.documentDAO.getDocumentByN(documentNumber);
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return !results.isEmpty();
	}

//    public List<FileEntry> getListFileEntryById(List<Long> listId) {
//        return fileEntryDAO.getFileEntryByIds(listId);
//    }

	@Override
	public boolean isExisted(String documentNumber, String content, Long documentId) {
		List<Document> results = new ArrayList<Document>();

		try {
			results = this.documentDAO.getDocumentByI_N_C(documentId, documentNumber, content);
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return !results.isEmpty();
	}

	@Override
	public List<FileEntry> removeFileEntry(List<FileEntry> deleteFiles) {
		List<FileEntry> delelteFiles = new ArrayList<FileEntry>();

		try {
			String instanceFolder = StaticUtil.FILE_UPLOAD_DIR;

			for (FileEntry fileEntry : deleteFiles) {
				if (FileUtil.remove(instanceFolder, String.valueOf(fileEntry.getFolderId()),
						String.valueOf(fileEntry.getFileId()))) {
					this.fileEntryDAO.delete(fileEntry);

					delelteFiles.add(fileEntry);
				}
			}

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return delelteFiles;
	}

	@Override
	public List<FileEntry> saveMedia(User user, List<Media> medium, Document document) {
		List<FileEntry> addFiles = new ArrayList<FileEntry>();

		try {
			String instanceFolder = StaticUtil.FILE_UPLOAD_DIR;
			// getLastest folder
			FolderEntry folderEntry = this.folderEntryDAO.getLastedFolderEntry();

			// neu ko co folder nao hoac fileCount > 100 thi tao moi folder
			if (Validator.isNull(folderEntry) || folderEntry.getFileCount() >= 100) {
				folderEntry = createFolderEntry(user);
			}

			Long folderId = folderEntry.getFolderId();

			if (Validator.isNull(folderId)) {
				return null;
			}

			String folderPath = FileUtil.getOrCreateFolder(instanceFolder, String.valueOf(folderId));

			if (Validator.isNull(folderPath)) {
				this.folderEntryDAO.rollback();

				return addFiles;
			}

			for (Media media : medium) {
				FileEntry fileEntry = createFileEntry(user, media, folderId, document);

				Long fileId = fileEntry.getFileId();

				if (Validator.isNull(fileId)) {
					continue;
				}

				// ghi file
				if (FileUtil.write(media, folderPath, String.valueOf(fileId))) {
					addFiles.add(fileEntry);

					folderEntry.setFileCount(folderEntry.getFileCount() + 1);
				} else {
					this.fileEntryDAO.delete(fileEntry);
				}
			}

			// update lai folder entry
			this.folderEntryDAO.saveOrUpdate(folderEntry);
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return addFiles;
	}

	@Override
	public void saveOrUpdate(Document document) {
		this.documentDAO.saveOrUpdate(document);
	}
}
