package org.contract.remote;

import org.contract.ioc.InstanceDefinition;

public class RemoteInstance<T> implements InstanceDefinition {

    private T t;

    public RemoteInstance(T t) {
        this.t = t;
    }

    @Override
    public T getDefinition() {
        return t;
    }
}
