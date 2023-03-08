package org.contract.cache;

/**
 * 缓存监听
 * 如果淘汰发生，可以通过此接口监听以便做出策略。
 * @author fuzhenfeng
 */
public interface Listener {
    void notify(String key, String value);
}
