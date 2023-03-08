package org.contract.lock;

import org.contract.common.InitException;
import org.contract.common.RunException;

import java.sql.*;
import java.util.concurrent.TimeUnit;

public class MysqlLock implements DistributedLock {

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
