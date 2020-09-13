package com.auto.di.guan.api;

import android.os.Build;


import com.auto.di.guan.BaseApp;
import com.auto.di.guan.BuildConfig;
import com.auto.di.guan.utils.LogUtils;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

public class GwsOkHttpClient {

    private static String TAG ="OkHttpClient";
    public static int CONNECT_TIME = 10;
    public static int RADE_TIMEOUR = 60;


    private static okhttp3.OkHttpClient client = null;

    public static okhttp3.OkHttpClient getInstance(){
        if(null == client){
            synchronized(GwsOkHttpClient.class){
                if(null == client){
                    //实例化OkHttpClient
                    okhttp3.OkHttpClient.Builder okHttpClientBuilder = createOkHttp(CONNECT_TIME,RADE_TIMEOUR);

                    if (BuildConfig.DEBUG) {
                        if (okHttpClientBuilder.interceptors() != null) {
                            okHttpClientBuilder.interceptors().clear();
                        }

                        //在debug模式,log拦截器
                        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                            @Override
                            public void log(String message) {
                                LogUtils.i(TAG, "----request   " + message);
                            }
                        });

                        //这行必须加 不然默认不打印
                        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                        okHttpClientBuilder.addInterceptor(interceptor);
                    }

                    okHttpClientBuilder.addInterceptor(chain -> {
                        boolean isConnected = BaseApp.isConnectNomarl();
                        if (isConnected) {
                            return chain.proceed(chain.request());
                        } else {
                            throw new NoNetworkException(GlobalConstant.SERVER_ERROR);
                        }
                    });

                    //默认是MaxRequests=64,RequestsPerHost=5
                    //okhttp连接线程太多
                    Dispatcher dispatcher = new Dispatcher();
                    dispatcher.setMaxRequests(500);
                    dispatcher.setMaxRequestsPerHost(50);

                    client = okHttpClientBuilder
                            .dispatcher(dispatcher)
                            .connectionPool(new ConnectionPool(100,5,TimeUnit.MINUTES))  //默认最大5个连接池
                            .build();
                }
            }
        }
        return client;
    }

    public static okhttp3.OkHttpClient.Builder createOkHttp(){
        return createOkHttp(CONNECT_TIME, RADE_TIMEOUR);
    }

    private static okhttp3.OkHttpClient.Builder createOkHttp(int connectTime, int readTime){
        //初始化OkHttp
        okhttp3.OkHttpClient.Builder okHttpClientBuilder = null;
        synchronized (Object.class){
            try{
                okHttpClientBuilder = new okhttp3.OkHttpClient.Builder();
                //默认信任所有的证书
                final X509TrustManager trustManager = new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] xcs, String string)
                            throws CertificateException {}
                    public void checkServerTrusted(X509Certificate[] xcs, String string)
                            throws CertificateException {}
                    public X509Certificate[] getAcceptedIssuers() {
                        //Here
                        return new X509Certificate[]{};
                    }
                };

                final SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null,new TrustManager[]{trustManager} , new java.security.SecureRandom());
                final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                okHttpClientBuilder.sslSocketFactory(sslSocketFactory,trustManager);
            }catch (Exception e){
                LogUtils.e(TAG,"默认信任所有的证书异常:"+e.getMessage());
            }

            try{
                okHttpClientBuilder.hostnameVerifier(new HostnameVerifier(){

                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                });

                okHttpClientBuilder.addInterceptor(new Interceptor() {

                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        // MY_APP_NAME为APP名称，MY_APP_VERSION_NAME为应用的版本名
                        String appName="chuanbei";
                        String appNameAgent="cashier";
                        String userAgent =  String.format("%s/%s(Linux; Android %s; %s Build/%s)", appName, appNameAgent,
                                Build.VERSION.RELEASE, Build.MANUFACTURER, Build.ID);
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("User-Agent",  userAgent)
                                .header("Connection", "close")
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    }
                });

                okHttpClientBuilder.connectTimeout(connectTime, TimeUnit.SECONDS);
                okHttpClientBuilder.readTimeout(readTime , TimeUnit.SECONDS);
                okHttpClientBuilder.writeTimeout(readTime, TimeUnit.SECONDS);
                okHttpClientBuilder.retryOnConnectionFailure(false);
                return okHttpClientBuilder;
            }catch (Exception e){
                LogUtils.e(TAG,e.getMessage());
                return null;
            }
        }
    }
}
