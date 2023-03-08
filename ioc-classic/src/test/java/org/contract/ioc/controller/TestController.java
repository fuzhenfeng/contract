package org.contract.ioc.controller;

import org.contract.ioc.controller.component.TestComponent;

import java.util.Date;

public class TestController {

    TestComponent testComponent;

    public String get(String body) {
        return testComponent.get(body);
    }

    public String post(String body) {
        return new Date().toString();
    }
}
