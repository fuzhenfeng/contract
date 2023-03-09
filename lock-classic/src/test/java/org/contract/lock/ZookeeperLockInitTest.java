package org.contract.lock;

import org.contract.common.InitException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ZookeeperLockInitTest extends AbsLockTest {

    DistributedLock distributedLock;

    @BeforeEach
    void setUp() throws InitException {
        fileName = "zookeeper.properties";
        super.setUp();
        distributedLock = new ZookeeperLock();
    }

    @AfterEach
    void tearDown() {
        super.tearDown();
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