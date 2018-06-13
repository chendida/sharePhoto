package com.zq.dynamicphoto.view;

import com.zq.dynamicphoto.base.BaseView;
import com.zq.dynamicphoto.bean.Result;

/**
 * Created by Administrator on 2018/6/12.
 */

public interface IDynamicView extends BaseView {
    void showData(Result result);
    void showDeleteResult(Result result);
    void showStickResult(Result result);
}
