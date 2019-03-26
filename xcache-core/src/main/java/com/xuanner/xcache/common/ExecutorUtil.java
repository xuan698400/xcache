package com.xuanner.xcache.common;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池工具类
 * Created by xuan on 2018/6/13.
 */
public abstract class ExecutorUtil {
    
    public static Executor getDefaultExecutor() {
        int CORE_POOL_SIZE = 2;//核心线程数，这个是肯定会分配的线程数
        int MAX_POLL_SIZE = 4;//最大线程数，实际上能否达到还取决于队列数queueCapacity是否满了
        int KEEPALIVE_SECONDS = 60;//空闲线程的存活时间，为0表示一直可以存活
        int QUEUE_CAPACITY = 1024;//阻塞队列长度
        RejectedExecutionHandler ABORT_POLICY = new ThreadPoolExecutor.AbortPolicy();//默认的拒绝策略

        //主要用来重命名线程的名字
        ThreadFactory threadFactory = new ThreadFactory() {

            final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "XCACHE-THREAD-" + threadNumber.getAndIncrement());
                t.setDaemon(true);
                return t;
            }
        };

        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POLL_SIZE, KEEPALIVE_SECONDS, TimeUnit.SECONDS,
                                      new LinkedBlockingQueue<>(QUEUE_CAPACITY), threadFactory, ABORT_POLICY);
    }

}
