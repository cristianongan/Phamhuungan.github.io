package com.evotek.qlns.dao.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.evotek.qlns.dao.Operations;
import com.google.common.base.Preconditions;

@SuppressWarnings("unchecked")
@Repository
public abstract class AbstractDAO<T extends Serializable> implements Operations<T> {

	@Autowired
	protected SessionFactory sessionFactory;

	@Override
	public void delete(final T entity) {
		Preconditions.checkNotNull(entity);

		getCurrentSession().delete(entity);
	}

	@Override
	public void deleteAll(Collection<T> entities) {
		Preconditions.checkNotNull(entities);

		for (T entity : entities) {
			delete(entity);
		}
	}

	@Override
	public void deleteById(Class<T> entityClass, Serializable entityId) {
		final T entity = findById(entityClass, entityId);

		Preconditions.checkState(entity != null);

		delete(entity);
	}

	@Override
	public List<T> findAll(Class<T> entityClass) {
		return getCurrentSession().createQuery("from " + entityClass.getName()).getResultList();
	}

	@Override
	public T findById(Class<T> entityClass, Serializable id) {
		return getCurrentSession().get(entityClass, id);
	}

	public Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public void refresh(T entity) {
		Preconditions.checkNotNull(entity);

		getCurrentSession().refresh(entity);
	}

	public void rollback() {
		Session session = getCurrentSession();

		Transaction tx = session.getTransaction();

		try {
			if (tx != null && !session.getTransaction().getStatus().equals(TransactionStatus.COMMITTED)) {
				tx.rollback();
			}
		} finally {
			session.close();
		}
	}

	@Override
	public void save(final T entity) {
		Preconditions.checkNotNull(entity);

		getCurrentSession().save(entity);
	}

	@Override
	public void saveOrUpdate(T entity) {
		Preconditions.checkNotNull(entity);

		getCurrentSession().saveOrUpdate(entity);
	}

	@Override
	public void saveOrUpdateAll(Collection<T> entities) {
		Preconditions.checkNotNull(entities);

		for (T entity : entities) {
			saveOrUpdate(entity);
		}
	}

	@Override
	public T update(final T entity) {
		Preconditions.checkNotNull(entity);

		return (T) getCurrentSession().merge(entity);
	}

}