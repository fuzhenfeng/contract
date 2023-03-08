package org.contract.ioc.repository;

import java.util.Date;

public class TestRepository {

    public String get(String body) {
        return new Date().toString();
    }
}
