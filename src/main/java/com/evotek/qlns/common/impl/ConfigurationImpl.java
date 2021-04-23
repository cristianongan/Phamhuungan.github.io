/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.common.impl;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.MapConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evotek.qlns.util.EncryptUtil;
import com.evotek.qlns.util.Validator;
import com.germinus.easyconf.AggregatedProperties;
import com.germinus.easyconf.ComponentConfiguration;
import com.germinus.easyconf.ComponentProperties;
import com.germinus.easyconf.EasyConf;

/**
 *
 * @author linhlh2
 */
public class ConfigurationImpl implements com.evotek.qlns.common.Configuration {

    public ConfigurationImpl(ClassLoader classLoader, String name) {

        EasyConf.refreshAll();

        this._componentConfiguration = EasyConf.getConfiguration(
                    getFileName(classLoader, name));

        printSources();
    }

    @Override
	public void addProperties(Properties properties) {
        try {
            ComponentProperties componentProperties =
                    this._componentConfiguration.getProperties();

            AggregatedProperties aggregatedProperties =
                    (AggregatedProperties) componentProperties.toConfiguration();

            Field field1 = CompositeConfiguration.class.getDeclaredField(
                    "configList");

            field1.setAccessible(true);

            // Add to configList of base conf

            List<Configuration> configurations =
                    (List<Configuration>) field1.get(aggregatedProperties);

            MapConfiguration newConfiguration =
                    new MapConfiguration(properties);

            configurations.add(0, newConfiguration);

            // Add to configList of AggregatedProperties itself

            Class<?> clazz = aggregatedProperties.getClass();

            Field field2 = clazz.getDeclaredField("baseConf");

            field2.setAccessible(true);

            CompositeConfiguration compositeConfiguration =
                    (CompositeConfiguration) field2.get(aggregatedProperties);

            configurations = (List<Configuration>) field1.get(
                    compositeConfiguration);

            configurations.add(0, newConfiguration);

            clearCache();
        } catch (Exception e) {
            _log.error("The properties could not be added", e);
        }
    }

    public void clearCache() {
        this._values.clear();
    }

    @Override
	public boolean contains(String key) {
        Object value = this._values.get(key);

        if (value == null) {
            ComponentProperties componentProperties = getComponentProperties();

            value = componentProperties.getProperty(key);

            if (value == null) {
                value = _nullValue;
            }

            this._values.put(key, value);
        }

        if (value == _nullValue) {
            return false;
        } else {
            return true;
        }
    }
    
    @Override
	public boolean contains(String key, boolean encrypt) {
        if(!encrypt){
            return this.contains(key);
        } else {
            return this.contains(_encryptor.encrypt(key));
        }
    }

    @Override
	public String get(String key) {
        Object value = this._values.get(key);

        if (value == null) {
            ComponentProperties componentProperties = getComponentProperties();

            value = componentProperties.getString(key);

            if (value == null) {
                value = _nullValue;
            }

            this._values.put(key, value);
        } else if (_PRINT_DUPLICATE_CALLS_TO_GET) {
            System.out.println("Duplicate call to get " + key);
        }

        if (value instanceof String) {
            return (String) value;
        } else {
            return null;
        }
    }

    @Override
	public String[] getArray(String key) {
        String cacheKey = _ARRAY_KEY_PREFIX.concat(key);

        Object value = this._values.get(cacheKey);

        if (value == null) {
            ComponentProperties componentProperties = getComponentProperties();

            String[] array = componentProperties.getStringArray(key);

            value = fixArrayValue(cacheKey, array);
        }

        if (value instanceof String[]) {
            return (String[]) value;
        } else {
            return _emptyArray;
        }
    }
    @Override
	public String get(String key, boolean encrypt) {
        if(!encrypt){
            return this.get(key);
        }
        
        key = _encryptor.encrypt(key);
        
        Object value = this._values.get(key);

        if (value == null) {
            ComponentProperties componentProperties = getComponentProperties();

            value = componentProperties.getString(key);

            if (value == null) {
                value = _nullValue;
            } else {
                value = _encryptor.decrypt((String) value);
            }

            this._values.put(key, value);
        } else if (_PRINT_DUPLICATE_CALLS_TO_GET) {
            System.out.println("Duplicate call to get " + key);
        }

        if (value instanceof String) {
            return (String) value;
        } else {
            return null;
        }
    }

    @Override
	public String[] getArray(String key, boolean encrypt) {
        if(!encrypt){
            return this.getArray(key);
        }
        
        key = _encryptor.encrypt(key);
        
        String cacheKey = _ARRAY_KEY_PREFIX.concat(key);

        Object value = this._values.get(cacheKey);

        if (value == null) {
            ComponentProperties componentProperties = getComponentProperties();
            
            String encryptValue = componentProperties.getString(key);
            
            String[] array = _emptyArray;
            
            if(encryptValue != null){
                array = _encryptor.decrypt(encryptValue).split(",");
            }

            value = fixArrayValue(cacheKey, array);
        }

        if (value instanceof String[]) {
            return (String[]) value;
        } else {
            return _emptyArray;
        }
    }
    
    @Override
	public Properties getProperties() {

        // For some strange reason, componentProperties.getProperties() returns
        // values with spaces after commas. So a property setting of "xyz=1,2,3"
        // actually returns "xyz=1, 2, 3". This can break applications that
        // don't expect that extra space. However, getting the property value
        // directly through componentProperties returns the correct value. This
        // method fixes the weird behavior by returing properties with the
        // correct values.

        Properties properties = new Properties();

        ComponentProperties componentProperties = getComponentProperties();

        Properties componentPropertiesProperties =
                componentProperties.getProperties();

        for (Map.Entry<Object, Object> entry :
                componentPropertiesProperties.entrySet()) {

            String key = (String) entry.getKey();
            String value = (String) entry.getValue();

            properties.setProperty(key, value);
        }

        return properties;
    }

