package com.zq.dynamicphoto.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.PhotoListAdapter;
import com.zq.dynamicphoto.adapter.SelectPhotoAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.Folder;
import com.zq.dynamicphoto.bean.ImageModel;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.checkphoto.AlbumBean;

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
    private ArrayList<AlbumBean> imageBuckets;
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
        tvTitle.setText("系统相册");
        layoutFinish.setVisibility(View.GONE);
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
        mAdapter = new PhotoListAdapter(imageBuckets,true,this);
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
        AlbumBean.getAllAlbumFromLocalStorage(this, new AlbumBean.AlbumListCallback() {
            @Override
            public void onSuccess(ArrayList<AlbumBean> albumList) {
                mAdapter.initFolders(albumList);
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
                MFGT.gotoHomeActivity(this);
                break;
            case R.id.layout_finish:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MFGT.gotoHomeActivity(this);
    }

    @Override
    public void selectListener(final AlbumBean folder) {
        AlbumBean.getAlbumPhotosFromLocalStorage(this, folder, new AlbumBean.AlbumPhotosCallback() {
            @Override
            public void onSuccess(ArrayList<SelectPhotoAdapter.SelectPhotoEntity> photos) {
                MFGT.gotoWaterPhotoListActivity(PhotoListActivity.this,photos,true,folder.getFolderName());
            }
        });
    }
}
