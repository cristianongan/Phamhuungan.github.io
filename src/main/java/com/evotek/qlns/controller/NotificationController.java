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
import org.zkoss.spring.SpringUtil;
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
 * @author My PC
 */
public class NotificationController extends BasicController<Window>
        implements Serializable{
    private Window winNotify;
    private Div parent;
    private Listbox lbNotify;

    private List<Notification> notifies;
    
    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp);
        
        this.winNotify = comp;
    }

    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);
        
        parent = (Div) arg.get(Constants.PARENT_WINDOW);
        
        this.refreshModel();
    }
    
    public void refreshModel(){
        notifies = startUpService.getNotifies();
        
        lbNotify.setModel(new ListModelList<Notification>(notifies));
        lbNotify.setItemRenderer(new NotificationRender(winNotify));
    }
    
    public void onDeleteNotify(Event event) {
        final Notification notify = (Notification) event.getData();

        Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
                Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE),
                Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
                Messagebox.OK, new EventListener() {

                    public void onEvent(Event e) throws Exception {
                        if (Messagebox.ON_OK.equals(e.getName())) {
                            try {
                                startUpService.expired(notify);

                                ComponentUtil.createSuccessMessageBox(
                                        LanguageKeys.MESSAGE_DELETE_SUCCESS);

                                refreshModel();
                                
                                Events.sendEvent("onUpdateNotification", parent, null);
                            } catch (Exception ex) {
                                _log.error(ex.getMessage(), ex);

                                Messagebox.show(Labels.getLabel(
                                                LanguageKeys.MESSAGE_DELETE_FAIL));
                            }
                        }
                    }
                });
    }
    
    public void onClick$btnCancel(){
        winNotify.detach();
    }
    //get set service
    public StartUpService getStartUpService() {
        if (startUpService == null) {
            startUpService = (StartUpService) SpringUtil.getBean("startUpService");
            setStartUpService(startUpService);
        }

        return startUpService;
    }

    public void setStartUpService(StartUpService startUpService) {
        this.startUpService = startUpService;
    }
    
    private StartUpService startUpService;
    
    private static final Logger _log =
            LogManager.getLogger(NotificationController.class);
}
