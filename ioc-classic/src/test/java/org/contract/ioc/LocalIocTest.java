package org.contract.ioc;

import org.contract.common.NameSpace;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class LocalIocTest extends LocalIocInitTest {

    @BeforeEach
    void setUp() {
        super.setUp();
        super.init();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void getClasses() {
        super.registerControllerByDirectory();
        List<Class> conCla = ioc.getClasses(NameSpace.CONTROLLER.name());
        assert conCla != null && conCla.size() != 0;
        assert conCla.size() == 2;
        assert conCla.stream().filter(cla -> "org.contract.ioc.controller.TestController".equals(cla.getName())).count() == 1;
        assert conCla.stream().filter(cla -> "org.contract.ioc.controller.component.TestComponent".equals(cla.getName())).count() == 1;

        super.registerServiceByDirectory();
        List<Class> serCla = ioc.getClasses(NameSpace.SERVICE.name());
        assert serCla != null && serCla.size() != 0;
        assert serCla.size() == 3;
        assert serCla.stream().filter(cla -> "org.contract.ioc.repository.TestRepository".equals(cla.getName())).count() == 1;
        assert serCla.stream().filter(cla -> "org.contract.ioc.domain.TestDomain".equals(cla.getName())).count() == 1;
        assert serCla.stream().filter(cla -> "org.contract.ioc.service.TestService".equals(cla.getName())).count() == 1;
    }

    @Test
    void getInstances() {
        super.registerControllerByDirectory();
        List<Object> conIns = ioc.getInstances(NameSpace.CONTROLLER.name());
        assert conIns != null && conIns.size() != 0;
        assert conIns.size() == 2;
        assert conIns.stream().filter(cla -> "org.contract.ioc.controller.TestController".equals(cla.getClass().getName())).count() == 1;
        assert conIns.stream().filter(cla -> "org.contract.ioc.controller.component.TestComponent".equals(cla.getClass().getName())).count() == 1;

        super.registerServiceByDirectory();
        List<Object> serIns = ioc.getInstances(NameSpace.SERVICE.name());
        assert serIns != null && serIns.size() != 0;
        assert serIns.size() == 3;
        assert serIns.stream().filter(cla -> "org.contract.ioc.repository.TestRepository".equals(cla.getClass().getName())).count() == 1;
        assert serIns.stream().filter(cla -> "org.contract.ioc.domain.TestDomain".equals(cla.getClass().getName())).count() == 1;
        assert serIns.stream().filter(cla -> "org.contract.ioc.service.TestService".equals(cla.getClass().getName())).count() == 1;
    }
}