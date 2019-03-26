package com.xuanner.xcache.cache.impl.async;

import com.xuanner.xcache.cache.impl.async.impl.DefaultAsyncCacheRefreshMgr;

import java.util.concurrent.Executor;

/**
 * 异步刷新器工厂
 * <p>
 * Created by xuan on 2018/1/19.
 */
public abstract class AsyncCacheRefreshMgrFactory {

    /**
     * 构建一个默认的异步刷新器
     *
     * @param executor 线程池
     * @return 刷新器
     */
    public static <R> AsyncCacheRefreshMgr buildDefault(Executor executor) {
        return new DefaultAsyncCacheRefreshMgr<R>(executor);
    }

}
