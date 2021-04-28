/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.evotek.qlns.dao.FolderEntryDAO;
import com.evotek.qlns.model.FolderEntry;

/**
 *
 * @author linhlh2
 */
@Repository
public class FolderEntryDAOImpl extends AbstractDAO<FolderEntry> implements FolderEntryDAO {

	private static final Logger _log = LogManager.getLogger(FolderEntryDAOImpl.class);

	@Override
	public FolderEntry getLastedFolderEntry() {
		FolderEntry folder = null;

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<FolderEntry> criteria = builder.createQuery(FolderEntry.class);

			Root<FolderEntry> root = criteria.from(FolderEntry.class);

			criteria.select(root);

			criteria.orderBy(builder.desc(root.get("folderId")));

			folder = (FolderEntry) session.createQuery(criteria).setMaxResults(1).uniqueResult();
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return folder;
	}
}
