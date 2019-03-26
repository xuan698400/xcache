package com.xuanner.xcache.cache.config;

/**
 * 全局配置，一般一个应用使用一个实例
 * <p>
 * Created by xuan on 2018/1/15.
 */
public class GlobalCacheConfig implements CacheConfig {

    /**
     * app应用标识
     */
    private String appName;

    /**
     * app版本号
     */
    private int appCacheVersion;

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getAppCacheVersion() {
        return this.appCacheVersion;
    }

    public void setAppCacheVersion(int appCacheVersion) {
        this.appCacheVersion = appCacheVersion;
    }

}
