package com.liwei.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CuratorOperatorDemo {

    public static void main(String[] args) {
        CuratorFramework curatorFramework = CuratorClientUtil.getInstance();
        System.out.println("连接成功。。。。。。。");

        /**
         * 创建节点
         */
//        try {
//            String result = curatorFramework.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).
//                    forPath("/curator/curator1/curator11", "123".getBytes());
//            System.out.println(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        /**
         * 删除操作
         */
//        try {
//            //默认，version为-1
//            curatorFramework.delete().deletingChildrenIfNeeded().forPath("/node");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        /**
         * 获取数据
         */
//        Stat stat = new Stat();
//        try {
//            byte[] bytes = curatorFramework.getData().storingStatIn(stat).forPath("/curator/curator1/curator11");
//            System.out.println(new String(bytes));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        /**
         * 修改
         */
//        Stat stat = null;
//        try {
//            stat = curatorFramework.setData().forPath("/curator", "123".getBytes());
//            System.out.println(stat);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        /**
         * 异步操作
         */
//        ExecutorService executorService = Executors.newFixedThreadPool(1);
//        final CountDownLatch countDownLatch = new CountDownLatch(1);
//        try {
//            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).
//                    inBackground(new BackgroundCallback() {
//                        public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
//                            System.out.println(Thread.currentThread().getName()+"---->resultCode:"+curatorEvent.getResultCode()+"-->"
//                            +curatorEvent.getType());
//                            countDownLatch.countDown();
//                        }
//                    },executorService).forPath("/mic","123".getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        executorService.shutdown();

        /**
         * 事务
         */
        try {
            Collection<CuratorTransactionResult> curatorTransactionResults = curatorFramework.inTransaction().create().forPath("/trans", "111".getBytes()).and().
                    setData().forPath("/curator", "111".getBytes()).and().commit();
            for(CuratorTransactionResult result : curatorTransactionResults){
                System.out.println(result.getForPath()+"->"+result.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
