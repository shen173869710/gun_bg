package com.auto.di.guan.entity;

import android.util.Log;
import android.widget.Switch;

import com.auto.di.guan.R;
import com.auto.di.guan.db.ControlInfo;

/**
 * Created by Administrator on 2017/7/9.
 */

public class ImageStatus {


    public static int STATUS_EMPTY = 0;
    public static int STATUS_ADD = 1;
    public static int STATUS_BIND = 2;
    public static int STATUS_WORK = 3;

    public static int STATUS_ERROE_1 = 4;
    public static int STATUS_ERROE_2 = 5;
    public static int STATUS_ERROE_3 = 6;
    public static int STATUS_ERROE_4 = 7;


    public static String [] GROUP_ICON = {

    };

    public static int PIPE_TYPE_1 = 0;
    public static int PIPE_TYPE_2 = 1;
    public static int PIPE_TYPE_MAIN_90 = 2;
    public static int PIPE_TYPE_MAIN_180 = 3;
    public static int PIPE_TYPE_MAIN_270 = 4;
    public static int PIPE_TYPE_MAIN_360 = 5;

    public static int PIPE_TYPE_CHILD_90 = 6;
    public static int PIPE_TYPE_CHILD_180 = 7;
    public static int PIPE_TYPE_CHILD_270 = 8;
    public static int PIPE_TYPE_CHILD_360 = 9;


    public static String[] IMAGE_TYPE = {
        "单控阀控器","双控阀控器","主管道(90)","主管道(180)","主管道(270)","分支管道(360)","分支管道(90)","分支管道(180)","分支管道(270)","分支管道(360)"
    };


    public static int []PIPE_1 = {
            R.mipmap.pipe_1_0,
            R.mipmap.pipe_1_1,
            R.mipmap.pipe_1_2,
            R.mipmap.pipe_1_3,
    };
    public static int []PIPE_2 = {
            R.mipmap.pipe_2_0,
            R.mipmap.pipe_2_1,
            R.mipmap.pipe_2_2,
            R.mipmap.pipe_2_3,
    };

    public static int []PIPE_3 = {
            R.mipmap.pipe_3_0,
            R.mipmap.pipe_3_1,
            R.mipmap.pipe_3_2,
            R.mipmap.pipe_3_3,
    };

    public static int []PIPE_4 = {
            R.mipmap.pipe_4_0,
            R.mipmap.pipe_4_1,
            R.mipmap.pipe_4_2,
            R.mipmap.pipe_4_3,
    };

    public static int []PIPES_1 = {
            R.mipmap.pipes_1_0,
            R.mipmap.pipes_1_1,
            R.mipmap.pipes_1_2,
            R.mipmap.pipes_1_3,
    };
    public static int []PIPES_2 = {
            R.mipmap.pipes_2_0,
            R.mipmap.pipes_2_1,
            R.mipmap.pipes_2_2,
            R.mipmap.pipes_2_3,
    };

    public static int []PIPES_3 = {
            R.mipmap.pipes_3_0,
            R.mipmap.pipes_3_1,
            R.mipmap.pipes_3_2,
            R.mipmap.pipes_3_3,
    };

    public static int []PIPES_4 = {
            R.mipmap.pipes_4_0,
            R.mipmap.pipes_4_1,
            R.mipmap.pipes_4_2,
            R.mipmap.pipes_4_3,
    };


    /**
     *   主管道
     */
    public static int []MAIN_PIPES_90_N = {
            R.mipmap.pipe_90_n_0,
            R.mipmap.pipe_90_n_1,
            R.mipmap.pipe_90_n_2,
            R.mipmap.pipe_90_n_3,
    };

    public static int []MAIN_PIPES_90_P = {
            R.mipmap.pipe_90_p_0,
            R.mipmap.pipe_90_p_1,
            R.mipmap.pipe_90_p_2,
            R.mipmap.pipe_90_p_3,
    };

    public static int []MAIN_PIPES_180_N = {
            R.mipmap.pipe_180_n_0,
            R.mipmap.pipe_180_n_1,

    };
    public static int []MAIN_PIPES_180_P = {
            R.mipmap.pipe_180_p_0,
            R.mipmap.pipe_180_p_1,
    };

    public static int []MAIN_PIPES_270_N = {
            R.mipmap.pipe_270_n_0,
            R.mipmap.pipe_270_n_1,
            R.mipmap.pipe_270_n_2,
            R.mipmap.pipe_270_n_3,
    };

    public static int []MAIN_PIPES_270_P = {
            R.mipmap.pipe_270_p_0,
            R.mipmap.pipe_270_p_1,
            R.mipmap.pipe_270_p_2,
            R.mipmap.pipe_270_p_3,
    };
    public static int []MAIN_PIPES_360 = {
            R.mipmap.pipe_360_n,
            R.mipmap.pipe_360_p,
    };
    /**
     *   分支
     */
    public static int []PIPES_90_N = {
            R.mipmap.pipe_90_n_0,
            R.mipmap.pipe_90_n_1,
            R.mipmap.pipe_90_n_2,
            R.mipmap.pipe_90_n_3,
    };

    public static int []PIPES_90_P = {
            R.mipmap.pipe_90_p_0,
            R.mipmap.pipe_90_p_1,
            R.mipmap.pipe_90_p_2,
            R.mipmap.pipe_90_p_3,
    };

    public static int []PIPES_180_N = {
            R.mipmap.pipe_180_n_0,
            R.mipmap.pipe_180_n_1,

    };
    public static int []PIPES_180_P = {
            R.mipmap.pipe_180_p_0,
            R.mipmap.pipe_180_p_1,
    };

    public static int []PIPES_270_N = {
            R.mipmap.pipe_270_n_0,
            R.mipmap.pipe_270_n_1,
            R.mipmap.pipe_270_n_2,
            R.mipmap.pipe_270_n_3,
    };

    public static int []PIPES_270_P = {
            R.mipmap.pipe_270_p_0,
            R.mipmap.pipe_270_p_1,
            R.mipmap.pipe_270_p_2,
            R.mipmap.pipe_270_p_3,
    };
    public static int []PIPES_360 = {
            R.mipmap.pipe_360_n,
            R.mipmap.pipe_360_p,
    };

    public static int getImageId (ControlInfo info){
        int id = 0;
        switch (info.status) {
            case Entiy.CONTROL_STATUS＿CONNECT:
                id = R.mipmap.lighe_1;
            break;
//            case Entiy.CONTROL_STATUS＿2:
//                id = R.mipmap.lighe_1;
//                break;
            case Entiy.CONTROL_STATUS＿RUN:
                id = R.mipmap.lighe_2;
                break;
            case Entiy.CONTROL_STATUS＿ERROR:
                id = R.mipmap.lighe_3;
                break;
        }
        return id;
    }
}
