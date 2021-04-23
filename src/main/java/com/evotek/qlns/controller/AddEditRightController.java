/*
 * To change this template, choose Tools | Templates
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
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Category;
import com.evotek.qlns.model.Right;
import com.evotek.qlns.model.SimpleModel;
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
public class AddEditRightController extends BasicController<Window>
        implements Serializable {

    private static final long serialVersionUID = 1371006808897L;

    private Window winEditRight;
    private Window winTemp;

    private Right right;
    
    private Category category;

    private Textbox tbRightName;
    private Textbox tbDescription;

    private Combobox cbRightType;

    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp);

        this.winEditRight = comp;
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

            right = (Right) arg.get(Constants.EDIT_OBJECT);

            if(Validator.isNotNull(right)){
                winEditRight.setTitle((String) arg.get(Constants.TITLE));

                this._setEditForm();
            } else {
                tbRightName.setValue(category.getFolderName());
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    private void _setEditForm(){
        tbRightName.setValue(right.getRightName());
        tbDescription.setValue(right.getDescription());
    }

    //even method
    public void onCreate$cbRightType(){
        List<SimpleModel> rightTypes = categoryService.getRightType();

        cbRightType.setModel(new ListModelList<SimpleModel>(rightTypes));
    }

    public void onAfterRender$cbRightType() {
        if (Validator.isNotNull(right)
                && Validator.isNotNull(right.getRightType())) {
            cbRightType.setSelectedIndex(right.getRightType().intValue());
        } else {
            cbRightType.setSelectedIndex(Values.FIRST_INDEX);
        }
    }

    public void onClick$btnCancel(){
        winEditRight.detach();
    }

    public void onClick$btnSave() throws Exception {
        boolean update = true;

        try {
            String rightName = GetterUtil.getString(tbRightName.getValue());
            Long rightType = ComponentUtil.getComboboxValue(cbRightType);
            String description = GetterUtil.getString(tbDescription.getValue());

            if (_validate(rightName, description)) {
                if(Validator.isNull(right)){
                    update = false;

                    right = new Right();

                    right.setUserId(getUserId());
                    right.setUserName(getUserName());
                    right.setCreateDate(new Date());
                    right.setCategoryId(category.getCategoryId());
                    right.setStatus(Values.STATUS_ACTIVE);
                }

                right.setRightName(rightName);
                right.setRightType(rightType);
                right.setDescription(description);
                right.setModifiedDate(new Date());

                categoryService.saveOrUpdateRight(right);

                ComponentUtil.createSuccessMessageBox(
                        ComponentUtil.getSuccessKey(update));

                winEditRight.detach();

                Events.sendEvent("onLoadRight", winTemp, null);
            }

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);

            Messagebox.show(Labels.getLabel(
                    ComponentUtil.getFailKey(update)));
        }
    }
    //even method

    private boolean _validate(String rightName, String description)
            throws Exception{
        if(Validator.isNull(rightName)){
            tbRightName.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.RIGHT_NAME)));

            return false;
        }

        if (rightName.length() > Values.MEDIUM_LENGTH) {
            tbRightName.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.RIGHT_NAME),
                    Values.MEDIUM_LENGTH));

            return false;
        }

        if(categoryService.isRightExist(rightName, right)){
            tbRightName.setErrorMessage(Values.getDuplicateMsg(
                    Labels.getLabel(LanguageKeys.RIGHT_NAME)));

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
            LogManager.getLogger(AddEditRightController.class);
}
