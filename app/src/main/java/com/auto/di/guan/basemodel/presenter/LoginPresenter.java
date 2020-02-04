package com.auto.di.guan.basemodel.presenter;



import com.auto.di.guan.api.HttpManager;
import com.auto.di.guan.basemodel.model.request.BaseRequest;
import com.auto.di.guan.basemodel.model.respone.BaseRespone;
import com.auto.di.guan.basemodel.view.ILoginView;
import com.auto.di.guan.utils.Md5Util;
import com.auto.di.guan.utils.ToastUtils;

import java.util.TreeMap;

/**
 * Created by czl on 2019/7/9.
 * 用户登录相关逻辑业务
 */

public class LoginPresenter extends BasePresenter<ILoginView>{

//    /**
//     *  设备激活
//     * **/
//    public void doDeviceActivation(String deviceCode,String deviceIdValue) {
//       TreeMap<String, Object> treeMap = new TreeMap<>();
//       treeMap.put("code",deviceCode);
//       treeMap.put("deviceIdValue", deviceIdValue);
//       doHttpTask(getApiService().deviceActivation(BaseRequest.toMerchantDeviceTreeMap(treeMap)),
//               new HttpManager.OnResultListener() {
//            @Override
//            public void onSuccess(BaseRespone respone) {
//                if (respone != null && respone.isOk() && null !=respone.getData()) {
//                    getBaseView().loginSuccess(respone);
//                    SharedPreferencesUtils.getInstance().putString(SharedPreferencesUtils.DEVICE_ID_VALUE,deviceIdValue);
//                }else{
//                    getBaseView().loginFail(null,-1, "设备码不存在,请重新输入!");
//                }
//            }
//
//            @Override
//            public void onError(Throwable error, Integer code,String msg) {
//                getBaseView().loginFail(error,code, msg);
//            }
//       });
//    }

    /**
     *
     * 登录请求
     * **/
    public void doLogin(String userName, final String pwd) {
        String password = Md5Util.md5(pwd);

        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put("username",userName);
        treeMap.put("password",password);
//        treeMap.put("client", GlobalConstant.client);
//        treeMap.put("deviceId",BaseApp.getInstance().getDeviceId());  //设备ID
//        treeMap.put("code", BaseApp.getInstance().getDeviceCode()+"");
//        treeMap.put("deviceIdValue",deviceIdValue);


        doHttpTask(getApiService().login(BaseRequest.toMerchantTreeMap(treeMap)), new HttpManager.OnResultListener() {
            @Override
            public void onSuccess(BaseRespone respone) {
                if(respone.getCode() ==4999 || respone.getCode()==4888 || respone.getCode()==4777){
                    ToastUtils.showLongToast("设备未绑定商户或已报废,请联系管理员!");
                    return;
                }



            }
            @Override
            public void onError(Throwable error, Integer code,String msg) {
                getBaseView().loginFail(error,code,msg);
            }
        });
    }

}
