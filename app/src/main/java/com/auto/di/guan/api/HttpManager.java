package com.auto.di.guan.api;


import android.text.TextUtils;

import com.auto.di.guan.basemodel.model.respone.BaseRespone;
import com.auto.di.guan.basemodel.view.BaseView;
import com.auto.di.guan.utils.LogUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by czl on 2019/9/7.
 */

public class HttpManager {
    public static final String TAG = "HttpManager";

    /**
     *    有loading 的请求
     * @param baseView
     * @param observable
     * @param onResultListener
     */
    public void doHttpTaskWithDialog(final BaseView baseView, Observable observable, final OnResultListener onResultListener) {
        if (baseView != null) {
            baseView.showDialog();
        }

        DisposableObserver disposableObserver = new DisposableObserver() {
            @Override
            public void onNext( Object obj) {
                try{
                    if(!isDisposed()){
                        if (baseView != null) {
                            baseView.dismissDialog();
                            BaseRespone respone =  (BaseRespone) obj;
                            if(null == respone) {
                                onResultListener.onError(new Exception(),500, GlobalConstant.SERVER_ERROR);
                            }else{
                                if(respone.isOk()){
                                    onResultListener.onSuccess(respone);
                                }else if (respone.getCode() == 401){

                                }else {
                                    String message = respone.getMessage();
                                    if(TextUtils.isEmpty(message)){
                                        message = GlobalConstant.SERVER_ERROR;
                                    }
                                    onResultListener.onError(new Exception(),respone.getCode(), message);
                                }
                            }
                        }
                    }
                }catch (Exception e){
                }
            }

            @Override
            public void onError(Throwable e) {
                if(!isDisposed()){
                    LogUtils.e(TAG, "doHttpTaskWithDialog==onError===" + e.toString());
                    if (baseView != null) {
                        baseView.dismissDialog();
                        onResultListener.onError(e, 500,GlobalConstant.SERVER_ERROR);
                    }
                }
            }

            @Override
            public void onComplete() {
                if (baseView != null) {
                    baseView.dismissDialog();
                }

                if(!isDisposed()){
                    dispose();
                }
            }
        };

        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()// 当执行了d.dispose()方法后将解除上下游的引用
                .compose(baseView.bindLifecycle())
                .subscribeWith(disposableObserver);
    }

    /**
     *         无loading 的请求
     * @param baseView
     * @param observable
     * @param onResultListener
     */
    public static void  doHttpTask(final BaseView baseView, Observable observable, final OnResultListener onResultListener) {
        DisposableObserver disposableObserver = new DisposableObserver() {
            @Override
            public void onNext(Object obj) {
                try{
                    if(!isDisposed()){
                        if (baseView != null) {
                            baseView.dismissDialog();
                            BaseRespone respone =  (BaseRespone) obj;
                            if(null == respone) {
                                onResultListener.onError(new Exception() ,500, GlobalConstant.SERVER_ERROR);
                            }else{
                                if(respone.isOk()){
                                    onResultListener.onSuccess(respone);
                                }else{
                                    String message = respone.getMessage();
                                    if(TextUtils.isEmpty(message)){
                                        message = "网络开小差,请重试";
                                    }
                                    onResultListener.onError(new Exception() , respone.getCode(), message);
                                }
                            }
                        }
                    }
                }catch (Exception e){
                }
            }
            @Override
            public void onError(Throwable e) {
                LogUtils.e(TAG, "onError===" + e.toString());
                if(!isDisposed()){
                    if (baseView != null) {
                        onResultListener.onError(e, 500,GlobalConstant.SERVER_ERROR);
                    }
                }
            }

            @Override
            public void onComplete() {
                if(!isDisposed()){
                    dispose();
                }
            }
        };

        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .onErrorResumeNext(funcException)
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()// 当执行了d.dispose()方法后将解除上下游的引用
                .compose(baseView.bindLifecycle())
                .subscribeWith(disposableObserver);
    }

    //接口回调
    public interface OnResultListener {
        void onSuccess(BaseRespone t);
        void onError(Throwable error, Integer code, String msg);
    }

    /**
     * 异常处理
     */
    static Function funcException = new Function<Throwable, Observable>() {
        @Override
        public Observable apply(Throwable throwable) throws Exception {
            return Observable.error(FactoryException.analysisExcetpion(throwable));
        }
    };
}
