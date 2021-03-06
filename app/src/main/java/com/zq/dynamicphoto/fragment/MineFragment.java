package com.zq.dynamicphoto.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.ui.SettingActivity;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的
 */
public class MineFragment extends BaseFragment {
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_photo_url)
    TextView tvPhotoUrl;
    @BindView(R.id.layout_mine_info)
    AutoRelativeLayout layoutMineInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view) {
        layoutMineInfo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!TextUtils.isEmpty(tvPhotoUrl.getText().toString())) {
                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(tvPhotoUrl.getText());
                    ToastUtils.showShort("相册地址已复制到粘贴板");
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        String userLogo = sp.getString(Constans.USERLOGO, "");
        String remarkName = sp.getString(Constans.REMARKNAME, "");
        String photoUrl = sp.getString(Constans.PHOTOURL, "");
        if (!TextUtils.isEmpty(userLogo)) {
            ImageLoaderUtils.displayImg(ivAvatar, userLogo);
        }
        if (!TextUtils.isEmpty(remarkName)) {
            tvNick.setText(remarkName);
        }
        if (!TextUtils.isEmpty(photoUrl)) {
            tvPhotoUrl.setText(photoUrl);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void loadData() {

    }


    @OnClick({R.id.layout_mine_info, R.id.layout_open_vip, R.id.layout_my_two_code,
            R.id.layout_my_follow, R.id.layout_my_fans, R.id.layout_problem,
            R.id.layout_about_app, R.id.layout_setting,R.id.layout_my_friend_circle,
            R.id.layout_my_small_programe})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_my_small_programe:
                MFGT.gotoMySmallProgrameActivity(getActivity());
                break;
            case R.id.layout_my_friend_circle:
                MFGT.gotoMyFriendCircleActivity(getActivity());
                break;
            case R.id.layout_mine_info:
                MFGT.gotoPhotoInfoActivity(getActivity());
                break;
            case R.id.layout_open_vip:
                MFGT.gotoOpenVipActivity(getActivity());
                break;
            case R.id.layout_my_two_code:
                /*SharedPreferences sp = SharedPreferencesUtils.getInstance();
                int userId = sp.getInt(Constans.USERID,0);
                MFGT.gotoHtmlManagerActivity(getActivity(),"share.html?userid="+userId,
                        getResources().getString(R.string.photo_two_code),1);*/
                MFGT.gotoMyTwoCodeActivity(getActivity());
                break;
            case R.id.layout_my_follow:
                MFGT.gotoMyFollowActivity(getActivity());
                break;
            case R.id.layout_my_fans:
                MFGT.gotoMyFansActivity(getActivity());
                break;
            case R.id.layout_problem:
                MFGT.gotoProblemAndFeedbackActivity(getActivity());
                break;
            case R.id.layout_about_app:
                MFGT.gotoAboutAppActivity(getActivity());
                break;
            case R.id.layout_setting:
                MFGT.startActivity(getActivity(), SettingActivity.class);
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
