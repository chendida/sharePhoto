package com.zq.dynamicphoto.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/30.
 */

public class ImageBucket implements Serializable{
    private String bucketName;//相册名
    private List<ImageItem> imageList;//相册中图片列表
    private List<ImageItem> selectedImageList = new ArrayList<>();//已选择的图片列表

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public List<ImageItem> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageItem> imageList) {
        this.imageList = imageList;
    }

    public List<ImageItem> getSelectedImageList() {
        return selectedImageList;
    }

    public void setSelectedImageList(List<ImageItem> selectedImageList) {
        this.selectedImageList = selectedImageList;
    }
}
