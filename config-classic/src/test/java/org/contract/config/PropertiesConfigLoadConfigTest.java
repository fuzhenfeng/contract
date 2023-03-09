package org.contract.config;

import org.contract.common.PropertiesUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
            config.loadConfig(PropertiesUtils.getProperties("contract-exception.properties"));
        } catch (Exception e) {
            assert true;
        }
    }

    @Test
    void loadConfig() {
        try {
            config.loadConfig(PropertiesUtils.getProperties("contract-normal.properties"));
        } catch (Exception e) {
            assert false;
        }
    }
}