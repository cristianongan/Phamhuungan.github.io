/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao;

import java.util.List;

import com.evotek.qlns.model.FileEntry;

/**
 *
 * @author linhlh2
 */
public interface FileEntryDAO {
    public void saveOrUpdate(FileEntry file);

    public void delete(FileEntry file);

    public List<FileEntry> getFileEntryByIds(List<Long> fileIds);

    public List<FileEntry> getFileListByDocumentId(Long documentId);

}
