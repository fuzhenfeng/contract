package org.contract.config;

import org.contract.common.ImmutableUtils;
import org.contract.common.InitException;
import org.contract.common.PropertiesHelper;
import org.contract.common.StringUtils;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class PropertiesConfig implements Config {

    private ReentrantLock reentrantLock = new ReentrantLock();

    private AppConfig appConfig;

    private Map<String, String> config;

    @Override
    public void loadConfig(Properties properties) throws InitException {
        buildConfig(properties);

        buildAppConfig(new PropertiesHelper(properties));
    }

    @Override
    public Map<String, String> getConfig() {
        return config;
    }

    @Override
    public AppConfig getAppConfig() {
        return appConfig;
    }

    private void buildAppConfig(PropertiesHelper helper) throws InitException {
        String appId = helper.get("appId");
        String appName = helper.get("appName");
        String appRealmName = helper.get("appRealmName");
        String appVersion = helper.get("appVersion");
        if(StringUtils.isBlank(appId))
            throw new InitException("appId is a must");
        if(StringUtils.isBlank(appName))
            throw new InitException("appName is a must");
        if(StringUtils.isBlank(appRealmName))
            throw new InitException("appRealmName is a must");
        if(StringUtils.isBlank(appVersion))
            throw new InitException("appVersion is a must");
        appConfig = new AppConfig(appId, appName, appRealmName, appVersion);

        String controllerName = StringUtils.defaultIfBlank(helper.get("controllerName"), "controller");
        String controllerComponentName = StringUtils.defaultIfBlank(helper.get("controllerComponentName"), "component");
        String controllerRpcName = StringUtils.defaultIfBlank(helper.get("controllerRpcName"), "remote");
        String serviceName = StringUtils.defaultIfBlank(helper.get("serviceName"), "service");
        String serviceDomainName = StringUtils.defaultIfBlank(helper.get("serviceDomainName"), "domain");
        String serviceRpcName = StringUtils.defaultIfBlank(helper.get("serviceRpcName"), "remote");
        String repositoryName = StringUtils.defaultIfBlank(helper.get("repositoryName"), "repository");
        String repositoryDaoName = StringUtils.defaultIfBlank(helper.get("repositoryDaoName"), "dao");
        appConfig.setExtra(controllerName, controllerComponentName, controllerRpcName, serviceName, serviceDomainName, serviceRpcName, repositoryName, repositoryDaoName);
    }

    private void buildConfig(Properties properties) {
        Map<String, String> collect = new HashMap<>();
        Iterator<Map.Entry<Object, Object>> iterator = properties.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> next = iterator.next();
            collect.put(
                    StringUtils.defaultIfBlank2(next.getKey(), StringUtils.EMPTY),
                    StringUtils.defaultIfBlank2(next.getValue(), StringUtils.EMPTY)
            );
        }
        config = ImmutableUtils.map(collect);
    }
}
