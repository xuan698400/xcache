package com.xuanner.xcache;

import com.xuanner.xcache.client.CacheClient;
import com.xuanner.xcache.client.CacheClientBuilder;
import com.xuanner.xcache.client.config.SpyClientConfig;
import com.xuanner.xcache.client.config.XMemcachedClientConfig;

/**
 * Created by xuan on 2018/6/21.
 */
public abstract class ClientMarker {

    /**
     * 获取spy客户端
     *
     * @return
     */
    public static CacheClient getSpyClient() {
        SpyClientConfig config = new SpyClientConfig();
        config.setHost("m-bp15ca9b090e59a4.memcache.rds.aliyuncs.com");
        config.setPort(11211);
        config.setUsername("m-bp15ca9b090e59a4");
        config.setPassword("Yangtuo228");
        return new CacheClientBuilder().config(config).build();
    }

    /**
     * 获取xmemcached客户端
     *
     * @return
     */
    public static CacheClient getXmemcacheClient() {
        XMemcachedClientConfig config = new XMemcachedClientConfig();
        config.setHost("m-bp15ca9b090e59a4.memcache.rds.aliyuncs.com");
        config.setPort(11211);
        config.setUsername("m-bp15ca9b090e59a4");
        config.setPassword("Yangtuo228");
        return new CacheClientBuilder().config(config).build();
    }

}
