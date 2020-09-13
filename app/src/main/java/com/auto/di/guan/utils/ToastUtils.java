package com.auto.di.guan.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.auto.di.guan.BaseApp;

import me.drakeet.support.toast.BadTokenListener;
import me.drakeet.support.toast.ToastCompat;

/**
 * Created by long on 2016/6/6.
 * 避免同样的信息多次触发重复弹出的问题
 */
public class ToastUtils {

    private static String oldMsg;
    protected static ToastCompat toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    private ToastUtils() {
        throw new RuntimeException("ToastUtils cannot be initialized!");
    }

    public static void showToast(String s) {
        try{
            Context context = BaseApp.getContext();
            if(null ==context){
                return;
            }

            if(null ==toast){
                synchronized (ToastCompat.class){
                    if(null ==toast){
                        toast = ToastCompat.makeText(context, s, Toast.LENGTH_SHORT)
                                .setBadTokenListener(new BadTokenListener() {
                                    @Override
                                    public void onBadTokenCaught(@NonNull Toast toast) {
                                        LogUtils.e( "failed toast", "hello");
                                    }
                                });
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                        oneTime = System.currentTimeMillis();
                    }
                }
            }

            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s+"");
                toast.show();
            }
            oneTime = twoTime;
        }catch (WindowManager.BadTokenException e) {

        }catch (Exception e){
        }
    }


    public static void showLongToast(String s) {
        try{
            Context context = BaseApp.getContext();
            if(null ==context){
                return;
            }

            if(null ==toast){
                synchronized (ToastCompat.class){
                    if(null ==toast){
                        toast = ToastCompat.makeText(context, s, Toast.LENGTH_LONG)
                                .setBadTokenListener(new BadTokenListener() {
                                    @Override
                                    public void onBadTokenCaught(@NonNull Toast toast) {
                                        LogUtils.e( "failed toast", "hello");
                                    }
                                });
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                        oneTime = System.currentTimeMillis();
                    }
                }
            }

            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_LONG) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s+"");
                toast.show();
            }
            oneTime = twoTime;
        }catch (WindowManager.BadTokenException e) {
        }catch (Exception e){
        }
    }

    public static void showLongToast(int resId) {
        Context context = BaseApp.getContext();
        if(null ==context){
            return;
        }
        showLongToast(context.getString(resId));
    }

    public static void showToast(int resId) {
        Context context = BaseApp.getContext();
        if(null ==context){
            return;
        }

        showToast(context.getString(resId));
    }


}
