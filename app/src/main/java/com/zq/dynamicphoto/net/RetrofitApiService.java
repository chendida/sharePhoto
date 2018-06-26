package com.zq.dynamicphoto.net;

import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 网络请求
 * Created by Administrator on 2018/5/24.
 */

public interface RetrofitApiService {
    /**
     * 初始化
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=0&a=0&b=0")
    Call<Result> initApp(@Body NetRequestBean netRequestBean);


    /**
     * 获取相册动态
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=6&a=0&b=0")
    Observable<Result> getPhotoDynamic(@Body NetRequestBean netRequestBean);


    /**
     * 微信登录
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=1&a=0&b=0")
    Observable<Result> wxLogin(@Body NetRequestBean netRequestBean);

    /**
     * 手机号登录
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=2&a=0&b=0")
    Observable<Result> login(@Body NetRequestBean netRequestBean);


    /**
     * 删除动态
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=22&a=0&b=0")
    Observable<Result> deleteDynamic(@Body NetRequestBean netRequestBean);

    /**
     * 置顶动态
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=23&a=0&b=0")
    Observable<Result> stitckDynamic(@Body NetRequestBean netRequestBean);

    /**
     * 查询标签
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=33&a=0&b=0")
    Observable<Result> findLabel(@Body NetRequestBean netRequestBean);


    /**
     * 新增标签
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=11&a=0&b=0")
    Observable<Result> createLabel(@Body NetRequestBean netRequestBean);

    /**
     * 删除标签
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=14&a=0&b=0")
    Observable<Result> deleteLabel(@Body NetRequestBean netRequestBean);

    /**
     * 修改标签
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=12&a=0&b=0")
    Observable<Result> updateLabel(@Body NetRequestBean netRequestBean);

    /**
     * 新增动态
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=3&a=0&b=0")
    Observable<Result> createDynamic(@Body NetRequestBean netRequestBean);

    /**
     * 转发别人动态
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=10&a=0&b=0")
    Observable<Result> repeatDynamic(@Body NetRequestBean netRequestBean);

    /**
     * 编辑相册动态
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=4&a=0&b=0")
    Observable<Result> editPhotoDynamic(@Body NetRequestBean netRequestBean);


    /**
     * 获取Cos上传的临时秘钥
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=21&a=0&b=0")
    Call<Result> getTemporarySecretKey(@Body NetRequestBean netRequestBean);


    /**
     * 获取朋友圈列表
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=19&a=0&b=0")
    Observable<Result> getFriendCircleList(@Body NetRequestBean netRequestBean);


    /**
     * 删除朋友圈
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=8&a=0&b=0")
    Observable<Result> deleteFriendCircle(@Body NetRequestBean netRequestBean);

    /**
     * 获取动态列表
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=25&a=0&b=0")
    Observable<Result> getDynamicList(@Body NetRequestBean netRequestBean);

    /**
     * 编辑朋友圈
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=9&a=0&b=0")
    Observable<Result> editFriendCircle(@Body NetRequestBean netRequestBean);


    /**
     * 新增朋友圈
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=7&a=0&b=0")
    Observable<Result> addFriendCircleDynamic(@Body NetRequestBean netRequestBean);

    /**
     * 编辑朋友圈界面进入时获取的原数据
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=18&a=0&b=0")
    Observable<Result> getFriendCircle(@Body NetRequestBean netRequestBean);
}
