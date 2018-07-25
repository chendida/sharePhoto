package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.AuthCodeGetImp;
import com.zq.dynamicphoto.model.GetRechargeCodeImp;
import com.zq.dynamicphoto.model.GetRechargeOrderIdImp;
import com.zq.dynamicphoto.model.ResetPwdImp;
import com.zq.dynamicphoto.view.IAccountRechargeView;

/**
 * Created by Administrator on 2018/7/25.
 */

public class AccountRechargePresenter<T extends IAccountRechargeView> extends BasePresenter<T> {
    BaseModel getRechargeCodeImp = new GetRechargeCodeImp();

    BaseModel getRechargeOrderIdImp = new GetRechargeOrderIdImp();

    //4.执行操作(UI逻辑)
    public void getRechargeCode(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (getRechargeCodeImp != null){
                getRechargeCodeImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showGetChargeCode(result);
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

    public void getRechargeOrderId(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (getRechargeOrderIdImp != null){
                getRechargeOrderIdImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showGetChargeOrderId(result);
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
