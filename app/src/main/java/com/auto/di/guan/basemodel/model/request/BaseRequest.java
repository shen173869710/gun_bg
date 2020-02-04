package com.auto.di.guan.basemodel.model.request;


import com.auto.di.guan.BaseApp;
import com.auto.di.guan.BuildConfig;
import com.auto.di.guan.api.GlobalConstant;
import com.auto.di.guan.basemodel.model.respone.LoginRespone;
import com.auto.di.guan.utils.Md5Util;

import java.util.TreeMap;

/**
 * Created by Administrator on 2017/6/26.
 * 请求的基类
 */

public class BaseRequest {


    /***
     * 公共请求参数签名,发送http商户请求请求TreeMap
     * @param map
     * @return
     */
    public static TreeMap<String, Object> toMerchantTreeMap(TreeMap<String, Object> map) {
        return toMerchantMap(map, GlobalConstant.MERCHANT_SECRET);
    }


    /***
     * 公共请求参数签名,发送http商户请求TreeMap(设备码激活)
     * @param map
     * @return
     */
    public static TreeMap<String, Object> toMerchantDeviceTreeMap(TreeMap<String, Object> map) {
        return toMerchantMap(map, BuildConfig.secret);
    }

    /***
     * 公共请求参数签名,发送http支付请求请求TreeMap
     * @param map
     * @return
     */
    public static TreeMap<String,Object> toPaymentTreeMap(TreeMap<String, Object> map){
        /*if(null !=map.get("deviceId") && !TextUtils.isEmpty(map.get("deviceId").toString())){
            return toPaymentTreeMap(map, BuildConfig.secret);
        }else{
            return toPaymentTreeMap(map, GlobalConstant.PAYMENT_SECRET);
        }*/
        return toPaymentTreeMap(map, BuildConfig.pay_secret);
    }


    /**
     * 指定screct组装发送http请求TreeMap
     * @param treeMap
     * @param screct
     * @return
     */
    private  static TreeMap<String, Object> toMerchantMap(TreeMap<String, Object> treeMap,String screct) {
        LoginRespone loginRespone= BaseApp.getLoginRespone();
        String token = "";
        if (null != loginRespone){
            token = loginRespone.getToken();
        }

        treeMap.put("client", "androidCashier");
        treeMap.put("token", token);
        treeMap.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));

        String sign = Md5Util.sign(treeMap, screct);
        treeMap.put("sign", sign);
        if(treeMap.containsKey("secret")){
            treeMap.remove("secret");
        }
        return treeMap;
    }

    /**
     * 指定screct组装发送http请求TreeMap
     * @param treeMap
     * @param screct
     * @return
     */
    public static TreeMap<String,Object> toPaymentTreeMap(TreeMap<String, Object> treeMap,String screct){
        treeMap.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
        String sign = Md5Util.sign(treeMap, screct);
        treeMap.put("sign", sign);
        if(treeMap.containsKey("secret")){
            treeMap.remove("secret");
        }
        return treeMap;
    }




}

