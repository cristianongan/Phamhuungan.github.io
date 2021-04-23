/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.evotek.qlns.dao.FileEntryDAO;
import com.evotek.qlns.model.FileEntry;
/**
 *
 * @author linhlh2
 */
public class FileEntryDAOImpl extends AbstractDAO<FileEntry>
        implements FileEntryDAO{

    private static final Logger _log =
            LogManager.getLogger(FileEntryDAOImpl.class);

    @Override
	public List<FileEntry> getFileEntryByIds(List<Long> fileIds) {
        List<FileEntry> results = new ArrayList<FileEntry>();

        try {
            Criteria cri = currentSession().createCriteria(FileEntry.class);

            if (fileIds != null && !fileIds.isEmpty()) {
                cri.add(Restrictions.in("fileId", fileIds));

                results = cri.list();
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
            Criteria cri = currentSession().createCriteria(FileEntry.class);

            if (documentId != null) {
                cri.add(Restrictions.eq("document.documentId", documentId));

                results = cri.list();
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return results;
    }


}
