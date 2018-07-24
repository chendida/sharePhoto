package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.GetLiveGoodListImp;
import com.zq.dynamicphoto.model.UploadLiveGoodsImp;
import com.zq.dynamicphoto.view.AddLiveGoodView;

/**
 * Created by Administrator on 2018/7/24.
 */

public class LiveGoodsPresenter<T extends AddLiveGoodView> extends BasePresenter<T>{
    BaseModel getLiveGoodListImp = new GetLiveGoodListImp();//获取直播间产品选择列表

    BaseModel uploadLiveGoodsImp = new UploadLiveGoodsImp();//上传直播间产品


    //3.构造方法
    public LiveGoodsPresenter() {
    }

    //4.执行操作(UI逻辑)
    public void getLiveGoodList(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (getLiveGoodListImp != null){
                getLiveGoodListImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showGetLiveGoodListResult(result);
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


    public void uploadLiveGoods(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (uploadLiveGoodsImp != null){
                uploadLiveGoodsImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showAddLiveGoodsResult(result);
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
