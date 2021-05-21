package com.evotek.qlns.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;

import com.evotek.qlns.model.ApplicantInformation;
import com.evotek.qlns.model.ExpProvider;
import com.evotek.qlns.service.ApplicantInformationService;
import com.evotek.qlns.service.ApplyPositionService;
import com.evotek.qlns.util.key.Constants;

@Controller("applicantController")
@Scope("prototype")
public class ApplicantController extends GenericForwardComposer<Component>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private ApplicantInformationService applicantInformationService;
	@Autowired
	private ApplyPositionService applyPositionService;
	
	private Textbox name;
	private Textbox mobiblePhone;
	private Textbox email;
	private Datebox applyDateC;
	private Combobox exp;
	private Textbox jobInfomationSource;
	private Combobox applyPosition;
	private Media media;
	private Textbox linkMedia;
	private Textbox note;
	private Component parent;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		init();
	}
	
	private void init() {
		ExpProvider.listExp.forEach(c->{
			Comboitem item = new Comboitem(c);
			item.setValue(c);
			exp.appendChild(item);
		});
		applyPositionService.getAll().forEach(c->{
			Comboitem item = new Comboitem(c.getName());
			item.setValue(c);
			applyPosition.appendChild(item);
		});
		applyDateC.setValue(new Date());
		this.self.addEventListener("onCreate", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				System.out.println("closing"+event.getTarget().isInvalidated());
//				event.getTarget().invalidate();
				System.out.println("closed"+event.getTarget().isInvalidated());
			}
		});
		this.parent = (Component)this.arg.get(Constants.Attr.PARENT_WINDOW);
		this.applyDateC.setFormat("yy-MM-dd");
		
	}
	private void addApplicant() {
		ApplicantInformation applicant = new ApplicantInformation(name.getValue(),
				mobiblePhone.getValue(), this.applyPosition.getSelectedItem().getValue(),
				this.email.getValue(), this.exp.getValue(),
				this.jobInfomationSource.getValue(), media, null, note.getValue(), this.linkMedia.getValue(),
				this.applyDateC.getValue());
		applicantInformationService.addApplicant(applicant);
	}
	public void onClick$btnSubmit() {
		try {
			addApplicant();
			Events.sendEvent("onLoad", this.parent, null);
		}catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	public void onClick$closeBtn() {
		System.out.println("closing");
	}

}
