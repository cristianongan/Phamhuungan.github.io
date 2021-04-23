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
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.extend.Messagebox;
import com.evotek.qlns.model.ContractType;
import com.evotek.qlns.model.render.ContractTypeRender;
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
public class AddEditContractTypeController extends BasicController<Window>
        implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4468247631962909572L;
	
	private Window winEditContractType;
    private Window winParent;
    
    private Textbox tbContractType;
    private Spinner spMonthDuration;
    
    private Listbox contractTypeListbox;
    
    private List<ContractType> contractTypes;
    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp); 
        
        this.winEditContractType = comp;
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
        contractTypes = staffService.getContract();
        
        contractTypeListbox.setModel(new ListModelList<ContractType>(contractTypes));
        contractTypeListbox.setItemRenderer(new ContractTypeRender(winEditContractType));
    }
    
    public void onDelete(Event event) {
        final ContractType ct = (ContractType) event.getData();

        Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
                Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE),
                Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, 
                Messagebox.OK, new EventListener() {

                    public void onEvent(Event e) throws Exception {
                        if (Messagebox.ON_OK.equals(e.getName())) {
                            try {
                                staffService.deleteContractType(ct);

                                ComponentUtil.createSuccessMessageBox(
                                        LanguageKeys.MESSAGE_DELETE_SUCCESS);

                                contractTypes.remove(ct);
                                
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
        final ContractType ct = (ContractType) event.getData();
            
        try {
            staffService.saveOrUpdate(ct);
            
            ComponentUtil.createSuccessMessageBox(
                        ComponentUtil.getSuccessKey(true));
            
            Events.sendEvent("onUpdateCbContractType", winParent, null);
        } catch (Exception ex) {
            _log.error(ex.getMessage());

            Messagebox.show(Labels.getLabel(
                    ComponentUtil.getFailKey(true)));
        }
    }
    
    public void onClick$btnAdd() throws Exception{
        try {
            String contractType = GetterUtil.getString(
                    tbContractType.getValue());
            Long monthDuration = GetterUtil.getLong(spMonthDuration.getValue());
            
            if (_validate(contractType, monthDuration)) {
                ContractType ct = new ContractType();
                
                ct.setUserId(getUserId());
                ct.setUserName(getUserName());
                ct.setCreateDate(new Date());
                ct.setModifiedDate(new Date());
                ct.setContractTypeName(contractType);
                ct.setMonthDuration(monthDuration);
                ct.setDescription(contractType);
                
                staffService.saveOrUpdate(ct);
                
                ComponentUtil.createSuccessMessageBox(
                        ComponentUtil.getSuccessKey(true));

                tbContractType.setValue(StringPool.BLANK);
                
                refreshModel();
                
                Events.sendEvent("onUpdateCbContractType", winParent, null);
            }
        }catch (Exception ex) {
            _log.error(ex.getMessage());

            Messagebox.show(Labels.getLabel(
                    ComponentUtil.getFailKey(false)));
        }
    }
    
    private boolean _validate(String contractType, Long monthDuration) {
        if (Validator.isNull(contractType)) {
            tbContractType.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.CONTRACT_TYPE)));
            
            tbContractType.setFocus(true);
            
            return false;
        }
        
        if (contractType.length() > Values.MEDIUM_LENGTH) {
            tbContractType.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.CONTRACT_TYPE),
                    Values.MEDIUM_LENGTH));
            
            tbContractType.setFocus(true);
            
            return false;
        }

        if (monthDuration != null
                && (monthDuration <= 0 || monthDuration > 3600)) {
            spMonthDuration.setErrorMessage(Values.getValueMustInRangeMsg(
                    Labels.getLabel(LanguageKeys.CONTRACT_DURATION),
                    0, 3600));

            spMonthDuration.setFocus(true);

            return false;
        }
        
        return true;
    }
    
    public void onClick$btnCancel(){
        winEditContractType.detach();
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
            LogManager.getLogger(AddEditContractTypeController.class);
}
