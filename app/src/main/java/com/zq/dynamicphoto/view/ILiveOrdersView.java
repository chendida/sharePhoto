package com.zq.dynamicphoto.view;

import com.zq.dynamicphoto.base.BaseView;
import com.zq.dynamicphoto.bean.Result;

/**
 * Created by Administrator on 2018/7/24.
 */

public interface ILiveOrdersView extends BaseView {
    void showGetOrdersList(Result result);//获取订单列表

    void showUpdateOrderStatus(Result result);//确认订单
}
