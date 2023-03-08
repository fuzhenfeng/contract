package org.contract.lock;

import org.contract.common.InitException;
import org.contract.common.RunException;
import org.contract.config.Config;

import java.util.concurrent.TimeUnit;

public interface Lock {

    void init(Config config) throws InitException;

    void close() throws RunException;

    boolean tryLock(long timeout, TimeUnit unit);

    boolean tryUnLock(long timeout, TimeUnit unit);
}
