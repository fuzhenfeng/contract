package org.contract.job;

/**
 * 任务处理器
 * @param <R>
 */
public interface JobHandle<R> {
    /**
     * 任务id
     * 全局唯一
     * @return
     */
    String id();

    /**
     * 任务名称
     * 名称主要用于分类
     * @return
     */
    String name();

    /**
     * 任务优先级
     * 优先级高的应该被优先处理
     * @return
     */
    int priority();

    /**
     * 任务
     */
    void run();

    /**
     * 任务结果
     * 结果不是必须，如果需要可以在任务完成时写入共享变量中，然后在此接口去查询。
     * @return
     */
    default R getResult(){
        return null;
    }
}
