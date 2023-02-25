package org.contract.common;

import java.util.Properties;

public class PropertiesHelper {

    private Properties properties;

    public PropertiesHelper(Properties properties) {
        this.properties = properties;
    }

    public String get(String key) {
        String property = properties.getProperty(key);
        return StringUtils.defaultIfBlank(property, StringUtils.EMPTY);
    }
}
