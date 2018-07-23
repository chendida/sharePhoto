package com.zq.dynamicphoto.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.photoview.PhotoViewAttacher;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.Image;
import com.zq.dynamicphoto.bean.WaterEvent;
import com.zq.dynamicphoto.utils.ImageSaveUtils;
import com.zq.dynamicphoto.utils.SaveTasks;
import com.zq.dynamicphoto.waterutil.EffectUtil;
import com.zq.dynamicphoto.waterutil.customview.MyHighlightView;
import com.zq.dynamicphoto.waterutil.customview.MyImageViewDrawableOverlay;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/1/3.
 */
public class PictureSlideFragment extends BaseFragment implements ImageSaveUtils.DownLoadListener{
    private static final String TAG = "PictureSlideFragment";
    @BindView(R.id.drawing_view_container)
    AutoRelativeLayout drawArea;
    @BindView(R.id.iv_main_pic)
    ImageView imageView;
    private static String url;
    private static Image image;
    private PhotoViewAttacher mAttacher;
    private MyImageViewDrawableOverlay mImageView;
    private static ArrayList<Image>images = new ArrayList<>();
    public static PictureSlideFragment newInstance(ArrayList<Image> imgs,Image i) {
        PictureSlideFragment f = new PictureSlideFragment();
        image = i;
        images.clear();
        images.addAll(imgs);
        Bundle args = new Bundle();
        url = image.getPath();
        BitmapFactory.Options options = new BitmapFactory.Options();
        args.putString("url", url);
        f.setArguments(args);
        return f;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_picture_slide;
    }

    @Override
    protected void initView(View view) {
    }


