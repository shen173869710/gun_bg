package com.auto.di.guan.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.auto.di.guan.MyApplication;
import com.auto.di.guan.SerialPort;
import com.auto.di.guan.SerialPortActivity;
import com.auto.di.guan.db.DBManager;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.entity.PollingEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Administrator on 2018/3/21 0021.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(), Entiy.ALERM_ACTION)) {
            Log.e("开始查询",intent.getAction()+""+ System.currentTimeMillis());
            EventBus.getDefault().post(new PollingEvent());
        }
    }


}
