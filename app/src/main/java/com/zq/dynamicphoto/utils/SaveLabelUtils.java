package com.zq.dynamicphoto.utils;

import com.zq.dynamicphoto.bean.DynamicLabel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/23.
 */

public class SaveLabelUtils {
    private static SaveLabelUtils instance;

    private static ArrayList<DynamicLabel> dynamicLabels;

    public ArrayList<DynamicLabel> getDynamicLabels() {
        return dynamicLabels;
    }

    /**
     * 单例
     * @return
     */
    public static SaveLabelUtils getInstance(){
        if (null == instance) {
            synchronized (SaveLabelUtils.class) {
                if (null == instance) {
                    instance = new SaveLabelUtils();
                    dynamicLabels = new ArrayList<>();
                }
            }
        }
        return instance;
    }
}
