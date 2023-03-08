package org.contract.lock;

import org.contract.common.InitException;
import org.contract.common.RunException;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 * 分布式环境下获取资源的访问权限
 * 根据CAP理论，只能在一致性和可用性做平衡
 */
public interface DistributedLock {
    void init() throws InitException;
    boolean tryLock(long timeout, TimeUnit unit);
    boolean tryUnLock(long timeout, TimeUnit unit);
    void close() throws RunException;
}
