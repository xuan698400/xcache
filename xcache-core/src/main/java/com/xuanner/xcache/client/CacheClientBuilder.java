package com.xuanner.xcache.client;

import com.xuanner.xcache.client.config.ClientConfig;
import com.xuanner.xcache.client.config.SpyClientConfig;
import com.xuanner.xcache.client.config.XMemcachedClientConfig;
import com.xuanner.xcache.client.impl.SpyCacheClient;
import com.xuanner.xcache.client.impl.XmemCacheClient;
import com.xuanner.xcache.common.CacheException;

/**
 * Created by xuan on 2018/6/13.
 */
public class CacheClientBuilder {

    private ClientConfig config;

    public CacheClientBuilder config(ClientConfig config) {
        this.config = config;
        return this;
    }

    public CacheClient build() {
        if (null == config) {
            throw new CacheException("config must not be null.");
        }

        AbstractCacheClient client;

        if (config instanceof SpyClientConfig) {
            client = new SpyCacheClient();
        } else if (config instanceof XMemcachedClientConfig) {
            client = new XmemCacheClient();
        } else {
            throw new CacheException("clientConfig type invalid. must extands ClientConfig.");
        }

        client.init(config);
        return client;
    }
    
}
