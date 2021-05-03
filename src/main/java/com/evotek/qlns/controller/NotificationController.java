/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.extend.Messagebox;
import com.evotek.qlns.model.Notification;
import com.evotek.qlns.model.render.NotificationRender;
import com.evotek.qlns.service.StartUpService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;

/**
 *
 * @author LinhLH
 */
@Controller
@Scope("prototype")
public class NotificationController extends BasicController<Window> implements Serializable {

	private static final long serialVersionUID = 4228531885399176712L;

	private static final Logger _log = LogManager.getLogger(NotificationController.class);

	@Autowired
	private StartUpService startUpService;

	private Div parent;

	private Listbox lbNotify;
	private List<Notification> notifies;

	private Window winNotify;

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		this.parent = (Div) this.arg.get(Constants.Attr.PARENT_WINDOW);

		this.refreshModel();
	}

	@Override
	public void doBeforeComposeChildren(Window comp) throws Exception {
		super.doBeforeComposeChildren(comp);

		this.winNotify = comp;
	}

	public void onClick$btnCancel() {
		this.winNotify.detach();
	}

	public void onDeleteNotify(Event event) {
		final Notification notify = (Notification) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, Messagebox.OK, new EventListener<Event>() {

					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								NotificationController.this.startUpService.expired(notify);

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_DELETE_SUCCESS);

								refreshModel();

								Events.sendEvent("onUpdateNotification", NotificationController.this.parent, null);
							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_DELETE_FAIL));
							}
						}
					}
				});
	}

	public void refreshModel() {
		this.notifies = this.startUpService.getNotifies();

		this.lbNotify.setModel(new ListModelList<Notification>(this.notifies));
		this.lbNotify.setItemRenderer(new NotificationRender(this.winNotify));
	}
}
