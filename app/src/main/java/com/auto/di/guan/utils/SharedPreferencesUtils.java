package com.auto.di.guan.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.auto.di.guan.BaseApp;


import java.util.Map;


public class SharedPreferencesUtils {
    private SharedPreferences share;
    private SharedPreferences.Editor editor;

    //deviceId
    public static final String DEVICE_ID="device_id";

    //登录的token
    public static final String SAVE_LOGIN_INFO = "save_login_info";
    public static final String SAVE_TOKEN_INFO = "save_token_info";

    //登录的账号密码
    public static final String SAVE_PWD = "save_pwd";
    public static final String SAVE_ACCOUNT = "save_account";

    //今天是否请求过
    public static final String BUSINESS_STATE = "business_state";

    /**
     *  保存本地请求过的banner数据
     */
    public static final String BANNER_IMAGES = "banner_images";

    /**
     * 打印小票公司logo
     */
    public static final String COMPANY_TICKET_LOGO="company_ticket_logo";
    public static final String COMPANY_TICKET_HTTP_URL="COMPANY_TICKET_HTTP_URL";

    /**
     * 初始化设备是否保存设备唯一值
     */
    public static final String DEVICE_ID_VALUE ="DEVICE_ID_VALUE";

    /**
     * 设备管理密码
     */
    public static final String DEVICE_MANAGER_PASSWORD="DEVICE_MANAGER_PASSWORD";

    //订单购物车记录
    public static final String ORDER_CART_PREFIX="ORDER_CART_";

    //临时订单id
    public static final String ORDER_Id="orderId";


    /**
     * 单例模式
     */
    private static SharedPreferencesUtils instance;//单例模式 双重检查锁定
    public static SharedPreferencesUtils getInstance() {
        if (instance == null) {
            synchronized (SharedPreferencesUtils.class) {
                if (instance == null) {
                    instance = new SharedPreferencesUtils();
                }
            }
        }
        return instance;
    }

    private SharedPreferencesUtils() {
        share = PreferenceManager.getDefaultSharedPreferences(BaseApp.getInstance());
        editor = share.edit();
    }

    /**
     * ------- Int ---------
     */
    public void putInt(String spName, int value) {
        editor.putInt(spName, value);
        editor.commit();
    }

    public int getInt(String spName, int defaultvalue) {
        return share.getInt(spName, defaultvalue);
    }

    /**
     * ------- String ---------
     */
    public boolean putString(String spName, String value) {
        editor.putString(spName, value);
        return editor.commit();
    }

    public String getString(String spName, String defaultvalue) {
        return share.getString(spName, defaultvalue);
    }

    public String getString(String spName) {
        return share.getString(spName, "");
    }


    /**
     * ------- boolean ---------
     */
    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return share.getBoolean(key, defValue);
    }

    /**
     * ------- float ---------
     */
    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public float getFloat(String key, float defValue) {
        return share.getFloat(key, defValue);
    }


    /**
     * ------- long ---------
     */
    public void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLong(String key, long defValue) {
        return share.getLong(key, defValue);
    }

    /**
     * 清空SP里所有数据 谨慎调用
     */
    public void clear() {
        editor.clear();//清空
        editor.commit();//提交
    }

    /**
     * 删除SP里指定key对应的数据项
     *
     * @param key
     */
    public boolean remove(String key) {
        editor.remove(key);//删除掉指定的值
        return editor.commit();//提交
    }

    /**
     * 查看sp文件里面是否存在此 key
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return share.contains(key);
    }


    public void deleteByPrefixKey(String prefixKey){
        try{
            Map<String,?> map = share.getAll();
            if(null == map || map.isEmpty()){
                return;
            }

            for (String key : map.keySet()) {
                if(key.contains(prefixKey)){
                    editor.remove(key);
                    editor.apply();
                }
            }
        }catch (Exception e){
            LogUtils.e("=========",e.getMessage());
        }
    }
}
