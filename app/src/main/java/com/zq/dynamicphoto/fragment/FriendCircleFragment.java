package com.zq.dynamicphoto.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.FriendCircleAdapter;
import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.Moments;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.presenter.MomentOperatePresenter;
import com.zq.dynamicphoto.ui.widge.ShareWxDialog;
import com.zq.dynamicphoto.utils.CosUtils;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.ImageSaveUtils;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.ShareUtils;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.BgUpdate;
import com.zq.dynamicphoto.view.DynamicDelete;
import com.zq.dynamicphoto.view.IFriendCircleView;
import com.zq.dynamicphoto.view.UploadView;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * 朋友圈
 */
public class FriendCircleFragment extends BaseFragment<IFriendCircleView,
        MomentOperatePresenter<IFriendCircleView>> implements IFriendCircleView,
        FriendCircleAdapter.MyClickListener,UploadView{
    private static final String TAG = "FriendCircleFragment";
    @BindView(R.id.rcl_friend_circle_list)
    RecyclerView rclFriendCircleList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int pager = 1;
    int pagerCount = 1;//总页码数
    FriendCircleAdapter mAdapter;
    ArrayList<Moments> friendCircleList = new ArrayList<>();
    private ShareWxDialog shareWxDialog;
    private static BgUpdate bgListener;
    private ImageView ivBg;
    private String imageUrl;
    private DynamicDelete listener;
    private int positon;
    //创建注册回调的函数
    public static void setOnDataListener(BgUpdate listener){
        //将参数赋值给接口类型的成员变量
        bgListener = listener;
    }

    @Override
    public void onUploadProcess(int percent) {

    }

    @Override
    public void onUploadResult(int code, String url) {
        hideLoading();
        if (code == Constans.REQUEST_OK){
            imageUrl = url;
            updateBg();
        }else {
            ToastUtils.showShort(getResources().getString(R.string.upload_fail));
        }
    }

    private void updateBg() {
        DeviceProperties dr = DrUtils.getInstance();
        final SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setBgImage(imageUrl);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setUserInfo(userInfo);
        if (mPresenter != null){
            mPresenter.updateBg(netRequestBean);
        }
    }

    //用于实现回调的类,实现的是处理回调的接口,并实现接口里面的函数
    class OnDataListener implements BgUpdate{
        //实现接口中处理数据的函数,只要右边的Fragment调用onData函数,这里就会收到传递的数据
        @Override
        public void onBgUpdate(String url) {
            pager = 1;
            getFriendCircleList(pager);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friend_circle;
    }

    @Override
    protected void initView(View view) {
        DynamicFragment.setOnDataListener(new OnDataListener());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rclFriendCircleList.setLayoutManager(manager);
        mAdapter = new FriendCircleAdapter(getActivity(), friendCircleList,this,getActivity());
        rclFriendCircleList.setAdapter(mAdapter);
        rclFriendCircleList.setNestedScrollingEnabled(false);
        rclFriendCircleList.setHasFixedSize(true);

        setListener();
    }

    private void setListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pager = 1;
                getFriendCircleList(pager);//上拉刷新
                refreshlayout.finishRefresh(600);
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pager++;
                if (pagerCount >= pager) {
                    getFriendCircleList(pager);
                } else {
                    ToastUtils.showShort("没有更多数据");
                }
                refreshlayout.finishLoadmore(1000);
            }
        });
    }

    private void getFriendCircleList(int pager) {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        Moments moments = new Moments();
        moments.setUserId(userId);
        moments.setPage(pager);
        moments.setTitle(mAdapter.getEtSearchContent());
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setMoments(moments);
        if (mPresenter != null){
            mPresenter.getMomentList(netRequestBean);
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    protected MomentOperatePresenter<IFriendCircleView> createPresenter() {
        return new MomentOperatePresenter<>();
    }

    @Override
    protected void loadData() {
        pager = 1;
        getFriendCircleList(pager);
    }

    @Override
    public void getMomentListResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                dealWithResult(result);
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

    private void dealWithResult(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            pagerCount = jsonObject.optInt("pageCount", 1);
            UserInfo userInfo = new Gson().fromJson(jsonObject.optString("userInfo"),UserInfo.class);
            friendCircleList = new Gson().fromJson(jsonObject.optString("momentsList"), new TypeToken<List<Moments>>() {
            }.getType());
            Log.i("momentsList", "size = " + friendCircleList.size());
            if (friendCircleList.size() != 0) {
                if (pager == 1){
                    mAdapter.initMomentsList(friendCircleList);
                    mAdapter.notifyDataSetChanged();
                }else {
                    mAdapter.addMomentsList(friendCircleList);
                }
            }else {
                if (pager == 1){
                    mAdapter.initMomentsList(friendCircleList);
                    mAdapter.notifyDataSetChanged();
                }
            }
            if (userInfo != null){
                mAdapter.setUserInfo(userInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMomentResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                ToastUtils.showShort(getResources().getString(R.string.tv_delete_success));
                if (listener != null){
                    listener.deleteSuccess(positon);
                }
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

    @Override
    public void updateBg(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                ImageLoaderUtils.displayImg(ivBg,imageUrl);
                SharedPreferences sp = SharedPreferencesUtils.getInstance();
                sp.edit().putString(Constans.BGIMAGE,imageUrl).commit();
                bgListener.onBgUpdate(imageUrl);
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shareWxDialog != null){
            if (shareWxDialog.isShowing()){
                shareWxDialog.dismiss();
                shareWxDialog = null;
            }else {
                shareWxDialog = null;
            }
        }
    }

    @Override
    public void clickListener(View v, Moments moments,int positon,DynamicDelete listener) {
        switch (v.getId()){
            case R.id.tv_delete:
                this.listener = listener;
                this.positon = positon;
                deleteMoment(moments);
                break;
            case R.id.tv_edit:
                MFGT.gotoAddFriendCircleActivity(getActivity(),moments);
                break;
            case R.id.layout_article:
                MFGT.gotoHtmlManagerActivity(getActivity(),"moments.html?id="+moments.getId(),
                        getResources().getString(R.string.tv_friend_circle_details));
                break;
            case R.id.et_search:
                pager = 1;
                getFriendCircleList(pager);
                break;
            case R.id.iv_add_friend_circle:
                MFGT.gotoAddFriendCircleActivity(getActivity());
                break;
            case R.id.layout_one_key_share:
                showShareWxDialog(moments);
                break;
            case R.id.iv_bg:
                ivBg = (ImageView) v;
                intoPicSelect();
                break;
        }
    }

    private void intoPicSelect() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(1)
                .previewImage(true)
                .enableCrop(true)
                .withAspectRatio(16,8)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 接收图片选择器返回结果，更新所选图片集合
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
                    String path = localMedia.get(0).getCutPath();
                    //updatePic(path);
                    //mAdapter.updateBg(path);
                    //ImageLoaderUtils.displayImg(ivBg,path);
                    compossImage(path,7);
                    break;
            }
        }
    }

    /**
     * 压缩并上传图片
     *
     * @param
     */
    private void compossImage(final String path, final int flag) {
        showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Tiny.FileCompressOptions tiny = new Tiny.FileCompressOptions();
                Tiny.getInstance().source(path).asFile().withOptions(tiny)
                        .compress(new FileCallback() {
                            @Override
                            public void callback(boolean isSuccess, String outfile, Throwable t) {
                                CosUtils.getInstance(FriendCircleFragment.this).uploadToCos(outfile,7);
                            }
                        });
            }
        }).start();
    }

    private void showShareWxDialog(final Moments moments) {
        shareWxDialog = new ShareWxDialog(getActivity(), R.style.dialog, new ShareWxDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                SharedPreferences sp = SharedPreferencesUtils.getInstance();
                String forwordUrl = sp.getString(Constans.FORWORDURL, "");
                if (TextUtils.isEmpty(forwordUrl)){
                    forwordUrl = "http://www.redshoping.cn";
                }
                String url = forwordUrl + "/moments.html?id=" + moments.getId();
                switch (position){
                    case 1:
                        ShareUtils.getInstance().shareLink(url,moments.getTitle(),
                                moments.getSignature(),moments.getForwardLogo(),position);
                        break;
                    case 2:
                        ShareUtils.getInstance().shareLink(url,moments.getTitle(),
                                moments.getSignature(),moments.getForwardLogo(),position);
                        break;
                }
            }
        });
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            ToastUtils.showShort(getResources().getString(R.string.have_no_wx));
        }else {
            shareWxDialog.show();
        }
    }

    private void deleteMoment(Moments moments) {
        DeviceProperties dr = DrUtils.getInstance();
        Moments moment = new Moments();
        moment.setId(moments.getId());
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setMoments(moment);
        if (mPresenter != null){
            mPresenter.deleteMoment(netRequestBean);
        }
    }
}
