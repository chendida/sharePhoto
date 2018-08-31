package com.zq.dynamicphoto.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.LiveListAdapter;
import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.bean.UserRelation;
import com.zq.dynamicphoto.mylive.bean.LiveRoom;
import com.zq.dynamicphoto.mylive.bean.MessageEvent;
import com.zq.dynamicphoto.mylive.bean.NewLiveRoom;
import com.zq.dynamicphoto.mylive.tools.PushUtil;
import com.zq.dynamicphoto.mylive.ui.LiveActivity;
import com.zq.dynamicphoto.presenter.LiveListPresenter;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.GotoLiveRoomListener;
import com.zq.dynamicphoto.view.ILiveListView;
import com.zq.dynamicphoto.view.UploadLiveOrder;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

import static android.content.ContentValues.TAG;

/**
 * 直播列表界面
 */
public class LiveListFragment extends BaseFragment<ILiveListView,LiveListPresenter<ILiveListView>>
        implements ILiveListView,GotoLiveRoomListener {
    @BindView(R.id.rcl_live_list)
    RecyclerView rclLiveList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    LiveListAdapter mAdapter;
    @BindView(R.id.layout_init)
    AutoRelativeLayout layoutInit;
    private int pager = 1;
    int pagerCount = 1;//总页码数
    ArrayList<NewLiveRoom> liveRoomsList = new ArrayList<>();
    private NewLiveRoom newLiveRoom;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_list;
    }

    @Override
    protected void initView(View view) {
        if (liveRoomsList == null) {
            liveRoomsList = new ArrayList<>();
        }
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rclLiveList.setLayoutManager(manager);
        mAdapter = new LiveListAdapter(getActivity(), liveRoomsList,this);
        rclLiveList.setAdapter(mAdapter);
        rclLiveList.setNestedScrollingEnabled(false);
        rclLiveList.setHasFixedSize(true);
        setListener();
    }

    private void setListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pager = 1;
                getLiveList(pager);//上拉刷新
                refreshlayout.finishRefresh(600);
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pager++;
                if (pagerCount >= pager) {
                    getLiveList(pager);
                }
                refreshlayout.finishLoadmore(1000);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getLiveList(pager);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected LiveListPresenter<ILiveListView> createPresenter() {
        return new LiveListPresenter<>();
    }

    @Override
    protected void loadData() {
    }


    private void getLiveList(int pager) {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt("userId", 0);
        UserRelation userRelation = new UserRelation();
        userRelation.setIuserId(userId);
        userRelation.setPage(pager);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setUserRelation(userRelation);
        if (mPresenter != null){
            mPresenter.getLiveListRoom(netRequestBean);
        }
    }

    @Override
    public void showLiveListRoomResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == 0) {
                dealWithResult(result);
            } else {
                ToastUtils.showShort(getResources().getString(R.string.data_error));
            }
        }else {
            showFailed();
        }
    }

    private void dealWithResult(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            pagerCount = jsonObject.optInt("pageCount", 1);
            liveRoomsList = new Gson().fromJson(jsonObject.optString("newLiveRoomList"), new TypeToken<List<NewLiveRoom>>() {
            }.getType());
            if (liveRoomsList != null) {
                Log.i("dynamicList", "size = " + liveRoomsList.size());
                if (liveRoomsList.size() != 0) {
                    layoutInit.setVisibility(View.GONE);
                    refreshLayout.setVisibility(View.VISIBLE);
                    if (pager == 1) {
                        mAdapter.initLiveList(liveRoomsList);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter.addLiveList(liveRoomsList);
                    }
                } else {
                    if (pager == 1) {
                        layoutInit.setVisibility(View.VISIBLE);
                        //refreshLayout.setVisibility(View.GONE);
                        mAdapter.initLiveList(liveRoomsList);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showGotoLiveRoomResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == 0) {
                dealWithResult(result,newLiveRoom);
            } else if (result.getResultCode() == -7){
                ToastUtils.showShort("主播已退出直播间");
            }else {
                ToastUtils.showShort("网络异常");
            }
        }
    }

    private void dealWithResult(Result result, NewLiveRoom newLiveRoom) {
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        String unionId = sp.getString("unionId", "");
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            UserInfo userInfo = new Gson().fromJson(jsonObject.optString("userInfo"),UserInfo.class);
            LiveRoom liveRoom = new Gson().fromJson(jsonObject.optString("liveRoom"),LiveRoom.class);
            String userSig = jsonObject.optString("userSig");
            Integer isLive = jsonObject.optInt("isLive");
            if (isLive == 1){//同一账号已开播，直接进入直播间
                MFGT.gotoLiveActivity(getActivity(),userInfo,newLiveRoom,liveRoom,false);
                return;
            }
            Log.i(TAG,jsonObject.toString());
            if (newLiveRoom != null) {
                if (!TextUtils.isEmpty(newLiveRoom.getPlay_url1())) {
                    if (!TextUtils.isEmpty(userSig)){
                        loginIM(unionId,userSig,userInfo,newLiveRoom,liveRoom);
                    }else {
                        ToastUtils.showShort("网络异常");
                    }
                }else {
                    ToastUtils.showShort("网络异常");
                }
            }else {
                ToastUtils.showShort("网络异常");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loginIM(String userName, String userSig,
                         final UserInfo userInfo, final NewLiveRoom newLiveRoom, final LiveRoom liveRoom) {
        // identifier为用户名，userSig 为用户登录凭证
        Log.i("1111111", "userName: " + userName + " userSig: " + userSig);
        showLoading();
        TIMManager.getInstance().login(userName, userSig, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                ToastUtils.showShort("网络异常");
                hideLoading();
            }

            @Override
            public void onSuccess() {
                hideLoading();
                //初始化程序后台后消息推送
                PushUtil.getInstance();
                //初始化消息监听
                MessageEvent.getInstance();
                String deviceMan = android.os.Build.MANUFACTURER;
                setIMNickName();
                MFGT.gotoLiveActivity(getActivity(),userInfo,newLiveRoom,liveRoom,false);
            }
        });
    }

    private void setIMNickName() {
        SharedPreferences instace = SharedPreferencesUtils.getInstance();
        String remarkName = instace.getString("remarkName", "");
        //初始化参数，修改昵称为“cat”
        TIMFriendshipManager.ModifyUserProfileParam param = new TIMFriendshipManager.ModifyUserProfileParam();
        param.setNickname(remarkName);

        TIMFriendshipManager.getInstance().modifyProfile(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                Log.e(TAG, "modifyProfile failed: " + code + " desc" + desc);
            }

            @Override
            public void onSuccess() {
                Log.e(TAG, "modifyProfile succ");
            }
        });
    }

    /**
     * 进入直播间回调
     * @param netRequestBean
     */
    @Override
    public void gotoLiveRoom(NewLiveRoom newLiveRoom, NetRequestBean netRequestBean) {
        if (mPresenter !=  null){
            this.newLiveRoom = newLiveRoom;
            mPresenter.gotoLiveRoom(netRequestBean);
        }
    }
}
