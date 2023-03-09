package org.contract.lock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.contract.common.InitException;
import org.contract.common.RunException;
import org.contract.common.StringUtils;
import org.contract.config.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author fuzhenfeng
 */
public class RedisLock implements DistributedLock {
    private final static Logger log = LogManager.getLogger(RedisLock.class);

    private static final String LOCK_SUCCESS = "OK";
    private String address;
    private Integer port;
    private Jedis jedis;

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

        jedis = new Jedis(this.address, port);
    }

    @Override
    public boolean tryLock(String key, String identity, long timeout, TimeUnit unit) {
        SetParams setParams = new SetParams().nx().ex(10);
        String result = jedis.set(key, identity, setParams);
        return LOCK_SUCCESS.equals(result);
    }

    @Override
    public boolean tryUnLock(String key, String identity, long timeout, TimeUnit unit) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(key), Collections.singletonList(identity));
        return LOCK_SUCCESS.equals(result);
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
