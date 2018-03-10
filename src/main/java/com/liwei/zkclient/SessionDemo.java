package com.liwei.zkclient;

import org.I0Itec.zkclient.ZkClient;

public class SessionDemo {
    private final static String CONNECTSTRING = "192.168.137.128:2181,192.168.137.129:2181,192.168.137.130:2181";

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient(CONNECTSTRING,4000);
        System.out.println(zkClient+"-->success");
    }
}
