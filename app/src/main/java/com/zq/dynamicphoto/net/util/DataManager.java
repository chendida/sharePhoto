package com.zq.dynamicphoto.net.util;

import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.net.RetrofitApiService;

import io.reactivex.Observable;

/**
 *该类用来管理RetrofitApiService中对应的各种API接口，
 *当做Retrofit和presenter中的桥梁，Activity就不用直接和retrofit打交道了
 * Created by Administrator on 2018/5/24.
 */

public class DataManager {
    private RetrofitApiService mRetrofitService;
    private volatile static DataManager instance;

    private DataManager(){
        this.mRetrofitService = RetrofitUtil.getInstance().getRetrofitApiService();
    }
    //由于该对象会被频繁调用，采用单例模式，下面是一种线程安全模式的单例写法
    public static DataManager getInstance() {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();
                }
            }
        }
        return instance;
    }

    //将retrofit的业务方法映射到DataManager中，以后统一用该类来调用业务方法
    //以后再retrofit中增加业务方法的时候，相应的这里也要添加，比如添加一个getOrder
    public Observable<Result> getPhotoDynamic(NetRequestBean netRequestBean){
        return mRetrofitService.getPhotoDynamic(netRequestBean);
    }

    /**
     * 微信登录
     * @param netRequestBean
     * @return
     */
    public Observable<Result> wxLogin(NetRequestBean netRequestBean){
        return mRetrofitService.wxLogin(netRequestBean);
    }

    /**
     * 手机号码登录
     * @param netRequestBean
     * @return
     */
    public Observable<Result> phoneLogin(NetRequestBean netRequestBean){
        return mRetrofitService.login(netRequestBean);
    }

    public Observable<Result> deleteDynamic(NetRequestBean netRequestBean){
        return mRetrofitService.deleteDynamic(netRequestBean);
    }

    public Observable<Result> stickDynamic(NetRequestBean netRequestBean){
        return mRetrofitService.stitckDynamic(netRequestBean);
    }

    public Observable<Result> getLabelsList(NetRequestBean netRequestBean){
        return mRetrofitService.findLabel(netRequestBean);
    }

    public Observable<Result> createLabels(NetRequestBean netRequestBean){
        return mRetrofitService.createLabel(netRequestBean);
    }

    public Observable<Result> deleteLabel(NetRequestBean netRequestBean){
        return mRetrofitService.deleteLabel(netRequestBean);
    }

    public Observable<Result> editLabel(NetRequestBean netRequestBean){
        return mRetrofitService.updateLabel(netRequestBean);
    }

    public Observable<Result> createDynamic(NetRequestBean netRequestBean){
        return mRetrofitService.createDynamic(netRequestBean);
    }

    public Observable<Result> editDynamic(NetRequestBean netRequestBean){
        return mRetrofitService.editPhotoDynamic(netRequestBean);
    }

    public Observable<Result> repeatDynamic(NetRequestBean netRequestBean){
        return mRetrofitService.repeatDynamic(netRequestBean);
    }

    public Observable<Result> getFirendCircleList(NetRequestBean netRequestBean){
        return mRetrofitService.getFriendCircleList(netRequestBean);
    }

    public Observable<Result> deleteFriendCircleItem(NetRequestBean netRequestBean){
        return mRetrofitService.deleteFriendCircle(netRequestBean);
    }

    public Observable<Result> getDynamicSelectList(NetRequestBean netRequestBean){
        return mRetrofitService.getDynamicList(netRequestBean);
    }

    public Observable<Result> addFriendCircle(NetRequestBean netRequestBean){
        return mRetrofitService.addFriendCircleDynamic(netRequestBean);
    }

    public Observable<Result> editFriendCircle(NetRequestBean netRequestBean){
        return mRetrofitService.editFriendCircle(netRequestBean);
    }

    public Observable<Result> getFriendCircleDetails(NetRequestBean netRequestBean){
        return mRetrofitService.getFriendCircle(netRequestBean);
    }

    public Observable<Result> getFollowList(NetRequestBean netRequestBean){
        return mRetrofitService.getFollowList(netRequestBean);
    }

    public Observable<Result> getPermissionStatus(NetRequestBean netRequestBean){
        return mRetrofitService.checkPermissionStatus(netRequestBean);
    }

    public Observable<Result> setPermission(NetRequestBean netRequestBean){
        return mRetrofitService.setPerssion(netRequestBean);
    }

    public Observable<Result> cancelSetPermission(NetRequestBean netRequestBean){
        return mRetrofitService.cancelSetPerssion(netRequestBean);
    }

    public Observable<Result> resetPwd(NetRequestBean netRequestBean){
        return mRetrofitService.updatePwd(netRequestBean);
    }

    public Observable<Result> bindPhone(NetRequestBean netRequestBean){
        return mRetrofitService.bindPhone(netRequestBean);
    }

    public Observable<Result> getAuthCode(NetRequestBean netRequestBean){
        return mRetrofitService.getAuthCode(netRequestBean);
    }

    public Observable<Result> getPhotoInfo(NetRequestBean netRequestBean){
        return mRetrofitService.getPhotoInfo(netRequestBean);
    }

    public Observable<Result> editPhotoInfo(NetRequestBean netRequestBean){
        return mRetrofitService.editPhotoInfo(netRequestBean);
    }

    public Observable<Result> getWaterMouldList(NetRequestBean netRequestBean){
        return mRetrofitService.getWaterMouldList(netRequestBean);
    }

    public Observable<Result> getSingleWaterMouldList(NetRequestBean netRequestBean){
        return mRetrofitService.getSingleMouldList(netRequestBean);
    }

    public Observable<Result> getAddMouldList(NetRequestBean netRequestBean){
        return mRetrofitService.getAddMouldList(netRequestBean);
    }

    public Observable<Result> addWaterMould(NetRequestBean netRequestBean){
        return mRetrofitService.addWaterMould(netRequestBean);
    }

    public Observable<Result> deleteWaterMould(NetRequestBean netRequestBean){
        return mRetrofitService.deleteWaterMould(netRequestBean);
    }

    public Observable<Result> getOrdersNumAndBalance(NetRequestBean netRequestBean){
        return mRetrofitService.getOrdersNumAndBalance(netRequestBean);
    }

    public Observable<Result> updateOrderStatus(NetRequestBean netRequestBean){
        return mRetrofitService.updateOrderStatus(netRequestBean);
    }

    public Observable<Result> getOrdersList(NetRequestBean netRequestBean){
        return mRetrofitService.getOrdersList(netRequestBean);
    }

    public Observable<Result> getLiveGoodsList(NetRequestBean netRequestBean){
        return mRetrofitService.getLiveGoodsList(netRequestBean);
    }

    public Observable<Result> uploadLiveGoodsList(NetRequestBean netRequestBean){
        return mRetrofitService.uploadLiveGoodsList(netRequestBean);
    }

    public Observable<Result> getUserConsumptionList(NetRequestBean netRequestBean){
        return mRetrofitService.getUserConsumptionList(netRequestBean);
    }

    public Observable<Result> getChargeCode(NetRequestBean netRequestBean){
        return mRetrofitService.getChargeCode(netRequestBean);
    }

    public Observable<Result> getOrderId(NetRequestBean netRequestBean){
        return mRetrofitService.getOrderId(netRequestBean);
    }

    public Observable<Result> getLiveInfo(NetRequestBean netRequestBean){
        return mRetrofitService.getLiveInfo(netRequestBean);
    }

    public Observable<Result> createLiveRoom(NetRequestBean netRequestBean){
        return mRetrofitService.createLiveRoom(netRequestBean);
    }

    public Observable<Result> getVipStatus(NetRequestBean netRequestBean){
        return mRetrofitService.getVipStatus(netRequestBean);
    }

    public Observable<Result> updateBg(NetRequestBean netRequestBean){
        return mRetrofitService.updateBg(netRequestBean);
    }

    public Observable<Result> searchDynamic(NetRequestBean netRequestBean){
        return mRetrofitService.serchDynamic(netRequestBean);
    }

    public Observable<Result> getLabels(NetRequestBean netRequestBean){
        return mRetrofitService.getLabelList(netRequestBean);
    }
}
