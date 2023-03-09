package org.contract.lock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.contract.common.InitException;
import org.contract.common.RunException;
import org.contract.common.StringUtils;
import org.contract.config.Config;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RedisLock implements DistributedLock {
    private final static Logger log = LogManager.getLogger(RedisLock.class);

    private String address;
    private Integer port;
    private Jedis jedis;

    @Override
    public void init(Config config) throws InitException {
        Map<String, String> map = config.getConfig();
        this.address = map.get("redis.address");
        try {
            this.port = Integer.valueOf(map.get("redis.port"));
        } catch (NumberFormatException e) {
            // ignore
        }
        if(StringUtils.isBlank(this.address) || port == null) {
            throw new InitException("redis parameter(address port) is a must");
        }

        jedis = new Jedis(this.address, port);
    }

    @Override
    public boolean tryLock(String key, String identity, long timeout, TimeUnit unit) {
        return false;
    }

    @Override
    public boolean tryUnLock(String key, String identity, long timeout, TimeUnit unit) {
        return false;
    }

    @Override
    public void close() throws RunException {
        try {
            jedis.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
