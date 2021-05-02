/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Nav;
import org.zkoss.zkmax.zul.Navbar;
import org.zkoss.zkmax.zul.Navitem;
import org.zkoss.zul.A;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Popup;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.extend.Messagebox;
import com.evotek.qlns.model.Document;
import com.evotek.qlns.model.DocumentType;
import com.evotek.qlns.model.list.ManagerDocumentListModel;
import com.evotek.qlns.model.render.ListboxDocumentRender;
import com.evotek.qlns.service.DocumentService;
import com.evotek.qlns.service.DocumentTypeService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.ExcelUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.QueryUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.ZkKeys;

/**
 *
 * @author LinhLH
 */
@Controller
@Scope("prototype")
public class DocumentController extends BasicController<Hlayout> implements Serializable {
	private static final long serialVersionUID = -3106857813427291244L;

	private static final Logger _log = LogManager.getLogger(DocumentController.class);

	private static final String DOCUMENT_TYPE_PAGE = "~./pages/document_type/view.zul";

	private static final String EDIT_PAGE = "~./pages/manager_document/edit.zul";

	@Autowired
	private DocumentService documentService;

	@Autowired
	private DocumentTypeService documentTypeService;

	private A btnClearDoc;
	private A toggler;
	private A togglerBack;

	private Bandbox bbDocumentType;

	private Button btnEnableAdvSearch;

	private Combobox cbDepartment;

	private Datebox dbFromDate;
	private Datebox dbToDate;

	private Div sidebar;

	private Hlayout winDocument;

	private Include icDocumentType;

	private List<DocumentType> docTypes;

	private Listbox listboxResult;

	private Map<Long, List<DocumentType>> docTypeMap;
	private Map<String, Object> paramMap = new HashMap<String, Object>();

	private Navbar navbar;
	private Navbar navbarSearch;

	private Navitem selectedNavitem;

	private Popup advanceSearchPopup;

	private Textbox tbKeyword;
	private Textbox tbSearchDocType;
	private Textbox txtDocumentContent;
	private Textbox txtDocumentNumber;

	private boolean isAdvance;

	public void advanceSearch() {
		String documentContent = GetterUtil.getString(this.txtDocumentContent.getValue());
		String documentNumber = GetterUtil.getString(this.txtDocumentNumber.getValue());
		Long documentType = ComponentUtil.getBandboxValue(this.bbDocumentType);
		String department = GetterUtil.getString(this.cbDepartment.getValue());
		Date fromDate = this.dbFromDate.getValue();
		Date toDate = this.dbToDate.getValue();

		// create param map
		this.paramMap.put("documentContent", documentContent);
		this.paramMap.put("documentNumber", documentNumber);
		this.paramMap.put("documentType", documentType);
		this.paramMap.put("department", department);
		this.paramMap.put("fromDate", fromDate);
		this.paramMap.put("toDate", toDate);

		ListModel model = new ManagerDocumentListModel(this.listboxResult.getPageSize(), documentContent,
				documentNumber, documentType, department, fromDate, toDate, this.isAdvance, null, false,
				this.documentService);

		this.listboxResult.setModel(model);

		this.listboxResult.setItemRenderer(new ListboxDocumentRender(this.winDocument, model, this.documentService));

		this.listboxResult.setMultiple(true);

		this.paramMap.remove("docTypeIds");
	}

	public void basicSearch() {
		String keyword = GetterUtil.getString(this.tbKeyword.getValue());

		this.paramMap.put("keyword", keyword);

		ListModel model = new ManagerDocumentListModel(this.listboxResult.getPageSize(), keyword, this.isAdvance, null,
				false, this.documentService);

		this.listboxResult.setModel(model);

		this.listboxResult.setItemRenderer(new ListboxDocumentRender(this.winDocument, model, this.documentService));

		this.listboxResult.setMultiple(true);

		this.paramMap.remove("docTypeIds");

		if (Validator.isNotNull(keyword)) {
			this.onHightLight(keyword);

			this.listboxResult.addEventListener("onPaging", new EventListener<Event>() {

				@Override
				public void onEvent(Event t) throws Exception {
					String keyword = GetterUtil.getString(DocumentController.this.tbKeyword.getValue());

					if (!DocumentController.this.isAdvance && Validator.isNotNull(keyword)) {
						onHightLight(keyword);
					}
				}
			});
		}
	}

