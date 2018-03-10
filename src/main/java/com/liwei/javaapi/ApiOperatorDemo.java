package com.liwei.javaapi;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ApiOperatorDemo implements Watcher{
    private final static String CONNECTSTRING = "192.168.137.128:2181,192.168.137.129:2181,192.168.137.130:2181";
    //private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper;
    private static Stat stat = new Stat();
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper(CONNECTSTRING, 5000,new ApiOperatorDemo());
        //countDownLatch.await();
        System.out.println("------"+zooKeeper.getState());

        //创建节点
        String result = zooKeeper.create("/mic1", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zooKeeper.getData("/mic1",new ApiOperatorDemo(),stat);
        System.out.println("创建成功"+result);
        Thread.sleep(2000);
        //修改节点
        zooKeeper.setData("/mic1","mic123".getBytes(),-1);
        Thread.sleep(2000);

        zooKeeper.setData("/mic1","mic234".getBytes(),-1);
        Thread.sleep(2000);

        //节点删除
        zooKeeper.delete("/mic1",-1);
        Thread.sleep(2000);

        //创建节点和子节点
        String path = "/node";
        zooKeeper.create(path,"123".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        TimeUnit.SECONDS.sleep(1);
        Stat stat1 = zooKeeper.exists(path + "/node1", true);
        if(stat1==null){//表示节点不存在
            zooKeeper.create(path+"/node1","123".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            TimeUnit.SECONDS.sleep(1);
        }
        //修改子路径
        zooKeeper.setData(path+"/node1","mic123".getBytes(),-1);
        TimeUnit.SECONDS.sleep(1);


    }

    public void process(WatchedEvent watchedEvent) {
        //如果当前的连接状态是连接成功的，那么通过计数器去控制
        if (watchedEvent.getState()==Event.KeeperState.SyncConnected){
            if (watchedEvent.getType()==Event.EventType.None && watchedEvent.getPath()==null){
                //countDownLatch.countDown();
                System.out.println(watchedEvent.getState()+"->"+watchedEvent.getType());
            }else if (watchedEvent.getType()==Event.EventType.NodeDataChanged){
                try {
                    System.out.println("数据变更触发：路径"+watchedEvent.getPath()+"->改变后的值："+
                            new String(zooKeeper.getData(watchedEvent.getPath(),true,stat)));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged){
                try {
                    System.out.println("子节点数据：变更路径"+watchedEvent.getPath()+"->节点的值："+
                            new String(zooKeeper.getData(watchedEvent.getPath(),true,stat)));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if (watchedEvent.getType() == Event.EventType.NodeCreated){
                try {
                    System.out.println("节点创建：路径"+watchedEvent.getPath()+"->节点的值："+
                            new String(zooKeeper.getData(watchedEvent.getPath(),true,stat)));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if (watchedEvent.getType() == Event.EventType.NodeDeleted){//子节点删除会触发
                System.out.println("节点删除：路径"+watchedEvent.getPath()+"被删除");
            }
        }
        System.out.println(watchedEvent.getType());
    }
}
