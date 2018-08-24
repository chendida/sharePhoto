package com.zq.dynamicphoto.mylive.ui;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.mylive.bean.LiveRoom;
import com.zq.dynamicphoto.mylive.bean.NewLiveRoom;
import com.zq.dynamicphoto.mylive.fragment.LayerFragment;
import com.zq.dynamicphoto.mylive.fragment.LiveFragment;
import com.zq.dynamicphoto.mylive.fragment.LiveViewFragment;
import com.zq.dynamicphoto.mylive.fragment.MainDialogFragment;
import com.zq.dynamicphoto.mylive.struct.FunctionManager;
import com.zq.dynamicphoto.mylive.struct.FunctionNoParamNoResult;
import com.zq.dynamicphoto.ui.AnchorContactDialog;
import com.zq.dynamicphoto.ui.widge.SelectDialog;
import com.zq.dynamicphoto.ui.widge.ShareWxDialog;
import com.zq.dynamicphoto.utils.ShareUtils;

import java.util.Timer;

public class LiveActivity extends BaseActivity {
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
        return R.layout.activity_live;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
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

    }

    private void upvoteAnchor() {

    }

    private void showCloseDialog() {

    }

    private void showAnchorContactDialog() {

    }

    /**
     * 复制微信号
     * @param wx
     */
    private void copyWx(String wx) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(wx);
        Toast.makeText(this, "主播微信号已复制到粘贴板", Toast.LENGTH_LONG).show();
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
        /*if (anchorExitDialog == null){
            anchorExitDialog = new AnchorExitDialog(this, R.style.dialog, new AnchorExitDialog.OnItemClickListener() {
                @Override
                public void onClick(Dialog dialog) {
                    dialog.dismiss();
                    AppManager.getInstance().finishCurrentActivity();
                }
            },msg);
        }
        anchorExitDialog.show();*/
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
}
