package com.zjlp.face.bigdata;


import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class App {
    public static void main(String[] args) {
        // 1. Create config object
        Config config = new Config();
        config.useClusterServers()
                .setScanInterval(2000) // cluster state scan interval in milliseconds
                .addNodeAddress("192.168.70.11:7000", "192.168.70.12:7000", "192.168.70.13:7000");

// 2. Create Redisson instance
        RedissonClient redisson = Redisson.create(config);
        RList<String> list = redisson.getList("myList");
        list.add("lingxin");
        redisson.shutdown();

    }
}
