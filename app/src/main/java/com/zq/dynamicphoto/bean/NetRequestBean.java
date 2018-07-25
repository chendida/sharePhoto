package com.zq.dynamicphoto.bean;

import com.zq.dynamicphoto.mylive.bean.Charge;
import com.zq.dynamicphoto.mylive.bean.ChargeOrder;
import com.zq.dynamicphoto.mylive.bean.ChargeType;
import com.zq.dynamicphoto.mylive.bean.LiveConsumeRecord;
import com.zq.dynamicphoto.mylive.bean.NewLiveRoom;

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

    private ChargeOrder chargeOrder;

    private NewLiveRoom newLiveRoom;

    private LiveConsumeRecord liveConsumeRecord;

    private ChargeType chargeType;

    private Charge charge;

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }

    public ChargeType getChargeType() {
        return chargeType;
    }

    public void setChargeType(ChargeType chargeType) {
        this.chargeType = chargeType;
    }

    public LiveConsumeRecord getLiveConsumeRecord() {
        return liveConsumeRecord;
    }

    public void setLiveConsumeRecord(LiveConsumeRecord liveConsumeRecord) {
        this.liveConsumeRecord = liveConsumeRecord;
    }

    public NewLiveRoom getNewLiveRoom() {
        return newLiveRoom;
    }

    public void setNewLiveRoom(NewLiveRoom newLiveRoom) {
        this.newLiveRoom = newLiveRoom;
    }

    public ChargeOrder getChargeOrder() {
        return chargeOrder;
    }

    public void setChargeOrder(ChargeOrder chargeOrder) {
        this.chargeOrder = chargeOrder;
    }

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
