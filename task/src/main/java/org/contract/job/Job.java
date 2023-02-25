package org.contract.job;

import org.contract.config.Config;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface Job {
    void init(Config config);

    void commit(Runnable runnable);

    <T> Future<T> commit(Callable<T> callable);
}
