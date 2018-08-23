package com.zq.dynamicphoto.ui;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.mylive.bean.Charge;
import com.zq.dynamicphoto.mylive.bean.ChargeType;
import com.zq.dynamicphoto.mylive.tools.PhoneIpUtils;
import com.zq.dynamicphoto.presenter.AccountRechargePresenter;
import com.zq.dynamicphoto.ui.widge.GlideCircleTransformWithBorder;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.IAccountRechargeView;
import com.zysdk.ZYSDK;
import com.zysdk.pay.ZYSDKPayIF;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zysdk.ZYSDK.PAY_CANCEL;
import static com.zysdk.ZYSDK.PAY_FAIL;
import static com.zysdk.ZYSDK.PAY_SUCCESS;

public class OpenVipActivity extends BaseActivity<IAccountRechargeView,
        AccountRechargePresenter<IAccountRechargeView>> implements IAccountRechargeView {

    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_code1)
    TextView tvCode1;//现价
    @BindView(R.id.tv_code2)
    TextView tvCode2;
    @BindView(R.id.tv_code1_hint)
    TextView tvCode1Hint;//原价
    @BindView(R.id.tv_code1_money_hint)
    TextView tvCode1MoneyHint;//节省了多少钱
    @BindView(R.id.tv_code2_hint)
    TextView tvCode2Hint;
    @BindView(R.id.tv_code2_money_hint)
    TextView tvCode2MoneyHint;
    @BindView(R.id.tv_vip_status)
    TextView tvVipStatus;
    @BindView(R.id.tv_no_vip)
    TextView tvNoVip;
    @BindView(R.id.tv_half_year_vip)
    TextView tvHalfYearVip;
    @BindView(R.id.tv_year_vip)
    TextView tvYearVip;
    private String totalDiamound = "";
    private int halfDiamound = 0;
    private int yearDiamound = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_vip;
    }

    @Override
    protected void initView() {
        layoutBack.setVisibility(View.VISIBLE);
        layoutFinish.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.tv_vip_center));
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        String userLogo = sp.getString(Constans.USERLOGO, "");
        String nick = sp.getString(Constans.REMARKNAME, "");
        if (!TextUtils.isEmpty(userLogo)) {
            Glide.with(this).load(userLogo)
                    .apply(new RequestOptions().error(getResources()
                            .getDrawable(R.drawable.vip_avatar))
                            .placeholder(R.drawable.vip_avatar)
                            .centerCrop()
                            .transform(new GlideCircleTransformWithBorder(this, 2,
                                    getResources().getColor(R.color.white)))
                    ).into(ivAvatar);
        }
        if (!TextUtils.isEmpty(nick)) {
            tvNick.setText(nick);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getChargeCode();
    }

    public void getChargeCode() {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        ChargeType chargeType = new ChargeType();
        chargeType.setPackageName(getPackageName());
        chargeType.setCode(7);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setChargeType(chargeType);
        netRequestBean.setUserInfo(userInfo);
        if (mPresenter != null) {
            mPresenter.getRechargeCode(netRequestBean);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected AccountRechargePresenter<IAccountRechargeView> createPresenter() {
        return new AccountRechargePresenter<>();
    }

    @Override
    public void showGetChargeCode(Result result) {
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
            ArrayList<ChargeType> chargeTypes = new Gson().fromJson(jsonObject.optString("chargeTypeList")
                    , new TypeToken<List<ChargeType>>() {
                    }.getType());
            UserInfo userInfo = new Gson().fromJson(jsonObject.optString("userInfo"), UserInfo.class);
            if (chargeTypes != null) {
                if (chargeTypes.size() == 1) {
                    updateView(chargeTypes.get(0), userInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateView(ChargeType chargeType, UserInfo userInfo) {
        if (chargeType != null) {
            List<String> feeCodeList = chargeType.getFeeCodeList();
            List<String> giveAwayList = chargeType.getGiveAwayList();
            if (feeCodeList.size() == 2 && giveAwayList.size() == 2) {
                if (!TextUtils.isEmpty(feeCodeList.get(0)) &&
                        !TextUtils.isEmpty(giveAwayList.get(0))) {
                    tvCode1.setText(feeCodeList.get(0));
                    tvCode1Hint.setText(giveAwayList.get(0));
                    halfDiamound = Integer.parseInt(feeCodeList.get(0));
                    int currentPrice = Integer.parseInt(feeCodeList.get(0));
                    int oldPrice = Integer.parseInt(giveAwayList.get(0));
                    tvCode1MoneyHint.setText(String.valueOf(oldPrice - currentPrice));
                }
                if (!TextUtils.isEmpty(feeCodeList.get(1)) &&
                        !TextUtils.isEmpty(giveAwayList.get(1))) {
                    tvCode2.setText(feeCodeList.get(1));
                    tvCode2Hint.setText(giveAwayList.get(1));
                    yearDiamound = Integer.parseInt(feeCodeList.get(1));
                    int currentPrice = Integer.parseInt(feeCodeList.get(1));
                    int oldPrice = Integer.parseInt(giveAwayList.get(1));
                    tvCode2MoneyHint.setText(String.valueOf(oldPrice - currentPrice));
                }
            } else {
                ToastUtils.showShort(getResources().getString(R.string.data_error));
            }
        }

        if (userInfo != null) {
            if (userInfo.getIsVip() != null) {
                SharedPreferences.Editor edit = SharedPreferencesUtils.getInstance().edit();
                if (userInfo.getIsVip() == 1) {
                    edit.putBoolean(Constans.IS_VIP, true);
                    if (!TextUtils.isEmpty(userInfo.getVip())) {
                        edit.putString(Constans.VIP, "");
                    }
                    SpannableStringBuilder style = new SpannableStringBuilder("会员有效期:" +
                            userInfo.getVip().substring(0, 10));
                    style.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvVipStatus.setText(style);
                    tvNoVip.setVisibility(View.GONE);
                    tvHalfYearVip.setText(getResources().getString(R.string.xufei));
                    tvYearVip.setText(getResources().getString(R.string.xufei));
                } else {
                    edit.putBoolean(Constans.IS_VIP, false);
                    tvVipStatus.setText(getResources().getString(R.string.tv_current_vip_status));
                    tvNoVip.setVisibility(View.VISIBLE);
                    tvHalfYearVip.setText(getResources().getString(R.string.kaiqi));
                    tvYearVip.setText(getResources().getString(R.string.kaiqi));
                }
                edit.commit();
            }
        }
    }

    @Override
    public void showGetChargeOrderId(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                analysisOrderId(result);
            } else {
                showFailed();
            }
        } else {
            showFailed();
        }
    }

    private void analysisOrderId(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            String orderId = jsonObject.optString("orderId");
            if (TextUtils.isEmpty(orderId)) {
                ToastUtils.showShort(getResources().getString(R.string.recharge_fail));
                return;
            } else {
                String ipAddress = PhoneIpUtils.getIPAddress(this);
                charge(orderId, totalDiamound, ipAddress);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void charge(String orderId, String totalDiamound, String ipAddress) {
        //调用统一支付接口
        ZYSDK.initPay(this, "A100001102", totalDiamound, "充值" + totalDiamound + "分",
                orderId, "app_name=微共享相册demo&package_name=" + getPackageName(),
                ipAddress, new ZYSDKPayIF() {
                    @Override
                    public void onPayResult(String result) {
                        switch (Integer.parseInt(result)) {
                            case PAY_SUCCESS://支付成功
                                ToastUtils.showShort(getResources().getString(R.string.pay_success));
                                break;
                            case PAY_FAIL://支付失败
                                ToastUtils.showShort(getResources().getString(R.string.pay_fail));
                                break;
                            case PAY_CANCEL://支付取消
                                ToastUtils.showShort(getResources().getString(R.string.pay_cancel));
                                break;
                        }
                    }
                });
    }


    @OnClick({R.id.layout_back, R.id.btn_half_year_vip, R.id.btn_year_vip,
            R.id.layout_vip_good, R.id.layout_free_open_vip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                this.finish();
                break;
            case R.id.btn_half_year_vip:
                if (halfDiamound != 0) {
                    getOrderId(String.valueOf(halfDiamound * 100), 2);
                } else {
                    ToastUtils.showShort(getResources().getString(R.string.data_error));
                }
                break;
            case R.id.btn_year_vip:
                if (yearDiamound != 0) {
                    getOrderId(String.valueOf(yearDiamound * 100), 3);
                } else {
                    ToastUtils.showShort(getResources().getString(R.string.data_error));
                }
                break;
            case R.id.layout_vip_good:
                break;
            case R.id.layout_free_open_vip:
                break;
        }
    }

    private void getOrderId(String totalDiamond, int type) {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        Charge charge = new Charge();
        charge.setUserId(userId);
        charge.setChargeMode(type);
        charge.setMoney(Integer.parseInt(totalDiamond));
        charge.setChargeTypeId(8);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setCharge(charge);
        if (mPresenter != null) {
            this.totalDiamound = totalDiamond;
            mPresenter.getRechargeOrderId(netRequestBean);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
