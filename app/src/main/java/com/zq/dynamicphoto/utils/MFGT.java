package com.zq.dynamicphoto.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.Folder;
import com.zq.dynamicphoto.bean.Image;
import com.zq.dynamicphoto.bean.Moments;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.mylive.bean.NewLiveRoom;
import com.zq.dynamicphoto.mylive.ui.AccountRechargeActivity;
import com.zq.dynamicphoto.mylive.ui.AddLiveGoodActivity;
import com.zq.dynamicphoto.mylive.ui.ConsumptionListActivity;
import com.zq.dynamicphoto.mylive.ui.LiveActivity;
import com.zq.dynamicphoto.mylive.ui.MyOrdersActivity;
import com.zq.dynamicphoto.mylive.ui.OpenLiveActivity;
import com.zq.dynamicphoto.ui.AboutAppActivity;
import com.zq.dynamicphoto.ui.AddFriendCircleActivity;
import com.zq.dynamicphoto.ui.AddLabelActivity;
import com.zq.dynamicphoto.ui.AddPicActivity;
import com.zq.dynamicphoto.ui.DynamicDetailsActivity;
import com.zq.dynamicphoto.ui.DynamicSelectActivity;
import com.zq.dynamicphoto.ui.EditDynamicActivity;
import com.zq.dynamicphoto.ui.EditWaterActivity;
import com.zq.dynamicphoto.ui.HtmlManagerActivity;
import com.zq.dynamicphoto.ui.HtmlPhotoDetailsActivity;
import com.zq.dynamicphoto.ui.LabelManagerActivity;
import com.zq.dynamicphoto.ui.MoneyWatermarkActivity;
import com.zq.dynamicphoto.ui.MyFansActivity;
import com.zq.dynamicphoto.ui.MyFollowsActivity;
import com.zq.dynamicphoto.ui.PhotoInfoActivity;
import com.zq.dynamicphoto.ui.PhotoListActivity;
import com.zq.dynamicphoto.ui.PhotoSelectActivity;
import com.zq.dynamicphoto.ui.ProblemAndFeedbackActivity;
import com.zq.dynamicphoto.ui.RecommendActivity;
import com.zq.dynamicphoto.ui.ResetPwdActivity;
import com.zq.dynamicphoto.ui.UploadWaterAvatarActivity;
import com.zq.dynamicphoto.ui.VideoPlayActivity;
import com.zq.dynamicphoto.ui.WaterStyleActivity;
import com.zq.dynamicphoto.ui.Watermark5009Activity;
import com.zq.dynamicphoto.ui.WatermarkActivity;
import com.zq.dynamicphoto.view.UploadView;

import java.util.ArrayList;

/**
 * 界面跳转工具类
 * Created by Administrator on 2018/6/8.
 */

public class MFGT {
    public static void startActivity(Activity activity, Class cls){
        activity.startActivity(new Intent(activity,cls));
    }

    public static void gotoAddPicActivity(Activity activity){
        activity.startActivity(new Intent(activity, AddPicActivity.class));
    }

    public static void gotoAddLabelActivity(Activity activity){
        activity.startActivity(new Intent(activity, AddLabelActivity.class));
    }

    public static void gotoLabelManagerActivity(Activity activity) {
        activity.startActivity(new Intent(activity, LabelManagerActivity.class));
    }

    public static void gotoEditDynamicActivity(Context context,Dynamic dynamic) {
        context.startActivity(new Intent(context, EditDynamicActivity.class)
        .putExtra(Constans.DYNAMIC,dynamic));
    }

    public static void gotoHtmlManagerActivity(Activity activity, String agreement,String title) {
        activity.startActivity(new Intent(activity, HtmlManagerActivity.class)
        .putExtra(Constans.HTML,agreement)
        .putExtra(Constans.HTML_TITLE,title));
    }

    public static void gotoDynamicDetailsActivity(Activity activity, String agreement,String title) {
        activity.startActivity(new Intent(activity, DynamicDetailsActivity.class)
                .putExtra(Constans.HTML,agreement)
                .putExtra(Constans.HTML_TITLE,title));
    }

    public static void gotoHtmlManagerActivity(Activity activity, String agreement,String title,int flag) {
        activity.startActivity(new Intent(activity, HtmlManagerActivity.class)
                .putExtra(Constans.HTML,agreement)
                .putExtra(Constans.HTML_TITLE,title)
                .putExtra(Constans.FLAG,flag));
    }

    public static void gotoAddFriendCircleActivity(Activity activity) {
        activity.startActivity(new Intent(activity, AddFriendCircleActivity.class));
    }

    public static void gotoDynamicSelectActivity(Activity activity,int requestCode) {
        activity.startActivityForResult(new Intent(activity,DynamicSelectActivity.class),requestCode);
    }

    public static void gotoDynamicSelectActivity(Activity activity,int requestCode,Integer momentsId) {
        activity.startActivityForResult(new Intent(activity,DynamicSelectActivity.class)
                .putExtra(Constans.MOMENTS_ID,momentsId),requestCode);
    }

