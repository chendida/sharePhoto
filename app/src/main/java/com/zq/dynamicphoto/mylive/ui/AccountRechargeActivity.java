package com.zq.dynamicphoto.mylive.ui;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.mylive.bean.Charge;
import com.zq.dynamicphoto.mylive.bean.ChargeType;
import com.zq.dynamicphoto.mylive.bean.Recharge;
import com.zq.dynamicphoto.mylive.tools.PhoneIpUtils;
import com.zq.dynamicphoto.presenter.AccountRechargePresenter;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.IAccountRechargeView;
import com.zysdk.ZYSDK;
import com.zysdk.pay.ZYSDKPayIF;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zysdk.ZYSDK.PAY_CANCEL;
import static com.zysdk.ZYSDK.PAY_FAIL;
import static com.zysdk.ZYSDK.PAY_SUCCESS;

/**
 * 账户充值界面
 */
public class AccountRechargeActivity extends BaseActivity<IAccountRechargeView,
        AccountRechargePresenter<IAccountRechargeView>> implements IAccountRechargeView{
    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.tv_diamond)
    TextView tvDiamond;
    @BindView(R.id.tv_diamond1)
    TextView tvDiamond1;
    @BindView(R.id.tv_money1)
    TextView tvMoney1;
    @BindView(R.id.layout_my_diamond1)
    AutoRelativeLayout layoutMyDiamond1;
    @BindView(R.id.tv_diamond2)
    TextView tvDiamond2;
    @BindView(R.id.tv_money2)
    TextView tvMoney2;
    @BindView(R.id.layout_my_diamond2)
    AutoRelativeLayout layoutMyDiamond2;
    @BindView(R.id.tv_diamond3)
    TextView tvDiamond3;
    @BindView(R.id.tv_money3)
    TextView tvMoney3;
    @BindView(R.id.layout_my_diamond3)
    AutoRelativeLayout layoutMyDiamond3;
    @BindView(R.id.layout_my_diamond4)
    AutoRelativeLayout layoutMyDiamond4;
    @BindView(R.id.tv_diamond4)
    TextView tvDiamond4;
    @BindView(R.id.tv_money4)
    TextView tvMoney4;
    @BindView(R.id.layout_one_line)
    AutoRelativeLayout layoutOneLine;
    Dialog dialogUtils;
    @BindView(R.id.tv_diamond1_hint)
    TextView tvDiamond1Hint;
    @BindView(R.id.tv_money1_hint)
    TextView tvMoney1Hint;
    @BindView(R.id.tv_diamond2_hint)
    TextView tvDiamond2Hint;
    @BindView(R.id.tv_money2_hint)
    TextView tvMoney2Hint;
    @BindView(R.id.tv_diamond3_hint)
    TextView tvDiamond3Hint;
    @BindView(R.id.tv_money3_hint)
    TextView tvMoney3Hint;
    @BindView(R.id.tv_diamond4_hint)
    TextView tvDiamond4Hint;
    @BindView(R.id.tv_money4_hint)
    TextView tvMoney4Hint;
    private Integer flag = 0;
    ArrayList<Recharge> rechargeList = new ArrayList<>();
    private String totalDiamound = "";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_recharge;
    }

    @Override
    protected void initView() {
        layoutBack.setVisibility(View.VISIBLE);
        layoutFinish.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.tv_my_profit));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getChargeCode();
    }

    @OnClick({R.id.layout_back, R.id.layout_my_diamond1, R.id.layout_my_diamond2,
            R.id.layout_my_diamond3, R.id.layout_my_diamond4,
            R.id.btn_recharge_ok})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_my_diamond1:
                flag = 0;
                updateSelectBg(1);
                updateTextColor(1);
                break;
            case R.id.layout_my_diamond2:
                flag = 1;
                updateSelectBg(2);
                updateTextColor(2);
                break;
            case R.id.layout_my_diamond3:
                flag = 2;
                updateSelectBg(3);
                updateTextColor(3);
                break;
            case R.id.layout_my_diamond4:
                flag = 3;
                updateSelectBg(4);
                updateTextColor(4);
                break;
            case R.id.btn_recharge_ok:
                //充值
                if (rechargeList != null){
                    if (rechargeList.size() == 4){
                        getOrderId(rechargeList.get(flag).getFeeCode());
                    }else {
                        ToastUtils.showShort("数据异常，请稍后重试");
                    }
                }else {
                    ToastUtils.showShort("数据异常，请稍后重试");
                }
                break;
        }
    }

    private void getOrderId(String totalDiamond) {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        Charge charge = new Charge();
        charge.setUserId(userId);
        charge.setMoney(Integer.parseInt(totalDiamond));
        charge.setChargeTypeId(6);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setCharge(charge);
        if (mPresenter != null){
            this.totalDiamound = totalDiamond;
            mPresenter.getRechargeOrderId(netRequestBean);
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
            rechargeList = new Gson().fromJson(jsonObject.optString("rechargeList"), new TypeToken<List<Recharge>>() {
            }.getType());
            UserInfo userInfo = new Gson().fromJson(jsonObject.optString("userInfo"), UserInfo.class);
            if (rechargeList != null) {
                if (rechargeList.size() == 4) {
                    updateView(rechargeList, userInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
            if (TextUtils.isEmpty(orderId)){
                ToastUtils.showShort(getResources().getString(R.string.recharge_fail));
                return;
            }else {
                String ipAddress = PhoneIpUtils.getIPAddress(this);
                charge(orderId,totalDiamound,ipAddress);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void charge(String orderId, String totalDiamound, String ipAddress) {
        //调用统一支付接口
        ZYSDK.initPay(this, "A100001102", totalDiamound, "充值" + totalDiamound + "分",
                orderId, "app_name=微共享相册demo&package_name="+getPackageName(),
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

    public void getChargeCode() {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        ChargeType chargeType = new ChargeType();
        chargeType.setPackageName(getPackageName());
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setChargeType(chargeType);
        netRequestBean.setUserInfo(userInfo);
        if (mPresenter != null){
            mPresenter.getRechargeCode(netRequestBean);
        }
    }

    private void updateView(ArrayList<Recharge> rechargeList, UserInfo userInfo) {
        if (userInfo != null) {
            tvDiamond.setText(String.valueOf(userInfo.getCoin()));
        }
        tvDiamond1.setText(rechargeList.get(0).getFeeCode());
        tvMoney1.setText(String.valueOf(rechargeList.get(0).getMoney()));

        tvDiamond2.setText(rechargeList.get(1).getFeeCode());
        tvMoney2.setText(String.valueOf(rechargeList.get(1).getMoney()));

        tvDiamond3.setText(rechargeList.get(2).getFeeCode());
        tvMoney3.setText(String.valueOf(rechargeList.get(2).getMoney()));

        tvDiamond4.setText(rechargeList.get(3).getFeeCode());
        tvMoney4.setText(String.valueOf(rechargeList.get(3).getMoney()));
    }

    /**
     * 改变选中的颜色
     *
     * @param flag
     */
    private void updateTextColor(int flag) {
        if (flag == 1) {
            tvDiamond1.setTextColor(getResources().getColor(R.color.btn_login_bg_color));
            tvDiamond1Hint.setTextColor(getResources().getColor(R.color.btn_login_bg_color));
            tvMoney1.setTextColor(getResources().getColor(R.color.tv_money_select));
            tvMoney1Hint.setTextColor(getResources().getColor(R.color.tv_money_select));

            tvDiamond2.setTextColor(getResources().getColor(R.color.black));
            tvDiamond2Hint.setTextColor(getResources().getColor(R.color.black));
            tvMoney2.setTextColor(getResources().getColor(R.color.tv_text_color1));
            tvMoney2Hint.setTextColor(getResources().getColor(R.color.tv_text_color1));

            tvDiamond3.setTextColor(getResources().getColor(R.color.black));
            tvDiamond3Hint.setTextColor(getResources().getColor(R.color.black));
            tvMoney3.setTextColor(getResources().getColor(R.color.tv_text_color1));
            tvMoney3Hint.setTextColor(getResources().getColor(R.color.tv_text_color1));

            tvDiamond4.setTextColor(getResources().getColor(R.color.black));
            tvDiamond4Hint.setTextColor(getResources().getColor(R.color.black));
            tvMoney4.setTextColor(getResources().getColor(R.color.tv_text_color1));
            tvMoney4Hint.setTextColor(getResources().getColor(R.color.tv_text_color1));
        } else if (flag == 2) {
            tvDiamond2.setTextColor(getResources().getColor(R.color.btn_login_bg_color));
            tvDiamond2Hint.setTextColor(getResources().getColor(R.color.btn_login_bg_color));
            tvMoney2.setTextColor(getResources().getColor(R.color.tv_money_select));
            tvMoney2Hint.setTextColor(getResources().getColor(R.color.tv_money_select));

            tvDiamond1.setTextColor(getResources().getColor(R.color.black));
            tvDiamond1Hint.setTextColor(getResources().getColor(R.color.black));
            tvMoney1.setTextColor(getResources().getColor(R.color.tv_text_color1));
            tvMoney1Hint.setTextColor(getResources().getColor(R.color.tv_text_color1));

            tvDiamond3.setTextColor(getResources().getColor(R.color.black));
            tvDiamond3Hint.setTextColor(getResources().getColor(R.color.black));
            tvMoney3.setTextColor(getResources().getColor(R.color.tv_text_color1));
            tvMoney3Hint.setTextColor(getResources().getColor(R.color.tv_text_color1));

            tvDiamond4.setTextColor(getResources().getColor(R.color.black));
            tvDiamond4Hint.setTextColor(getResources().getColor(R.color.black));
            tvMoney4.setTextColor(getResources().getColor(R.color.tv_text_color1));
            tvMoney4Hint.setTextColor(getResources().getColor(R.color.tv_text_color1));
        } else if (flag == 3) {
            tvDiamond3.setTextColor(getResources().getColor(R.color.btn_login_bg_color));
            tvDiamond3Hint.setTextColor(getResources().getColor(R.color.btn_login_bg_color));
            tvMoney3.setTextColor(getResources().getColor(R.color.tv_money_select));
            tvMoney3Hint.setTextColor(getResources().getColor(R.color.tv_money_select));

            tvDiamond2.setTextColor(getResources().getColor(R.color.black));
            tvDiamond2Hint.setTextColor(getResources().getColor(R.color.black));
            tvMoney2.setTextColor(getResources().getColor(R.color.tv_text_color1));
            tvMoney2Hint.setTextColor(getResources().getColor(R.color.tv_text_color1));

            tvDiamond1.setTextColor(getResources().getColor(R.color.black));
            tvDiamond1Hint.setTextColor(getResources().getColor(R.color.black));
            tvMoney1.setTextColor(getResources().getColor(R.color.tv_text_color1));
            tvMoney1Hint.setTextColor(getResources().getColor(R.color.tv_text_color1));

            tvDiamond4.setTextColor(getResources().getColor(R.color.black));
            tvDiamond4Hint.setTextColor(getResources().getColor(R.color.black));
            tvMoney4.setTextColor(getResources().getColor(R.color.tv_text_color1));
            tvMoney4Hint.setTextColor(getResources().getColor(R.color.tv_text_color1));
        } else if (flag == 4) {
            tvDiamond4.setTextColor(getResources().getColor(R.color.btn_login_bg_color));
            tvDiamond4Hint.setTextColor(getResources().getColor(R.color.btn_login_bg_color));
            tvMoney4.setTextColor(getResources().getColor(R.color.tv_money_select));
            tvMoney4Hint.setTextColor(getResources().getColor(R.color.tv_money_select));

            tvDiamond2.setTextColor(getResources().getColor(R.color.black));
            tvDiamond2Hint.setTextColor(getResources().getColor(R.color.black));
            tvMoney2.setTextColor(getResources().getColor(R.color.tv_text_color1));
            tvMoney2Hint.setTextColor(getResources().getColor(R.color.tv_text_color1));

            tvDiamond3.setTextColor(getResources().getColor(R.color.black));
            tvDiamond3Hint.setTextColor(getResources().getColor(R.color.black));
            tvMoney3.setTextColor(getResources().getColor(R.color.tv_text_color1));
            tvMoney3Hint.setTextColor(getResources().getColor(R.color.tv_text_color1));

            tvDiamond1.setTextColor(getResources().getColor(R.color.black));
            tvDiamond1Hint.setTextColor(getResources().getColor(R.color.black));
            tvMoney1.setTextColor(getResources().getColor(R.color.tv_text_color1));
            tvMoney1Hint.setTextColor(getResources().getColor(R.color.tv_text_color1));
        }
    }

    /**
     * 改变选中的背景
     *
     * @param flag
     */
    private void updateSelectBg(int flag) {
        if (flag == 1) {
            layoutMyDiamond1.setBackground(getResources().getDrawable(R.drawable.frame_onclick));
            layoutMyDiamond2.setBackground(getResources().getDrawable(R.drawable.frame));
            layoutMyDiamond3.setBackground(getResources().getDrawable(R.drawable.frame));
            layoutMyDiamond4.setBackground(getResources().getDrawable(R.drawable.frame));
        } else if (flag == 2) {
            layoutMyDiamond1.setBackground(getResources().getDrawable(R.drawable.frame));
            layoutMyDiamond2.setBackground(getResources().getDrawable(R.drawable.frame_onclick));
            layoutMyDiamond3.setBackground(getResources().getDrawable(R.drawable.frame));
            layoutMyDiamond4.setBackground(getResources().getDrawable(R.drawable.frame));
        } else if (flag == 3) {
            layoutMyDiamond1.setBackground(getResources().getDrawable(R.drawable.frame));
            layoutMyDiamond2.setBackground(getResources().getDrawable(R.drawable.frame));
            layoutMyDiamond3.setBackground(getResources().getDrawable(R.drawable.frame_onclick));
            layoutMyDiamond4.setBackground(getResources().getDrawable(R.drawable.frame));
        } else if (flag == 4) {
            layoutMyDiamond1.setBackground(getResources().getDrawable(R.drawable.frame));
            layoutMyDiamond2.setBackground(getResources().getDrawable(R.drawable.frame));
            layoutMyDiamond3.setBackground(getResources().getDrawable(R.drawable.frame));
            layoutMyDiamond4.setBackground(getResources().getDrawable(R.drawable.frame_onclick));
        }
    }
}
