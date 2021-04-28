/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.util;

import java.util.Properties;

/**
 *
 * @author linhlh2
 */
public class SafeProperties extends Properties {

	private static final String _SAFE_NEWLINE_CHARACTER = "_SAFE_NEWLINE_CHARACTER_";

	private static String _decode(String value) {
		return StringUtil.replace(value, _SAFE_NEWLINE_CHARACTER, StringPool.NEW_LINE);
	}

	private static String _encode(String value) {
		return StringUtil.replace(value,
				new String[] { StringPool.RETURN_NEW_LINE, StringPool.NEW_LINE, StringPool.RETURN },
				new String[] { _SAFE_NEWLINE_CHARACTER, _SAFE_NEWLINE_CHARACTER, _SAFE_NEWLINE_CHARACTER });
	}

	public SafeProperties() {
		super();
	}

	@Override
	public synchronized Object get(Object key) {
		Object value = super.get(key);

		value = _decode((String) value);

		return value;
	}

	public String getEncodedProperty(String key) {
		return super.getProperty(key);
	}

	@Override
	public String getProperty(String key) {
		return (String) get(key);
	}

	@Override
	public synchronized Object put(Object key, Object value) {
		if (key == null) {
			return null;
		} else if (value == null) {
			return super.remove(key);
		} else {
			value = _encode((String) value);

			return super.put(key, value);
		}
	}

	@Override
	public synchronized Object remove(Object key) {
		if (key == null) {
			return null;
		} else {
			return super.remove(key);
		}
	}

}
