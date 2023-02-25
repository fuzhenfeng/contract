package org.contract.domain;

import org.contract.common.InitException;
import org.contract.common.RunException;
import org.contract.config.Environment;
import org.contract.config.Config;
import org.contract.ioc.Ioc;

public interface Service {
    void init(Config config, Environment environment, Ioc ioc) throws InitException;
    void start() throws InterruptedException;
    void stop() throws InterruptedException;
}