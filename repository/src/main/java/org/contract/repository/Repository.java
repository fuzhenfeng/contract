package org.contract.repository;

import org.contract.config.Environment;
import org.contract.config.Config;

public interface Repository {

    void init(Config config, Environment environment);

}