/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.A;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Bandpopup;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.evotek.qlns.event.OnUploadAttachmentListener;
import com.evotek.qlns.event.OpenDialogListener;
import com.evotek.qlns.event.PreviewFileListener;
import com.evotek.qlns.model.FileEntry;
import com.evotek.qlns.model.SimpleModel;
import com.evotek.qlns.model.render.DownloadFileGridRender;
import com.evotek.qlns.model.render.DownloadFileGridViewRender;
import com.evotek.qlns.model.render.OldFileGridRender;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;
import com.evotek.qlns.util.key.ZkKeys;

/**
 *
 * @author linhlh2
 */
public class ComponentUtil {

	

	public static void clear(Component comp) {
		if (comp.getChildren() != null) {
			comp.getChildren().clear();
		}
	}

	public static Listcell createAddRemoveButton(final Listbox listbox, final Listitem item, final Object object,
			final int index, boolean allowRemoveOld) {
		Listcell cell = new Listcell();

		Hbox hbox = new Hbox();

		Toolbarbutton btnAddRow = new Toolbarbutton();

		btnAddRow.setImage(Constants.Icon.ADD);
		btnAddRow.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event t) throws Exception {
				ListModelList listModelList = (ListModelList) listbox.getListModel();
				listModelList.add(item.getIndex() + 1, object);
			}
		});

		hbox.appendChild(btnAddRow);

		if (allowRemoveOld) {
			Toolbarbutton btnDeleteRow = new Toolbarbutton();

			btnDeleteRow.setImage(Constants.Icon.REMOVE);
			btnDeleteRow.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

				@Override
				public void onEvent(Event t) throws Exception {
					ListModelList listModelList = (ListModelList) listbox.getListModel();
					listModelList.remove(item.getIndex());

					if (listModelList.isEmpty()) {
						listModelList.add(index, object);
					}
				}
			});

			hbox.appendChild(btnDeleteRow);
		}
