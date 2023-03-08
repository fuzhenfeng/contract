package org.contract.domain;

import org.contract.common.InitException;
import org.contract.common.RunException;
import org.contract.config.Environment;
import org.contract.config.Config;
import org.contract.ioc.Ioc;

/**
 * 服务提供者接口
 * 该接口是面向内部调用的接口，一般是控制器的请求。
 * 服务提供者可以和控制器进行交互，完成请求，但是这样做使得控制器责任较重，
 * 所以建议实现路由器接口，使得功能更加聚合，方便扩展。
 * @author fuzhenfeng
 * @see    server-serverimpl.puml
 */
public interface Service {
    /**
     * 服务提供者初始化
     * 用户可以根据配置文件和环境变量进行初始化，当然不是必须要使用配置文件和环境变量。
     * 需要注意的是初始化需要向{@link Ioc}容器完成注册
     * @param config
     * @param environment
     * @param ioc
     * @throws InitException
     */
    void init(Config config, Environment environment, Ioc ioc) throws InitException;

    /**
     * 启动服务提供者
     * 服务提供者可以自行实现符合标准化或者自定义协议的服务，以便后面进行服务接收处理。
     * @throws InterruptedException
     */
    void start() throws InterruptedException;

    /**
     * 结束服务提供者
     * 服务提供者需要在结束时完成收尾工作，例如释放相应的资源，结束还没有来得及完成工作（取决于自己）。
     * @throws InterruptedException
     */
    void stop() throws InterruptedException;
}