package org.contract.job;

import org.contract.config.Config;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 任务中心
 * 任务可以用进程、线程或者协程完成
 * @author fuzhenfeng
 */
public interface Job {

    /**
     * 启动任务
     * 根据配置启动任务，但是配置不是必须。
     * @param config
     */
    void start(Config config);

    /**
     * 结束任务
     */
    void stop();

    /**
     * 提交任务
     * @param jobHandle
     * @param <R>
     */
    <R> void commit(JobHandle<R> jobHandle);

    /**
     * 查询任务结果
     * 一般而言是{@link JobHandle}中的id
     * 如果在任务中心在内存中，应该告知会出现查询不到的可能（即查询不到也不表示任务没有结果）。
     * @param id
     * @return
     * @param <R>
     */
    <R> R getResult(String id);
}
