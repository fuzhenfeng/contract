package org.contract.config;

public class Environment {
    private ClassLoader classLoader;

    public Environment(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
