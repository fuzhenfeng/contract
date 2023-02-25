package org.contract.cache;

public interface Cache {
    void init(DataSource dataSource, Listener listener);
    void get(String key);
}
