package com.zq.dynamicphoto.utils;

import com.zq.dynamicphoto.bean.UserInfo;

/**
 * Created by Administrator on 2018/3/23.
 */

public class SaveContactSelectUtils {
    private static SaveContactSelectUtils instance;

    private static UserInfo userInfo;


    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        SaveContactSelectUtils.userInfo = userInfo;
    }

    /**
     * 单例
     * @return
     */
    public static SaveContactSelectUtils getInstance(){
        if (null == instance) {
            synchronized (SaveContactSelectUtils.class) {
                if (null == instance) {
                    instance = new SaveContactSelectUtils();
                    userInfo = new UserInfo();
                }
            }
        }
        return instance;
    }
}
