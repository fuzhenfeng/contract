package org.contract.config;

import org.contract.common.InitException;

import java.util.Map;
import java.util.Properties;

/**
 * 配置接口
 * 通过此接口的实现加载配置数据，考虑到数据依赖的问题，加载配置是整个应用最前置的操作。
 * 此操作不应事件过长逻辑过于复杂。
 * @author fuzhenfeng
 */
public interface Config {

    /**
     * 通过Properties文件加载
     * @param properties
     * @throws InitException
     */
    void loadConfig(Properties properties) throws InitException;

    /**
     * 查询所有的配置。
     * 应用大部分情况下可以根据{@link AppConfig}获取相关信息，
     * 但是模块加载仍然有定制化的需求，所以额外的不确定的配置信息可以从这里获取，
     * 这里返回的是所有配置信息，包括{@link AppConfig}内的信息
     * @return
     */
    Map<String, String> getConfig();

    /**
     * 查询某项的配置。
     * @return
     */
    String getConfigValue(String key);

    /**
     * 查询AppConfig配置
     * AppConfig配置仍然取自于Properties文件，只是进行了对象封装，因为应用必须获取这些信息。
     * @return
     */
    AppConfig getAppConfig();
}
