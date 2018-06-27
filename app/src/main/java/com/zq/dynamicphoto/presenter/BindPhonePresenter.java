package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.AuthCodeGetImp;
import com.zq.dynamicphoto.model.BindPhoneImp;
import com.zq.dynamicphoto.model.PhoneLoginImp;
import com.zq.dynamicphoto.model.ResetPwdImp;
import com.zq.dynamicphoto.view.IBindPhoneView;

/**
 * Created by Administrator on 2018/6/27.
 */

public class BindPhonePresenter<T extends IBindPhoneView> extends BasePresenter<T> {
    BaseModel bindPhoneImp = new BindPhoneImp();

    BaseModel resetPwdImp = new ResetPwdImp();

    BaseModel authCodeGetImp = new AuthCodeGetImp();

    //4.执行操作(UI逻辑)
    public void getAuthCode(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (authCodeGetImp != null){
                authCodeGetImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showGetAuthCodeResult(result);
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

    public void bindPhone(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (bindPhoneImp != null){
                bindPhoneImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showBindPhoneResult(result);
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

    public void resetPwd(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (resetPwdImp != null){
                resetPwdImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showResetPwdResult(result);
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
