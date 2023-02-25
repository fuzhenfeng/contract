package org.contract.ioc;

public class LocalInstance<T> implements InstanceDefinition<T> {

    private T t;

    public LocalInstance(T t) {
        this.t = t;
    }

    @Override
    public T getDefinition() {
        return t;
    }
}
