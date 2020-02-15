package com.auto.di.guan.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.auto.di.guan.R;

public class InputDialog extends Dialog {

    private static InputDialog dialog;
    private Context mContext;
    private String value;

    public InputDialog(Context context, DialogContent content, OnDialogClick onDialogClick) {
        super(context, R.style.MyDialog);
        this.mContext = context;
        init(content,onDialogClick);
    }

    public InputDialog(Context context, int theme) {
        super(context, R.style.MyDialog);
    }

    private void init(DialogContent content, OnDialogClick onDialogClick) {
        setContentView(R.layout.dialog_input);
         setCancelable(true);
        TextView input_title = findViewById(R.id.input_title);
        input_title.setText(content.desc);
        EditText input_edittext = findViewById(R.id.input_edittext);

        TextView dialog_ok = findViewById(R.id.input_ok);
        dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value = input_edittext.getText().toString().trim();
                if(!TextUtils.isEmpty(value)) {
                    onDialogClick.onDialogOkClick(value);
                }
            }
        });
    }

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

            if (dialog != null && dialog.isShowing()) {
                dismiss(context);
                dialog = null;
            }

            dialog = new InputDialog(context, content,onDialogClick);
            dialog.show();
        }catch (Exception e){
        }
    }

    public static void dismiss(Context context) {
        try {
            if(null ==context){
                return;
            }

            if (context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    dialog = null;
                    return;
                }
            }
            if (dialog != null && dialog.isShowing()) {
                Context loadContext = dialog.getContext();
                if (loadContext instanceof Activity) {
                    if (((Activity) loadContext).isFinishing() || ((Activity) context).isDestroyed()) {
                        dialog = null;
                        return;
                    }
                }
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {
        } finally {
            dialog = null;
        }
    }
}