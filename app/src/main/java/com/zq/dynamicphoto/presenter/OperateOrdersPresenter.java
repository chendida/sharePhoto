package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.DeleteWaterMouldImp;
import com.zq.dynamicphoto.model.EditOrdreStatusImp;
import com.zq.dynamicphoto.model.GetAddWaterMouldListImp;
import com.zq.dynamicphoto.model.OrdersListGetImp;
import com.zq.dynamicphoto.view.ILiveOrdersView;

/**
 * Created by Administrator on 2018/7/24.
 */

public class OperateOrdersPresenter<T extends ILiveOrdersView> extends BasePresenter<T> {
    //2.model层的引用
    BaseModel ordersListGetImp = new OrdersListGetImp();//获取订单列表

    BaseModel editOrdreStatusImp = new EditOrdreStatusImp();//编辑订单状态


    //3.构造方法
    public OperateOrdersPresenter() {
    }

    //4.执行操作(UI逻辑)
    public void getOrdersList(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (ordersListGetImp != null){
                ordersListGetImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showGetOrdersList(result);
                        }
                    }

                    @Override
                    public void onFail() {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showFailed();
                        }
                    }
                },netRequestBean);
            }
        }
    }


    public void editOrderStatus(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (editOrdreStatusImp != null){
                editOrdreStatusImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showUpdateOrderStatus(result);
                        }
                    }

                    @Override
                    public void onFail() {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showFailed();
                        }
                    }
                },netRequestBean);
            }
        }
    }
}
