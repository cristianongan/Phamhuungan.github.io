/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.model.list;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evotek.qlns.model.Document;
import com.evotek.qlns.service.DocumentService;

/**
 *
 * @author PC
 */
public class ManagerDocumentListModel extends AbstractModelList<Document> {

    private String _keyword;
    private String _documentContent;
    private String _documentNumber;
    private Long _documentType;
    private String _department;
    private Date _fromDate;
    private Date _toDate;
    private boolean _isAdvance;
    private boolean _view;
    private List<Long> _idList;
    
    private DocumentService _documentService;
    
    private static final Logger _log =
            LogManager.getLogger(ManagerDocumentListModel.class);

    public ManagerDocumentListModel(int pageSize, String documentContent,
            String documentNumber, Long documentType, String department,
            Date fromDate, Date toDate, boolean isAdvance, List<Long> idList, 
            boolean view, DocumentService documentService) {
        this._pageSize = pageSize;
        this._documentContent = documentContent;
        this._documentNumber = documentNumber;
        this._documentType = documentType;
        this._department = department;
        this._fromDate = fromDate;
        this._toDate = toDate;
        this._isAdvance = isAdvance;
        this._idList = idList;
        this._view = view;
        this._documentService = documentService;

        setMultiple(true);
    }

    public ManagerDocumentListModel(int pageSize, String keyword, boolean isAdvance, 
            List<Long> idList, boolean view, DocumentService documentService) {
        this._pageSize = pageSize;
        this._keyword = keyword;
        this._isAdvance = isAdvance;
        this._idList = idList;
        this._view = view;
        this._documentService = documentService;

        setMultiple(true);
    }
    
    public void loadToCache(int itemStartNumber, int pageSize) {
        try {
            if (!_view) {
                if (_isAdvance) {
                    _cache = _documentService.getDocumentListAdv(_documentContent,
                            _documentNumber, _documentType, _department, _fromDate,
                            _toDate, itemStartNumber, pageSize, _orderByColumn,
                            _orderByType);
                } else {
                    _cache = _documentService.getDocumentListBasic(_keyword,
                            itemStartNumber, pageSize, _orderByColumn, _orderByType);

                }
            } else {
                _cache = _documentService.getDocumentListByIdList(_idList,
                        itemStartNumber, pageSize, _orderByColumn, _orderByType);
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public int getSize() {
        if (_cachedSize < 0) {
            try {
                if (!_view) {
                    if (_isAdvance) {
                        _cachedSize = _documentService.getDocumentCountAdv(_documentContent,
                                _documentNumber, _documentType, _department, _fromDate, _toDate);
                    } else {
                        _cachedSize = _documentService.getDocumentCountBasic(_keyword);
                    }
                } else {
                    _cachedSize = _documentService.getDocumentByIdListCount(_idList);
                }
            } catch (Exception ex) {
                _log.error(ex.getMessage(), ex);
            }
        }

        return _cachedSize;
    }
}
