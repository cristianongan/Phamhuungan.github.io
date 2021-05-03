/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.model.render;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.evotek.qlns.model.Document;
import com.evotek.qlns.model.FileEntry;
import com.evotek.qlns.service.DocumentService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.DateUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;

/**
 *
 * @author LinhLH
 */
public class ListboxDocumentRender implements ListitemRenderer<Document> {

	private DocumentService _documentService;

	private ListModel _model;

	private Hlayout _window;

	public ListboxDocumentRender(Hlayout window, ListModel model, DocumentService documentService) {
		this._window = window;
		this._model = model;
		this._documentService = documentService;
	}

	private Map<String, Object> _createParameterMap(Document document, String title, int index) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Constants.Attr.PARENT_WINDOW, this._window);
		parameters.put(Constants.Attr.TITLE, title);
		parameters.put(Constants.Attr.OBJECT, document);
		parameters.put(Constants.Attr.MODEL, this._model);
		parameters.put(Constants.Attr.INDEX, index);

		return parameters;
	}

	private Listcell _getAction(Document document, int index) {

		Listcell action = new Listcell();

		Hlayout hlayout = new Hlayout();

		hlayout.setSpacing("0");

		hlayout.appendChild(ComponentUtil.createButton(null, Labels.getLabel(LanguageKeys.EDIT), Constants.Tooltip.EDIT,
				Events.ON_CLICK, Constants.Page.ManagerDocument.ADD_EDIT,
				_createParameterMap(document, Labels.getLabel(LanguageKeys.TITLE_EDIT_DOCUMENT), index),
				Constants.Zicon.PENCIL, Constants.Sclass.BLUE));
		hlayout.appendChild(
				ComponentUtil.createButton(this._window, Labels.getLabel(LanguageKeys.DELETE), Constants.Tooltip.DEL,
						Events.ON_CLICK, "onDelete", document, Constants.Zicon.TRASH_O, Constants.Sclass.RED));

		action.appendChild(hlayout);

		return action;
	}

	private Listcell _getFile(Document document) {
		Listcell file = new Listcell();

		Div divFile = new Div();

		List<FileEntry> fileEntrys = this._documentService.getListFileEntrys(document.getDocumentId());

		Component comp = ComponentUtil.createDownloadFileBox(fileEntrys);

		divFile.appendChild(comp);

		file.appendChild(divFile);

		return file;
	}

	@Override
	public void render(Listitem item, Document doc, int index) throws Exception {
		item.setAttribute("data", doc);

		item.appendChild(ComponentUtil.createListcell(StringPool.BLANK, Constants.Style.TEXT_ALIGN_CENTER));
		item.appendChild(ComponentUtil.createListcell(Integer.toString(index + 1), Constants.Style.TEXT_ALIGN_CENTER));
		item.appendChild(ComponentUtil.createListcell(doc.getDocumentNumber()));
		item.appendChild(ComponentUtil.createListcell(doc.getTypeName()));
		item.appendChild(ComponentUtil.createListcell(doc.getPromulgationDept()));
		item.appendChild(
				ComponentUtil.createListcell(GetterUtil.getDate(doc.getPromulgationDate(), DateUtil.SHORT_DATE_PATTERN),
						Constants.Style.TEXT_ALIGN_CENTER));

		item.appendChild(_getFile(doc));

		item.appendChild(ComponentUtil.createListcell(doc.getContent()));
		item.appendChild(_getAction(doc, index));
	}
}
