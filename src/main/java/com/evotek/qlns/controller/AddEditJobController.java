/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.extend.Messagebox;
import com.evotek.qlns.model.Job;
import com.evotek.qlns.model.render.JobRender;
import com.evotek.qlns.service.StaffService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author linhlh2
 */
public class AddEditJobController extends BasicController<Window>
        implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2598959786950022235L;
	private Window winEditJob;
    private Window winParent;
    
    private Textbox tbJobTitle;
    
    private Listbox jobListbox;
    
    private List<Job> jobs;
    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp); 
        
        this.winEditJob = comp;
    }
    
    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        initData();
    }
    
    public void initData() {
        winParent = (Window) arg.get(Constants.PARENT_WINDOW);
        
        refreshModel();
    }
    
    public void refreshModel() {
        jobs = staffService.getJobTitle();
        
        jobListbox.setModel(new ListModelList<Job>(jobs));
        jobListbox.setItemRenderer(new JobRender(winEditJob));
    }
    
    public void onDelete(Event event) {
        final Job job = (Job) event.getData();

        Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
                Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE),
                Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, 
                Messagebox.OK, new EventListener() {

                    public void onEvent(Event e) throws Exception {
                        if (Messagebox.ON_OK.equals(e.getName())) {
                            try {
                                staffService.deleteJob(job);

                                ComponentUtil.createSuccessMessageBox(
                                        LanguageKeys.MESSAGE_DELETE_SUCCESS);

                                jobs.remove(job);
                                
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
    
    public void onUpdateJob(Event event) {
        final Job job = (Job) event.getData();
            
        try {
            staffService.saveOrUpdate(job);
            
            ComponentUtil.createSuccessMessageBox(
                        ComponentUtil.getSuccessKey(true));
            
            Events.sendEvent("onUpdateCbJob", winParent, null);
        } catch (Exception ex) {
            _log.error(ex.getMessage());

            Messagebox.show(Labels.getLabel(
                    ComponentUtil.getFailKey(true)));
        }
        
        
    }
    
    public void onClick$btnAdd() throws Exception{
        try {
            String jobTitle = GetterUtil.getString(
                    tbJobTitle.getValue());
            
            if (_validate(jobTitle)) {
                Job job = new Job();
                
                job.setUserId(getUserId());
                job.setUserName(getUserName());
                job.setCreateDate(new Date());
                job.setModifiedDate(new Date());
                job.setJobTitle(jobTitle);
                job.setDescription(jobTitle);
                
                staffService.saveOrUpdate(job);
                
                ComponentUtil.createSuccessMessageBox(
                        ComponentUtil.getSuccessKey(true));

                tbJobTitle.setValue(StringPool.BLANK);
                
                refreshModel();
                
                Events.sendEvent("onUpdateCbJob", winParent, null);
            }
        }catch (Exception ex) {
            _log.error(ex.getMessage());

            Messagebox.show(Labels.getLabel(
                    ComponentUtil.getFailKey(false)));
        }
    }
    
    private boolean _validate(String jobTitle) {
        if (Validator.isNull(jobTitle)) {
            tbJobTitle.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.JOB_TITLE)));
            
            tbJobTitle.setFocus(true);
            
            return false;
        }
        
        if (jobTitle.length() > Values.MEDIUM_LENGTH) {
            tbJobTitle.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.JOB_TITLE),
                    Values.MEDIUM_LENGTH));
            
            tbJobTitle.setFocus(true);
            
            return false;
        }
        
        return true;
    }
    
    public void onClick$btnCancel(){
        winEditJob.detach();
    }
    //get set service
    public StaffService getStaffService() {
        if (staffService == null) {
            staffService = (StaffService) SpringUtil.getBean("staffService");
            setStaffService(staffService);
        }
        return staffService;
    }

    public void setStaffService(StaffService staffService) {
        this.staffService = staffService;
    }
    
    private transient StaffService staffService;
    
    private static final Logger _log =
            LogManager.getLogger(AddEditJobController.class);
}
