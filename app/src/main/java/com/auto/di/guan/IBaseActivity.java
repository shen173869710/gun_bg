package com.auto.di.guan;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

public abstract class IBaseActivity extends Activity implements IUrlRequestCallBack{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(setLayout());
		ActivityStackUtil.add(this);

		init();
		setListener();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		return ActivityHelper.createLoadingDialog(this);
	}

	/**
	 * 显示整形数据
	 * 
	 * @param msg
	 */
	protected void showToastMsg(int msg) {
		showToastMsg(getString(msg));
	}

	/**
	 * 显示整形数据
	 * 
	 * @param msg
	 */
	protected void showToastLongMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * 显示字符串类型数据
	 * 
	 * @param msg
	 */
	protected void showToastMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 设置layout
	 * 
	 * @return
	 */
	protected abstract int setLayout();

	/**
	 * 初始化
	 */
	protected abstract void init();

	/**
	 * 设置监听
	 */
	protected abstract void setListener();

}
