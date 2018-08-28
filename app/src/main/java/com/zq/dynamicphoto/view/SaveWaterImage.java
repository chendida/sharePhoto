package com.zq.dynamicphoto.view;

/**
 * Created by Administrator on 2018/8/27.
 */

public interface SaveWaterImage {
    void startSave(int size);

    void endSave();

    void saveProcess(int num,int size);
}