    @Override
	public Properties getProperties(String prefix, boolean removePrefix) {
        Properties properties = getProperties();

        return getProperties(properties, prefix, removePrefix);
    }

    public Properties getProperties(
            Properties properties, String prefix, boolean removePrefix) {

        Properties subProperties = new Properties();

        Enumeration<String> enu =
                (Enumeration<String>) properties.propertyNames();

        while (enu.hasMoreElements()) {
            String key = enu.nextElement();

            if (key.startsWith(prefix)) {
                String value = properties.getProperty(key);

                if (removePrefix) {
                    key = key.substring(prefix.length());
                }

                subProperties.setProperty(key, value);
            }
        }

        return subProperties;
    }

    @Override
	public void removeProperties(Properties properties) {
        try {
            ComponentProperties componentProperties =
                    this._componentConfiguration.getProperties();

            AggregatedProperties aggregatedProperties =
                    (AggregatedProperties) componentProperties.toConfiguration();

            Class<?> clazz = aggregatedProperties.getClass();

            Field field1 = clazz.getDeclaredField("baseConf");

            field1.setAccessible(true);

            CompositeConfiguration compositeConfiguration =
                    (CompositeConfiguration) field1.get(aggregatedProperties);

            Field field2 = CompositeConfiguration.class.getDeclaredField(
                    "configList");

            field2.setAccessible(true);

            List<Configuration> configurations =
                    (List<Configuration>) field2.get(compositeConfiguration);

            Iterator<Configuration> itr = configurations.iterator();

            while (itr.hasNext()) {
                Configuration configuration = itr.next();

                if (!(configuration instanceof MapConfiguration)) {
                    return;
                }

                MapConfiguration mapConfiguration =
                        (MapConfiguration) configuration;

                if (mapConfiguration.getMap() == properties) {
                    itr.remove();

                    aggregatedProperties.removeConfiguration(configuration);
                }
            }

            clearCache();
        } catch (Exception e) {
            _log.error("The properties could not be removed", e);
        }
    }

    @Override
	public void set(String key, String value) {
        ComponentProperties componentProperties = getComponentProperties();

        componentProperties.setProperty(key, value);

        this._values.put(key, value);
    }

    protected Object fixArrayValue(String cacheKey, String[] array) {
        if (cacheKey == null) {
            return array;
        }

        Object value = _nullValue;

        if ((array != null) && (array.length > 0)) {

            // Commons Configuration parses an empty property into a String
            // array with one String containing one space. It also leaves a
            // trailing array member if you set a property in more than one
            // line.

            if (Validator.isNull(array[array.length - 1])) {
                String[] subArray = new String[array.length - 1];

                System.arraycopy(array, 0, subArray, 0, subArray.length);

                array = subArray;
            }

            if (array.length > 0) {
                value = array;
            }
        }

        this._values.put(cacheKey, value);

        return value;
    }

    protected ComponentProperties getComponentProperties() {
        return this._componentConfiguration.getProperties();
    }

    private String getFileName(ClassLoader classLoader, String name) {
        URL url = classLoader.getResource(name + ".properties");

        // If the resource is located inside of a JAR, then EasyConf needs the
        // "jar:file:" prefix appended to the path. Use URL.toExternalForm() to
        // achieve that. When running under JBoss, the protocol returned is
        // "vfs", "vfsfile", or "vfszip". When running under OC4J, the protocol
        // returned is "code-source". When running under WebLogic, the protocol
        // returned is "zip". When running under WebSphere, the protocol
        // returned is "wsjar".

        String protocol = url.getProtocol();

        if (protocol.equals("code-source") || protocol.equals("jar")
                || protocol.equals("vfs") || protocol.equals("vfsfile")
                || protocol.equals("vfszip") || protocol.equals("wsjar")
                || protocol.equals("zip")) {

            name = url.toExternalForm();
        } else {
            try {
                name = new URI(url.getPath()).getPath();
            } catch (URISyntaxException urise) {
                name = url.getFile();
            }
        }

        int pos = name.lastIndexOf(".properties");

        if (pos != -1) {
            name = name.substring(0, pos);
        }

        return name;
    }

    private void printSources() {
        ComponentProperties componentProperties = getComponentProperties();

        List<String> sources = componentProperties.getLoadedSources();

        for (int i = sources.size() - 1; i >= 0; i--) {
            String source = sources.get(i);

            if (this._printedSources.contains(source)) {
                continue;
            }

            this._printedSources.add(source);

            String info = "Loading " + source;

            System.out.println(info);
        }
    }


    private static final String _ARRAY_KEY_PREFIX = "ARRAY_";

    private static final boolean _PRINT_DUPLICATE_CALLS_TO_GET = false;

    private static final Logger _log = LogManager.getLogger(ConfigurationImpl.class);

    private static String[] _emptyArray = new String[0];
    private static Object _nullValue = new Object();

    private ComponentConfiguration _componentConfiguration;
    private Set<String> _printedSources = new HashSet<String>();
    private Map<String, Object> _values =
            new ConcurrentHashMap<String, Object>();
    private static EncryptUtil _encryptor = new EncryptUtil();
}
