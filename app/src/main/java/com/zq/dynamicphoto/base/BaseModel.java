package com.zq.dynamicphoto.base;

import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;

/**
 * Created by Administrator on 2018/5/24.
 */

public interface BaseModel {
    void loadData(OnLoadListener onLoadListener, NetRequestBean netRequestBean);

    //设计一个内部回调接口
    interface OnLoadListener{
        void onComplete(Result result);

        void onFail();
    }
}
