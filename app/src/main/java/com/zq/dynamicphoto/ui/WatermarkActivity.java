package com.zq.dynamicphoto.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.SelectWaterPicAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.Image;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserWatermark;
import com.zq.dynamicphoto.bean.WaterEvent;
import com.zq.dynamicphoto.bean.WaterImage;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.fragment.PictureSlideFragment;
import com.zq.dynamicphoto.presenter.OperateWaterPresenter;
import com.zq.dynamicphoto.ui.widge.NoPreloadViewPager;
import com.zq.dynamicphoto.ui.widge.SaveImageUtils;
import com.zq.dynamicphoto.ui.widge.SelectDialog;
import com.zq.dynamicphoto.ui.widge.SelectPicDialog;
import com.zq.dynamicphoto.ui.widge.SwitchButton;
import com.zq.dynamicphoto.ui.widge.WaterMouldSelectDialog;
import com.zq.dynamicphoto.utils.FastClickUtils;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.IOperateWaterView;
import com.zq.dynamicphoto.view.SaveWaterImage;
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
        OperateWaterPresenter<IOperateWaterView>> implements
        WaterMouldView, IOperateWaterView/*, SaveWaterImage */{
    private static final String TAG = "WatermarkActivity";

    @BindView(R.id.imgs_viewpager)
    NoPreloadViewPager viewPager;
    ArrayList<Image> imgs;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.iv_top_pic)
    ImageView ivTopPic;
    @BindView(R.id.iv_next_pic)
    ImageView ivNextPic;
    @BindView(R.id.seek_bar)
    SeekBar seekBar;
    @BindView(R.id.check_water)
    CheckBox checkWater;
    private PictureSlidePagerAdapter mAdapter;
    private Dialog selectWaterDialog;
    private SelectWaterPicAdapter mWaterAdapter;
    private WaterMouldSelectDialog selectDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_watermark;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
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
                Log.i(TAG, "progress = " + progress);
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

    @OnClick({R.id.layout_back, R.id.iv_top_pic, R.id.iv_next_pic,
            R.id.layout_save, R.id.layout_water, R.id.check_water})
    public void onClicked(View view) {
        int currentItem = viewPager.getCurrentItem();
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.iv_top_pic:
                viewPager.setCurrentItem(currentItem - 1);
                break;
            case R.id.iv_next_pic:
                viewPager.setCurrentItem(currentItem + 1);
                break;
            case R.id.layout_save:
                if (!FastClickUtils.isFastDoubleClick()) {
                    showHintDialog();
                }
                break;
            case R.id.layout_water:
            case R.id.check_water:
                checkWater.setChecked(true);
                getAddWaterList(1);
                break;
            /*case R.id.rb_tab_text:
                getAddWaterList(2);
                break;*/
        }
    }

    private void showHintDialog() {
        Log.i(TAG, "save");
        new SelectDialog(this, R.style.dialog, new SelectDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                if (position == 1) {
                    WaterEvent event1 = new WaterEvent(2);
                    EventBus.getDefault().post(event1);
                }
            }
        }, "确认保存？").show();
    }

    private void showPopWindow(final ArrayList<UserWatermark> urls) {
        selectWaterDialog = new Dialog(this, R.style.MainDialog);
        AutoRelativeLayout root = (AutoRelativeLayout) LayoutInflater.from(this)
                .inflate(R.layout.water_pic_popup_window, null);
        selectWaterDialog.setContentView(root);
        RecyclerView rclWaterPic = root.findViewById(R.id.rcl_water_select);
        TextView cancel = root.findViewById(R.id.layout_cancel);
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        rclWaterPic.setLayoutManager(manager);
        mWaterAdapter = new SelectWaterPicAdapter(urls, this);
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
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectWaterDialog.dismiss();
            }
        });
        selectWaterDialog.show();
        selectWaterDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                checkWater.setChecked(true);
            }
        });

        selectWaterDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                checkWater.setChecked(false);
            }
        });
    }


    @Override
    public void showSelectDialog() {
        if (selectDialog == null) {
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
                                    intoPicSelect();
                                    break;
                            }
                        }
                    }
            );
        }
        selectDialog.show();
    }

    private void intoPicSelect() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(1)
                .previewImage(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 接收图片选择器返回结果，更新所选图片集合
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
                    String path = localMedia.get(0).getPath();
                    Image image = new Image(path);
                    addWaterImage(image);
                    break;
            }
        }
    }

    @Override
    public void addWaterImage(Image image) {
        WaterEvent waterEvent = new WaterEvent(1);
        waterEvent.setImage(image);
        EventBus.getDefault().post(waterEvent);
    }

    @Override
    public void showLongClickOperate(UserWatermark image) {
        showSelectPicDialog(image);
    }

    private void showSelectPicDialog(final UserWatermark image) {
        new SelectPicDialog("存入相册", "删除", this, R.style.dialog, new SelectPicDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                switch (position) {
                    case 1://选择存入相册
                        SaveImageUtils.saveImageByGlide(image.getWatermarkUrl());
                        break;
                    case 2://选择删除水印
                        deleteWatermark(image);
                        break;
                }
            }
        }).show();
    }

    private void deleteWatermark(UserWatermark image) {
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(DrUtils.getInstance());
        netRequestBean.setUserWatermark(image);
        if (mPresenter != null) {
            mPresenter.deleteWaterMould(netRequestBean);
        }
    }

    @Override
    public void showAddWaterMouldList(Result result) {
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
            ArrayList<UserWatermark> userWatermarkList = new Gson().fromJson(jsonObject.optString("userWatermarkList"),
                    new TypeToken<List<UserWatermark>>() {
                    }.getType());
            int type = jsonObject.optInt("watermarkType");
            if (type == 1) {//显示水印弹窗
                showPopWindow(userWatermarkList);
            } else if (type == 2) {//显示文字水印弹窗
                //showTextPopWindow(userWatermarkList);
            }
            Log.i("userWatermarkList", "userWatermarkList = " + userWatermarkList.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteWaterMould(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                if (selectWaterDialog.isShowing()) {
                    selectWaterDialog.dismiss();
                }
                getAddWaterList(1);
            } else {
                showFailed();
            }
        } else {
            showFailed();
        }
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
        if (mPresenter != null) {
            mPresenter.getAddWaterMouldList(netRequestBean);
        }
    }

    /*@Override
    public void startSave(final int size) {
        Log.i(TAG, "startSave");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layoutProcess.setVisibility(View.VISIBLE);
                String str = 0 + "/" + size + ")";
                tvNum.setText(str);
                seekBarProcess.setMax(size);
            }
        });
    }

    @Override
    public void saveProcess(final int num, final int size) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String str = num + "/" + size + ")";
                tvNum.setText(str);
                seekBarProcess.setProgress(num);
            }
        });
    }

    @Override
    public void endSave() {
        layoutProcess.setVisibility(View.GONE);
        ToastUtils.showShort("保存成功");
    }*/


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
    public void Event(WaterImage image) {
        Log.i(TAG, "image = " + image.getUrl());
        WaterEvent waterEvent = new WaterEvent(1);
        waterEvent.setImage(new Image(image.getUrl()));
        EventBus.getDefault().post(waterEvent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
