/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.service;

import java.util.List;
import java.util.Map;

import com.evotek.qlns.model.DocumentType;

/**
 *
 * @author LinhLH
 */
public interface DocumentTypeService {

	public void delete(DocumentType documentType);

	public void delete(DocumentType root, List<Long> docTypeGroupIds) throws Exception;

	public List<DocumentType> getAllDocumentType();

	public DocumentType getById(Long documentTypeId);

	public List<DocumentType> getDocTypeByParentId(Long parentId);

	public Map<Long, List<DocumentType>> getDocTypeMap();

	public void saveOrUpdate(DocumentType documentType);

	public void saveOrUpdate(DocumentType documentType, boolean flush);

	public void updateDeleteOrdinal(DocumentType parent, DocumentType remove);

	public void updateDocTypeMap(DocumentType docType, DocumentType parentDocType);

	public void updateDocTypeMap(DocumentType docType, DocumentType parentDocType, DocumentType oldParentDocType);
}
