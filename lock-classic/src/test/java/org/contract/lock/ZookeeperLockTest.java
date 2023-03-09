package org.contract.lock;

import org.contract.common.InitException;
import org.contract.common.RunException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

class ZookeeperLockTest extends ZookeeperLockInitTest {

    @BeforeEach
    void setUp() throws InitException {
        super.setUp();
        super.init();
    }

    @AfterEach
    void tearDown() {
        super.tearDown();
        close();
    }

    @Test
    void testLock() {
        String key = "test";
        String identity = "test";
        long timeout = 1;
        TimeUnit unit = TimeUnit.SECONDS;

        for (int i = 0; i < 10; i++) {
            boolean lock = distributedLock.tryLock(key, identity, timeout, unit);
            assert lock;

            boolean unlock = distributedLock.tryUnLock(key, identity, timeout, unit);
            assert unlock;
        }
    }

    @Test
    void testLockRepeat() {
        String key = "test";
        String identity = "test";
        long timeout = 1;
        TimeUnit unit = TimeUnit.SECONDS;

        int count = 10;
        for (int i = 0; i < count; i++) {
            boolean lock = distributedLock.tryLock(key, identity, timeout, unit);
            assert lock;
        }

        for (int i = 0; i < count; i++) {
            boolean unlock = distributedLock.tryUnLock(key, identity, timeout, unit);
            assert unlock;
        }
    }

    @Test
    void close() {
        try {
            distributedLock.close();
        } catch (RunException e) {
            assert false;
        }
    }
}