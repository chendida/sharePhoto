package com.zq.dynamicphoto.utils;

import com.zq.dynamicphoto.bean.DynamicLabel;

/**
 * Created by Administrator on 2018/3/24.
 */

public class SaveSelectLabelUtils {
    private static SaveSelectLabelUtils instance;

    private static DynamicLabel dynamicLabel;

    public DynamicLabel getDynamicLabel() {
        return dynamicLabel;
    }

    public void setDynamicLabel(DynamicLabel dynamicLabel) {
        SaveSelectLabelUtils.dynamicLabel = dynamicLabel;
    }

    /**
     * 单例
     * @return
     */
    public static SaveSelectLabelUtils getInstance(){
        if (null == instance) {
            synchronized (SaveSelectLabelUtils.class) {
                if (null == instance) {
                    instance = new SaveSelectLabelUtils();
                    dynamicLabel = new DynamicLabel();
                }
            }
        }
        return instance;
    }
}
