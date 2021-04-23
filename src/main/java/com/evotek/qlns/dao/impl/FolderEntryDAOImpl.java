/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

import com.evotek.qlns.dao.FolderEntryDAO;
import com.evotek.qlns.model.FolderEntry;
/**
 *
 * @author linhlh2
 */
public class FolderEntryDAOImpl extends BasicDAO<FolderEntry>
        implements FolderEntryDAO{

    private static final Logger _log =
            LogManager.getLogger(FolderEntryDAOImpl.class);

    public FolderEntry getLastedFolderEntry() {
        FolderEntry folder = null;
        
        try{
            Criteria cri = currentSession().createCriteria(FolderEntry.class);

            cri.addOrder(Order.desc("folderId"));

            cri.setMaxResults(1);

            folder = (FolderEntry) cri.uniqueResult();
        }catch(Exception ex){
            _log.error(ex.getMessage(), ex);
        }

        return folder;
    }
}
