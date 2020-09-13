package com.auto.di.guan.mqtt;

import android.util.Log;

import com.auto.di.guan.utils.LogUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttSimple {
    private final String TAG = "MqttSimple";
    private MqttAndroidClient mqttAndroidClient;
    public MqttSimple(MqttAndroidClient mqttAndroidClient) {
        this.mqttAndroidClient = mqttAndroidClient;
    }

    public void init() {
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                //when connect success ,need resub
                subscribeToTopic();
            }

            @Override
            public void connectionLost(Throwable cause) {
                LogUtils.e(TAG+"-----close", "connectionLost", cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String body = new String(message.getPayload());
                LogUtils.e(TAG+"-----messageArrived", body);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setConnectionTimeout(3000);
        mqttConnectOptions.setKeepAliveInterval(90);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(true);

        try {
            // Signature 方式
            mqttConnectOptions.setUserName("Signature|" + Config.accessKey + "|" + Config.instanceId);
            mqttConnectOptions.setPassword(Tool.macSignature(Config.clientId, Config.secretKey).toCharArray());

            /**
             * Token方式
             *  mqttConnectOptions.setUserName("Token|" + Config.accessKey + "|" + Config.instanceId);
             *  mqttConnectOptions.setPassword("RW|xxx");
             */
        } catch (Exception e) {
            LogUtils.e(TAG+"-----exception", "setPassword", e);
        }

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    LogUtils.e(TAG+"-----connect", "onSuccess");
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    LogUtils.e(TAG+"-----connect", "onFailure", exception);
                }
            });
        } catch (MqttException e) {
            LogUtils.e(TAG+"-----connect", "exception", e);
        }
    }

    public void subscribeToTopic() {
        try {
            final String topicFilter[] = {Config.topic};
            final int[] qos = {1};
            mqttAndroidClient.subscribe(topicFilter, qos, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    LogUtils.e(TAG+"-----subscribe", "success");

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    LogUtils.e(TAG+"-----subscribe", "failed", exception);
                }
            });

        } catch (MqttException ex) {
            LogUtils.e(TAG+"-----subscribe", "exception", ex);
        }
    }


    public void publishMessage(String msg) {
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(msg.getBytes());
            mqttAndroidClient.publish(Config.p2pTopic, message, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    LogUtils.e(TAG+"-----publish", "success:" + msg);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    LogUtils.e(TAG+"-----publish", "failed:" + msg);
                }
            });
        } catch (MqttException e) {
            LogUtils.e(TAG+"-----publish", "exception", e);
        }
    }

}
