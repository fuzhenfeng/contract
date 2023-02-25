package org.contract.remote;

import org.contract.common.InitException;
import org.contract.config.Environment;
import org.contract.config.Config;
import org.contract.ioc.Ioc;

public interface Remote {
    void init(Config config, Environment environment, Ioc ioc) throws InitException;
}
