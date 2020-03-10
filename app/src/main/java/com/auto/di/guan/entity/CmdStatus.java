package com.auto.di.guan.entity;

/**
 * Created by Administrator on 2018/7/3
 * 更新命令的通信状态
 */

public class CmdStatus {
    private int control_id;
    private String controlName;
    private String cmd_name;
    private String cmd_start;
    private String cmd_end;
    private String cmd_read_start;
    private String cmd_read_middle;
    private String cmd_read_end;
    private int cmdType;

    public int getControl_id() {
        return control_id;
    }

    public void setControl_id(int control_id) {
        this.control_id = control_id;
    }

    public String getControlName() {
        return controlName;
    }

    public void setControlName(String controlName) {
        this.controlName = controlName;
    }

    public String getCmd_name() {
        return cmd_name;
    }

    public void setCmd_name(String cmd_name) {
        this.cmd_name = cmd_name;
    }

    public String getCmd_start() {
        return cmd_start;
    }

    public void setCmd_start(String cmd_start) {
        this.cmd_start = cmd_start;
    }

    public String getCmd_end() {
        return cmd_end;
    }

    public void setCmd_end(String cmd_end) {
        this.cmd_end = cmd_end;
    }

    public String getCmd_read_start() {
        return cmd_read_start;
    }

    public void setCmd_read_start(String cmd_read_start) {
        this.cmd_read_start = cmd_read_start;
    }

    public String getCmd_read_middle() {
        return cmd_read_middle;
    }

    public void setCmd_read_middle(String cmd_read_middle) {
        this.cmd_read_middle = cmd_read_middle;
    }

    public String getCmd_read_end() {
        return cmd_read_end;
    }

    public void setCmd_read_end(String cmd_read_end) {
        this.cmd_read_end = cmd_read_end;
    }

    public int getCmdType() {
        return cmdType;
    }

    public void setCmdType(int cmdType) {
        this.cmdType = cmdType;
    }
}
