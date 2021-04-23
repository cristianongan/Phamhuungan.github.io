/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.util;

import org.hibernate.criterion.MatchMode;
import org.hibernate.dialect.Dialect;

/**
 *
 * @author tienbv2
 * @version 1.0
 * @since since_text
 */
public class IlikeExpression extends LikeExpression {

    protected IlikeExpression(
            String propertyName,
            Object value) {
        super(propertyName, value);
    }

    protected IlikeExpression(
            String propertyName,
            String value,
            MatchMode matchMode) {
        super(propertyName, value, matchMode);
    }

    protected IlikeExpression(
            String propertyName,
            String value,
            Character escapeChar) {
        super(propertyName, value, escapeChar);
    }

    @Override
    protected String lhs(Dialect dialect, String column) {
        return dialect.getLowercaseFunction() + '(' + column + ')';
    }

    @Override
    protected String typedValue(String value) {
        return super.typedValue(value).toLowerCase();
    }
}
