package com.auto.di.guan;


import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.auto.di.guan.basemodel.model.respone.LoginRespone;
import com.auto.di.guan.db.DBManager;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.User;
import com.auto.di.guan.db.UsrtLevel;
import com.auto.di.guan.db.greendao.DaoMaster;
import com.auto.di.guan.db.greendao.DaoSession;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.utils.CrashHandler;
import com.auto.di.guan.utils.FloatWindowUtil;
import com.auto.di.guan.utils.GsonUtil;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.ShareUtil;
import com.auto.di.guan.utils.SharedPreferencesUtils;
import com.facebook.stetho.Stetho;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/6/28.
 */

public class BaseApp extends Application {

    public static String TAG = "BaseApp";

    public static String DB_NAME = "test.db";
    public static String PERJECT_DIR = "/1111111111";
    public static DaoSession daoSession;
    private static DaoMaster daoMaster;

    private static BaseApp instance;
    public static List<UsrtLevel>usrtLevels = new ArrayList<>();

    public static User user;

    public static boolean groupIsStart;

    public SerialPortFinder mSerialPortFinder;
    private SerialPort mSerialPort = null;

    private static Context mContext=null;//上下文



    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;
        mContext = getApplicationContext();
        Stetho.initializeWithDefaults(this);
        mSerialPortFinder = new SerialPortFinder();
        LogUtils.setFilterLevel(LogUtils.ALL);
        CrashHandler.getInstance().init(getApplicationContext());

        FloatWindowUtil.getInstance().initFloatWindow(this);
        getDaoSession(this);
    }

    public static BaseApp getInstance() {
        return instance;
    }

    public static Context getContext() {
        return mContext;
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
                     * @see android.content.ContextWrapper#openOrCreateDatabase
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


    /**
     * 判断网络是否连接
     */
    public static boolean isConnectNomarl() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (null == connectivityManager) {
                return false;
            }

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                        return true;
                    }
                }
            }else{
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        return true;
                    }
                } catch (Exception e) {
                    LogUtils.e(TAG,e.getMessage());
                    return false;
                }
            }

            //根据Android版本判断网络是否可用：6.0以后系统提供API可用，6.0之前使用ping命令
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if(null !=networkCapabilities){
                    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
                }
            } else {
                Process ipProcess = null;
                try {
                    Runtime runtime = Runtime.getRuntime();
                    if(null !=runtime){
                        ipProcess = runtime.exec("ping -c 3 t.wuzhenpay.com");
                    }

                    if(null !=ipProcess){
                        int exitValue = ipProcess.waitFor();
                        LogUtils.i(TAG, "Process:" + exitValue);
                        return (exitValue == 0);
                    }
                } catch (Exception e) {
                    LogUtils.e(TAG, e.getMessage());
                    return false;
                }finally {
                    if(null !=ipProcess){
                        ipProcess.destroy();
                    }
                }
            }

            InputStream stream = null;
            try {
                URL url = new URL("https://www.baidu.com");
                stream = url.openStream();
                if (null != stream) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }finally {
                if(null !=stream){
                    stream.close();
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 用户登录的token信息
     */
    private static LoginRespone loginRespone;

    public static LoginRespone getLoginRespone() {
        if (loginRespone == null) {
            String info = SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.SAVE_TOKEN_INFO);
            if (!TextUtils.isEmpty(info)) {
                loginRespone = GsonUtil.fromJson(info, LoginRespone.class);
            }
        }
        return loginRespone;
    }
}
