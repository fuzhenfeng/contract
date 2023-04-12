package org.example.repository;

import org.example.repository.dao.ExampleDao;

public class ExampleRepository {

    ExampleDao exampleDao;

    public String get(String body) {
        return exampleDao.select(Integer.valueOf(body)) + "";
    }
}
