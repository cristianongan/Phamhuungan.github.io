/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.service;

import java.util.Date;
import java.util.List;

import org.zkoss.util.media.Media;

import com.evotek.qlns.model.Document;
import com.evotek.qlns.model.FileEntry;
import com.evotek.qlns.model.User;

/**
 *
 * @author hungnt81
 */
public interface DocumentService {

	public void delete(Document document);

	public void deleteAll(List<Document> documents);

	public List<String> getDepartment();

	public int getDocumentByIdListCount(List<Long> idList);

	public int getDocumentCountAdv(String documentContent, String documentNumber, Long documentType, String department,
			Date fromDate, Date toDate);

	public int getDocumentCountBasic(String textSearch);

	public List<Document> getDocumentListAdv(String documentContent, String documentNumber, Long documentType,
			String department, Date fromDate, Date toDate, int firstResult, int maxResult, String orderByColumn,
			String orderByType);

	public List<Document> getDocumentListBasic(String textSearch, int firstResult, int maxResult, String orderByColumn,
			String orderByType);

	public List<Document> getDocumentListByIdList(List<Long> idList, int firstResult, int maxResult,
			String orderByColumn, String orderByType);

	public List<FileEntry> getListFileEntrys(Long documentId);

	public boolean isExisted(String documentNumber);

	public boolean isExisted(String documentNumber, String content, Long documentId);

	public List<FileEntry> removeFileEntry(List<FileEntry> deleteFiles);

//    public List<FileEntry> getListFileEntryById(List<Long> listId);

	public List<FileEntry> saveMedia(User user, List<Media> medium, Document document);

	public void saveOrUpdate(Document document);

}