	private void createDocTypeNav(DocumentType docType, Component parentNode) {
		List<DocumentType> childs = this.docTypeMap.get(docType.getDocumentTypeId());

		if (Validator.isNull(childs)) {
			final Navitem navItem = new Navitem();

			navItem.setLabel(docType.getTypeName());

			if (Validator.isNotNull(docType.getIcon())) {
				navItem.setIconSclass(docType.getIcon());
			} else {
				navItem.setIconSclass("z-icon-angle-double-right");
			}

			navItem.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

				@Override
				public void onEvent(Event t) throws Exception {
					navItem.setSelected(true);
				}
			});

			navItem.addForward(Events.ON_CLICK, this.winDocument, ZkKeys.ON_LOAD_DATA,
					new Object[] { navItem, docType });

			parentNode.appendChild(navItem);
		} else {
			Collections.sort(childs);

			Nav nav = new Nav(docType.getTypeName());

			if (Validator.isNotNull(docType.getIcon())) {
				nav.setIconSclass(docType.getIcon());
			} else {
				nav.setIconSclass("z-icon-angle-double-right");
			}

			for (DocumentType child : childs) {
				if (Validator.isNull(child)) {
					continue;
				}

				createDocTypeNav(child, nav);
			}

			nav.addForward(Events.ON_OPEN, this.winDocument, ZkKeys.ON_LOAD_DATA, new Object[] { nav, docType });

			parentNode.appendChild(nav);
		}
	}

	@Override
	public void doAfterCompose(Hlayout comp) throws Exception {
		super.doAfterCompose(comp);

		this.winDocument = comp;

		this.init();
	}

	@Override
	public void doBeforeComposeChildren(Hlayout comp) throws Exception {
		super.doBeforeComposeChildren(comp); // To change body of generated methods, choose Tools | Templates.

		this.winDocument = comp;

		this.docTypeMap = this.documentTypeService.getDocTypeMap();
	}

	public List<Object[]> getHeaderInfors() {
		List<Object[]> headerInfors = new ArrayList<Object[]>();

		headerInfors.add(new Object[] { Labels.getLabel(LanguageKeys.COLUMN_NUMBER), 2000 });
		headerInfors.add(new Object[] { Labels.getLabel(LanguageKeys.DOCUMENT_NUMBER), 8000 });
		headerInfors.add(new Object[] { Labels.getLabel(LanguageKeys.DOCUMENT_TYPE), 8000 });
		headerInfors.add(new Object[] { Labels.getLabel(LanguageKeys.PUBLISHER), 8000 });
		headerInfors.add(new Object[] { Labels.getLabel(LanguageKeys.PUBLIC_DATE), 9000 });
		headerInfors.add(new Object[] { Labels.getLabel(LanguageKeys.CONTENT), 18000 });
		//

		return headerInfors;
	}

	public List<Long> getListDocumentTypeChild(DocumentType docParent, List<Long> docTypeList) {
		if (Validator.isNull(docParent)) {
			return docTypeList;
		}

		List<DocumentType> childs = this.docTypeMap.get(docParent.getDocumentTypeId());

		docTypeList.add(docParent.getDocumentTypeId());

		if (Validator.isNotNull(childs)) {
			for (DocumentType documentType : childs) {
				getListDocumentTypeChild(documentType, docTypeList);
			}
		}

		return docTypeList;
	}

	public List<Document> getListExport() throws Exception {
		List<Document> results = new ArrayList<Document>();

		try {
			List<Long> docTypeIds = (List<Long>) this.paramMap.get("docTypeIds");

			if (Validator.isNotNull(docTypeIds)) {
				return this.documentService.getDocumentListByIdList(docTypeIds, QueryUtil.GET_ALL, QueryUtil.GET_ALL,
						null, null);
			}

			if (this.isAdvance) {
				String documentContent = (String) this.paramMap.get("documentContent");
				String documentNumber = (String) this.paramMap.get("documentNumber");
				Long documentType = (Long) this.paramMap.get("documentType");
				String department = (String) this.paramMap.get("department");
				Date fromDate = (Date) this.paramMap.get("fromDate");
				Date toDate = (Date) this.paramMap.get("toDate");

				results = this.documentService.getDocumentListAdv(documentContent, documentNumber, documentType,
						department, fromDate, toDate, QueryUtil.GET_ALL, QueryUtil.GET_ALL, null, null);
			} else {
				String keyword = (String) this.paramMap.get("keyword");

				results = this.documentService.getDocumentListBasic(keyword, QueryUtil.GET_ALL, QueryUtil.GET_ALL, null,
						null);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

	public List<String> getProperties() {
		List<String> properties = new ArrayList<String>();

		properties.add("documentNumber");
		properties.add("typeName");
		properties.add("promulgationDept");
		properties.add("promulgationDate");
		properties.add("content");

		return properties;
	}

	private List<Document> getSelectedDocument() {
		List<Document> docs = new ArrayList<Document>();

		for (Listitem item : this.listboxResult.getSelectedItems()) {
			Document doc = (Document) item.getAttribute("data");

			if (Validator.isNotNull(doc)) {
				docs.add(doc);
			}
		}

		return docs;
	}

	public void init() {
		refreshModel();
	}

	// event method
	public void onAfterRender$cbStatus() {
		advanceSearch();
	}

	public void onChanging$tbSearchDocType(InputEvent event) {
		String _key = event.getValue();

		boolean visible = Validator.isNotNull(_key);

		this.navbarSearch.setVisible(visible);
		this.togglerBack.setVisible(visible);
		this.navbar.setVisible(!visible);
		this.toggler.setVisible(!visible);

		if (visible) {
			this.onFilterDocType(_key);
		}
	}

	public void onClick$btnAddDoc() {
		Map map = new HashMap();

		map.put(Constants.PARENT_WINDOW, this.winDocument);
		map.put(Constants.OBJECT, null);

		Window win = (Window) Executions.createComponents(this.EDIT_PAGE, null, map);

		win.doModal();
	}

	public void onClick$btnAdvSearch() {
		advanceSearch();
	}

	public void onClick$btnBasicSearch() {
		this.isAdvance = false;

		this.refreshModel();

	}

	// Bandbox documentType
	public void onClick$btnClearDoc() {
		this.bbDocumentType.setValue(StringPool.BLANK);
		this.bbDocumentType.setAttribute(Constants.ID, null);

		this.btnClearDoc.setDisabled(true);
		this.btnClearDoc.setVisible(false);
	}

	public void onClick$btnDelDoc() {
		final List<Document> docs = this.getSelectedDocument();

		if (Validator.isNull(docs)) {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD), Labels.getLabel(LanguageKeys.ERROR),
					Messagebox.OK, Messagebox.EXCLAMATION, Messagebox.OK);
		} else {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
					Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE), Messagebox.OK | Messagebox.CANCEL,
					Messagebox.QUESTION, Messagebox.OK, new EventListener<Event>() {

						@Override
						public void onEvent(Event e) throws Exception {
							if (Messagebox.ON_OK.equals(e.getName())) {
								try {
									DocumentController.this.documentService.deleteAll(docs);

									ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_DELETE_SUCCESS);

									refreshModel();

								} catch (Exception ex) {
									_log.error(ex.getMessage(), ex);

									Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_DELETE_FAIL));
								}
							}
						}
					});
		}
	}

	public void onClick$btnDocTypeManager() {
		Map map = new HashMap();

		map.put(Constants.PARENT_WINDOW, this.winDocument);

		Window win = (Window) Executions.createComponents(this.DOCUMENT_TYPE_PAGE, null, map);

		win.doModal();
	}

	public void onClick$btnEnableAdvSearch() {
		this.advanceSearchPopup.open(this.btnEnableAdvSearch, ZkKeys.OVERLAP_END);

		this.txtDocumentNumber.setFocus(true);

		this.isAdvance = true;
	}

	public void onClick$btnExport() {
		try {
			List<Object[]> headerInfors = getHeaderInfors();
			List<String> properties = getProperties();

			List<Document> datas = getListExport();

			if (!datas.isEmpty()) {
				String title = Labels.getLabel(LanguageKeys.MENU_ITEM_MANAGER_DOCUMENT).toUpperCase();

				ExcelUtil excelUtil = new ExcelUtil<Document>();

				excelUtil.toSingleSheetXlsx("danh_sach_cong_van", title, headerInfors, properties, datas);
			} else {
				Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_NO_RECORD_EXPORT),
						Labels.getLabel(LanguageKeys.ERROR), Messagebox.OK, Messagebox.ERROR);
			}

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	public void onClick$toggler() {
		Include include = (Include) this.winDocument.getParent();

		if (this.navbar.isCollapsed()) {
			this.sidebar.setSclass("sidebar");
			this.navbar.setCollapsed(false);
			this.toggler.setIconSclass("z-icon-angle-double-left");
			include.setSclass("bodylayout-max");

			this.tbSearchDocType.setVisible(true);
		} else {
			this.sidebar.setSclass("sidebar sidebar-min");
			this.navbar.setCollapsed(true);
			this.toggler.setIconSclass("z-icon-angle-double-right");
			include.setSclass("bodylayout-min");

			this.tbSearchDocType.setVisible(false);
		}
		// Force the hlayout contains sidebar to recalculate its size
		Clients.resize(this.sidebar.getRoot().query("#main"));
	}

	public void onClick$togglerBack() {
		this.tbSearchDocType.setValue(StringPool.BLANK);

		this.navbarSearch.setVisible(false);
		this.togglerBack.setVisible(false);
		this.navbar.setVisible(true);
		this.toggler.setVisible(true);
	}

	public void onCreate$cbDepartment() {
		ListModel dictModel = new SimpleListModel(this.documentService.getDepartment());
		this.cbDepartment.setModel(dictModel);
	}

	public void onCreate$navbar() {
		List<DocumentType> roots = this.docTypeMap.get(null);

		Collections.sort(roots);

		for (DocumentType root : roots) {
			if (Validator.isNull(root)) {
				continue;
			}

			createDocTypeNav(root, this.navbar);
		}
	}

	public void onCreate$navbarSearch() {
		this.docTypes = this.documentTypeService.getAllDocumentType();
	}

	public void onDelete(Event event) {
		final Document document = (Document) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, Messagebox.OK, new EventListener<Event>() {

					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								DocumentController.this.documentService.delete(document);

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_DELETE_SUCCESS);

								refreshModel();

							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_DELETE_FAIL));
							}
						}
					}
				});
	}

	public void onFilterDocType(String _key) {
		this.navbarSearch.getChildren().clear();

		for (DocumentType docType : this.docTypes) {
			if (docType.getTypeName().toLowerCase().contains(_key.toLowerCase())) {
				final Navitem navItem = new Navitem();

				navItem.setLabel(docType.getTypeName());

				if (Validator.isNotNull(docType.getIcon())) {
					navItem.setIconSclass(docType.getIcon());
				} else {
					navItem.setIconSclass("z-icon-angle-double-right");
				}

				navItem.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event t) throws Exception {
						navItem.setSelected(true);
					}
				});

				navItem.addForward(Events.ON_CLICK, this.winDocument, ZkKeys.ON_LOAD_DATA,
						new Object[] { navItem, docType });

				this.navbarSearch.appendChild(navItem);
			}
		}

		Clients.evalJavaScript("hightLight('.search-nav','" + _key + "')");
	}

	public void onHightLight(String keyword) {
		Clients.evalJavaScript("hightLight('.search-result-grid .z-listbox-body','" + keyword + "')");
	}

	public void onLoadData(Event event) {
		Object[] ob = (Object[]) event.getData();

		Component comp = (Component) ob[0];
		DocumentType docType = (DocumentType) ob[1];

		if (comp instanceof Nav) {
			Nav nav = (Nav) comp;

			if (!nav.isOpen()) {
				return;
			} else if (this.selectedNavitem != null) {
				this.selectedNavitem.setSelected(false);
			}
		} else if (comp instanceof Navitem) {
			this.selectedNavitem = (Navitem) comp;
		}

		List<Long> docTypeIds = new ArrayList<Long>();

		getListDocumentTypeChild(docType, docTypeIds);

		this.paramMap.put("docTypeIds", docTypeIds);

		ListModel model = new ManagerDocumentListModel(this.listboxResult.getPageSize(), StringPool.BLANK, true,
				docTypeIds, true, this.documentService);

		this.listboxResult.setModel(model);

		this.listboxResult.setItemRenderer(new ListboxDocumentRender(this.winDocument, model, this.documentService));

		this.listboxResult.setMultiple(true);
	}

	public void onLoadDataCRUD(Event event) {
		refreshModel();
	}

	public void onLoadPage(Event event) {
		Component comp = this.winDocument.getParent();

		if (comp instanceof Include) {
			Include inc = (Include) comp;

			String src = inc.getSrc();

			inc.setSrc(null);
			inc.setSrc(src);
		}
	}

	public void onOK$advanceSearchPopup() {
		advanceSearch();
	}

	public void onOK$tbKeyword() {
		this.isAdvance = false;

		this.refreshModel();
	}

	public void onOK$winDocument() {
		this.refreshModel();
	}

	public void onOpen$bbDocumentType() {
		if (this.bbDocumentType.isOpen() && Validator.isNull(this.icDocumentType.getSrc())) {
			this.icDocumentType.setAttribute("bandbox", this.bbDocumentType);
			this.icDocumentType.setAttribute("btnclear", this.btnClearDoc);

			this.icDocumentType.setSrc(Constants.TREE_DOCUMENT_TYPE_PAGE);
		}
	}

	public void refreshModel() {
		this.navbar.clearSelection();

		if (this.isAdvance) {
			this.advanceSearch();
		} else {
			this.basicSearch();
		}
	}
}
