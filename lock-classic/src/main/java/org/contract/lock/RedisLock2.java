package org.contract.lock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.contract.common.InitException;
import org.contract.common.RunException;
import org.contract.common.StringUtils;
import org.contract.config.Config;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * redisson 实现
 * 使用lua脚本实现原子操作
 * 使用scheduleExpirationRenewal实现锁续期
 * @author fuzhenfeng
 */
public class RedisLock2 implements DistributedLock {
    private final static Logger log = LogManager.getLogger(RedisLock2.class);

    private String address;
    private Integer port;
    private RedissonClient redissonClient;

    @Override
    public void init(Config config) throws InitException {
        this.address = config.getConfigValue("redis.address");
        try {
            this.port = Integer.valueOf(config.getConfigValue("redis.port"));
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

    /**
     * 加锁
     * 根据线程重入，identity设置无效
     * @param key 锁定的资源标志
     * @param identity 同一身份应该允许重入
     * @param timeout
     * @param unit
     * @return
     */
    @Override
    public boolean tryLock(String key, String identity, long timeout, TimeUnit unit) {
        RLock lock = redissonClient.getLock(key);
        try {
            return lock.tryLock(timeout, unit);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 解锁
     * @param key
     * @param identity
     * @param timeout
     * @param unit
     * @return
     */
    @Override
    public boolean tryUnLock(String key, String identity, long timeout, TimeUnit unit) {
        RLock lock = redissonClient.getLock(key);
        lock.unlock();
        return true;
    }

    @Override
    public void close() throws RunException {

    }
}
