package org.contract.lock;

import org.contract.common.InitException;
import org.contract.common.RunException;

import java.util.concurrent.TimeUnit;

public class ZookeeperLock implements DistributedLock {

    @Override
    public void init() throws InitException {

    }

    @Override
    public boolean tryLock(long timeout, TimeUnit unit) {
        return false;
    }

    @Override
    public boolean tryUnLock(long timeout, TimeUnit unit) {
        return false;
    }

    @Override
    public void close() throws RunException {

    }
}
