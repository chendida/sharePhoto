package com.zq.dynamicphoto.view;

import com.zq.dynamicphoto.base.BaseView;
import com.zq.dynamicphoto.bean.Result;

/**
 * 开播界面UI逻辑
 * Created by Administrator on 2018/7/25.
 */

public interface IOpenLiveView extends BaseView{
    void showGetLiveInfo(Result result);//获取主播的直播信息

    void showOpenLiveResult(Result result);//开播
}
