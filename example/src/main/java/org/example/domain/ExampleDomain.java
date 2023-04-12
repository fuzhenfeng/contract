package org.example.domain;

import org.example.repository.ExampleRepository;

public class ExampleDomain {
    ExampleDomain2 exampleDomain2;

    ExampleRepository exampleRepository;

    public String get(String body) {
        return exampleDomain2.get(body);
    }

    public String get2(String body) {
        return exampleRepository.get(body);
    }
}
