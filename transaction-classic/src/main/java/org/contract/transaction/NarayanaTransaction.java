package org.contract.transaction;

import org.contract.config.Config;

import java.io.File;

public class NarayanaTransaction implements XATransaction {

    @Override
    public void init(Config config, final File yamlFile) {

    }

    @Override
    public void prepare() {

    }

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }
}
