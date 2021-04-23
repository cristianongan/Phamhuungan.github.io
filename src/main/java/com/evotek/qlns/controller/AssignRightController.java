/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Group;
import com.evotek.qlns.model.Right;
import com.evotek.qlns.model.render.AssignRightRender;
import com.evotek.qlns.service.CategoryService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;

/**
 *
 * @author linhlh2
 */
public class AssignRightController extends BasicController<Window>
        implements Serializable {

    private static final long serialVersionUID = 1371028045961L;

    private Window winAssignRight;
    private Window winTemp;

    private Listbox searchResult;

    private Group group;

    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp);

        this.winAssignRight = comp;
    }

    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        //init data
        this.initData();

        this.searchRight();
    }

    //init data
    public void initData() throws Exception {
        try {
            this.winTemp = (Window) this.arg.get(Constants.PARENT_WINDOW);

            this.group = (Group) this.arg.get(Constants.EDIT_OBJECT);
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public void searchRight() throws Exception{
        try {
            List<Right> rights = this.categoryService.
                    getRightByCategoryId(this.group.getCategoryId());

            this.searchResult.setItemRenderer(new AssignRightRender(this.group.getRights()));
            this.searchResult.setModel(new ListModelList<Right>(rights));
            this.searchResult.setMultiple(true);
        }catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    //even method
    public void onClick$btnSave() throws Exception {
        try {
            Set<Right> newRights = new HashSet<Right>();

            Set<Listitem> listitems = this.searchResult.getSelectedItems();

            for(Listitem item: listitems){
                Right right = (Right) item.getAttribute("data");

                if(right!=null){
                    newRights.add(right);
                }
            }

            this.group.setRights(newRights);

            this.categoryService.saveOrUpdateGroup(this.group);

            //delete old groupRight
//            categoryService.deleteGroupsRight(groupsRights);

            ComponentUtil.createSuccessMessageBox(
                    LanguageKeys.MESSAGE_UPDATE_SUCCESS);

            this.winAssignRight.detach();

//                Events.sendEvent("onLoadGroups", winTemp, null);


        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);

            Messagebox.show(Labels.getLabel(
                    LanguageKeys.MESSAGE_UPDATE_FAIL));
        }
    }
    
    public void onClick$btnCancel(){
        this.winAssignRight.detach();
    }
    //even method
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
            LogManager.getLogger(AssignRightController.class);
}
