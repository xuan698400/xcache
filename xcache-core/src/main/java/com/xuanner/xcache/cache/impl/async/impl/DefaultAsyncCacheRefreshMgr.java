package com.xuanner.xcache.cache.impl.async.impl;

import com.xuanner.xcache.cache.Cache;
import com.xuanner.xcache.cache.impl.async.AsyncCacheRefreshMgr;
import com.xuanner.xcache.common.CacheException;
import com.xuanner.xcache.common.CacheLoader;
import com.xuanner.xcache.common.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * 异步设置缓存，主要解决如下问题
 * 缓存穿透问题：即查询一个不存在的数据，如果不做缓存处理，每次都会走DB。（异步设置解决了这个问题，如果加载不到数据，会设置一个null给NCacheObject,再设置到memcached）
 * 缓存击穿问题：即查询一个热点数据，当缓存失效时，如果流量过大，会重复查询DB，也会使DB被拖垮的风险（异步设置解决了这个问题，目前做到单台机器缓存key不重复设置缓存）
 * 缓存雪崩问题：即大量缓存数据设置了相同的缓存过期时间，当缓存同时失效时，也会引起DB被拖垮的风险。（目前异步设置，针对不同领域的缓存进行了差异化配置，目前应该可以杜绝这种风险）
 * Created by xuan on 2018/1/16.
 */
public class DefaultAsyncCacheRefreshMgr<R> implements AsyncCacheRefreshMgr<R> {

    private final Logger log = LoggerFactory.getLogger(DefaultAsyncCacheRefreshMgr.class);

    /**
     * 设置缓存线程池
     */
    private Executor executor;

    /**
     * 减少重复key提交Set
     */
    private Set<String> repeatCheckSet = new ConcurrentHashSet<>();

    public DefaultAsyncCacheRefreshMgr(Executor executor) {
        if (null == executor) {
            throw new CacheException("executor must not be null.");
        }
        this.executor = executor;
    }

    @Override
    public void refresh(Collection<String> keys, Cache<R> cache, CacheLoader<R> cacheLoader) {
        if (null == keys || keys.size() == 0 || null == cache || null == cacheLoader) {
            log.warn("[DefaultAsyncCacheRefreshMgr-refresh] param invalid. keys={}, cache={}, cacheLoader={}", keys,
                     cache, cacheLoader);
            return;
        }

        List<String> needRefreshKeyList = new ArrayList<>();
        for (String key : keys) {
            //在Set中的说明已经在设置缓存的路上了，不用处理了，只处理没有包含在Set中的缓存key
            if (!repeatCheckSet.contains(key)) {
                needRefreshKeyList.add(key);
                repeatCheckSet.add(key);
            }
        }

        //异步设置缓存
        if (needRefreshKeyList.size() > 0) {
            executor.execute(new DefaultAsyncCacheRefreshMgr.RefreshRunnable(needRefreshKeyList, cache, cacheLoader));
        }
    }

    /**
     * 加载数据，并设置缓存
     * Created by xuan on 17/11/29.
     */
    private class RefreshRunnable implements Runnable {

        private Cache<R> cache;

        private CacheLoader<R> cacheLoader;

        private List<String> needRefreshKeyList;

        private RefreshRunnable(List<String> needRefreshKeyList, Cache<R> cache, CacheLoader<R> cacheLoader) {
            this.needRefreshKeyList = needRefreshKeyList;
            this.cache = cache;
            this.cacheLoader = cacheLoader;
        }

        @Override
        public void run() {
            try {
                //单个优化
                if (needRefreshKeyList.size() == 1) {
                    String needRefreshKey = needRefreshKeyList.iterator().next();
                    cache.put(needRefreshKey, cacheLoader.loadData(needRefreshKey));
                } else {
                    //多个批量查找，循环设置
                    Map<String, R> key2DataMap = cacheLoader.loadData(needRefreshKeyList);
                    final Map<String, R> key2DataMapF = null == key2DataMap ? new HashMap<>() : key2DataMap;
                    needRefreshKeyList.forEach(validKey -> cache.put(validKey, key2DataMapF.get(validKey)));
                }
            } catch (Exception e) {
                log.error("[DefaultAsyncCacheRefreshMgr-WorkerRunnable] exception. e={}", e.getMessage());
            } finally {
                needRefreshKeyList.forEach(repeatCheckSet::remove);
            }
        }
    }

}
