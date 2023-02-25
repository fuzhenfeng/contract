package org.contract.config;

public class AppConfig {
    private String appId;
    private String appName;
    private String appRealmName;
    private String appVersion;
    private String controllerName;
    private String controllerComponentName;
    private String controllerRpcName;
    private String serviceName;
    private String serviceDomainName;
    private String serviceRpcName;
    private String repositoryName;
    private String repositoryDaoName;

    public AppConfig(String appId, String appName, String appRealmName, String appVersion) {
        this.appId = appId;
        this.appName = appName;
        this.appRealmName = appRealmName;
        this.appVersion = appVersion;
    }

    public AppConfig setExtra(String controllerName, String controllerComponentName, String controllerRpcName, String serviceName, String serviceDomainName, String serviceRpcName, String repositoryName, String repositoryDaoName) {
        this.controllerName = controllerName;
        this.controllerComponentName = controllerComponentName;
        this.controllerRpcName = controllerRpcName;
        this.serviceName = serviceName;
        this.serviceDomainName = serviceDomainName;
        this.serviceRpcName = serviceRpcName;
        this.repositoryName = repositoryName;
        this.repositoryDaoName = repositoryDaoName;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppRealmName() {
        return appRealmName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getControllerName() {
        return controllerName;
    }

    public String getControllerComponentName() {
        return controllerComponentName;
    }

    public String getControllerRpcName() {
        return controllerRpcName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceDomainName() {
        return serviceDomainName;
    }

    public String getServiceRpcName() {
        return serviceRpcName;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public String getRepositoryDaoName() {
        return repositoryDaoName;
    }
}
