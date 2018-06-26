package com.zq.dynamicphoto.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.WebView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/6/26.
 */

public class ShareCodeUtils {
    public static void shareToWx(Activity activity,WebView webView) {
        Bitmap bitmap = captureWebView(webView);
        String path = saveImage(bitmap);
        insertImageToSystemGallery(activity, path, bitmap);
        File file = new File(Environment.getExternalStorageDirectory(), path);
        Intent intent = new Intent();
        intent.setPackage("com.tencent.mm");
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");

        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        imageUris.add(Uri.fromFile(file));
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        activity.startActivity(Intent.createChooser(intent,"一键分享"));
    }

    /**
     * 截取webView快照(webView加载的整个内容的大小)
     * @param webView
     * @return
     */
    private static Bitmap captureWebView(WebView webView){
        Picture snapShot = webView.capturePicture();

        Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(),snapShot.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        snapShot.draw(canvas);
        return bmp;
    }

    /**
     * 保存位图到本地
     * @param bitmap
     * @return void
     */
    private static String saveImage(Bitmap bitmap){
        FileOutputStream out = null;
        String path = System.currentTimeMillis() + ".jpg";
        File file = new File(Environment.getExternalStorageDirectory(),path);
        try {
            out = new FileOutputStream(file);
            android.util.Log.i("1111111111",path);
            if (bitmap != null) {
                android.util.Log.i("1111111111","bitmap");
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
    private static void insertImageToSystemGallery(Context context, String filePath, Bitmap bitmap){
        MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "", "");
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setData(uri);
        context.sendBroadcast(intent);
    }
}
