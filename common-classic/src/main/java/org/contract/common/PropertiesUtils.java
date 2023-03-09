package org.contract.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

    public static Properties getProperties(String fileName) throws IOException {
        Properties properties = new Properties();
        try {
            InputStream input = PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName);
            try {
                properties.load(input);
            } finally {
                input.close();
            }
        } catch (Throwable e) {
            throw e;
        }
        return properties;
    }
}
