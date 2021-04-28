/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.service;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.evotek.qlns.model.DocumentType;

/**
 *
 * @author MRHOT
 */
public interface DocumentTypeService {

	public void delete(DocumentType documentType);

	public void delete(DocumentType root, List<Long> docTypeGroupIds, ServletContext context) throws Exception;

	public List<DocumentType> getAllDocumentType();

	public DocumentType getById(Long documentTypeId);

	public List<DocumentType> getDocTypeByParentId(Long parentId);

	public Map<Long, List<DocumentType>> getDocTypeMap(ServletContext context);

	public void saveOrUpdate(DocumentType documentType);

	public void saveOrUpdate(DocumentType documentType, boolean flush);

	public void updateDelDocTypeMap(ServletContext context, DocumentType docType, DocumentType parentDocType,
			List<Long> docTypeGroupIds);

	public void updateDeleteOrdinal(ServletContext context, DocumentType parent, DocumentType remove);

	public void updateDocTypeMap(ServletContext context, DocumentType docType, DocumentType parentDocType);

	public void updateDocTypeMap(ServletContext context, DocumentType docType, DocumentType parentDocType,
			DocumentType oldParentDocType);
}
