package com.auto.di.guan.db.sql;

import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.greendao.DaoSession;
import com.auto.di.guan.db.greendao.GroupInfoDao;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.utils.LogUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class GroupInfoSql extends BaseSql {
    /**
     *       删除一个组
     * @param info
     */
    public static void deleteGroup(GroupInfo info) {
        DaoSession daoSession = getDaoWriteSession();
        GroupInfoDao dao = daoSession.getGroupInfoDao();
        dao.delete(info);
    }
    /**
     *        更新单个组的状态
     * @param info
     */
    public static void updateGroup(GroupInfo info) {
        DaoSession daoSession = getDaoWriteSession();
        GroupInfoDao dao = daoSession.getGroupInfoDao();
        dao.update(info);
    }
    /**
     *        更新组的状态
     * @param infos
     */
    public static void updateGroupList(List<GroupInfo> infos) {
        DaoSession daoSession = getDaoWriteSession();
        GroupInfoDao userDao = daoSession.getGroupInfoDao();
        userDao.updateInTx(infos);
    }
    /**
     *       插入一条组信息
     * @param info
     */
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


    /**
     * 查询所有组的信息
     */
    public static List<GroupInfo> queryGroupList() {
        DaoSession daoSession = getDaoWriteSession();
        GroupInfoDao dao = daoSession.getGroupInfoDao();
        QueryBuilder<GroupInfo> qb = dao.queryBuilder().orderAsc(GroupInfoDao.Properties.GroupLevel);
        List<GroupInfo> list = qb.list();
        return list;
    }


    /**
     * 查询已经设置轮灌优先级的设备
     */
    public static List<GroupInfo> queryGroupSettingList() {
        DaoSession daoSession = getDaoWriteSession();
        GroupInfoDao dao = daoSession.getGroupInfoDao();
        QueryBuilder<GroupInfo> qb = dao.queryBuilder();
        qb.where(GroupInfoDao.Properties.GroupIsJoin.eq(true));
        qb.orderAsc(GroupInfoDao.Properties.GroupLevel);
        List<GroupInfo> list = qb.list();
        return list;
    }

    /**
     * 查询正在开启的轮灌分组
     */
    public static List<GroupInfo> queryOpenGroupList() {
        DaoSession daoSession = getDaoWriteSession();
        GroupInfoDao dao = daoSession.getGroupInfoDao();
        QueryBuilder<GroupInfo> qb = dao.queryBuilder();
        qb.where(GroupInfoDao.Properties.GroupStatus.eq(1));
        qb.where(GroupInfoDao.Properties.GroupIsJoin.eq(true));
        qb.orderAsc(GroupInfoDao.Properties.GroupLevel);
        List<GroupInfo> list = qb.list();
        if(list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    /**
     *   查询下一组需要执行的分组
     */
    public static GroupInfo queryNextGroup() {
        DaoSession daoSession = getDaoWriteSession();
        GroupInfoDao dao = daoSession.getGroupInfoDao();
        QueryBuilder<GroupInfo> qb = dao.queryBuilder();
        qb.where(GroupInfoDao.Properties.GroupTime.gt(0));
        qb.where(GroupInfoDao.Properties.GroupIsJoin.eq(true));
        qb.orderAsc(GroupInfoDao.Properties.GroupLevel);
        List<GroupInfo> list = qb.list();
        if(list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     *   查询下一组需要执行的分组
     */
    public static List<GroupInfo> queryNextGroupList(int groupId) {
        DaoSession daoSession = getDaoWriteSession();
        GroupInfoDao dao = daoSession.getGroupInfoDao();
        QueryBuilder<GroupInfo> qb = dao.queryBuilder();
        qb.where(GroupInfoDao.Properties.GroupTime.gt(0));
        qb.where(GroupInfoDao.Properties.GroupId.notEq(groupId));
        qb.where(GroupInfoDao.Properties.GroupIsJoin.eq(true));
        qb.orderAsc(GroupInfoDao.Properties.GroupLevel);
        List<GroupInfo> list = qb.list();
        if(list != null && list.size() > 0) {
            return list;
        }
        return null;
    }


    /**
     *   更新当前的group
     */
    public static void updateRunGroup (GroupInfo groupInfo){
        if (groupInfo.getGroupRunTime() % Entiy.DB_SAVE_TIME== 0) {
            GroupInfoSql.updateGroup(groupInfo);
        }
    }
}
