package com.auto.di.guan.utils;


import android.app.Activity;

import com.auto.di.guan.IUrlRequestCallBack;
import com.auto.di.guan.RequestAsyncTask;

import java.util.HashMap;


/***
 * 网络请求工具类
  *
 */
public class HttpUtil {
	
	public HashMap<Integer , Integer> taskMap = new HashMap<Integer, Integer>();
	private static HttpUtil instance;
	private static final byte[] sLock = new byte[0];
//	private int currentVersion=android.os.Build.VERSION.SDK_INT;
	public static HttpUtil getInstance(){
		if (instance == null) {
			synchronized (sLock) {
				if (instance == null) {
			instance = new HttpUtil();
				}
			}
		}
		return instance;
	}

	/**
	 * @param @param context   请求数据的对象
	 * @param @param url       发送请求的地址
	 * @param @param params    发送请求的参数
	 * @param @param callback  返回数据的回调方法
	 * @return void
	 * @Title: doPost以Map参数的形式提交post数据到服务器
	 */
	public void doHttpTask(Activity activity, final Task task, final IUrlRequestCallBack callback) {
		if (taskMap.containsKey(task.getId())) {
			return;
		} else {
			taskMap.put(task.getId(), task.getId());
			new RequestAsyncTask(activity, task, callback).execute();
		}
	}
}
