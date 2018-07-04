package com.zq.dynamicphoto.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.zhy.autolayout.AutoLinearLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.PhotoListAdapter;
import com.zq.dynamicphoto.adapter.SelectedPhotoListAdapter;
import com.zq.dynamicphoto.adapter.WaterPhotoListAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.Folder;
import com.zq.dynamicphoto.bean.Image;
import com.zq.dynamicphoto.bean.ImageModel;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.utils.MFGT;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

public class PhotoSelectActivity extends BaseActivity implements
        WaterPhotoListAdapter.MyClickListener, SelectedPhotoListAdapter.DeleteListener
        ,PhotoListAdapter.SelectListener {
    private static final String TAG = "PhotoSelectActivity";
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
    SelectedPhotoListAdapter selectAdapter;
    ArrayList<Image> imageItems;
    @BindView(R.id.rcl_photo_file)
    RecyclerView rclPhotoFile;
    @BindView(R.id.layout_dialog)
    AutoLinearLayout layoutDialog;
    PhotoListAdapter photoListAdapter;
    ArrayList<Folder> imageBuckets;
    Folder imageBucket;

    public static final int PHOTO_RESULT_CODE = 100;        //标志符，图片的结果码，判断是哪一个Intent
    private Uri mImageUri;                                  //指定的uri
    private String mImageName;                              //保存的图片的名字
    private File mImageFile;                                //图片文件
    private String mImagePath;                   //用于存储最终目录，即根目录 / 要操作（存储文件）的文件

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_select;
    }

    @Override
    protected void initView() {
        if (imageItems == null) {
            imageItems = new ArrayList<>();
        }
        selectAdapter = new SelectedPhotoListAdapter(imageItems, this);
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        rclSelectPhotoList.setLayoutManager(manager);
        rclSelectPhotoList.setAdapter(selectAdapter);
        rclAllList.setLayoutManager(new StaggeredGridLayoutManager(
                3, StaggeredGridLayoutManager.VERTICAL));
        rclAllList.addItemDecoration(new GridSpacingItemDecoration(3, 8, false));
        imageBucket = (Folder) getIntent().getSerializableExtra(Constans.IMAGEBUCKET);
        setAdapter(imageBucket);
    }

    public void setAdapter(Folder imageBucket) {
        if (imageBucket != null) {
            tvTitle.setText(imageBucket.getName());
            mAdapter = new WaterPhotoListAdapter((ArrayList<Image>) imageBucket.getImages(), this);
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

    @OnClick({R.id.layout_back, R.id.layout_select_category, R.id.iv_shot,
            R.id.iv_material,R.id.layout_outside,R.id.iv_start_water_mark})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_start_water_mark:
                MFGT.gotoWatermarkActivity(this);
                break;
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_select_category:
                if (layoutDialog.getVisibility() == View.VISIBLE){
                    layoutDialog.setVisibility(View.GONE);
                    rotate(360);
                }else {
                    layoutDialog.setVisibility(View.VISIBLE);
                    rotate(180);
                    getPhotoFileList();
                }
                break;
            case R.id.iv_shot:
                //检查是否获得写入权限，未获得则向用户请求
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //未获得，向用户请求
                    Log.d(TAG, "无读写权限，开始请求权限。");
                    ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                } else {
                    Log.d(TAG, "有读写权限，准备启动相机。");
                    //启动照相机
                    startCamera();
                }
                break;
            case R.id.iv_material:
                break;
            case R.id.layout_outside:
                layoutDialog.setVisibility(View.GONE);
                rotate(360);
                break;
        }
    }

    /**
     * 返回用户是否允许权限的结果，并处理
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        if (requestCode == 200) {
            //用户允许权限
            if (grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "用户已允许权限，准备启动相机。");
                //启动照相机
                startCamera();
            } else {  //用户拒绝
                Log.d(TAG, "用户已拒绝权限，程序终止。");
                ToastUtils.showShort("程序需要写入权限才能运行");
            }
        }
    }

    /**
     * 启动相机，创建文件，并要求返回uri
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void startCamera() {
        Intent intent = new Intent();
        //指定动作，启动相机
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Log.d(TAG, "指定启动相机动作，完成。");
        //创建文件
        createImageFile();
        Log.d(TAG, "创建图片文件结束。");
        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Log.d(TAG, "添加权限。");
        //获取uri
        mImageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName()+".provider", mImageFile);
        Log.d(TAG, "根据图片文件路径获取uri。");
        //将uri加入到额外数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        Log.d(TAG, "将uri加入启动相机的额外数据。");
        Log.d(TAG, "启动相机...");
        //启动相机并要求返回结果
        startActivityForResult(intent, PHOTO_RESULT_CODE);
        Log.d(TAG, "拍摄中...");
    }

    /**
     * 创建图片文件
     */
    private void createImageFile(){
        Log.d(TAG, "开始创建图片文件...");
        //设置图片文件名（含后缀），以当前时间的毫秒值为名称
        mImageName = Calendar.getInstance().getTimeInMillis() + ".jpg";
        Log.d(TAG, "设置图片文件的名称为："+mImageName);
        //创建图片文件
        File file = new File(imageBucket.getImages().get(0).getPath());
        String absolutePath = file.getParentFile().getAbsolutePath()+"/";
        Log.d(TAG, "absolutePath："+absolutePath);
        mImageFile = new File(absolutePath, mImageName);
        //将图片的绝对路径设置给mImagePath，后面会用到
        mImagePath = mImageFile.getAbsolutePath();
        //按设置好的目录层级创建
        if (!mImageFile.getParentFile().exists()) {
            mImageFile.getParentFile().mkdirs();
        }
        Log.d(TAG, "按设置的目录层级创建图片文件，路径："+mImagePath);
        //不加这句会报Read-only警告。且无法写入SD
        mImageFile.setWritable(true);
        Log.d(TAG, "将图片文件设置可写。");
    }

    /**
     * 处理返回结果。
     * 1、图片
     * 2、音频
     * 3、视频
     *
     * @param requestCode 请求码
     * @param resultCode  结果码 成功 -1 失败 0
     * @param data        返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "拍摄结束。");
        if (resultCode == Activity.RESULT_OK) {
            showLoading();
            Log.d(TAG, "返回成功。");
            Log.d(TAG, "请求码：" + requestCode + "  结果码：" + resultCode + "  data：" + data);
            switch (requestCode) {
                case PHOTO_RESULT_CODE: {
                    Bitmap bitmap = null;
                    try {
                        //根据uri设置bitmap
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                        Log.d(TAG, "根据uri设置bitmap。");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //将图片保存到SD的指定位置
                    savePhotoToSD(bitmap);
                    //更新系统图库
                    //updateSystemGallery();
                    scanPhoto();
                    Log.d(TAG, "结束。");
                    break;
                }
            }
        }
    }

    /**
     * 保存照片到SD卡的指定位置
     */
    private void savePhotoToSD(Bitmap bitmap) {
        Log.d(TAG, "将图片保存到指定位置。");
        //创建输出流缓冲区
        BufferedOutputStream os = null;
        try {
            //设置输出流
            os = new BufferedOutputStream(new FileOutputStream(mImageFile));
            Log.d(TAG, "设置输出流。");
            //压缩图片，100表示不压缩
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            Log.d(TAG, "保存照片完成。");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    hideLoading();
                    //不管是否出现异常，都要关闭流
                    os.flush();
                    os.close();
                    Log.d(TAG, "刷新、关闭流");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解决将图片保存在本地在相册中不能看到图片的问题
     * 让Gallery上能马上看到该图片
     */
    private void scanPhoto() {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.parse("file://" + mImagePath);
        Log.i(TAG,"mImagePath = " + mImagePath);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Image imageItem = new Image(mImagePath,System.currentTimeMillis(),mImageName);
        imageItem.setSelected(true);
        mAdapter.addImageItem(imageItem);
        selectAdapter.addImageItemList(imageItem);
        rclSelectPhotoList.smoothScrollToPosition(selectAdapter.getItemCount() - 1);
        updateSelectNum();
    }

    private void rotate(int rotate) {
        ivSanjiao.setPivotX(ivSanjiao.getWidth()/2);
        ivSanjiao.setPivotY(ivSanjiao.getHeight()/2);//支点在图片中心
        ivSanjiao.setRotation(rotate);
    }

    @Override
    public void clickListener(Image imageItem, Boolean isAdd) {
        if (isAdd) {//添加选择的图片
            selectAdapter.addImageItemList(imageItem);
            rclSelectPhotoList.smoothScrollToPosition(selectAdapter.getItemCount() - 1);
        } else {//删除选择的图片
            Log.i(TAG,"delete");
            selectAdapter.deleteImageItemList(imageItem);
        }
        updateSelectNum();
    }

    private void updateSelectNum() {
        if (selectAdapter != null) {
            tvSelectNum.setText(selectAdapter.getItemCount() + "");
        }
    }

    @Override
    public void deleteItem(Image imageItem) {
        mAdapter.deleteImageItem(imageItem);
        updateSelectNum();
    }

    public void getPhotoFileList() {
        if (imageBuckets == null){
            imageBuckets = new ArrayList<>();
        }
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        rclPhotoFile.setLayoutManager(manager);
        photoListAdapter = new PhotoListAdapter(imageBuckets,true,this);
        rclPhotoFile.setAdapter(photoListAdapter);
        rclPhotoFile.setNestedScrollingEnabled(false);
        rclPhotoFile.setHasFixedSize(true);
        ImageModel.loadImageForSDCard(this, new ImageModel.DataCallback() {
            @Override
            public void onSuccess(final ArrayList<Folder> folders) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        photoListAdapter.initFolders(folders);
                    }
                });
            }
        });
    }

    @Override
    public void selectListener(Folder imageBucket) {
        rotate(360);
        layoutDialog.setVisibility(View.GONE);
        this.imageBucket = imageBucket;
        for (Image image:imageBucket.getImages()) {
            for (Image i:selectAdapter.getmList()) {
                if (image.getPath().equals(i.getPath())){
                    image.setSelected(true);
                }
            }
        }
        setAdapter(this.imageBucket);
    }
}
