package com.xuanner.xcache.client;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 缓存客户端接口
 * Created by xuan on 17/12/28.
 */
public interface CacheClient {

    /**
     * 单个获取缓存
     *
     * @param key 缓存key
     * @return 缓存值
     */
    Object get(String key);

    /**
     * 批量获取缓存
     *
     * @param keys 缓存key列表
     * @return 缓存值列表
     */
    Map<String, Object> getBulk(Collection<String> keys);

    /**
     * 设置缓存，可以自己指定时间单位
     *
     * @param key      缓存key
     * @param obj      缓存值
     * @param exp      超时时间，单位：由timeUnit决定
     * @param timeUnit 时间单位
     */
    void put(String key, Object obj, long exp, TimeUnit timeUnit);

    /**
     * 删除缓存
     *
     * @param key 缓存key
     */
    void delete(String key);
}
