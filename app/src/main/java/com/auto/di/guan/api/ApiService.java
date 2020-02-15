package com.auto.di.guan.api;


import com.auto.di.guan.basemodel.model.respone.BaseRespone;
import com.auto.di.guan.basemodel.model.respone.LoginRespone;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 请求的相关接口
 */
public interface ApiService {



    /**
     *  设备激活
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/open/device/login")
    Observable<BaseRespone<LoginRespone>> deviceActivation(@FieldMap Map<String, Object> map);

    /**
     *  用户登录接口
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/open/device/login")
    Observable<BaseRespone<LoginRespone>> login(@FieldMap Map<String, Object> map);
}
