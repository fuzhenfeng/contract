package org.example.repository.dao;

import org.contract.common.SQL;

public interface ExampleDao {

    @SQL(str = "select count(*) from example where id = %d", result = Integer.class)
    Integer select(Integer body);
}
