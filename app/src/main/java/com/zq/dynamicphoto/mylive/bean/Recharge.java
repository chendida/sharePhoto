package com.zq.dynamicphoto.mylive.bean;

/**
 * Created by Administrator on 2018/5/17.
 */

public class Recharge {
    private String feeCode;//钻石数量

    private Integer money;//金额

    private Integer scale;//比例

    public String getFeeCode() {
        return feeCode;
    }

    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }
}
