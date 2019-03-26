package com.xuanner.xcache.cache.config;

/**
 * 局部配置，一般一个模块的缓存一个实例
 *
 * Created by xuan on 2018/1/15.
 */
public class ModuleCacheConfig implements CacheConfig {

    private static final long DEFAULT_MEMCACHED_EXP_TIME = 2505600000L;//29天

    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * 缓存版本号
     */
    private int cacheVersion;

    /**
     * 缓存获取时间，单位：毫秒
     */
    private long expTime;

    /**
     * 真实memcached缓存过期时间，单位：毫秒
     */
    private long memcachedExpTime = UNSET_INT;

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public int getCacheVersion() {
        return cacheVersion;
    }

    public void setCacheVersion(int cacheVersion) {
        this.cacheVersion = cacheVersion;
    }

    public long getExpTime() {
        return expTime;
    }

    public void setExpTime(long expTime) {
        this.expTime = expTime;
    }

    public long getMemcachedExpTime() {
        return UNSET_INT == this.memcachedExpTime ? DEFAULT_MEMCACHED_EXP_TIME : this.memcachedExpTime;
    }

    public void setMemcachedExpTime(long memcachedExpTime) {
        this.memcachedExpTime = memcachedExpTime;
    }

}
