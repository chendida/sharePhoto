package com.zq.dynamicphoto.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.WaterPhotoListAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.ImageBucket;
import com.zq.dynamicphoto.bean.ImageItem;
import com.zq.dynamicphoto.common.Constans;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class PhotoSelectActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_select_num)
    TextView tvSelectNum;
    @BindView(R.id.rcl_select_photo_list)
    RecyclerView rclSelectPhotoList;
    @BindView(R.id.rcl_all_list)
    RecyclerView rclAllList;
    @BindView(R.id.iv_sanjiao)
    ImageView ivSanjiao;
    WaterPhotoListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_select;
    }

    @Override
    protected void initView() {
        rclAllList.setLayoutManager(new StaggeredGridLayoutManager(
                3,StaggeredGridLayoutManager.VERTICAL));
        rclAllList.addItemDecoration(new GridSpacingItemDecoration(3,8,false));
        ImageBucket imageBucket = (ImageBucket) getIntent().getSerializableExtra(Constans.IMAGEBUCKET);
        if (imageBucket != null){
            tvTitle.setText(imageBucket.getBucketName());
            mAdapter = new WaterPhotoListAdapter((ArrayList<ImageItem>) imageBucket.getImageList());
            rclAllList.setAdapter(mAdapter);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @OnClick({R.id.layout_back, R.id.layout_select_category, R.id.iv_shot, R.id.iv_material})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_select_category:
                break;
            case R.id.iv_shot:
                break;
            case R.id.iv_material:
                break;
        }
    }
}
