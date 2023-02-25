package org.contract.config;

import org.contract.common.InitException;

import java.util.Map;
import java.util.Properties;

public interface Config {

    void loadConfig(Properties properties) throws InitException;

    Map<String, String> getConfig();

    AppConfig getAppConfig();
}
