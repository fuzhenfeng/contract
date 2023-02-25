package org.contract.job;

import org.contract.config.Config;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class LocalJob implements Job {

    @Override
    public void init(Config config) {

    }

    @Override
    public void commit(Runnable runnable) {

    }

    @Override
    public <T> Future<T> commit(Callable<T> callable) {
        return null;
    }
}
