package com.xuanner.xcache.client.impl;

import com.xuanner.xcache.client.AbstractCacheClient;
import com.xuanner.xcache.client.config.ClientConfig;
import com.xuanner.xcache.client.config.XMemcachedClientConfig;
import com.xuanner.xcache.common.CacheException;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.utils.AddrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * xmemcached客户端实现
 * Created by xuan on 18/1/8.
 */
public class XmemCacheClient extends AbstractCacheClient {

    private final static Logger log = LoggerFactory.getLogger(XmemCacheClient.class);

    private MemcachedClient client;

    @Override
    protected void onInit(ClientConfig clientConfig) {
        XMemcachedClientConfig xMemcachedClientConfig = (XMemcachedClientConfig) clientConfig;

        MemcachedClientBuilder builder = new XMemcachedClientBuilder(
                AddrUtil.getAddresses(xMemcachedClientConfig.getHost() + ":" + xMemcachedClientConfig.getPort()));
        builder.addAuthInfo(
                AddrUtil.getOneAddress(xMemcachedClientConfig.getHost() + ":" + xMemcachedClientConfig.getPort()),
                AuthInfo.plain(xMemcachedClientConfig.getUsername(), xMemcachedClientConfig.getPassword()));
        builder.setCommandFactory(new BinaryCommandFactory());
        builder.setOpTimeout(xMemcachedClientConfig.getOpTimeout());

        try {
            client = builder.build();
        } catch (IOException e) {
            throw new CacheException("XmemCacheClient init fail.", e);
        }
    }

    @Override
    public Object onGet(String key) {
        try {
            return client.get(key);
        } catch (Exception e) {
            log.error("[XmemCacheClient-get]exception. e={}", e);
            return null;
        }
    }

    @Override
    public Map<String, Object> onGetBulk(Collection<String> keys) {
        try {
            return client.get(keys);
        } catch (Exception e) {
            log.error("[XmemCacheClient-getBulk]exception. e={}", e);
            return new HashMap<>();
        }
    }

    @Override
    public void onPut(String key, Object obj, long expTime, TimeUnit timeUnit) {
        try {
            client.set(key, (int) timeUnit.toSeconds(expTime), obj);
        } catch (Exception e) {
            log.error("[XmemCacheClient-put]exception. e={}", e);
        }
    }

    @Override
    public void onDelete(String key) {
        try {
            client.delete(key);
        } catch (Exception e) {
            log.error("[XmemCacheClient-delete]exception. e={}", e);
        }
    }

}
