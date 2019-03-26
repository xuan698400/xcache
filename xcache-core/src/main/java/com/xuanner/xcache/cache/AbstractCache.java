package com.xuanner.xcache.cache;

import com.xuanner.xcache.cache.config.GlobalCacheConfig;
import com.xuanner.xcache.cache.config.ModuleCacheConfig;
import com.xuanner.xcache.client.CacheClient;
import com.xuanner.xcache.common.CacheKeyHelper;
import com.xuanner.xcache.common.CacheLoader;

/**
 * 缓存抽象基类
 * Created by xuan on 17/12/27.
 */
public abstract class AbstractCache<R> implements Cache<R> {

    /**
     * 缓存的全局配置，一般一个app一个全局配置对象即可以
     */
    private GlobalCacheConfig globalCacheConfig;

    /**
     * 缓存的局部配置，一般一个cache一个配置对象
     */
    private ModuleCacheConfig moduleCacheConfig;

    /**
     * 缓存客户端
     */
    private CacheClient cacheClient;

    /**
     * 数据加载器
     */
    private CacheLoader<R> cacheLoader;

    public GlobalCacheConfig getGlobalCacheConfig() {
        return globalCacheConfig;
    }

    public void setGlobalCacheConfig(GlobalCacheConfig globalCacheConfig) {
        this.globalCacheConfig = globalCacheConfig;
    }

    public ModuleCacheConfig getModuleCacheConfig() {
        return moduleCacheConfig;
    }

    public void setModuleCacheConfig(ModuleCacheConfig moduleCacheConfig) {
        this.moduleCacheConfig = moduleCacheConfig;
    }

    public CacheClient getCacheClient() {
        return cacheClient;
    }

    public void setCacheClient(CacheClient cacheClient) {
        this.cacheClient = cacheClient;
    }

    public CacheLoader<R> getCacheLoader() {
        return cacheLoader;
    }

    public void setCacheLoader(CacheLoader<R> cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    /**
     * 生成缓存key
     *
     * @param key
     * @return
     */
    protected String getCacheKey(String key) {
        CacheKeyHelper.KeyObj keyObj = new CacheKeyHelper.KeyObj();
        keyObj.setAppName(getGlobalCacheConfig().getAppName());
        keyObj.setAppCacheVersion(getGlobalCacheConfig().getAppCacheVersion());
        keyObj.setCacheName(getModuleCacheConfig().getCacheName());
        keyObj.setCacheVersion(getModuleCacheConfig().getCacheVersion());
        keyObj.setKey(key);
        return CacheKeyHelper.toCacheKey(keyObj);
    }

}
