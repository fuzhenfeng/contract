package org.contract.remote;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import org.contract.common.NameSpace;

public class RemoteInstanceFactory<T> implements RemoteFactory<T> {

    private NameSpace nameSpace;
    private Class<T> aClass;

    public RemoteInstanceFactory(NameSpace nameSpace, Class<T> aClass) {
        this.nameSpace = nameSpace;
        this.aClass = aClass;
    }

    @Override
    public T getInstance() {
        try {
            ProxyFactory factory = new ProxyFactory();
            factory.setInterfaces(new Class[]{aClass});
            factory.setHandler(getHandle(nameSpace));

            Class<?> c = factory.createClass();
            T t = (T) c.newInstance();
            return t;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private MethodHandler getHandle(NameSpace nameSpace) {
        return new RemoteMethodHandler(aClass);
    }
}
