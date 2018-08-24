package com.zq.dynamicphoto.view;

import com.zq.dynamicphoto.base.BaseView;
import com.zq.dynamicphoto.bean.Result;

/**
 * Created by Administrator on 2018/6/22.
 */

public interface IFriendCircleView extends BaseView{
    void getMomentListResult(Result result);
    void deleteMomentResult(Result result);
    void updateBg(Result result);
}
