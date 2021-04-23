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

import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
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
 * @author hungnt81
 */
public class DocumentController extends BasicController<Hlayout>
        implements Serializable {

    private Hlayout winDocument;
    
    private Navbar navbar;
    private Navbar navbarSearch;
    
    private Div sidebar;
    
    private A toggler;
    private A togglerBack;
    private A btnClearDoc;
    
    private Textbox tbSearchDocType;
    
    private Listbox listboxResult;
    
    private Navitem selectedNavitem;
    
    private Button btnEnableAdvSearch;
    
    private Popup advanceSearchPopup;
    
    private Textbox tbKeyword;
    private Textbox txtDocumentContent;
    private Textbox txtDocumentNumber;
    private Bandbox bbDocumentType;
    private Include icDocumentType;
    private Datebox dbFromDate;
    private Datebox dbToDate;
    private Combobox cbDepartment;
    
    private Map<Long, List<DocumentType>> docTypeMap;
    private Map<String, Object> paramMap = new HashMap<String, Object>();
    
    private List<DocumentType> docTypes;
    //
    private boolean isAdvance;

    @Override
    public void doBeforeComposeChildren(Hlayout comp) throws Exception {
        super.doBeforeComposeChildren(comp); //To change body of generated methods, choose Tools | Templates.
        
        this.winDocument = comp;
        
        ServletContext context = Sessions.getCurrent().getWebApp().getServletContext();
        
        docTypeMap = documentTypeService.getDocTypeMap(context);
    }
    
    @Override
    public void doAfterCompose(Hlayout comp) throws Exception {
        super.doAfterCompose(comp);

        this.winDocument = comp;

        this.init();
    }

    public void init(){
        refreshModel();
    }

    public void onCreate$navbar() {
        List<DocumentType> roots = docTypeMap.get(null);
        
        Collections.sort(roots);

        for (DocumentType root : roots) {
            if (Validator.isNull(root)) {
                continue;
            }

            createDocTypeNav(root, navbar);
        }
    }
    
    public void onCreate$navbarSearch() {
        docTypes = documentTypeService.getAllDocumentType();
    }
    
    public void onFilterDocType(String _key) {
        navbarSearch.getChildren().clear();

        for (DocumentType docType : docTypes) {
            if (docType.getTypeName().toLowerCase().contains(_key.toLowerCase())) {
                final Navitem navItem = new Navitem();

                navItem.setLabel(docType.getTypeName());

                if (Validator.isNotNull(docType.getIcon())) {
                    navItem.setIconSclass(docType.getIcon());
                } else {
                    navItem.setIconSclass("z-icon-angle-double-right");
                }

                navItem.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

                    public void onEvent(Event t) throws Exception {
                        navItem.setSelected(true);
                    }
                });

                navItem.addForward(Events.ON_CLICK, winDocument, ZkKeys.ON_LOAD_DATA, 
                    new Object[]{navItem, docType});
                
                navbarSearch.appendChild(navItem);
            }
        }
        
        Clients.evalJavaScript("hightLight('.search-nav','"+_key+"')");
    }
    
    private void createDocTypeNav(DocumentType docType, Component parentNode){
        List<DocumentType> childs = docTypeMap.get(docType.getDocumentTypeId());
        
        if(Validator.isNull(childs)){
            final Navitem navItem = new Navitem();
            
            navItem.setLabel(docType.getTypeName());
            
            if(Validator.isNotNull(docType.getIcon())){
                navItem.setIconSclass(docType.getIcon());
            } else {
                navItem.setIconSclass("z-icon-angle-double-right");
            }
            
            navItem.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

                public void onEvent(Event t) throws Exception {
                    navItem.setSelected(true);
                }
            });
            
            navItem.addForward(Events.ON_CLICK, winDocument, ZkKeys.ON_LOAD_DATA, 
                    new Object[]{navItem, docType});
            
            parentNode.appendChild(navItem);
        } else {
            Collections.sort(childs);
            
            Nav nav = new Nav(docType.getTypeName());
            
            if(Validator.isNotNull(docType.getIcon())){
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
            
            nav.addForward(Events.ON_OPEN, winDocument, ZkKeys.ON_LOAD_DATA, 
                    new Object[]{nav, docType});
            
            parentNode.appendChild(nav);
        }
    }
    
    public void onClick$toggler() {
        Include include = (Include) winDocument.getParent();
        
        if (navbar.isCollapsed()) {
            sidebar.setSclass("sidebar");
            navbar.setCollapsed(false);
            toggler.setIconSclass("z-icon-angle-double-left");
            include.setSclass("bodylayout-max");
            
            tbSearchDocType.setVisible(true);
        } else {
            sidebar.setSclass("sidebar sidebar-min");
            navbar.setCollapsed(true);
            toggler.setIconSclass("z-icon-angle-double-right");
            include.setSclass("bodylayout-min");
            
            tbSearchDocType.setVisible(false);
        }
        // Force the hlayout contains sidebar to recalculate its size
        Clients.resize(sidebar.getRoot().query("#main"));
    }
    
    public void onChanging$tbSearchDocType(InputEvent event){
        String _key = event.getValue();
        
        boolean visible = Validator.isNotNull(_key);
        
        navbarSearch.setVisible(visible);
        togglerBack.setVisible(visible);
        navbar.setVisible(!visible);
        toggler.setVisible(!visible);
        
        if(visible){
            this.onFilterDocType(_key);
        }
    }
    
    public void onClick$togglerBack(){
        tbSearchDocType.setValue(StringPool.BLANK);
        
        navbarSearch.setVisible(false);
        togglerBack.setVisible(false);
        navbar.setVisible(true);
        toggler.setVisible(true);
    }
    
    public void onLoadData(Event event) {
        Object[] ob = (Object[]) event.getData();
        
        Component comp = (Component) ob[0];
        DocumentType docType = (DocumentType) ob[1];

        if (comp instanceof Nav) {
            Nav nav = (Nav) comp;
            
            if(!nav.isOpen()){
                return;
            } else if (selectedNavitem!=null){
                selectedNavitem.setSelected(false);
            }
        } else if(comp instanceof Navitem){
            selectedNavitem = (Navitem) comp;
        }
        
        List<Long> docTypeIds = new ArrayList<Long>();

        getListDocumentTypeChild(docType, docTypeIds);
        
        paramMap.put("docTypeIds", docTypeIds);
        
        ListModel model = new ManagerDocumentListModel(listboxResult.getPageSize(), 
                StringPool.BLANK, true, docTypeIds, true, documentService);
        
        listboxResult.setModel(model);
        
        listboxResult.setItemRenderer(
                new ListboxDocumentRender(winDocument, model, documentService));

        listboxResult.setMultiple(true);
    }
    
    public List<Long> getListDocumentTypeChild(DocumentType docParent, 
            List<Long> docTypeList) {
        if(Validator.isNull(docParent)){
            return docTypeList;
        }

        List<DocumentType> childs = docTypeMap.get(docParent.getDocumentTypeId());

        docTypeList.add(docParent.getDocumentTypeId());
        
        if (Validator.isNotNull(childs)) {
            for (DocumentType documentType : childs) {
                getListDocumentTypeChild(documentType, docTypeList);
            }
        }
        
        return docTypeList;
    }
    
    public void onOK$winDocument() {
        this.refreshModel();
    }
    
    public void onOK$tbKeyword() {
        isAdvance = false;
        
        this.refreshModel();
    }
    
    public void onClick$btnBasicSearch() {
        isAdvance = false;
        
        this.refreshModel();
        
    }
    
    public void onClick$btnEnableAdvSearch() {
        advanceSearchPopup.open(btnEnableAdvSearch, ZkKeys.OVERLAP_END);
        
        txtDocumentNumber.setFocus(true);
        
        isAdvance = true;
    }
    
    public void onClick$btnAdvSearch(){
        advanceSearch();
    }
    
    public void onOK$advanceSearchPopup(){   
        advanceSearch();
    }

    public void onClick$btnDelDoc() {
        final List<Document> docs = this.getSelectedDocument();

        if (Validator.isNull(docs)) {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD),
                    Labels.getLabel(LanguageKeys.ERROR), Messagebox.OK,
                    Messagebox.EXCLAMATION, Messagebox.OK);
        } else {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
                    Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE),
                    Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, 
                    Messagebox.OK, new EventListener() {

                        public void onEvent(Event e) throws Exception {
                            if (Messagebox.ON_OK.equals(e.getName())) {
                                try {
                                    documentService.deleteAll(docs);

                                    ComponentUtil.createSuccessMessageBox(
                                            LanguageKeys.MESSAGE_DELETE_SUCCESS);

                                    refreshModel();

                                } catch (Exception ex) {
                                    _log.error(ex.getMessage(), ex);

                                    Messagebox.show(Labels.getLabel(
                                            LanguageKeys.MESSAGE_DELETE_FAIL));
                                }
                            }
                        }
                    });
        }
    }

    private List<Document> getSelectedDocument(){
        List<Document> docs = new ArrayList<Document>();

        for (Listitem item : listboxResult.getSelectedItems()) {
            Document doc = (Document) item.getAttribute("data");

            if(Validator.isNotNull(doc)){
                docs.add(doc);
            }
        }

        return docs;
    }
    
    public void refreshModel() {
        navbar.clearSelection();
        
        if (isAdvance) {
            this.advanceSearch();
        } else {
            this.basicSearch();
        }
    }
    
    public void onClick$btnAddDoc() {
        Map map = new HashMap();
        
        map.put(Constants.PARENT_WINDOW, winDocument);
        map.put(Constants.OBJECT, null);

        Window win = (Window) Executions.createComponents(EDIT_PAGE, null, map);
        
        win.doModal();
    }

    public void onDelete(Event event) {
        final Document document = (Document) event.getData();

        Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
                Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE),
                Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, 
                Messagebox.OK, new EventListener() {

                    public void onEvent(Event e) throws Exception {
                        if (Messagebox.ON_OK.equals(e.getName())) {
                            try {
                                documentService.delete(document);

                                ComponentUtil.createSuccessMessageBox(
                                        LanguageKeys.MESSAGE_DELETE_SUCCESS);

                                refreshModel();

                            } catch (Exception ex) {
                                _log.error(ex.getMessage(), ex);

                                Messagebox.show(Labels.getLabel(
                                        LanguageKeys.MESSAGE_DELETE_FAIL));
                            }
                        }
                    }
                });
    }
    
    public void onClick$btnDocTypeManager() {
        Map map = new HashMap();
        
        map.put(Constants.PARENT_WINDOW, winDocument);

        Window win = (Window) Executions.createComponents(DOCUMENT_TYPE_PAGE, 
                null, map);
        
        win.doModal();
    }
    
    public void onClick$btnExport(){
        try {
            List<Object[]> headerInfors = getHeaderInfors();
            List<String> properties = getProperties();

            List<Document> datas = getListExport();

            if (!datas.isEmpty()) {
                String title = Labels.getLabel(LanguageKeys.MENU_ITEM_MANAGER_DOCUMENT).
                        toUpperCase();

                ExcelUtil excelUtil =
                        new ExcelUtil<Document>();

                excelUtil.toSingleSheetXlsx("danh_sach_cong_van",
                        title, headerInfors, properties, datas);
            } else {
                Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_NO_RECORD_EXPORT),
                        Labels.getLabel(LanguageKeys.ERROR), Messagebox.OK,
                        Messagebox.ERROR);
            }

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }
    
    public List<Object[]> getHeaderInfors() {
        List<Object[]> headerInfors = new ArrayList<Object[]>();

        headerInfors.add(new Object[]{
                    Labels.getLabel(LanguageKeys.COLUMN_NUMBER), 2000});
        headerInfors.add(new Object[]{
                    Labels.getLabel(LanguageKeys.DOCUMENT_NUMBER), 8000});
        headerInfors.add(new Object[]{
                    Labels.getLabel(LanguageKeys.DOCUMENT_TYPE), 8000});
        headerInfors.add(new Object[]{
                    Labels.getLabel(LanguageKeys.PUBLISHER), 8000});
        headerInfors.add(new Object[]{
                    Labels.getLabel(LanguageKeys.PUBLIC_DATE), 9000});
        headerInfors.add(new Object[]{
                    Labels.getLabel(LanguageKeys.CONTENT), 18000});
        //

        return headerInfors;
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
    
    public List<Document> getListExport() throws Exception {
        List<Document> results = new ArrayList<Document>();

        try {
            List<Long> docTypeIds = (List<Long>) paramMap.get("docTypeIds");
            
            if(Validator.isNotNull(docTypeIds)){
                return documentService.getDocumentListByIdList(docTypeIds,
                        QueryUtil.GET_ALL, QueryUtil.GET_ALL, null, null);
            }
            
            if (isAdvance) {
                String documentContent = (String) paramMap.get("documentContent");
                String documentNumber = (String) paramMap.get("documentNumber");
                Long documentType = (Long) paramMap.get("documentType");
                String department = (String) paramMap.get("department");
                Date fromDate = (Date) paramMap.get("fromDate");
                Date toDate = (Date) paramMap.get("toDate");

                results = documentService.getDocumentListAdv(documentContent,
                            documentNumber, documentType, department, fromDate,
                            toDate, QueryUtil.GET_ALL, QueryUtil.GET_ALL, null, 
                            null);
            } else {
                String keyword = (String) paramMap.get("keyword");

                results = documentService.getDocumentListBasic(keyword,
                        QueryUtil.GET_ALL, QueryUtil.GET_ALL, null, null);
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return results;
    }

    public void onLoadDataCRUD(Event event) {        
        refreshModel();
    }

    public void onLoadPage(Event event){
        Component comp = winDocument.getParent();
        
        if(comp instanceof Include){
            Include inc = (Include) comp;
            
            String src = inc.getSrc();
            
            inc.setSrc(null);
            inc.setSrc(src);
        }
    }

    public void onCreate$cbDepartment() {
        ListModel dictModel = new SimpleListModel(documentService.getDepartment());
        cbDepartment.setModel(dictModel);
    }

    public void advanceSearch() {
        String documentContent = GetterUtil.getString(txtDocumentContent.getValue());
        String documentNumber = GetterUtil.getString(txtDocumentNumber.getValue());
        Long documentType = ComponentUtil.getBandboxValue(bbDocumentType);
        String department = GetterUtil.getString(cbDepartment.getValue());
        Date fromDate = dbFromDate.getValue();
        Date toDate = dbToDate.getValue();
        
        //create param map
        paramMap.put("documentContent", documentContent);
        paramMap.put("documentNumber", documentNumber);
        paramMap.put("documentType", documentType);
        paramMap.put("department", department);
        paramMap.put("fromDate", fromDate);
        paramMap.put("toDate", toDate);
        
        ListModel model = new ManagerDocumentListModel(listboxResult.getPageSize(),
                documentContent, documentNumber, documentType, department,
                fromDate, toDate, isAdvance, null, false, documentService);
        
        listboxResult.setModel(model);
        
        listboxResult.setItemRenderer(
                new ListboxDocumentRender(winDocument, model, documentService));
        
        listboxResult.setMultiple(true);
        
        paramMap.remove("docTypeIds");
    }

    public void basicSearch() {
        String keyword = GetterUtil.getString(tbKeyword.getValue());

        paramMap.put("keyword", keyword);
        
        ListModel model = new ManagerDocumentListModel(listboxResult.getPageSize(), 
                keyword, isAdvance, null, false, documentService);
        
        listboxResult.setModel(model);
        
        listboxResult.setItemRenderer(
                new ListboxDocumentRender(winDocument, model, documentService));

        listboxResult.setMultiple(true);
        
        paramMap.remove("docTypeIds");
        
        if(Validator.isNotNull(keyword)){
            this.onHightLight(keyword);
            
            listboxResult.addEventListener("onPaging", new EventListener<Event>() {
                
                public void onEvent(Event t) throws Exception {
                    String keyword = GetterUtil.getString(tbKeyword.getValue());
                    
                    if(!isAdvance && Validator.isNotNull(keyword)){
                        onHightLight(keyword);
                    }
                }
            });
        }
    }

    public void onHightLight(String keyword){
        Clients.evalJavaScript("hightLight('.search-result-grid .z-listbox-body','"+ keyword +"')");
    }
    
    //event method
    public void onAfterRender$cbStatus() {
        advanceSearch();
    }
    
    //Bandbox documentType
    public void onClick$btnClearDoc() {
        bbDocumentType.setValue(StringPool.BLANK);
        bbDocumentType.setAttribute(Constants.ID, null);
        
        btnClearDoc.setDisabled(true);
        btnClearDoc.setVisible(false);
    }
    
    public void onOpen$bbDocumentType(){
        if(bbDocumentType.isOpen() 
                && Validator.isNull(icDocumentType.getSrc())) {
            icDocumentType.setAttribute("bandbox", bbDocumentType);
            icDocumentType.setAttribute("btnclear", btnClearDoc);
            
            icDocumentType.setSrc(Constants.TREE_DOCUMENT_TYPE_PAGE);
        }
    }
    //get set service
    public DocumentService getDocumentService() {
        if (documentService == null) {
            documentService = (DocumentService)
                    SpringUtil.getBean("documentService");
            setDocumentService(documentService);
        }
        return documentService;
    }

    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    public DocumentTypeService getDocumentTypeService() {
        if (documentTypeService == null) {
            documentTypeService = (DocumentTypeService)
                    SpringUtil.getBean("documentTypeService");
            setDocumentTypeService(documentTypeService);
        }
        return documentTypeService;
    }

    public void setDocumentTypeService(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }

    private transient DocumentTypeService documentTypeService;
    private transient DocumentService documentService;
    
    private final String EDIT_PAGE = "/html/pages/manager_document/edit.zul";
    private final String DOCUMENT_TYPE_PAGE = "/html/pages/document_type/view.zul";
    
    private static final Logger _log =
            LogManager.getLogger(DocumentController.class);
}
