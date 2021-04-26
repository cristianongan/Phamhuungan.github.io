/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private DocumentTypeDAO documentTypeDAO;

	@Autowired
	private DocumentDAO documentDAO;

	private static final Logger _log = LogManager.getLogger(DocumentTypeServiceImpl.class);

	@Override
	public void saveOrUpdate(DocumentType documentType) {
		this.documentTypeDAO.saveOrUpdate(documentType);
	}

	@Override
	public void saveOrUpdate(DocumentType documentType, boolean flush) {
		this.documentTypeDAO.saveOrUpdate(documentType, flush);
	}

	@Override
	public List<DocumentType> getDocTypeByParentId(Long parentId) {
		return this.documentTypeDAO.getDocTypeByParentId(parentId);
	}

	@Override
	public void updateDeleteOrdinal(ServletContext context, DocumentType parent, DocumentType remove) {
		if (Validator.isNull(parent)) {
			this.documentTypeDAO.delete(remove);

			List<DocumentType> siblings = getDocTypeMap(context).get(null);

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

	@Override
	public void delete(DocumentType documentType) {
		this.documentTypeDAO.deleteDocType(documentType);
	}

	@Override
	public void delete(DocumentType root, List<Long> docTypeGroupIds, ServletContext context) throws Exception {
		try {
			DocumentType parent = root.getParentDocumentType();
			// xoa
			updateDeleteOrdinal(context, parent, root);

			// cap nhat lai map document type
//            updateDelDocTypeMap(context, root, parent);
			updateDelDocTypeMap(context, root, parent, docTypeGroupIds);
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
	public DocumentType getById(Long documentTypeId) {
		return this.documentTypeDAO.getById(documentTypeId);
	}

	@Override
	public List<DocumentType> getAllDocumentType() {
		return this.documentTypeDAO.getAllDocumentType();
	}

	@Override
	public Map<Long, List<DocumentType>> getDocTypeMap(ServletContext context) {
		return getDocTypeMap(context, false);
	}

	public void updateDocTypeMap(ServletContext context) {
		this.getDocTypeMap(context, true);
	}

	public Map<Long, List<DocumentType>> getDocTypeMap(ServletContext context, boolean doUpdate) {
		Map<Long, List<DocumentType>> docTypeMap = (Map<Long, List<DocumentType>>) context.getAttribute("docTypeMap");

		if (Validator.isNull(docTypeMap) || doUpdate) {
			docTypeMap = new HashMap<Long, List<DocumentType>>();

			// get root
			List<DocumentType> roots = getDocTypeByParentId(null);

			roots.add(new DocumentType(-1L, "Chưa phân loại", Constants.Z_ICON_RANDOM));

			docTypeMap.put(null, roots);

			for (DocumentType root : roots) {
				if (Validator.isNotNull(root)) {
					getDocTypeMap(root, docTypeMap);
				}
			}

			context.setAttribute("docTypeMap", docTypeMap);
		}

		return docTypeMap;
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

	public void updateDelDocTypeMap(ServletContext context, DocumentType docType, DocumentType parentDocType) {
		Map<Long, List<DocumentType>> docTypeMap = (Map<Long, List<DocumentType>>) context.getAttribute("docTypeMap");

		if (Validator.isNull(docTypeMap)) {
			docTypeMap = getDocTypeMap(context);
		}

		// xoa o nut cha
		List<DocumentType> docTypes;
		Long patentId = null;

		if (Validator.isNotNull(parentDocType)) {
			patentId = parentDocType.getDocumentTypeId();
		}

		docTypes = docTypeMap.get(patentId);

		if (Validator.isNotNull(docTypes)) {
			docTypes.remove(docType);

			if (docTypes.isEmpty()) {
				docTypeMap.remove(patentId);
			}
		}
		// xoa trong docTypeMap
		docTypeMap.remove(docType.getDocumentTypeId());

		context.setAttribute("docTypeMap", docTypeMap);
	}

	@Override
	public void updateDelDocTypeMap(ServletContext context, DocumentType docType, DocumentType parentDocType,
			List<Long> docTypeGroupIds) {
		Map<Long, List<DocumentType>> docTypeMap = (Map<Long, List<DocumentType>>) context.getAttribute("docTypeMap");

		if (Validator.isNull(docTypeMap)) {
			docTypeMap = getDocTypeMap(context);
		}

		// xoa o nut cha
		List<DocumentType> docTypes;
		Long patentId = null;

		if (Validator.isNotNull(parentDocType)) {
			patentId = parentDocType.getDocumentTypeId();
		}

		docTypes = docTypeMap.get(patentId);

		if (Validator.isNotNull(docTypes)) {
			docTypes.remove(docType);

			if (docTypes.isEmpty()) {
				docTypeMap.remove(patentId);
			}
		}
		// xoa trong docTypeMap
		for (Long docTypeId : docTypeGroupIds) {
			docTypeMap.remove(docTypeId);
		}

		context.setAttribute("docTypeMap", docTypeMap);
	}

	@Override
	public void updateDocTypeMap(ServletContext context, DocumentType docType, DocumentType parentDocType) {
		Map<Long, List<DocumentType>> docTypeMap = (Map<Long, List<DocumentType>>) context.getAttribute("docTypeMap");

		if (Validator.isNull(docTypeMap)) {
			docTypeMap = getDocTypeMap(context);
		}

		Long patentId = null;

		if (Validator.isNotNull(parentDocType)) {
			patentId = parentDocType.getDocumentTypeId();
		}

		List<DocumentType> childs = docTypeMap.get(patentId);

		if (Validator.isNull(childs)) {
			childs = new ArrayList<DocumentType>();

			docTypeMap.put(patentId, childs);
		}

		if (!childs.contains(docType)) {
			childs.add(docType);
		}

		if (Validator.isNotNull(parentDocType)) {
//            childs.add(docType);

			parentDocType.setChildDocumentTypes(childs);

			this.saveOrUpdate(parentDocType, true);
		} else {
			this.saveOrUpdate(docType, true);
		}

		context.setAttribute("docTypeMap", docTypeMap);
	}

	@Override
	public void updateDocTypeMap(ServletContext context, DocumentType docType, DocumentType parentDocType,
			DocumentType oldParentDocType) {

		if ((parentDocType == null && oldParentDocType == null)
				|| (parentDocType != null && parentDocType.equals(oldParentDocType))) {
			this.saveOrUpdate(docType, true);

			return;
		}

		Map<Long, List<DocumentType>> docTypeMap = (Map<Long, List<DocumentType>>) context.getAttribute("docTypeMap");

		if (Validator.isNull(docTypeMap)) {
			docTypeMap = getDocTypeMap(context);
		}

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

		context.setAttribute("docTypeMap", docTypeMap);
	}

	private void updateOrdinal(List<DocumentType> docTypes) {
		for (DocumentType docType : docTypes) {
			docType.setOrdinal(Long.valueOf(docTypes.indexOf(docType)));
		}
	}
}
