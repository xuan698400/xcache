package com.xuanner.xcache.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 缓存客户端
 * Created by xuan on 17/11/29.
 */
public interface Cache<R> {

    /**
     * 单个获取缓存
     *
     * @param key 缓存key
     * @return 缓存值
     */
    R get(String key);

    /**
     * 批量获取缓存
     *
     * @param keys 缓存key列表
     * @return 缓存值列表
     */
    Map<String, R> getBulk(Collection<String> keys);

    /**
     * 设置缓存
     *
     * @param key 缓存key
     */
    void put(String key, R r);

    /**
     * 设置缓存，会根据cacheloader加载数据
     *
     * @param key 缓存key
     */
    void set(String key);

    /**
     * 批量设置缓存
     *
     * @param keyList keyList
     */
    void setBulk(List<String> keyList);

    /**
     * 删除缓存
     *
     * @param key 缓存key
     */
    void delete(String key);

}
