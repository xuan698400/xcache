package com.xuanner.xcache.cache;

import com.xuanner.xcache.cache.config.GlobalCacheConfig;
import com.xuanner.xcache.cache.config.ModuleCacheConfig;
import com.xuanner.xcache.cache.impl.AsyncCache;
import com.xuanner.xcache.cache.impl.SimpleCache;
import com.xuanner.xcache.client.CacheClient;
import com.xuanner.xcache.common.CacheLoader;
import com.xuanner.xcache.common.ExecutorUtil;

import java.util.concurrent.Executor;

/**
 * 缓存构造器
 * Created by xuan on 17/12/28.
 */
public class CacheBuilder<R> {

    public static final String SIMPLE = "simple";
    public static final String ASYNC  = "async";

    /**
     * 全局配置
     */
    private GlobalCacheConfig globalCacheConfig = new GlobalCacheConfig();

    /**
     * 局部缓存配置
     */
    private ModuleCacheConfig moduleCacheConfig = new ModuleCacheConfig();

    /**
     * 数据加载器
     */
    private CacheLoader<R> cacheLoader;

    /**
     * 缓存客户端
     */
    private CacheClient cacheClient;

    /**
     * 使用异步缓存策略时才能使用
     */
    private Executor asyncStrategyExecutor;

    public CacheBuilder<R> setGlobalCacheConfig(GlobalCacheConfig globalCacheConfig) {
        this.globalCacheConfig = globalCacheConfig;
        return this;
    }

    public CacheBuilder<R> setModuleCacheConfig(ModuleCacheConfig moduleCacheConfig) {
        this.moduleCacheConfig = moduleCacheConfig;
        return this;
    }

    public CacheBuilder<R> setCacheLoader(CacheLoader<R> cacheLoader) {
        this.cacheLoader = cacheLoader;
        return this;
    }

    public CacheBuilder<R> setCacheClient(CacheClient cacheClient) {
        this.cacheClient = cacheClient;
        return this;
    }

    public CacheBuilder<R> setAsyncStrategyExecutor(Executor asyncStrategyExecutor) {
        this.asyncStrategyExecutor = asyncStrategyExecutor;
        return this;
    }

    public Cache<R> build(String strategy) {
        AbstractCache<R> abstractCache;
        if (SIMPLE.equals(strategy)) {
            abstractCache = new SimpleCache<>();
        } else if (ASYNC.equals(strategy)) {
            if (null == this.asyncStrategyExecutor) {
                //使用默认线程池
                this.asyncStrategyExecutor = ExecutorUtil.getDefaultExecutor();
            }
            abstractCache = new AsyncCache<>(this.asyncStrategyExecutor);
        } else {
            abstractCache = new SimpleCache<>();
        }

        //缓存客户端
        abstractCache.setGlobalCacheConfig(this.globalCacheConfig);
        abstractCache.setModuleCacheConfig(this.moduleCacheConfig);
        abstractCache.setCacheClient(this.cacheClient);
        abstractCache.setCacheLoader(this.cacheLoader);
        return abstractCache;
    }

}
