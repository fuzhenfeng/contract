package org.contract.domain;

import org.contract.ioc.Ioc;
import org.contract.server.HttpReq;
import org.contract.server.HttpResp;

/**
 * 路由器
 * 路由器由控制器完成初始化
 * 路由器和服务分发器进行交互，完成请求。
 * 路由器责任更轻，主要是完成路由。
 * @author fuzhenfeng
 */
public interface Route {

    /**
     * 路由器初始化
     * @param ioc
     * @param namespace
     */
    void init(Ioc ioc, String namespace);

    /**
     * 根据请求进行路由
     * @param httpReq
     * @param namespace
     * @return
     */
    HttpResp request(HttpReq httpReq, String namespace);
}
