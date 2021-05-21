package com.evotek.qlns.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.Even;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Popup;
import org.zkoss.zul.South;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.ext.Paginal;

import com.evotek.qlns.model.ApplicantInformation;
import com.evotek.qlns.model.ApplyPosition;
import com.evotek.qlns.model.ExpProvider;
import com.evotek.qlns.model.render.ApplicantRender;
import com.evotek.qlns.service.ApplicantInformationService;
import com.evotek.qlns.service.ApplyPositionService;
import com.evotek.qlns.util.key.Constants;

@Controller("recruitmentManagerController")
@Scope("prototype")
public class RecruitmentManagerController extends BasicController<Component>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private ApplicantInformationService applicantInformationService;
	@Autowired
	private ApplyPositionService applyPositionService;
	
	
	private List<ApplyPosition> applyPositionList;
	private Page<ApplicantInformation> page;
	private Pageable pageable;
	ListModelArray<ApplicantInformation> listResult;
	private Paging pagi;
//	private Window addApplicantPop;
	private Popup success;
	
	private Combobox applyPositionCombobox;
	private Combobox expList;
	private Listbox applicantListbox;
	private Textbox inputKey;
	private Datebox startDate;
	private Datebox endDate;
	
	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		super.doBeforeComposeChildren(comp);
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		init();
		refreshData();
	}
	private void init() {
		applyPositionList = applyPositionService.getAll();
		applyPositionCombobox.appendChild(new Comboitem("ALL"));
		applyPositionList.forEach(position->{
			Comboitem c = new Comboitem(position.getName());
			c.setValue(position);
			c.setParent(applyPositionCombobox);
		});
		
		applyPositionCombobox.setSelectedIndex(0);
		expList.appendChild(new Comboitem("ALL"));
		ExpProvider.listExp.forEach(exp ->{
			Comboitem c = new Comboitem(exp);
			c.setValue(exp);
			c.setParent(expList);
		});
		expList.setSelectedIndex(0);
		refreshPageable();
		try {
			startDate.setValue(new SimpleDateFormat("yyyy-MM-dd").parse("1999-07-03"));
		} catch (WrongValueException e) {
			System.out.println(e);
		} catch (ParseException e) {
			System.out.println(e);
		}
		endDate.setValue(new Date());
		page = applicantInformationService.getListByPage(pageable, inputKey.getValue(),
				applyPositionCombobox.getSelectedItem()!=null?applyPositionCombobox.getSelectedItem().getValue():null,
				expList.getSelectedItem()!=null?expList.getSelectedItem().getValue():null ,
				startDate.getValue(), endDate.getValue());
		listResult = new ListModelArray<ApplicantInformation>(page.getContent());
		applicantListbox.setItemRenderer(new ApplicantRender(this.self) );
		listResult.setMultiple(true);
		applicantListbox.setModel(listResult);
		this.pagi.setActivePage(0);
		setPage();
		this.endDate.setFormat("yyyy-MM-dd");
		this.startDate.setFormat("yyyy-MM-dd");
	}
	private void refreshData() {
		page = applicantInformationService.getListByPage(pageable, inputKey.getValue(),
				applyPositionCombobox.getSelectedItem()!=null?applyPositionCombobox.getSelectedItem().getValue():null,
				expList.getSelectedItem()!=null?expList.getSelectedItem().getValue():null ,
				startDate.getValue(), endDate.getValue());
		
//		listResult = new ListModelArray<ApplicantInformation>(page.getContent());
		ListModelArray<ApplicantInformation> rs = new ListModelArray<ApplicantInformation>(page.getContent());
		rs.setMultiple(true);
		
		applicantListbox.setModel(rs);
		applicantListbox.setItemRenderer(new ApplicantRender(this.self) );
	}
	private void refreshPageable() {
		this.pageable = PageRequest.of(0, 12);
	}
	private void setPage() {
		if(this.page!=null) {
			this.pagi.setTotalSize((int) this.page.getTotalElements());
			this.pagi.setPageSize(this.page.getSize());
		}
	}
	
	public void onPaging$pagi() {
		System.out.println("pgi:"+pagi.getActivePage());
		this.pageable = PageRequest.of(this.pagi.getActivePage(), this.pagi.getPageSize());
		refreshData();
	}
	
	public void onDeleteApplicant(Event event) {
		this.applicantInformationService.deleteApplicantInformation((ApplicantInformation)event.getData());
		refreshData();
	}

	public void onClick$addBtn() {
		Map<String, Object> map = new HashMap<>();
		map.put(Constants.Attr.PARENT_WINDOW, this.self);
		Window win = (Window) Executions.createComponents(Constants.Page.Recruitment.ADD_APPLICANT, null,map);
		win.setVisible(true);
		win.doHighlighted();
	}
	public void onClick$removeBtn() {
		List<ApplicantInformation> toRemove = new ArrayList<ApplicantInformation>();
		applicantListbox.getSelectedItems().forEach(c->toRemove.add(c.getValue()));
		this.applicantInformationService.deleteManyApplicantInformation(toRemove);
		refreshData();
	}
	public void onClick$searchBtn() {
		refreshPageable();
		refreshData();
	}
	public void onLoad(Event event) {
		System.out.println("-----------_____>");
		this.success.open(self);
		refreshData();
	}
	public void onUpdateApplicant(Event event) {
		
	}
	public void onClick$addPositionBtn() {
		
	}
}
