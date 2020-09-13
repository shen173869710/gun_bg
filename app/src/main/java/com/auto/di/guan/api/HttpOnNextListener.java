package com.auto.di.guan.api;


import com.auto.di.guan.basemodel.model.respone.BaseRespone;

/**
 * 成功回调处理
 * Created by czl on 2019/7/9.
 */
public interface HttpOnNextListener {

    /**
     * 成功后回调方法
     *
     * @param resulte
     * @param mothead
     */
    void onNext(String resulte, BaseRespone mothead);

    /**
     * 失败
     * 失败或者错误方法
     * 自定义异常处理
     *
     */
    void onError();


}
