package org.contract.transaction;

import org.contract.config.Config;

import java.io.File;

public interface XATransaction {
    void init(Config config, final File yamlFile);
    void prepare();
    void commit();
    void rollback();
}
