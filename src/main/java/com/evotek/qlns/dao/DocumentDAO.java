/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.evotek.qlns.model.Document;

/**
 *
 * @author MRHOT
 */
public interface DocumentDAO {

    public List<Document> getAll();

    public int getDocumentCountAdv(String documentContent,
            String documentNumber, Long documentType, String department,
            Date fromDate, Date toDate);

    public List<Document> getDocumentListAdv(String documentContent,
            String documentNumber, Long documentType, String department,
            Date fromDate, Date toDate, int firstResult, int maxResult,
            String orderByColumn, String orderByType);

    public int getDocumentCountBasic(String textSearch);

    public List<Document> getDocumentListBasic(String textSearch, int firstResult,
            int maxResult, String orderByColumn, String orderByType);

    public void delete(Document document) throws DataAccessException;

    public void delete(Collection<Document> documents);

    public void saveOrUpdate(Document document);
    
    public void saveOrUpdate(Collection<Document> documents);

    public int getDocumentByIdListCount(List<Long> idList);

    public List<Document> getDocumentListByIdList(List<Long> idList, int firstResult,
            int maxResult, String orderByColumn, String orderByType);

    public List<Document> getDocumentByI_N_C(Long documentId, String documentNumber, 
            String content);

    public List<Document> getDocumentByN(String documentNumber);
}
