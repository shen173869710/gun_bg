package com.auto.di.guan.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.auto.di.guan.R;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.NoFastClickUtils;


public class OptionDialog extends Dialog {

    private static OptionDialog optionDialog;

    TextView cancle;
    TextView desc;
    TextView ok;
    TextView icon;

    public OptionDialog(Context context, DialogContent content, OnDialogClick onDialogClick) {
        super(context, R.style.MyDialog);
        init(context, content,onDialogClick);
    }

    private void init(Context context, DialogContent content, OnDialogClick onDialogClick) {
        setContentView(R.layout.dialog_option);
        setCanceledOnTouchOutside(content.touchOutside);

        cancle = findViewById(R.id.dialog_cancle);
        desc = findViewById(R.id.dialog_desc);
        ok = findViewById(R.id.dialog_ok);
        icon = findViewById(R.id.dialog_icon);


        if (!TextUtils.isEmpty(content.desc)) {
            desc.setText(content.desc);
        }
        if (!TextUtils.isEmpty(content.cancle)) {
            cancle.setText(content.cancle);
        }

        if (content.hideOk) {
            ok.setVisibility(View.GONE);
        }else {
            ok.setVisibility(View.VISIBLE);
        }

        if (content.hideCancle) {
            cancle.setVisibility(View.GONE);
        }else {
            cancle.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(content.ok)) {
            ok.setText(content.ok);
        }

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NoFastClickUtils.isFastClick()){
                    return;
                }

                if (null != onDialogClick) {
                    OptionDialog.dismiss(context);
                    onDialogClick.onDialogCloseClick(null);
                }
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NoFastClickUtils.isFastClick()){
                    return;
                }
                if (null != onDialogClick) {
                    OptionDialog.dismiss(context);
                    onDialogClick.onDialogOkClick(null);
                }
            }
        });
    }


    /**
     * @param context
     * @param content
     * @param onDialogClick
     */
    public static void show(Context context, DialogContent content,OnDialogClick onDialogClick) {
        try{
            if(null ==context){
                return;
            }
            if (context instanceof Activity) {
                if (((Activity) context).isFinishing() || ((Activity) context).isDestroyed()) {
                    return;
                }
            }
            if (onDialogClick == null) {
                return;
            }
            if (optionDialog != null && optionDialog.isShowing()) {
                optionDialog.dismiss();
                optionDialog = null;
            }
            optionDialog = new OptionDialog(context, content,onDialogClick);
            optionDialog.show();
        }catch (Exception e){
            LogUtils.e("========",e.getMessage());
        }
    }


    public static void dismiss(Context context) {
        try {
            if(null ==context){
                return;
            }

            if (context instanceof Activity) {
                if (((Activity) context).isFinishing() || ((Activity) context).isDestroyed()) {
                    optionDialog = null;
                    return;
                }
            }
            if (optionDialog != null && optionDialog.isShowing()) {
                Context loadContext = optionDialog.getContext();
                if (loadContext instanceof Activity) {
                    if (((Activity) loadContext).isFinishing() || ((Activity) context).isDestroyed()) {
                        optionDialog = null;
                        return;
                    }
                }
                optionDialog.dismiss();
                optionDialog = null;
            }
        } catch (Exception e) {
        } finally {
            optionDialog = null;
        }
    }


}