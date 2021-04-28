package com.evotek.qlns.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface Operations<T extends Serializable> {

	void delete(final T entity);

	void deleteAll(final Collection<T> entities);

	void deleteById(final Class<T> entityClass, final Serializable entityId);

	List<T> findAll(final Class<T> entityClass);

	T findById(final Class<T> entityClass, final Serializable id);

	public void refresh(T entity);

	public void rollback();

	void save(final T entity);

	void saveOrUpdate(final T entity);

	void saveOrUpdateAll(final Collection<T> entities);

	T update(final T entity);
}
