package com.zq.dynamicphoto.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectResult;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.base.Presenter;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.net.RetrofitApiService;
import com.zq.dynamicphoto.net.util.RetrofitUtil;
import com.zq.dynamicphoto.view.UploadView;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/3/7.
 */

public class CosUtils extends Presenter {
    String appid = "1253738394";
    String region = "ap-guangzhou";
    String bucket = "gxxc"; // cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454
    String cosPath = ""; //格式如 cosPath = "/test.txt";
    long signDuration = 600; //签名的有效期，单位为秒
    private static CosUtils instance = null;
    String savePath = null;
    //创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
    CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
            .setAppidAndRegion(appid, region)
            .setDebuggable(true)
            .builder();

    //创建获取签名类(请参考下面的生成签名示例，或者参考 sdk中提供的ShortTimeCredentialProvider类）
    static LocalSessionCredentialProvider localCredentialProvider;

    private Context mContext;
    private static UploadView mView;
    private Handler mMainHandler;
    private final static int MAIN_CALL_BACK = 1;
    private final static int MAIN_PROCESS = 2;
    private final static int SAVE_SUCCESS= 3;

    private CosUtils(Context context, UploadView view) {
        mContext = context;
        mView = view;

        mMainHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case MAIN_CALL_BACK:
                        if (null != mView)
                            Log.i("TEST","success");
                            Log.i("TEST","arg1 = "+msg.arg1);
                            Log.i("TEST","obg ="+msg.obj);
                            mView.onUploadResult(msg.arg1, (String) msg.obj);
                        break;
                    case MAIN_PROCESS:
                        if (null != mView)
                            mView.onUploadProcess(msg.arg1);
                        break;
                    case SAVE_SUCCESS:
                        Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    public static CosUtils getInstance(UploadView view) {
        mView = view;
        if (null == instance) {
            synchronized (CosUtils.class) {
                if (null == instance) {
                    instance = new CosUtils(MyApplication.getAppContext(),view);
                }
            }
        }
        return instance;
    }

    //创建 CosXmlService 对象，实现对象存储服务各项操作.
    static CosXmlService cosXmlService;
    /**
     * 图片保存到cos的绝对路径
     android/ dynamic/photo/ [userId]    动态相关图片(1)
     android/ dynamic/ video/[userId]    动态相关视频(2)
     android / dynamic/ [userId]/bg   动态背景图片(3)
     android /user/ [userId]/logo    用户头像相关图片(4)
     android /user/ [userId]/code    用户微信二维码相关图片(8)
     android /moments/[userId]/bg   朋友圈背景相关图片(5)
     android /moments/[userId]/forward  朋友圈转发图标相关图片(6)
     android /user/ [userId]/bg    用户背景相关图片(7）
     * @param flag
     * @return
     */
    private String createNetUrl(int flag) {
        String srcPath = "android/";
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        if (flag == 1){
            srcPath = srcPath + "dynamic/photo/" + userId +"/"+ System.currentTimeMillis() + ".jpg";
        }else if (flag == 2){
            srcPath = srcPath + "dynamic/video/" + userId +"/"+ System.currentTimeMillis() + ".mp4";
        }else if (flag == 3){
            srcPath = srcPath + "dynamic/" + userId + "/bg/"+ System.currentTimeMillis() + ".jpg";
        }else if (flag == 4){
            srcPath = srcPath + "user/" + userId + "/logo/"+ System.currentTimeMillis() + ".jpg";
        }else if (flag == 5){
            srcPath = srcPath + "moments/" + userId + "/bg/"+ System.currentTimeMillis() + ".jpg";
        }else if (flag == 6){
            srcPath = srcPath + "moments/" + userId + "/forward/"+ System.currentTimeMillis() + ".jpg";
        }else if (flag == 7){
            srcPath = srcPath + "user/" + userId + "/bg/"+ System.currentTimeMillis() + ".jpg";
        }else if (flag == 8){
            srcPath = srcPath + "user/" + userId + "/live/cover/"+ System.currentTimeMillis() + ".jpg";
        }else if (flag == 9){
            srcPath = srcPath + "user/" + userId + "/code/"+ System.currentTimeMillis() + ".jpg";
        }else if (flag == 10){
            srcPath = srcPath + "user/" + userId + "/watermark/"+ System.currentTimeMillis() + ".jpg";
        }
        return srcPath;
    }

    private void uploadCos(String srcPath, LocalSessionCredentialProvider localCredentialProvider, int flag) {
        cosXmlService = new CosXmlService(mContext,serviceConfig, localCredentialProvider);
        cosPath = createNetUrl(flag);
        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, cosPath, srcPath);
        putObjectRequest.setSign(signDuration,null,null); //若不调用，则默认使用sdk中sign duration（60s）

        /*设置进度显示
        实现 CosXmlProgressListener.onProgress(long progress, long max)方法，
        progress 已上传的大小， max 表示文件的总大小
        */
        putObjectRequest.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long progress, long max) {
                float result = (float) (progress * 100.0/max);
                //mView.onUploadProcess((int) (result*100));
                Log.w("TEST","progress =" + (long)result + "%");
                Message msg = new Message();
                msg.what = MAIN_PROCESS;
                msg.arg1 = (int) (result*100);

                mMainHandler.sendMessage(msg);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                //使用同步方法上传
                try {
                    PutObjectResult putObjectResult = cosXmlService.putObject(putObjectRequest);

                    Log.w("TEST","success: " + putObjectResult.accessUrl);

                    Log.w("TEST","success =" + putObjectResult.accessUrl);
                    Message msg = new Message();
                    msg.what = MAIN_CALL_BACK;
                    msg.arg1 = 0;
                    String url = "http://"+ putObjectResult.accessUrl;
                    //msg.obj = url.replace("cos.ap-guangzhou","file");
                    msg.obj = url;
                    mMainHandler.sendMessage(msg);
                } catch (CosXmlClientException e) {
                    //抛出异常
                    Log.w("TEST","CosXmlClientException =" + e.toString());
                } catch (CosXmlServiceException e) {
                    //抛出异常
                    Log.w("TEST","CosXmlServiceException =" + e.toString());
                }
            }
        }).start();
    }


    /**
     * 下载Cos中的视频，并 将其保存在本地
     * @param videoUrl
     */
    public void startDownload(String videoUrl, LocalSessionCredentialProvider localCredentialProvider){
        Log.i("COSUtils","开始下载");
        cosXmlService = new CosXmlService(mContext,serviceConfig, localCredentialProvider);
        String fileName = System.currentTimeMillis() + ".mp4";
        try {
            savePath = Environment.getExternalStorageDirectory().getCanonicalPath()+ "/共享相册/";
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
                    Message msg = new Message();
                    msg.what = SAVE_SUCCESS;
                    mMainHandler.sendMessage(msg);
                    scanPhoto(mContext,savePath);
                }

                @Override
                public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException

                        serviceException)  {
                    String errorMsg = clientException != null ? clientException.toString() : serviceException.toString();
                    Log.w("TEST",errorMsg);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
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


    public void downloadVideo(final String videoUrl){
        Log.i("COSUtils","videoUrl = "+ videoUrl);
        NetRequestBean netRequestBean = new NetRequestBean();
        DeviceProperties dr = DrUtils.getInstance();
        netRequestBean.setDeviceProperties(dr);
        RetrofitApiService retrofitApiService = RetrofitUtil.getInstance().getRetrofitApiService();
        final Call<Result> call = retrofitApiService.getTemporarySecretKey(netRequestBean);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result = response.body();
                if (result != null) {
                    if (result.getResultCode() == 0) {
                        dealWithResult(result,videoUrl);
                    } else {
                        Toast.makeText(mContext, result.getResultInfo(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
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
            localCredentialProvider = new LocalSessionCredentialProvider(tmpSecretId, tmpSecretKey, sessionToken, expiredTime);
            startDownload(videoUrl,localCredentialProvider);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件到COS
     * @param srcPath
     * @param flag
     */
    public void uploadToCos(final String srcPath, final int flag){
        NetRequestBean netRequestBean = new NetRequestBean();
        DeviceProperties dr = DrUtils.getInstance();
        netRequestBean.setDeviceProperties(dr);
        RetrofitApiService retrofitApiService = RetrofitUtil.getInstance().getRetrofitApiService();
        final Call<Result> call = retrofitApiService.getTemporarySecretKey(netRequestBean);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result = response.body();
                if (result != null) {
                    if (result.getResultCode() == 0) {
                        dealWithResult(result,srcPath,flag);
                    } else {
                        Toast.makeText(mContext, result.getResultInfo(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }

    private void dealWithResult(Result result, String srcPath, int flag) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result.getData());
            JSONObject jsonObject1 = jsonObject.optJSONObject("data");
            JSONObject jsonObject2 = jsonObject1.optJSONObject("credentials");
            long expiredTime = jsonObject1.optLong("expiredTime");
            String tmpSecretId = jsonObject2.optString("tmpSecretId");
            String tmpSecretKey = jsonObject2.optString("tmpSecretKey");
            String sessionToken = jsonObject2.optString("sessionToken");
            localCredentialProvider = new LocalSessionCredentialProvider(tmpSecretId, tmpSecretKey, sessionToken, expiredTime);
            uploadCos(srcPath,localCredentialProvider,flag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestory() {
        mView = null;
        mContext = null;
    }
}
