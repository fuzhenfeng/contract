package org.contract.ioc.domain;


import org.contract.ioc.repository.TestRepository;

public class TestDomain {
    TestRepository testRepository;

    public String get(String body) {
        return testRepository.get(body);
    }
}
