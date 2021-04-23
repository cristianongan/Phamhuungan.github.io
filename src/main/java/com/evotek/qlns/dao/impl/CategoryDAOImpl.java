package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.evotek.qlns.dao.CategoryDAO;
import com.evotek.qlns.model.Category;
import com.evotek.qlns.model.User;
import com.evotek.qlns.util.QueryUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author LinhLH2
 */
@Repository
public class CategoryDAOImpl extends AbstractDAO<Category> implements CategoryDAO {

	private static final Logger _log = LogManager.getLogger(CategoryDAOImpl.class);

	@Override
	public List<Category> getAllCategory() throws Exception {
		List<Category> categories = new ArrayList<Category>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Category> criteria = builder.createQuery(Category.class);

			Root<Category> root = criteria.from(Category.class);

			criteria.select(root).where(builder.notEqual(root.get("status"), QueryUtil.STATUS_DEACTIVE));

			List<Order> orderList = new ArrayList<>();

			orderList.add(builder.asc(root.get("type")));
			orderList.add(builder.asc(root.get("weight")));
			orderList.add(builder.asc(root.get("createDate")));

			criteria.orderBy(orderList);

			categories = session.createQuery(criteria).getResultList();

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return categories;
	}

	@Override
	public Category getCategoryById(Long categoryId) throws Exception {
		return findById(Category.class, categoryId);
	}

	@Override
	public List<Category> getCategoryByParentId(Long parentId) throws Exception {
		List<Category> results = new ArrayList<Category>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Category> criteria = builder.createQuery(Category.class);

			Root<Category> root = criteria.from(Category.class);

			Predicate pre = Validator.isNull(parentId) ? builder.isNull(root.get("parentId"))
					: builder.equal(root.get("parentId"), parentId);

			criteria.select(root).where(pre);

			List<Order> orderList = new ArrayList<>();

			orderList.add(builder.asc(root.get("weight")));
			orderList.add(builder.asc(root.get("createDate")));

			criteria.orderBy(orderList);

			results = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return results;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Category> getCategoryByUser(User user) throws Exception {
		List<Category> categorys = new ArrayList<Category>();

		try {
			Session session = getCurrentSession();

			StringBuilder sb = new StringBuilder();

			sb.append("SELECT distinct c FROM Category c, RightView rv");
			sb.append(" WHERE rv.userId = :userId AND rv.rightType in (1, 2)");
			sb.append(" AND c.folderName = rv.rightName AND rv.status = 1");
			sb.append(" AND c.status = 1");
			sb.append(" ORDER BY c.type ASC, c.weight ASC, c.createDate ASC");

			Query q = session.createQuery(sb.toString());

			q.setParameter("userId", user.getUserId());

			categorys = q.getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return categorys;
	}

	@Override
	public List<Category> getCategoryItems() throws Exception {
		List<Category> categories = new ArrayList<Category>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Category> criteria = builder.createQuery(Category.class);

			Root<Category> root = criteria.from(Category.class);

			criteria.select(root).where(builder.equal(root.get("type"), Values.MENU_TYPE_ITEM),
					builder.notEqual(root.get("status"), QueryUtil.STATUS_DEACTIVE));

			List<Order> orderList = new ArrayList<>();

			orderList.add(builder.asc(root.get("type")));
			orderList.add(builder.asc(root.get("weight")));
			orderList.add(builder.asc(root.get("createDate")));

			criteria.orderBy(orderList);

			categories = session.createQuery(criteria).getResultList();
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return categories;
	}

	@Override
	public List<Category> getCategoryMenuByParentId(Long parentId) throws Exception {
		List<Category> results = new ArrayList<Category>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Category> criteria = builder.createQuery(Category.class);

			Root<Category> root = criteria.from(Category.class);

			Predicate preParentId = Validator.isNull(parentId) ? builder.isNull(root.get("parentId"))
					: builder.equal(root.get("parentId"), parentId);

			Predicate preType = builder.equal(root.get("type"), Values.MENU_TYPE_CATEGORY);

			criteria.select(root).where(preParentId, preType);

			List<Order> orderList = new ArrayList<>();

			orderList.add(builder.asc(root.get("weight")));
			orderList.add(builder.asc(root.get("createDate")));

			criteria.orderBy(orderList);

			results = session.createQuery(criteria).getResultList();

		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return results;
	}
}
