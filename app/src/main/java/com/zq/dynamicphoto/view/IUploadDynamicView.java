package com.zq.dynamicphoto.view;

import com.zq.dynamicphoto.base.BaseView;
import com.zq.dynamicphoto.bean.Result;

/**
 * Created by Administrator on 2018/6/21.
 */

public interface IUploadDynamicView extends BaseView {
    void showUploadDynamicResult(Result result);//上传动态结果回调

    void showEditDynamicResult(Result result);//编辑动态结果回调

    void showRepeatDynamicResult(Result result);//转发动态结果回调
}
