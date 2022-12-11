package com.elio.bean;

import java.util.Arrays;
import java.util.Date;

/**
 * created by elio on 30/11/2022
 */
public class Offset extends Persistence {
    private Integer id;
    private String offsets;
    private Date createTime;
    private Date updateTime;

    public Offset() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append("0,0&");
        }
        offsets = sb.toString();
    }


    public Offset(Integer id, String offsets, Date createTime, Date updateTime) {
        this.id = id;
        this.offsets = offsets;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOffsets() {
        return offsets;
    }

    public void setOffsets(String offsets) {
        this.offsets = offsets;
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

    @Override
    public String toString() {
        return "Offset{" +
                "id=" + id +
                ", offsets='" + offsets + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
