package org.contract.config;

/**
 * 环境变量
 * 考虑到用户可能需要自定义类加载器进行加载，封装此对象在模块启动是可以该对象的加载器进行加载。
 * @author fuzhenfeng
 */
public class Environment {
    private ClassLoader classLoader;

    public Environment(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
