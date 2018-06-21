package com.zq.dynamicphoto.bean;

/**
 * Created by Administrator on 2018/6/21.
 */

public class MessageEvent {
    private DynamicBean dynamicBean;

    public MessageEvent(DynamicBean dynamicBean) {
        this.dynamicBean = dynamicBean;
    }

    public DynamicBean getDynamicBean() {
        return dynamicBean;
    }

    public void setDynamicBean(DynamicBean dynamicBean) {
        this.dynamicBean = dynamicBean;
    }
}
