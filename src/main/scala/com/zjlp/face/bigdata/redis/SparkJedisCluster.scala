package com.zjlp.face.bigdata.redis

import java.util

import org.apache.spark.{SparkConf, SparkContext}
import redis.clients.jedis._

/**
 *  spark-submit --class com.zjlp.face.bigdata.SparkJedis  --master spark://192.168.70.11:7077
 * /data/work/luciuschina/spark-redis/target/spark-redis-1.0.jar
 */
object SparkJedisCluster {
  def main(args: Array[String]) = {
    val conf = new SparkConf()
      .setAppName("spark demo")
    val sc = new SparkContext(conf)
    sc.makeRDD(Seq("a2", "b2", "abc2", "efg2"), 2).foreachPartition {
      data =>
        val jedisClusterNodes: util.Set[HostAndPort] = new util.HashSet[HostAndPort]()
        jedisClusterNodes.add(new HostAndPort("192.168.70.11", 7000))
        jedisClusterNodes.add(new HostAndPort("192.168.70.12", 7000))
        jedisClusterNodes.add(new HostAndPort("192.168.70.13", 7000))
        val jc: JedisCluster = new JedisCluster(jedisClusterNodes)
        data.foreach(a => jc.rpush("myList1", a))
        jc.close()
    }
    sc.stop()
  }

}
