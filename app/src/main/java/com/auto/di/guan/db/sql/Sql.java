package com.auto.di.guan.db.sql;

import com.auto.di.guan.BaseApp;
import com.auto.di.guan.db.greendao.DaoSession;
import com.auto.di.guan.utils.LogUtils;

import org.greenrobot.greendao.AbstractDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhaolin on 2019/7/15.
 *   基础查询类
 */

public class Sql implements ISql{
    private static String TAG ="Sql";

    private static Sql sql;
    private Sql(){}

    public static synchronized Sql getInstance(){
        // 返回Sensor唯一实例
        if(null == sql) {
            synchronized (Sql.class){
                if(null ==sql){
                    sql = new Sql();
                }
            }
        }
        return sql;
    }


    public AbstractDao<?, ?> getWriteDao(Class<? extends Object> entityClass) {
        try {
            DaoSession daoSession = BaseApp.getInstance().getDaoWriteSession();
            if(null !=daoSession){
                return daoSession.getDao(entityClass);
            }
            return null;
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
            return null;
        }
    }

    public AbstractDao<?, ?> getReadDao(Class<? extends Object> entityClass) {
        try{
            DaoSession daoSession = BaseApp.getInstance().getDaoWriteSession();
            if(null !=daoSession){
                return daoSession.getDao(entityClass);
            }
            return null;
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
            return null;
        }
    }

    @Override
    public void insert(Object object) {
        try {
            Class cls;
            if (object instanceof List) {
                List listObject = (List) object;
                if (listObject.isEmpty()) {
                    throw new IllegalArgumentException("List Object Not Allow Empty!");
                }
                cls = listObject.get(0).getClass();
                AbstractDao dao = getWriteDao(cls);
                if(null !=dao){
                    dao.insertInTx(listObject);
                }
            } else {
                cls = object.getClass();
                AbstractDao abstractDao = getWriteDao(cls);
                if(null !=abstractDao){
                    abstractDao.insert(object);
                }
            }
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
        }
    }

    @Override
    public void insertOrUpdate(Object object) {
        try {
            Class cls;
            if (object instanceof List) {
                List listObject = (List) object;
                if (listObject.isEmpty()) {
                    throw new IllegalArgumentException("List Object Not Allow Empty!");
                }

                cls = listObject.get(0).getClass();
                AbstractDao dao = getWriteDao(cls);
                if(null !=dao){
                    dao.insertOrReplaceInTx(listObject);
                }
            } else {
                cls = object.getClass();
                AbstractDao dao = getWriteDao(cls);
                if(null !=dao){
                    dao.insertOrReplace(object);
                }
            }
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
        }
    }

    @Override
    public void update(Object object) {
        try{
            Class cls;
            if (object instanceof List) {
                List listObject = (List) object;
                if (listObject.isEmpty()) {
                    throw new IllegalArgumentException("List Object Not Allow Empty!");
                }
                cls = listObject.get(0).getClass();
                AbstractDao dao = getWriteDao(cls);
                if(null !=dao){
                    dao.updateInTx(listObject);
                }
            } else {
                cls = object.getClass();
                AbstractDao dao = getWriteDao(cls);
                if(null !=dao){
                    dao.update(object);
                }
            }
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
        }
    }

    @Override
    public void delete(Object object) {
        try{
            Class cls;
            if (object instanceof List) {
                List listObject = (List) object;
                if (listObject.isEmpty()) {
                    throw new IllegalArgumentException("List Object Not Allow Empty!");
                }
                cls = listObject.get(0).getClass();
                AbstractDao dao = getWriteDao(cls);
                if(null !=dao){
                    dao.deleteInTx(listObject);
                }
            } else {
                cls = object.getClass();
                AbstractDao dao = getWriteDao(cls);
                if(null !=dao){
                    dao.delete(object);
                }
            }
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
        }
    }

    @Override
    public void deleteByLongKey(Class<?> cls, Long key) {
        try{
            AbstractDao dao = getWriteDao(cls);
            if(null !=dao){
                dao.deleteByKey(key);
            }
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
        }
    }

    @Override
    public void deleteByStringKey(Class<?> cls, String key) {
        try{
            AbstractDao dao = getWriteDao(cls);
            if(null !=dao){
                dao.deleteByKey(key);
            }
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
        }
    }

    @Override
    public void deleteAll(Class<?> cls) {
        try{
            AbstractDao dao = getWriteDao(cls);
            if(null !=dao){
                dao.deleteAll();
            }
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
        }
    }

    @Override
    public <T> T quaryByLongKey(Class<T> cls, Long key) {
        try{
            AbstractDao dao = getReadDao(cls);
            if(null !=dao){
                return ((AbstractDao<T, Long>)dao).load(key);
            }
            return null;
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
            return null;
        }
    }

    @Override
    public <T> T quaryByStringKey(Class<T> cls, String key) {
        try{
            AbstractDao dao = getReadDao(cls);
            if(null !=dao){
                return ((AbstractDao<T, String>) dao).load(key);
            }
            return null;
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
            return null;
        }
    }

    @Override
    public <T> List<T> quary(Class<T> cls, String where, String... selectionArg) {
        try{
            AbstractDao dao = getReadDao(cls);
            if(null !=dao){
                return ((AbstractDao<T, String>) dao).queryRaw(where, selectionArg);
            }
            return new ArrayList<>();
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
            return new ArrayList<>();
        }

    }

    @Override
    public <T> List<T> quaryAll(Class<T> cls) {
        try {
            AbstractDao dao = getReadDao(cls);
            if(null !=dao){
                return ((AbstractDao<T, String>) dao).loadAll();
            }

            return new ArrayList<>();
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
            return new ArrayList<>();
        }
    }
}
