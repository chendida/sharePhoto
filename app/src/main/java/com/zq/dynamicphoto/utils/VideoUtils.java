package com.zq.dynamicphoto.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.zq.dynamicphoto.MyApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2018/3/9.
 */

public class VideoUtils {
    public static VideoUtils instance;

    public static VideoUtils getInstance() {
        if (null == instance) {
            synchronized (VideoUtils.class) {
                if (null == instance) {
                    instance = new VideoUtils();
                }
            }
        }
        return instance;
    }

    // 获取本地视频缩略图
    public Bitmap getVideoThumbnail(String filePath) {
        Bitmap b=null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            b=retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();

        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return b;
    }


    /**
     * 保存位图到本地
     *
     * @param bitmap
     * @return void
     */
    public String saveImage(Bitmap bitmap) {
        FileOutputStream out = null;
        String path = System.currentTimeMillis() + ".jpg";
        File file = new File(Environment.getExternalStorageDirectory(), path);
        try {
            out = new FileOutputStream(file);
            if (bitmap != null) {
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
     * 图片插入到系统相册,解决系统图库不能打开图片的问题
     */
    public void insertImageToSystemGallery(String filePath, Bitmap bitmap) {
        Context context = MyApplication.getAppContext();
        MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "", "");
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    public void getPicHightAndWidth(String url) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        SaveTasks.getInstance().getList().get(0).setHeight(options.outHeight);
        SaveTasks.getInstance().getList().get(0).setWidth(options.outWidth);
    }
}
