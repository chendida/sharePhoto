package com.zq.dynamicphoto.mylive.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.mylive.ui.LiveActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import static com.tencent.rtmp.TXLiveConstants.PLAY_ERR_NET_DISCONNECT;
import static com.tencent.rtmp.TXLiveConstants.PLAY_WARNING_RECONNECT;
import static com.tencent.rtmp.TXLiveConstants.PUSH_WARNING_NET_BUSY;

/**
 * 该Fragment用于对直播或观看直播的初始化
 * 直播的控件的创建以及销毁等等都可以在这里进行操作，这样就与我们自己的交互代码分离了
 */
public class LiveViewFragment extends BaseFragment {
    private static final String TAG = "LiveViewFragment";
    @BindView(R.id.iv_init_bg)
    ImageView ivInitBg;
    @BindView(R.id.video_view)
    TXCloudVideoView videoView;
    private TXLivePushConfig mLivePushConfig = new TXLivePushConfig();
    TXLivePusher mLivePusher;
    TXLivePlayer mLivePlayer;
    //断网提示
    public static final String INTERFACE_NETWORK_ERROR = LiveViewFragment.class.getName() + "networkError";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_liveview;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (LiveActivity.getIsAnchor()) {
            startPush(LiveActivity.getNewLiveRoom().getPushURL());
        }else{
            startPlay(LiveActivity.getNewLiveRoom().getPlay_url1());
        }
    }

    /**
     * 开始拉流
     * @param playUrl
     */
    private void startPlay(String playUrl) {
        Log.i("playUrl","playUrl = "+playUrl);
        //创建 player 对象
        mLivePlayer = new TXLivePlayer(getActivity());
        mLivePlayer.setPlayListener(new ITXLivePlayListener() {
            @Override
            public void onPlayEvent(int i, Bundle bundle) {
                Log.i("playstatus","i = "+ i);
                if (i == PLAY_ERR_NET_DISCONNECT || i == PLAY_WARNING_RECONNECT){
                    ToastUtils.showShort(getResources().getString(R.string.wifi_error));
                }
            }

            @Override
            public void onNetStatus(Bundle bundle) {

            }
        });
        //关键 player 对象与界面 view
        mLivePlayer.setPlayerView(videoView);
        mLivePlayer.startPlay(playUrl+".flv", TXLivePlayer.PLAY_TYPE_LIVE_FLV);
    }

    private void startPush(String livePushUrl) {
        if (mLivePusher == null) {
            mLivePusher = new TXLivePusher(getActivity());
            mLivePushConfig.setAutoAdjustBitrate(false);
            //切后台推流图片
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pause_publish, options);
            mLivePushConfig.setPauseImg(bitmap);
            mLivePushConfig.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO| TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO);
            mLivePushConfig.setBeautyFilter(6, 3, 0);
            mLivePushConfig.setFaceSlimLevel(5);
            mLivePushConfig.setEyeScaleLevel(5);
            mLivePushConfig.setTouchFocus(true);
            mLivePushConfig.enableHighResolutionCaptureMode(false);
            mLivePusher.setConfig(mLivePushConfig);
            mLivePusher.setBeautyFilter(0,6,6,6);
            //设置视频质量：高清
            mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION, false, false);
            videoView.enableHardwareDecode(true);
            mLivePusher.startCameraPreview(videoView);
        }
        mLivePusher.setPushListener(new ITXLivePushListener() {
            @Override
            public void onPushEvent(int i, Bundle bundle) {
                Log.i(TAG,"pushListener = "+ i);
                if (i == PUSH_WARNING_NET_BUSY){
                    ToastUtils.showShort(getResources().getString(R.string.wifi_error));
                }
            }

            @Override
            public void onNetStatus(Bundle bundle) {

            }
        });
        Log.i("pushUrl","开始推流" + "pushUrl = " + livePushUrl);
        mLivePusher.startPusher(livePushUrl);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (LiveActivity.getIsAnchor()) {
            stopRtmpPublish();
        }else {
            mLivePlayer.stopPlay(true);
            if (videoView != null) {
                videoView.onDestroy();
            }
        }
    }

    //结束推流，注意做好清理工作
    public void stopRtmpPublish() {
        mLivePusher.stopCameraPreview(true); //停止摄像头预览
        mLivePusher.stopPusher();            //停止推流
        mLivePusher.setPushListener(null);   //解绑 listener
    }


    public void changeCamera(){
        if (mLivePusher != null) {
            mLivePusher.switchCamera();
        }
    }
}