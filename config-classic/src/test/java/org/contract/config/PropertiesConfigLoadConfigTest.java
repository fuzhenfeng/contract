package org.contract.config;

import org.contract.common.InitException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Properties;

class PropertiesConfigLoadConfigTest {

    Config config;

    @BeforeEach
    void setUp() {
        config = new PropertiesConfig();
    }

    @AfterEach
    void tearDown() {
        config = null;
    }

    @Test
    void loadConfigException() {
        try {
            config.loadConfig(PropertiesConfigTool.getProperties("contract-exception.properties"));
        } catch (InitException e) {
            assert true;
        }
    }

    @Test
    void loadConfig() {
        try {
            config.loadConfig(PropertiesConfigTool.getProperties("contract-normal.properties"));
        } catch (InitException e) {
            assert false;
        }
    }
}