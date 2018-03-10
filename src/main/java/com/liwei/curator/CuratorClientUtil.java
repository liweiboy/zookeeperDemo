package com.liwei.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorClientUtil {

    private final static String CONNECTSTRING = "192.168.137.128:2181,192.168.137.129:2181,192.168.137.131:2181";
    private static CuratorFramework curatorFramework;

    public static CuratorFramework getInstance(){
        curatorFramework = CuratorFrameworkFactory.
                newClient(CONNECTSTRING,5000,5000,
                        new ExponentialBackoffRetry(1000,3));
        curatorFramework.start();
        return curatorFramework;
    }
}
