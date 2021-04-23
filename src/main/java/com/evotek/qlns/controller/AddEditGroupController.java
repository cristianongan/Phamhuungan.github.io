/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Category;
import com.evotek.qlns.model.Group;
import com.evotek.qlns.service.CategoryService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author linhlh2
 */
public class AddEditGroupController extends BasicController<Window>
        implements Serializable {

    private static final long serialVersionUID = 1371000290846L;
    
    private Window winEditGroups;
    private Window winTemp;

    private Group group;

    private Category category;

    private Textbox tbGroupsName;
    private Textbox tbDescription;

    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp);

        this.winEditGroups = comp;
    }

    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        //init data
        this.initData();
    }

    //init data
    public void initData() throws Exception {
        try {
            winTemp = (Window) arg.get(Constants.PARENT_WINDOW);

            category = (Category) arg.get(Constants.OBJECT);

            if(Validator.isNull(category)){
                category = new Category();
            }

            group = (Group) arg.get(Constants.EDIT_OBJECT);

            if(Validator.isNotNull(group)){
                winEditGroups.setTitle((String) arg.get(Constants.TITLE));

                this._setEditForm();
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    private void _setEditForm(){
        tbGroupsName.setValue(group.getGroupName());
        tbDescription.setValue(group.getDescription());
    }

    //even method
    public void onClick$btnCancel(){
        winEditGroups.detach();
    }

    public void onClick$btnSave() throws Exception {
        boolean update = true;

        try {
            String groupName = GetterUtil.getString(tbGroupsName.getValue());
            String description = GetterUtil.getString(tbDescription.getValue());

            if (_validate(groupName, description)) {
                if(Validator.isNull(group)){
                    update = false;

                    group = new Group();

                    group.setUserId(getUserId());
                    group.setUserName(getUserName());
                    group.setCreateDate(new Date());
                    group.setCategoryId(category.getCategoryId());
                    group.setStatus(Values.STATUS_ACTIVE);
                }

                group.setGroupName(groupName);
                group.setDescription(description);
                group.setModifiedDate(new Date());

                categoryService.saveOrUpdateGroup(group);

                ComponentUtil.createSuccessMessageBox(
                        ComponentUtil.getSuccessKey(update));

                winEditGroups.detach();

                Events.sendEvent("onLoadGroups", winTemp, null);
            }

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);

            Messagebox.show(Labels.getLabel(
                    ComponentUtil.getFailKey(update)));
        }
    }
    //even method

    private boolean _validate(String groupName, String description) {
        if (Validator.isNull(groupName)) {
            tbGroupsName.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.GROUP_NAME)));

            return false;
        }

        if (groupName.length() > Values.MEDIUM_LENGTH) {
            tbGroupsName.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.GROUP_NAME),
                    Values.MEDIUM_LENGTH));

            return false;
        }

        if (Validator.isNotNull(description)
                && description.length() > Values.GREATE_LONG_LENGTH) {
            tbDescription.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.DESCRIPTION),
                    Values.GREATE_LONG_LENGTH));

            return false;
        }

        return true;
    }
    //get set service
    public CategoryService getCategoryService() {
        if (this.categoryService == null) {
            this.categoryService = (CategoryService)
                    SpringUtil.getBean("categoryService");
            setCategoryService(this.categoryService);
        }

        return this.categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    private transient CategoryService categoryService;
    //get set service

    private static final Logger _log =
            LogManager.getLogger(AddEditGroupController.class);
}