//        if (index == 0) { //khong xoa dong dau tien
//            btnDeleteRow.setVisible(false);
//        }

		cell.appendChild(hbox);

		return cell;
	}

	public static A createAIcon(String iconSclass) {
		A a = new A();

		a.setIconSclass(iconSclass);

		return a;
	}

	public static Button createAttachButton(Div divContainer, Div divFileList, String config) {
		Button button = new Button(Labels.getLabel(LanguageKeys.DOCUMENT_ATTACH));

		button.setUpload(config);
		button.setIconSclass(Constants.Zicon.PAPERCLIP);
		button.setSclass(Constants.BTN_SUCCESS);

		List<Media> medium = new ArrayList<Media>();

		button.addEventListener(Events.ON_UPLOAD, new OnUploadAttachmentListener(medium, divFileList));

		divContainer.setAttribute(Constants.DATA, medium);

		return button;
	}

	public static Div createAttachment(String config, List<FileEntry> oldFiles) {
		Div divContainer = new Div();
		Div divFileList = new Div();

		divContainer.setSclass("z-div-attach");

		divContainer.appendChild(createAttachButton(divContainer, divFileList, config));
		// Tao grid chua file cu da upload
		if (!oldFiles.isEmpty()) {
			List<FileEntry> deleteFiles = new ArrayList<FileEntry>();

			divContainer.appendChild(createOldFileGrid(oldFiles, deleteFiles));

			divContainer.setAttribute(Constants.OBJECT, deleteFiles);
		}

		divContainer.appendChild(divFileList);

		return divContainer;
	}

	public static Bandbox createBandbox(String value, boolean readOnly) {
		Bandbox bb = new Bandbox();

		if (Validator.isNotNull(value)) {
			bb.setValue(value);
		}

		bb.setReadonly(readOnly);
		bb.setMold(ZkKeys.ROUNDED);
		bb.setHflex(ZkKeys.HFLEX_1);

		return bb;
	}

	public static Bandpopup createBandpopup(String width) {
		Bandpopup bp = new Bandpopup();

		bp.setWidth(width);

		return bp;
	}

	public static Bandpopup createBandpopup(String width, String pageInclude) {
		Bandpopup bp = new Bandpopup();

		bp.setWidth(width);

		Include include = new Include(pageInclude);

		bp.appendChild(include);

		return bp;
	}

	public static Button createButton(Component parent, String label, String event, String url,
			Map<String, Object> parameter) {
		Button button = new Button();

		button.setTooltiptext(label);

		button.addEventListener(event, new OpenDialogListener(url, parent, parameter));

		return button;
	}

	public static Button createButton(Component parent, String label, String event, String url,
			Map<String, Object> parameter, String iconSclass, String sclass) {
		Button button = createButton(parent, label, event, url, parameter);

		button.setIconSclass(iconSclass);
		button.setSclass(sclass);

		return button;
	}

	public static Button createButton(Component parent, String label, String event, String eventForward, Object data) {
		Button button = new Button();

		button.setTooltiptext(label);

		button.addForward(event, parent, eventForward, data);

		return button;
	}

	public static Button createButton(Component parent, String label, String event, String eventForward, Object data,
			String iconSclass, String sclass) {
		Button button = createButton(parent, label, event, eventForward, data);

		button.setIconSclass(iconSclass);
		button.setSclass(sclass);

		return button;
	}

	public static Button createButton(Component parent, String label, String toolTipId, String event, String url,
			Map<String, Object> parameter) {
		Button button = new Button();

		if (Validator.isNotNull(toolTipId)) {
			button.setTooltip(toolTipId + ", position=after_center");
		} else {
			button.setTooltiptext(label);
		}

		button.addEventListener(event, new OpenDialogListener(url, parent, parameter));

		return button;
	}

	public static Button createButton(Component parent, String label, String toolTipId, String event, String url,
			Map<String, Object> parameter, String iconSclass, String sclass) {
		Button button = createButton(parent, label, toolTipId, event, url, parameter);

		button.setIconSclass(iconSclass);
		button.setSclass(sclass);

		return button;
	}

	public static Button createButton(Component parent, String label, String toolTipId, String event,
			String eventForward, Object data) {
		Button button = new Button();

		if (Validator.isNotNull(toolTipId)) {
			button.setTooltip(toolTipId + ", position=after_center");
		} else {
			button.setTooltiptext(label);
		}

		button.addForward(event, parent, eventForward, data);

		return button;
	}

	public static Button createButton(Component parent, String label, String toolTipId, String event,
			String eventForward, Object data, String iconSclass, String sclass) {
		Button button = createButton(parent, label, toolTipId, event, eventForward, data);

		button.setIconSclass(iconSclass);
		button.setSclass(sclass);

		return button;
	}

	public static Button createButton(String label, String iconSclass, String sclass) {
		Button button = new Button();

		button.setIconSclass(iconSclass);
		button.setSclass(sclass);
		button.setTooltiptext(label);

		return button;
	}

	public static Button createButton(String label, String toolTipId, String iconSclass, String sclass) {
		Button button = new Button();

		button.setIconSclass(iconSclass);
		button.setSclass(sclass);

		if (Validator.isNotNull(toolTipId)) {
			button.setTooltip(toolTipId + ", position=after_center");
		} else {
			button.setTooltiptext(label);
		}

		return button;
	}

	public static Cell createCell(String label, String style) {
		Cell cell = new Cell();

		cell.appendChild(new Label(label));

		cell.setStyle(/* Constants.STYLE_BORDER_NONE + */style);

		return cell;
	}

	public static Checkbox createCheckbox(Long value) {
		Checkbox cb = new Checkbox();

		cb.setChecked(Values.STATUS_DEACTIVE.equals(value));

		return cb;
	}

	public static Checkbox createCheckbox(String id, boolean checked) {
		Checkbox cb = new Checkbox();

		cb.setId(id);
		cb.setChecked(checked);

		return cb;
	}

	public static Column createColumn(String width) {
		return createColumn(StringPool.BLANK, width);
	}

	public static Column createColumn(String header, String width) {
		Column column = new Column(header);

		if (Validator.isNotNull(width)) {
			column.setWidth(width);
		}

		return column;
	}

	public static Columns createColumns(String[] widths) {
		Columns columns = new Columns();

		for (int i = 0; i < widths.length; i++) {
			columns.appendChild(createColumn(widths[i]));
		}

		return columns;
	}

	public static Combobox createCombobox() {
		Combobox cb = new Combobox();

		cb.setMold(ZkKeys.ROUNDED);
		cb.setHflex(ZkKeys.HFLEX_1);

		cb.setReadonly(true);

		return cb;
	}

	public static Combobox createCombobox(String[] values, boolean defaultValue) {
		Combobox cb = createCombobox();

		if (defaultValue) {
			cb.appendChild(createComboitem(StringPool.BLANK, Labels.getLabel(LanguageKeys.OPTION)));
		}

		for (int i = 0; i < values.length; i++) {
			Comboitem item = createComboitem(Long.valueOf(i), values[i]);

			item.setAttribute(Constants.DATA, values[i]);

			cb.appendChild(item);

			cb.setSelectedIndex(0);
		}

		cb.setMold(ZkKeys.ROUNDED);
		cb.setHflex(ZkKeys.HFLEX_1);

		cb.setReadonly(true);

		return cb;
	}

	public static Combobox createComboboxNoneHflex() {
		Combobox cb = new Combobox();

		cb.setMold(ZkKeys.ROUNDED);

		cb.setReadonly(true);

		return cb;
	}

	public static Comboitem createComboitem(Long value, String label) {
		Comboitem cbItem = new Comboitem();

		cbItem.setValue(value);
		cbItem.setLabel(label);

		return cbItem;
	}

	public static Comboitem createComboitem(String value) {
		return createComboitem(value, value);
	}

	// public static Div createAttachment(String config, List<FileEntry> oldFiles) {
