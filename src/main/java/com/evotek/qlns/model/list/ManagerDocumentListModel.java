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
 * @author LinhLH
 */
public class ManagerDocumentListModel extends AbstractModelList<Document> {

	private static final long serialVersionUID = 3488184656876613925L;
	
	private static final Logger _log = LogManager.getLogger(ManagerDocumentListModel.class);
	
	private String _department;
	private String _documentContent;
	private String _documentNumber;
	private DocumentService _documentService;
	private Long _documentType;
	private Date _fromDate;
	private List<Long> _idList;
	private boolean _isAdvance;
	private String _keyword;

	private Date _toDate;

	private boolean _view;

	public ManagerDocumentListModel(int pageSize, String keyword, boolean isAdvance, List<Long> idList, boolean view,
			DocumentService documentService) {
		this._pageSize = pageSize;
		this._keyword = keyword;
		this._isAdvance = isAdvance;
		this._idList = idList;
		this._view = view;
		this._documentService = documentService;

		setMultiple(true);
	}

	public ManagerDocumentListModel(int pageSize, String documentContent, String documentNumber, Long documentType,
			String department, Date fromDate, Date toDate, boolean isAdvance, List<Long> idList, boolean view,
			DocumentService documentService) {
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

	@Override
	public int getSize() {
		if (this._cachedSize < 0) {
			try {
				if (!this._view) {
					if (this._isAdvance) {
						this._cachedSize = this._documentService.getDocumentCountAdv(this._documentContent,
								this._documentNumber, this._documentType, this._department, this._fromDate,
								this._toDate);
					} else {
						this._cachedSize = this._documentService.getDocumentCountBasic(this._keyword);
					}
				} else {
					this._cachedSize = this._documentService.getDocumentByIdListCount(this._idList);
				}
			} catch (Exception ex) {
				_log.error(ex.getMessage(), ex);
			}
		}

		return this._cachedSize;
	}

	@Override
	public void loadToCache(int itemStartNumber, int pageSize) {
		try {
			if (!this._view) {
				if (this._isAdvance) {
					this._cache = this._documentService.getDocumentListAdv(this._documentContent, this._documentNumber,
							this._documentType, this._department, this._fromDate, this._toDate, itemStartNumber,
							pageSize, this._orderByColumn, this._orderByType);
				} else {
					this._cache = this._documentService.getDocumentListBasic(this._keyword, itemStartNumber, pageSize,
							this._orderByColumn, this._orderByType);

				}
			} else {
				this._cache = this._documentService.getDocumentListByIdList(this._idList, itemStartNumber, pageSize,
						this._orderByColumn, this._orderByType);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}
}
