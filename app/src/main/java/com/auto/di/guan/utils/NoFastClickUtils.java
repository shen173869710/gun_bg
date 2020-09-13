package com.auto.di.guan.utils;

public class NoFastClickUtils {
    private static long lastClickTime = 0;

    public static boolean isFastClick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > 1000) {
            lastClickTime = currentTime;
            return false;
        }
        return true;
    }


    public static boolean isFastClick(int spaceTime) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > spaceTime) {
            lastClickTime = currentTime;
            return false;
        }
        return true;
    }

}

