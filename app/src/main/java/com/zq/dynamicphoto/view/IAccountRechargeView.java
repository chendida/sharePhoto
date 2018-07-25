package com.zq.dynamicphoto.view;

import com.zq.dynamicphoto.base.BaseView;
import com.zq.dynamicphoto.bean.Result;

/**
 * 账户充值界面UI逻辑
 * Created by Administrator on 2018/7/25.
 */

public interface IAccountRechargeView extends BaseView{
    void showGetChargeCode(Result result);//获取计费点

    void showGetChargeOrderId(Result result);//获取充值的订单id
}
