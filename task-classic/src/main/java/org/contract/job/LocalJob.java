package org.contract.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.contract.common.Heap;
import org.contract.common.LoopQueue;
import org.contract.common.RunException;
import org.contract.config.Config;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class LocalJob implements Job {
    private final static Logger log = LogManager.getLogger(LocalJob.class);

    private ReentrantLock reentrantLock = new ReentrantLock();
    private AtomicBoolean start = new AtomicBoolean(false);
    private Thread daemon;
    private Thread work;
    private Map<String, JobHandle> map = new HashMap();
    private LoopQueue<JobHandle> resultQueue = new LoopQueue(10, 100);
    private Heap<JobHandle> heap = new Heap<>(Comparator.comparingInt(JobHandle::priority));
    private ThreadJob threadJob = new ThreadJob();
    private String jobImpl;
    @Override
    public void start(Config config) {
        String jobImpl = config.getConfig().get("jobImpl");
        if("thread".equals(jobImpl)) {
            this.jobImpl = jobImpl;
        }
        start.compareAndSet(true, false);
        daemon = new Thread(() -> {
            while (start.get()) {
                doClear();
            }
        });
        daemon.setDaemon(true);
        daemon.start();
        work = new Thread(() -> {
            while (start.get()) {
                doRun();
            }
            log.info("job has ended");
        });
        work.start();
    }

    @Override
    public void stop() {
        start.compareAndSet(false, true);
    }

    @Override
    public <R> void commit(JobHandle<R> jobHandle) {
        try {
            boolean isLock = reentrantLock.tryLock(1, TimeUnit.SECONDS);
            if(!isLock) {
                log.warn("job busy");
                return;
            }
            map.put(jobHandle.id(), jobHandle);
            heap.add(jobHandle);
        } catch (InterruptedException e) {
            log.warn("try lock interrupted");
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override
    public <R> R getResult(String id) {
        JobHandle jobHandle = map.get(id);
        if(jobHandle == null) {
            return null;
        }
        Object result = jobHandle.getResult();
        return (R) result;
    }

    private void doRun() {
        JobHandle object = heap.extractMax();
        if(object == null) {
            return;
        }
        try {
            if("thread".equals(jobImpl)) {
                threadJob.commit(() -> object.run());
            } else {
                // todo
            }
        } catch (Exception e) {
            log.error(object.id() + "-" + object.name() + ":", e);
        }
        try {
            resultQueue.enqueue(object);
        } catch (RunException e) {
            log.error(object.id() + "-" + object.name() + ":", e);
        }
        log.warn("ome jobs were not handled in time");
    }

    private void doClear() {
        if(!resultQueue.canResize()) {
            try {
                reentrantLock.lock();
                JobHandle jobHandle = resultQueue.dequeue();
                map.remove(jobHandle.id());
            } finally {
                reentrantLock.unlock();
            }
        }
    }
}
