package org.contract.config;

import org.contract.common.InitException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesConfigTest {

    Config config;

    @BeforeEach
    void setUp() {
        config = new PropertiesConfig();
        try {
            config.loadConfig(PropertiesConfigTool.getProperties("contract-normal.properties"));
        } catch (InitException e) {
            assert false;
        }
    }

    @AfterEach
    void tearDown() {
        config = null;
    }

    @Test
    void getConfig() {
        Map<String, String> map = config.getConfig();
        assert map != null;

        String xxx = map.get("xxx");
        assert xxx == null;
    }

    @Test
    void getAppConfig() {
        AppConfig appConfig = config.getAppConfig();
        assert appConfig != null;

        assert appConfig.getAppId() != null;
        assert "test".equals(appConfig.getAppId());

        assert appConfig.getAppName() != null;
        assert "test".equals(appConfig.getAppName());

        assert appConfig.getAppRealmName() != null;
        assert "org.test".equals(appConfig.getAppRealmName());

        assert appConfig.getAppVersion() != null;
        assert "1.0-SNAPSHOT".equals(appConfig.getAppVersion());
    }
}