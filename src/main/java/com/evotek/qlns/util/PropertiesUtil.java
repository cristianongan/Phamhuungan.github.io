/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author linhlh2
 */
public class PropertiesUtil {

	public static void copyProperties(Properties from, Properties to) {
		Iterator itr = from.entrySet().iterator();

		while (itr.hasNext()) {
			Map.Entry entry = (Map.Entry) itr.next();

			to.setProperty((String) entry.getKey(), (String) entry.getValue());
		}
	}

	public static Properties fromMap(Map map) {
		if (map instanceof Properties) {
			return (Properties) map;
		}

		Properties p = new Properties();

		Iterator itr = map.entrySet().iterator();

		while (itr.hasNext()) {
			Map.Entry entry = (Map.Entry) itr.next();

			String key = (String) entry.getKey();
			String value = (String) entry.getValue();

			if (value != null) {
				p.setProperty(key, value);
			}
		}

		return p;
	}

	public static void fromProperties(Properties p, Map map) {
		map.clear();

		Iterator itr = p.entrySet().iterator();

		while (itr.hasNext()) {
			Map.Entry entry = (Map.Entry) itr.next();

			map.put(entry.getKey(), entry.getValue());
		}
	}

	public static String list(Map map) {
		Properties props = fromMap(map);

		ByteArrayMaker bam = new ByteArrayMaker();
		PrintStream ps = new PrintStream(bam);

		props.list(ps);

		return bam.toString();
	}

	public static void list(Map map, PrintStream out) {
		Properties props = fromMap(map);

		props.list(out);
	}

	public static void list(Map map, PrintWriter out) {
		Properties props = fromMap(map);

		props.list(out);
	}

	public static void load(Properties p, String s) throws IOException {
		if (Validator.isNotNull(s)) {
			s = UnicodeFormatter.toString(s);

			s = StringUtil.replace(s, "\\u003d", "=");
			s = StringUtil.replace(s, "\\u000a", "\n");
			s = StringUtil.replace(s, "\\u0021", "!");
			s = StringUtil.replace(s, "\\u0023", "#");
			s = StringUtil.replace(s, "\\u0020", " ");
			s = StringUtil.replace(s, "\\u005c", "\\");

			p.load(new ByteArrayInputStream(s.getBytes()));

			List propertyNames = Collections.list(p.propertyNames());

			for (int i = 0; i < propertyNames.size(); i++) {
				String key = (String) propertyNames.get(i);

				String value = p.getProperty(key);

				// Trim values because it may leave a trailing \r in certain
				// Windows environments. This is a known case for loading SQL
				// scripts in SQL Server.
				if (value != null) {
					value = value.trim();

					p.setProperty(key, value);
				}
			}
		}
	}

	public static Properties load(String s) throws IOException {
		Properties p = new Properties();

		load(p, s);

		return p;
	}

	public static void merge(Properties p1, Properties p2) {
		Enumeration enu = p2.propertyNames();

		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			String value = p2.getProperty(key);

			p1.setProperty(key, value);
		}
	}

	public static String toString(Properties p) {
		SafeProperties safeProperties = null;

		if (p instanceof SafeProperties) {
			safeProperties = (SafeProperties) p;
		}

		StringBuilder sb = new StringBuilder();

		Enumeration enu = p.propertyNames();

		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();

			sb.append(key);
			sb.append(StringPool.EQUAL);

			if (safeProperties != null) {
				sb.append(safeProperties.getEncodedProperty(key));
			} else {
				sb.append(p.getProperty(key));
			}

			sb.append(StringPool.NEW_LINE);
		}

		return sb.toString();
	}

	public static void trimKeys(Properties p) {
		Enumeration enu = p.propertyNames();

		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			String value = p.getProperty(key);

			String trimmedKey = key.trim();

			if (!key.equals(trimmedKey)) {
				p.remove(key);
				p.setProperty(trimmedKey, value);
			}
		}
	}
}
