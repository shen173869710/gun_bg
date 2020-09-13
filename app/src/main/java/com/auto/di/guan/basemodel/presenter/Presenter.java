package com.auto.di.guan.basemodel.presenter;


import com.auto.di.guan.basemodel.view.BaseView;

public interface Presenter<V extends BaseView> {
    void attachView(V mvpView);
    void detachView();
    void destroy();
}
