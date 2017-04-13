package com.zjlp.face.bigdata.solr;


import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.common.SolrInputDocument;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * http://www.programcreek.com/java-api-examples/index.php?api=org.apache.solr.client.solrj.SolrClient
 * http://solr-user.lucene.apache.narkive.com/A1Cau0LW/concurrentupdatesolrclient-ignoring-the-collection-param-in-some-methods
 */
public class SolrJTest {
    public void CloudSolrClientTest() {
        //zkServerA:2181,zkServerB:2181,zkServerC:2181/solr
        String zkHostString = "192.168.175.11:2181/mysolr";

        CloudSolrClient solrCloudClient = new CloudSolrClient.Builder().withZkHost(zkHostString).build();
        solrCloudClient.setDefaultCollection("myco");

        long start = System.nanoTime();
        try {
            for (int i = 0; i < 1000000; ++i) {
                SolrInputDocument doc = new SolrInputDocument();
                doc.addField("cat", "book");
                doc.addField("id", "book-" + i);
                doc.addField("name", "The Legend of the Hobbit part " + i);
                solrCloudClient.add(doc);
                if (i % 5000 == 0) {
                   // System.out.println(i+"   "+TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start));
                    System.out.println("Every 5000 records flush it. cost:"+TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start));
                    solrCloudClient.commit(); // periodically flush
                }
            }
            solrCloudClient.commit();
            solrCloudClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        long seconds = TimeUnit.NANOSECONDS.toSeconds(end - start);
        System.out.println(" All records are indexed, took " + seconds + " seconds");
    }

    public void ConcurrentUpdateSolrClientTest() {
        String url="http://192.168.175.11:8984/solr";
        ConcurrentUpdateSolrClient solrClient= new ConcurrentUpdateSolrClient.Builder(url).withQueueSize(50000).withThreadCount(10).build();
        addDocsTest(solrClient);
        updateDocsTest(solrClient);
    }

    public static void addDocsTest(ConcurrentUpdateSolrClient solrClient) {
        long start = System.nanoTime();
        try {
            for (int i = 0; i < 20000000; ++i) {
                SolrInputDocument doc = new SolrInputDocument();
                doc.addField("cat", "book");
                doc.addField("id", "book-" + i);
                doc.addField("name", "The Legend of the Hobbit part " + i);
                solrClient.add("myco", doc); //trying to commit the document to a specific collection
                if (i % 500000 == 0) {
                    System.out.println(i + " cost:" + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start));
                    solrClient.commit("myco"); // commit to the same collection
                }
            }
            solrClient.commit("myco");
            solrClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        long seconds = TimeUnit.NANOSECONDS.toSeconds(end - start);
        System.out.println(" All records are indexed, took " + seconds + " seconds");
    }

    public static void updateDocsTest(ConcurrentUpdateSolrClient solrClient) {
        long start = System.nanoTime();
        try {
            for (int i = 0; i < 10000; ++i) {
                int id = new Random().nextInt(200000) +1;
                SolrInputDocument doc = new SolrInputDocument();
                doc.addField("cat", "book");
                doc.addField("id", "book-" + id);
                doc.addField("name", "11111" + i);
                solrClient.add("myco", doc); //trying to commit the document to a specific collection
                System.out.println(i + " 修改id为：" + id+ "的文档");
            }
            solrClient.commit("myco");
            solrClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        long seconds = TimeUnit.NANOSECONDS.toSeconds(end - start);
        System.out.println(" All records are update, took " + seconds + " seconds");
    }
}
