package com.auto.di.guan.utils;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by chenzhaolin on 2019/7/12.
 */

public class PostUtil {

    public static void post(PostData data) {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, new Gson().toJson(data));
        final Request request = new Request.Builder()
                .url("http://data.farmeasy.cn/farmeasy-equipment-service/device/trickle/add")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(" ======== ", "onFailure == "+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.e(" ======== ", "onResponse == "+new Gson().toJson(response));
            }
        });
    }

    public static class PostData{
        /**
         *      执行的行为
         *      0 自动轮灌
         *      1 手动开启
         *      2 手动关闭
         */
        public int operateResult;
        public int operateType;
        public String operateName;
        public String projectId;
        public String projectName;
        public String valveCode;
        public String valveName;
    }
}