//        Div divContainer = new Div();
//        Div divFileList = new Div();
//
//        divContainer.appendChild(
//                createAttachButton(divContainer, divFileList, config));
//        //Tao grid chua file cu da upload
//        if (!oldFiles.isEmpty()) {
//            List<FileEntry> deleteFiles = new ArrayList<FileEntry>();
//
//            divContainer.appendChild(createOldFileGrid(oldFiles, deleteFiles));
//
//            divContainer.setAttribute(Constants.OBJECT, deleteFiles);
//        }
//
//        divContainer.appendChild(divFileList);
//
//        return divContainer;
//    }
	public static Comboitem createComboitem(String value, String label) {
		Comboitem cbItem = new Comboitem();

		cbItem.setValue(value);
		cbItem.setLabel(label);

		return cbItem;
	}

	public static Component createComponent(Long type, String id) {
		Component comp = null;

		switch (type.intValue()) {
		case 4:
//                comp = createDatebox(DateUtil.SHORT_DATE_PATTERN);
//
//                break;

		case 5:
			comp = createDatebox(DateUtil.SHORT_DATE_PATTERN);

//                ((Datebox) comp).setReadonly(true);

			break;

		default:
			comp = createTextbox();

			break;
		}

		comp.setId(id);

		return comp;
	}

	public static Component createComponent(Long type, String id, Map item) {
		Component comp = null;

		switch (type.intValue()) {
		case 4:
			comp = createDatebox(DateUtil.SHORT_DATE_PATTERN);

			if (Validator.isNotNull(item)) {
				((Datebox) comp).setValue((Date) item.get(id));
			}

			break;

		case 5:
			comp = createDatebox(DateUtil.LONG_DATE_PATTERN);

			if (Validator.isNotNull(item)) {
				((Datebox) comp).setValue((Date) item.get(id));
			}
//                ((Datebox) comp).setReadonly(true);

			break;

		default:
			comp = createTextbox();

			((Textbox) comp).setMultiline(true);

			if (Validator.isNotNull(item)) {
				((Textbox) comp).setValue((String) item.get(id));
				((Textbox) comp).setTooltiptext((String) item.get(id));
			}

			break;
		}

		comp.setId(id);

		return comp;
	}

	public static Datebox createDatebox() {
		Datebox db = new Datebox();

		db.setMold(ZkKeys.ROUNDED);
		db.setHflex(ZkKeys.HFLEX_1);

		return db;
	}

	public static Datebox createDatebox(String format) {
		Datebox db = createDatebox();

		db.setFormat(format);

		return db;
	}

	public static Div createDiv(String cssId) {
		Div div = new Div();

		div.setId(cssId);

		return div;
	}

	public static Div createDiv(String label, String width) {
		Div div = new Div();

		div.appendChild(createLabel(label));

		div.setStyle(width);

		return div;
	}

	public static Vlayout createDownloadFileBox(Collection<FileEntry> files) {
		Vlayout vbox = new Vlayout();

		if (Validator.isNull(files)) {
			Grid grid = ComponentUtil.createGrid(Labels.getLabel(LanguageKeys.MESSAGE_NO_DOCUMENT_WAS_FOUND),
					Constants.SCLASS_NO_STYLE);

			vbox.appendChild(grid);
		} else {
			for (FileEntry file : files) {
				A rm = new A(file.getName());

				rm.addEventListener(Events.ON_CLICK, new PreviewFileListener(file));

				vbox.appendChild(rm);
			}
		}

		return vbox;
	}

	public static Div createDownloadFileGrid(List<FileEntry> files) {
		Div div = new Div();

		Grid grid = ComponentUtil.createGrid(Labels.getLabel(LanguageKeys.MESSAGE_NO_DOCUMENT_WAS_FOUND),
				Constants.SCLASS_NO_STYLE);

		grid.setHflex("1");
		grid.appendChild(ComponentUtil.createColumns(new String[] { "", "38px" }));

		grid.setModel(new ListModelList<FileEntry>(files));

		grid.setRowRenderer(new DownloadFileGridViewRender());

		div.appendChild(grid);

		return div;
	}

	// public static Div createOldFileGrid(List<FileEntry> oldFiles,
