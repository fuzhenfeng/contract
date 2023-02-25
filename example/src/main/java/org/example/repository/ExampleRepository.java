package org.example.repository;

import java.util.Date;

public class ExampleRepository {

    public String get(String body) {
        return new Date().toString();
    }
}
