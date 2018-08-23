package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.DynamicDeleteImp;
import com.zq.dynamicphoto.model.DynamicStickImp;
import com.zq.dynamicphoto.model.SearchDynamicImp;
import com.zq.dynamicphoto.view.ISearchView;

/**
 * Created by Administrator on 2018/8/23.
 */

public class SearchPresenter<T extends ISearchView> extends BasePresenter<T> {
    //2.model层的引用
    BaseModel searchDynamicImp = new SearchDynamicImp();
    BaseModel dynamicDeleteImp = new DynamicDeleteImp();
    BaseModel dynamicStickImp = new DynamicStickImp();
    //3.构造方法

    public SearchPresenter() {
    }

    //4.执行操作(UI逻辑)
    public void searchDynamicList(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (searchDynamicImp != null){
                searchDynamicImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showGetDynamicList(result);
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


    public void dynamicDelete(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (dynamicDeleteImp != null){
                dynamicDeleteImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showDeleteResult(result);
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

    public void dynamicStick(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (dynamicStickImp != null){
                dynamicStickImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showStickResult(result);
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