//            List<FileEntry> deleteFiles) {
//        Div div = new Div();
//
//        Grid grid = ComponentUtil.createGrid(StringPool.BLANK,
//                Constants.SCLASS_NO_STYLE);
//
//        grid.appendChild(ComponentUtil.createColumns(
//                new String[]{"90%", "10%"}));
//
//        grid.setModel(new ListModelList<FileEntry>(oldFiles));
//
//        grid.setRowRenderer(new OldFileGridRender(deleteFiles));
//
//        div.appendChild(grid);
//
//        return div;
//    }
	public static Div createDownloadFileGrid(List<FileEntry> files, Window window) {
		Div div = new Div();

		Grid grid = ComponentUtil.createGrid(Labels.getLabel(LanguageKeys.MESSAGE_NO_DOCUMENT_WAS_FOUND),
				Constants.SCLASS_NO_STYLE);

		grid.appendChild(ComponentUtil.createColumns(new String[] { "80%", "10%", "10%" }));

		grid.setModel(new ListModelList<FileEntry>(files));

		grid.setRowRenderer(new DownloadFileGridRender(window));

		div.appendChild(grid);

		return div;
	}

	public static void createErrorMessageBox(String label) {
		createMessageBox(Constants.Message.MSG_ERROR, label);
	}

	public static void createErrorMessageBox(String label, Object[] args) {
		createMessageBox(Constants.Message.MSG_ERROR, label, args);
	}

	public static Grid createGrid(String emptyString) {
		Grid grid = new Grid();

		if (Validator.isNotNull(emptyString)) {
			grid.setEmptyMessage(emptyString);
		}

		grid.setStyle(Constants.STYLE_BORDER_NONE);

		grid.appendChild(new Rows());

		return grid;
	}

	public static Grid createGrid(String emptyString, String sClass) {
		Grid grid = createGrid(emptyString);

		grid.setSclass(sClass);

		return grid;
	}

	public static Groupbox createGroupbox(String title, boolean border) {
		Groupbox gb = new Groupbox();

		gb.setMold(ZkKeys.MOLD_3D);
		gb.setTitle(title);

		if (!border) {
			gb.setStyle(Constants.STYLE_BORDER_NONE);
		}

		return gb;
	}

	public static void createInforMessageBox(String label) {
		createMessageBox(Constants.Message.MSG_INFORMATION, label);
	}

	public static void createInforMessageBox(String label, Object[] args) {
		createMessageBox(Constants.Message.MSG_INFORMATION, label, args);
	}

	public static Label createLabel(String value) {
		Label label = new Label(value);

		label.setHflex(ZkKeys.HFLEX_1);

		return label;
	}

	public static Label createLabel(String value, boolean multiLine) {
		Label label = new Label(value);

		label.setMultiline(multiLine);

		return label;
	}

	public static Label createLabel(String value, String style) {
		Label label = new Label(value);

		label.setStyle(style);

		return label;
	}

	public static Label createLabel(String value, String style, boolean multiLine) {
		Label label = createLabel(value, multiLine);

		label.setStyle(style);

		return label;
	}

	public static Listbox createListbox(String mold, boolean checkmark, boolean multiple, Integer pageSize, String id) {
		Listbox listbox = new Listbox();

		listbox.setCheckmark(checkmark);
		listbox.setMultiple(multiple);
		if (!"".equals(mold)) {
			listbox.setMold(mold);
			listbox.setPageSize(pageSize);
		}
		listbox.setId(id);
		listbox.setEmptyMessage(Labels.getLabel(LanguageKeys.NO_RECORD_FOUNND));

		return listbox;
	}

	public static Listcell createListcell(Component comp) {
		Listcell cell = new Listcell();

		cell.appendChild(comp);

		return cell;
	}

	public static Listcell createListcell(Component parent, String label, String event, String url,
			Map<String, Object> parameter) {
		Listcell cell = new Listcell();

		Label lb = new Label(label);

		lb.setZclass(Constants.CLASS_LINK_BUTTON);

		cell.appendChild(lb);

		cell.addEventListener(event, new OpenDialogListener(url, parent, parameter));

		return cell;
	}

	public static Listcell createListcell(String label) {
		Listcell cell = new Listcell();

		cell.appendChild(new Label(label));

		return cell;
	}

