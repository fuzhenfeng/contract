package org.contract.job;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadJob {
    Executor executor = Executors.newFixedThreadPool(4);
    public void commit(Runnable command) {
        executor.execute(command);
    }
}
