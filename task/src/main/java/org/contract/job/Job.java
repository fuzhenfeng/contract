package org.contract.job;

import org.contract.config.Config;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface Job {
    void start(Config config);

    void stop();

    <R> void commit(JobHandle<R> jobHandle);

    <R> R getResult(String id);
}
