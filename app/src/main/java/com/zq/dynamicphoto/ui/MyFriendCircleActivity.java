package com.zq.dynamicphoto.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.FriendCircleAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.Moments;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.presenter.MomentOperatePresenter;
import com.zq.dynamicphoto.ui.widge.SelectDialog;
import com.zq.dynamicphoto.ui.widge.ShareWxDialog;
import com.zq.dynamicphoto.utils.CosUtils;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.ShareUtils;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
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
import butterknife.OnClick;

public class MyFriendCircleActivity extends BaseActivity<IFriendCircleView,
        MomentOperatePresenter<IFriendCircleView>> implements IFriendCircleView,
        FriendCircleAdapter.MyClickListener, UploadView {
    private static final String TAG = "MyFriendCircleActivity";
    @BindView(R.id.rcl_friend_circle_list)
    RecyclerView rclFriendCircleList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    private int pager = 1;
    int pagerCount = 1;//总页码数
    FriendCircleAdapter mAdapter;
    ArrayList<Moments> friendCircleList = new ArrayList<>();
    private ShareWxDialog shareWxDialog;
    private ImageView ivBg;
    private String imageUrl;
    private DynamicDelete listener;
    private int positon;

    @Override
    public void onUploadProcess(int percent) {

    }

    @Override
    public void onUploadResult(int code, String url) {
        hideLoading();
        if (code == Constans.REQUEST_OK) {
            imageUrl = url;
            updateBg();
        } else {
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
        if (mPresenter != null) {
            mPresenter.updateBg(netRequestBean);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_friend_circle;
    }

    @Override
    protected void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        layoutBack.setVisibility(View.VISIBLE);
        layoutFinish.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.tv_friend_circle));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rclFriendCircleList.setLayoutManager(manager);
        mAdapter = new FriendCircleAdapter(this, friendCircleList, this);
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
        if (mPresenter != null) {
            mPresenter.getMomentList(netRequestBean);
        }
    }

    @Override
    protected void initData() {
        pager = 1;
        getFriendCircleList(pager);
    }

    @Override
    protected MomentOperatePresenter<IFriendCircleView> createPresenter() {
        return new MomentOperatePresenter<>();
    }


    @Override
    public void getMomentListResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                dealWithResult(result);
            } else {
                showFailed();
            }
        } else {
            showFailed();
        }
    }

    private void dealWithResult(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            pagerCount = jsonObject.optInt("pageCount", 1);
            UserInfo userInfo = new Gson().fromJson(jsonObject.optString("userInfo"), UserInfo.class);
            friendCircleList = new Gson().fromJson(jsonObject.optString("momentsList"), new TypeToken<List<Moments>>() {
            }.getType());
            Log.i("momentsList", "size = " + friendCircleList.size());
            if (friendCircleList.size() != 0) {
                if (pager == 1) {
                    mAdapter.initMomentsList(friendCircleList);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mAdapter.addMomentsList(friendCircleList);
                }
            } else {
                if (pager == 1) {
                    mAdapter.initMomentsList(friendCircleList);
                    mAdapter.notifyDataSetChanged();
                }
            }
            if (userInfo != null) {
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
                if (listener != null) {
                    listener.deleteSuccess(positon);
                }
            } else {
                showFailed();
            }
        } else {
            showFailed();
        }
    }

    @Override
    public void updateBg(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                ImageLoaderUtils.displayImg(ivBg, imageUrl);
                SharedPreferences sp = SharedPreferencesUtils.getInstance();
                sp.edit().putString(Constans.BGIMAGE, imageUrl).commit();
            } else {
                showFailed();
            }
        } else {
            showFailed();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shareWxDialog != null) {
            if (shareWxDialog.isShowing()) {
                shareWxDialog.dismiss();
                shareWxDialog = null;
            } else {
                shareWxDialog = null;
            }
        }
    }

    @Override
    public void clickListener(View v, Moments moments, int positon, DynamicDelete listener) {
        switch (v.getId()) {
            case R.id.tv_delete:
                this.listener = listener;
                this.positon = positon;
                deleteMoment(moments);
                break;
            case R.id.tv_edit:
                MFGT.gotoAddFriendCircleActivity(this, moments);
                break;
            case R.id.layout_article:
                MFGT.gotoHtmlManagerActivity(this, "moments.html?id=" + moments.getId(),
                        getResources().getString(R.string.tv_friend_circle_details));
                break;
            case R.id.et_search:
                pager = 1;
                getFriendCircleList(pager);
                break;
            case R.id.iv_add_friend_circle:
                MFGT.gotoAddFriendCircleActivity(this);
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
                .withAspectRatio(16, 8)
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
                    compossImage(path, 7);
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
                                CosUtils.getInstance(MyFriendCircleActivity.this).uploadToCos(outfile, 7);
                            }
                        });
            }
        }).start();
    }

    private void showShareWxDialog(final Moments moments) {
        shareWxDialog = new ShareWxDialog(this, R.style.dialog, new ShareWxDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                SharedPreferences sp = SharedPreferencesUtils.getInstance();
                String forwordUrl = sp.getString(Constans.FORWORDURL, "");
                if (TextUtils.isEmpty(forwordUrl)) {
                    forwordUrl = "http://www.redshoping.cn";
                }
                String url = forwordUrl + "/moments.html?id=" + moments.getId();
                switch (position) {
                    case 1:
                        ShareUtils.getInstance().shareLink(url, moments.getTitle(),
                                moments.getSignature(), moments.getForwardLogo(), position);
                        break;
                    case 2:
                        ShareUtils.getInstance().shareLink(url, moments.getTitle(),
                                moments.getSignature(), moments.getForwardLogo(), position);
                        break;
                }
            }
        });
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            ToastUtils.showShort(getResources().getString(R.string.have_no_wx));
        } else {
            shareWxDialog.show();
        }
    }

    private void deleteMoment(final Moments moments) {
        new SelectDialog(this, R.style.dialog, new SelectDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                if (position == 1) {
                    DeviceProperties dr = DrUtils.getInstance();
                    Moments moment = new Moments();
                    moment.setId(moments.getId());
                    NetRequestBean netRequestBean = new NetRequestBean();
                    netRequestBean.setDeviceProperties(dr);
                    netRequestBean.setMoments(moment);
                    if (mPresenter != null) {
                        mPresenter.deleteMoment(netRequestBean);
                    }
                }
            }
        }, "确定删除？").show();
    }

    @OnClick(R.id.layout_back)
    public void onViewClicked() {
        finish();
    }
}
