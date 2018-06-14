package com.zq.dynamicphoto.utils;

import android.app.Activity;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.ui.AddPicActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/6/14.
 */

public class PicSelectUtils {
    private static PicSelectUtils instance;

    public static PicSelectUtils getInstance(){
        if (instance == null){
            synchronized (PicSelectUtils.class){
                if (instance == null){
                    instance = new PicSelectUtils();
                }
            }
        }
        return instance;
    }
    /**
     * 选择图片
     * @param activity
     */
    public void intoPicSelect(Activity activity) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .previewImage(true)
                .previewVideo(true)
                .maxSelectNum(Constans.MAX_PIC_NUM)
                //.selectionMedia(mSelectedImages)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
    /**
     * 选择视频
     */
    public void intoVideoSelect(Activity activity) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofVideo())
                .maxSelectNum(Constans.MAX_VIDEO_NUM)
                .previewImage(true)
                .previewVideo(true)
                .videoMaxSecond(11)
                //.selectionMedia(mSelectedImages)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
}
