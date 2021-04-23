package com.evotek.qlns.dao;

import java.io.Serializable;
import java.util.List;

public interface Operations<T extends Serializable> {

    T findById(final Class<T> entityClass, final Serializable id);

    List<T> findAll( final Class<T> entityClass);

    void save(final T entity);

    T update(final T entity);

    void delete(final T entity);

    void deleteById(final Class<T> entityClass, final Serializable entityId);

    void saveOrUpdate(final T entity);
}
