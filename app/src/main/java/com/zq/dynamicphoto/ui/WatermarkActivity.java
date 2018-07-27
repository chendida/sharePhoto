package com.zq.dynamicphoto.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.SelectWaterPicAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.Image;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserWatermark;
import com.zq.dynamicphoto.bean.WaterEvent;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.fragment.PictureSlideFragment;
import com.zq.dynamicphoto.presenter.OperateWaterPresenter;
import com.zq.dynamicphoto.ui.widge.NoPreloadViewPager;
import com.zq.dynamicphoto.ui.widge.SwitchButton;
import com.zq.dynamicphoto.ui.widge.WaterMouldSelectDialog;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.IOperateWaterView;
import com.zq.dynamicphoto.view.WaterMouldView;
import com.zq.dynamicphoto.waterutil.EffectUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 图片编辑水印界面
 */
public class WatermarkActivity extends BaseActivity<IOperateWaterView,
        OperateWaterPresenter<IOperateWaterView>> implements WaterMouldView,IOperateWaterView{
    private static final String TAG = "WatermarkActivity";

    @BindView(R.id.btn_switchbutton)
    SwitchButton btnSwitchbutton;
    @BindView(R.id.imgs_viewpager)
    NoPreloadViewPager viewPager;
    ArrayList<Image> imgs;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.iv_top_pic)
    ImageView ivTopPic;
    @BindView(R.id.iv_next_pic)
    ImageView ivNextPic;
    @BindView(R.id.rg_tab)
    RadioGroup rgTab;
    @BindView(R.id.rb_tab_water)
    RadioButton rbTabWater;
    @BindView(R.id.rb_tab_text)
    RadioButton rbTabText;
    @BindView(R.id.seek_bar)
    SeekBar seekBar;
    private PictureSlidePagerAdapter mAdapter;
    private Dialog selectWaterDialog;
    private Dialog selectTextWaterDialog;
    private SelectWaterPicAdapter mWaterAdapter;
    private WaterMouldSelectDialog selectDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_watermark;
    }

    @Override
    protected void initView() {
        EffectUtil.clear();
        setListener();
        seekBar.setMax(255);
        seekBar.setProgress(255);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //float alpha = (float) progress / 100;
                WaterEvent event = new WaterEvent(3);
                event.setAlpha(progress);
                EventBus.getDefault().post(event);
                Log.i(TAG,"progress = " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected OperateWaterPresenter<IOperateWaterView> createPresenter() {
        return new OperateWaterPresenter<>();
    }

    private void setListener() {
        btnSwitchbutton.setHydropowerListener(hydropowerListener);
        btnSwitchbutton.setSoftFloorListener(softFloorListener);
        imgs = (ArrayList<Image>) getIntent().getSerializableExtra(Constans.SELECT_LIST);
        mAdapter = new PictureSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setOnPageChangeListener(new NoPreloadViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (imgs.size() == 1) {
                    ivNextPic.setVisibility(View.GONE);
                    ivTopPic.setVisibility(View.GONE);
                } else {
                    if ((position + 1) == imgs.size()) {//最后一张
                        ivNextPic.setVisibility(View.GONE);
                    } else {
                        ivNextPic.setVisibility(View.VISIBLE);
                    }
                    if (position == 0) {
                        ivTopPic.setVisibility(View.GONE);
                    } else {
                        ivTopPic.setVisibility(View.VISIBLE);
                    }
                }
                tvNum.setText(String.valueOf(position + 1) + "/" + imgs.size());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    SwitchButton.HydropowerListener hydropowerListener = new SwitchButton.HydropowerListener() {
        @Override
        public void hydropower() {
            ToastUtils.showShort("单张");
        }
    };
    SwitchButton.SoftFloorListener softFloorListener = new SwitchButton.SoftFloorListener() {
        @Override
        public void softFloor() {
            ToastUtils.showShort("批量");
        }
    };

    @OnClick({R.id.layout_back, R.id.iv_top_pic, R.id.iv_next_pic,
            R.id.layout_save, R.id.rb_tab_water, R.id.rb_tab_text})
    public void onClicked(View view) {
        int currentItem = viewPager.getCurrentItem();
        /*ArrayList<String> url = new ArrayList<>();
        for (Image im : imgs) {
            url.add(im.getPath());
        }*/
        switch (view.getId()) {
            case R.id.layout_back:
                WaterEvent event = new WaterEvent(1);
                event.setImage(imgs.get(0));
                EventBus.getDefault().post(event);
                break;
            case R.id.iv_top_pic:
                viewPager.setCurrentItem(currentItem - 1);
                break;
            case R.id.iv_next_pic:
                viewPager.setCurrentItem(currentItem + 1);
                break;
            case R.id.layout_save:
                Log.i("PictureSlideFragment", "save");
                WaterEvent event1 = new WaterEvent(2);
                EventBus.getDefault().post(event1);
                break;
            case R.id.rb_tab_water:
                Log.i("PictureSlideFragment", "rb_tab_water");
                getAddWaterList(1);
                /*url.remove(0);
                showPopWindow(url);*/
                break;
            case R.id.rb_tab_text:
                Log.i("PictureSlideFragment", "rb_tab_text");
                //showTextPopWindow(url);
                getAddWaterList(2);
                break;
        }
    }

    private void showPopWindow(final ArrayList<UserWatermark> urls) {
        selectWaterDialog = new Dialog(this, R.style.MainDialog);
        AutoRelativeLayout root = (AutoRelativeLayout) LayoutInflater.from(this)
                .inflate(R.layout.water_pic_popup_window, null);
        selectWaterDialog.setContentView(root);
        RecyclerView rclWaterPic = root.findViewById(R.id.rcl_water_select);
        final RadioButton btnWater = root.findViewById(R.id.rb_tab_water);
        RadioButton btnText = root.findViewById(R.id.rb_tab_text);
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        rclWaterPic.setLayoutManager(manager);
        mWaterAdapter = new SelectWaterPicAdapter(urls,this);
        rclWaterPic.setAdapter(mWaterAdapter);
        Window dialogWindow = selectWaterDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM); //设置显示在底部
        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f;
        dialogWindow.setAttributes(lp);
        selectWaterDialog.setCanceledOnTouchOutside(true);
        selectWaterDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                rbTabWater.setChecked(false);
                btnWater.setChecked(false);
            }
        });
        selectWaterDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                btnWater.setChecked(true);
            }
        });
        btnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectWaterDialog.isShowing()) {
                    selectWaterDialog.dismiss();
                }
            }
        });
        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectWaterDialog.isShowing()) {
                    selectWaterDialog.dismiss();
                    //showTextPopWindow(urls);
                    getAddWaterList(2);
                }
            }
        });
        selectWaterDialog.show();
    }

    private void showTextPopWindow(final ArrayList<UserWatermark> urls) {
        selectTextWaterDialog = new Dialog(this, R.style.MainDialog);
        AutoRelativeLayout root = (AutoRelativeLayout) LayoutInflater.from(this)
                .inflate(R.layout.water_pic_popup_window, null);
        selectTextWaterDialog.setContentView(root);
        RecyclerView rclWaterPic = root.findViewById(R.id.rcl_water_select);
        RadioButton btnWater = root.findViewById(R.id.rb_tab_water);
        final RadioButton btnText = root.findViewById(R.id.rb_tab_text);
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        rclWaterPic.setLayoutManager(manager);
        mWaterAdapter = new SelectWaterPicAdapter(urls,this);
        rclWaterPic.setAdapter(mWaterAdapter);
        Window dialogWindow = selectTextWaterDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM); //设置显示在底部
        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f;
        dialogWindow.setAttributes(lp);
        selectTextWaterDialog.setCanceledOnTouchOutside(true);
        selectTextWaterDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                btnText.setChecked(false);
                rbTabText.setChecked(false);
            }
        });
        selectTextWaterDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                btnText.setChecked(true);
            }
        });
        btnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectTextWaterDialog.isShowing()) {
                    selectTextWaterDialog.dismiss();
                    //showPopWindow(urls);
                    getAddWaterList(1);
                }
            }
        });
        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectTextWaterDialog.isShowing()) {
                    selectTextWaterDialog.dismiss();
                }
            }
        });
        selectTextWaterDialog.show();
    }

    @Override
    public void showSelectDialog() {
        if (selectDialog == null){
            selectDialog = new WaterMouldSelectDialog(this, R.style.dialog,
                    new WaterMouldSelectDialog.OnItemClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int position) {
                            dialog.dismiss();
                            switch (position) {
                                case 1://水印模板
                                    MFGT.gotoWaterStyleActivity(WatermarkActivity.this);
                                    break;
                                case 2://付费水印设计
                                    break;
                                case 3://相册上传

                                    break;
                            }
                        }
                    }
            );
        }
        selectDialog.show();
    }

    @Override
    public void addWaterImage(Image image) {

    }

    @Override
    public void showAddWaterMouldList(Result result) {
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
            ArrayList<UserWatermark> userWatermarkList = new Gson().fromJson(jsonObject.optString("userWatermarkList"),
                    new TypeToken<List<UserWatermark>>() {
            }.getType());
            int type = jsonObject.optInt("watermarkType");
            if (type == 1){//显示水印弹窗
                showPopWindow(userWatermarkList);
            }else if (type == 2){//显示文字水印弹窗
                showTextPopWindow(userWatermarkList);
            }
            Log.i("userWatermarkList", "userWatermarkList = " + userWatermarkList.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteWaterMould(Result result) {

    }

    public void getAddWaterList(int type) {
        DeviceProperties dr = DrUtils.getInstance();
        UserWatermark userWatermark = new UserWatermark();
        NetRequestBean netRequestBean = new NetRequestBean();
        userWatermark.setWatermarkType(type);
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        userWatermark.setUserId(userId);
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setUserWatermark(userWatermark);
        if (mPresenter != null){
            mPresenter.getAddWaterMouldList(netRequestBean);
        }
    }


    private class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {
        private PictureSlideFragment fragment;

        private PictureSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public PictureSlideFragment getItem(int position) {
            fragment = PictureSlideFragment.newInstance(imgs, imgs.get(position));
            return fragment;
        }

        @Override
        public int getCount() {
            return imgs.size();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(Image image) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
