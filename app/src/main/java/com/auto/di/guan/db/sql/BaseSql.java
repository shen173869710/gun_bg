package com.auto.di.guan.db.sql;



import com.auto.di.guan.BaseApp;
import com.auto.di.guan.db.greendao.DaoSession;
import com.auto.di.guan.utils.LogUtils;

import org.greenrobot.greendao.database.Database;

import java.util.List;

/**
 * Created by chenzhaolin on 2019/7/15.
 */

public class BaseSql {
    private static String TAG = "BaseSql";

    public static DaoSession getDaoWriteSession(){
        try{
            return BaseApp.getDaoWriteSession();
        }catch (Exception e){
            return null;
        }

    }

    public static DaoSession getDaoReadSession(){
        try{
            return BaseApp.getDaoReadSession();
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 批量执行sql
     *
     * @param sqls
     */
    public static void execSQLByBatch(List<String> sqls) {
        if (sqls == null || sqls.isEmpty()) {
            return;
        }
        Database db = null;
        try {
            db= getDaoWriteSession().getDatabase();
            if(null == db){
                return;
            }

            db.beginTransaction();
            for (String sql : sqls) {
                db.execSQL(sql);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtils.e(TAG,e.getMessage());
        } finally {
            if(null !=db){
                db.endTransaction();
            }
        }
    }
}
