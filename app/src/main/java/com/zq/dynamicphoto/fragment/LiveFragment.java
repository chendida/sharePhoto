package com.zq.dynamicphoto.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tencent.rtmp.TXLiveBase;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.presenter.BalanceAndOrdersNumPresenter;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.ILoadView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 直播
 */
public class LiveFragment extends BaseFragment<ILoadView,BalanceAndOrdersNumPresenter<ILoadView>>
        implements ILoadView{
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_order_num)
    TextView tvOrderNum;
    @BindView(R.id.tv_diamond_num)
    TextView tvDiamondNum;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live;
    }

    @Override
    protected void initView(View view) {
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        String userLogo = sp.getString(Constans.USERLOGO, "");
        String userNick = sp.getString(Constans.REMARKNAME, "");
        int userId = sp.getInt(Constans.USERID, 0);
        if (!TextUtils.isEmpty(userLogo)) {
            ImageLoaderUtils.displayImg(ivAvatar,userLogo);
        }
        if (!TextUtils.isEmpty(userNick)) {
            tvNick.setText(userNick);
        }
        String ID = "ID:" + userId;
        tvId.setText(ID);
    }

    @OnClick({R.id.layout_my_orders, R.id.layout_live_good, R.id.layout_my_profit,
            R.id.btn_open_live,R.id.layout_my_consumption})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_my_orders:
                MFGT.totoMyOrdersActivity(getActivity());
                //startActivity(new Intent(getActivity(), MyOrderActivity.class));
                break;
            case R.id.layout_live_good:
                //startActivity(new Intent(getActivity(), AddLiveGoodActivity.class));
                break;
            case R.id.layout_my_profit:
                //startActivity(new Intent(getActivity(), AccountRechargeActivity.class));
                break;
            case R.id.btn_open_live:
                //startActivity(new Intent(getActivity(), OpenLiveActivity.class));
                break;
            case R.id.layout_my_consumption://消费清单
                //startActivity(new Intent(getActivity(), ConsumptionListActivity.class));
                break;
        }
    }

    @Override
    protected void initData() {
        String sdkver = TXLiveBase.getSDKVersionStr();
        Log.d("liteavsdk", "liteav sdk version is : " + sdkver);
    }

    @Override
    protected BalanceAndOrdersNumPresenter<ILoadView> createPresenter() {
        return new BalanceAndOrdersNumPresenter<>();
    }

    @Override
    protected void loadData() {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setUserInfo(userInfo);
        if (mPresenter != null){
            mPresenter.getOrdersNumAndBalance(netRequestBean);
        }
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
            UserInfo userInfo = new Gson().fromJson(jsonObject.optString("userInfo"),UserInfo.class);
            if (userInfo != null){
                if (userInfo.getCoin() != null){
                    tvDiamondNum.setText(String.valueOf(userInfo.getCoin()));
                }
                if (userInfo.getNum() != null){
                    tvOrderNum.setText(String.valueOf(userInfo.getNum()));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
