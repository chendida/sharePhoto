package com.zq.dynamicphoto.view;

import com.zq.dynamicphoto.base.BaseView;
import com.zq.dynamicphoto.bean.Result;

/**
 * Created by Administrator on 2018/8/23.
 */

public interface ISearchView extends BaseView {
    void showDeleteResult(Result result);

    void showStickResult(Result result);

    void showGetDynamicList(Result result);
}
