package com.xuanner.xcache.common;

/**
 * 表示操作缓存发生异常的异常类
 * <p>
 * Created by xuan on 17/11/29.
 */
public class CacheException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        //复写这个方法可以防止打出堆栈信息
        return this;
    }

}
