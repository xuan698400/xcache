package com.xuanner.xcache.common;

/**
 * 缓存key拼接工具类
 * Created by xuan on 17/11/29.
 */
public abstract class CacheKeyHelper {

    /**
     * 缓存key的分割符
     */
    private static final String SEPARATOR = ":";

    /**
     * 生成cacheKey
     *
     * @param keyObj 缓存key对象
     * @return 缓存key
     */
    public static String toCacheKey(KeyObj keyObj) {
        return keyObj.getAppName() + SEPARATOR + String.valueOf(keyObj.getAppCacheVersion()) + SEPARATOR
               + keyObj.getCacheName() + SEPARATOR + String.valueOf(keyObj.getCacheVersion()) + SEPARATOR
               + keyObj.getKey();
    }

    /**
     * 解析key对象
     *
     * @param cacheKey 缓存key
     * @return 缓存key对象
     */
    public static KeyObj parseCacheKey(String cacheKey) {
        String[] keys = CacheKeyHelper.checkCacheKey(cacheKey);
        if (keys == null || keys.length < 5) {
            return null;
        }
        KeyObj keyObj = new KeyObj();
        keyObj.setAppName(keys[0]);
        keyObj.setAppCacheVersion(Integer.valueOf(keys[1]));
        keyObj.setCacheName(keys[2]);
        keyObj.setCacheVersion(Integer.valueOf(keys[3]));
        keyObj.setKey(keys[4]);
        return keyObj;
    }

    /**
     * 缓存key的格式校验
     * 格式必须：appName_appVersion_cacheName_cacheVersion_key
     *
     * @param cacheKey 缓存
     * @return 如果合法返回key的组成数据
     */
    public static String[] checkCacheKey(String cacheKey) {
        if (null == cacheKey || cacheKey.trim().length() == 0) {
            throw new CacheException("cacheKey must not empty.");
        }

        String[] keys = cacheKey.split(SEPARATOR);

        if (keys.length != 5) {
            //throw new CacheException("cacheKey is invalid. cacheKey=" + cacheKey);
        }
        return keys;
    }

    public static class KeyObj {

        private String appName;

        private int appCacheVersion;

        private String cacheName;

        private int cacheVersion;

        private String key;

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public int getAppCacheVersion() {
            return appCacheVersion;
        }

        public void setAppCacheVersion(int appCacheVersion) {
            this.appCacheVersion = appCacheVersion;
        }

        public String getCacheName() {
            return cacheName;
        }

        public void setCacheName(String cacheName) {
            this.cacheName = cacheName;
        }

        public int getCacheVersion() {
            return cacheVersion;
        }

        public void setCacheVersion(int cacheVersion) {
            this.cacheVersion = cacheVersion;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

}
