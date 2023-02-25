package org.contract.ioc;

import org.contract.common.InitException;
import org.contract.config.Config;
import org.contract.config.Environment;

import java.util.List;

public interface Ioc {

    void init(Config config, Environment environment);

    void registerByDirectory(String layer, String path) throws InitException;

    <T> void register(String layer, InstanceDefinition<T> instanceDefinition, String name) throws InitException;

    List<Class> getClasses(String layer);

    List<Object> getInstances(String layer);
}
