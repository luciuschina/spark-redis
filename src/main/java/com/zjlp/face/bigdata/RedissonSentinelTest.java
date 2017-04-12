package com.zjlp.face.bigdata;

import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * Created by root on 4/12/17.
 */
public class RedissonSentinelTest {
    public static void main(String[] args) {
        Config config = new Config();
        config.useSentinelServers().setMasterName("mymaster")
                .addSentinelAddress("192.168.175.11:26379", "192.168.175.12:26379")
                .addSentinelAddress("192.168.175.13:26379");
        RedissonClient redisson = Redisson.create(config);
        RList<String> list= redisson.getList("myList1");
        list.add("aaa");
        redisson.shutdown();
    }
}