    public static void gotoAddFriendCircleActivity(Activity activity, Moments moments) {
        activity.startActivity(new Intent(activity, AddFriendCircleActivity.class)
            .putExtra(Constans.MOMENTS,moments));
    }

    public static void gotoMyFollowActivity(Activity activity) {
        activity.startActivity(new Intent(activity, MyFollowsActivity.class));
    }

    public static void gotoHtmlPhotoDetailsActivity(Context mContext,String url,String title,Integer userId) {
        mContext.startActivity(new Intent(mContext, HtmlPhotoDetailsActivity.class)
                .putExtra(Constans.HTML,url)
                .putExtra(Constans.HTML_TITLE,title)
                .putExtra(Constans.USERID,userId));
    }

    public static void gotoMyFansActivity(Activity activity) {
        activity.startActivity(new Intent(activity, MyFansActivity.class));
    }

    public static void gotoProblemAndFeedbackActivity(Activity activity) {
        activity.startActivity(new Intent(activity, ProblemAndFeedbackActivity.class));
    }

    public static void gotoAboutAppActivity(Activity activity) {
        activity.startActivity(new Intent(activity, AboutAppActivity.class));
    }

    public static void gotoResetPwdActivity(Activity activity, boolean isBind) {
        activity.startActivity(new Intent(activity, ResetPwdActivity.class)
        .putExtra(Constans.ISBIND,isBind));
    }

    public static void gotoPhotoInfoActivity(Activity activity) {
        activity.startActivity(new Intent(activity, PhotoInfoActivity.class));
    }

    public static void gotoVideoPlayActivity(Context activity, String path) {
        activity.startActivity(new Intent(activity, VideoPlayActivity.class)
        .putExtra(Constans.VIDEO_URL,path));
    }

    public static void gotoPhotoListActivity(Activity activity) {
        activity.startActivity(new Intent(activity, PhotoListActivity.class));
    }

    public static void gotoWaterPhotoListActivity(Context context, Folder imageBucket) {
        context.startActivity(new Intent(context, PhotoSelectActivity.class)
                .putExtra(Constans.IMAGEBUCKET,imageBucket));
    }

    public static void gotoWatermarkActivity(Context context, ArrayList<Image>list) {
        context.startActivity(new Intent(context, WatermarkActivity.class)
                .putExtra(Constans.SELECT_LIST,list));
    }

    public static void gotoWaterStyleActivity(Context context) {
        context.startActivity(new Intent(context, WaterStyleActivity.class));
    }

    public static void gotoMyOrdersActivity(Context context) {
        context.startActivity(new Intent(context, MyOrdersActivity.class));
    }

    public static void gotoAddLiveGoodActivity(Activity context) {
        context.startActivity(new Intent(context, AddLiveGoodActivity.class));
    }

    public static void gotoConsumptionListActivity(Activity context) {
        context.startActivity(new Intent(context, ConsumptionListActivity.class));
    }

    public static void gotoAccountRechargeActivity(Activity context) {
        context.startActivity(new Intent(context, AccountRechargeActivity.class));
    }

    public static void gotoOpenLiveActivity(Activity activity) {
        activity.startActivity(new Intent(activity, OpenLiveActivity.class));
    }

    public static void gotoLiveActivity(OpenLiveActivity activity, NewLiveRoom newLiveRoom,
                                        UserInfo userInfo, boolean flag) {
        activity.startActivity(new Intent(activity,LiveActivity.class)
                .putExtra(Constans.NEW_LIVE_ROOM,newLiveRoom)
                .putExtra(Constans.USERINFO,userInfo)
                .putExtra(Constans.ISANCHOR,flag));
    }

    public static void gotoEditWaterActivity(Activity activity,String watermarkId) {
        activity.startActivity(new Intent(activity, EditWaterActivity.class)
                .putExtra(Constans.WATERMARKID,watermarkId));
        //activity.finish();
    }

    public static void gotoEditWatermark5009Activity(Activity activity,String watermarkId) {
        activity.startActivity(new Intent(activity, Watermark5009Activity.class)
                .putExtra(Constans.WATERMARKID,watermarkId));
    }

    public static void gotoMoneyWatermarkActivity(Activity activity, String watermarkId) {
        activity.startActivity(new Intent(activity, MoneyWatermarkActivity.class)
                .putExtra(Constans.WATERMARKID,watermarkId));
    }

    public static void gotoRecommendActivity(Activity activity, String watermarkId) {
        activity.startActivity(new Intent(activity, RecommendActivity.class)
                .putExtra(Constans.WATERMARKID,watermarkId));
    }

    public static void gotoUploadWaterAvatarActivity(Activity activity, String watermarkId,Boolean isChange,
                                                     int type,String avatarPath) {
        activity.startActivityForResult(new Intent(activity, UploadWaterAvatarActivity.class)
                .putExtra(Constans.WATERMARKID,watermarkId)
                .putExtra(Constans.AVATAR_CHANGE,isChange)
                .putExtra(Constans.FRAME_TYPE,type)
                .putExtra(Constans.AVATAR_PATH,avatarPath)
                ,Constans.REQUEST_CODE);
    }
}
