package org.contract.ioc;

import org.contract.common.InitException;
import org.contract.common.NameSpace;
import org.contract.common.StringUtils;
import org.contract.config.AppConfig;
import org.contract.config.Config;
import org.contract.config.Environment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URL;

import static org.mockito.Mockito.when;

class LocalIocInitTest {

    Config config;

    AppConfig appConfig;

    Environment environment;

    Ioc ioc;

    @BeforeEach
    void setUp() {
        ioc = new LocalIoc();

        config = Mockito.mock(Config.class);
        appConfig = new AppConfig("iocTest", "iocTest", "org.contract.ioc", "1");
        when(config.getAppConfig()).thenReturn(appConfig);

        environment = Mockito.mock(Environment.class);
        when(environment.getClassLoader()).thenReturn(this.getClass().getClassLoader());
    }

    @AfterEach
    void tearDown() {
        ioc = null;
    }

    @Test
    void init() {
        try {
            ioc.init(config, environment);
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    void registerControllerByDirectory() {
        init();
        try {
            ioc.registerByDirectory(NameSpace.CONTROLLER.name(), getPath() + StringUtils.LINUX_SEPARATOR + "controller");
        } catch (InitException e) {
            assert false;
        }
    }

    @Test
    void registerServiceByDirectory() {
        init();
        try {
            ioc.registerByDirectory(NameSpace.SERVICE.name(), getPath() + StringUtils.LINUX_SEPARATOR + "service");
        } catch (InitException e) {
            assert false;
        }
    }

    @Test
    void register() {
        init();
        try {
            InstanceDefinition instanceDefinition = () -> new Object();
            ioc.register(NameSpace.SERVICE.name(), instanceDefinition, "test");
        } catch (InitException e) {
            assert false;
        }
    }

    private String getPath() {
        URL resource = environment.getClassLoader().getResource(StringUtils.EMPTY);
        return resource.getPath() + appConfig.getAppRealmName2();
    }
}