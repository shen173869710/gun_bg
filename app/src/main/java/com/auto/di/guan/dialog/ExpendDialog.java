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
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.auto.di.guan.R;
import com.auto.di.guan.adapter.ImageExpandableListViewaAdapter;
import com.auto.di.guan.entity.ImageInfo;
import com.auto.di.guan.utils.NoFastClickUtils;

import java.util.List;


public class ExpendDialog extends Dialog {

	public Button main_expand_cancle;
	public Button main_expand_sure;
	public ExpandableListView expandableListView;
	public TextView main_expand_title;

	public void setGroupPosition(int groupPosition) {
		this.groupPosition = groupPosition;
	}

	public void setChildPosition(int childPosition) {
		this.childPosition = childPosition;
	}

	public int groupPosition;
	public int childPosition;
	public  List<ImageInfo>imageInfos;
	private Context context;

	public ExpendDialog(Context context) {
		super(context, R.style.UpdateDialog);
		setCustomView();
	}

	public ExpendDialog(Context context, List<ImageInfo>imageInfos) {
		super(context, R.style.UpdateDialog);
		this.imageInfos = imageInfos;
		this.context = context;
		setCustomView();
	}

	public ExpendDialog(Context context, int theme) {
		super(context, R.style.UpdateDialog);
		setCustomView();
	}

	private void setCustomView() {
		View mView = LayoutInflater.from(getContext()).inflate(R.layout.main_expend_dialog, null);
		main_expand_cancle = (Button) mView.findViewById(R.id.main_expand_cancle);
		main_expand_sure = (Button) mView.findViewById(R.id.main_expand_sure);
		expandableListView = (ExpandableListView) mView.findViewById(R.id.main_expand_expand);
		main_expand_title = (TextView) mView.findViewById(R.id.main_expand_title);
		mView.setBackgroundResource(R.drawable.bg_d_rect_border);
		ImageExpandableListViewaAdapter adapter = new ImageExpandableListViewaAdapter(context,imageInfos);
		expandableListView.setAdapter(adapter);
		expandableListView.setGroupIndicator(null);
		expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				setGroupPosition(groupPosition);
				setChildPosition(childPosition);
				return false;
			}
		});
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
		main_expand_sure.setOnClickListener(listener);
	}

	public void setOnNegativeListener(View.OnClickListener listener) {
		main_expand_cancle.setOnClickListener(listener);
	}

	public static void ShowDialog(final Activity context, String title, String msg, final View.OnClickListener listener, List<ImageInfo>imageInfos) {
		final ExpendDialog dialog = new ExpendDialog(context);
		dialog.main_expand_title.setText(title);
		ImageExpandableListViewaAdapter adapter = new ImageExpandableListViewaAdapter(context,imageInfos);
		dialog.expandableListView.setAdapter(adapter);
		dialog.setOnPositiveListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				if(listener != null)
					listener.onClick(v);
				dialog.dismiss();
			}
		});

		dialog.setOnNegativeListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		LayoutParams lay = dialog.getWindow().getAttributes();
		DisplayMetrics dm = new DisplayMetrics();// 获取屏幕分辨率
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);//
		Rect rect = new Rect();
		View view = context.getWindow().getDecorView();
		view.getWindowVisibleDisplayFrame(rect);
		lay.width = dm.widthPixels * 5 / 10;
		dialog.show();
	}
}
