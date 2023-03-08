package org.contract.lock;

import org.contract.common.InitException;
import org.contract.common.RunException;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

public class RedisLock implements DistributedLock {

    Jedis jedis;

    public RedisLock() {
        jedis = new Jedis("127.0.0.1", 6379);
    }

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
