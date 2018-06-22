package com.zq.dynamicphoto.bean;

import com.luck.picture.lib.entity.LocalMedia;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/18.
 */

public class DynamicBean implements Serializable{

    private ArrayList<String> mSelectedImages = new ArrayList<>();//保存当前所选择的图片地址

    private String content;//动态内容

    private Integer requestType;//1新增，2编辑，3转发

    private String permission;//私密还是公开

    private ArrayList<DynamicLabel> dynamicLabels;

    private Integer dynamicId;

    private DynamicForward dynamicForward;

    public DynamicForward getDynamicForward() {
        return dynamicForward;
    }

    public void setDynamicForward(DynamicForward dynamicForward) {
        this.dynamicForward = dynamicForward;
    }

    public Integer getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(Integer dynamicId) {
        this.dynamicId = dynamicId;
    }

    private ArrayList<String> selectUrl = new ArrayList<>();//保存传到Cos上的图片地址

    private Integer picType;//上传动态的类型

    private Integer width;

    private Integer height;

    public Integer getPicType() {
        return picType;
    }

    public void setPicType(Integer picType) {
        this.picType = picType;
    }

    public ArrayList<DynamicLabel> getDynamicLabels() {
        return dynamicLabels;
    }

    public void setDynamicLabels(ArrayList<DynamicLabel> dynamicLabels) {
        this.dynamicLabels = dynamicLabels;
    }

    public ArrayList<String> getmSelectedImages() {
        return mSelectedImages;
    }

    public void setmSelectedImages(ArrayList<String> mSelectedImages) {
        this.mSelectedImages = mSelectedImages;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRequestType() {
        return requestType;
    }

    public void setRequestType(Integer requestType) {
        this.requestType = requestType;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public ArrayList<String> getSelectUrl() {
        return selectUrl;
    }

    public void setSelectUrl(ArrayList<String> selectUrl) {
        this.selectUrl = selectUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
