package org.contract.cache;

public interface Cache {
    void put(String key, String value);
    String get(String key);
}
