package com.auto.di.guan;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;


import com.auto.di.guan.utils.HttpUtil;
import com.auto.di.guan.utils.Task;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 异步网络请求
 * @author Administrator
 *
 */
public class RequestAsyncTask extends AsyncTask<String,Integer,Task>
{
	private IUrlRequestCallBack callback;
	private Task task;

	private Activity context;


	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;


	public RequestAsyncTask(Activity activity,Task task, IUrlRequestCallBack callback) {
		this.task = task;
		this.callback = callback;
		this.context = activity;
	}
	
	@Override
	protected void onPreExecute() {
		Log.e("------","onPreExecute");
		if (context != null && context instanceof IBaseActivity) {
			Log.e("------","showDialog");
			context.showDialog(1);
		}

//		try {
//			mSerialPort = MyApplication.getInstance().getSerialPort();
//			mOutputStream = mSerialPort.getOutputStream();
//			mInputStream = mSerialPort.getInputStream();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}
	
	/**
	 * 后台线程
	 */
	@Override
	protected Task doInBackground(String... params) {
		if (task == null) {
			return null;
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		/**解析Json**/
		task.setmTaskObject("");
		return task;
	}
	/**
	 * 前台线程
	 */
	@Override
	protected void onPostExecute(Task result)
	{
		if (context != null && context instanceof IBaseActivity) {
			context.dismissDialog(1);
		}
		if (task == null) {
			return;
		}
		HttpUtil.getInstance().taskMap.remove(result.getId());

		if(result.getmTaskObject() == null){
			callback.urlRequestException(result);
		}else{
			callback.urlRequestEnd(result);
		}
	}
	
	
}
