package com.elio.bean;

import java.util.Date;

/**
 * created by elio on 30/11/2022
 */
public class Info extends Persistence {
    private Integer id;
    private String name;
    private String identification;
    private String telephoneNumber;
    private String healthCodePath;
    private String routineCodePath;
    private String browserExecPath;
    private Date createTime;
    private Date updateTime;

    public Info() {
    }

    public Info(Integer id, String name, String identification, String telephoneNumber, String healthCodePath, String routineCodePath, String browserExecPath, Date createTime, Date updateTime) {
        this.id = id;
        this.name = name;
        this.identification = identification;
        this.telephoneNumber = telephoneNumber;
        this.healthCodePath = healthCodePath;
        this.routineCodePath = routineCodePath;
        this.browserExecPath = browserExecPath;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Info{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", identification='" + identification + '\'' +
                ", telephoneNumber=" + telephoneNumber +
                ", healthCodePath='" + healthCodePath + '\'' +
                ", routineCodePath='" + routineCodePath + '\'' +
                ", browserExecPath='" + browserExecPath + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getHealthCodePath() {
        return healthCodePath;
    }

    public void setHealthCodePath(String healthCodePath) {
        this.healthCodePath = healthCodePath;
    }

    public String getRoutineCodePath() {
        return routineCodePath;
    }

    public void setRoutineCodePath(String routineCodePath) {
        this.routineCodePath = routineCodePath;
    }

    public String getBrowserExecPath() {
        return browserExecPath;
    }

    public void setBrowserExecPath(String browserExecPath) {
        this.browserExecPath = browserExecPath;
    }
}
