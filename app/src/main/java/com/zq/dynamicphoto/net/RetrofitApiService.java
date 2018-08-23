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
     * 查询标签
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=13&a=0&b=0")
    Observable<Result> getLabelList(@Body NetRequestBean netRequestBean);


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

    /**
     * 获取已关注列表
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=16&a=0&b=0")
    Observable<Result> getFollowList(@Body NetRequestBean netRequestBean);

    /**
     * 查看权限状态
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=29&a=0&b=0")
    Observable<Result> checkPermissionStatus(@Body NetRequestBean netRequestBean);

    /**
     * 设置打开查看动态权限
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=17&a=0&b=0")
    Observable<Result> setPerssion(@Body NetRequestBean netRequestBean);

    /**
     * 取消权限设置
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=28&a=0&b=0")
    Observable<Result> cancelSetPerssion(@Body NetRequestBean netRequestBean);

    /**
     * 修改密码
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=20&a=0&b=0")
    Observable<Result> updatePwd(@Body NetRequestBean netRequestBean);

    /**
     * 获取验证码
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=30&a=0&b=0")
    Observable<Result> getAuthCode(@Body NetRequestBean netRequestBean);

    /**
     * 绑定手机号
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=15&a=0&b=0")
    Observable<Result> bindPhone(@Body NetRequestBean netRequestBean);

    /**
     * 获取相册信息
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=26&a=0&b=0")
    Observable<Result> getPhotoInfo(@Body NetRequestBean netRequestBean);


    /**
     * 编辑相册信息
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=27&a=0&b=0")
    Observable<Result> editPhotoInfo(@Body NetRequestBean netRequestBean);

    /**
     * 获取全部水印模板
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=81&a=0&b=0")
    Observable<Result> getWaterMouldList(@Body NetRequestBean netRequestBean);

    /**
     * 获取单个类型的水印模板
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=85&a=0&b=0")
    Observable<Result> getSingleMouldList(@Body NetRequestBean netRequestBean);

    /**
     * 获取用户用过的水印模板列表
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=83&a=0&b=0")
    Observable<Result> getAddMouldList(@Body NetRequestBean netRequestBean);

    /**
     * 添加用户水印
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=82&a=0&b=0")
    Observable<Result> addWaterMould(@Body NetRequestBean netRequestBean);


    /**
     * 删除用户水印
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=84&a=0&b=0")
    Observable<Result> deleteWaterMould(@Body NetRequestBean netRequestBean);

    /**
     *  获取订单数量和余额
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=54&a=0&b=0")
    Observable<Result> getOrdersNumAndBalance(@Body NetRequestBean netRequestBean);

    /**
     * 修改订单状态
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=43&a=0&b=0")
    Observable<Result> updateOrderStatus(@Body NetRequestBean netRequestBean);


    /**
     * 查询所有订单
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=46&a=0&b=0")
    Observable<Result> getOrdersList(@Body NetRequestBean netRequestBean);

    /**
     * 获取直播间产品选择列表
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=47&a=0&b=0")
    Observable<Result> getLiveGoodsList(@Body NetRequestBean netRequestBean);

    /**
     * 上传直播间产品
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=40&a=0&b=0")
    Observable<Result> uploadLiveGoodsList(@Body NetRequestBean netRequestBean);

    /**
     *  查询用户消费清单
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=70&a=0&b=0")
    Observable<Result> getUserConsumptionList(@Body NetRequestBean netRequestBean);

    /**
     * 获取计费点
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=51&a=0&b=0")
    Observable<Result> getChargeCode(@Body NetRequestBean netRequestBean);


    /**
     *  获取订单id
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=52&a=0&b=0")
    Observable<Result> getOrderId(@Body NetRequestBean netRequestBean);

    /**
     * 动态搜索
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=34&a=0&b=0")
    Observable<Result> serchDynamic(@Body NetRequestBean netRequestBean);

    /**
     * 修改背景
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=35&a=0&b=0")
    Observable<Result> updateBg(@Body NetRequestBean netRequestBean);

    /**
     * 创建直播间
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=36&a=0&b=0")
    Observable<Result> createLiveRoom(@Body NetRequestBean netRequestBean);


    /**
     * 获取直播间信息
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=50&a=0&b=0")
    Observable<Result> getLiveInfo(@Body NetRequestBean netRequestBean);

    /**
     * 获取当前vip状态
     * @param netRequestBean
     * @return
     */
    @POST("sdkData.shtml?requestId=91&a=0&b=0")
    Observable<Result> getVipStatus(@Body NetRequestBean netRequestBean);
}
