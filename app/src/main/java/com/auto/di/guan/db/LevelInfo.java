package com.auto.di.guan.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/7/25.
 */
@Entity
public class LevelInfo {
    @Id(autoincrement = true)
    public Long id;

    public int levelId;
    public boolean isGroupUse;
    public boolean isLevelUse;
    public boolean isOtherUse;
    @Generated(hash = 1904933810)
    public LevelInfo(Long id, int levelId, boolean isGroupUse, boolean isLevelUse,
            boolean isOtherUse) {
        this.id = id;
        this.levelId = levelId;
        this.isGroupUse = isGroupUse;
        this.isLevelUse = isLevelUse;
        this.isOtherUse = isOtherUse;
    }
    @Generated(hash = 883524217)
    public LevelInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getLevelId() {
        return this.levelId;
    }
    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }
    public boolean getIsGroupUse() {
        return this.isGroupUse;
    }
    public void setIsGroupUse(boolean isGroupUse) {
        this.isGroupUse = isGroupUse;
    }
    public boolean getIsLevelUse() {
        return this.isLevelUse;
    }
    public void setIsLevelUse(boolean isLevelUse) {
        this.isLevelUse = isLevelUse;
    }
    public boolean getIsOtherUse() {
        return this.isOtherUse;
    }
    public void setIsOtherUse(boolean isOtherUse) {
        this.isOtherUse = isOtherUse;
    }

}
