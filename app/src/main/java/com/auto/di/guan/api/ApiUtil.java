package com.auto.di.guan.api;

import com.auto.di.guan.BuildConfig;
import com.auto.di.guan.utils.LogUtils;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by czl on 2019-7-9.
 */
public class ApiUtil {
    public static String TAG = "RetrofitClient";

    private static ApiService paymentApiService = null;
    private static ApiService merchantApiService = null;

    private static ApiService getPaymentInstance(){
        if(null ==paymentApiService){
            synchronized(ApiService.class){
                if(null ==paymentApiService){
                    //初始化 retrofit
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("")
                            .client(GwsOkHttpClient.getInstance())
                            .addConverterFactory(MyGsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                    paymentApiService = retrofit.create(ApiService.class);
                }
            }
        }
        return paymentApiService;
    }

    private static ApiService getMerchantApiInstance(){
        if(null == merchantApiService){
            synchronized(ApiService.class){
                if(null == merchantApiService){
                    //初始化 retrofit
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BuildConfig.http_post_url)
                            .client(GwsOkHttpClient.getInstance())
                            .addConverterFactory(MyGsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                    merchantApiService = retrofit.create(ApiService.class);
                }
            }
        }
        return merchantApiService;
    }

    /**
     * 对外提供调用支付API的接口
     *
     * @return
     */
    public static ApiService createPaymentAPIService() {
        return getPaymentInstance();
    }

    /**
     * 对外提供调用支付API的接口
     *
     * @return
     */
    public static ApiService createMerchantAPIService() {
        return getMerchantApiInstance();
    }


    /**
     * 发送http post表单数据
     *
     * @param requestUrl
     * @param bodyParams
     */
    public static void POST(String requestUrl, Map<String, Object> bodyParams) {
        try {
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            if (null != bodyParams && !bodyParams.isEmpty()) {
                for (Map.Entry<String, Object> item : bodyParams.entrySet()) {
                    bodyBuilder.add(item.getKey(), String.valueOf(item.getValue()));
                }
            }

            FormBody formBody = bodyBuilder.build();
            // 发送请求
            Request request = new Request
                    .Builder()
                    .url(BuildConfig.http_post_url + requestUrl)
                    .addHeader("Connection", "close")
                    .post(formBody)
                    .build();


            GwsOkHttpClient.getInstance().newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    LogUtils.e(TAG, e.getMessage());
                }

                @Override
                public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

                }
            });
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
    }
}
