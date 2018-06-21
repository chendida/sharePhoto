package com.zq.dynamicphoto.utils;

import com.zq.dynamicphoto.bean.DynamicBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SaveTasks {
    private static SaveTasks instance;

    private static ArrayList<DynamicBean> list;

    public ArrayList<DynamicBean> getList() {
        return list;
    }

    public void setList(ArrayList<DynamicBean> list) {
        SaveTasks.list = list;
    }

    /**
     * 单例
     * @return
     */
    public static SaveTasks getInstance(){
        if (null == instance) {
            synchronized (SaveTasks.class) {
                if (null == instance) {
                    instance = new SaveTasks();
                    list = new ArrayList<>();
                }
            }
        }
        return instance;
    }
}

