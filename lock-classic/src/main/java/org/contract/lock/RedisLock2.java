package org.contract.lock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.contract.common.InitException;
import org.contract.common.RunException;
import org.contract.common.StringUtils;
import org.contract.config.Config;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RedisLock2 implements DistributedLock {
    private final static Logger log = LogManager.getLogger(RedisLock2.class);

    private String address;
    private Integer port;
    private RedissonClient redissonClient;

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

        org.redisson.config.Config redissonConfig = new org.redisson.config.Config();
        redissonConfig.useClusterServers().addNodeAddress(this.address);
        redissonClient = Redisson.create(redissonConfig);
    }

    @Override
    public boolean tryLock(String key, String identity, long timeout, TimeUnit unit) {
        redissonClient.getFairLock("");
        return false;
    }

    @Override
    public boolean tryUnLock(String key, String identity, long timeout, TimeUnit unit) {
        return false;
    }

    @Override
    public void close() throws RunException {

    }
}
