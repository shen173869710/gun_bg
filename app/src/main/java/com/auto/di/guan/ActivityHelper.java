package com.auto.di.guan;

import android.app.Dialog;
import android.content.Context;

import com.auto.di.guan.dialog.WaitingDialog;


public class ActivityHelper {
	public static final int LOADING_ID = 1;
	// 无网络显示
	public static final int NO_NETWORK_ID = 1000001;
	
	public static Dialog createLoadingDialog(Context context)
	{
		WaitingDialog dialog = new WaitingDialog(context, R.style.dialog);
		return dialog;
	}



}