//    public static Button createButton(Component parent, String label,
//            String event, String url, Map<String, Object> parameter) {
//        Button button = new Button(label);
//
//        button.addEventListener(event, new OpenDialogListener(
//                url, parent, parameter));
//
//        return button;
//    }

	public static Listcell createListcell(String label, boolean multiline) {
		Label lb = createLabel(label, multiline);

		Listcell cell = new Listcell();

		cell.appendChild(lb);
		cell.setTooltiptext(label);
		cell.setStyle(Constants.STYLE_BORDER_NONE);

		return cell;
	}

	public static Listcell createListcell(String label, String style) {
		Listcell cell = new Listcell(label);

		cell.setStyle(style);

		return cell;
	}

	public static Listcell createListcell(String label, String style, boolean multiline) {
		Label lb = createLabel(label, style, multiline);

		Listcell cell = new Listcell();

		cell.appendChild(lb);
		cell.setStyle(Constants.STYLE_BORDER_NONE);
		return cell;
	}

	public static Listcell createListcell(String label, String tooltip, String style) {
		Listcell cell = new Listcell();

		Label label1 = new Label(label);
		label1.setTooltiptext(tooltip);
		label1.setStyle(style);
		cell.appendChild(label1);
		return cell;
	}

	public static Listhead createListhead(String[] widths) {
		Listhead listhead = new Listhead();

		for (int i = 0; i < widths.length; i++) {
			listhead.appendChild(createListheader(widths[i]));
		}

		return listhead;
	}

	public static Listheader createListheader(String width) {
		return createListheader(StringPool.BLANK, width);
	}

	public static Listheader createListheader(String header, String width) {
		Listheader listheader = new Listheader(header);

		listheader.setStyle(Constants.STYLE_COLUMN_MULTILINE);

		if (Validator.isNotNull(width)) {
			listheader.setWidth(width);
		}

		return listheader;
	}

	public static Listheader createListheader(String header, String width, String columnSort) {
		Listheader listheader = createListheader(header, width);

		listheader.setSort("auto(" + columnSort + ")");

		return listheader;
	}

	public static Menuitem createMenuitem(Component parent, String label, String event, String url,
			Map<String, Object> parameter) {
		Menuitem menuitem = new Menuitem(label);

		menuitem.addEventListener(event, new OpenDialogListener(url, parent, parameter));

		return menuitem;
	}

	public static Menuitem createMenuitem(Component parent, String label, String event, String url,
			Map<String, Object> parameter, String imageUrl) {
		Menuitem menuitem = createMenuitem(parent, label, event, url, parameter);

		menuitem.setImage(imageUrl);

		return menuitem;
	}

	public static Menuitem createMenuitem(Component parent, String label, String event, String eventForward,
			Object data) {
		Menuitem menuitem = new Menuitem(label);

		menuitem.addForward(event, parent, eventForward, data);

		return menuitem;
	}

	public static Menuitem createMenuitem(Component parent, String label, String event, String eventForward,
			Object data, String imageUrl) {
		Menuitem menuitem = createMenuitem(parent, label, event, eventForward, data);

		menuitem.setImage(imageUrl);

		return menuitem;
	}

	public static Menuitem createMenuitem(Component parent, String label, String event, String eventForward,
			Object data, String iconSclass, String sclass) {
		Menuitem menuitem = createMenuitem(parent, label, event, eventForward, data);

		menuitem.setIconSclass(iconSclass);
		menuitem.setSclass(sclass);

		return menuitem;
	}

	public static void createMessageBox(String type, String label) {
		HashMap<String, Object> mapNotice = new HashMap<String, Object>();

		mapNotice.put("type", type);
		mapNotice.put("notice", Labels.getLabel(label));

		Executions.createComponents(Constants.Page.Common.MESSAGE_BOX_PAGE, null, mapNotice);
	}

	public static void createMessageBox(String type, String label, Object[] args) {
		HashMap<String, Object> mapNotice = new HashMap<String, Object>();

		mapNotice.put("type", type);
		mapNotice.put("notice", Labels.getLabel(label, args));

		Executions.createComponents(Constants.Page.Common.MESSAGE_BOX_PAGE, null, mapNotice);
	}

	public static Div createOldFileGrid(List<FileEntry> oldFiles, List<FileEntry> deleteFiles) {
		Div div = new Div();

		Grid grid = ComponentUtil.createGrid(StringPool.BLANK, Constants.SCLASS_NO_STYLE);

		grid.appendChild(ComponentUtil.createColumns(new String[] { "88%", "12%" }));

		grid.setModel(new ListModelList<FileEntry>(oldFiles));

		grid.setRowRenderer(new OldFileGridRender(deleteFiles));

		div.appendChild(grid);

		return div;
	}

	public static Listcell createRemoveButton(final Listbox listbox, final Listitem item) {
		Listcell cell = new Listcell();

		Toolbarbutton btnDeleteRow = new Toolbarbutton();

		btnDeleteRow.setImage(Constants.Icon.REMOVE);
		btnDeleteRow.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event t) throws Exception {
				ListModelList listModelList = (ListModelList) listbox.getListModel();
				listModelList.remove(item.getIndex());
			}
		});

		return cell;
	}

	public static Div createSimpleGrid(List<String> values) {
		Div div = new Div();

		for (String value : values) {
			Div child = new Div();

			child.appendChild(new Label(value));

			div.appendChild(child);
		}

		return div;
	}

	public static Row createSimpleRow(String label) {
		Row row = new Row();

		row.appendChild(new Label(label));

		return row;
	}

	public static Listcell createSpinnerListcell(Long value, String id) {
		return createSpinnerListcell(value, id, StringPool.BLANK);
	}

	public static Listcell createSpinnerListcell(Long value, String id, String style) {
		Listcell cell = new Listcell();

		Spinner sp = new Spinner();

		if (value != null) {
			sp.setValue(value.intValue());
		}

		if (Validator.isNotNull(style)) {
			sp.setStyle(style);
		}

		sp.setWidth("98%");
		sp.setId(id);

		cell.appendChild(sp);

		return cell;
	}

	public static Image createStatusImage(Long status) {
		if (Validator.isNull(status)) {
			return null;
		}

		if (status.equals(Values.STATUS_ACTIVE)) {
			return new Image(Constants.Icon.OK);
		}

		return new Image(Constants.Icon.LOCK);
	}

	public static void createSuccessMessageBox(String label) {
		createMessageBox(Constants.Message.MSG_SUCCESS, label);
	}

	public static void createSuccessMessageBox(String label, Object[] args) {
		createMessageBox(Constants.Message.MSG_SUCCESS, label, args);
	}

	public static Textbox createTextbox() {
		return createTextbox(StringPool.BLANK);
	}

	public static Textbox createTextbox(String id) {
		return createTextbox(StringPool.BLANK, id);
	}

	public static Textbox createTextbox(String value, String id) {
		Textbox tb = new Textbox(value);

		tb.setId(id);
		tb.setMold(ZkKeys.ROUNDED);
		tb.setHflex(ZkKeys.HFLEX_1);

		return tb;
	}

	public static Textbox createTextbox(String value, String id, String placeHolder) {
		Textbox tb = createTextbox(value, id);

		tb.setPlaceholder(placeHolder);

		return tb;
	}

	public static Listcell createTextboxListcell(String label) {
		Listcell cell = new Listcell();

		Textbox tb = new Textbox(label);

		tb.setWidth("100%");

		cell.appendChild(tb);

		return cell;
	}

	public static Listcell createTextboxListcell(String label, String id) {
		Listcell cell = new Listcell();

		Textbox tb = new Textbox(label);

		tb.setWidth("100%");
		tb.setId(id);

		cell.appendChild(tb);

		return cell;
	}

	public static Toolbarbutton createToolbarButton(Component parent, String label, String event, String url,
			Map<String, Object> parameter) {
		Toolbarbutton tbb = new Toolbarbutton();

		tbb.setTooltiptext(label);

		tbb.addEventListener(event, new OpenDialogListener(url, parent, parameter));

		return tbb;
	}

	public static Toolbarbutton createToolbarButton(Component parent, String label, String event, String url,
			Map<String, Object> parameter, String iconSclass, String sclass) {
		Toolbarbutton tbb = createToolbarButton(parent, label, event, url, parameter);

		tbb.setIconSclass(iconSclass);
		tbb.setSclass(sclass);

		return tbb;
	}

	public static Toolbarbutton createToolbarButton(Component parent, String label, String event, String eventForward,
			Object data) {
		Toolbarbutton tbb = new Toolbarbutton();

		tbb.setTooltiptext(label);

		tbb.addForward(event, parent, eventForward, data);

		return tbb;
	}

	public static Toolbarbutton createToolbarButton(Component parent, String label, String event, String eventForward,
			Object data, String iconSclass, String sclass) {
		Toolbarbutton tbb = createToolbarButton(parent, label, event, eventForward, data);

		tbb.setIconSclass(iconSclass);
		tbb.setSclass(sclass);

		return tbb;
	}

	public static Label createTooltip(String text, int lengthLimit) {
		Label label = new Label();

		if (Validator.isNull(text) || lengthLimit < 0 || text.length() < lengthLimit) {
			label.setValue(text);
		} else {
			label.setValue(text.substring(0, lengthLimit) + StringPool.THREE_PERIOD);

			label.setTooltiptext(text);
		}

		return label;
	}

	public static Treecell createTreeCell(Label label) {
		Treecell cell = new Treecell();

		cell.appendChild(label);
		cell.setStyle(Constants.STYLE_BORDER_NONE);

		return cell;
	}

	public static Treecell createTreeCell(Long status) {
		Treecell cell = new Treecell();

		cell.appendChild(createStatusImage(status));
		cell.setStyle(Constants.STYLE_BORDER_NONE + Constants.STYLE_TEXT_ALIGN_CENTER);

		return cell;
	}

	public static Treecell createTreeCell(String label) {
		return createTreeCell(new Label(label));
	}

	public static Treecell createTreeCell(String label, String style) {
		Label labelTemp = createLabel(label, style);

		Treecell cell = createTreeCell(labelTemp);

		cell.setStyle(style);

		return cell;
	}

	public static Treecell createTreeCell(String label, String labelStyle, String cellStyle) {
		Treecell cell = createTreeCell(label, labelStyle);

		cell.setStyle(cellStyle);

		return cell;
	}

	public static Treecell createTreeCell(String label, String tooltip, String style, String cellStyle) {
		Label labelTemp = createLabel(label, style);
		labelTemp.setTooltiptext(tooltip);
		Treecell cell = new Treecell();
		cell.appendChild(labelTemp);
		cell.setStyle(cellStyle);
		return cell;
	}

	public static Treerow createTreerow(Label label) {
		Treerow treeRow = new Treerow();

		treeRow.appendChild(createTreeCell(label));

		return treeRow;
	}

	public static Treerow createTreerow(String label) {
		return createTreerow(new Label(label));
	}

	public static void createWarningMessageBox(String label) {
		createMessageBox(Constants.Message.MSG_WARNING, label);
	}

	public static void createWarningMessageBox(String label, Object[] args) {
		createMessageBox(Constants.Message.MSG_WARNING, label, args);
	}

	public static Long getBandboxValue(Bandbox bandbox) {
		Long bbValue = GetterUtil.getLong(bandbox.getAttribute(Constants.ID));

		if (Validator.isNull(bbValue)) {
			return null;
		}

		return bbValue;
	}

	public static String getComboboxLabel(Combobox combobox) {
		String cboLabel = GetterUtil.getString(combobox.getValue());

		return cboLabel;
	}

	public static List<SimpleModel> getComboboxStatusValue(String[] statusKeys, boolean hasDefault) {
		List<SimpleModel> threadStatusList = new ArrayList<SimpleModel>();

		if (hasDefault) {
			threadStatusList
					.add(new SimpleModel(Values.DEFAULT_OPTION_VALUE_INT, Labels.getLabel(LanguageKeys.OPTION)));
		}

		for (int i = 0; i < statusKeys.length; i++) {
			threadStatusList.add(new SimpleModel(i, statusKeys[i]));
		}

		return threadStatusList;
	}

	public static Long getComboboxValue(Combobox combobox) {
		Long cboValue = GetterUtil.getLong(getValue(combobox));

		if (Validator.isNull(cboValue)) {
			return null;
		}

		return cboValue;
	}

	public static Long getComboboxValue(Component parentCmp) {
		if (parentCmp instanceof Combobox) {
			return getComboboxValue((Combobox) parentCmp);
		}

		return null;
	}

	public static String getFailKey(boolean update) {
		if (update) {
			return LanguageKeys.MESSAGE_UPDATE_FAIL;
		}

		return LanguageKeys.MESSAGE_INSERT_FAIL;
	}

	public static Component getParentByLevel(Component startComp, int level) {
		Component comp = startComp.getParent();

		while (level > 1) {
			comp = comp.getParent();

			level--;
		}

		return comp;
	}

	public static List<Long> getSelectedId(Tree tree) {
		List<Long> ids = new ArrayList<Long>();

		Set<Treeitem> items = tree.getSelectedItems();

		for (Treeitem item : items) {
			Long id = (Long) item.getAttribute(Constants.OBJECT_ID);

			if (Validator.isNotNull(id)) {
				ids.add(id);
			}
		}

		return ids;
	}

	public static String getSuccessKey(boolean update) {
		if (update) {
			return LanguageKeys.MESSAGE_UPDATE_SUCCESS;
		}

		return LanguageKeys.MESSAGE_INSERT_SUCCESS;
	}

	public static String getTextboxValue(Component cmp) {
		if (cmp.getFirstChild() instanceof Textbox) {
			return GetterUtil.getString(((Textbox) cmp.getFirstChild()).getValue());
		}

		return StringPool.BLANK;
	}

	public static Object getValue(Combobox combobox) {
		if (combobox.getSelectedItem() == null) {
			return null;
		}

		return combobox.getSelectedItem().getValue();
	}

	public static Object getValue(Component comp) {
		if (comp == null) {
			return null;
		}

		if (comp instanceof Textbox) {
			return ((Textbox) comp).getValue();
		}

		if (comp instanceof Datebox) {
			return ((Datebox) comp).getValue();
		}

		return null;
	}

	public static void refreshMainMenu() {
		Window winParent = (Window) Sessions.getCurrent().getAttribute("mainMenuWindow");

		winParent.invalidate();

		Events.sendEvent("onLoadData", winParent, null);
	}

	public static void setSelectedItem(Combobox cb, String value) {
		for (Comboitem item : cb.getItems()) {
			if (value.equals(item.getValue())) {
				cb.setSelectedItem(item);

				break;
			}
		}
	}

}
