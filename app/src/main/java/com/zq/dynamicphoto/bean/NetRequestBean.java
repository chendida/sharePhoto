package com.zq.dynamicphoto.bean;

/**
 * Created by Administrator on 2018/5/24.
 */

public class NetRequestBean {
    private DeviceProperties deviceProperties;

    private User user;

    private UserRelation userRelation;



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
