package com.zq.dynamicphoto.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.entity.LocalMedia;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.DynamicPhoto;
import com.zq.dynamicphoto.bean.DynamicVideo;
import com.zq.dynamicphoto.bean.Moments;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.ui.widge.ShareDialog;
import com.zq.dynamicphoto.view.CompressView;
import com.zq.dynamicphoto.view.UploadView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Administrator on 2018/6/27.
 */

public class ShareUtils /*implements CompressView,UploadView*/{
    private static ShareUtils instance;

    private String content;

    /**
     * 单例
     * @return
     */
    public static ShareUtils getInstance(){
        if (null == instance) {
            synchronized (ShareUtils.class) {
                if (null == instance) {
                    instance = new ShareUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 分享给微信好友
     * @param dynamic
     */
    public void shareFriend(Dynamic dynamic,int flag,Activity mContext){
        if (dynamic != null){
            if (dynamic.getDynamicType() == 1) {//图文
                if (dynamic.getDynamicPhotos() != null) {
                    if (dynamic.getDynamicPhotos().size() != 0) {
                        ArrayList<LocalMedia> list = new ArrayList<>();
                        for (DynamicPhoto dynamicPhoto : dynamic.getDynamicPhotos()) {
                            LocalMedia localMedia = new LocalMedia();
                            localMedia.setPath(dynamicPhoto.getThumbnailURL());
                            list.add(localMedia);
                        }
                        shareToWxFriend(list,flag,dynamic.getContent(),mContext);
                    } else {
                        shareTextToWxFriend(dynamic.getContent(),flag);
                    }
                }else {
                    shareTextToWxFriend(dynamic.getContent(),flag);
                }
            }else if (dynamic.getDynamicType() == 2){//视频
                if (dynamic.getDynamicVideos() != null){
                    if (dynamic.getDynamicVideos().size() != 0){
                        DynamicVideo dynamicVideo = dynamic.getDynamicVideos().get(0);
                        if (dynamicVideo.getVideoURL().startsWith("http")) {
                            shareUrlVideo(dynamicVideo.getVideoURL(), dynamic.getContent(), flag);
                        }else {
                            if (flag == 1){
                                shareVideo(new File(dynamicVideo.getVideoURL()),mContext);
                            }else {
                                //shareVideoToFriendCircle(dynamicVideo,dynamic.getContent(),mContext);
                            }
                        }
                    }
                }
            }
        }
    }

    /*private void shareVideoToFriendCircle(final DynamicVideo dynamicVideo,
                                          final String content,Activity mContext) {
        LoadingUtils.showLoading(mContext);
        this.content = content;
        CompressVideoUtils.getInstance().compressVideoResouce(mContext,dynamicVideo.getVideoURL(),this);
    }*/

    private void copyText(String content,int flag){
        if (!TextUtils.isEmpty(content) && flag == 2) {
            ClipboardManager cmb = (ClipboardManager) MyApplication.getAppContext().getSystemService(CLIPBOARD_SERVICE);
            cmb.setText(content);
            ToastUtils.showShort("内容已复制，分享时可以粘贴");
        }
    }

    /**
     * 分享链接
     * flag  1表示好友，2表示朋友圈
     */
    public void shareLink(final String url, final String title, final String content,
                          final String thumbUrl,final int flag){
        new Thread(new Runnable() {
            @Override
            public void run() {
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = url;
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.description = content;
                if (!TextUtils.isEmpty(title)) {
                    msg.title = title;
                }else {
                    msg.title = "朋友圈";
                }
                //这里替换一张自己工程里的图片资源
                Bitmap bitmap = GetImageInputStream(thumbUrl);

                bitmap = ThumbnailUtils.extractThumbnail(bitmap, 210, 210);
                msg.setThumbImage(bitmap);

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf("webpage");
                req.message = msg;
                req.scene = flag == 1 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
                MyApplication.mWxApi.sendReq(req);
            }
        }).start();
    }

    /**
     * 分享网络视频到微信
     * flag  1表示好友，2表示朋友圈
     */
    private void shareUrlVideo(final String videoUrl,final String content, final int flag){
        copyText(content,flag);
        new Thread(new Runnable() {
            @Override
            public void run() {
                WXVideoObject video = new WXVideoObject();
                video.videoUrl = videoUrl;
                WXMediaMessage msg = new WXMediaMessage(video);
                msg.title = "小视频";
                msg.description = content;
                Log.i("ShareUtils",videoUrl);
                Bitmap bitmap = GetImageInputStream(videoUrl);
                if (bitmap != null) {
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, 210, 210);
                    msg.setThumbImage(bitmap);
                }
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf("video");
                req.message = msg;
                req.scene = flag == 1 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
                MyApplication.mWxApi.sendReq(req);
            }
        }).start();
    }

    private void shareTextToWxFriend(String content,int flag) {
        WXTextObject textObject = new WXTextObject();
        textObject.text = content;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = content;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf("text");
        req.message = msg;
        req.scene = flag == 1 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        MyApplication.mWxApi.sendReq(req);
    }

    /**
     * 分享多张图片给好友
     */
    private void shareToFriend(File[] file,int flag,Activity mContext) {
        String  ail = "";
        if (flag == 1){
            ail = "com.tencent.mm.ui.tools.ShareImgUI";
        }else {
            ail = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
        }
        Log.i("share",file.length+"");
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", ail);
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");

        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        if (file.length > 0) {
            for (File f : file) {
                imageUris.add(Uri.fromFile(f));
            }
            if (imageUris.size() != 0) {
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
            }
        }
        intent.putExtra(Intent.EXTRA_TEXT, "一些文字");
        mContext.startActivityForResult(Intent.createChooser(intent,"一键转发"),666);
    }

    /**
     * 分享图片给微信好友
     * @param mSelectedImages
     */
    private void shareToWxFriend(final ArrayList<LocalMedia> mSelectedImages,
                                 final int flag, final String content, final Activity mContext) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File[] files = new File[mSelectedImages.size()];
                for (int i = 0; i < mSelectedImages.size(); i++) {
                    Bitmap bitmap = GetImageInputStream(mSelectedImages.get(i).getPath());
                    if (bitmap != null) {
                        String filePath = saveImage(bitmap);
                        insertImageToSystemGallery(MyApplication.getAppContext(), filePath, bitmap);
                        files[i] = new File(Environment.getExternalStorageDirectory(), filePath);
                    }
                }
                shareToFriend(files,flag,mContext);
            }
        }).start();
        copyText(content,flag);
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
    }

    /**
     * 保存位图到本地
     * @param bitmap
     * @return void
     */
    private String saveImage(Bitmap bitmap){
        FileOutputStream out = null;
        String path = System.currentTimeMillis() + ".jpg";
        File file = new File(Environment.getExternalStorageDirectory(),path);
        try {
            out = new FileOutputStream(file);
            Log.i("1111111111",path);
            if (bitmap != null) {
                Log.i("1111111111","bitmap");
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (out != null) {
                out.flush();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 获取网络图片
     * @param imageurl 图片网络地址
     * @return Bitmap 返回位图
     */
    private Bitmap GetImageInputStream(String imageurl){
        RequestOptions myOptions = new RequestOptions()
                .fitCenter();
        Bitmap bitmap=null;
        try {
            bitmap = Glide.with(MyApplication.getAppContext())
                    .asBitmap()
                    .apply(myOptions)
                    .load(imageurl)
                    .into(720, 1280)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 分享本地视频到微信好友
     */
    private void shareVideo(final File file,Activity mContext){
        Log.i("ShareUtils","分享视频到微信好友");
        setIntent("video", "com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareImgUI", file,mContext);
    }


    private void setIntent(String type,String packageName,String className,File file,Activity mContext){
        if(file.exists()){
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType(type);
            if(stringCheck(packageName) && stringCheck(className)){
                intent.setComponent(new ComponentName(packageName, className));
            }else if (stringCheck(packageName)) {
                intent.setPackage(packageName);
            }
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            Intent chooserIntent = Intent.createChooser(intent, "分享到:");
            mContext.startActivityForResult(chooserIntent,666);
        }else {
            ToastUtils.showShort("文件不存在");
        }
    }

    private static boolean stringCheck(String str){
        if(null != str && !TextUtils.isEmpty(str)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 分享直播到微信好友，或者朋友圈
     * flag  1表示好友，2表示朋友圈
     */
    public void shareLiveLink(final String playUrl, final String liveTitle,
                              final int flag, final String liveCover){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!MyApplication.mWxApi.isWXAppInstalled()) {
                    ToastUtils.showShort("您还没有安装微信");
                    return;
                }else {
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl = playUrl;
                    WXMediaMessage msg = new WXMediaMessage(webpage);
                    msg.description = "直播";
                    msg.title = liveTitle;
                    //这里替换一张自己工程里的图片资源

                    Bitmap bitmap = GetImageInputStream(liveCover);

                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, 210, 210);
                    msg.setThumbImage(bitmap);

                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = String.valueOf("webpage");
                    req.message = msg;
                    req.scene = flag == 1 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
                    MyApplication.mWxApi.sendReq(req);
                }
            }
        }).start();
    }
}
