package org.contract.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.contract.common.InitException;
import org.contract.common.NameSpace;
import org.contract.common.StringUtils;
import org.contract.config.AppConfig;
import org.contract.config.Environment;
import org.contract.config.Config;
import org.contract.ioc.Ioc;
import org.contract.server.HttpServer;

import java.net.URL;

public class HttpService implements Service {
    private final static Logger log = LogManager.getLogger(HttpService.class);

    private Config config;
    private Environment environment;
    private Ioc ioc;
    private AppConfig appConfig;
    private Route route;
    private HttpServer httpServer;

    @Override
    public void init(Config config, Environment environment, Ioc ioc) throws InitException {
        if(config == null || config.getAppConfig() == null || environment == null || ioc == null) {
            throw new InitException("config is a must");
        }
        this.config = config;
        this.environment = environment;
        this.ioc = ioc;
        this.appConfig = config.getAppConfig();

        register();
        buildRoute();
    }

    @Override
    public void start() throws InterruptedException {
        this.httpServer = new HttpServer(new ServiceDispatch(route), Integer.valueOf(config.getConfig().get("service.port")));
        this.httpServer.start();
    }

    @Override
    public void stop() throws InterruptedException {
        this.httpServer.stop();
        this.httpServer = null;
    }

    private void register() throws InitException {
        String path = getPath();
        ioc.registerByDirectory(NameSpace.SERVICE.name(), path + StringUtils.LINUX_SEPARATOR + "service");
    }

    private void buildRoute() {
        Route route = new HttpRoute();
        route.init(ioc, NameSpace.SERVICE.name());
        this.route = route;
    }

    private String getPath() {
        URL resource = environment.getClassLoader().getResource(StringUtils.EMPTY);
        return resource.getPath() + appConfig.getAppRealmName2();
    }
}
