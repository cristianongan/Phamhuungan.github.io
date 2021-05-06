/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.A;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Include;
import com.evotek.qlns.extend.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.DocumentType;
import com.evotek.qlns.service.DocumentTypeService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author LinhLH
 */
@Controller
@Scope("prototype")
public class AddEditDocumentTypeController extends BasicController<Window> implements Serializable {

	private static final long serialVersionUID = -6515082727563393176L;

	private static final Logger _log = LogManager.getLogger(AddEditDocumentTypeController.class);

	@Autowired
	private DocumentTypeService documentTypeService;

	private A btnClearDoc;
	
	private Bandbox bbDocumentType;
	
	private DocumentType docType;
	private DocumentType oldParentDocType;
	private DocumentType parentDocType;
	
	private Include icDocumentType;
	
	private Map<Long, List<DocumentType>> docTypeMap;
	
	private Textbox tbDescription;
	private Textbox tbTypeName;
	
	private Window winDocType;
	private Window winTemp;

	private void _setEditForm() throws Exception {
		this.tbTypeName.setValue(this.docType.getTypeName());
		this.tbDescription.setValue(this.docType.getDescription());

		if (Validator.isNotNull(this.oldParentDocType)) {
			this.bbDocumentType.setValue(this.oldParentDocType.getTypeName());
			this.bbDocumentType.setAttribute(Constants.Attr.ID, this.oldParentDocType.getDocumentTypeId());
			this.bbDocumentType.setAttribute(Constants.Attr.OBJECT, this.oldParentDocType);
			this.btnClearDoc.setVisible(true);
		}
	}

	public boolean _validate(String typeName, String description) {
		if (Validator.isNull(typeName)) {
			this.tbTypeName
					.setErrorMessage(Values.getRequiredInputMsg(Labels.getLabel(LanguageKeys.DOCUMENT_TYPE_NAME)));

			this.tbTypeName.setFocus(true);

			return false;
		}

		if (typeName.length() > Values.LONG_LENGTH) {
			this.tbTypeName.setErrorMessage(Values
					.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.DOCUMENT_TYPE_NAME), Values.LONG_LENGTH));

			this.tbTypeName.setFocus(true);

			return false;
		}

		if (Validator.isNotNull(description) && description.length() > Values.GREATE_LONG_LENGTH) {
			this.tbDescription.setErrorMessage(Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.DESCRIPTION),
					Values.GREATE_LONG_LENGTH));

			return false;
		}

		return true;
	}

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		// init data
		this.initData();
	}

	@Override
	public void doBeforeComposeChildren(Window comp) throws Exception {
		super.doBeforeComposeChildren(comp);

		this.winDocType = comp;

		this.docTypeMap = this.documentTypeService.getDocTypeMap();
	}

	private Long getOrdinal() {
		List<DocumentType> docTypes;

		if (Validator.isNull(this.parentDocType)) {
			docTypes = this.docTypeMap.get(null);
		} else {
			docTypes = this.docTypeMap.get(this.parentDocType.getDocumentTypeId());
		}

		if (Validator.isNull(docTypes)) {
			return 0L;
		}

		Long ordinal = docTypes.get(docTypes.size() - 1).getOrdinal();

		return Validator.isNull(ordinal) ? docTypes.size() : ordinal + 1;
	}

	// init data
	public void initData() throws Exception {
		try {
			this.winTemp = (Window) this.arg.get(Constants.Attr.PARENT_WINDOW);

			this.docType = (DocumentType) this.arg.get(Constants.Attr.OBJECT);
			this.parentDocType = (DocumentType) this.arg.get(Constants.Attr.SECOND_OBJECT);

			if (Validator.isNotNull(this.parentDocType)) {
				this.bbDocumentType.setValue(this.parentDocType.getTypeName());
				this.bbDocumentType.setAttribute(Constants.Attr.ID, this.parentDocType.getDocumentTypeId());
				this.bbDocumentType.setAttribute(Constants.Attr.OBJECT, this.parentDocType);
			}

			if (Validator.isNotNull(this.docType)) {
				this.winDocType.setTitle((String) this.arg.get(Constants.Attr.TITLE));

				this.oldParentDocType = this.docType.getParentDocumentType();

				this._setEditForm();
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	public void onClick$btnCancel() {
		this.winDocType.detach();
	}

	// Bandbox documentType
	public void onClick$btnClearDoc() {
		this.bbDocumentType.setValue(StringPool.BLANK);
		this.bbDocumentType.setAttribute(Constants.Attr.ID, null);

		this.btnClearDoc.setDisabled(true);
		this.btnClearDoc.setVisible(false);
	}

	public void onClick$btnSave() throws Exception {
		boolean update = true;

		try {
			String typeName = GetterUtil.getString(this.tbTypeName.getValue());
			String description = GetterUtil.getString(this.tbDescription.getValue());
			this.parentDocType = (DocumentType) this.bbDocumentType.getAttribute(Constants.Attr.OBJECT);

			if (this._validate(typeName, description)) {
				if (Validator.isNull(this.docType)) {
					update = false;

					this.docType = new DocumentType();

					this.docType.setUserId(getUserId());
					this.docType.setUserName(getUserName());
					this.docType.setCreateDate(new Date());
					this.docType.setStatus(Values.ENABLE);
					this.docType.setOrdinal(getOrdinal());
				}

				this.docType.setTypeName(typeName);
				this.docType.setDescription(description);
				this.docType.setModifiedDate(new Date());
				this.docType.setParentDocumentType(this.parentDocType);

				if (!update) {
					this.documentTypeService.updateDocTypeMap(this.docType, this.parentDocType);
				} else {
					this.documentTypeService.updateDocTypeMap(this.docType, this.parentDocType, this.oldParentDocType);
				}

				ComponentUtil.createSuccessMessageBox(ComponentUtil.getSuccessKey(update));

				this.winDocType.detach();

				Events.sendEvent("onLoadData", this.winTemp, null);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);

			Messagebox.show(Labels.getLabel(ComponentUtil.getFailKey(update)));
		}
	}

	public void onOpen$bbDocumentType() {
		if (this.bbDocumentType.isOpen() && Validator.isNull(this.icDocumentType.getSrc())) {
			this.icDocumentType.setAttribute("bandbox", this.bbDocumentType);
			this.icDocumentType.setAttribute("btnclear", this.btnClearDoc);

			if (Validator.isNotNull(this.docType)) {
				this.icDocumentType.setAttribute("exclude", this.docType);
			}

			this.icDocumentType.setSrc(Constants.Page.Common.TREE_DOCUMENT_TYPE);
		}
	}
	// Bandbox documentType
}