    @Override
    protected void initData() {
        url = getArguments() != null ? getArguments().getString("url") : "http://www.zhagame.com/wp-content/uploads/2016/01/JarvanIV_6.jpg";
        mAttacher = new PhotoViewAttacher(imageView);
        if (getActivity() != null) {
            Glide.with(getActivity()).load(url).into(imageView);
            mAttacher.update();
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void loadData() {
        getPicHeightAndWidth();
    }


    public void getPicHeightAndWidth() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        int width = MyApplication.getAppContext().getResources().getDisplayMetrics().widthPixels;
        int height = width * options.outHeight / options.outWidth;
        Log.i(TAG,"options.outHeight = " + options.outHeight);
        Log.i(TAG,"height = " + height);
        initView(width,height);
    }

    private void initView(int width,int height) {
        AutoRelativeLayout.LayoutParams rlp=new AutoRelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        int heightPixels = MyApplication.getAppContext().getResources().getDisplayMetrics().heightPixels;//屏幕高度
        float scale = (float) heightPixels / width;//倍数
        rlp.topMargin = (int) ((heightPixels - (196 * scale + height))/2);
        rlp.bottomMargin = rlp.topMargin;
        //添加贴纸水印的画布
        View overlay = LayoutInflater.from(MyApplication.getAppContext()).inflate(
                R.layout.view_drawable_overlay, null);
        mImageView = (MyImageViewDrawableOverlay) overlay.findViewById(R.id.drawable_overlay);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width,height);
        mImageView.setLayoutParams(params);
        overlay.setLayoutParams(params);
        drawArea.addView(overlay,rlp);
        initCanvasWater();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(WaterEvent event) {
        Log.i(TAG,"start save");
        if (event.getType() == 1){
            if (event.getImage() != null){
                if (!TextUtils.isEmpty(event.getImage().getPath())){
                    initStickerToolBar(event.getImage());
                }
            }
        }else if (event.getType() == 2){
            savePicture();
        }else if (event.getType() == 3){
            mImageView.getSelectedHighlightView().getContent().setAlpha(event.getAlpha());
            mImageView.invalidate();
        }
    }

    //保存图片
    private void savePicture() {
        if (EffectUtil.getHightlistViews() != null){
            if (EffectUtil.getHightlistViews().size() == 0){//未加水印情况
                Log.i(TAG,"未加水印情况");
                showLoading();
                ArrayList<LocalMedia> list = new ArrayList<>();
                for (Image image:images) {
                    LocalMedia localMedia = new LocalMedia();
                    localMedia.setPath(image.getPath());
                    list.add(localMedia);
                }
                ImageSaveUtils.getInstance(this).saveAllImage(list);
            }else {//加水印情况的保存
                saveWaterBitmap();
            }
        }else {//加水印情况的保存

        }
    }

    private void saveWaterBitmap() {
        //showLoading();
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        bitmaps.clear();
        Log.i(TAG,"image.size = " + images.size());
        for (Image image:images) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            //options.inJustDecodeBounds = true;// 设置为ture,只读取图片的大小，不把它加载到内存中去
            Bitmap bitmap = BitmapFactory.decodeFile(image.getPath(), options);
            //加滤镜
            int width = MyApplication.getAppContext().getResources().getDisplayMetrics().widthPixels;//屏幕宽度
            float scale = (float) width / options.outWidth;//倍数
            final Bitmap newBitmap = Bitmap.createBitmap((int) (options.outWidth*scale), (int) (options.outHeight*scale),
                    Bitmap.Config.ARGB_8888);
            Canvas cv = new Canvas(newBitmap);
            //在画布 0，0坐标上开始绘制原始图片
            if (bitmap == null){
                Log.i(TAG,"bitmap is null");
                return;
            }
            cv.drawBitmap(bitmap, 0, 0, null);
            RectF dst = new RectF(0, 0,(int) (options.outWidth*scale), (int) (options.outHeight*scale));
            cv.drawBitmap(bitmap, null, dst, null);
            //加贴纸水印
            EffectUtil.applyOnSave(cv, mImageView,(int) (options.outWidth*scale), (int) (options.outHeight*scale));
            bitmaps.add(zoomImg(newBitmap,options.outWidth,options.outHeight));
            bitmap.recycle();
        }
        for (Bitmap bitmap:bitmaps) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bytes = baos.toByteArray();
            savaBitmap(System.currentTimeMillis()+".jpg",bytes);
        }
        //hideLoading();
        ToastUtils.showShort("保存成功");
    }

    // 等比缩放图片
    private Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        Log.i(TAG,"width = " + width);
        Log.i(TAG,"height = " + height);
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }


    private void savaBitmap(String imgName, byte[] bytes) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileOutputStream fos = null;
            //filePath = null;
            try {
                String filePath = Environment.getExternalStorageDirectory().getCanonicalPath()+ "/共享相册";
                File imgDir = new File(filePath);
                if (!imgDir.exists()) {
                    imgDir.mkdirs();
                }
                imgName = filePath + "/" + imgName;
                fos = new FileOutputStream(imgName);
                fos.write(bytes);
                scanPhoto(MyApplication.getAppContext(),imgName);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            ToastUtils.showShort("请检查SD卡是否可用");
        }
    }

    /**
     * * 解决将图片保存在本地在相册中不能看到图片的问题
     * 让Gallery上能马上看到该图片
     */
    private void scanPhoto(Context context, String imgFileName) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFileName);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            EventBus.getDefault().register(this);
        }else {
            EventBus.getDefault().unregister(this);
        }
    }

    public void initCanvasWater(){
        if (mImageView == null){
            Log.i(TAG, "mImageView == null");
            return;
        }
        Log.i(TAG, "mImageView != null");
        mImageView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (EffectUtil.getHightlistViews() != null){
                            Log.i(TAG,"EffectUtil.getHightlistViews().size() = "
                                    + EffectUtil.getHightlistViews().size());
                            if (EffectUtil.getHightlistViews().size() != 0){
                                for (MyHighlightView view :EffectUtil.getHightlistViews()) {
                                    mImageView.addHighlightView(mImageView.getWidth(),
                                            mImageView.getHeight(),view,0);
                                }
                            }
                        }
                        mImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                });
    }

    //初始化贴图
    private void initStickerToolBar(Image image) {
        if (this.isVisible()) {
            if (mImageView == null) {
                Log.i(TAG, "mImageView == null");
                return;
            }
            EffectUtil.addStickerImage(mImageView, image,
                    new EffectUtil.StickerCallback() {
                        @Override
                        public void onRemoveSticker(Image sticker) {
                        }
                    });
        }
    }

    @Override
    public void callBack(int code, String msg) {
        hideLoading();
        ToastUtils.showShort(msg);
    }
}
