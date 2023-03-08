package org.contract.job;

public interface JobHandle<R> {
    String id();
    String name();
    int priority();
    void run();
    default R getResult(){
        return null;
    }
}
