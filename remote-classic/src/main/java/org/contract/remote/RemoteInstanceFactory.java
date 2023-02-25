package org.contract.remote;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import org.contract.common.Layer;

public class RemoteInstanceFactory<T> implements RemoteFactory<T> {

    private Layer layer;
    private Class<T> aClass;

    public RemoteInstanceFactory(Layer layer, Class<T> aClass) {
        this.layer = layer;
        this.aClass = aClass;
    }

    @Override
    public T getInstance() {
        try {
            ProxyFactory factory = new ProxyFactory();
            factory.setInterfaces(new Class[]{aClass});
            factory.setHandler(getHandle(layer));

            Class<?> c = factory.createClass();
            T t = (T) c.newInstance();
            return t;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private MethodHandler getHandle(Layer layer) {
        return new RemoteMethodHandler(aClass);
    }
}
