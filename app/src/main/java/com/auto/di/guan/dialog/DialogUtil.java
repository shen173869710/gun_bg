package com.auto.di.guan.dialog;

import android.content.Context;

public class DialogUtil {


    /**
     *   暂停计时
     */
    public static void showStopCount(Context context, OnDialogClick onDialogClick) {
        DialogContent content = new DialogContent();
        content.desc = "是否进行暂停计时操作?";
        content.cancle = "取消";
        content.ok = "确认暂停";
        OptionDialog.show(context,content,onDialogClick);
    }

    /**
     *   开始计时
     */
    public static void showStartCount(Context context, OnDialogClick onDialogClick) {
        DialogContent content = new DialogContent();
        content.desc = "是否进行开始计时操作?";
        content.cancle = "取消";
        content.ok = "确认开始";
        OptionDialog.show(context,content,onDialogClick);
    }
}
