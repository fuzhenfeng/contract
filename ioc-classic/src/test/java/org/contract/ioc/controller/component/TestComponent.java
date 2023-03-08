package org.contract.ioc.controller.component;


import org.contract.ioc.controller.remote.TestRemote;

public class TestComponent {
    TestRemote remote;

    public String get(String body) {
        return remote.get(body);
    }
}
