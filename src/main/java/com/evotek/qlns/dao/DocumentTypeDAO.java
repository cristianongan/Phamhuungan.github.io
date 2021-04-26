/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.dao;

import java.util.Collection;
import java.util.List;

import com.evotek.qlns.model.DocumentType;

/**
 *
 * @author MRHOT
 */
public interface DocumentTypeDAO {    
    public void saveOrUpdate(DocumentType documentType);
    
    public void saveOrUpdate(DocumentType documentType, boolean flush);

    public void delete(DocumentType entitie);
    
    public void delete(Collection<DocumentType> entities) throws Exception;

    public List<DocumentType> getDocTypeByParentId(Long parentId);

    public void deleteDocType(DocumentType documentType);

    public DocumentType getById(Long documentTypeId);
    
    public List<DocumentType> getAllDocumentType();
}
