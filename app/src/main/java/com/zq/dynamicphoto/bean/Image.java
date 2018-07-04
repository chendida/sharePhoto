package com.zq.dynamicphoto.bean;

/**
 * Created by Administrator on 2018/7/4.
 */

import java.io.Serializable;

/**
 *图片实体类
 */
public class Image implements Serializable{

    private String path;
    private long time;
    private String name;
    private Boolean isSelected = false;

    public Boolean isSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public Image(String path, long time, String name) {
        this.path = path;
        this.time = time;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}