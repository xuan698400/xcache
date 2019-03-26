package com.xuanner.xcache.cache.impl.async;

import com.xuanner.xcache.cache.Cache;
import com.xuanner.xcache.common.CacheLoader;

import java.util.Collection;

/**
 * 异步数据刷新
 * Created by xuan on 2018/1/19.
 */
public interface AsyncCacheRefreshMgr<R> {

    /**
     * 刷新缓存
     *
     * @param keys        需要刷新的缓存key
     * @param cache       缓存
     * @param cacheLoader 数据加载器
     */
    void refresh(Collection<String> keys, Cache<R> cache, CacheLoader<R> cacheLoader);
}
