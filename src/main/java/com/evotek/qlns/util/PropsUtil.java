/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.util;

import java.util.Properties;

import com.evotek.qlns.common.Configuration;
import com.evotek.qlns.common.impl.ConfigurationImpl;
import com.evotek.qlns.util.key.PropsFiles;

/**
 *
 * @author linhlh2
 */
public class PropsUtil {

    public static void addProperties(Properties properties) {
        _instance._configuration.addProperties(properties);
    }

    public static boolean contains(String key) {
        return _instance._configuration.contains(key);
    }

    public static String get(String key) {
        return _instance._configuration.get(key);
    }

    public static String[] getArray(String key) {
        return _instance._configuration.getArray(key);
    }
    
    public static boolean contains(String key, boolean encrypt) {
        return _instance._configuration.contains(key, encrypt);
    }

    public static String get(String key, boolean encrypt) {
        return _instance._configuration.get(key, encrypt);
    }

    public static String[] getArray(String key, boolean encrypt) {
        return _instance._configuration.getArray(key, encrypt);
    }

    public static Properties getProperties() {
        return _instance._configuration.getProperties();
    }

    public static void removeProperties(Properties properties) {
        _instance._configuration.removeProperties(properties);
    }

    public static void set(String key, String value) {
        _instance._configuration.set(key, value);
    }

    private PropsUtil() {
        this._configuration = new ConfigurationImpl(
			PropsUtil.class.getClassLoader(), PropsFiles.CONFIG);
    }

    private static PropsUtil _instance = new PropsUtil();

    private Configuration _configuration;
}
