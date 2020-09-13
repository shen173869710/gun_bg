package com.auto.di.guan.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


/**
 *
 * @Description: 基于Gson封装的jsonUtil
 * @author hbb
 * @date 2019年1月1日
 *
 */
public class GsonUtil {

    private static Gson gson = null;

    static {
        synchronized (GsonUtil.class){
            if (gson == null) {
                gson = new Gson();
            }
        }
    }

    private GsonUtil() {
    }

    /**
     * 对象转成json
     *
     * @param object
     * @return json
     */
    public static String toJson(Object object) {
        String json = null;
        if (gson != null) {
            json = gson.toJson(object);
        }
        return json;
    }

    /**
     * Json转成对象
     *
     * @param json
     * @param cls
     * @return 对象
     */
    public static <T> T fromJson(String json, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(json, cls);
        }
        return t;
    }

    public static <T> T fromJson(String json, Type type) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(json, type);
        }
        return t;
    }


    /**
     * json转成list<T>
     *
     * @param json
     * @param cls
     * @return list<T>
     */
    public static <T> List<T> gsonToList(String json, Class<T> cls) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(json, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    /**
     * json转成list中有map的
     *
     * @param json
     * @return List<Map<String, T>>
     */
    public static <T> List<Map<String, T>> gsonToListMaps(String json) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(json, new TypeToken<List<Map<String, T>>>() {
            }.getType());
        }
        return list;
    }

    /**
     * json转成map的
     *
     * @param json
     * @return Map<String, T>
     */
    public static <T> Map<String, T> gsonToMaps(String json) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(json, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }
}
