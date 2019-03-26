package com.xuanner.xcache.client.impl;

import com.xuanner.xcache.client.AbstractCacheClient;
import com.xuanner.xcache.client.config.ClientConfig;
import com.xuanner.xcache.client.config.SpyClientConfig;
import com.xuanner.xcache.common.CacheException;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
import net.spy.memcached.internal.OperationFuture;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * spy客户端实现
 * Created by xuan on 18/1/8.
 */
public class SpyCacheClient extends AbstractCacheClient {

    private static final long INIT_SLEEP = 300;

    private MemcachedClient client;

    @Override
    protected void onInit(ClientConfig clientConfig) {
        SpyClientConfig spyClientConfig = (SpyClientConfig) clientConfig;

        ConnectionFactoryBuilder builder = new ConnectionFactoryBuilder();
        builder.setOpTimeout(spyClientConfig.getOpTimeout());
        builder.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY);
        builder.setAuthDescriptor(new AuthDescriptor(new String[] { "PLAIN" },
                                                     new PlainCallbackHandler(spyClientConfig.getUsername(),
                                                                              spyClientConfig.getPassword())));
        try {
            client = new MemcachedClient(builder.build(), AddrUtil.getAddresses(
                    spyClientConfig.getHost() + ":" + spyClientConfig.getPort()));
            Thread.sleep(INIT_SLEEP);
        } catch (Exception e) {
            throw new CacheException("SpyCacheClient init fail.", e);
        }
    }

    @Override
    public Object onGet(String key) {
        return client.get(key);
    }

    @Override
    public Map<String, Object> onGetBulk(Collection<String> keys) {
        return client.getBulk(keys);
    }

    @Override
    public void onPut(String key, Object obj, long expTime, TimeUnit timeUnit) {
        client.set(key, (int) timeUnit.toSeconds(expTime), obj);
    }

    @Override
    public void onDelete(String key) {
        client.delete(key);
    }

}
