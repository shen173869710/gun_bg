package com.auto.di.guan;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import com.auto.di.guan.basemodel.model.respone.LoginRespone;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.User;
import com.auto.di.guan.db.greendao.DaoMaster;
import com.auto.di.guan.db.greendao.DaoSession;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.db.sql.UserSql;
import com.auto.di.guan.db.update.MySQLiteOpenHelper;
import com.auto.di.guan.utils.FloatWindowUtil;
import com.auto.di.guan.utils.GsonUtil;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.SharedPreferencesUtils;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.log.CustomLogger;
import com.facebook.stetho.Stetho;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/6/28.
 */

public class BaseApp extends Application {

    public static String TAG = "BaseApp";
    public static String DB_NAME = "guan.db";
    private static MySQLiteOpenHelper mSQLiteOpenHelper;
    public static DaoSession mDaoWriteSession;
    private static DaoSession mDaoReadSession;
    private static BaseApp instance;

    public static User getUser() {
        if (user == null) {
            user = UserSql.queryUserInfo();
        }
        return user;
    }

    public static void setUser(User user) {
        BaseApp.user = user;
    }

    private static User user;
    public static boolean groupIsStart;

    public SerialPortFinder mSerialPortFinder;
    private SerialPort mSerialPort = null;

    private static Context mContext=null;//上下文
    private JobManager jobManager;
    public JobManager getJobManager() {
        return jobManager;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;
        mContext = getApplicationContext();
        Stetho.initializeWithDefaults(this);
        mSerialPortFinder = new SerialPortFinder();
        LogUtils.setFilterLevel(LogUtils.ALL);
//        CrashHandler.getInstance().init(getApplicationContext());

        FloatWindowUtil.getInstance().initFloatWindow(this);

        configureJobManager();

    }

    public static BaseApp getInstance() {
        return instance;
    }

    public static Context getContext() {
        return mContext;
    }


    public static DaoSession getDaoWriteSession(){
        if(null == mSQLiteOpenHelper){
            initSQLiteOpenHelper();
        }
        if(null == mDaoWriteSession){
            synchronized (DaoSession.class){
                if(null == mDaoWriteSession){
                    SQLiteDatabase sqLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
                    if(null !=sqLiteDatabase && sqLiteDatabase.isOpen()){
                        mDaoWriteSession = new DaoMaster(sqLiteDatabase).newSession(IdentityScopeType.None);
                    }else{
                        mDaoWriteSession = null;
                        return null;
                    }
                }
            }
        }
        return  mDaoWriteSession;
    }

    /**
     * 磁盘满了依然可读,
     * newSession 不能使用默认级别,否则写进去读取有问题
     *
     * @return
     */
    public static DaoSession getDaoReadSession(){
        if(null == mSQLiteOpenHelper){
            initSQLiteOpenHelper();
        }

        if(null == mDaoReadSession){
            synchronized (DaoSession.class){
                if(null == mDaoReadSession){
                    SQLiteDatabase sqLiteDatabase = mSQLiteOpenHelper.getReadableDatabase();
                    //sqLiteDatabase.setLocale(Locale.getDefault()); //设置数据库使用的语言
                    if(null!=sqLiteDatabase && sqLiteDatabase.isOpen()){
                        mDaoReadSession = new DaoMaster(sqLiteDatabase).newSession(IdentityScopeType.None);
                    }else{
                        mDaoReadSession = null;
                    }
                }
            }
        }
        return  mDaoReadSession;
    }


    public static MySQLiteOpenHelper initSQLiteOpenHelper(){
        try{
            if(null == mSQLiteOpenHelper){
                synchronized (MySQLiteOpenHelper.class){
                    if(null ==mSQLiteOpenHelper){
                        mSQLiteOpenHelper = new MySQLiteOpenHelper(mContext, DB_NAME, null);//建库
                    }
                }
            }
            return mSQLiteOpenHelper;
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
            return null;
        }
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

    public void exit() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 200);
    }


    public static boolean isGroupStart() {
        List<GroupInfo> groupInfos = GroupInfoSql.queryGrouplList();
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
        List<GroupInfo> groupInfos = GroupInfoSql.queryGrouplList();
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


    public static String getProjectId() {
        return user.getProjectId();
    }


    /**
     * 配置JobMananger
     */
    private void configureJobManager() {
        Configuration configuration = new Configuration.Builder(this)
                //日志设置，便于用户查看任务队列的工作信息
                .customLogger(new CustomLogger() {
                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        LogUtils.e(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        LogUtils.e(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(String text, Object... args) {
                        LogUtils.e(TAG, String.format(text, args));
                    }

                    @Override
                    public void v(String text, Object... args) {
                        LogUtils.e(TAG, String.format(text, args));
                    }
                })
                //最少开启的线程
                .minConsumerCount(1)
                //最多开启的线程
                .maxConsumerCount(1)
                //一个Thread设置多3个任务
                .loadFactor(1)
                //设置线程在没有任务的情况下保持存活的时长，以秒为单位
                .consumerKeepAlive(120)
                .build();
        jobManager = new JobManager(configuration);
    }

}
