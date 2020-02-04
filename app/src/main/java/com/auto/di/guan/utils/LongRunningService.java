package com.auto.di.guan.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import io.reactivex.annotations.Nullable;


/**
 * Created by Administrator on 2018/7/10.
 */

public class LongRunningService extends Service {




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
