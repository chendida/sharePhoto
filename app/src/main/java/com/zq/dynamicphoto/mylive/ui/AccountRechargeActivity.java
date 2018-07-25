package com.zq.dynamicphoto.mylive.ui;

import android.app.Dialog;
import android.widget.TextView;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.mylive.bean.Recharge;
import java.util.ArrayList;
import butterknife.BindView;

/**
 * 账户充值界面
 */
public class AccountRechargeActivity extends BaseActivity {
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_recharge;
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
}
