package com.zq.dynamicphoto.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.ui.widge.SelectPicDialog;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/6/14.
 */

public class PicSelectUtils {
    private static PicSelectUtils instance;
    private SelectPicDialog selectPicDialog;
    private Activity activity;
    private ArrayList<LocalMedia>localMedias = new ArrayList<>();
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
    private void intoPicSelect(Activity activity,ArrayList<LocalMedia>mSelectedImages,int num) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .previewImage(true)
                .previewVideo(true)
                .maxSelectNum(num)
                .selectionMedia(mSelectedImages)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 选择视频
     */
    private void intoVideoSelect(Activity activity,ArrayList<LocalMedia>mSelectedImages) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofVideo())
                .maxSelectNum(Constans.MAX_VIDEO_NUM)
                .previewImage(true)
                .previewVideo(true)
                .videoMaxSecond(Constans.MAX_VIDEO_TIME)
                .selectionMedia(mSelectedImages)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 预览
     */
    public void preview(int position, ArrayList<LocalMedia>mSelectedImages, Context activity) {
        LocalMedia media = mSelectedImages.get(position);
        if (media.getPath().endsWith(".mp4")){
            PictureSelector.create((Activity) activity).externalPictureVideo(mSelectedImages.get(position).getPath());
        }else {
            PictureSelector.create((Activity) activity).externalPicturePreview(position, mSelectedImages);
        }
    }

    public void gotoSelectPicOrVideo(ArrayList<LocalMedia>localMedias,Activity activity,int num){
        if (localMedias != null){
            if (localMedias.size() != 0){
                int pictureType = PictureMimeType.isPictureType(localMedias.get(0).getPictureType());
                if (pictureType == PictureConfig.TYPE_VIDEO){//选的视频
                    intoVideoSelect(activity,localMedias);
                }else {//选的图片
                    intoPicSelect(activity,localMedias,num);
                }
            }else {
                this.activity = activity;
                this.localMedias = localMedias;
                showSelectPicDialog(num);
            }
        }else {
            this.activity = activity;
            this.localMedias = localMedias;
            showSelectPicDialog(num);
        }
    }

    public void gotoSelectPic(Activity activity,int num){
        intoPicSelect(activity,null,num);
    }


    private void showSelectPicDialog(final int num) {
        if (selectPicDialog == null){
            selectPicDialog = new SelectPicDialog(activity, R.style.dialog, new SelectPicDialog.OnItemClickListener() {
                @Override
                public void onClick(Dialog dialog, int position) {
                    dialog.dismiss();
                    switch (position) {
                        case 1://选择图片
                            intoPicSelect(activity,localMedias,num);
                            break;
                        case 2://选择视频
                            intoVideoSelect(activity,localMedias);
                            break;
                    }
                }
            });
        }
        selectPicDialog.show();
    }

    public void clear(){
        activity = null;
        localMedias.clear();
        selectPicDialog = null;
    }
}
