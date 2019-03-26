package com.xuanner.xcache.cache.impl;

import com.xuanner.xcache.cache.AbstractCache;
import com.xuanner.xcache.common.CacheObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 默认实现模版
 * 1、所有缓存会进行CacheObject包装
 * 2、使用CacheObject.isExpired进行过期判断
 * Created by xuan on 2018/1/16.
 */
public abstract class DefaultCache<R> extends AbstractCache<R> {

    @Override
    public R get(String key) {
        return doGet(key);
    }

    @Override
    public Map<String, R> getBulk(Collection<String> keys) {

        Map<String, R> resultMap = new HashMap<>();
        if (null == keys || keys.size() == 0) {
            return resultMap;
        }

        //去null去重处理
        List<String> keyList = keys.stream().filter(key -> null != key && key.length() > 0).distinct().collect(
                Collectors.toList());
        if (null == keyList || keyList.size() == 0) {
            return resultMap;
        }

        //判定单个的话，进行单个获取优化处理
        if (keyList.size() == 1) {
            R singleCache = this.get(keyList.get(0));
            if (singleCache != null) {
                resultMap.put(keyList.get(0), singleCache);
                return resultMap;
            } else {
                return resultMap;
            }
        }

        return doGetBulk(keys);
    }

    @Override
    public void put(String key, R r) {
        getCacheClient().put(getCacheKey(key), CacheObject.of(r, getModuleCacheConfig().getExpTime()),
                             getModuleCacheConfig().getMemcachedExpTime(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void set(String key) {
        R r = getCacheLoader().loadData(key);
        put(key, r);
    }

    @Override
    public void setBulk(List<String> keyList) {
        if (null == keyList || keyList.size() == 0) {
            return;
        }
        Map<String, R> key2DataMap = getCacheLoader().loadData(keyList);
        if (null == key2DataMap || key2DataMap.size() == 0) {
            return;
        }
        key2DataMap.forEach((key, data) -> put(key, data));
    }

    @Override
    public void delete(String key) {
        getCacheClient().delete(getCacheKey(key));
    }

    /**
     * 从缓存对象中获取缓存数据
     *
     * @param cacheObj 缓存对象
     * @return 缓存数据
     */
    @SuppressWarnings("unchecked")
    R getValue(Object cacheObj) {
        return null != cacheObj ? (R) ((CacheObject) cacheObj).getValue() : null;
    }

    /**
     * 判断缓存对象是否过期。
     * 注意：空和过期都算过期
     *
     * @param cacheObj 缓存对象
     * @return ture / false
     */
    boolean isExpTime(Object cacheObj) {
        return null == cacheObj || ((CacheObject) cacheObj).isExpired();
    }

    protected abstract R doGet(String key);

    protected abstract Map<String, R> doGetBulk(Collection<String> keys);

}
