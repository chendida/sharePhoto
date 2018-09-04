package com.zq.dynamicphoto.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.AlbumListAdapter;
import com.zq.dynamicphoto.adapter.SelectPhotoAdapter;
import com.zq.dynamicphoto.adapter.SelectedPhotoListAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.Image;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.checkphoto.AlbumBean;
import com.zq.dynamicphoto.utils.checkphoto.AlxPermissionHelper;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

public class PhotoSelectActivity extends BaseActivity
        implements SelectPhotoAdapter.CallBackActivity,SelectedPhotoListAdapter.DeleteListener {
    @BindView(R.id.tv_album_name)
    TextView tv_album_name;
    @BindView(R.id.layout_select)
    AutoRelativeLayout layoutSelect;
    @BindView(R.id.tv_select_num)
    TextView tv_select_num;
    @BindView(R.id.rl_album)
    RelativeLayout rl_album;
    @BindView(R.id.lv_albumlist)
    ListView lv_albumlist;
    @BindView(R.id.iv_showalbum)
    ImageView iv_showalbum;
    @BindView(R.id.gv_photo)
    GridView gvPhotoList;
    @BindView(R.id.rcl_select_photo_list)
    RecyclerView rclSelectedPhoto;
    SelectedPhotoListAdapter selectedPhotoListAdapter;
    SelectPhotoAdapter allPhotoAdapter = null;
    AlxPermissionHelper permissionHelper = new AlxPermissionHelper();
    ArrayList<SelectPhotoAdapter.SelectPhotoEntity> selectedPhotoList = new ArrayList<>();//用于放置即将要发送的photo
    private AlbumListAdapter albumListAdapter;
    private List<AlbumBean> albumList = new ArrayList<>();//相册列表
    boolean isShowAlbum;
    public static final int SELECT_PHOTO_OK = 20;//选择照片成功的result code
    private Boolean isHide = false;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_photo;
    }

    @Override
    protected void initView() {
        isHide = getIntent().getBooleanExtra(Constans.IS_HIDE,false);
        Log.i("isHide","isHide = " + isHide);
        if (isHide){
            layoutSelect.setVisibility(View.GONE);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        rclSelectedPhoto.setLayoutManager(manager);
        selectedPhotoListAdapter = new SelectedPhotoListAdapter(selectedPhotoList,this);
        rclSelectedPhoto.setAdapter(selectedPhotoListAdapter);
        allPhotoAdapter = new SelectPhotoAdapter(this,
                new ArrayList<SelectPhotoAdapter.SelectPhotoEntity>(),isHide);
        gvPhotoList.setAdapter(allPhotoAdapter);
        //选择相册的布局
        ArrayList<SelectPhotoAdapter.SelectPhotoEntity> photos =
                (ArrayList<SelectPhotoAdapter.SelectPhotoEntity>)
                        getIntent().getSerializableExtra(Constans.IMAGEBUCKET);

        String folderName = getIntent().getStringExtra(Constans.FOLDERNAME);
        tv_album_name.setText(folderName);
        allPhotoAdapter.allPhotoList.clear();//因为是ArrayAdapter，所以引用不能重置
        allPhotoAdapter.allPhotoList.addAll(photos);
        allPhotoAdapter.notifyDataSetChanged();
        //检查权限,获取权限之后将手机所有注册图片搜索出来，并按照相册进行分类
        permissionHelper.checkPermission(this, new AlxPermissionHelper.AskPermissionCallBack() {
            @Override
            public void onSuccess() {
                AlbumBean.getAllAlbumFromLocalStorage(PhotoSelectActivity.this, new AlbumBean.AlbumListCallback() {
                    @Override
                    public void onSuccess(ArrayList<AlbumBean> result) {
                        albumList.addAll(result);
                        albumListAdapter = new AlbumListAdapter(PhotoSelectActivity.this, albumList);
                        lv_albumlist.setAdapter(albumListAdapter);
                    }
                });
            }

            @Override
            public void onFailed() {
                PhotoSelectActivity.this.finish();
            }
        });

        lv_albumlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (albumList != null && position < albumList.size() && albumList.get(position) != null) {//满足条件才能set
                    tv_album_name.setText(albumList.get(position).folderName);
                }
                isShowAlbum = false;
                hideAlbum();
                AlbumBean.getAlbumPhotosFromLocalStorage(PhotoSelectActivity.this, albumList.get(position), new AlbumBean.AlbumPhotosCallback() {
                    @Override
                    public void onSuccess(ArrayList<SelectPhotoAdapter.SelectPhotoEntity> photos) {
                        Log.i("Alex", "new photo list是" + photos);
                        allPhotoAdapter.allPhotoList.clear();//因为是ArrayAdapter，所以引用不能重置
                        //allPhotoAdapter.allPhotoList.addAll(photos);
                        allPhotoAdapter.allPhotoList.addAll(photos);
                        allPhotoAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @Override
    public void remove(SelectPhotoAdapter.SelectPhotoEntity entity) {
        if (selectedPhotoListAdapter != null){
            selectedPhotoListAdapter.deleteImageItemList(entity);
            if (selectedPhotoListAdapter.getItemCount() > 1) {
                rclSelectedPhoto.smoothScrollToPosition(selectedPhotoListAdapter.getItemCount() - 1);
            }
            tv_select_num.setText(String.valueOf(selectedPhotoListAdapter.getItemCount()));
        }
    }

    @Override
    public void add(SelectPhotoAdapter.SelectPhotoEntity entity) {
        if (selectedPhotoListAdapter != null){
            selectedPhotoListAdapter.addImageItemList(entity);
            rclSelectedPhoto.smoothScrollToPosition(selectedPhotoListAdapter.getItemCount()-1);
            tv_select_num.setText(String.valueOf(selectedPhotoListAdapter.getItemCount()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.registActivityResult(requestCode, permissions, grantResults);
    }

    /**
     * 隐藏相册选择页
     */
    void hideAlbum() {
        if (Build.VERSION.SDK_INT >= 11) {
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(rl_album, "alpha", 1.0f, 0.0f);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(iv_showalbum, "rotationX", 180f, 360f);
            ObjectAnimator animator3 = ObjectAnimator.ofFloat(rl_album, "translationY", -dp2Px(45));
            AnimatorSet set = new AnimatorSet();
            set.setDuration(300).playTogether(animator1, animator2, animator3);
            set.start();
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    rl_album.setVisibility(View.GONE);
                }
            });
        } else {
            rl_album.setVisibility(View.GONE);
        }
        isShowAlbum = false;
    }

    /**
     * 显示相册选择页
     */
    void showAlbum() {
        if (Build.VERSION.SDK_INT >= 11) {
            rl_album.setVisibility(View.VISIBLE);//一定要先顯示，才能做動畫操作
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(rl_album, "alpha", 0.0f, 1.0f);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(iv_showalbum, "rotationX", 0f, 180f);
            ObjectAnimator animator3 = ObjectAnimator.ofFloat(rl_album, "translationY", dp2Px(45));
            AnimatorSet set = new AnimatorSet();
            set.setDuration(300).playTogether(animator1, animator2, animator3);
            set.start();
        } else {
            rl_album.setVisibility(View.VISIBLE);//一定要先顯示，才能做動畫操作
        }
        isShowAlbum = true;
    }

    public int dp2Px(int dp) {
        try {
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            return (int) (dp * metric.density + 0.5f);
        } catch (Exception e) {
            e.printStackTrace();
            return dp;
        }
    }

    @OnClick({R.id.rl_head, R.id.tv_cancel,R.id.rl_album,R.id.iv_start_water_mark})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_head:
                if (isShowAlbum) hideAlbum();//现在是展示album的状态
                else showAlbum();//现在是关闭（正常）状态
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.rl_album:
                hideAlbum();
                break;
            case R.id.iv_start_water_mark:
                if (selectedPhotoListAdapter != null){
                    if (selectedPhotoListAdapter.getItemCount() == 0){
                        ToastUtils.showShort(getResources().getString(R.string.please_least_select_pic));
                    }else {
                        ArrayList<Image> images = new ArrayList<>();
                        for (SelectPhotoAdapter.SelectPhotoEntity entity:selectedPhotoListAdapter.getmList()) {
                            images.add(new Image(entity.url));
                        }
                        MFGT.gotoWatermarkActivity(this,images);
                    }
                }
                break;
        }
    }

    @Override
    public void deleteItem(SelectPhotoAdapter.SelectPhotoEntity imageItem) {
        allPhotoAdapter.selectedPhotosSet.remove(imageItem);
        if (selectedPhotoListAdapter != null)
        tv_select_num.setText(String.valueOf(selectedPhotoListAdapter.getItemCount()));
    }
}
