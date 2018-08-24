package com.zq.dynamicphoto.view;

import com.zq.dynamicphoto.base.BaseView;
import com.zq.dynamicphoto.bean.Result;

/**
 * Created by Administrator on 2018/8/24.
 */

public interface LiveView extends BaseView {
    void showCloseRoomResult(Result result);

    void showLiveHeartResult(Result result);

    void showUpvoteResult(Result result);
}
