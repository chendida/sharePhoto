package com.zq.dynamicphoto.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.PhotoListAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.ImageBucket;
import com.zq.dynamicphoto.bean.ImageItem;
import com.zq.dynamicphoto.bean.ImageProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 相册列表界面
 */
public class PhotoListActivity extends BaseActivity {
    private static final String TAG = "PhotoListActivity";
    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.rcl_photo_dir_list)
    RecyclerView rclPhotoDirList;
    private ArrayList<ImageBucket> imageBuckets;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    private PhotoListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_list;
    }

    @Override
    protected void initView() {
        layoutBack.setVisibility(View.VISIBLE);
        ivCamera.setVisibility(View.GONE);
        tvTitle.setText("系统相册");
        tvFinish.setText("素材库");
        tvFinish.setTextColor(getResources().getColor(R.color.tv_text_color3));
        layoutFinish.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageBuckets = null;
    }

    @Override
    protected void initData() {
        if (imageBuckets == null){
            imageBuckets = new ArrayList<>();
        }
        imageBuckets = (ArrayList<ImageBucket>) ImageProvider.getInstance().getImageBucketList();
        Log.i(TAG,"size = " + imageBuckets.size());
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        rclPhotoDirList.setLayoutManager(manager);
        if (imageBuckets.size() == 0) {
            ToastUtils.showShort("您当前相册里面无照片");
        } else {
            Boolean flag = false;
            for (ImageBucket images:imageBuckets) {
                if (images.getBucketName().equals("全部照片")){
                    flag = true;
                    break;
                }
            }
            if (!flag){
                Collections.reverse(imageBuckets);
                ImageBucket imageBucket = new ImageBucket();
                imageBucket.setBucketName("全部照片");
                ArrayList<ImageItem> list = new ArrayList<>();
                for (ImageBucket bucket : imageBuckets) {
                    list.addAll(bucket.getImageList());
                }
                imageBucket.setImageList(list);
                imageBuckets.add(0, imageBucket);
            }
            mAdapter = new PhotoListAdapter(imageBuckets);
            rclPhotoDirList.setAdapter(mAdapter);
            rclPhotoDirList.setNestedScrollingEnabled(false);
            rclPhotoDirList.setHasFixedSize(true);
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @OnClick({R.id.layout_back, R.id.layout_finish})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_finish:
                break;
        }
    }

}
