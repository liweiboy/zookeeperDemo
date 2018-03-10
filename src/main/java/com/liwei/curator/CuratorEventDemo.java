package com.liwei.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.TimeUnit;

public class CuratorEventDemo {


    /**
     * 三种watcher来做节点的监听
     * pathcache 监视一个路径下子节点的创建、删除、节点数据更新
     * NodeCache 监视一个节点的创建、更新、删除
     * TreeCache pathcache+nodecache的合体(监视路径下的创建、更新、删除时间)，缓存路径下的所有子节点的数据
     */
    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework = CuratorClientUtil.getInstance();
        /**
         * 节点变化NodeCache
         */
        /*final NodeCache nodeCache = new NodeCache(curatorFramework,"/curator");
        nodeCache.start(true);

        nodeCache.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                System.out.println("节点数据数据发生变化，变化后的结果"+":"+new String(nodeCache.getCurrentData().getData()));
            }
        });
        curatorFramework.setData().forPath("/curator","feifei".getBytes());*/

        /**
         * PathChildrenCache
         */
        PathChildrenCache cache = new PathChildrenCache(curatorFramework,"/event",true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                switch (pathChildrenCacheEvent.getType()){
                    case CHILD_ADDED:
                        System.out.println("增加子节点");
                        break;
                    case CHILD_REMOVED:
                        System.out.println("删除子节点");
                        break;
                    case CHILD_UPDATED:
                        System.out.println("更新子节点");
                        break;
                    default:break;
                }
            }
        });
        curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/event","event".getBytes());
        TimeUnit.SECONDS.sleep(1);
        System.out.println(1);
        curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath("/event/event1","1".getBytes());
        TimeUnit.SECONDS.sleep(1);
        System.out.println(2);
        curatorFramework.setData().forPath("/event/event1","2222".getBytes());
        TimeUnit.SECONDS.sleep(1);
        System.out.println(3);
        curatorFramework.delete().forPath("/event/event1");
        System.out.println(4);
        System.in.read();
    }

}
