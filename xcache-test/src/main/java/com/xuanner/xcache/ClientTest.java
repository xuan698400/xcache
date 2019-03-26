package com.xuanner.xcache;

import com.xuanner.xcache.client.CacheClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuan on 2018/6/13.
 */
public class ClientTest {

    private CacheClient cacheClient;

    @Before
    public void setup() {
        cacheClient = ClientMarker.getSpyClient();
    }

    @Test
    public void test() {
        cacheClient.delete("testKey");

        //取不到值
        String testValue = (String) cacheClient.get("testKey");
        Assert.assertTrue(null == testValue);

        //设置key可以获取到值
        cacheClient.put("testKey", "testValue", 2000, TimeUnit.MILLISECONDS);
        testValue = (String) cacheClient.get("testKey");
        Assert.assertTrue("testValue".equals(testValue));

        //阻塞3S，缓存获取，取不到
        sleep(3000);
        testValue = (String) cacheClient.get("testKey");
        Assert.assertTrue(null == testValue);

        //设置key，批量获取
        cacheClient.put("testKey", "testValue", 2000, TimeUnit.MILLISECONDS);
        List<String> keyList = new ArrayList<>();
        keyList.add("testKey");
        keyList.add("testKeyNoValue");
        Map<String, Object> valueMap = (Map<String, Object>) cacheClient.getBulk(keyList);
        Assert.assertTrue("testValue".equals(valueMap.get("testKey")));
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
