package com.xuanner.xcache.common;

import java.io.Serializable;

/**
 * 缓存包装对象
 * <p>
 * Created by xuan on 17/11/29.
 */
public class CacheObject implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 对象值
     */
    private Object value;

    /**
     * 缓存创建时间戳，单位：毫秒
     */
    private long creationTime;

    /**
     * 缓存过期时间，单位：毫秒
     */
    private long expiryTime;

    /**
     * 判断缓存是否过期
     *
     * @return 返回缓存是否过期
     */
    public boolean isExpired() {
        long time = creationTime + expiryTime;
        long now = System.currentTimeMillis();
        return time > 0 && time <= now;
    }

    /**
     * 构建缓存对象
     *
     * @param value      缓存数据
     * @param expiryTime 过期时间
     * @return 缓存对象
     */
    public static CacheObject of(Object value, long expiryTime) {
        CacheObject cacheObject = new CacheObject();
        cacheObject.setValue(value);
        cacheObject.setExpiryTime(expiryTime);
        cacheObject.setCreationTime(System.currentTimeMillis());
        return cacheObject;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }

}
