package com.zq.dynamicphoto.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.MessageEvent;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.fragment.FriendCircleFragment;
import com.zq.dynamicphoto.fragment.HomePageFragment;
import com.zq.dynamicphoto.fragment.LiveFragment;
import com.zq.dynamicphoto.fragment.MineFragment;
import com.zq.dynamicphoto.presenter.DynamicUploadPresenter;
import com.zq.dynamicphoto.ui.widge.ScrollViewPager;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.view.IUploadDynamicView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity<IUploadDynamicView,
        DynamicUploadPresenter<IUploadDynamicView>> implements IUploadDynamicView{
    @BindView(R.id.view_pager)
    ScrollViewPager viewPager;
    @BindView(R.id.rb_tab_dynamic)
    RadioButton rbTabDynamic;
    @BindView(R.id.rb_tab_friend_circle)
    RadioButton rbTabFriendCircle;
    @BindView(R.id.rb_tab_live)
    RadioButton rbTabLive;
    @BindView(R.id.rb_tab_mine)
    RadioButton rbTabMine;
    @BindView(R.id.rg_tab)
    RadioGroup rgTab;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    ViewPagerAdapter mViewPagerAdapter;
    private long exitTime = 0;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTitleStrs = new ArrayList<>();
    private HomePageFragment dynamicFragment;
    private FriendCircleFragment friendCircleFragment;
    private LiveFragment liveFragment;
    private MineFragment mineFragment;
    private int mCurrentTabPos = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        initFragments();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initFragments() {
        dynamicFragment = new HomePageFragment();
        friendCircleFragment = new FriendCircleFragment();
        liveFragment = new LiveFragment();
        mineFragment = new MineFragment();

        mFragments.add(dynamicFragment);
        mFragments.add(friendCircleFragment);
        mFragments.add(liveFragment);
        mFragments.add(mineFragment);

        mTitleStrs.add(getResources().getString(R.string.tv_photo_dynamic));
        mTitleStrs.add(getResources().getString(R.string.tv_friend_circle));
        mTitleStrs.add(getResources().getString(R.string.tv_live));
        mTitleStrs.add(getResources().getString(R.string.tv_mine));
    }

    @Override
    protected void initData() {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mViewPagerAdapter);
        viewPager.setCurrentItem(mCurrentTabPos);
        initListener();
    }

    private void initListener() {
        rgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int pos = 0;
                switch (checkedId) {
                    case R.id.rb_tab_dynamic:
                        pos = 0;
                        break;
                    case R.id.rb_tab_friend_circle:
                        pos = 1;
                        break;
                    case R.id.rb_tab_live:
                        pos = 2;
                        break;
                    case R.id.rb_tab_mine:
                        pos = 3;
                        break;
                    default:
                        break;
                }
                viewPager.setCurrentItem(pos);
                tvTitle.setText(mTitleStrs.get(pos));
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tvTitle.setText(mTitleStrs.get(position));
                switch (position) {
                    case 0:
                        rbTabDynamic.setChecked(true);
                        break;
                    case 1:
                        rbTabFriendCircle.setChecked(true);
                        break;
                    case 2:
                        rbTabLive.setChecked(true);
                        break;
                    case 3:
                        rbTabMine.setChecked(true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected DynamicUploadPresenter<IUploadDynamicView> createPresenter() {
        return new DynamicUploadPresenter<>();
    }

    @OnClick(R.id.layout_finish)
    public void onClicked() {
        MFGT.gotoAddPicActivity(this);
    }

    @Override
    public void showUploadDynamicResult(Result result) {

    }

    @Override
    public void showEditDynamicResult(Result result) {

    }

    @Override
    public void showRepeatDynamicResult(Result result) {

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 1000) {
            ToastUtils.showShort(R.string.exit_program);
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
