package org.contract.lock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.contract.common.InitException;
import org.contract.common.RunException;
import org.contract.common.StringUtils;
import org.contract.config.Config;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LockImpl implements Lock {
    private final static Logger log = LogManager.getLogger(LockImpl.class);

    private DistributedLock distributedLock;

    @Override
    public void init(Config config) throws InitException {
        Map<String, String> map = config.getConfig();
        if(map == null) {
            throw new InitException("config is a must");
        }
        String lock = map.get("lock");
        if(StringUtils.isBlank(lock)) {
            return;
        }
        if("mysql".equals(lock)) {
            distributedLock = new MysqlLock();
        }
        if("redis".equals(lock)) {
            distributedLock = new RedisLock();
        }
        if("zookeeper".equals(lock)) {
            distributedLock = new ZookeeperLock();
        }
    }

    @Override
    public void close() throws RunException {

    }

    @Override
    public boolean tryLock(String key, String identity, long timeout, TimeUnit unit) {
        return distributedLock.tryLock(key, identity, timeout, unit);
    }

    @Override
    public boolean tryUnLock(String key, String identity, long timeout, TimeUnit unit) {
        return distributedLock.tryUnLock(key, identity, timeout, unit);
    }
}
