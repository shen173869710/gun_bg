package com.auto.di.guan.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.auto.di.guan.R;

public class CustomShowDialog extends Dialog {
	
	private Button mPositiveBut;//确定
	private Button mNegativeBut;//取消
	private static TextView dialog_hint_message_tv; 
	private TextView dailog_title;
	private static Context context;
	
	
	public CustomShowDialog(Context context) {
		super(context, R.style.UpdateDialog);
		this.context = context;
		setCustomView();
	}

	public CustomShowDialog(Context context, OnCancelListener cancelListener) {
		super(context, R.style.UpdateDialog);
		this.setOnCancelListener(cancelListener);
		this.context = context;
		setCustomView();
	}

	public CustomShowDialog(Context context, int theme) {
		super(context, R.style.UpdateDialog);
		this.context = context;
		setCustomView();
	}

	/** 修改msg */
	public void setMsgHintText(String msg) {
		dialog_hint_message_tv.setText(Html.fromHtml(msg));
		
	}
	
	/** 修改确定按钮上的文字 */
	public void setPositiveText(String post) {
		mPositiveBut.setText(post);
	}

	/** 修改取消按钮上的文字 */
	public void setNegativeText(String nega) {
		mNegativeBut.setText(nega);
	}
	
	public void setDialogTitle(String nega) {
		dailog_title.setText(nega);
	}

	private void setCustomView() {
		View mView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog, null);
		mNegativeBut = (Button) mView.findViewById(R.id.dialog_cancle_but);
		mPositiveBut = (Button) mView.findViewById(R.id.dialog_sure_but);
		dialog_hint_message_tv = (TextView)mView.findViewById(R.id.dialog_hint_message_tv);
		dailog_title =(TextView)mView.findViewById(R.id.dailog_title);
		mView.setBackgroundResource(R.drawable.bg_d_rect_border);
		super.setContentView(mView);
	}

	@Override
	public void setCanceledOnTouchOutside(boolean cancel) {
		super.setCanceledOnTouchOutside(cancel);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
	}

	public void setOnPositiveListener(View.OnClickListener listener) {
		mPositiveBut.setOnClickListener(listener);
	}

	public void setOnNegativeListener(View.OnClickListener listener) {
		mNegativeBut.setOnClickListener(listener);
	}
	
	public static void ShowCustomDialog(final Activity context,String msg, final String url, int uptate) {
		final CustomShowDialog dialog = new CustomShowDialog(context);
		dialog.setMsgHintText(msg);
		LayoutParams lay = dialog.getWindow().getAttributes();
		DisplayMetrics dm = new DisplayMetrics();//获取屏幕分辨率
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);//
		Rect rect = new Rect();
		View view = context.getWindow().getDecorView();
		view.getWindowVisibleDisplayFrame(rect);
		lay.width = dm.widthPixels * 9 / 10;
		dialog.show();
	}
}
