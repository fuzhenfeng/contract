package org.contract.cache;

/**
 * 缓存数据源
 * 通过此接口加载数据，应该允许加载到空数据。
 * @author fuzhenfeng
 */
public interface DataSource {
    String load(String key);
}
