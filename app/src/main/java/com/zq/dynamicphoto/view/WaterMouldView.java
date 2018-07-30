package com.zq.dynamicphoto.view;

import com.zq.dynamicphoto.bean.Image;
import com.zq.dynamicphoto.bean.UserWatermark;

/**
 * Created by Administrator on 2018/7/20.
 */

public interface WaterMouldView {
    void showSelectDialog();

    void addWaterImage(Image image);

    void showLongClickOperate(UserWatermark image);
}
