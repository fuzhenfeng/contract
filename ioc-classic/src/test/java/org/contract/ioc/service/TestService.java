package org.contract.ioc.service;

import org.contract.ioc.domain.TestDomain;

public class TestService {
    TestDomain domain;

    public String get(String body) {
        return domain.get(body);
    }
}
