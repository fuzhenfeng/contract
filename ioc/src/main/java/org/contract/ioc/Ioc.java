package org.contract.ioc;

import org.contract.common.InitException;
import org.contract.config.Config;
import org.contract.config.Environment;

import java.util.List;

/**
 * ioc
 * 缓存并注入实例，方便进行框架操作。
 * 需要注意不是所有的实例都托管到ioc，托管到ioc的实例应该是面向接口的，或者是模块与模块之前、层与层之前的交互。
 * @author fuzhenfeng
 */
public interface Ioc {

    /**
     * 初始化准备工作
     * @param config
     * @param environment
     */
    void init(Config config, Environment environment);

    /**
     * 根据命名空间和文件路径注入
     * @param namespace
     * @param path
     * @throws InitException
     */
    void registerByDirectory(String namespace, String path) throws InitException;

    /**
     * 根据命名空间和自定义实例注入
     * @param namespace
     * @param instanceDefinition
     * @param name
     * @param <T>
     * @throws InitException
     */
    <T> void register(String namespace, InstanceDefinition<T> instanceDefinition, String name) throws InitException;

    /**
     * 查询加载的class
     * @param namespace
     * @return
     */
    List<Class> getClasses(String namespace);

    /**
     * 查询加载的实例
     * @param namespace
     * @return
     */
    List<Object> getInstances(String namespace);
}
