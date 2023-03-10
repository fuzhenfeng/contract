package org.contract.cache;

/**
 * 缓存接口
 * @author fuzhenfeng
 */
public interface Cache {

    /**
     * 缓存容量
     * 创建时设定
     * @return 返回
     */
    int capacity();

    /**
     * 缓存大小
     * 实际缓存大小，一般小于等于{@link #capacity}缓存容量
     * @return
     */
    int size();

    /**
     * 根据key存储value，如果key相同可以覆盖，缓存需要实现淘汰策略，该方法不应该有异常。
     * @param key
     * @param value
     */
    void put(String key, String value);

    /**
     * 根据key查询value，如果没有缓存或者如果缓存被淘汰，可能返回空。
     * @param key
     * @return
     */
    String get(String key);
}
