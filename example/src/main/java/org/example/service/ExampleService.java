package org.example.service;

import org.example.domain.ExampleDomain;

public class ExampleService {
    ExampleDomain domain;

    public String get(String body) {
        return domain.get(body);
    }
}
