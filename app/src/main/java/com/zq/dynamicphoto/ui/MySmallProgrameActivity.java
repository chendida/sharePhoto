package com.zq.dynamicphoto.ui;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.DynamicStatic;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.User;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.presenter.GetSmallProgramePresenter;
import com.zq.dynamicphoto.ui.widge.ExpandableTextView;
import com.zq.dynamicphoto.ui.widge.ShareDialog;
import com.zq.dynamicphoto.utils.PicSelectUtils;
import com.zq.dynamicphoto.utils.ShareUtils;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.ILoadView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的小程序码界面
 */
public class MySmallProgrameActivity extends BaseActivity<ILoadView,
        GetSmallProgramePresenter<ILoadView>> implements ILoadView {
    private static final String TAG = "MySmallProgrameActivity";
    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_hint)
    ExpandableTextView tvHint;
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    RequestOptions options = new RequestOptions()
            .circleCrop();
    String userLogo, nick;
    private Boolean avatar = false;//头像图片是否加载完成
    private Boolean smallPrograme = false;//小程序码是否加载完成
    private Bitmap myAvatar, mySmallPrograme;
    String imageName;
    private Bitmap myBitmap;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_small_programe;
    }

    @Override
    protected void initView() {
        layoutBack.setVisibility(View.VISIBLE);
        layoutFinish.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.tv_my_small_program));
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        userLogo = sp.getString(Constans.USERLOGO, "");
        nick = sp.getString(Constans.REMARKNAME, "");
        if (!TextUtils.isEmpty(userLogo)) {
            Glide.with(this)
                    .load(userLogo)
                    .apply(options)
                    .into(ivAvatar);
            Glide.with(this)
                    .asBitmap()
                    .load(userLogo)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource,
                                                    @Nullable Transition<? super Bitmap> transition) {
                            avatar = true;
                            myAvatar = resource;
                            startDraw();
                        }
                    });
        }
        if (!TextUtils.isEmpty(nick)) {
            tvNick.setText(nick);
        }
    }

    private void startDraw() {
        if (avatar && smallPrograme) {//两张都加载完成
            myBitmap = composePic(myAvatar, mySmallPrograme);
            ivPic.setImageBitmap(myBitmap);
        }
    }

    @Override
    protected void initData() {
        getSmallPrograme();
    }

    @Override
    protected GetSmallProgramePresenter<ILoadView> createPresenter() {
        return new GetSmallProgramePresenter<>();
    }

    @Override
    public void showData(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                dealWithResult(result);
            } else {
                ToastUtils.showShort(getResources().getString(R.string.data_error));
            }
        } else {
            ToastUtils.showShort(getResources().getString(R.string.data_error));
        }
    }

    private void dealWithResult(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            String urlCode = jsonObject.optString("urlCode", "");
            ArrayList<DynamicStatic> list = new Gson().fromJson(jsonObject.optString("dynamicStaticList"), new TypeToken<List<DynamicStatic>>() {
            }.getType());
            if (list != null) {
                if (list.size() == 6) {
                    tvHint.setText(list.get(1).getJson());
                }
            }
            drawPic(urlCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void drawPic(String urlCode) {
        if (TextUtils.isEmpty(urlCode)) {
            urlCode = "http://gxxc-1253738394.cos.ap-guangzhou.myqcloud.com/xcx/gxxc/wxacode/10258/15355963217251535596327765.png";
        }
        Log.i(TAG, "urlCode = " + urlCode);
        Glide.with(this)
                .asBitmap()
                .load(urlCode)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        smallPrograme = true;
                        mySmallPrograme = resource;
                        startDraw();
                    }
                });
    }

    public void getSmallPrograme() {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        User user = new User();
        user.setUserId(userId);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setUser(user);
        if (mPresenter != null) {
            mPresenter.getSmallPrograme(netRequestBean);
        }
    }

    /**
     * 合成图片
     *
     * @return 返回带有白色背景框logo
     */
    public Bitmap composePic(Bitmap myAvatar, Bitmap mySmallPrograme) {
        Bitmap bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_small_programe_bg);
        Log.i(TAG, "w = " + mySmallPrograme.getWidth());
        Log.i(TAG, "h = " + mySmallPrograme.getHeight());
        //通过ThumbnailUtils压缩原图片，并指定宽高为背景图的3/4
        mySmallPrograme = zoomImg(mySmallPrograme, 360, 360);
        myAvatar = zoomImg(myAvatar, 84, 84);
        bgBitmap = zoomImg(bgBitmap, 720, 1280);
        Bitmap cvBitmap = Bitmap.createBitmap(720, 1280, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(cvBitmap);
        // 开始合成图片
        canvas.drawBitmap(bgBitmap, 0, 0, null);
        canvas.drawBitmap(myAvatar, (720 - 84) / 2, 530, null);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int color = getResources().getColor(R.color.tv_text_color1);
        paint.setColor(color);
        paint.setTextSize(32);
        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        Rect bounds = new Rect();
        if (nick.length() > 12) {
            nick = nick.substring(0, 12);
        }
        paint.getTextBounds(nick, 0, nick.length(), bounds);
        int x = (720 - bounds.width()) / 2;
        canvas.drawText(nick, x, 650, paint);
        canvas.drawBitmap(mySmallPrograme, (720 - 360) / 2, 670, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);// 保存
        canvas.restore();
        if (cvBitmap.isRecycled()) {
            cvBitmap.recycle();
        }
        saveImage(cvBitmap);
        return cvBitmap;
    }

    private void saveImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        saveBitmap(System.currentTimeMillis()+".jpg", bytes);
    }

    private void saveBitmap(String imgName, byte[] bytes) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileOutputStream fos = null;
            //filePath = null;
            try {
                String filePath = Environment.getExternalStorageDirectory().getCanonicalPath()+ "/共享相册";
                File imgDir = new File(filePath);
                if (!imgDir.exists()) {
                    imgDir.mkdirs();
                }
                imageName = filePath + "/" + imgName;
                fos = new FileOutputStream(imageName);
                fos.write(bytes);
                /*scanPhoto(MyApplication.getAppContext(),imgName);
                ToastUtils.showShort("图片成功保存到"+filePath);*/
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

    // 等比缩放图片
    private Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
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

    @OnClick({R.id.layout_back, R.id.iv_pic, R.id.layout_one_key_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.iv_pic:
                ArrayList<LocalMedia> list = new ArrayList<>();
                if (!TextUtils.isEmpty(imageName)){
                    LocalMedia media = new LocalMedia();
                    media.setPath(imageName);
                    list.add(media);
                }
                PicSelectUtils.getInstance().preview(0,list,this);
                break;
            case R.id.layout_one_key_share:
                if (!TextUtils.isEmpty(imageName)){
                    showShareDialog(imageName);
                }
                break;
        }
    }

    private void showShareDialog(final String imageName) {
        new ShareDialog(this, R.style.dialog, new ShareDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                File[] files = new File[1];
                files[0] = new File(imageName);
                switch (position){
                    case 1:
                        copyText(tvHint.getText().toString());
                        ShareUtils.getInstance().shareToFriend(files,1,MySmallProgrameActivity.this);
                        break;
                    case 2:
                        copyText(tvHint.getText().toString());
                        ShareUtils.getInstance().shareToFriend(files,2,MySmallProgrameActivity.this);
                        break;
                    case 3:
                        insertImageToSystemGallery(MySmallProgrameActivity.this,imageName,myBitmap);
                        break;
                }
            }
        }).show();
    }

    /**
     * 图片插入到系统相册,解决系统图库不能打开图片的问题
     */
    private static void insertImageToSystemGallery(Context context, String filePath, Bitmap bitmap){
        MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "", "");
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setData(uri);
        context.sendBroadcast(intent);
        ToastUtils.showShort("图片已成功保存到共享相册中");
    }

    private void copyText(String content){
        if (!TextUtils.isEmpty(content)) {
            ClipboardManager cmb = (ClipboardManager) MyApplication.getAppContext().getSystemService(CLIPBOARD_SERVICE);
            cmb.setText(content);
            ToastUtils.showShort("内容已复制，分享时可以粘贴");
        }
    }
}
