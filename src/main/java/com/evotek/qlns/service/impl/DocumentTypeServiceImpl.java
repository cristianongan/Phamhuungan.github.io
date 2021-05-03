/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evotek.qlns.dao.DocumentDAO;
import com.evotek.qlns.dao.DocumentTypeDAO;
import com.evotek.qlns.model.Document;
import com.evotek.qlns.model.DocumentType;
import com.evotek.qlns.service.DocumentTypeService;
import com.evotek.qlns.util.QueryUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;

/**
 *
 * @author linhlh2
 */
@Service
@Transactional
public class DocumentTypeServiceImpl implements DocumentTypeService {

	private static final Logger _log = LogManager.getLogger(DocumentTypeServiceImpl.class);

	@Autowired
	private DocumentDAO documentDAO;

	@Autowired
	private DocumentTypeDAO documentTypeDAO;

//	@Autowired
//	private ServletContext _context; 
	
//	@PostConstruct
//	public void initDocumentType() {
//		System.out.println("DocumentType init............");
//		
//		this.getDocTypeMap(this._context);
//	}
	
	@Override
	public void delete(DocumentType documentType) {
		this.documentTypeDAO.deleteDocType(documentType);
	}

	@Override
	@CacheEvict(value = "allDocumentType", keyGenerator="customKeyGenerator")
	public void delete(DocumentType root, List<Long> docTypeGroupIds) throws Exception {
		try {
			DocumentType parent = root.getParentDocumentType();
			// xoa
			updateDeleteOrdinal(parent, root);

			// get danh sach tai lieu
			List<Document> docs = this.documentDAO.getDocumentListByIdList(docTypeGroupIds, QueryUtil.GET_ALL,
					QueryUtil.GET_ALL, null, null);

			for (Document doc : docs) {
				// set document type ve null
				doc.setDocumentTypeId(null);
			}

			// cap nhat tai lieu
			this.documentDAO.saveOrUpdate(docs);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Cacheable(value="allDocumentType", keyGenerator="customKeyGenerator", sync=true)
	public List<DocumentType> getAllDocumentType() {
		return this.documentTypeDAO.getAllDocumentType();
	}

	@Override
	public DocumentType getById(Long documentTypeId) {
		return this.documentTypeDAO.getById(documentTypeId);
	}

	@Override
	public List<DocumentType> getDocTypeByParentId(Long parentId) {
		return this.documentTypeDAO.getDocTypeByParentId(parentId);
	}

	private void getDocTypeMap(DocumentType root, Map<Long, List<DocumentType>> docTypeMap) {
		List<DocumentType> childs = root.getChildDocumentTypes();

		docTypeMap.put(root.getDocumentTypeId(), childs);

		if (Validator.isNull(childs)) {
			return;
		}

		for (DocumentType child : childs) {
			if (Validator.isNotNull(child)) {
				getDocTypeMap(child, docTypeMap);
			}
		}
	}

	@Override
	public Map<Long, List<DocumentType>> getDocTypeMap() {
		Map<Long, List<DocumentType>> docTypeMap = new HashMap<>();

		// get root
		List<DocumentType> roots = getDocTypeByParentId(null);

		roots.add(new DocumentType(-1L, "Chưa phân loại", Constants.Zicon.RANDOM));

		docTypeMap.put(null, roots);

		for (DocumentType root : roots) {
			if (Validator.isNotNull(root)) {
				getDocTypeMap(root, docTypeMap);
			}
		}

		return docTypeMap;
	}

	@Override
	public void saveOrUpdate(DocumentType documentType) {
		this.documentTypeDAO.saveOrUpdate(documentType);
	}

	@Override
	public void saveOrUpdate(DocumentType documentType, boolean flush) {
		this.documentTypeDAO.saveOrUpdate(documentType, flush);
	}

	@Override
	public void updateDeleteOrdinal(DocumentType parent, DocumentType remove) {
		if (Validator.isNull(parent)) {
			this.documentTypeDAO.delete(remove);

			List<DocumentType> siblings = getDocTypeMap().get(null);

			siblings.remove(remove);

			for (DocumentType docType : siblings) {
				if (Validator.isNull(docType.getDocumentTypeId())) {
					continue;
				}

				docType.setOrdinal(Long.valueOf(siblings.indexOf(docType)));

				this.documentTypeDAO.saveOrUpdate(docType, true);
			}
		} else {
			List<DocumentType> childs = parent.getChildDocumentTypes();

			childs.remove(remove);

			this.documentTypeDAO.delete(remove);

			for (int i = 0; i < childs.size(); i++) {
				childs.get(i).setOrdinal(Long.valueOf(i));
			}

			this.documentTypeDAO.saveOrUpdate(parent, true);
		}
	}

	public void updateDocTypeMap() {
		this.getDocTypeMap();
	}

	@Override
	public void updateDocTypeMap(DocumentType docType, DocumentType parentDocType) {
		Map<Long, List<DocumentType>> docTypeMap = getDocTypeMap();;

		Long patentId = null;

		if (Validator.isNotNull(parentDocType)) {
			patentId = parentDocType.getDocumentTypeId();
		}

		List<DocumentType> childs = docTypeMap.get(patentId);

		if (Validator.isNull(childs)) {
			childs = new ArrayList<>();

			docTypeMap.put(patentId, childs);
		}

		if (!childs.contains(docType)) {
			childs.add(docType);
		}

		if (Validator.isNotNull(parentDocType)) {

			parentDocType.setChildDocumentTypes(childs);

			this.saveOrUpdate(parentDocType, true);
		} else {
			this.saveOrUpdate(docType, true);
		}
	}

	@Override
	public void updateDocTypeMap(DocumentType docType, DocumentType parentDocType,
			DocumentType oldParentDocType) {

		if ((parentDocType == null && oldParentDocType == null)
				|| (parentDocType != null && parentDocType.equals(oldParentDocType))) {
			this.saveOrUpdate(docType, true);

			return;
		}

		Map<Long, List<DocumentType>> docTypeMap = getDocTypeMap();

		List<DocumentType> newDocTypes;
		List<DocumentType> oldDocTypes;
		Long patentId = null;
		Long oldPatentId = null;

		// update old doc
		if (Validator.isNotNull(oldParentDocType)) {
			oldPatentId = oldParentDocType.getDocumentTypeId();
		}

//        oldDocTypes = docTypeMap.get(oldPatentId);
		oldDocTypes = oldParentDocType.getChildDocumentTypes();

		oldDocTypes.remove(docType);

		// kiem tra list neu ko con phan tu nao thi xoa luon
		if (oldDocTypes.isEmpty()) {
			docTypeMap.remove(oldPatentId);
		} else {
			this.updateOrdinal(oldDocTypes);

			docTypeMap.put(oldPatentId, oldDocTypes);
		}

//        this.updateOrdinal(oldParentDocType.getChildDocumentTypes());

		// update new doc
		if (Validator.isNotNull(parentDocType)) {
			patentId = parentDocType.getDocumentTypeId();
		}

		newDocTypes = docTypeMap.get(parentDocType.getDocumentTypeId());

		if (Validator.isNull(newDocTypes)) {
			newDocTypes = new ArrayList<DocumentType>();

			parentDocType.setChildDocumentTypes(newDocTypes);

			docTypeMap.put(patentId, newDocTypes);
		}

		if (!newDocTypes.contains(docType)) {
			newDocTypes.add(docType);
		}

		// cap nhat lai ordinal
		docType.setOrdinal(Long.valueOf(newDocTypes.size() - 1));

		// luu thay doi

		if (Validator.isNotNull(parentDocType)) {
			docType.setParentDocumentType(parentDocType);

			this.saveOrUpdate(parentDocType, true);
		}

		if (Validator.isNotNull(oldParentDocType)) {
			this.saveOrUpdate(oldParentDocType, true);
		}
	}

	private void updateOrdinal(List<DocumentType> docTypes) {
		for (DocumentType docType : docTypes) {
			docType.setOrdinal(Long.valueOf(docTypes.indexOf(docType)));
		}
	}
}
