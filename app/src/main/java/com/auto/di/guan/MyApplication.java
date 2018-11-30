package com.auto.di.guan;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.auto.di.guan.db.DBManager;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.User;
import com.auto.di.guan.db.UsrtLevel;
import com.auto.di.guan.db.greendao.DaoMaster;
import com.auto.di.guan.db.greendao.DaoSession;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.utils.CrashHandler;
import com.auto.di.guan.utils.FloatWindowUtil;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.ShareUtil;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/6/28.
 */

public class MyApplication extends Application {

    public static String DB_NAME = "test.db";
    public static String PERJECT_DIR = "/1111111111";
    public static DaoSession daoSession;
    private static DaoMaster daoMaster;

    private static MyApplication instance;
    public static List<UsrtLevel>usrtLevels = new ArrayList<>();

    public static User user;

    public static boolean groupIsStart;

    public SerialPortFinder mSerialPortFinder;
    private SerialPort mSerialPort = null;

    public static MyApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;
        mSerialPortFinder = new SerialPortFinder();
        LogUtils.setFilterLevel(LogUtils.ALL);
        CrashHandler.getInstance().init(getApplicationContext());

        FloatWindowUtil.getInstance().initFloatWindow(this);
        getDaoSession(this);
    }

    public static String getProjectId () {
        return ShareUtil.getStringLocalValue(instance, Entiy.GROUP_NAME);
    }

    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {

            try{
                ContextWrapper wrapper = new ContextWrapper(context) {
                    /**
                     * 获得数据库路径，如果不存在，则创建对象对象
                     *
                     * @param name
                     */
                    @Override
                    public File getDatabasePath(String name) {
                        // 判断是否存在sd卡
                        boolean sdExist = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
                        if (!sdExist) {// 如果不存在,
                            Log.e("SD卡管理：", "SD卡不存在，请加载SD卡");
                            return null;
                        } else {// 如果存在
                            // 获取sd卡路径
                            String dbDir = Environment.getExternalStorageDirectory().getPath();
                            dbDir += PERJECT_DIR;// 数据库所在目录
                            String dbPath = dbDir + "/" + name;// 数据库路径
                            // 判断目录是否存在，不存在则创建该目录
                            Log.e("--------","dbDir = "+dbPath);
                            File dirFile = new File(dbDir.trim());
                            if (!dirFile.exists()){
                                dirFile.mkdirs();
                            }


                            // 数据库文件是否创建成功
                            boolean isFileCreateSuccess = false;
                            // 判断文件是否存在，不存在则创建该文件
                            File dbFile = new File(dbPath.trim());
                            if (!dbFile.exists()) {
                                try {
                                    isFileCreateSuccess = dbFile.createNewFile();// 创建文件
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else
                                isFileCreateSuccess = true;
                            // 返回数据库文件对象
                            if (isFileCreateSuccess)
                                return dbFile;
                            else
                                return super.getDatabasePath(name);
                        }
                    }

                    /**
                     * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
                     *
                     * @param name
                     * @param mode
                     * @param factory
                     */
                    @Override
                    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
                        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
                    }

                    /**
                     * Android 4.0会调用此方法获取数据库。
                     *
                     * @see android.content.ContextWrapper#openOrCreateDatabase(java.lang.String,
                     *      int,
                     *      android.database.sqlite.SQLiteDatabase.CursorFactory,
                     *      android.database.DatabaseErrorHandler)
                     * @param name
                     * @param mode
                     * @param factory
                     * @param errorHandler
                     */
                    @Override
                    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
                        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
                    }
                };
                DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(wrapper,DB_NAME,null);
                daoMaster = new DaoMaster(helper.getWritableDatabase()); //获取未加密的数据库
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return daoMaster;
    }

    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
			/* Read serial port parameters */
//            SharedPreferences sp = getSharedPreferences("android_serialport_api.sample_preferences", MODE_PRIVATE);
//            String path = sp.getString("DEVICE", "");
//            int baudrate = Integer.decode(sp.getString("BAUDRATE", "-1"));
//
//			/* Check parameters */
//            if ( (path.length() == 0) || (baudrate == -1)) {
//                throw new InvalidParameterException();
//            }

			/* Open the serial port */
//            mSerialPort = new SerialPort(new File(path), baudrate, 0);
            mSerialPort = new SerialPort(new File("/dev/ttyS1"), 115200, 0);
        }

        return mSerialPort;
    }

    public void closeSerialPort() {
         if (mSerialPort != null) {
                mSerialPort.close();
            mSerialPort = null;
        }
    }

    /**
     * 获取DaoSession对象
     *
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }


    public void exit() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 200);
    }


    public static boolean isGroupStart() {
        List<GroupInfo> groupInfos = DBManager.getInstance(getInstance()).queryGrouplList();
        if (groupInfos != null && groupInfos.size() > 0) {
            int size = groupInfos.size();
            for (int i = 0; i < size; i++) {
                if (groupInfos.get(i).getGroupStatus() > 0) {
                    Toast.makeText(getInstance(), getInstance().getString(R.string.group_open_error),Toast.LENGTH_LONG).show();
                    return true;
                }
            }
        }
        return false;
    }

    public static GroupInfo isGroupInfoStart() {
        List<GroupInfo> groupInfos = DBManager.getInstance(getInstance()).queryGrouplList();
        if (groupInfos != null && groupInfos.size() > 0) {
            int size = groupInfos.size();
            for (int i = 0; i < size; i++) {
                if (groupInfos.get(i).getGroupStatus() > 0) {
                    Toast.makeText(getInstance(), getInstance().getString(R.string.group_open_error),Toast.LENGTH_LONG).show();
                    return groupInfos.get(i);
                }
            }
        }
        return null;
    }
}
