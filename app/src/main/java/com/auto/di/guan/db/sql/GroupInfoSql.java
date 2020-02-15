package com.auto.di.guan.db.sql;

import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.greendao.DaoMaster;
import com.auto.di.guan.db.greendao.DaoSession;
import com.auto.di.guan.db.greendao.GroupInfoDao;
import org.greenrobot.greendao.query.QueryBuilder;
import java.util.List;

public class GroupInfoSql extends BaseSql {

    private final static String TAG = "GroupInfoSql";

    public static void deleteGroup(GroupInfo info) {
        DaoSession daoSession = getDaoWriteSession();
        GroupInfoDao dao = daoSession.getGroupInfoDao();
        dao.delete(info);
    }

    public static void updateGroup(GroupInfo info) {
        DaoSession daoSession = getDaoWriteSession();
        GroupInfoDao dao = daoSession.getGroupInfoDao();
        dao.update(info);
    }

    public static void updateGroupList(List<GroupInfo> infos) {
        DaoSession daoSession = getDaoWriteSession();
        GroupInfoDao userDao = daoSession.getGroupInfoDao();
        userDao.updateInTx(infos);
    }

    public static void insertGroup(GroupInfo info) {
        DaoSession daoSession = getDaoWriteSession();
        GroupInfoDao dao = daoSession.getGroupInfoDao();
        dao.insert(info);
    }

    public static List<GroupInfo> queryGroupInfoById(int id) {
        DaoSession daoSession = getDaoWriteSession();
        GroupInfoDao userDao = daoSession.getGroupInfoDao();
        QueryBuilder<GroupInfo> qb = userDao.queryBuilder();
        qb.where(GroupInfoDao.Properties.GroupId.eq(id));
        List<GroupInfo> list = qb.list();
        return list;
    }

    public static void insertGrouplList(List<GroupInfo> users) {
        if (users == null || users.isEmpty()) {
            return;
        }
        DaoSession daoSession = getDaoWriteSession();
        GroupInfoDao dao = daoSession.getGroupInfoDao();
        dao.insertInTx(users);
    }

    /**
     * 查询分组列表
     */
    public static List<GroupInfo> queryGrouplList() {
        DaoSession daoSession = getDaoWriteSession();
        GroupInfoDao dao = daoSession.getGroupInfoDao();
        QueryBuilder<GroupInfo> qb = dao.queryBuilder().orderAsc(GroupInfoDao.Properties.GroupLevel);
        List<GroupInfo> list = qb.list();
        return list;
    }

}
