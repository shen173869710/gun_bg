package com.auto.di.guan.basemodel.view;


import com.auto.di.guan.basemodel.model.respone.BaseRespone;

/**
 * Created by czl on 2019/7/9.
 * 登录页面抽象的接口
 */

public interface ILoginView extends BaseView{

    /***登录成功**/
    void loginSuccess(BaseRespone respone);
    /***登录失败**/
    void loginFail(Throwable error, Integer code, String msg);


    /***激活成功**/
    void activationSuccess(BaseRespone respone);
    /***激活失败**/
    void activationFail(Throwable error, Integer code, String msg);
}
