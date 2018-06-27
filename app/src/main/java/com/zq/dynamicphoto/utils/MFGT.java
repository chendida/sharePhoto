package com.zq.dynamicphoto.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.Moments;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.fragment.FriendCircleFragment;
import com.zq.dynamicphoto.fragment.MineFragment;
import com.zq.dynamicphoto.ui.AboutAppActivity;
import com.zq.dynamicphoto.ui.AddFriendCircleActivity;
import com.zq.dynamicphoto.ui.AddLabelActivity;
import com.zq.dynamicphoto.ui.AddPicActivity;
import com.zq.dynamicphoto.ui.DynamicDetailsActivity;
import com.zq.dynamicphoto.ui.DynamicSelectActivity;
import com.zq.dynamicphoto.ui.EditDynamicActivity;
import com.zq.dynamicphoto.ui.HtmlManagerActivity;
import com.zq.dynamicphoto.ui.HtmlPhotoDetailsActivity;
import com.zq.dynamicphoto.ui.LabelManagerActivity;
import com.zq.dynamicphoto.ui.MyFansActivity;
import com.zq.dynamicphoto.ui.MyFollowsActivity;
import com.zq.dynamicphoto.ui.ProblemAndFeedbackActivity;

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
}
