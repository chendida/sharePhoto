package com.zq.dynamicphoto.ui.widge;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.ImageUtil;
import com.zq.dynamicphoto.utils.PicSelectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 作者：HMY
 * 时间：2016/5/12
 */
public class NineGridImageLayout extends NineGridLayout {

    protected static final int MAX_W_H_RATIO = 3;

    public NineGridImageLayout(Context context) {
        super(context);
    }

    public NineGridImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean displayOneImage(final RatioImageView imageView, String url, final int parentWidth,Boolean flag) {
        if (flag){
            Log.i("video","url = "+url);
             Glide.with(mContext)
                    .asBitmap()
                     .load(url)
                     .into(new SimpleTarget<Bitmap>() {
                         @Override
                         public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                             int w = bitmap.getWidth();
                             int h = bitmap.getHeight();
                             int newW;
                             int newH;
                             if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
                                 newW = parentWidth / 2;
                                 newH = newW * 5 / 3;
                             } else if (h < w) {//h:w = 2:3
                                 newW = parentWidth * 2 / 3;
                                 newH = newW * 2 / 3;
                             } else {//newH:h = newW :w
                                 newW = parentWidth / 2;
                                 newH = h * newW / w;
                             }
                             setOneImageLayoutParams(imageView, newW, newH);
                             Bitmap bmp= BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_play);
                             displayImage(imageView, ImageUtil.createWaterMaskCenter(bitmap,bmp));
                         }
                     });
        }else {
            Glide.with(mContext)
                    .asBitmap()
                    .load(url)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            int w = bitmap.getWidth();
                            int h = bitmap.getHeight();

                            int newW;
                            int newH;
                            if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
                                newW = parentWidth / 2;
                                newH = newW * 5 / 3;
                            } else if (h < w) {//h:w = 2:3
                                newW = parentWidth * 2 / 3;
                                newH = newW * 2 / 3;
                            } else {//newH:h = newW :w
                                newW = parentWidth / 2;
                                newH = h * newW / w;
                            }
                            setOneImageLayoutParams(imageView, newW, newH);
                        }
                    });
            displayImage(imageView, url);
        }
        return false;
    }

    @Override
    protected void displayImage(RatioImageView imageView, String url) {
        Log.i("loadurl","url = "+url);
        //Glide.with(mContext).load(url).into(imageView);
        ImageLoaderUtils.displayImg(imageView,url);
    }

    private void displayImage(RatioImageView imageView,Bitmap bitmap){
        //Glide.with(mContext).load(bitmap).into(imageView);
        ImageLoaderUtils.displayImg(imageView,bitmap);
    }

    @Override
    protected void onClickImage(int i, String url, List<String> urlList) {
        ArrayList<LocalMedia> list = new ArrayList<>();
        for (String pic:urlList) {
            LocalMedia media = new LocalMedia();
            media.setPath(pic);
            list.add(media);
        }
        PicSelectUtils.getInstance().preview(i,list,mContext);
        Log.i("url","url = "+url);
        //Toast.makeText(mContext, "点击了图片" + url, Toast.LENGTH_SHORT).show();
    }
}
