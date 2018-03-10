package com.liwei.javaapi;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AuthControlDemo implements Watcher {
    private final static String CONNECTSTRING = "192.168.137.128:2181,192.168.137.129:2181,192.168.137.130:2181";
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        zooKeeper = new ZooKeeper(CONNECTSTRING, 5000,new AuthControlDemo());
        countDownLatch.await();

        ACL acl = new ACL(ZooDefs.Perms.CREATE,new Id("digest","root:root"));
        List<ACL> acls = new ArrayList<ACL>();
        acls.add(acl);
        zooKeeper.create("/auth","123".getBytes(), acls, CreateMode.PERSISTENT);

        zooKeeper.addAuthInfo("digest","root:root".getBytes());
        zooKeeper.create("/auth","123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        ZooKeeper zooKeeper1 = new ZooKeeper(CONNECTSTRING,5000,new AuthControlDemo());
        zooKeeper1.delete("/auth",-1);


    }

    public void process(WatchedEvent watchedEvent) {
        //如果当前的连接状态是连接成功的，那么通过计数器去控制
        if (watchedEvent.getState()== Watcher.Event.KeeperState.SyncConnected){
            if (watchedEvent.getType()== Watcher.Event.EventType.None && watchedEvent.getPath()==null){
                countDownLatch.countDown();
                System.out.println(watchedEvent.getState()+"-->"+watchedEvent.getType());
            }/*else if (watchedEvent.getType()== Watcher.Event.EventType.NodeDataChanged){
                try {
                    System.out.println("数据变更触发：路径"+watchedEvent.getPath()+"->改变后的值："+
                            new String(zooKeeper.getData(watchedEvent.getPath(),true,stat)));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if (watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged){
                try {
                    System.out.println("子节点数据：变更路径"+watchedEvent.getPath()+"->节点的值："+
                            new String(zooKeeper.getData(watchedEvent.getPath(),true,stat)));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if (watchedEvent.getType() == Watcher.Event.EventType.NodeCreated){
                try {
                    System.out.println("节点创建：路径"+watchedEvent.getPath()+"->节点的值："+
                            new String(zooKeeper.getData(watchedEvent.getPath(),true,stat)));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if (watchedEvent.getType() == Watcher.Event.EventType.NodeDeleted){//子节点删除会触发
                System.out.println("节点删除：路径"+watchedEvent.getPath()+"被删除");
            }*/
        }
//        System.out.println(watchedEvent.getType());
    }
}
