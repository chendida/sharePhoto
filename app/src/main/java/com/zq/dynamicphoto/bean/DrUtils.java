package com.zq.dynamicphoto.bean;

import java.util.UUID;

/**
 * Created by Administrator on 2017/9/6.
 */

public class DrUtils {
    private static DeviceProperties instance;

    public static DeviceProperties getInstance() {
        if (instance == null) {
            synchronized (DeviceProperties.class) {
                if (instance == null) {
                    instance = new DeviceProperties();
                    instance.version = "1.0.2";
                    instance.channelId = "90000002";//渠道ID
                    instance.sdkVersion = "1.0.2";//客户端版本号
                    instance.imei = UUID.randomUUID().toString();
                    instance.phoneMode = 2;//1表示ios,2表示安卓
                    instance.setClientType(1);
                }
            }
        }
        return instance;
    }
}
