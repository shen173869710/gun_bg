package com.auto.di.guan.dialog;

import android.text.Spanned;

/**
 * Created by chenzhaolin on 2019/7/18.
 */

public class DialogContent {
    /**
     *  顶部的logo
     */
    public int resId;
    /**
     *  中路的描述
     */
    public String desc;
    public String cancle;
    public String ok;
    public Spanned spanned;

    /**
     *  是否显示OK
     */
    public boolean hideOk;
    /**
     *  是否显示取消
     */
    public boolean hideCancle;
    /**
     *  对话框弹出后点击或按返回键不消失;
     *   false 不可以消失
     *   true  可以
     */
    public boolean cancelable;
    public int maxLength;

    /**
     *  dialog弹出后会点击屏幕，false dialog不消失；点击物理返回键dialog消失
     *                      true
     */
    public boolean touchOutside;


    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCancle() {
        return cancle;
    }

    public void setCancle(String cancle) {
        this.cancle = cancle;
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    public Spanned getSpanned() {
        return spanned;
    }

    public void setSpanned(Spanned spanned) {
        this.spanned = spanned;
    }

    public boolean isHideOk() {
        return hideOk;
    }

    public void setHideOk(boolean hideOk) {
        this.hideOk = hideOk;
    }

    public boolean isHideCancle() {
        return hideCancle;
    }

    public void setHideCancle(boolean hideCancle) {
        this.hideCancle = hideCancle;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }


}
