package com.zq.dynamicphoto.net;

import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 网络请求
 * Created by Administrator on 2018/5/24.
 */

public interface RetrofitApiService {
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
    @POST("sdkData.shtml?requestId=13&a=0&b=0")
    Observable<Result> findLabel(@Body NetRequestBean netRequestBean);


    /**
     * 新增标签
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=11&a=0&b=0")
    Observable<Result> createLabel(@Body NetRequestBean netRequestBean);
}
