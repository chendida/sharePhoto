package com.zq.dynamicphoto.view;

import com.zq.dynamicphoto.base.BaseView;
import com.zq.dynamicphoto.bean.Result;

/**
 * Created by Administrator on 2018/7/23.
 */

public interface IOperateWaterView extends BaseView{
    void showAddWaterMouldList(Result result);

    void deleteWaterMould(Result result);
}
