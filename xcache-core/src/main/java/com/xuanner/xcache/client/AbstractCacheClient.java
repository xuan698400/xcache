package com.xuanner.xcache.client;

import com.xuanner.xcache.client.config.ClientConfig;
import com.xuanner.xcache.common.CacheException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 缓存客户端抽象基类
 * Created by xuan on 17/12/28.
 */
public abstract class AbstractCacheClient implements CacheClient {

    /**
     * 客户端初始化
     *
     * @param clientConfig 配置参数
     */
    void init(ClientConfig clientConfig) {
        onInit(clientConfig);
    }

    @Override
    public Object get(String key) {
        if (null == key || key.trim().length() == 0) {
            throw new CacheException("[AbstractCacheClient-get]key must not empty.");
        }
        return onGet(key);
    }

    @Override
    public Map<String, Object> getBulk(Collection<String> keys) {
        if (null == keys || keys.isEmpty()) {
            return new HashMap<>();
        }

        keys.forEach(key -> {
            if (null == key || key.trim().length() == 0) {
                throw new CacheException("[AbstractCacheClient-getBulk]key must not empty.");
            }
        });

        return onGetBulk(keys);
    }

    @Override
    public void put(String key, Object obj, long exp, TimeUnit timeUnit) {
        if (null == key || key.trim().length() == 0) {
            throw new CacheException("[AbstractCacheClient-put]key must not empty.");
        }
        onPut(key, obj, exp, timeUnit);
    }

    @Override
    public void delete(String key) {
        if (null == key || key.trim().length() == 0) {
            throw new CacheException("[AbstractCacheClient-delete]key must not empty.");
        }
        onDelete(key);
    }

    protected abstract void onInit(ClientConfig clientConfig);

    protected abstract Object onGet(String key);

    protected abstract Map<String, Object> onGetBulk(Collection<String> keys);

    protected abstract void onPut(String key, Object obj, long expTime, TimeUnit timeUnit);

    protected abstract void onDelete(String key);

}
