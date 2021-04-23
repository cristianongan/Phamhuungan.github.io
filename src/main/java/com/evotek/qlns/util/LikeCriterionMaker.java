/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.util;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;

/**
 *
 * @author tienbv2
 * @version 1.0
 * @since since_text
 */
public class LikeCriterionMaker {

    public static Criterion like(String propertyName, Object value) {
        return new LikeExpression(propertyName, value);
    }

    public static Criterion like(String propertyName, String value, MatchMode matchMode) {
        return new LikeExpression(propertyName, value, matchMode);
    }

    public static Criterion like(String propertyName, String value, Character escapeChar) {
        return new LikeExpression(propertyName, value, escapeChar);
    }

    public static Criterion ilike(String propertyName, Object value) {
        return new IlikeExpression(propertyName, value);
    }

    public static Criterion ilike(String propertyName, String value, MatchMode matchMode) {
        return new IlikeExpression(propertyName, value, matchMode);
    }

    public static Criterion ilike(String propertyName, String value, Character escapeChar) {
        return new IlikeExpression(propertyName, value, escapeChar);
    }
}
