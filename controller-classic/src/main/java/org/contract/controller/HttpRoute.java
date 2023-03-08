package org.contract.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.contract.common.StringUtils;
import org.contract.ioc.Ioc;
import org.contract.server.HttpReq;
import org.contract.server.HttpResp;
import org.contract.server.MethodInstant;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpRoute implements Route {
    private final static Logger log = LogManager.getLogger(HttpRoute.class);

    private Map<String, Map<String, MethodInstant>> layerCacheMap = new ConcurrentHashMap<>();

    @Override
    public void init(Ioc ioc, String layer) {
        List<Object> instances = ioc.getInstances(layer);
        for (Object obj: instances) {
            Class<?> aClass = obj.getClass();
            Method[] declaredMethods = aClass.getDeclaredMethods();
            for (Method method: declaredMethods) {
                String[] split = aClass.getName()
                        .split(StringUtils.SEPARATOR);
                String name = split[split.length - 1]
                        .replace("Controller", StringUtils.EMPTY);
                Map<String, MethodInstant> layerCache = layerCacheMap.get(layer);
                if(layerCache == null) {
                    layerCache = new ConcurrentHashMap<>();
                    layerCacheMap.put(layer, layerCache);
                }
                layerCache.put(StringUtils.captureName(name) + StringUtils.REALM_SEPARATOR + method.getName(), new MethodInstant(method, obj));
            }
        }
    }

    @Override
    public HttpResp request(HttpReq httpReq, String layer) {
        try {
            MethodInstant methodInstant = getMethodInstant(httpReq, layer);
            if(methodInstant == null) {
                log.error("\"" + httpReq.getMethod() + " " + httpReq.getRealmName() + " \" no route found");
                return new HttpResp(500);
            }
            String body = methodInstant.call(httpReq.getBody());
            return new HttpResp(200, body);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new HttpResp(500);
        }
    }

    private MethodInstant getMethodInstant(HttpReq httpReq, String layer) {
        String realmName = httpReq.getRealmName()
                .replace(StringUtils.LINUX_SEPARATOR, StringUtils.EMPTY);
        String method = httpReq.getMethod().toLowerCase();
        Map<String, MethodInstant> layerCache = layerCacheMap.get(layer);
        String key = realmName + StringUtils.REALM_SEPARATOR + method;
        return layerCache.get(key);
    }
}
