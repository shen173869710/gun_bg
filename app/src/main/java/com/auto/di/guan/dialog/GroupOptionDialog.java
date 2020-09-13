package com.auto.di.guan.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import com.auto.di.guan.R;
import com.auto.di.guan.utils.NoFastClickUtils;
import com.auto.di.guan.utils.PollingUtils;


public class GroupOptionDialog extends Dialog {

	public Button option_open;
	public Button option_close;
	public Button option_auto;
	public TextView option_title;

	public GroupOptionDialog(Context context) {
		super(context, R.style.UpdateDialog);
		setCustomView();
	}

	public GroupOptionDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, R.style.UpdateDialog);
		this.setCancelable(cancelable);
		this.setOnCancelListener(cancelListener);
		setCustomView();
	}

	public GroupOptionDialog(Context context, int theme) {
		super(context, R.style.UpdateDialog);
		setCustomView();
	}

	private void setCustomView() {
		View mView = LayoutInflater.from(getContext()).inflate(R.layout.group_option_dialog, null);
		option_open = (Button) mView.findViewById(R.id.option_open);
		option_close = (Button) mView.findViewById(R.id.option_close);
		option_auto = (Button) mView.findViewById(R.id.option_auto);
		option_title = (TextView) mView.findViewById(R.id.option_title);
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



	public static void ShowDialog(final Activity context,String title,final ItemClick listener) {
		final GroupOptionDialog dialog = new GroupOptionDialog(context);
		dialog.option_title.setText(title);
		dialog.option_open.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				listener.onItemClick(1);
				dialog.dismiss();
			}
		});
		dialog.option_close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				listener.onItemClick(2);
				dialog.dismiss();
			}
		});


		if (PollingUtils.isIsStart()) {
			dialog.option_auto.setText("关闭自动查询");
		}else {
			dialog.option_auto.setText("开启自动查询");
		}

		dialog.option_auto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				listener.onItemClick(3);
				dialog.dismiss();
			}
		});

		LayoutParams lay = dialog.getWindow().getAttributes();
		DisplayMetrics dm = new DisplayMetrics();// 获取屏幕分辨率
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);//
		Rect rect = new Rect();
		View view = context.getWindow().getDecorView();
		view.getWindowVisibleDisplayFrame(rect);
		lay.width = dm.widthPixels * 9 / 10;
		dialog.show();
	}

	public interface ItemClick {
		public void  onItemClick(int index);
	}
}
