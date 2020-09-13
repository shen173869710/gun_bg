package com.auto.di.guan.db.update;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.auto.di.guan.BaseApp;
import com.auto.di.guan.db.greendao.DaoMaster;
import com.auto.di.guan.db.greendao.DeviceInfoDao;
import com.auto.di.guan.db.greendao.GroupInfoDao;
import com.auto.di.guan.db.greendao.LevelInfoDao;
import com.auto.di.guan.db.greendao.UserActionDao;
import com.auto.di.guan.db.greendao.UserDao;
import com.auto.di.guan.utils.LogUtils;
import org.greenrobot.greendao.database.Database;
import java.io.File;

/*
 * 数据库升级帮助类
 */
public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    private String TAG="MySQLiteOpenHelper";
    private static boolean mainTmpDirSet = false;

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }


    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        LogUtils.e("MySQLiteOpenHelper","oldVersion:"+oldVersion+",newVersion="+newVersion);

        try{
            GwsMigrationHelper.migrate(db, new GwsMigrationHelper.ReCreateAllTableListener() {
                        @Override
                        public void onCreateAllTables(Database db, boolean ifNotExists) {
                            DaoMaster.createAllTables(db, ifNotExists);
                        }

                        @Override
                        public void onDropAllTables(Database db, boolean ifExists) {
                            DaoMaster.dropAllTables(db, ifExists);
                        }
                    },
                    DeviceInfoDao.class,
                    GroupInfoDao.class,
                    LevelInfoDao.class,
                    UserActionDao.class,
                    UserDao.class
            );
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
        }
    }

    @Override
    public SQLiteDatabase getReadableDatabase(){
        try{
            if(!mainTmpDirSet){
                String packageName = getPackageName(BaseApp.getContext());
                String dirPath = "/data/data/"+packageName+"/databases/main";
                String dbPath =  super.getReadableDatabase().getPath();
                if(!TextUtils.isEmpty(dbPath)){
                    int idx = dbPath.lastIndexOf("/");
                    if(idx>0){
                        dirPath = dbPath.substring(0,idx)+"/main";
                    }
                }

                File dirFile = new File(dirPath);
                if(null !=dirFile){
                    if(dirFile.exists() || dirFile.isDirectory()){
                        dirFile.delete();
                    }

                    boolean hasExistsDir = dirFile.mkdir();
                    if(hasExistsDir){
                        String sql ="PRAGMA temp_store_directory = '"+dirPath+"'";
                        super.getReadableDatabase().execSQL(sql);
                    }
                    mainTmpDirSet = true;
                }
            }
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
        }
        return super.getReadableDatabase();
    }


    /**
     * 获取包名
     * @param ctx
     * @return
     */
    public  String getPackageName(Context ctx) {
        String packageName = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            packageName = packageInfo.packageName;
        } catch (Exception e) {
        }

        if(TextUtils.isEmpty(packageName)){
            packageName ="com.auto.di.guan";
        }
        return packageName;
    }

}
