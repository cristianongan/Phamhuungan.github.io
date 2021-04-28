/*
 * Copyright 2013 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.service;

import java.util.List;

import com.evotek.qlns.model.Category;
import com.evotek.qlns.model.Group;
import com.evotek.qlns.model.Right;
import com.evotek.qlns.model.SimpleModel;

/**
 *
 * @author hungnt81
 */
public interface CategoryService {

	public void deleteCategory(Category category) throws Exception;

	public void deleteGroup(Group group) throws Exception;

	public void deleteRight(Right right) throws Exception;

	public List<Category> getAllCategory() throws Exception;

	public List<Category> getCategoryByParentId(Long parentId) throws Exception;

	public List<Category> getCategoryMenuByParentId(Long parentId) throws Exception;

	public List<Group> getGroupByCategoryId(Long categoryId) throws Exception;

	public List<SimpleModel> getMenuType();

	public Category getParentCategory(Long parentId) throws Exception;

	public List<Right> getRightByCategoryId(Long categoryId) throws Exception;

	public List<SimpleModel> getRightType();

	public boolean isRightExist(String rightName, Right right) throws Exception;

	public void lockCategory(Category category) throws Exception;

	public void lockGroup(Group group) throws Exception;

	public void lockRight(Right right) throws Exception;

	public void saveOrUpdateCategory(Category category, String rightName, boolean isNew) throws Exception;

	public void saveOrUpdateGroup(Group group) throws Exception;

	public void saveOrUpdateRight(Right right) throws Exception;

	public void unlockCategory(Category category) throws Exception;

	public void unlockGroup(Group group) throws Exception;

	public void unlockRight(Right right) throws Exception;

}