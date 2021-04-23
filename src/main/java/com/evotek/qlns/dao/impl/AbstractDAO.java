package com.evotek.qlns.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.evotek.qlns.dao.Operations;
import com.google.common.base.Preconditions;

@SuppressWarnings("unchecked")
public abstract class AbstractDAO<T extends Serializable> implements Operations<T> {

	@Autowired
	protected SessionFactory sessionFactory;

	@Override
	public T findById(Class<T> entityClass, Serializable id) {
		return getCurrentSession().get(entityClass, id);
	}

	@Override
	public List<T> findAll(Class<T> entityClass) {
		return getCurrentSession().createQuery("from " + entityClass.getName()).getResultList();
	}

	@Override
	public void save(final T entity) {
		Preconditions.checkNotNull(entity);

		getCurrentSession().save(entity);
	}

	@Override
	public T update(final T entity) {
		Preconditions.checkNotNull(entity);

		return (T) getCurrentSession().merge(entity);
	}

	@Override
	public void delete(final T entity) {
		Preconditions.checkNotNull(entity);

		getCurrentSession().delete(entity);
	}

	@Override
	public void deleteById(Class<T> entityClass, Serializable entityId) {
		final T entity = findById(entityClass, entityId);

		Preconditions.checkState(entity != null);

		delete(entity);
	}

	@Override
	public void saveOrUpdate(T entity) {
		Preconditions.checkNotNull(entity);

		getCurrentSession().saveOrUpdate(entity);
	}
	
	public Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}

}