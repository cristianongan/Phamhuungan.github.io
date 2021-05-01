/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Document;
import com.evotek.qlns.model.FileEntry;
import com.evotek.qlns.model.User;
import com.evotek.qlns.service.DocumentService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;
import com.evotek.qlns.util.key.ZkKeys;

/**
 *
 * @author LinhLH
 */
@Controller
@Scope("prototype")
public class AddEditDocumentController extends BasicController<Window> implements Serializable {

	private static final Logger _log = LogManager.getLogger(AddEditDocumentController.class);

	private static final long serialVersionUID = 5009409376172819411L;

	@Autowired
	private DocumentService documentService;

	// Bandbox documentType
	private Bandbox bbDocumentType;

	private A btnClearDoc;
	// Bandbox documentType
	private Combobox cbDepartment;

	private Datebox dbDate;

	private Div divUpload;

	private Document document;

	private Include icDocumentType;

	private Integer index;

	private ListModel model;

	private Textbox txtContent;

	private Textbox txtDocumentNumber;

	private Window winAddEditDocument;

	private Hlayout winParent;

	public boolean _validate(String documentNumber, Long documentTypeId, String content) {
		Long documentId = Validator.isNull(this.document) ? null : this.document.getDocumentId();

		if (Validator.isNull(documentNumber)) {
			this.txtDocumentNumber
					.setErrorMessage(Values.getRequiredInputMsg(Labels.getLabel(LanguageKeys.DOCUMENT_NUMBER)));

			this.txtDocumentNumber.setFocus(true);

			return false;
		}

		if (documentNumber.length() > Values.LONG_LENGTH) {
			this.txtDocumentNumber.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.DOCUMENT_NUMBER), Values.LONG_LENGTH));

			this.txtDocumentNumber.setFocus(true);

			return false;
		}

		if (Validator.isNull(documentTypeId)) {
			this.bbDocumentType
					.setErrorMessage(Values.getRequiredInputMsg(Labels.getLabel(LanguageKeys.DOCUMENT_TYPE)));

			this.bbDocumentType.setFocus(true);

			return false;
		}

		if (Validator.isNull(content)) {
			this.txtContent.setErrorMessage(Values.getRequiredInputMsg(Labels.getLabel(LanguageKeys.CONTENT)));

			this.txtContent.setFocus(true);

			return false;
		}

		if (content.length() > Values.GREATE_LONG_LENGTH) {
			this.txtContent.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.CONTENT), Values.GREATE_LONG_LENGTH));

			this.txtContent.setFocus(true);

			return false;
		}

		if (this.documentService.isExisted(documentNumber, content, documentId)) {
			this.txtDocumentNumber.setErrorMessage(Values.getDuplicateMsg(Labels.getLabel(LanguageKeys.DOCUMENT)));

			this.txtDocumentNumber.setFocus(true);

			return false;
		}

		return true;
	}

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		this.winParent = (Hlayout) this.arg.get(Constants.PARENT_WINDOW);

		this.document = (Document) this.arg.get(Constants.OBJECT);

		this.model = (ListModel) this.arg.get(Constants.MODEL);

		this.index = (Integer) this.arg.get(Constants.INDEX);

		initData();
	}

	@Override
	public void doBeforeComposeChildren(Window comp) throws Exception {
		super.doBeforeComposeChildren(comp); // To change body of generated methods, choose Tools | Templates.

		this.winAddEditDocument = comp;
	}

	public void initData() {
		if (Validator.isNotNull(this.document)) {
			this.winAddEditDocument.setTitle((String) this.arg.get(Constants.TITLE));

			onLoadForm(this.document);
		} else {
			this.txtDocumentNumber.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					if (AddEditDocumentController.this.documentService.isExisted(
							GetterUtil.getString(AddEditDocumentController.this.txtDocumentNumber.getValue()))) {
						Clients.showNotification(
								Labels.getLabel(LanguageKeys.MESSAGE_CHECK_EXISTED_DATA_BEFORE_SAVE,
										new Object[] { Labels.getLabel(LanguageKeys.DOCUMENT_NUMBER) }),
								ZkKeys.WARNING, AddEditDocumentController.this.txtDocumentNumber, ZkKeys.END_CENTER, 0,
								true);
					}
				}
			});
		}

		// create File
		this.onCreateFile();
	}

	public void onClick$btnCancel() {
		this.winAddEditDocument.detach();
	}

	// Bandbox documentType
	public void onClick$btnClearDoc() {
		this.bbDocumentType.setValue(StringPool.BLANK);
		this.bbDocumentType.setAttribute(Constants.ID, null);

		this.btnClearDoc.setDisabled(true);
		this.btnClearDoc.setVisible(false);
	}

	public void onClick$btnNext() throws Exception {
		if (Validator.isNotNull(this.document)) {
			if (this.index == (this.model.getSize() - 1)) {
				this.index = 0;
			} else {
				this.index++;
			}

			this.document = (Document) this.model.getElementAt(this.index);

			this.divUpload.getChildren().clear();

			initData();

			onCreate$cbDepartment();
		}
	}

	public void onClick$btnPrevious() throws Exception {
		if (Validator.isNotNull(this.document)) {
			if (this.index == 0) {
				this.index = (this.model.getSize() - 1);
			} else {
				this.index--;
			}

			this.document = (Document) this.model.getElementAt(this.index);

			this.divUpload.getChildren().clear();

			initData();

			onCreate$cbDepartment();
		}
	}

	public void onClick$btnSave() {
		boolean update = true;

		try {
			String documentNumber = GetterUtil.getString(this.txtDocumentNumber.getValue());
			Long documentTypeId = ComponentUtil.getBandboxValue(this.bbDocumentType);
			Date publishDate = this.dbDate.getValue();
			String publisherDept = GetterUtil.getString(this.cbDepartment.getValue());
			String content = GetterUtil.getString(this.txtContent.getValue());

			if (_validate(documentNumber, documentTypeId, content)) {
				if (Validator.isNull(this.document)) {
					update = false;

					this.document = new Document();

					this.document.setUserId(getUserId());
					this.document.setUserName(getUserName());
					this.document.setCreateDate(new Date());
				}

				this.document.setDocumentNumber(documentNumber);
				this.document.setDocumentTypeId(documentTypeId);
				this.document.setModifiedDate(new Date());
				this.document.setPromulgationDate(publishDate);
				this.document.setPromulgationDept(publisherDept);
				this.document.setContent(content);

				this.documentService.saveOrUpdate(this.document);

				updateFileEntry(this.document);

				ComponentUtil.createSuccessMessageBox(ComponentUtil.getSuccessKey(update));

				this.winAddEditDocument.detach();

				Events.sendEvent("onLoadDataCRUD", this.winParent, null);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage());

			Messagebox.show(Labels.getLabel(ComponentUtil.getFailKey(update)));
		}

	}

	public void onCreate$cbDepartment() {
		ListModel dictModel = new SimpleListModel(this.documentService.getDepartment());
		this.cbDepartment.setModel(dictModel);
	}

	/**
	 * Hàm tạo control upload file
	 */
	public void onCreateFile() {
		if (this.divUpload.getChildren().isEmpty()) {
			List<FileEntry> oldFiles = new ArrayList<FileEntry>();

			Component comp = null;

			if (Validator.isNotNull(this.document)) {
				oldFiles = this.documentService.getListFileEntrys(this.document.getDocumentId());
			}

			String config = "true,multiple=true";

			comp = ComponentUtil.createAttachment(config, oldFiles);

			comp.setAttribute(Constants.ID, oldFiles);

			this.divUpload.appendChild(comp);
		}
	}

	public void onLoadForm(Document document) {
		this.txtDocumentNumber.setValue(document.getDocumentNumber());

		if (Validator.isNotNull(document.getDocumentTypeId())) {
			this.bbDocumentType.setValue(document.getTypeName());
			this.bbDocumentType.setAttribute(Constants.ID, document.getDocumentTypeId());
			this.btnClearDoc.setVisible(true);
		}

		this.cbDepartment.setValue(document.getPromulgationDept());
		this.dbDate.setValue(document.getPromulgationDate());
		this.txtContent.setValue(document.getContent());
	}

	public void onOpen$bbDocumentType() {
		if (this.bbDocumentType.isOpen() && Validator.isNull(this.icDocumentType.getSrc())) {
			this.icDocumentType.setAttribute("bandbox", this.bbDocumentType);
			this.icDocumentType.setAttribute("btnclear", this.btnClearDoc);

			this.icDocumentType.setSrc(Constants.TREE_DOCUMENT_TYPE_PAGE);
		}
	}
	// Bandbox documentType

	/**
	 * Hàm save file upload
	 * 
	 * @param ticket
	 * @throws Exception
	 */

	public List<FileEntry> updateFileEntry(Document document) {
		User users = getUser();

		List<FileEntry> oldFiles = (List<FileEntry>) this.divUpload.getFirstChild().getAttribute(Constants.ID);

		// Lay list cac file xoa di trong list file cu
		List<FileEntry> deleteFiles = (List<FileEntry>) this.divUpload.getFirstChild().getAttribute(Constants.OBJECT);

		List<FileEntry> deletedFiles = new ArrayList<FileEntry>();
		List<FileEntry> addedFiles = new ArrayList<FileEntry>();

		// xoa file trong truong hop sua doi tuong
		if (deleteFiles != null && !deleteFiles.isEmpty()) {
			deletedFiles = this.documentService.removeFileEntry(deleteFiles);

			oldFiles.removeAll(deletedFiles);
		}

		List<Media> medium = (List<Media>) this.divUpload.getFirstChild().getAttribute(Constants.DATA);

		if (medium != null && !medium.isEmpty()) {
			addedFiles = this.documentService.saveMedia(users, medium, document);

			oldFiles.addAll(addedFiles);
		}

		return oldFiles;
	}
}
