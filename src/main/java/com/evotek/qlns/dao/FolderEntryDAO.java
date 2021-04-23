/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao;

import com.evotek.qlns.model.FolderEntry;

/**
 *
 * @author linhlh2
 */
public interface FolderEntryDAO {
    public FolderEntry getLastedFolderEntry();

    public void saveOrUpdate(FolderEntry folder);

    public void rollback();
}
