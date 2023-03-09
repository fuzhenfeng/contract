package org.contract.lock;

import org.contract.common.InitException;
import org.contract.common.RunException;
import org.contract.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 * 分布式环境下获取资源的访问权限
 * 根据CAP理论，只能在一致性和可用性做平衡
 */
public interface DistributedLock {
    /**
     * 初始化
     * 主要是测试配置参数，建立锁对象
     * @param config
     * @throws InitException
     */
    void init(Config config) throws InitException;

    /**
     * 获取锁
     * 根据资源key和身份标志identity获取锁
     * @param key 锁定的资源标志
     * @param identity 同一身份应该允许重入
     * @param timeout
     * @param unit
     * @return
     */
    boolean tryLock(String key, String identity, long timeout, TimeUnit unit);

    /**
     * 释放锁
     * @param key
     * @param identity
     * @param timeout
     * @param unit
     * @return
     */
    boolean tryUnLock(String key, String identity, long timeout, TimeUnit unit);

    /**
     * 释放资源
     * @throws RunException
     */
    void close() throws RunException;
}
