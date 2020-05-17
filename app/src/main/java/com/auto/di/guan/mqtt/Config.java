package com.auto.di.guan.mqtt;

public class Config {
    public static final String serverUri = "tcp://post-cn-mp91ixdux02.mqtt.aliyuncs.com:1883";
    public static final String clientId = "GID_Smart@@@"+System.currentTimeMillis();
    public static final String instanceId = "1111111111";
    public static final String accessKey = "2222222222222222";
    public static final String secretKey = "3333333333333333";
    public static final String topic = "topic_smart";


//    public static final String p2pTopic =topic+"/p2p/"+clientId;
public static final String p2pTopic =topic;
}
