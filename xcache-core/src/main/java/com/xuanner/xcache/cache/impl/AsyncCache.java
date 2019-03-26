package com.xuanner.xcache.cache.impl;

import com.xuanner.xcache.cache.impl.async.AsyncCacheRefreshMgr;
import com.xuanner.xcache.cache.impl.async.AsyncCacheRefreshMgrFactory;
import com.xuanner.xcache.common.CacheLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 异步刷新缓存实现
 * Created by xuan on 2018/1/16.
 */
public class AsyncCache<R> extends DefaultCache<R> {

    /**
     * 异步刷新器
     */
    private AsyncCacheRefreshMgr<R> asyncCacheRefreshMgr;

    public AsyncCache(Executor executor) {
        asyncCacheRefreshMgr = AsyncCacheRefreshMgrFactory.buildDefault(executor);
    }

    @Override
    protected R doGet(String key) {
        Object cacheObj = getCacheClient().get(getCacheKey(key));
        boolean needRefresh;
        R resultObj;

        if (null == cacheObj) {
            //没有缓存直接加载返回
            needRefresh = true;
            CacheLoader<R> cacheLoader = getCacheLoader();
            if (null == cacheLoader) {
                return null;
            } else {
                resultObj = getCacheLoader().loadData(key);
            }
        } else {
            //取到缓存，还需要判断是否过期
            needRefresh = isExpTime(cacheObj);
            resultObj = getValue(cacheObj);
        }

        if (needRefresh && null != this.getCacheLoader()) {
            //异步刷新

            List<String> keyList = new ArrayList<>();
            keyList.add(key);
            asyncCacheRefreshMgr.refresh(keyList, this, this.getCacheLoader());
        }

        return resultObj;
    }

    @Override
    protected Map<String, R> doGetBulk(Collection<String> keys) {

        Map<String, R> resultMap = new HashMap<>();

        //获取缓存，分拣出无缓存、缓存过期、有效缓存
        List<String> cacheKeyList = keys.stream().map(this::getCacheKey).collect(Collectors.toList());
        Map<String, Object> cacheMap = getCacheClient().getBulk(cacheKeyList);
        List<String> noCacheKeyList = new ArrayList<>();//没有缓存的key
        List<String> needRefreshCacheKeyList = new ArrayList<>();//需要刷新缓存的key，其中包括：没有缓存的key + 缓存过期的key

        if (null == cacheMap || cacheMap.size() == 0) {
            noCacheKeyList.addAll(keys);
            needRefreshCacheKeyList.addAll(keys);
        } else {
            for (String key : keys) {
                Object value = cacheMap.get(getCacheKey(key));
                if (null == value) {
                    //没有缓存
                    noCacheKeyList.add(key);
                    needRefreshCacheKeyList.add(key);
                } else {
                    if (isExpTime(value)) {
                        needRefreshCacheKeyList.add(key);
                    }
                    R r = getValue(value);
                    if (null != r) {
                        resultMap.put(key, r);
                    }
                }
            }
        }

        //没有缓存的需要同步加载
        fillNoCacheKeyList(noCacheKeyList, resultMap);

        //缓存过期的异步加载
        if (needRefreshCacheKeyList.size() > 0 && null != this.getCacheLoader()) {
            //异步刷新
            asyncCacheRefreshMgr.refresh(needRefreshCacheKeyList, this, this.getCacheLoader());
        }

        return resultMap;
    }

    /**
     * 同步加载缓存
     *
     * @param noCacheKeyList 没有命中的缓存key
     * @param resultMap      返回结果集
     */
    private void fillNoCacheKeyList(List<String> noCacheKeyList, Map<String, R> resultMap) {
        if (null == noCacheKeyList || noCacheKeyList.size() == 0) {
            return;
        }

        CacheLoader<R> cacheLoader = getCacheLoader();
        if (null == cacheLoader) {
            return;
        }

        //没有缓存的同步加载
        Map<String, R> dataMap = getCacheLoader().loadData(noCacheKeyList);
        if (null == dataMap || dataMap.size() == 0) {
            return;
        }

        dataMap.forEach((k, v) -> {
            if (null != v) {
                resultMap.put(k, v);
            }
        });
    }

}
