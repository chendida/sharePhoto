package com.zq.dynamicphoto.view;

import com.zq.dynamicphoto.base.BaseView;
import com.zq.dynamicphoto.bean.Result;

/**
 * Created by Administrator on 2018/6/25.
 */

public interface IUploadMomentsView extends BaseView {
    void showUploadMomentsResult(Result result);

    void showEditMomentsResult(Result result);

    void showGetMomentsDetailsResult(Result result);
}
