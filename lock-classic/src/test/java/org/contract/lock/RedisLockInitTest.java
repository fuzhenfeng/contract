package org.contract.lock;

import org.contract.common.InitException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RedisLockInitTest extends AbsLockTest {

    DistributedLock distributedLock;

    @BeforeEach
    void setUp() throws InitException {
        fileName = "redis.properties";
        super.setUp();
        distributedLock = new RedisLock();
    }

    @AfterEach
    void tearDown() {
        super.tearDown();
        distributedLock = null;
    }

    @Test
    void init() {
        try {
            distributedLock.init(config);
        } catch (InitException e) {
            assert false;
        }
    }
}