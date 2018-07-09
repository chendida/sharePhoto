package com.zq.dynamicphoto.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zq.dynamicphoto.R;

/**
 * Created by Administrator on 2018/6/12.
 */

public class ImageLoaderUtils {
    static RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.color.colorBackground);

    public static void displayImg(ImageView imageView, String url){
        if (imageView.getContext() != null) {
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(options)
                    .into(imageView);
        }
    }

    public static void displayImg(ImageView imageView, Bitmap bitmap){
        Glide.with(imageView.getContext())
                .load(bitmap)
                .apply(options)
                .into(imageView);
    }
}
