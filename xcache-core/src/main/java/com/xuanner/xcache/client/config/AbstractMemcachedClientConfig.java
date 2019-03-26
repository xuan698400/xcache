package com.xuanner.xcache.client.config;

/**
 * memcached客户端的抽象基类
 * Created by xuan on 2018/1/15.
 */
public abstract class AbstractMemcachedClientConfig extends ClientConfig {

    //默认值设置
    public static final long DEFAULT_OPTIMEOUT = 1000;

    /**
     * 缓存域名地址
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 操作超时，单位：毫秒，默认：1000
     */
    private long opTimeout = UNSET_INT;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getOpTimeout() {
        return UNSET_INT == this.opTimeout ? DEFAULT_OPTIMEOUT : opTimeout;
    }

    public void setOpTimeout(long opTimeout) {
        this.opTimeout = opTimeout;
    }

}
