package org.contract.remote;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.contract.common.InitException;
import org.contract.common.NameSpace;
import org.contract.common.StringUtils;
import org.contract.config.AppConfig;
import org.contract.config.Config;
import org.contract.config.Environment;
import org.contract.ioc.Ioc;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class HttpRemote implements Remote {
    private final static Logger log = LogManager.getLogger(HttpRemote.class);

    private Config config;
    private Environment environment;
    private Ioc ioc;
    private AppConfig appConfig;

    @Override
    public void init(Config config, Environment environment, Ioc ioc) throws InitException {
        if(config == null || config.getAppConfig() == null || environment == null || ioc == null) {
            throw new InitException("appConfig is a must");
        }
        this.config = config;
        this.environment = environment;
        this.ioc = ioc;
        this.appConfig = config.getAppConfig();

        if(StringUtils.isNotBlank(appConfig.getControllerRpcName())) {
            String path = getPath(appConfig.getAppRealmName() + StringUtils.LINUX_SEPARATOR +
                    appConfig.getControllerName() + StringUtils.LINUX_SEPARATOR +
                    appConfig.getControllerRpcName());
            registerIoc(NameSpace.CONTROLLER, path);
        }

        if(StringUtils.isNotBlank(appConfig.getServiceRpcName())) {
            String path = getPath(appConfig.getAppRealmName() + StringUtils.LINUX_SEPARATOR +
                    appConfig.getServiceDomainName() + StringUtils.LINUX_SEPARATOR +
                    appConfig.getServiceRpcName());
            registerIoc(NameSpace.SERVICE, path);
        }
    }

    private void registerIoc(NameSpace nameSpace, String path) throws InitException {
        List<Class> classes = getClasses(path);
        Iterator<Class> iterator = classes.iterator();
        while (iterator.hasNext()) {
            Class next = iterator.next();
            RemoteFactory remoteFactory = new RemoteInstanceFactory(nameSpace, next);
            RemoteInstance remoteInstance = new RemoteInstance(remoteFactory.getInstance());
            ioc.register(nameSpace.name(), remoteInstance, next.getName());
        }
        // todo 不考虑接口属性
    }

    private List<Class> getClasses(String path) throws InitException {
        File file = new File(path);
        if(!file.exists()) {
            return new ArrayList<>();
        }
        Iterator<File> files = Arrays.stream(file.listFiles()).iterator();
        List<Class> classes = new ArrayList<>(file.listFiles().length);
        ClassLoader classLoader = environment.getClassLoader();
        try {
            while (files.hasNext()) {
                File next = files.next();
                if(!next.isFile() || !next.getName().contains(".class"))
                    continue;
                String className = getClassName(next.getPath(), classLoader);
                Class<?> aClass = classLoader.loadClass(className);
                classes.add(aClass);
            }
            return classes;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InitException("class load error");
        }
    }

    private String getClassName(String name, ClassLoader classLoader) {
        String path = classLoader.getResource(StringUtils.EMPTY).getPath()
                .replace("file:", StringUtils.EMPTY);
        return name
                .replace(path, StringUtils.EMPTY)
                .replace(".class", StringUtils.EMPTY)
                .replace(StringUtils.LINUX_SEPARATOR, StringUtils.REALM_SEPARATOR);
    }

    private String getPath(String path) {
        URL resource = environment.getClassLoader().getResource(StringUtils.EMPTY);
        return resource.getPath() + path.replace(StringUtils.REALM_SEPARATOR, StringUtils.LINUX_SEPARATOR);
    }
}
