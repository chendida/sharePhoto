package com.zq.dynamicphoto.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.zq.dynamicphoto.adapter.DynamicListAdapter;
import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.bean.UserRelation;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.presenter.DynamicLoadPresenter;
import com.zq.dynamicphoto.ui.SettingPermissionActivity;
import com.zq.dynamicphoto.ui.widge.DynamicDialog;
import com.zq.dynamicphoto.ui.widge.SelectDialog;
import com.zq.dynamicphoto.ui.widge.ShareDialog;
import com.zq.dynamicphoto.utils.CosUtils;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.ImageSaveUtils;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.ShareUtils;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.BgUpdate;
import com.zq.dynamicphoto.view.DynamicDelete;
import com.zq.dynamicphoto.view.IDynamicView;
import com.zq.dynamicphoto.view.UploadView;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import static android.app.Activity.RESULT_OK;

/**
 * 动态列表界面
 */
public class DynamicFragment extends BaseFragment<IDynamicView,DynamicLoadPresenter<IDynamicView>>
        implements IDynamicView,DynamicListAdapter.MyClickListener,
        ImageSaveUtils.DownLoadListener,UploadView{

    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.refresh_Layout)
    SmartRefreshLayout refreshLayout;
    private int pager = 1;
    int pagerCount = 1;//总页码数
    ArrayList<Dynamic> dynamicsList;
    DynamicListAdapter mAdapter;
    private DynamicDialog dynamicDialog;
    private ShareDialog shareDialog;
    private DynamicDelete listener;
    private int positon;
    private ImageView ivBg;
    private String imageUrl;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void initView(View view) {
        if (dynamicsList == null) {
            dynamicsList = new ArrayList<>();
        }
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rcl.setLayoutManager(manager);
        mAdapter = new DynamicListAdapter(getActivity(), dynamicsList,this);
        rcl.setAdapter(mAdapter);
        rcl.setNestedScrollingEnabled(false);
        rcl.setHasFixedSize(true);
        setListener();
    }

    private void setListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pager = 1;
                getDynamicList(pager);//上拉刷新
                refreshlayout.finishRefresh(600);
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pager++;
                if (pagerCount >= pager) {
                    getDynamicList(pager);
                }
                refreshlayout.finishLoadmore(1000);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected DynamicLoadPresenter<IDynamicView> createPresenter() {
        return new DynamicLoadPresenter<>();
    }

    @Override
    protected void loadData() {
        getDynamicList(pager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dynamicDialog != null){
            if (dynamicDialog.isShowing()){
                dynamicDialog.dismiss();
                dynamicDialog = null;
            }else {
                dynamicDialog = null;
            }
        }
        if (shareDialog != null){
            if (shareDialog.isShowing()){
                shareDialog.dismiss();
                shareDialog = null;
            }else {
                shareDialog = null;
            }
        }
        ImageSaveUtils.getInstance(this).clearListener();
        mAdapter.clear();
        mAdapter = null;
    }

    @Override
    public void showData(Result result) {
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
            dynamicsList = new Gson().fromJson(jsonObject.optString("dynamicList"), new TypeToken<List<Dynamic>>() {
            }.getType());
            Log.i("dynamicList", "size = " + dynamicsList.size());
            if (dynamicsList.size() != 0) {
                if (pager == 1) {
                    mAdapter.initDynamicList(dynamicsList);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mAdapter.addDynamicList(dynamicsList);
                }
            } else {
                if (pager == 1) {
                    mAdapter.initDynamicList(dynamicsList);
                    mAdapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDynamicList(int pager){
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        UserRelation userRelation = new UserRelation();
        userRelation.setIuserId(userId);
        userRelation.setPage(pager);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setUserRelation(userRelation);
        if (mPresenter != null) {
            mPresenter.fetch(netRequestBean);
        }
    }

    @Override
    public void clickListener(View view, int position, NetRequestBean netRequestBean,
                              DynamicDelete listener) {
        int userId = SharedPreferencesUtils.getInstance().getInt(Constans.USERID, 0);
        switch (view.getId()){
            case R.id.tv_delete:
                this.listener = listener;
                this.positon = position;
                deleteDynamic(netRequestBean);
                break;
            case R.id.tv_stick:
                stickDynamic(netRequestBean);
                break;
            case R.id.tv_all_save:
                Dynamic dynamic = netRequestBean.getDynamic();
                if (dynamic != null){
                    showLoading();
                    ImageSaveUtils.getInstance(this).saveAll(dynamic);
                }
                break;
            case R.id.iv_avatar:
                showSelectDialog(netRequestBean.getDynamic());
                break;
            case R.id.layout_one_key_share:
                showShareDialog(netRequestBean.getDynamic());
                break;
            case  R.id.layout_search:
                MFGT.gotoSearchActivity(getActivity());
                break;
            case R.id.iv_my_avatar:
                MFGT.gotoHtmlPhotoDetailsActivity(getActivity(),"friends.html?userId="+
                               userId,
                        getResources().getString(R.string.tv_photo_details),
                        userId);
                break;
            case R.id.layout_share_my_photo:
                MFGT.gotoMyTwoCodeActivity(getActivity());
                /*MFGT.gotoHtmlManagerActivity(getActivity(),"share.html?userid="+userId,
                        getResources().getString(R.string.photo_two_code),1);*/
                break;
            case R.id.iv_bg:
                ivBg = (ImageView) view;
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
                                CosUtils.getInstance(DynamicFragment.this).uploadToCos(outfile,7);
                            }
                        });
            }
        }).start();
    }


    private void showShareDialog(final Dynamic dynamic) {
        shareDialog = new ShareDialog(getActivity(), R.style.dialog, new ShareDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                switch (position) {
                    case 1://分享给好友
                        ShareUtils.getInstance().shareFriend(dynamic,1,getActivity());
                        break;
                    case 2://分享给微信朋友圈
                        ShareUtils.getInstance().shareFriend(dynamic,2,getActivity());
                        break;
                    case 3://批量保存
                        if (dynamic != null){
                            showLoading();
                            ImageSaveUtils.getInstance(DynamicFragment.this).saveAll(dynamic);
                        }
                        break;
                }
            }
        });
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            ToastUtils.showShort(getResources().getString(R.string.have_no_wx));
        }else {
            shareDialog.show();
        }
    }

    private void showSelectDialog(final Dynamic dynamic) {
        dynamicDialog = new DynamicDialog(getActivity(), R.style.dialog, new DynamicDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                switch (position) {
                    case 1://进入相册
                        MFGT.gotoHtmlPhotoDetailsActivity(getActivity(),"friends.html?userId="+
                                        dynamic.getUserId(),
                                getResources().getString(R.string.tv_photo_details),
                                dynamic.getUserId());
                        break;
                    case 2://设置权限
                        startActivity(new Intent(getActivity(),
                                SettingPermissionActivity.class)
                                .putExtra(Constans.USERID,dynamic.getUserId()));
                        break;
                    case 3://投诉
                        MFGT.gotoHtmlManagerActivity(getActivity(),"feedback.html?userId="+dynamic.getUserId(),
                                getResources().getString(R.string.tv_feedback));
                        break;
                }
            }
        });
        dynamicDialog.show();
    }

    /**
     * 置顶动态
     * @param netRequestBean
     */
    private void stickDynamic(NetRequestBean netRequestBean) {
        if (mPresenter != null)
            mPresenter.fetchStickDynamic(netRequestBean);
    }

    /**
     * 删除动态
     * @param netRequestBean
     */
    private void deleteDynamic(final NetRequestBean netRequestBean){
        new SelectDialog(getActivity(), R.style.dialog, new SelectDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                if (position == 1){
                    if (mPresenter != null)
                        mPresenter.fetchDeleteDynamic(netRequestBean);
                }
            }
        },"确定删除？").show();
    }

    /**
     * 删除回调
     * @param result
     */
    @Override
    public void showDeleteResult(Result result) {
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

    /**
     * 置顶回调
     * @param result
     */
    @Override
    public void showStickResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                ToastUtils.showShort(getResources().getString(R.string.tv_stick_success));
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

    @Override
    public void showUpdateBgResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                //ToastUtils.showShort(getResources().getString(R.string.tv_stick_success));
                ImageLoaderUtils.displayImg(ivBg,imageUrl);
                SharedPreferences sp = SharedPreferencesUtils.getInstance();
                sp.edit().putString(Constans.BGIMAGE,imageUrl).commit();
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

    @Override
    public void callBack(int code,String msg) {
        hideLoading();
        ToastUtils.showShort(msg);
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
}
