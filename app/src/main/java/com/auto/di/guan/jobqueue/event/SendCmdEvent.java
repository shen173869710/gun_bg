package com.auto.di.guan.jobqueue.event;


/**
 *   发送开关阀的命令
 */
public class SendCmdEvent {
    private String cmd;

    public SendCmdEvent(String cmd) {
        this.cmd = cmd;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
