package com.zq.dynamicphoto.ui;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.DynamicPhoto;
import com.zq.dynamicphoto.bean.DynamicStatic;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.User;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.fragment.DynamicFragment;
import com.zq.dynamicphoto.presenter.GetSmallProgramePresenter;
import com.zq.dynamicphoto.ui.widge.ExpandableTextView;
import com.zq.dynamicphoto.ui.widge.SaveImageUtils;
import com.zq.dynamicphoto.ui.widge.ShareDialog;
import com.zq.dynamicphoto.utils.CodeUtils;
import com.zq.dynamicphoto.utils.ImageSaveUtils;
import com.zq.dynamicphoto.utils.PicSelectUtils;
import com.zq.dynamicphoto.utils.ShareUtils;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.ILoadView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyTwoCodeActivity extends BaseActivity<ILoadView,
        GetSmallProgramePresenter<ILoadView>> implements ILoadView,ImageSaveUtils.DownLoadListener {
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
    @BindView(R.id.iv_avatar2)
    ImageView ivAvatar2;
    @BindView(R.id.tv_nick2)
    TextView tvNick2;
    @BindView(R.id.tv_hint2)
    ExpandableTextView tvHint2;
    @BindView(R.id.iv_pic2)
    ImageView ivPic2;
    @BindView(R.id.iv_avatar3)
    ImageView ivAvatar3;
    @BindView(R.id.tv_nick3)
    TextView tvNick3;
    @BindView(R.id.tv_hint3)
    ExpandableTextView tvHint3;
    @BindView(R.id.iv_pic3)
    ImageView ivPic3;

    @BindView(R.id.iv_avatar4)
    ImageView ivAvatar4;
    @BindView(R.id.tv_nick4)
    TextView tvNick4;
    @BindView(R.id.tv_hint4)
    ExpandableTextView tvHint4;
    @BindView(R.id.iv_pic41)
    ImageView ivPic41;
    @BindView(R.id.iv_pic42)
    ImageView ivPic42;
    @BindView(R.id.iv_avatar5)
    ImageView ivAvatar5;
    @BindView(R.id.tv_nick5)
    TextView tvNick5;
    @BindView(R.id.tv_hint5)
    ExpandableTextView tvHint5;
    @BindView(R.id.iv_pic51)
    ImageView ivPic51;
    @BindView(R.id.iv_pic52)
    ImageView ivPic52;
    private Boolean avatar = false;//头像图片是否加载完成
    private Boolean twoCode = false;//二维码是否加载完成
    private Bitmap myAvatar, myTwoCode;
    String imageName1, imageName2, imageName3;
    private Bitmap myBitmap, myBitmap2, myBitmap3;
    String userLogo, nick;
    RequestOptions options = new RequestOptions()
            .circleCrop();
    private String rz = "http://srsoure.redshoping.cn/common/rz.png";
    private String sl = "http://srsoure.redshoping.cn/common/sus.png";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_two_code;
    }

    @Override
    protected void initView() {
        layoutBack.setVisibility(View.VISIBLE);
        layoutFinish.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.tv_my_two_code));
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        userLogo = sp.getString(Constans.USERLOGO, "");
        nick = sp.getString(Constans.REMARKNAME, "");
        if (!TextUtils.isEmpty(userLogo)) {
            Glide.with(this)
                    .load(userLogo)
                    .apply(options)
                    .into(ivAvatar);
            Glide.with(this)
                    .load(userLogo)
                    .apply(options)
                    .into(ivAvatar2);
            Glide.with(this)
                    .load(userLogo)
                    .apply(options)
                    .into(ivAvatar3);
            Glide.with(this)
                    .load(userLogo)
                    .apply(options)
                    .into(ivAvatar4);
            Glide.with(this)
                    .load(userLogo)
                    .apply(options)
                    .into(ivAvatar5);
            Glide.with(this)
                    .asBitmap()
                    .load(userLogo)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource,
                                                    @Nullable Transition<? super Bitmap> transition) {
                            avatar = true;
                            myAvatar = resource;
                            startTwoCode();
                        }
                    });
        }
        if (!TextUtils.isEmpty(nick)) {
            tvNick.setText(nick);
            tvNick2.setText(nick);
            tvNick3.setText(nick);
            tvNick4.setText(nick);
            tvNick5.setText(nick);
        }
        Glide.with(this)
                .load(sl)
                .into(ivPic42);
        Glide.with(this)
                .load(rz)
                .into(ivPic51);
    }

    @Override
    protected void initData() {
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
            String url = jsonObject.optString("url", "");
            ArrayList<DynamicStatic> list = new Gson().fromJson(jsonObject.optString("dynamicStaticList"), new TypeToken<List<DynamicStatic>>() {
            }.getType());
            if (list != null) {
                if (list.size() == 6) {
                    tvHint.setText(list.get(0).getJson());
                    tvHint4.setText(list.get(4).getJson());
                    tvHint5.setText(list.get(5).getJson());
                }
            }
            drawPic(url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void drawPic(String url) {
        if (!TextUtils.isEmpty(url)) {
            myTwoCode = CodeUtils.getInstance().createQRImage(url, 720, 720);
            twoCode = true;
            startTwoCode();
        }
    }

    /**
     * 绘制二维码
     */
    private void startTwoCode() {
        if (twoCode && avatar) {
            myBitmap = composePic(myAvatar, myTwoCode);
            ivPic.setImageBitmap(myBitmap);
            ivPic41.setImageBitmap(myBitmap);
            ivPic52.setImageBitmap(myBitmap);
            myBitmap2 = composePic2(myAvatar, myTwoCode);
            ivPic2.setImageBitmap(myBitmap2);
            myBitmap3 = composePic3(myAvatar, myTwoCode);
            ivPic3.setImageBitmap(myBitmap3);
        }
    }

    private Bitmap composePic3(Bitmap myAvatar, Bitmap myTwoCode) {
        Bitmap bgBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_two_code_bg3);
        myTwoCode = CodeUtils.getInstance().zoomImg(myTwoCode, 520,
                520);
        myAvatar = CodeUtils.getInstance().zoomImg(myAvatar, 100, 100);
        bgBitmap = CodeUtils.getInstance().zoomImg(bgBitmap, 720, 895);
        Bitmap cvBitmap = Bitmap.createBitmap(720, 895, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(cvBitmap);
        // 开始合成图片
        canvas.drawBitmap(bgBitmap, 0, 0, null);
        canvas.drawBitmap(myAvatar, 76, 70, null);
        canvas.drawBitmap(myTwoCode, (720 - 520) / 2, 250, null);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int color = getResources().getColor(R.color.black);
        paint.setColor(color);
        paint.setTextSize(40);
        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        Rect bounds = new Rect();
        if (nick.length() > 12) {
            nick = nick.substring(0, 12);
        }
        paint.getTextBounds(nick, 0, nick.length(), bounds);

        Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        int color1 = getResources().getColor(R.color.black);
        paint1.setColor(color1);
        paint1.setTextSize(36);
        paint1.setDither(true); // 获取跟清晰的图像采样
        paint1.setFilterBitmap(true);// 过滤一些
        Rect bounds1 = new Rect();
        String str = getResources().getString(R.string.tv_code_view_hint);
        if (str.length() > 12) {
            str = str.substring(0, 12);
        }
        paint1.getTextBounds(str, 0, str.length(), bounds1);
        canvas.drawText(nick, 200, 110, paint);
        canvas.drawText(str, 200, 166, paint1);
        canvas.save(Canvas.ALL_SAVE_FLAG);// 保存
        canvas.restore();
        if (cvBitmap.isRecycled()) {
            cvBitmap.recycle();
        }
        imageName3 = CodeUtils.getInstance().saveImage(cvBitmap);
        return cvBitmap;
    }

    private Bitmap composePic2(Bitmap myAvatar, Bitmap myTwoCode) {
        Bitmap bgBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_two_code_bg2);
        myTwoCode = CodeUtils.getInstance().zoomImg(myTwoCode, 360,
                360);
        myAvatar = CodeUtils.getInstance().zoomImg(myAvatar, 100, 100);
        bgBitmap = CodeUtils.getInstance().zoomImg(bgBitmap, 720, 978);
        Bitmap cvBitmap = Bitmap.createBitmap(720, 978, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(cvBitmap);
        // 开始合成图片
        canvas.drawBitmap(bgBitmap, 0, 0, null);
        canvas.drawBitmap(myAvatar, (720 - 90) / 2, 70, null);
        canvas.drawBitmap(myTwoCode, (720 - 360) / 2, 470, null);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int color = getResources().getColor(R.color.black);
        paint.setColor(color);
        paint.setTextSize(36);
        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        Rect bounds = new Rect();
        if (nick.length() > 12) {
            nick = nick.substring(0, 12);
        }
        paint.getTextBounds(nick, 0, nick.length(), bounds);
        int x = (720 - bounds.width()) / 2;
        canvas.drawText(nick, x, 210, paint);
        canvas.save(Canvas.ALL_SAVE_FLAG);// 保存
        canvas.restore();
        if (cvBitmap.isRecycled()) {
            cvBitmap.recycle();
        }
        imageName2 = CodeUtils.getInstance().saveImage(cvBitmap);
        return cvBitmap;
    }

    /**
     * 合成图片
     *
     * @return 返回带有白色背景框logo
     */
    public Bitmap composePic(Bitmap myAvatar, Bitmap mySmallPrograme) {
        Bitmap bgBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_two_code_bg1);
        mySmallPrograme = CodeUtils.getInstance().zoomImg(mySmallPrograme, 380,
                380);
        myAvatar = CodeUtils.getInstance().zoomImg(myAvatar, 84, 84);
        bgBitmap = CodeUtils.getInstance().zoomImg(bgBitmap, 720, 1280);
        Bitmap cvBitmap = Bitmap.createBitmap(720, 1280, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(cvBitmap);
        // 开始合成图片
        canvas.drawBitmap(bgBitmap, 0, 0, null);
        canvas.drawBitmap(myAvatar, (720 - 84) / 2, 530, null);
        canvas.drawBitmap(mySmallPrograme, (720 - 380) / 2, 660, null);
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
        canvas.drawText(nick, x, 660, paint);
        canvas.save(Canvas.ALL_SAVE_FLAG);// 保存
        canvas.restore();
        if (cvBitmap.isRecycled()) {
            cvBitmap.recycle();
        }
        imageName1 = CodeUtils.getInstance().saveImage(cvBitmap);
        return cvBitmap;
    }

    @OnClick({R.id.layout_back, R.id.iv_pic, R.id.layout_one_key_share, R.id.iv_pic2,
            R.id.layout_one_key_share2, R.id.iv_pic3, R.id.layout_one_key_share3,
            R.id.iv_pic41, R.id.iv_pic42, R.id.layout_one_key_share4, R.id.iv_pic51,
            R.id.iv_pic52, R.id.layout_one_key_share5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.iv_pic:
                previewImage(imageName1);
                break;
            case R.id.layout_one_key_share:
                if (!TextUtils.isEmpty(imageName1)){
                    showShareDialog(imageName1,myBitmap);
                }
                break;
            case R.id.iv_pic2:
                previewImage(imageName2);
                break;
            case R.id.layout_one_key_share2:
                if (!TextUtils.isEmpty(imageName2)){
                    showShareDialog(imageName2,myBitmap2);
                }
                break;
            case R.id.iv_pic3:
                previewImage(imageName3);
                break;
            case R.id.layout_one_key_share3:
                if (!TextUtils.isEmpty(imageName3)){
                    showShareDialog(imageName3,myBitmap3);
                }
                break;
            case R.id.iv_pic41:
                previewImage(imageName1,sl,0);
                break;
            case R.id.iv_pic42:
                previewImage(imageName1,sl,1);
                break;
            case R.id.layout_one_key_share4:
                if (!TextUtils.isEmpty(imageName1)){
                    showShareDialog(imageName1,sl,0);
                }
                break;
            case R.id.iv_pic51:
                previewImage5(rz,imageName1,0);
                break;
            case R.id.iv_pic52:
                previewImage5(rz,imageName1,1);
                break;
            case R.id.layout_one_key_share5:
                if (!TextUtils.isEmpty(imageName1)){
                    showShareDialog(rz,imageName1,1);
                }
                break;
        }
    }

    private void previewImage(String imageName1, String sl, int i) {
        ArrayList<LocalMedia> list = new ArrayList<>();
        if (!TextUtils.isEmpty(imageName1)){
            LocalMedia media = new LocalMedia();
            media.setPath(imageName1);
            list.add(media);
            LocalMedia media1 = new LocalMedia();
            media1.setPath(sl);
            list.add(media1);
            PicSelectUtils.getInstance().preview(i,list,this);
        }
    }

    private void previewImage5(String imageName1, String sl, int i) {
        ArrayList<LocalMedia> list = new ArrayList<>();
        if (!TextUtils.isEmpty(sl)){
            LocalMedia media = new LocalMedia();
            media.setPath(imageName1);
            list.add(media);
            LocalMedia media1 = new LocalMedia();
            media1.setPath(sl);
            list.add(media1);
            PicSelectUtils.getInstance().preview(i,list,this);
        }
    }

    private void previewImage(String imageName1) {
        ArrayList<LocalMedia> list = new ArrayList<>();
        if (!TextUtils.isEmpty(imageName1)){
            LocalMedia media = new LocalMedia();
            media.setPath(imageName1);
            list.add(media);
            PicSelectUtils.getInstance().preview(0,list,this);
        }
    }

    private void showShareDialog(final String imageName,final Bitmap myBitmap) {
        new ShareDialog(this, R.style.dialog, new ShareDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                File[] files = new File[1];
                files[0] = new File(imageName);
                switch (position){
                    case 1:
                        copyText(tvHint.getText().toString());
                        ShareUtils.getInstance().shareToFriend(files,1,MyTwoCodeActivity.this);
                        break;
                    case 2:
                        copyText(tvHint.getText().toString());
                        ShareUtils.getInstance().shareToFriend(files,2,MyTwoCodeActivity.this);
                        break;
                    case 3:
                        CodeUtils.getInstance().insertImageToSystemGallery(
                                MyTwoCodeActivity.this,imageName,myBitmap);
                        break;
                }
            }
        }).show();
    }


    private void showShareDialog(final String imageName,final String imageName1,int flag) {
        if (flag == 0){
            if (!TextUtils.isEmpty(imageName)){
                Dynamic dynamic = new Dynamic();
                dynamic.setDynamicType(1);
                ArrayList<DynamicPhoto> dynamicPhotos = new ArrayList<>();
                DynamicPhoto dynamicPhoto = new DynamicPhoto();
                dynamicPhoto.setThumbnailURL(imageName);
                dynamicPhotos.add(dynamicPhoto);

                DynamicPhoto dynamicPhoto1 = new DynamicPhoto();
                dynamicPhoto1.setThumbnailURL(imageName1);
                dynamicPhotos.add(dynamicPhoto1);
                dynamic.setDynamicPhotos(dynamicPhotos);
                showShareDialog(dynamic,tvHint4.getText().toString());
            }
        }else {
            if (!TextUtils.isEmpty(imageName1)){
                Dynamic dynamic = new Dynamic();
                dynamic.setDynamicType(1);
                ArrayList<DynamicPhoto> dynamicPhotos = new ArrayList<>();
                DynamicPhoto dynamicPhoto = new DynamicPhoto();
                dynamicPhoto.setThumbnailURL(imageName);
                dynamicPhotos.add(dynamicPhoto);

                DynamicPhoto dynamicPhoto1 = new DynamicPhoto();
                dynamicPhoto1.setThumbnailURL(imageName1);
                dynamicPhotos.add(dynamicPhoto1);
                dynamic.setDynamicPhotos(dynamicPhotos);
                showShareDialog(dynamic,tvHint5.getText().toString());
            }
        }
    }

    private void showShareDialog(final Dynamic dynamic, final String content) {
        new ShareDialog(this, R.style.dialog, new ShareDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                switch (position){
                    case 1:
                        copyText(content);
                        ShareUtils.getInstance().shareFriend(dynamic,1,MyTwoCodeActivity.this);
                        break;
                    case 2:
                        copyText(content);
                        ShareUtils.getInstance().shareFriend(dynamic,2,MyTwoCodeActivity.this);
                        break;
                    case 3:
                        ImageSaveUtils.getInstance(MyTwoCodeActivity.this).saveAll(dynamic);
                        break;
                }
            }
        }).show();
    }

    private void copyText(String content){
        if (!TextUtils.isEmpty(content)) {
            ClipboardManager cmb = (ClipboardManager) MyApplication.getAppContext().getSystemService(CLIPBOARD_SERVICE);
            cmb.setText(content);
            ToastUtils.showShort("内容已复制，分享时可以粘贴");
        }
    }

    @Override
    public void callBack(int code, String msg) {
        ToastUtils.showShort(msg);
    }
}
