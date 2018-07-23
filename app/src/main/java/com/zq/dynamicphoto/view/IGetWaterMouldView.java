package com.zq.dynamicphoto.view;

import com.zq.dynamicphoto.base.BaseView;
import com.zq.dynamicphoto.bean.Result;

/**
 * 获取水印模板
 * Created by Administrator on 2018/7/23.
 */

public interface IGetWaterMouldView extends BaseView{
    void getAllWaterMould(Result result);//获取所有的水印模板

    void getSingleWaterMould(Result result);//获取单类水印模板
}
