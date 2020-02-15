package com.auto.di.guan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.auto.di.guan.mqtt.Config;
import com.auto.di.guan.mqtt.MqttSimple;

import org.eclipse.paho.android.service.MqttAndroidClient;

public class WelcomeActivity extends Activity{



	private MqttAndroidClient mqttAndroidClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), Config.serverUri, Config.clientId);
		MqttSimple mqttSimple = new MqttSimple(mqttAndroidClient);
		mqttSimple.init();

//		List<User> users = UserSql.queryUserList();
//		if (users == null) {
//
//		}

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(WelcomeActivity.this, ActivationActivity.class));
			}
		},2000);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mqttAndroidClient.unregisterResources();
	}
}
