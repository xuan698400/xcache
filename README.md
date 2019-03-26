# xcache

#### 项目介绍
微服务时代，我们一般会用分布式缓存在提高系统的并发能力。
例如使用memcached、redis等比较知名的。但是很多时候，
作为普通业务程序员，他们可能在写业务的时候，并不想关心底层到底用的是memcached还是redis。
而且常用的缓存策略最好也是能够封装在缓存组件里面，供业务程序员使用。
这就是我封装这个组件的初衷。后面会慢慢支持各种实现，供使用者实现。

#### 软件架构
组件分两个模块：

1. client模块，对底层使用哪种缓存框架进行屏蔽封装。例如，目前支持memcached的spy客户端和xmemcached客户端。
2. cache模块，在client的基础上加了一些缓存策略，例如：同步设置缓存策略、异步设置缓存策略（防缓存击穿和缩短RT时间）等

其中特别说明cache模块的两种策略：同步设置策略、异步设置策略<br/>
（1）同步设置策略<br/>
比较常见的做法，当get缓存时，如果缓存不存在或者缓存过期，那么就回源到DB获取数据，并
设置缓存，最后返回数据，这些步骤都在主线程中操作完成，简单明了，但有个缺点，存在缓存
击穿风险，当缓存过期时，由于要同时查询DB并设置缓存，RT会较长。<br/>
（2）异步设置策略<br/>
当get缓存时，如果缓存不存在，那按同步策略走，当缓存存在但是过期时，主线程先返回过期缓存
数据，再另起线程，查询DB，更新缓存，这中间会判断是否有同key的缓存正在设置，保证同一时间
不会出现多请求同key重复设置。这种方案，单机避免了缓存击穿风险，当缓存过期，RT也短，缺点
就是会有额外的线程池耗资源，还有就是缓存过期时，那一瞬间返回的时过期的数据。

#### 版本说明

1. V1.0支持memcached客户端的实现（spy、xmemcached都支持）

#### Maven支持
<pre><code>
&lt;dependency>
    &lt;groupId>com.xuanner&lt;/groupId>
    &lt;artifactId>xcache-core&lt;/artifactId>
    &lt;version>1.0&lt;/version>
&lt;/dependency>
</pre></code>

#### 使用说明

1. client模块的API使用：
<pre><code>
/**
 * 获取spy客户端
 *
 * @return
 */
public static CacheClient getSpyClient() {
    SpyClientConfig config = new SpyClientConfig();
    config.setHost("xxx");
    config.setPort(11211);
    config.setUsername("xxx");
    config.setPassword("xxx");
    return new CacheClientBuilder().config(config).build();
}

/**
 * 获取xmemcached客户端
 *
 * @return
 */
public static CacheClient getXmemcacheClient() {
    XMemcachedClientConfig config = new XMemcachedClientConfig();
    config.setHost("xxx");
    config.setPort(11211);
    config.setUsername("xxx");
    config.setPassword("xxx");
    return new CacheClientBuilder().config(config).build();
}

public class ClientTest {

    private CacheClient cacheClient;

    @Before
    public void setup() {
        cacheClient = getSpyClient();
    }

    @Test
    public void test() {
        //删除
        cacheClient.delete("testKey");
        //设置
        cacheClient.put("testKey", "testValue", 2000, TimeUnit.MILLISECONDS);
        //获取
        String testValue = (String) cacheClient.get("testKey");
    }

}
</pre></code>

2. cache模块的API使用：
<pre><code>
/**
 * 缓存的全局配置，一般一个应用一个配置，可以用全局版本号控制全缓存失效
 *
 * @return
 */
public static GlobalCacheConfig getGlobalCacheConfig() {
    GlobalCacheConfig globalCacheConfig = new GlobalCacheConfig();
    globalCacheConfig.setAppName("xuannerApp");
    globalCacheConfig.setAppCacheVersion(1);
    return globalCacheConfig;
}

/**
 * 缓存的模块配置，表示需要缓存的一类数据，例如一个电商有商品数据，类目数据等,可以用模块的版本控制本模块的缓存失效
 *
 * @return
 */
public static ModuleCacheConfig getModuleCacheConfig() {
    ModuleCacheConfig moduleCacheConfig = new ModuleCacheConfig();
    moduleCacheConfig.setCacheName("itemData");
    moduleCacheConfig.setCacheVersion(1);
    moduleCacheConfig.setExpTime(2000);//超时时间
    return moduleCacheConfig;
}

/**
 * 当缓存不存在或者过期时，需要回源到DB查询时的查询接口
 *
 * @return
 */
public static CacheLoader<String> getCacheLoader() {
    return new CacheLoader<String>() {

        @Override
        public Map<String, String> loadData(Collection<String> keys) {
            System.out.println("多个loader...");
            Map<String, String> key2DataMap = new HashMap<>();
            keys.forEach(key -> key2DataMap.put(key, key + "_data"));
            return null;
        }

        @Override
        public String loadData(String key) {
            System.out.println("单个loader...");
            return key + "_data";
        }
    };
}

/**
 * 构建一个缓存
 *
 * @param strategy
 * @return
 */
public static Cache<String> getCache(String strategy) {
    return new CacheBuilder<String>().setCacheClient(getSpyClient()).setCacheLoader(
            getCacheLoader()).setGlobalCacheConfig(getGlobalCacheConfig()).setModuleCacheConfig(
            getModuleCacheConfig()).build(strategy);
}

public class CacehTest {

    private Cache<String> cache;

    @Before
    public void setup() {
        //ASYNC表示异步设置缓存策略，SIMPLE表示同步设置缓存策略
        cache = getCache(CacheBuilder.ASYNC);
    }

    @Test
    public void test() {
        //删除缓存
        cache.delete("testKey");
        
        //获取缓存，当缓存不存在，会触发CacheLoader接口获取数据，并自动设置缓存，返回结果
        cache.get("testKey");
        
        //设置缓存，会触发CacheLoader接口获取数据，并自动设置缓存
        cache.set("testKey");
    }
}
</pre></code>

####文档
访问地址：https://apidoc.gitee.com/xuan698400/xcache

#### 联系方式
1. 姓名：徐安
2. 邮箱:javaandswing@163.com
3. QQ：349309307
4. 个人博客：xuanner.com