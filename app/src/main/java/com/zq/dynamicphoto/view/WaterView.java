package com.zq.dynamicphoto.view;

import com.zq.dynamicphoto.bean.Image;

/**
 * Created by Administrator on 2018/7/9.
 */

public interface WaterView {
    void solaWaterPic(Image image);//单张

    void batchWaterPic(Image image);//批量
}
