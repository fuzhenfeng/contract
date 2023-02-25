package org.example.domain;

import org.example.repository.ExampleRepository;

public class ExampleDomain {
    ExampleRepository exampleRepository;

    public String get(String body) {
        return exampleRepository.get(body);
    }
}
