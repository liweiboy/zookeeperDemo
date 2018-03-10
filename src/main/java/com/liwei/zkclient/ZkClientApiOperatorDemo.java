package com.liwei.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ZkClientApiOperatorDemo {
    private final static String CONNECTSTRING = "192.168.137.128:2181,192.168.137.129:2181,192.168.137.130:2181";

    public static ZkClient getInstance(){
        return new ZkClient(CONNECTSTRING,5000);
    }

    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = getInstance();
        //zkClient 提供递归创建父节点的功能
        //zkClient.createEphemeral("/zkClient");

        //zkClient.delete("/node/node1");
        //zkClient.deleteRecursive("/node");//递归删除

        //获取子节点
        //List<String> list = zkClient.getChildren("/node");
        //System.out.println(list);

        //watcher
        zkClient.subscribeDataChanges("/node", new IZkDataListener() {
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println(s+"->"+o);
            }

            public void handleDataDeleted(String s) throws Exception {

            }
        });

        zkClient.writeData("/node","node");
        TimeUnit.SECONDS.sleep(2);
    }


}
