package com.zjlp.face.bigdata.redis

import org.apache.spark.{SparkConf, SparkContext}
import org.redisson.Redisson
import org.redisson.api.{RList, RedissonClient}
import org.redisson.config.Config

/**
 * spark-submit --class com.zjlp.face.bigdata.SparkRedisson  --master spark://192.168.70.11:7077
 * /data/work/luciuschina/spark-redis/target/spark-redis-1.0.jar
 */
object SparkRedissonCluster {
  def main(args: Array[String]) =  {
    val conf = new SparkConf()
    .setAppName("spark demo").setMaster("local[4]")
    val sc = new SparkContext(conf)
    sc.makeRDD(Seq("a","b","abc","efg"),2).foreachPartition{
      data=>
      val config: Config = new Config
      config.useClusterServers.setScanInterval(2000)
        .addNodeAddress("192.168.175.11:6379", "192.168.175.12:6379", "192.168.175.13:6379")
      val redisson: RedissonClient = Redisson.create(config)
      val list: RList[String] = redisson.getList("myList1")
        data.foreach(list.add(_))
        redisson.shutdown
    }
    sc.stop()
  }
}
