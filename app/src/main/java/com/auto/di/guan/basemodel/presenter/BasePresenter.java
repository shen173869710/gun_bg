package com.auto.di.guan.basemodel.presenter;
import com.auto.di.guan.BaseApp;
import com.auto.di.guan.api.ApiService;
import com.auto.di.guan.api.ApiUtil;
import com.auto.di.guan.api.GlobalConstant;
import com.auto.di.guan.api.HttpManager;
import com.auto.di.guan.basemodel.view.BaseView;
import com.auto.di.guan.utils.ToastUtils;


import io.reactivex.Observable;

public class BasePresenter<T extends BaseView> implements Presenter<T> {
    private String TAG = "BasePresenter";
    private T mBaseView;
    private ApiService apiService;
    private HttpManager httpManager;

    @Override
    public void attachView(T baseView) {
        mBaseView = baseView;
        apiService = ApiUtil.createMerchantAPIService();
        httpManager = new HttpManager();
    }

    @Override
    public void detachView() {
        mBaseView = null;
    }

    @Override
    public void destroy() {

    }


    public T getBaseView() {
        return mBaseView;
    }

    public ApiService getApiService() {
        return apiService;
    }

    public HttpManager getHttpManager() {
        return httpManager;
    }

    public void doHttpTask(Observable observable, final HttpManager.OnResultListener onResultListener) {
        if (BaseApp.getInstance().isConnectNomarl()) {
            httpManager.doHttpTaskWithDialog(getBaseView(), observable,onResultListener);
        }else {
            String error = GlobalConstant.SERVER_ERROR;
            if (onResultListener != null) {
                onResultListener.onError(null,500, error);
            }
            ToastUtils.showToast(error);
        }
    }

    public void doHttpTaskWihtDialog(Observable observable, final HttpManager.OnResultListener onResultListener) {
        if (BaseApp.getInstance().isConnectNomarl()) {
            httpManager.doHttpTaskWithDialog(getBaseView(), observable,onResultListener);
        }else {
            String error = GlobalConstant.SERVER_ERROR;
            if (onResultListener != null) {
                onResultListener.onError(null,500, error);
            }
            ToastUtils.showToast(error);
        }

    }
}

