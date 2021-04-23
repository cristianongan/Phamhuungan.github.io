package com.evotek.qlns.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 * @author LinhLH
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

	private static final Logger _log = LogManager.getLogger(CategoryServiceImpl.class);

	@Autowired
	private CategoryDAO categoryDAO;

	@Autowired
	private GroupDAO groupDAO;

	@Autowired
	private RightDAO rightDAO;

	@Override
	public void deleteCategory(Category category) throws Exception {
		try {
			Long immune = category.getImmune();
			Long status = category.getStatus();

			if (Validator.isNotNull(immune) && !Values.IMMUNE.equals(immune) && Values.STATUS_DEACTIVE.equals(status)) {
				// delete category
				this.categoryDAO.delete(category);

				// delete right
				this.rightDAO.deleteByCategoryId(category.getCategoryId());

				// delete group
				this.groupDAO.deleteByCategoryId(category.getCategoryId());

				// delete category child
				List<Category> childs = this.categoryDAO.getCategoryByParentId(category.getCategoryId());

				for (Category child : childs) {
					deleteCategory(child);
				}
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public void deleteGroup(Group group) throws Exception {
		try {
			Long status = group.getStatus();

			if (!Values.STATUS_ACTIVE.equals(status)) {
				this.groupDAO.delete(group);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public void deleteRight(Right right) throws Exception {
		try {
			Long status = right.getStatus();

			if (!Values.STATUS_ACTIVE.equals(status)) {
				this.rightDAO.delete(right);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public List<Category> getAllCategory() throws Exception {
		return this.categoryDAO.getAllCategory();
	}

@Override
public List<Category> getCategoryByParentId(Long parentId) throws Exception {
		return this.categoryDAO.getCategoryByParentId(parentId);
	}

	@Override
	public List<Category> getCategoryMenuByParentId(Long parentId) throws Exception {
		return this.categoryDAO.getCategoryMenuByParentId(parentId);
	}

	@Override
	public List<Group> getGroupByCategoryId(Long categoryId) throws Exception {
		return this.groupDAO.getGroupByCategoryId(categoryId);
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

	@Override
	public Category getParentCategory(Long parentId) throws Exception {
		return this.categoryDAO.getCategoryById(parentId);
	}

	@Override
	public List<Right> getRightByCategoryId(Long categoryId) throws Exception {
		return this.rightDAO.getRightByCategoryId(categoryId);
	}

	@Override
	public List<SimpleModel> getRightType() {
		List<SimpleModel> types = new ArrayList<SimpleModel>();

		String[] statusArray = StaticUtil.MENU_RIGHT_TYPE;

		for (int i = 0; i < statusArray.length; i++) {
			types.add(new SimpleModel(i, statusArray[i]));
		}

		return types;
	}

	@Override
	public boolean isRightExist(String rightName, Right right) throws Exception {
		Long rightId = null;

		if (right != null) {
			rightId = right.getRightId();
		}

		List<Right> rights = this.rightDAO.getRightByName(rightName, rightId);

		return !rights.isEmpty();
	}

	//    @Override
	@Override
	public void lockCategory(Category category) throws Exception {
		try {
			Long immune = category.getImmune();

			if (Validator.isNotNull(immune) && !immune.equals(Values.IMMUNE)) {
				category.setStatus(Values.STATUS_DEACTIVE);

				this.categoryDAO.saveOrUpdate(category);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public void lockGroup(Group group) throws Exception {
		try {
			group.setStatus(Values.STATUS_DEACTIVE);

			this.groupDAO.saveOrUpdate(group);
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public void lockRight(Right right) throws Exception {
		try {
			right.setStatus(Values.STATUS_DEACTIVE);

			this.rightDAO.saveOrUpdate(right);
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public void saveOrUpdateCategory(Category category, String rightName, boolean isNew) throws Exception {
		try {
			this.categoryDAO.saveOrUpdate(category);

			if (category.getType().equals(Values.MENU_TYPE_CATEGORY)) {
				return;
			}
			// save right
			if (isNew) {
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
				} else {
					right.setRightType(PermissionConstants.TYPE_MENU_ITEM);
				}

				this.rightDAO.saveOrUpdate(right);
			} else {
				Right right = this.rightDAO.getRightByCI_RN(category.getCategoryId(), rightName);

				if (Validator.isNotNull(right)) {
					right.setRightName(category.getFolderName());

					this.rightDAO.saveOrUpdate(right);
				}
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public void saveOrUpdateGroup(Group group) throws Exception {
		this.groupDAO.saveOrUpdate(group);
	}

	@Override
	public void saveOrUpdateRight(Right right) throws Exception {
		this.rightDAO.saveOrUpdate(right);
	}

	@Override
	public void unlockCategory(Category category) throws Exception {
		try {
			Long immune = category.getImmune();

			if (Validator.isNotNull(immune) && !immune.equals(Values.IMMUNE)) {
				category.setStatus(Values.STATUS_ACTIVE);

				this.categoryDAO.saveOrUpdate(category);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public void unlockGroup(Group group) throws Exception {
		try {
			group.setStatus(Values.STATUS_ACTIVE);

			this.groupDAO.saveOrUpdate(group);
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	@Override
	public void unlockRight(Right right) throws Exception {
		try {
			right.setStatus(Values.STATUS_ACTIVE);

			this.rightDAO.saveOrUpdate(right);
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}
}
