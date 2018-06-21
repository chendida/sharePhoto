package com.zq.dynamicphoto.bean;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/18.
 */

public class DynamicBean {

    private ArrayList<LocalMedia> mSelectedImages = new ArrayList<>();//保存当前所选择的图片地址

    private String content;//动态内容

    private Integer requestType;//1新增，2编辑，3转发

    private String permission;//私密还是公开

    private ArrayList<DynamicLabel> dynamicLabels;


    private ArrayList<String> selectUrl = new ArrayList<>();//保存传到Cos上的图片地址

    private Boolean isImage = false;//是否是图片

    private Boolean isVideo = false;//是否是视频

    private Boolean isShare = false;//是否是分享视频

    private Integer width;

    private Integer height;

    public ArrayList<DynamicLabel> getDynamicLabels() {
        return dynamicLabels;
    }

    public void setDynamicLabels(ArrayList<DynamicLabel> dynamicLabels) {
        this.dynamicLabels = dynamicLabels;
    }

    public ArrayList<LocalMedia> getmSelectedImages() {
        return mSelectedImages;
    }

    public void setmSelectedImages(ArrayList<LocalMedia> mSelectedImages) {
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

    public Boolean getImage() {
        return isImage;
    }

    public void setImage(Boolean image) {
        isImage = image;
    }

    public Boolean getVideo() {
        return isVideo;
    }

    public void setVideo(Boolean video) {
        isVideo = video;
    }

    public Boolean getShare() {
        return isShare;
    }

    public void setShare(Boolean share) {
        isShare = share;
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
