/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.evotek.qlns.dao.DocumentTypeDAO;
import com.evotek.qlns.model.DocumentType;
import com.evotek.qlns.util.Validator;

/**
 *
 * @author MRHOT
 */
public class DocumentTypeDAOImpl extends AbstractDAO<DocumentType>
        implements DocumentTypeDAO {

    @Override
	public void saveOrUpdate(DocumentType documentType, boolean flush) {
        saveOrUpdate(documentType);
        
        currentSession().flush();
    }

    @Override
	public void delete(Collection<DocumentType> entities) throws Exception {
        deleteAll(entities);
    }

    @Override
	public void deleteDocType(DocumentType documentType) {
        delete(documentType);
    }

    @Override
	public List<DocumentType> getDocTypeByParentId(Long parentId) {
        List<DocumentType> results = new ArrayList<DocumentType>();

        try {
            Session session = currentSession();

            Criteria cri = session.createCriteria(DocumentType.class);

            if (Validator.isNull(parentId)) {
                cri.add(Restrictions.isNull("parentDocumentType.documentTypeId"));
            } else {
                cri.add(Restrictions.eq("parentDocumentType.documentTypeId",
                        parentId));
            }

            cri.addOrder(Order.asc("ordinal"));

            results = cri.list();

        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return results;
    }
    
    @Override
	public List<DocumentType> getAllDocumentType(){
        List<DocumentType> results = new ArrayList<DocumentType>();
        
        try {
            Session session = currentSession();
            
            Criteria cri = session.createCriteria(DocumentType.class);
            
            cri.addOrder(Order.asc("typeName").ignoreCase());
            
            results = cri.list();

        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }
        
        return results;
    }

    @Override
	public DocumentType getById(Long documentTypeId) {
        return (DocumentType) get(DocumentType.class, documentTypeId);
    }

    private static final Logger _log = LogManager.getLogger(DocumentTypeDAOImpl.class);

}
