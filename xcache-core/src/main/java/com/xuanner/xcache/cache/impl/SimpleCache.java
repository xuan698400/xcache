package com.xuanner.xcache.cache.impl;

import com.xuanner.xcache.common.CacheLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 简单同步缓存实现
 * Created by xuan on 2018/1/16.
 */
public class SimpleCache<R> extends DefaultCache<R> {

    @Override
    protected R doGet(String key) {
        Object cacheObj = getCacheClient().get(getCacheKey(key));
        if (isExpTime(cacheObj)) {
            CacheLoader<R> cacheLoader = getCacheLoader();
            if (null == cacheLoader) {
                return null;
            } else {
                R r = getCacheLoader().loadData(key);
                put(key, r);
                return r;
            }
        } else {
            return getValue(cacheObj);
        }
    }

    @Override
    public Map<String, R> doGetBulk(Collection<String> keys) {
        Map<String, R> resultMap = new HashMap<>();

        //获取缓存，分拣出缓存过期（包括不存在的）、有效缓存
        List<String> cacheKeyList = keys.stream().map(this::getCacheKey).collect(Collectors.toList());
        Map<String, Object> cacheMap = getCacheClient().getBulk(cacheKeyList);
        List<String> noCacheKeyList = new ArrayList<>();//没有缓存的key

        if (null == cacheMap || cacheMap.size() == 0) {
            noCacheKeyList.addAll(keys);
        } else {
            for (String key : keys) {
                Object value = cacheMap.get(getCacheKey(key));
                if (isExpTime(value)) {
                    //缓存过期或者不存在
                    noCacheKeyList.add(key);
                } else {
                    R r = getValue(value);
                    if (null != r) {
                        resultMap.put(key, r);
                    }
                }
            }
        }

        //没有缓存的需要同步加载
        fillAndPutNoCacheKeyList(noCacheKeyList, resultMap);
        return resultMap;
    }

    /**
     * 同步加载缓存
     *
     * @param noCacheKeyList 没有命中的缓存key
     * @param resultMap      返回结果集
     */
    private void fillAndPutNoCacheKeyList(List<String> noCacheKeyList, Map<String, R> resultMap) {
        if (null == noCacheKeyList || noCacheKeyList.size() == 0) {
            return;
        }

        CacheLoader<R> cacheLoader = getCacheLoader();
        if (null == cacheLoader) {
            return;
        }

        Map<String, R> dataMap = getCacheLoader().loadData(noCacheKeyList);
        if (null == dataMap) {
            dataMap = new HashMap<>();
        }

        for (String key : noCacheKeyList) {
            R r = dataMap.get(key);
            put(key, r);
            if (null != r) {
                resultMap.put(key, r);
            }
        }
    }

}
