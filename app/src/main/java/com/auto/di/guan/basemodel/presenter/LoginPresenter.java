package com.auto.di.guan.basemodel.presenter;


import com.auto.di.guan.api.HttpManager;
import com.auto.di.guan.basemodel.model.request.BaseRequest;
import com.auto.di.guan.basemodel.model.respone.BaseRespone;
import com.auto.di.guan.basemodel.view.ILoginView;
import com.auto.di.guan.utils.MacInfo;
import com.auto.di.guan.utils.Md5Util;

import java.util.TreeMap;

/**
 * Created by czl on 2019/7/9.
 * 用户登录相关逻辑业务
 */

public class LoginPresenter extends BasePresenter<ILoginView>{

    /**
     *  设备激
     * **/
    public void doDeviceActivation(String loginName,String pwd) {
        String mac = MacInfo.getMacAddress();
       TreeMap<String, Object> treeMap = new TreeMap<>();
       treeMap.put("loginName",loginName);
       treeMap.put("password", pwd);
       treeMap.put("mac", mac);
       doHttpTask(getApiService().deviceActivation(BaseRequest.toMerchantDeviceTreeMap(treeMap)),
               new HttpManager.OnResultListener() {
            @Override
            public void onSuccess(BaseRespone respone) {
                if (respone != null && respone.isOk() && null !=respone.getData()) {
                    getBaseView().activationSuccess(respone);
                }else{
                    getBaseView().activationFail(null,-1, "设备码不存在,请重新输入!");
                }
            }

            @Override
            public void onError(Throwable error, Integer code,String msg) {
                getBaseView().activationFail(error,code, msg);
            }
       });
    }

    /**
     *
     * 登录请求
     * **/
    public void doLogin(String userName, final String pwd) {
        String password = Md5Util.md5(pwd);
        String mac = MacInfo.getMacAddress();
        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put("loginName",userName);
        treeMap.put("password",password);
        treeMap.put("mac", mac);
        doHttpTask(getApiService().login(BaseRequest.toMerchantTreeMap(treeMap)), new HttpManager.OnResultListener() {
            @Override
            public void onSuccess(BaseRespone respone) {
                getBaseView().loginSuccess(respone);
            }
            @Override
            public void onError(Throwable error, Integer code,String msg) {
                getBaseView().loginFail(error,code,msg);
            }
        });
    }

}
