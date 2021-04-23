/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.util;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.TypedValue;

/**
 *
 * @author LinhLH
 */
public class LikeExpression implements Criterion {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8104991921478185171L;
	
	private final String propertyName;
    private final String value;
    private final Character escapeChar;

    protected LikeExpression(
            String propertyName,
            Object value) {
        this(propertyName, value.toString(), (Character) null);
    }

    protected LikeExpression(
            String propertyName,
            String value,
            MatchMode matchMode) {
        this(propertyName, matchMode.toMatchString(value
                .toString()
                .replaceAll("!", "!!")
                .replaceAll("%", "!%")
                .replaceAll("_", "!_")), '!');
    }

    protected LikeExpression(
            String propertyName,
            String value,
            Character escapeChar) {
        this.propertyName = propertyName;
        this.value = value;
        this.escapeChar = escapeChar;
    }

    @Override
	public String toSqlString(
            Criteria criteria,
            CriteriaQuery criteriaQuery) throws HibernateException {
        Dialect dialect = criteriaQuery.getFactory().getDialect();
        String[] columns = criteriaQuery.getColumnsUsingProjection(criteria, this.propertyName);
        if (columns.length != 1) {
            throw new HibernateException("Like may only be used with single-column properties");
        }
        String lhs = lhs(dialect, columns[0]);
        return lhs + " like ?" + (this.escapeChar == null ? "" : " escape ?");

    }

    @Override
	public TypedValue[] getTypedValues(
            Criteria criteria,
            CriteriaQuery criteriaQuery) throws HibernateException {
        return new TypedValue[]{
                    criteriaQuery.getTypedValue(criteria, this.propertyName, typedValue(this.value)),
                    criteriaQuery.getTypedValue(criteria, this.propertyName, this.escapeChar.toString())
                };
    }

    protected String lhs(Dialect dialect, String column) {
        return column;
    }

    protected String typedValue(String value) {
        return value;
    }
}
