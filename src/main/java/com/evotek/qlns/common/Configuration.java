package com.evotek.qlns.common;

import java.util.Properties;

/**
 *
 * @author linhlh2
 */
public interface Configuration {

	public void addProperties(Properties properties);

	public boolean contains(String key);

	public boolean contains(String key, boolean encrypt);

	public String get(String key);

	public String get(String key, boolean encrypt);

	public String[] getArray(String key);

	public String[] getArray(String key, boolean encrypt);

	public Properties getProperties();

	public Properties getProperties(String prefix, boolean removePrefix);

	public void removeProperties(Properties properties);

	public void set(String key, String value);
}
