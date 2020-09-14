package com.auto.di.guan.socket;


/**
 *  开启 kb 20000 0 ok
 *  关闭 gb 20000 0 ok
 *  查询
 */

/**
 *    bzt 20000   0            0      385    000   0
 *
 *               水泵编号       0待机  电压    电流  错误信息 0 正常
 *                             1启动                       1 无法连接
 *                             2运行                       2 过流保护
 *                             3停车                       3. 其他
 *                             4故障
 *
 *                   0    不查询
 *                   1 3  1分钟
 *                   2    10分钟
 */
public class SoketEvent {
    private String type;
    private String projectId;
    private String bengName;
    private String status;
    private String voltage;
    private String electricity;
    private String errorCode;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getBengName() {
        return bengName;
    }

    public void setBengName(String bengName) {
        this.bengName = bengName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getElectricity() {
        return electricity;
    }

    public void setElectricity(String electricity) {
        this.electricity = electricity;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}