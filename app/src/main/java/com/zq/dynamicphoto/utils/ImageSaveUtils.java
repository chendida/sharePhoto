package com.zq.dynamicphoto.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.entity.LocalMedia;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.DynamicPhoto;
import com.zq.dynamicphoto.bean.DynamicVideo;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.net.RetrofitApiService;
import com.zq.dynamicphoto.net.util.RetrofitUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/6/27.
 */

public class ImageSaveUtils {
    //自定义接口，用于回调
    private static DownLoadListener mListener;

    private static ImageSaveUtils instance;

    /**
     * 单例
     * @return
     */
    public static ImageSaveUtils getInstance(DownLoadListener listener){
        if (null == instance) {
            synchronized (ImageSaveUtils.class) {
                if (null == instance) {
                    instance = new ImageSaveUtils(listener);
                }
            }
        }
        return instance;
    }

    public interface DownLoadListener {
        void callBack(int code,String msg);
    }

    public void clearListener(){
        mListener = null;
        instance = null;
    }

    private ImageSaveUtils(DownLoadListener listener) {
        mListener = listener;
    }

    private String filePath = "";
    public void saveAll(Dynamic dynamic) {
        if (dynamic.getDynamicType() == 1){//图文
            if (dynamic.getDynamicPhotos() != null){
                if (dynamic.getDynamicPhotos().size() != 0){
                    ArrayList<LocalMedia> list = new ArrayList<>();
                    for (DynamicPhoto dynamicPhoto:dynamic.getDynamicPhotos()) {
                        LocalMedia localMedia = new LocalMedia();
                        localMedia.setPath(dynamicPhoto.getThumbnailURL());
                        list.add(localMedia);
                    }
                    saveAllImage(list);
                }else {
                    mListener.callBack(-1,"至少要有一张图片");
                }
            }
        }else if (dynamic.getDynamicType() == 2){
            if (dynamic.getDynamicVideos() != null){
                if (dynamic.getDynamicVideos().size() != 0){
                    DynamicVideo dynamicVideo = dynamic.getDynamicVideos().get(0);
                    if (!TextUtils.isEmpty(dynamicVideo.getDownloadVideoURL())) {
                        downloadVideo(dynamicVideo.getDownloadVideoURL());
                    }
                }
            }
        }
    }

    private void downloadVideo(final String videoUrl){
        NetRequestBean netRequestBean = new NetRequestBean();
        DeviceProperties dr = DrUtils.getInstance();
        netRequestBean.setDeviceProperties(dr);
        RetrofitApiService retrofitApiService = RetrofitUtil.getInstance().getRetrofitApiService();
        Call<Result> call = retrofitApiService.getTemporarySecretKey(netRequestBean);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result = response.body();
                if (result != null) {
                    if (result.getResultCode() == 0) {
                        dealWithResult(result,videoUrl);
                    } else {
                        mListener.callBack(-1,"视频下载失败");
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                mListener.callBack(-1,"视频下载失败");
            }
        });
    }

    private void dealWithResult(Result result, String videoUrl) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result.getData());
            JSONObject jsonObject1 = jsonObject.optJSONObject("data");
            JSONObject jsonObject2 = jsonObject1.optJSONObject("credentials");
            long expiredTime = jsonObject1.optLong("expiredTime");
            String tmpSecretId = jsonObject2.optString("tmpSecretId");
            String tmpSecretKey = jsonObject2.optString("tmpSecretKey");
            String sessionToken = jsonObject2.optString("sessionToken");
            LocalSessionCredentialProvider localCredentialProvider = new LocalSessionCredentialProvider(tmpSecretId, tmpSecretKey, sessionToken, expiredTime);
            startDownload(videoUrl,localCredentialProvider);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载Cos中的视频，并 将其保存在本地
     * @param videoUrl
     */
    private void startDownload(String videoUrl,LocalSessionCredentialProvider localCredentialProvider){
        //创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
        String appid = "1253738394";
        String region = "ap-guangzhou";
        String bucket = "gxxc";
        long signDuration = 600; //签名的有效期，单位为秒
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion(appid, region)
                .setDebuggable(true)
                .builder();

        Log.i("COSUtils","开始下载");
        CosXmlService cosXmlService = new CosXmlService(MyApplication.getAppContext(),serviceConfig, localCredentialProvider);
        String fileName = System.currentTimeMillis() + ".mp4";
        try {
            final String savePath = Environment.getExternalStorageDirectory().getCanonicalPath()+ "/共享相册/";
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, videoUrl, savePath);
            getObjectRequest.setSign(signDuration,null,null);
            getObjectRequest.setProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(long progress, long max) {
                    float result = (float) (progress * 100.0/max);
                    Log.w("TEST","progress =" + (long)result + "%");
                }
            });

            cosXmlService.getObjectAsync(getObjectRequest, new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
                    Log.w("TEST","success" + cosXmlResult.printResult());
                    scanPhoto(MyApplication.getAppContext(),savePath);
                    mListener.callBack(0,"视频保存成功");
                }

                @Override
                public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException

                        serviceException)  {
                    String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
                    Log.w("TEST",errorMsg);
                    mListener.callBack(-1,"视频保存失败");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量保存图片
     */
    public void saveAllImage(ArrayList<LocalMedia> mSelectedImages) {
        if (mSelectedImages != null){
            if (mSelectedImages.size() == 0){
                mListener.callBack(-1,"至少要有一张图片");
            }else {
                saveImageToSdCard(mSelectedImages);
            }
        }
    }

    private void saveImageToSdCard(ArrayList<LocalMedia>mSelectedImages) {
        for (LocalMedia media:mSelectedImages) {
            saveImageByGlide(media.getPath());
        }
        try {
            filePath = Environment.getExternalStorageDirectory().getCanonicalPath()+ "/共享相册";
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(filePath)){
            mListener.callBack(-1,"批量保存失败");
        }else {
            mListener.callBack(0,"图片成功保存到"+filePath);
        }
    }

    /**
     * 通过Glide将图片保存在本地
     * @param url
     */
    private void saveImageByGlide(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestOptions myOptions = new RequestOptions()
                        .fitCenter();
                try {
                    Bitmap bitmap = Glide.with(MyApplication.getAppContext())
                            .asBitmap()
                            .apply(myOptions)
                            .load(url).into(720, 1280)
                            .get();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] bytes = baos.toByteArray();
                    savaBitmap(System.currentTimeMillis()+".jpg", bytes);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void savaBitmap(String imgName, byte[] bytes) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileOutputStream fos = null;
            //filePath = null;
            try {
                filePath = Environment.getExternalStorageDirectory().getCanonicalPath()+ "/共享相册";
                File imgDir = new File(filePath);
                if (!imgDir.exists()) {
                    imgDir.mkdirs();
                }
                imgName = filePath + "/" + imgName;
                fos = new FileOutputStream(imgName);
                fos.write(bytes);
                scanPhoto(MyApplication.getAppContext(),imgName);
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
}
