package com.zjlp.face.bigdata.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import java.util.HashSet;

/**
 * https://github.com/xetorthio/jedis/blob/master/src/test/java/redis/clients/jedis/tests/JedisSentinelPoolTest.java
 */
public class JedisSentinelTest {
    public static void main(String[] args) {
        HashSet<String> sentinels = new HashSet<String>();
        sentinels.add(new HostAndPort("192.168.175.11", 26379).toString());
        sentinels.add(new HostAndPort("192.168.175.12", 26379).toString());
        sentinels.add(new HostAndPort("192.168.175.13", 26379).toString());
        JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinels, new GenericObjectPoolConfig(), 1000);
        Jedis jedis = pool.getResource();
        try {
            jedis.rpush("myList1", "jedis");
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        pool.destroy();
    }
}
