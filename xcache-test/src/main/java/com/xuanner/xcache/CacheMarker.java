package com.xuanner.xcache;

import com.xuanner.xcache.cache.Cache;
import com.xuanner.xcache.cache.CacheBuilder;
import com.xuanner.xcache.cache.config.GlobalCacheConfig;
import com.xuanner.xcache.cache.config.ModuleCacheConfig;
import com.xuanner.xcache.common.CacheLoader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuan on 2018/6/21.
 */
public abstract class CacheMarker {

    /**
     * 缓存的全局配置，一般一个应用一个配置，可以用全局版本号控制全缓存失效
     *
     * @return
     */
    public static GlobalCacheConfig getGlobalCacheConfig() {
        GlobalCacheConfig globalCacheConfig = new GlobalCacheConfig();
        globalCacheConfig.setAppName("xuannerApp");
        globalCacheConfig.setAppCacheVersion(1);
        return globalCacheConfig;
    }

    /**
     * 缓存的模块配置，表示需要缓存的一类数据，例如一个电商有商品数据，类目数据等,可以用模块的版本控制本模块的缓存失效
     *
     * @return
     */
    public static ModuleCacheConfig getModuleCacheConfig() {
        ModuleCacheConfig moduleCacheConfig = new ModuleCacheConfig();
        moduleCacheConfig.setCacheName("itemData");
        moduleCacheConfig.setCacheVersion(1);
        moduleCacheConfig.setExpTime(2000);//超时时间
        return moduleCacheConfig;
    }

    /**
     * 当缓存不存在或者过期时，需要回源到DB查询时的查询接口
     *
     * @return
     */
    public static CacheLoader<String> getCacheLoader() {
        return new CacheLoader<String>() {

            @Override
            public Map<String, String> loadData(Collection<String> keys) {
                System.out.println("多个loader...");
                Map<String, String> key2DataMap = new HashMap<>();
                keys.forEach(key -> key2DataMap.put(key, key + "_data"));
                return null;
            }

            @Override
            public String loadData(String key) {
                System.out.println("单个loader...");
                return key + "_data";
            }
        };
    }

    /**
     * 构建一个缓存
     *
     * @param strategy
     * @return
     */
    public static Cache<String> getCache(String strategy) {
        return new CacheBuilder<String>().setCacheClient(ClientMarker.getSpyClient()).setCacheLoader(
                getCacheLoader()).setGlobalCacheConfig(getGlobalCacheConfig()).setModuleCacheConfig(
                getModuleCacheConfig()).build(strategy);
    }

}
