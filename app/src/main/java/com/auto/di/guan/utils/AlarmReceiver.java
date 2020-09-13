package com.auto.di.guan.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.entity.PollingEvent;

import org.greenrobot.eventbus.EventBus;

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
