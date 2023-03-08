package org.contract.cache;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocalCacheTest {

    Cache cache;

    @BeforeEach
    void setUp() {
        cache = new LocalCache(10);
    }

    @AfterEach
    void tearDown() {
        cache = null;
    }

    @Test
    void testCapacity() {
        for (int i = 0; i < 20; i++) {
            cache = new LocalCache(i);
            int capacity = cache.capacity();
            assert capacity == i;
        }
    }

    @Test
    void testSize() {
        for (int i = 0; i < 20; i++) {
            int size = cache.size();
            assert size == 0;
        }

        for (int i = 0; i < 0; i++) {
            cache.put(String.valueOf(i), String.valueOf(i));
        }
        assert cache.size() == 0;

        for (int i = 0; i < 10; i++) {
            cache.put(String.valueOf(i), String.valueOf(i));
        }
        assert cache.size() == 10;

        for (int i = 0; i < 20; i++) {
            cache.put(String.valueOf(i), String.valueOf(i));
        }
        int size = cache.size();
        assert cache.size() == 10;
    }

    @Test
    void testPut() {
        for (int i = 0; i < 20; i++) {
            cache.put(String.valueOf(i), String.valueOf(i));
            String value = cache.get(String.valueOf(i));
            assert value != null;
            assert String.valueOf(i).equals(value);
        }
    }

    @Test
    void get() {
        String value = cache.get(String.valueOf(1));
        assert value == null;

        cache.put(String.valueOf(1), String.valueOf(1));
        String value1 = cache.get(String.valueOf(1));
        assert value1 != null;
        assert String.valueOf(1).equals(value1);
    }
}