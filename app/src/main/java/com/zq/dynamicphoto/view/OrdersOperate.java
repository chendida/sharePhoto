package com.zq.dynamicphoto.view;

import android.widget.TextView;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.mylive.bean.ChargeOrder;

/**
 * 对订单的操作接口
 * Created by Administrator on 2018/7/24.
 */

public interface OrdersOperate {
    void updateOrderStatus(String msg, ChargeOrder chargeOrder, int type, AutoRelativeLayout btnOk
            ,AutoRelativeLayout btnSend, AutoRelativeLayout btnCancel,
                 AutoRelativeLayout layoutOrderStatus,TextView tvOrderStatus);
}
