package com.zq.dynamicphoto.view;

import com.zq.dynamicphoto.base.BaseView;
import com.zq.dynamicphoto.bean.Result;

/**
 * Created by Administrator on 2018/6/26.
 */

public interface IPermissionView extends BaseView{
    void showGetPermissionStatusResult(Result result);

    void showSetPermissionResult(Result result);

    void showCancelPermissionResult(Result result);
}
