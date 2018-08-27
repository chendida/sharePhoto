package com.zq.dynamicphoto.mylive.ui;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.User;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.mylive.bean.LiveRoom;
import com.zq.dynamicphoto.mylive.bean.MessageEvent;
import com.zq.dynamicphoto.mylive.bean.NewLiveRoom;
import com.zq.dynamicphoto.mylive.tools.PushUtil;
import com.zq.dynamicphoto.presenter.OpenLivePresenter;
import com.zq.dynamicphoto.ui.widge.InputDialog;
import com.zq.dynamicphoto.ui.widge.TextHintDialog;
import com.zq.dynamicphoto.utils.CosUtils;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.IOpenLiveView;
import com.zq.dynamicphoto.view.UploadView;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileBatchCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.OnClick;

public class OpenLiveActivity extends BaseActivity<IOpenLiveView,
        OpenLivePresenter<IOpenLiveView>> implements UploadView,IOpenLiveView{

    private static final String TAG = "OpenLiveActivity";
    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.iv_anchor_cover)
    ImageView ivAnchorCover;
    InputDialog inputDialog;
    @BindView(R.id.tv_start_fans)
    TextView tvStartFans;
    @BindView(R.id.tv_add_fans)
    TextView tvAddFans;
    @BindView(R.id.et_live_title)
    EditText etLiveTitle;
    @BindView(R.id.et_live_phone)
    EditText etLivePhone;
    @BindView(R.id.et_live_wx)
    EditText etLiveWx;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.check_clause)
    CheckBox checkClause;
    private int flag = 0;
    Dialog dialogUtils;
    private String cover;
    TextHintDialog anchorExitDialog;
    String unionId = "";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_live;
    }

    @Override
    protected void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        layoutBack.setVisibility(View.VISIBLE);
        layoutFinish.setVisibility(View.GONE);
        tvTitle.setText(getString(R.string.release_live));
    }

    @Override
    protected void initData() {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        LiveRoom liveRoom = new LiveRoom();
        liveRoom.setUserId(userId);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setLiveRoom(liveRoom);
        if (mPresenter != null){
            mPresenter.getLiveRoomInfo(netRequestBean);
        }
    }

    @Override
    protected OpenLivePresenter<IOpenLiveView> createPresenter() {
        return new OpenLivePresenter<>();
    }

    private InputDialog.OnItemClickListener clickListener = new InputDialog.OnItemClickListener() {
        @Override
        public void onClick(Dialog dialog, int position) {
            dialog.dismiss();
            switch (position) {
                case 1:
                    String labelName = inputDialog.et_label.getText().toString();
                    if (flag == 0) {
                        int i = Integer.parseInt(labelName);
                        if (i > 100) {
                            Toast.makeText(OpenLiveActivity.this, "初始人气最多设置为100", Toast.LENGTH_SHORT).show();
                            tvStartFans.setText(String.valueOf(100));
                            return;
                        }
                        tvStartFans.setText(labelName);
                    } else {
                        int i = Integer.parseInt(labelName);
                        if (i > 10) {
                            Toast.makeText(OpenLiveActivity.this, "递增人气最多设置为10", Toast.LENGTH_SHORT).show();
                            tvAddFans.setText(String.valueOf(10));
                            return;
                        }
                        tvAddFans.setText(labelName);
                    }
                    break;
            }
        }
    };


    @OnClick({R.id.layout_back, R.id.layout_anchor_cover, R.id.layout_start_fans, R.id.layout_add_fans,
            R.id.btn_open_live,R.id.tv_about_clause})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_about_clause:
                MFGT.gotoHtmlManagerActivity(this,"userProtocol.html",getResources().getString(R.string.user_privacy_protocol));
                break;
            case R.id.btn_open_live:
                if (checkClause.isChecked()) {
                    if (isAll()) {
                        openLive();
                    }
                }else {
                    ToastUtils.showShort("请阅读并同意主播开播协议");
                }
                break;
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_anchor_cover:
                intoPicSelect();
                break;
            case R.id.layout_start_fans:
                flag = 0;
                showInputDialog(getResources().getString(R.string.please_input_start_fans));
                break;
            case R.id.layout_add_fans:
                flag = 1;
                showInputDialog(getResources().getString(R.string.please_input_add_fans));
                break;
        }
    }

    private void openLive() {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        unionId = sp.getString(Constans.UNIONID, "");
        int userId = sp.getInt(Constans.USERID, 0);
        User user = new User();
        user.setUnionId(unionId);
        LiveRoom liveRoom = new LiveRoom();
        liveRoom.setUserId(userId);
        liveRoom.setCover(cover);
        liveRoom.setPhone(etLivePhone.getText().toString());
        liveRoom.setWx(etLiveWx.getText().toString());
        liveRoom.setContent(etDescription.getText().toString());
        liveRoom.setTitle(etLiveTitle.getText().toString());
        NewLiveRoom newLiveRoom = new NewLiveRoom();
        newLiveRoom.setWatchNum(Integer.parseInt(tvStartFans.getText().toString()));//初始人数
        newLiveRoom.setWatchNumRate(Integer.parseInt(tvAddFans.getText().toString()));//递增人数
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setLiveRoom(liveRoom);
        netRequestBean.setNewLiveRoom(newLiveRoom);
        netRequestBean.setUser(user);
        if (mPresenter != null){
            mPresenter.creLiveRoom(netRequestBean);
        }
    }

    private void showDiamondDialog() {
        if (anchorExitDialog == null) {
            anchorExitDialog = new TextHintDialog(this, R.style.dialog,
                    new TextHintDialog.OnItemClickListener() {
                @Override
                public void onClick(Dialog dialog) {
                    dialog.dismiss();
                    MFGT.gotoOpenLiveActivity(OpenLiveActivity.this);
                }
            }, "余额不足，去充值？");
        }
        anchorExitDialog.show();
    }

    private void showInputDialog(String title) {
        inputDialog = new InputDialog(this, R.style.dialog, clickListener, title);
        inputDialog.show();
    }


    private void intoPicSelect() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .previewImage(true)
                .maxSelectNum(1)
                .enableCrop(true)
                .withAspectRatio(16,11)
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
                    ArrayList<LocalMedia> newFiles = (ArrayList<LocalMedia>) PictureSelector.obtainMultipleResult(data);
                    updateCover(newFiles);
                    break;
            }
        }
    }

    private void updateCover(ArrayList<LocalMedia> newFiles) {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        if (!TextUtils.isEmpty(newFiles.get(0).getCutPath())) {
            Glide.with(this).load(newFiles.get(0).getCutPath()).apply(options).into(ivAnchorCover);
            ArrayList<String> coverList = new ArrayList<>();
            coverList.clear();
            coverList.add(newFiles.get(0).getCutPath());
            compossImage(coverList);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (anchorExitDialog != null) {
            if (anchorExitDialog.isShowing()) {
                anchorExitDialog.dismiss();
            }
            anchorExitDialog = null;
        }
        clickListener = null;
    }

    /**
     * 压缩并上传图片
     *
     * @param
     */
    private void compossImage(final ArrayList<String> imageUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Tiny.FileCompressOptions tiny = new Tiny.FileCompressOptions();
                Tiny.getInstance()
                        .source(imageUrl.toArray(new String[imageUrl.size()]))
                        .batchAsFile().withOptions(tiny)
                        .batchCompress(new FileBatchCallback() {
                            @Override
                            public void callback(boolean isSuccess, String[] outfile, Throwable t) {
                                for (String path : outfile) {
                                    CosUtils.getInstance(OpenLiveActivity.this).uploadToCos(path,8);
                                }
                            }
                        });
            }
        }).start();
    }

    private boolean isAll() {
        if (TextUtils.isEmpty(cover)) {
            ToastUtils.showShort(getResources().getString(R.string.please_upload_cover));
            return false;
        } else if (TextUtils.isEmpty(etLiveTitle.getText().toString())) {
            ToastUtils.showShort(getResources().getString(R.string.live_title_no_empty));
            return false;
        }
        return true;
    }

    @Override
    public void onUploadProcess(int percent) {

    }

    @Override
    public void onUploadResult(int code, String url) {
        if (code == 0) {
            cover = url;
        } else {
            Toast.makeText(this, getResources().getString(R.string.upload_cover_fail), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showGetLiveInfo(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                updateView(result);
            } else {
                showFailed();
            }
        } else {
            showFailed();
        }
    }

    private void updateView(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            LiveRoom liveRoom = new Gson().fromJson(jsonObject.optString("liveRoom"), LiveRoom.class);
            if (liveRoom != null) {
                if (!TextUtils.isEmpty(liveRoom.getCover())) {
                    cover = liveRoom.getCover();
                    ImageLoaderUtils.displayImg(ivAnchorCover,cover);
                }
                if (!TextUtils.isEmpty(liveRoom.getTitle())) {
                    etLiveTitle.setText(liveRoom.getTitle());
                }
                if (!TextUtils.isEmpty(liveRoom.getPhone())) {
                    etLivePhone.setText(liveRoom.getPhone());
                }
                if (!TextUtils.isEmpty(liveRoom.getWx())) {
                    etLiveWx.setText(liveRoom.getWx());
                }
                if (!TextUtils.isEmpty(liveRoom.getContent())) {
                    etDescription.setText(liveRoom.getContent());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showOpenLiveResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                dealWithResult(result);
            } else if (result.getResultCode() == -7) {
                showDiamondDialog();
            } else if (result.getResultCode() == -8) {
                ToastUtils.showShort("您的账号在其它地方已经开播");
            } else {
                showHint();
            }
        } else {
            showFailed();
        }
    }

    private void showHint(){
        ToastUtils.showShort("创建直播间失败");
    }

    private void dealWithResult(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            NewLiveRoom newLiveRoom = new Gson().fromJson(jsonObject.optString("newLiveRoom"), NewLiveRoom.class);
            UserInfo userInfo = new Gson().fromJson(jsonObject.optString("userInfo"), UserInfo.class);
            String userSig = jsonObject.optString("userSig");
            Log.i(TAG, "userSig = " + userSig);
            if (newLiveRoom != null) {
                if (!TextUtils.isEmpty(newLiveRoom.getPushURL())) {
                    if (!TextUtils.isEmpty(userSig)) {
                        loginIM(unionId, userSig, newLiveRoom, userInfo);
                    } else {
                        showHint();
                    }
                } else {
                    showHint();
                }
            } else {
                showHint();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loginIM(String unionId, String userSig,
                         final NewLiveRoom newLiveRoom, final UserInfo userInfo) {
        // identifier为用户名，userSig 为用户登录凭证
        showLoading();
        TIMManager.getInstance().login(unionId, userSig, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见错误码表
                Log.i("1111111", "login failed. code: " + code + " errmsg: " + desc);
                hideLoading();
                showHint();
            }

            @Override
            public void onSuccess() {
                hideLoading();
                Log.i("1111111", "login succ");
                //初始化程序后台后消息推送
                PushUtil.getInstance();
                //初始化消息监听
                MessageEvent.getInstance();
                String deviceMan = Build.MANUFACTURER;
                Log.d("111111", "imsdk env " + TIMManager.getInstance().getEnv());
                setIMNickName();
                MFGT.gotoLiveActivity(OpenLiveActivity.this,newLiveRoom,userInfo,true);
            }
        });
    }

    private void setIMNickName() {
        SharedPreferences instace = SharedPreferencesUtils.getInstance();
        String remarkName = instace.getString(Constans.REMARKNAME, "");
        //初始化参数，修改昵称为“cat”
        TIMFriendshipManager.ModifyUserProfileParam param = new TIMFriendshipManager.ModifyUserProfileParam();
        param.setNickname(remarkName);
        TIMFriendshipManager.getInstance().modifyProfile(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                Log.e(TAG, "modifyProfile failed: " + code + " desc" + desc);
            }

            @Override
            public void onSuccess() {
                Log.e(TAG, "modifyProfile succ");
            }
        });
    }
}
