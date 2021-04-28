/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.evotek.qlns.dao.DocumentTypeDAO;
import com.evotek.qlns.model.DocumentType;
import com.evotek.qlns.util.Validator;

/**
 *
 * @author LinhLH
 */
@Repository
public class DocumentTypeDAOImpl extends AbstractDAO<DocumentType> implements DocumentTypeDAO {

	private static final Logger _log = LogManager.getLogger(DocumentTypeDAOImpl.class);

	@Override
	public void delete(Collection<DocumentType> entities) throws Exception {
		deleteAll(entities);
	}

	@Override
	public void deleteDocType(DocumentType documentType) {
		delete(documentType);
	}

	@Override
	public List<DocumentType> getAllDocumentType() {
		List<DocumentType> results = new ArrayList<DocumentType>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<DocumentType> criteria = builder.createQuery(DocumentType.class);

			Root<DocumentType> root = criteria.from(DocumentType.class);

			criteria.select(root);

			criteria.orderBy(builder.asc(builder.lower(root.get("typeName"))));

			results = session.createQuery(criteria).getResultList();

		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return results;
	}

	@Override
	public DocumentType getById(Long documentTypeId) {
		return (DocumentType) findById(DocumentType.class, documentTypeId);
	}

	@Override
	public List<DocumentType> getDocTypeByParentId(Long parentId) {
		List<DocumentType> results = new ArrayList<DocumentType>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<DocumentType> criteria = builder.createQuery(DocumentType.class);

			Root<DocumentType> root = criteria.from(DocumentType.class);

			Join<DocumentType, DocumentType> parentJoin = root.join("parentDocumentType", JoinType.INNER);

			criteria.select(root);

			if (Validator.isNull(parentId)) {
				criteria.where(builder.isNull(parentJoin.get("documentTypeId")));
			} else {
				criteria.where(builder.equal(parentJoin.get("documentTypeId"), parentId));
			}

			criteria.orderBy(builder.asc(root.get("ordinal")));

			results = session.createQuery(criteria).getResultList();

		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return results;
	}

	@Override
	public void saveOrUpdate(DocumentType documentType, boolean flush) {
		saveOrUpdate(documentType);

		getCurrentSession().flush();
	}

}
