package com.auto.di.guan.utils;

import java.util.TreeMap;


public class Md5Util {

    public static String sign(TreeMap<String, Object> map, String secret) {
        StringBuilder builder = new StringBuilder();
        for (String key : map.keySet()) {
            if (key.equals("sign")) {
                continue;
            }

            Object value = map.get(key);
            if(null == value || "null".equals(value) || String.valueOf(value).length() == 0){
                map.put(key,"");
            }else{
                builder.append(key).append("=").append(value).append("&");// 拼接字符串
            }
        }

        String linkString = builder.toString().concat("secret=").concat(secret);
        LogUtils.i("linkString",linkString);
        return md5(linkString);
    }


    public static String md5(String data) {
        return new String(data);
    }
}
