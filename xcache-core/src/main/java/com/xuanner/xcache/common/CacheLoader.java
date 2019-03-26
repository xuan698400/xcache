package com.xuanner.xcache.common;

import java.util.Collection;
import java.util.Map;

/**
 * 设置缓存时，使用该接口获取数据
 * <p>
 * Created by xuan on 17/11/29.
 */
public interface CacheLoader<R> {

    /**
     * 批量缓存加载
     *
     * @param keys 缓存key,使用集合传入
     * @return 缓存对象, key=缓存key;value=缓存对象
     */
    Map<String, R> loadData(Collection<String> keys);

    /**
     * 单个加载缓存
     *
     * @param key 缓存key
     * @return 缓存对象
     */
    R loadData(String key);

}
