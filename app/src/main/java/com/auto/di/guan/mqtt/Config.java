package com.auto.di.guan.mqtt;

public class Config {
    public static final String serverUri = "tcp://post-cn-mp91ixdux02.mqtt.aliyuncs.com:1883";
    public static final String clientId = "GID_Smart@@@RECV0005";
    public static final String instanceId = "post-cn-mp91ixdux02";
    public static final String accessKey = "LTAI4FvBCkY9u92zFQWrGPVq";
    public static final String secretKey = "4ant7plK6mwQErXH28gv3o7TGtbT0R";
    public static final String topic = "topic_smart";


    public static final String p2pTopic =topic+"/p2p/"+clientId;
}
