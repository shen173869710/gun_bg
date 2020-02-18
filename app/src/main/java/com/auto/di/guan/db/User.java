package com.auto.di.guan.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

@Entity
public class User implements Serializable {
    static final long serialVersionUID=1L;
    @Id(autoincrement = true)
    private Long id;
    /** 用户ID */
    @Property(nameInDb = "userId")
    private Long userId;
    /** 登录名称 */
    @Property(nameInDb = "loginName")
    private String loginName;
    /** 用户名称 */
    @Property(nameInDb = "userName")
    private String userName;
    /** 用户邮箱 */
    @Property(nameInDb = "email")
    private String email;
    /** 手机号码 */
    @Property(nameInDb = "phonenumber")
    private String phonenumber;
    /** 用户性别 */
    @Property(nameInDb = "sex")
    private String sex;
    /** 用户头像 */
    @Property(nameInDb = "avatar")
    private String avatar;
    /** 密码 */
    @Property(nameInDb = "password")
    private String password;
    /** 帐号状态（0正常 1停用） */
    @Property(nameInDb = "status")
    private String status;
    // 项目id
    @Property(nameInDb = "projectId")
    private String projectId;
    // 项目组id
    @Property(nameInDb = "projectGroupId")
    private String projectGroupId;
    // 分干管(数量)
    @Property(nameInDb = "trunkPipeNum")
    private Integer trunkPipeNum;
    // 出地桩(数量)
    @Property(nameInDb = "pileOutNum")
    private Integer pileOutNum;
    // 项目名称
    @Property(nameInDb = "projectName")
    private String projectName;
    // 项目描述
    @Property(nameInDb = "projectDesc")
    private String projectDesc;
    // 项目备注
    @Property(nameInDb = "projectRemarks")
    private String projectRemarks;
    // 项目经纬度
    @Property(nameInDb = "longitudeLatitude")
    private String longitudeLatitude;
    @Generated(hash = 756738866)
    public User(Long id, Long userId, String loginName, String userName,
            String email, String phonenumber, String sex, String avatar,
            String password, String status, String projectId, String projectGroupId,
            Integer trunkPipeNum, Integer pileOutNum, String projectName,
            String projectDesc, String projectRemarks, String longitudeLatitude) {
        this.id = id;
        this.userId = userId;
        this.loginName = loginName;
        this.userName = userName;
        this.email = email;
        this.phonenumber = phonenumber;
        this.sex = sex;
        this.avatar = avatar;
        this.password = password;
        this.status = status;
        this.projectId = projectId;
        this.projectGroupId = projectGroupId;
        this.trunkPipeNum = trunkPipeNum;
        this.pileOutNum = pileOutNum;
        this.projectName = projectName;
        this.projectDesc = projectDesc;
        this.projectRemarks = projectRemarks;
        this.longitudeLatitude = longitudeLatitude;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getLoginName() {
        return this.loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhonenumber() {
        return this.phonenumber;
    }
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getProjectId() {
        return this.projectId;
    }
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    public String getProjectGroupId() {
        return this.projectGroupId;
    }
    public void setProjectGroupId(String projectGroupId) {
        this.projectGroupId = projectGroupId;
    }
    public Integer getTrunkPipeNum() {
        return this.trunkPipeNum;
    }
    public void setTrunkPipeNum(Integer trunkPipeNum) {
        this.trunkPipeNum = trunkPipeNum;
    }
    public Integer getPileOutNum() {
        return this.pileOutNum;
    }
    public void setPileOutNum(Integer pileOutNum) {
        this.pileOutNum = pileOutNum;
    }
    public String getProjectName() {
        return this.projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getProjectDesc() {
        return this.projectDesc;
    }
    public void setProjectDesc(String projectDesc) {
        this.projectDesc = projectDesc;
    }
    public String getProjectRemarks() {
        return this.projectRemarks;
    }
    public void setProjectRemarks(String projectRemarks) {
        this.projectRemarks = projectRemarks;
    }
    public String getLongitudeLatitude() {
        return this.longitudeLatitude;
    }
    public void setLongitudeLatitude(String longitudeLatitude) {
        this.longitudeLatitude = longitudeLatitude;
    }
}
