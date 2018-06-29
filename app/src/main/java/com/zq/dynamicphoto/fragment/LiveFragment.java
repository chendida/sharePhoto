package com.zq.dynamicphoto.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 直播
 */
public class LiveFragment extends BaseFragment {

    @BindView(R.id.iv_water)
    ImageView ivWater;
    @BindView(R.id.tv_text1)
    TextView tvText;
    @BindView(R.id.layout_water_one)
    AutoRelativeLayout layoutWaterOne;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void loadData() {

    }

    public static Bitmap createRepeater(int width,Bitmap src){
        int count=(width+src.getWidth()-1)/src.getWidth();
        Log.i("width","count = "+ count);
        Bitmap bitmap=Bitmap.createBitmap(width,src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        for(int idx=0;idx<count;++idx){
            canvas.drawBitmap(src,idx*src.getWidth(),0,null);
        }
        return createRepeaterY(width,bitmap);
    }

    public static Bitmap createRepeaterY(int height,Bitmap src){
        int count=(height+src.getHeight()-1)/src.getHeight();
        Log.i("width","count = "+ count);
        Bitmap bitmap=Bitmap.createBitmap(src.getWidth(),height, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        for(int idx=0;idx<count;++idx){
            canvas.drawBitmap(src,0,idx*src.getHeight(),null);
        }
        return bitmap;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.btn_1, R.id.btn_2})
    public void onClicked(View view) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Bitmap bitmap1,repeater;
        int width = dm.widthPixels;
        switch (view.getId()) {
            case R.id.btn_1:
                tvText.setText("haha");
                bitmap1 = convertViewToBitmap(layoutWaterOne);
                repeater = createRepeater(width, bitmap1);
                layoutWaterOne.setVisibility(View.GONE);
                ImageLoaderUtils.displayImg(ivWater,repeater);
                break;
            case R.id.btn_2:
                bitmap1 = convertViewToBitmap(layoutWaterOne);
                repeater = createRepeater(width, bitmap1);
                layoutWaterOne.setVisibility(View.GONE);
                ImageLoaderUtils.displayImg(ivWater,repeater);
                break;
        }
    }

    /**
     * 将一个view转换为Bitmap
     * @param view
     * @return
     */
    public static Bitmap convertViewToBitmap(View view){
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    private Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片   www.2cto.com
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }
}
