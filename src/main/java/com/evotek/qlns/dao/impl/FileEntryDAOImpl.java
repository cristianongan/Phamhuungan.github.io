/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.evotek.qlns.dao.FileEntryDAO;
import com.evotek.qlns.model.Document;
import com.evotek.qlns.model.FileEntry;

/**
 *
 * @author linhlh2
 */
public class FileEntryDAOImpl extends AbstractDAO<FileEntry> implements FileEntryDAO {

	private static final Logger _log = LogManager.getLogger(FileEntryDAOImpl.class);

	@Override
	public List<FileEntry> getFileEntryByIds(List<Long> fileIds) {
		List<FileEntry> results = new ArrayList<FileEntry>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<FileEntry> criteria = builder.createQuery(FileEntry.class);

			Root<FileEntry> root = criteria.from(FileEntry.class);

			if (fileIds != null && !fileIds.isEmpty()) {
				criteria.select(root).where(root.get("fileId").in(fileIds));

				results = session.createQuery(criteria).getResultList();
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

	@Override
	public List<FileEntry> getFileListByDocumentId(Long documentId) {
		List<FileEntry> results = new ArrayList<FileEntry>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<FileEntry> criteria = builder.createQuery(FileEntry.class);

			Root<FileEntry> root = criteria.from(FileEntry.class);

			Join<FileEntry, Document> documentJoin = root.join("document", JoinType.INNER);

			if (documentId != null) {
				criteria.select(root).where(builder.equal(documentJoin.get("documentId"), documentId));

				results = session.createQuery(criteria).getResultList();
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

}
