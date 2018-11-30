package com.auto.di.guan.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.auto.di.guan.R;
import com.auto.di.guan.utils.StringUtils;


/**
 * 满足两个按钮的dialog
 */
public class CustomDialog {

    private Context context;
//    private AlertDialog.Builder builder;
    private Dialog dialog;
    private TextView content, title;
    private Button okBtn;
    private Button cancelBtn;
    private String tip;
    private String ok;
    private String cancel;

    private OnClickListener mOkOnClick;
    private OnClickListener mCancelOnClick;
    public CustomDialog(Context context, String tip) {
        this.context = context;
        this.tip=tip;
        cancel = "取消";
        initView();
    }

    /**
     *
     * @param context
     * @param tip  提示语
     * @param ok
     * @param cancel
     */
    public CustomDialog(Context context, String tip, String ok, String cancel) {
        this.context = context;
        this.tip=tip;
        this.ok = ok;
        this.cancel = cancel;
        initView();
    }

    public CustomDialog(Context context, String tip, String ok) {
        this.context = context;
        this.tip=tip;
        this.ok = ok;
        initView();
    }

    private void initView() {
        dialog = new Dialog(context, R.style.EasyDialogTheme);
        View view = View.inflate(context, R.layout.dialog_two_button, null);
        content = (TextView) view.findViewById(R.id.content);
        okBtn = (Button) view.findViewById(R.id.okBtn);
        cancelBtn = (Button) view.findViewById(R.id.cancelBtn);
        title = (TextView) view.findViewById(R.id.tit);
        title.setVisibility(View.GONE);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOkOnClick.onClick(v);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCancelOnClick.onClick(v);
            }
        });
        initUI();

        dialog.setContentView(view);
    }

    public void initUI() {
        if(!StringUtils.isEmpty(tip)) {
            content.setText(tip);
        }
        if(!StringUtils.isEmpty(ok)) {
            okBtn.setText(ok);
        }
        if(!StringUtils.isEmpty(cancel)) {
            cancelBtn.setText(cancel);
        }else {
            cancelBtn.setVisibility(View.GONE);
        }
    }


    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void setTitle(String text) {
        if(!TextUtils.isEmpty(text)){
            title.setText(text);
            title.setVisibility(View.VISIBLE);
        }
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
    }
    public void setCancelable(boolean flag) {
        dialog.setCancelable(flag);
    }

    public void setOkOnClick(OnClickListener okOnClick){
        this.mOkOnClick = okOnClick;
    }

    public void setCancelOnClick(OnClickListener cancelOnClick){
        this.mCancelOnClick = cancelOnClick;
    }

    public interface OnClickListener{
        void onClick(View v);
    }

}
