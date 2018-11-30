package com.auto.di.guan.entity;

import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.GroupInfo;

/**
 * Created by Administrator on 2017/12/22 0022.
 */

public class ControlOptionEvent {
    public ControlOptionEvent(ControlInfo groupInfo, boolean isStart) {
        this.controlInfo = groupInfo;
        this.isStart = isStart;
    }

    public ControlInfo controlInfo;
    public boolean isStart;

}
