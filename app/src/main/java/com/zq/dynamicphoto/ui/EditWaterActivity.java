package com.zq.dynamicphoto.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserWatermark;
import com.zq.dynamicphoto.bean.WaterImage;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.mylive.tools.Message;
import com.zq.dynamicphoto.presenter.AddWatermarkPresenter;
import com.zq.dynamicphoto.ui.widge.FullScreenWatermarkDialog;
import com.zq.dynamicphoto.ui.widge.WaterBgDialog;
import com.zq.dynamicphoto.ui.widge.WaterTitleDialog;
import com.zq.dynamicphoto.ui.widge.WholeColorEditDialog;
import com.zq.dynamicphoto.utils.ColorUtils;
import com.zq.dynamicphoto.utils.CosUtils;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.ILoadView;
import com.zq.dynamicphoto.view.UploadView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import butterknife.BindView;
import butterknife.OnClick;

public class EditWaterActivity extends BaseActivity<ILoadView,AddWatermarkPresenter<ILoadView>>
        implements UploadView,ILoadView{
    private static final String TAG = "EditWaterActivity";
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_water_title)
    TextView tvWaterTitle;
    @BindView(R.id.iv_water_icon)
    ImageView ivWaterIcon;
    @BindView(R.id.tv_wx)
    TextView tvWx;
    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.layout_whole_water_content)
    AutoRelativeLayout layoutWholeWaterContent;
    private WholeColorEditDialog colorEditDialog;
    private FullScreenWatermarkDialog fullScreenWatermarkDialog;
    private WaterBgDialog waterBgDialog;
    private WaterTitleDialog waterTitleDialog;
    private String path;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_water;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        layoutBack.setVisibility(View.VISIBLE);
        ivCamera.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.mould_preview));
        tvFinish.setText(getResources().getString(R.string.create_water));
        tvFinish.setTextColor(getResources().getColor(R.color.tv_text_color7));
        initDialog();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(Message image) {
        //Log.i(TAG,"image = " + image.getUrl());
    }

    private void initDialog() {
        if (colorEditDialog == null) {
            colorEditDialog = new WholeColorEditDialog(this, R.style.dialog, new WholeColorEditDialog.OnItemClickListener() {
                @Override
                public void onClick(Dialog dialog, int position) {
                    Log.i(TAG, "position = " + position);
                    updateWholeColor(position);
                }
            });
        }
        if (fullScreenWatermarkDialog == null) {
            fullScreenWatermarkDialog = new FullScreenWatermarkDialog(this, R.style.dialog);
        }
        if (waterBgDialog == null) {
            waterBgDialog = new WaterBgDialog(this, R.style.dialog);
        }
        if (waterTitleDialog == null) {
            waterTitleDialog = new WaterTitleDialog(this, R.style.dialog);
        }
    }

    private void updateWholeColor(int position) {
        int color = ColorUtils.getColor(position);
        ivHead.setColorFilter(color);
        ivWaterIcon.setColorFilter(color);
        tvWaterTitle.setTextColor(color);
        tvWx.setTextColor(color);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected AddWatermarkPresenter<ILoadView> createPresenter() {
        return new AddWatermarkPresenter<>();
    }

    @OnClick({R.id.layout_bg, R.id.layout_whole_color, R.id.check_full_watermark,
            R.id.check_water_bg_setting, R.id.layout_bg_setting, R.id.layout_water_title,
            R.id.layout_back, R.id.layout_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_finish:
                addWater();
                break;
            case R.id.layout_bg:
                break;
            case R.id.layout_whole_color:
                colorEditDialog.show();
                break;
            case R.id.check_water_bg_setting:

                break;
            case R.id.check_full_watermark:
                fullScreenWatermarkDialog.show();
                break;
            case R.id.layout_bg_setting:
                waterBgDialog.show();
                break;
            case R.id.layout_water_title:
                waterTitleDialog.show();
                break;
        }
    }

    private void addWater() {
        showLoading();
        layoutWholeWaterContent.setDrawingCacheEnabled(true);
        //tvWaterTitle.buildDrawingCache();
        Bitmap bitmap = layoutWholeWaterContent.getDrawingCache();
        //bitmap = drawBg4Bitmap(getResources().getColor(R.color.alpha), bitmap);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        path = saveBitmap(System.currentTimeMillis() + ".png",bytes);
        CosUtils.getInstance(this).uploadToCos(path,10);
        layoutWholeWaterContent.setDrawingCacheEnabled(false);
    }

    public Bitmap drawBg4Bitmap(int color, Bitmap orginBitmap) {
        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(orginBitmap.getWidth(),
                orginBitmap.getHeight(), orginBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, orginBitmap.getWidth(), orginBitmap.getHeight(), paint);
        canvas.drawBitmap(orginBitmap, 0, 0, paint);
        return bitmap;
    }


    private String saveBitmap(String imgName, byte[] bytes) {
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
                //scanPhoto(MyApplication.getAppContext(),imgName);
                return imgName;
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
        return imgName;
    }

    /**
     * 解决将图片保存在本地在相册中不能看到图片的问题
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
    public void onUploadProcess(int percent) {

    }

    @Override
    public void onUploadResult(int code, String url) {
        Log.i(TAG,"code = " + code + ",url = " + url);
        hideLoading();
        if (code == Constans.REQUEST_OK){
            UserWatermark userWatermark = new UserWatermark();
            userWatermark.setWatermarkType(1);//1表示图片水印，2表示文字水印
            userWatermark.setWatermarkUrl(url);
            int userId = SharedPreferencesUtils.getInstance().getInt(Constans.USERID, 0);
            userWatermark.setUserId(userId);
            NetRequestBean netRequestBean = new NetRequestBean();
            netRequestBean.setDeviceProperties(DrUtils.getInstance());
            netRequestBean.setUserWatermark(userWatermark);
            if (mPresenter != null){
                mPresenter.addWatermarkPic(netRequestBean);
            }
        }else {
            ToastUtils.showShort(getResources().getString(R.string.tv_water_mark_upload_fail));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showData(Result result) {
        if (result != null){
            if (result.getResultCode() == Constans.REQUEST_OK){
                WaterImage image = new WaterImage(path);
                EventBus.getDefault().post(image);
                EditWaterActivity.this.finish();
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }
}
