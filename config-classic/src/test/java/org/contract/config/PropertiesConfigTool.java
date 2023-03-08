package org.contract.config;

import org.contract.common.InitException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Properties;

class PropertiesConfigTool {

    public static Properties getProperties(String fileName) throws InitException {
        Properties properties = new Properties();
        try {
            InputStream input = PropertiesConfigTool.class.getClassLoader().getResourceAsStream(fileName);
            try {
                properties.load(input);
            } finally {
                input.close();
            }
        } catch (Throwable e) {
            // ignore
        }
        return properties;
    }
}