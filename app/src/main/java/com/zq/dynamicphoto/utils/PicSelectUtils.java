package com.zq.dynamicphoto.utils;

import android.app.Activity;
import android.app.Dialog;
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
    private void intoPicSelect(Activity activity,ArrayList<LocalMedia>mSelectedImages) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .previewImage(true)
                .previewVideo(true)
                .maxSelectNum(Constans.MAX_PIC_NUM)
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
    public void preview(int position, ArrayList<LocalMedia>mSelectedImages, Activity activity) {
        LocalMedia media = mSelectedImages.get(position);
        String pictureType = media.getPictureType();
        int mediaType = PictureMimeType.pictureToVideo(pictureType);
        // 跳转到预览界面
        if (mediaType == PictureConfig.TYPE_IMAGE) {
            PictureSelector.create(activity).externalPicturePreview(position, mSelectedImages);
        } else if (mediaType == PictureConfig.TYPE_VIDEO) {
            PictureSelector.create(activity).externalPictureVideo(mSelectedImages.get(position).getPath());
        }
    }

    public void gotoSelectPicOrVideo(ArrayList<LocalMedia>localMedias,Activity activity){
        if (localMedias != null){
            if (localMedias.size() != 0){
                int pictureType = PictureMimeType.isPictureType(localMedias.get(0).getPictureType());
                if (pictureType == PictureConfig.TYPE_VIDEO){//选的视频
                    intoVideoSelect(activity,localMedias);
                }else {//选的图片
                    intoPicSelect(activity,localMedias);
                }
            }else {
                this.activity = activity;
                this.localMedias = localMedias;
                showSelectPicDialog();
            }
        }else {
            this.activity = activity;
            this.localMedias = localMedias;
            showSelectPicDialog();
        }
    }


    private void showSelectPicDialog() {
        if (selectPicDialog == null){
            selectPicDialog = new SelectPicDialog(activity, R.style.dialog, new SelectPicDialog.OnItemClickListener() {
                @Override
                public void onClick(Dialog dialog, int position) {
                    dialog.dismiss();
                    switch (position) {
                        case 1://选择图片
                            intoPicSelect(activity,localMedias);
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
