
/*
 * Copyright 2013 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.evotek.qlns.model.Category;
import com.evotek.qlns.model.User;

/**
 *
 * @author hungnt81
 */
public interface CategoryDAO {

    public List<Category> getAllCategory() throws Exception;

    public List<Category> getCategoryItems() throws Exception;

    public List<Category> getCategoryByUser(User user) throws Exception;

    public List<Category> getCategoryByParentId(Long parentId) throws Exception;

    public List<Category> getCategoryMenuByParentId(Long parentId) throws Exception;

    public Category getCategoryById(Long categoryId) throws Exception;

    public void saveOrUpdate(Category category) throws DataAccessException;
    
    public void delete(Category category) throws DataAccessException;

}

