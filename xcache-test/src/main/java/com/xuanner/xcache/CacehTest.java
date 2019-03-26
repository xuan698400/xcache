package com.xuanner.xcache;

import com.xuanner.xcache.cache.Cache;
import com.xuanner.xcache.cache.CacheBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xuan on 2018/6/13.
 */
public class CacehTest {

    private Cache<String> cache;

    @Before
    public void setup() {
        cache = CacheMarker.getCache(CacheBuilder.ASYNC);
    }

    @Test
    public void test() {
        cache.delete("testKey");

        //取单个数loader...
        String testValue = cache.get("testKey");
        Assert.assertTrue("testKey_data".equals(testValue));

        //设置key可以获取到值
        testValue = cache.get("testKey");
        Assert.assertTrue("testKey_data".equals(testValue));

        //阻塞3S，缓存获取，取单个数loader...
        sleep(3000);
        testValue = cache.get("testKey");
        Assert.assertTrue("testKey_data".equals(testValue));

        //设置key，批量获取
        List<String> keyList = new ArrayList<>();
        keyList.add("testKey");
        keyList.add("testKeyNoValue");
        cache.setBulk(keyList);

        //取批量loader...
        Map<String, String> valueMap = cache.getBulk(keyList);
        Assert.assertTrue("testKey_data".equals(valueMap.get("testKey")));
        Assert.assertTrue(null == valueMap.get("testKeyNoValue"));

        System.out.println(valueMap);
    }

    private void sleep(long t) {
        try {
            Thread.sleep(t);
        } catch (Exception e) {
            //Ignore
        }
    }

}
