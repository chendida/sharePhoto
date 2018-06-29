package com.zq.dynamicphoto.utils;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.qiniu.pili.droid.shortvideo.PLShortVideoTranscoder;
import com.qiniu.pili.droid.shortvideo.PLVideoSaveListener;
import com.zq.dynamicphoto.view.CompressView;

import static com.qiniu.pili.droid.shortvideo.PLErrorCode.ERROR_LOW_MEMORY;
import static com.qiniu.pili.droid.shortvideo.PLErrorCode.ERROR_NO_VIDEO_TRACK;
import static com.qiniu.pili.droid.shortvideo.PLErrorCode.ERROR_SRC_DST_SAME_FILE_PATH;

/**
 * Created by Administrator on 2018/6/29.
 */

public class CompressVideoUtils {
    private static CompressVideoUtils instance;

    /**
     * 单例
     * @return
     */
    public static CompressVideoUtils getInstance(){
        if (null == instance) {
            synchronized (CompressVideoUtils.class) {
                if (null == instance) {
                    instance = new CompressVideoUtils();
                }
            }
        }
        return instance;
    }
    /**
     * 压缩视频
     *
     * @param mContext
     * @param filepath
     */
    public void compressVideoResouce(final Context mContext, String filepath, final CompressView view) {
        Log.i("precent","filePath = "+filepath);
        if (TextUtils.isEmpty(filepath)) {
            view.onCompressResult(-1,"请先选择转码文件！");
            return;
        }
        //PLShortVideoTranscoder初始化，三个参数，第一个context，第二个要压缩文件的路径，第三个视频压缩后输出的路径
        PLShortVideoTranscoder mShortVideoTranscoder = new PLShortVideoTranscoder(mContext, filepath, Environment.DIRECTORY_MOVIES + System.currentTimeMillis()+"/compress.mp4");
        MediaMetadataRetriever retr = new MediaMetadataRetriever();
        retr.setDataSource(filepath);
        String height = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
        String width = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽度
        //String rotation = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION); // 视频旋转方向
        int transcodingBitrateLevel = 4;//我这里选择的2500*1000压缩，这里可以自己选择合适的压缩比例
        mShortVideoTranscoder.transcode(Integer.parseInt(width), Integer.parseInt(height), getEncodingBitrateLevel(transcodingBitrateLevel), false, new PLVideoSaveListener() {
            @Override
            public void onSaveVideoSuccess(String s) {
                view.onCompressResult(0,s);
            }

            @Override
            public void onSaveVideoFailed(final int errorCode) {
                switch (errorCode) {
                    case ERROR_NO_VIDEO_TRACK:
                        view.onCompressResult(-1,"该文件没有视频信息！");
                        break;
                    case ERROR_SRC_DST_SAME_FILE_PATH:
                        view.onCompressResult(-1,"源文件路径和目标路径不能相同！");
                        break;
                    case ERROR_LOW_MEMORY:
                        view.onCompressResult(-1,"手机内存不足，无法对该视频进行时光倒流！");
                        break;
                    default:
                        view.onCompressResult(-1,"transcode failed: " + errorCode);
                }
            }

            @Override
            public void onSaveVideoCanceled() {
//                LogUtil.e("onSaveVideoCanceled");
            }

            @Override
            public void onProgressUpdate(float percentage) {
                Log.i("precent","percentage = "+percentage);
//                LogUtil.e("onProgressUpdate==========" + percentage);
            }
        });
    }

    /**
     * 设置压缩质量
     *
     * @param position
     * @return
     */
    private int getEncodingBitrateLevel(int position) {
        return ENCODING_BITRATE_LEVEL_ARRAY[position];
    }

    /**
     * 选的越高文件质量越大，质量越好
     */
    public static final int[] ENCODING_BITRATE_LEVEL_ARRAY = {
            500 * 1000,
            800 * 1000,
            1000 * 1000,
            1200 * 1000,
            1600 * 1000,
            2000 * 1000,
            2500 * 1000,
            4000 * 1000,
            8000 * 1000,
    };
}
