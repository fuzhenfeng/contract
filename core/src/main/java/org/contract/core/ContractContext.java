package org.contract.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.contract.cache.Cache;
import org.contract.common.InitException;
import org.contract.config.Environment;
import org.contract.config.Config;
import org.contract.controller.Controller;
import org.contract.controller.Route;
import org.contract.domain.Service;
import org.contract.ioc.Ioc;
import org.contract.remote.Remote;
import org.contract.job.Job;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.ServiceLoader;

public class ContractContext {
    private final static Logger log = LogManager.getLogger(ContractContext.class);

    private Config config;
    private Route route;
    private Controller controller;
    private Service service;
    private Cache cache;
    private Remote remote;
    private Ioc ioc;
    private Job job;

    public static Builder newBuilder() throws InitException {
        return new Builder();
    }

    public Config getConfig() {
        return config;
    }

    public Controller getController() {
        return controller;
    }

    public Service getDomain() {
        return service;
    }

    public static class Builder {
        private String[] args;
        private ContractContext contractContext;
        private ClassLoader classLoader;
        private Environment environment;

        public Builder args(String[] args) {
            this.args = args;
            // todo
            return this;
        }

        public Builder classLoader(ClassLoader classLoader) {
            if(classLoader == null)
                classLoader = ContractContext.class.getClassLoader();
            this.classLoader = classLoader;
            this.environment = new Environment(classLoader);
            return this;
        }

        public Builder config(String fileName) throws InitException {
            ContractContext contractContext = new ContractContext();
            Properties properties = getProperties(fileName);

            Config config = load(Config.class);
            if(config != null) {
                config.loadConfig(properties);
                contractContext.config = config;
            }

            Ioc ioc = load(Ioc.class);
            if(ioc != null) {
                ioc.init(config, environment);
                contractContext.ioc = ioc;
            }
            Remote remote = load(Remote.class);
            if(remote != null) {
                remote.init(config, environment, contractContext.ioc);
                contractContext.remote = remote;
            }
            Controller controller = load(Controller.class);
            if(controller != null) {
                controller.init(config, environment, contractContext.ioc);
                contractContext.controller = controller;
            }
            Service service = load(Service.class);
            if(service != null) {
                service.init(config, environment, contractContext.ioc);
                contractContext.service = service;
            }
            this.contractContext = contractContext;
            return this;
        }

        public ContractContext build() {
            return contractContext;
        }

        private Properties getProperties(String fileName) throws InitException {
            Properties properties = new Properties();
            try {
                InputStream input = classLoader.getResourceAsStream(fileName);
                try {
                    properties.load(input);
                } finally {
                    input.close();
                }
            } catch (Throwable e) {
                log.warn("Failed to load " + fileName + " file from " + fileName + "(ignore this file): " + e.getMessage(), e);
            }
            return properties;
        }

        private <T> T load(Class<T> clazz) {
            Iterator<T> iterator = ServiceLoader.load(clazz).iterator();
            if(iterator.hasNext()) {
                T next = iterator.next();
                if(iterator.hasNext()) {
                    log.warn(clazz.getName() + " are multiple implementations.");
                }
                return next;
            }
            return null;
        }
    }
}
