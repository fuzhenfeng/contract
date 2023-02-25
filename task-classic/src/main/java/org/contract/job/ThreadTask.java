package org.contract.job;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadTask {

    Executor executor = Executors.newFixedThreadPool(3);
}
