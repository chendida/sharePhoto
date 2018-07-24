package com.zq.dynamicphoto.bean;

/**
 * Created by Administrator on 2018/5/24.
 */

public class NetRequestBean {
    private DeviceProperties deviceProperties;

    private User user;

    private UserRelation userRelation;

    private Dynamic dynamic;

    private UserInfo userInfo;

    private DynamicLabel dynamicLabel;

    private DynamicForward dynamicForward;

    private Moments moments;

    private UserPermissions userPermissions;

    private AuthCode authCode;

    private Watermark watermark;

    private UserWatermark userWatermark;

    public UserWatermark getUserWatermark() {
        return userWatermark;
    }

    public void setUserWatermark(UserWatermark userWatermark) {
        this.userWatermark = userWatermark;
    }

    public Watermark getWatermark() {
        return watermark;
    }

    public void setWatermark(Watermark watermark) {
        this.watermark = watermark;
    }

    public void setAuthCode(AuthCode authCode) {
        this.authCode = authCode;
    }

    public UserPermissions getUserPermissions() {
        return userPermissions;
    }

    public void setUserPermissions(UserPermissions userPermissions) {
        this.userPermissions = userPermissions;
    }

    public Moments getMoments() {
        return moments;
    }

    public void setMoments(Moments moments) {
        this.moments = moments;
    }

    public DynamicForward getDynamicForward() {
        return dynamicForward;
    }

    public void setDynamicForward(DynamicForward dynamicForward) {
        this.dynamicForward = dynamicForward;
    }

    public DynamicLabel getDynamicLabel() {
        return dynamicLabel;
    }

    public void setDynamicLabel(DynamicLabel dynamicLabel) {
        this.dynamicLabel = dynamicLabel;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Dynamic getDynamic() {
        return dynamic;
    }

    public void setDynamic(Dynamic dynamic) {
        this.dynamic = dynamic;
    }

    public UserRelation getUserRelation() {
        return userRelation;
    }

    public void setUserRelation(UserRelation userRelation) {
        this.userRelation = userRelation;
    }

    public DeviceProperties getDeviceProperties() {
        return deviceProperties;
    }

    public void setDeviceProperties(DeviceProperties deviceProperties) {
        this.deviceProperties = deviceProperties;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
