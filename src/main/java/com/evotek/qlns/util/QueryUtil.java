/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 *
 * @author linhlh2
 */
public class QueryUtil {

    public static final Long STATUS_DEACTIVE = 0L;
    public static final Long STATUS_ACTIVE = 1L;
    public static final Long ALL = -1L;
    public static final int FIRST_INDEX = 0;
    public static final int GET_ALL = -1;

    public static String addOrder(Class c, String orderByColumn,
            String orderByType) {
        StringBuilder sb = new StringBuilder();

        if (hasProperty(c, orderByColumn)) {
            sb.append(" order by ");
            sb.append(orderByColumn);
            sb.append(StringPool.SPACE);
            sb.append(orderByType);
        }

        return sb.toString();
    }

    public static String getFullStringParam(String param) {
        StringBuilder sb = new StringBuilder(5);

        sb.append(StringPool.PERCENT);
        sb.append(_replaceSpecialCharacter(param));
        sb.append(StringPool.PERCENT);

        return sb.toString();
    }

    public static String getLeftStringParam(String param) {
        StringBuilder sb = new StringBuilder(2);

        sb.append(StringPool.PERCENT);
        sb.append(_replaceSpecialCharacter(param));


        return sb.toString();
    }

    public static String getRightStringParam(String param) {
        StringBuilder sb = new StringBuilder(2);

        sb.append(_replaceSpecialCharacter(param));
        sb.append(StringPool.PERCENT);

        return sb.toString();
    }

    public static void setParamerterMap(Query q, Map<String, Object> mapParams) {
        for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
            String _key = entry.getKey();
            Object _value = entry.getValue();

            if (_value instanceof List) {
                q.setParameterList(_key, (List) _value);
            } else {
                q.setParameter(_key, _value);
            }
        }
    }
    
    public static void setParamerterMap(SQLQuery sql, Map<String, Object> mapParams) {
        for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
            String _key = entry.getKey();
            Object _value = entry.getValue();

            if (_value instanceof List) {
                sql.setParameterList(_key, (List) _value);
            } else {
                sql.setParameter(_key, _value);
            }
        }
    }
    
    //LinhLH2 fix
    private static String _replaceSpecialCharacter(String param) {
            return param.replaceAll("%", "\\\\%")
                    .replaceAll("_", "\\\\_");
    }

    private static boolean hasProperty(Class c, String name) {
        boolean has = false;

        try {
            Field field = c.getDeclaredField(name);

            if (Validator.isNotNull(field)) {
                has = true;
            }
        } catch (Exception e) {
        }

        return has;
    }
}
