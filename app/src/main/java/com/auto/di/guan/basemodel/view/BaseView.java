package com.auto.di.guan.basemodel.view;

import android.app.Activity;

import com.trello.rxlifecycle3.LifecycleTransformer;

/**
 * Created by Administrator on 2017/6/21.
 */

public interface BaseView {

    /**
     * RxLifecycle用于绑定组件生命周期
     *
     * @return
     */
    LifecycleTransformer bindLifecycle();

    /**
     * 获取Activity实例
     *
     * @return
     */
    Activity getActivity();

    public void showDialog();

    public void dismissDialog();
}
