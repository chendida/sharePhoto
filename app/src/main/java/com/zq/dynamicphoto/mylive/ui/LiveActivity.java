package com.zq.dynamicphoto.mylive.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMGroupAddOpt;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.mylive.bean.LiveRoom;
import com.zq.dynamicphoto.mylive.bean.NewLiveRoom;
import com.zq.dynamicphoto.mylive.fragment.GoodsDialog;
import com.zq.dynamicphoto.mylive.fragment.LayerFragment;
import com.zq.dynamicphoto.mylive.fragment.LiveFragment;
import com.zq.dynamicphoto.mylive.fragment.LiveViewFragment;
import com.zq.dynamicphoto.mylive.fragment.MainDialogFragment;
import com.zq.dynamicphoto.mylive.struct.FunctionManager;
import com.zq.dynamicphoto.mylive.struct.FunctionNoParamNoResult;
import com.zq.dynamicphoto.presenter.LivePresenter;
import com.zq.dynamicphoto.ui.AnchorContactDialog;
import com.zq.dynamicphoto.ui.widge.SelectDialog;
import com.zq.dynamicphoto.ui.widge.ShareWxDialog;
import com.zq.dynamicphoto.utils.ShareUtils;
import com.zq.dynamicphoto.view.LiveView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveActivity extends BaseActivity<LiveView,
        LivePresenter<LiveView>> implements LiveView {
    private static final String TAG = "LiveActivity";
    private Timer heartTimer;//心跳定时器
    SelectDialog selectDialog;
    private static NewLiveRoom newLiveRoom;
    private static UserInfo userInfo;
    private static Boolean isAnchor;
    private static LiveRoom liveRoom;
    AnchorContactDialog anchorContactDialog;
    //AnchorExitDialog anchorExitDialog;
    LiveViewFragment liveViewFragment;
    MainDialogFragment mainDialogFragment;

    public static LiveRoom getLiveRoom() {
        return liveRoom;
    }

    public static UserInfo getUserInfo() {
        return userInfo;
    }

    public static NewLiveRoom getNewLiveRoom() {
        return newLiveRoom;
    }

    public static Boolean getIsAnchor() {
        return isAnchor;
    }
    @Override
    protected int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        return R.layout.activity_live;
    }

    @Override
    protected void initView() {
        checkPublishPermission();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        /*这里可以看到的就是我们将初始化直播的Fragment添加到了这个页面作为填充
        * 并且将MainDialogFragment显示在该页面的顶部已达到各种不同交互的需求*/
        newLiveRoom = (NewLiveRoom) getIntent().getSerializableExtra("newLiveRoom");
        isAnchor = (Boolean) getIntent().getSerializableExtra("isAnchor");
        userInfo = (UserInfo) getIntent().getSerializableExtra("userInfo");
        liveRoom = (LiveRoom) getIntent().getSerializableExtra("liveRoom");
        //接收参数
        acceptParam(newLiveRoom,isAnchor,userInfo);
    }

    private void acceptParam(NewLiveRoom newLiveRoom, Boolean isAnchor, UserInfo userInfo) {
        liveViewFragment = new LiveViewFragment();
        if (isAnchor){
            createGroup(newLiveRoom.getLiveId());
        }
        getSupportFragmentManager().beginTransaction().add(R.id.flmain, liveViewFragment).commit();
        if (mainDialogFragment == null){
            mainDialogFragment = new MainDialogFragment();
        }
        mainDialogFragment.show(getSupportFragmentManager(), "MainDialogFragment");
        if (isAnchor) {
            heartTiming();
        }else {
            joinLiveRoom();
        }
    }

    private void joinLiveRoom() {
        TIMGroupManager.getInstance().applyJoinGroup(""+newLiveRoom.getLiveId(), "some reason", new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //接口返回了错误码 code 和错误描述 desc，可用于原因
                //错误码 code 列表请参见错误码表
                Log.e(TAG, "disconnected");
            }
            @Override
            public void onSuccess() {
                Log.i(TAG, "join group");
            }
        });
    }

    /**
     * 定时心跳汇报,10s一次
     */
    private void heartTiming() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                notifyHeart();
            }
        };
        heartTimer = new Timer();
        heartTimer.schedule(task, 0, 5000);
    }

    /**
     * 心跳
     */
    private void notifyHeart() {
        if (newLiveRoom == null){
            return;
        }else {
            if (newLiveRoom.getId() == null){
                return;
            }
        }
        Log.i(TAG,"heart");
        DeviceProperties dr = DrUtils.getInstance();
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        NewLiveRoom newLiveRoom = new NewLiveRoom();
        newLiveRoom.setId(this.newLiveRoom.getId());
        netRequestBean.setNewLiveRoom(newLiveRoom);
        if (mPresenter != null){
            mPresenter.liveHeart(netRequestBean);
        }
    }

    /**
     * 创建群组
     * @param liveId
     */
    private void createGroup(final int liveId) {
        TIMGroupManager.CreateGroupParam createGroupParam = new TIMGroupManager.CreateGroupParam("AVChatRoom",""+liveId);
        createGroupParam.setGroupId(""+liveId);
        createGroupParam.setAddOption(TIMGroupAddOpt.TIM_GROUP_ADD_ANY);
        TIMGroupManager.getInstance().createGroup(createGroupParam, new TIMValueCallBack<String>() {
            @Override
            public void onError(int i, String s) {
                Log.i(TAG,"code = "+i);
                Log.i(TAG,"s = "+s);
            }

            @Override
            public void onSuccess(String s) {
                Log.i(TAG,"成功s = "+s);
            }
        });
    }

    private boolean checkPublishPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(this,
                        permissions.toArray(new String[0]),
                        100);
                return false;
            }
        }

        return true;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected LivePresenter<LiveView> createPresenter() {
        return new LivePresenter<>();
    }


    /**
     * 接口回调监听，用于fragment和宿主activity之间的通信
     */
    public void setFunctionForFragment() {
        LiveFragment baseFragment = new LiveFragment();
        FunctionManager manager = FunctionManager.getInstance();
        baseFragment.setmFunctionManager(manager.addFunction(new FunctionNoParamNoResult
                (LayerFragment.INTERFACE_SHOW_CONTAT) {
            @Override
            public void function() {//点击联系主播的回调监听
                if (liveRoom == null){
                    return;
                }else {
                    if (!TextUtils.isEmpty(liveRoom.getWx()) && !TextUtils.isEmpty(liveRoom.getPhone())){
                        showAnchorContactDialog();
                    }else if (!TextUtils.isEmpty(liveRoom.getWx()) && TextUtils.isEmpty(liveRoom.getPhone())){
                        //只有微信，直接复制微信号并提示用户
                        copyWx(liveRoom.getWx());
                    }else if (TextUtils.isEmpty(liveRoom.getWx()) && !TextUtils.isEmpty(liveRoom.getPhone())){
                        //只有电话，直接跳转到拨号界面
                        gotoPhone(liveRoom.getPhone());
                    }
                }
            }
        }).addFunction(new FunctionNoParamNoResult(LayerFragment.INTERFACE_SHOW_GOOS_LIST) {
            @Override
            public void function() {
                showGoodsList();
            }
        }).addFunction(new FunctionNoParamNoResult(LayerFragment.INTERFACE_CLOSE_LIVE) {
            @Override
            public void function() {
                if (!isAnchor){
                    LiveActivity.this.finish();
                }else {
                    showCloseDialog();
                }
            }
        }).addFunction(new FunctionNoParamNoResult(LayerFragment.INTERFACE_UPVOTE) {//点赞
            @Override
            public void function() {
                upvoteAnchor();
            }
        }).addFunction(new FunctionNoParamNoResult(LayerFragment.INTERFACE_SHARE_TO_WX) {//分享
            @Override
            public void function() {
                showShareWxDialog();
            }
        }).addFunction(new FunctionNoParamNoResult(LayerFragment.INTERFACE_ANCHOR_EXIT) {//主播退出直播间
            @Override
            public void function() {
                showAnchorExitDialog("主播已退出，是否离开？");
            }
        }).addFunction(new FunctionNoParamNoResult(LayerFragment.INTERFACE_ChANGE_CAMERA) {
            @Override
            public void function() {
                //切换摄像头
                // 默认是前置摄像头
                liveViewFragment.changeCamera();
            }
        }).addFunction(new FunctionNoParamNoResult(LiveViewFragment.INTERFACE_NETWORK_ERROR) {
            @Override
            public void function() {
                showAnchorExitDialog("网络异常，是否离开？");
            }
        }).addFunction(new FunctionNoParamNoResult(LayerFragment.INTERFACE_REPORT_ANCHOR) {
            @Override
            public void function() {
                showAnchorExitDialog("网络异常，是否离开？");
            }
        }));
    }

    private void showGoodsList() {
        new GoodsDialog(newLiveRoom).show(getSupportFragmentManager(),"GoodsDialog");
    }

    /**
     * 给主播点赞
     */
    private void upvoteAnchor() {
        DeviceProperties dr = DrUtils.getInstance();
        NewLiveRoom newLiveRoom = new NewLiveRoom();
        newLiveRoom.setId(this.newLiveRoom.getId());
        newLiveRoom.setLiveId(this.newLiveRoom.getLiveId());
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setNewLiveRoom(newLiveRoom);
        if (mPresenter != null){
            mPresenter.upvote(netRequestBean);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().setGroupListener(null);
        liveViewFragment = null;
        mainDialogFragment = null;
        if (isAnchor){
            deleteGroup(newLiveRoom.getLiveId());
        }else {
            quitLiveGroup();
        }
        if (heartTimer != null) {
            heartTimer.cancel();
        }
        if (selectDialog != null){
            if (selectDialog.isShowing()){
                selectDialog.dismiss();
            }
            selectDialog = null;
        }
        if (anchorContactDialog != null){
            if (anchorContactDialog.isShowing()){
                anchorContactDialog.dismiss();
            }
            anchorContactDialog = null;
        }
        newLiveRoom = null;
        userInfo = null;
        isAnchor = null;
    }

    /**
     * 退出群组
     */
    private void quitLiveGroup() {
        //创建回调
        TIMCallBack cb = new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 含义请参见错误码表
            }

            @Override
            public void onSuccess() {
                Log.e(TAG, "quit group succ");
            }
        };

        //退出群组
        TIMGroupManager.getInstance().quitGroup(
                newLiveRoom.getLiveId()+"",  //群组 ID
                cb);      //回调
    }

    private void deleteGroup(Integer liveId) {
        //解散群组
        TIMGroupManager.getInstance().deleteGroup(liveId+"", new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                Log.d(TAG, "login failed. code: " + code + " errmsg: " + desc);
            }

            @Override
            public void onSuccess() {
                //解散群组成功
                Log.d(TAG, "success");
            }
        });
    }


    private void showCloseDialog() {
        new SelectDialog(this, R.style.dialog, new SelectDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                switch (position) {
                    case 1://确定关闭
                        if (heartTimer != null) {
                            heartTimer.cancel();
                        }
                        closeLiveRoom();
                        LiveActivity.this.finish();
                        break;
                    case 2://取消关闭
                        break;
                }
            }
        },"").show();
    }

    private void closeLiveRoom() {
        if (newLiveRoom == null){
            return;
        }else {
            if (newLiveRoom.getLiveId() == null){
                return;
            }
        }
        DeviceProperties dr = DrUtils.getInstance();
        LiveRoom liveRoom = new LiveRoom();
        liveRoom.setLiveId(newLiveRoom.getLiveId());
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setLiveRoom(liveRoom);
        if (mPresenter != null){
            mPresenter.closeLiveRoom(netRequestBean);
        }
    }

    private void showAnchorContactDialog() {
        if (anchorContactDialog == null) {
            anchorContactDialog = new AnchorContactDialog(this, R.style.dialog, new AnchorContactDialog.OnItemClickListener() {
                @Override
                public void onClick(Dialog dialog, int position) {
                    dialog.dismiss();
                    switch (position) {
                        case 1://复制微信
                            copyWx(liveRoom.getWx());
                            break;
                        case 2://打电话
                            gotoPhone(liveRoom.getPhone());
                            break;
                        case 3:
                            break;
                    }
                }
            }, liveRoom.getWx(), liveRoom.getPhone(), liveRoom.getContent());
        }
        anchorContactDialog.show();
    }

    /**
     * 复制微信号
     * @param wx
     */
    private void copyWx(String wx) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(wx);
        ToastUtils.showShort("主播微信号已复制到粘贴板");
    }

    /**
     * 跳转到拨号界面
     * @param phone
     */
    private void gotoPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void showAnchorExitDialog(String msg) {
        new SelectDialog(this, R.style.dialog, new SelectDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                switch (position) {
                    case 1://确定关闭
                        LiveActivity.this.finish();
                        break;
                    case 2://取消关闭
                        break;
                }
            }
        },msg).show();
    }

    private void showShareWxDialog() {
        final String url = Constans.HTML_Url + "wx.html?liveId="+newLiveRoom.getLiveId()
                +"&newliveId="+newLiveRoom.getId()+"&liveUserId="+newLiveRoom.getUserId();
        new ShareWxDialog(this, R.style.dialog, new ShareWxDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                switch (position) {
                    case 1:
                        ShareUtils.getInstance().shareLiveLink(
                                url,newLiveRoom.getTitle(),
                                1,newLiveRoom.getCover());
                        break;
                    case 2://分享到微信朋友圈
                        ShareUtils.getInstance().shareLiveLink(
                                url,newLiveRoom.getTitle(),
                                2,newLiveRoom.getCover());
                        break;
                }
            }
        }).show();
    }

    @Override
    public void showCloseRoomResult(Result result) {

    }

    @Override
    public void showLiveHeartResult(Result result) {

    }

    @Override
    public void showUpvoteResult(Result result) {

    }
}
