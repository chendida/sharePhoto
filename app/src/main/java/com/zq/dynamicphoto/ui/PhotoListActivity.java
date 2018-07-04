package com.zq.dynamicphoto.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.PhotoListAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.Folder;
import com.zq.dynamicphoto.bean.ImageModel;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 相册列表界面
 */
public class PhotoListActivity extends BaseActivity implements PhotoListAdapter.SelectListener{
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
    private ArrayList<Folder> imageBuckets;
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
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        rclPhotoDirList.setLayoutManager(manager);
        mAdapter = new PhotoListAdapter(imageBuckets,false,this);
        rclPhotoDirList.setAdapter(mAdapter);
        rclPhotoDirList.setNestedScrollingEnabled(false);
        rclPhotoDirList.setHasFixedSize(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadImageForSDCard();
    }

    /**
     * 从SDCard加载图片。
     */
    private void loadImageForSDCard() {
        ImageModel.loadImageForSDCard(this, new ImageModel.DataCallback() {
            @Override
            public void onSuccess(final ArrayList<Folder> folders) {
                //folders是图片文件夹的列表，每个文件夹中都有若干张图片。
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.initFolders(folders);
                    }
                });
            }
        });
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

    @Override
    public void selectListener(Folder imageBucket) {

    }
}
