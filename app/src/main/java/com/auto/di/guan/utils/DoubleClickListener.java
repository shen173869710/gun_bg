package com.auto.di.guan.utils;

import android.view.View;

public abstract class DoubleClickListener implements View.OnClickListener {

    // 两次点击按钮之间的点击间隔
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public abstract void onMultiClick(View v);


    @Override
    public void onClick(View v) {
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) <= MIN_CLICK_DELAY_TIME) {
            onMultiClick(v);
        } else {
            lastClickTime = curClickTime;
            ToastUtils.showLongToast("再次点击关闭");
        }
    }
}

