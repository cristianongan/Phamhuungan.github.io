/*
 * Copyright 2013 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evotek.qlns.dao.CategoryDAO;
import com.evotek.qlns.dao.GroupDAO;
import com.evotek.qlns.dao.RightDAO;
import com.evotek.qlns.model.Category;
import com.evotek.qlns.model.Group;
import com.evotek.qlns.model.Right;
import com.evotek.qlns.model.SimpleModel;
import com.evotek.qlns.service.CategoryService;
import com.evotek.qlns.util.StaticUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.PermissionConstants;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author hungnt81
 */
public class CategoryServiceImpl implements CategoryService {
    private transient CategoryDAO categoryDAO;
    private transient RightDAO rightDAO;
    private transient GroupDAO groupDAO;

    public CategoryDAO getCategoryDAO() {
        return categoryDAO;
    }

    public void setCategoryDAO(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public RightDAO getRightDAO() {
        return rightDAO;
    }

    public void setRightDAO(RightDAO rightDAO) {
        this.rightDAO = rightDAO;
    }

    public GroupDAO getGroupDAO() {
        return groupDAO;
    }

    public void setGroupDAO(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    @Override
    public List<Category> getAllCategory() throws Exception{
        return categoryDAO.getAllCategory();
    }

    @Override
    public void saveOrUpdateCategory(Category category, String rightName, 
            boolean isNew) throws Exception{
        try{
            categoryDAO.saveOrUpdate(category);

            if(category.getType().equals(Values.MENU_TYPE_CATEGORY)){
                return;
            }
            //save right
            if(isNew){
                Right right = new Right();

                right.setUserId(category.getUserId());
                right.setUserName(category.getUserName());
                right.setCreateDate(new Date());
                right.setModifiedDate(new Date());
                right.setRightName(category.getFolderName());
                right.setStatus(Values.STATUS_ACTIVE);
                right.setCategoryId(category.getCategoryId());

                if (category.getType().equals(Values.MENU_TYPE_CATEGORY)) {
                    right.setRightType(PermissionConstants.TYPE_MENU_CATEGORY);
                } else{
                    right.setRightType(PermissionConstants.TYPE_MENU_ITEM);
                }

                rightDAO.saveOrUpdate(right);
            } else {
                Right right = rightDAO.getRightByCI_RN(category.getCategoryId(),
                        rightName);

                if(Validator.isNotNull(right)){
                    right.setRightName(category.getFolderName());

                    rightDAO.saveOrUpdate(right);
                }
            }
        }catch(Exception ex){
            _log.error(ex.getMessage(), ex);
        }
    }

    @Override
    public List<SimpleModel> getMenuType() {
        List<SimpleModel> types = new ArrayList<SimpleModel>();

        String[] statusArray = StaticUtil.MENU_TYPE;
        
        for (int i = 0; i < statusArray.length; i++) {
            types.add(new SimpleModel(i, statusArray[i]));
        }
        
        return types;
    }

    public List<SimpleModel> getRightType() {
        List<SimpleModel> types = new ArrayList<SimpleModel>();

        String[] statusArray = StaticUtil.MENU_RIGHT_TYPE;

        for (int i = 0; i < statusArray.length; i++) {
            types.add(new SimpleModel(i, statusArray[i]));
        }

        return types;
    }

    public Category getParentCategory(Long parentId) throws Exception{
        return categoryDAO.getCategoryById(parentId);
    }

//    @Override
    public void lockCategory(Category category) throws Exception{
        try{
            Long immune = category.getImmune();

            if(Validator.isNotNull(immune)
                    && !immune.equals(Values.IMMUNE)){
                category.setStatus(Values.STATUS_DEACTIVE);

                categoryDAO.saveOrUpdate(category);
            }
        }catch(Exception ex){
            _log.error(ex.getMessage(), ex);
        }
    }

    public void unlockCategory(Category category) throws Exception{
        try{
            Long immune = category.getImmune();

            if(Validator.isNotNull(immune)
                    && !immune.equals(Values.IMMUNE)){
                category.setStatus(Values.STATUS_ACTIVE);

                categoryDAO.saveOrUpdate(category);
            }
        }catch(Exception ex){
            _log.error(ex.getMessage(), ex);
        }
    }

    public void deleteCategory(Category category) throws Exception{
        try{
            Long immune = category.getImmune();
            Long status = category.getStatus();

            if(Validator.isNotNull(immune)
                    && !Values.IMMUNE.equals(immune)
                    && Values.STATUS_DEACTIVE.equals(status)){
                //delete category
                categoryDAO.delete(category);

                //delete right
                rightDAO.deleteByCategoryId(category.getCategoryId());

                //delete group
                groupDAO.deleteByCategoryId(category.getCategoryId());

                //delete category child
                List<Category> childs = categoryDAO.getCategoryByParentId(
                        category.getCategoryId());

                for(Category child: childs){
                    deleteCategory(child);
                }
            }
        }catch(Exception ex){
            _log.error(ex.getMessage(), ex);
        }
    }
    
    public List<Category> getCategoryByParentId(Long parentId) throws Exception{
        return categoryDAO.getCategoryByParentId(parentId);
    }

    public List<Category> getCategoryMenuByParentId(Long parentId) throws Exception{
        return categoryDAO.getCategoryMenuByParentId(parentId);
    }

    public List<Group> getGroupByCategoryId(Long categoryId) throws Exception{
        return groupDAO.getGroupByCategoryId(categoryId);
    }

    public List<Right> getRightByCategoryId(Long categoryId) throws Exception{
        return rightDAO.getRightByCategoryId(categoryId);
    }

    public void saveOrUpdateGroup(Group group) throws Exception{
        groupDAO.saveOrUpdate(group);
    }

    public void saveOrUpdateRight(Right right) throws Exception{
        rightDAO.saveOrUpdate(right);
    }

    public void lockGroup(Group group) throws Exception {
        try{
            group.setStatus(Values.STATUS_DEACTIVE);

            groupDAO.saveOrUpdate(group);
        }catch(Exception ex){
            _log.error(ex.getMessage(), ex);
        }
    }

    public void unlockGroup(Group group) throws Exception {
        try{
            group.setStatus(Values.STATUS_ACTIVE);

            groupDAO.saveOrUpdate(group);
        }catch(Exception ex){
            _log.error(ex.getMessage(), ex);
        }
    }

    public void deleteGroup(Group group) throws Exception {
        try{
            Long status = group.getStatus();

            if(!Values.STATUS_ACTIVE.equals(status)){
                groupDAO.delete(group);
            }
        }catch(Exception ex){
            _log.error(ex.getMessage(), ex);
        }
    }


    public void lockRight(Right right) throws Exception {
        try{
            right.setStatus(Values.STATUS_DEACTIVE);

            rightDAO.saveOrUpdate(right);
        }catch(Exception ex){
            _log.error(ex.getMessage(), ex);
        }
    }

    public void unlockRight(Right right) throws Exception {
        try{
            right.setStatus(Values.STATUS_ACTIVE);

            rightDAO.saveOrUpdate(right);
        }catch(Exception ex){
            _log.error(ex.getMessage(), ex);
        }
    }

    public void deleteRight(Right right) throws Exception {
        try{
            Long status = right.getStatus();

            if(!Values.STATUS_ACTIVE.equals(status)){
                rightDAO.delete(right);
            }
        }catch(Exception ex){
            _log.error(ex.getMessage(), ex);
        }
    }

    public boolean isRightExist(String rightName, Right right) throws Exception {
        Long rightId = null;

        if (right != null) {
            rightId = right.getRightId();
        }

        List<Right> rights = rightDAO.getRightByName(rightName, rightId);

        return !rights.isEmpty();
    }
    
    private static final Logger _log =
            LogManager.getLogger(CategoryServiceImpl.class);
}
