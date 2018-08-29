package com.zq.dynamicphoto.ui;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;
import com.luck.picture.lib.config.PictureConfig;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.DynamicBean;
import com.zq.dynamicphoto.bean.DynamicPhoto;
import com.zq.dynamicphoto.bean.DynamicVideo;
import com.zq.dynamicphoto.bean.MessageEvent;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.fragment.AddWaterFragment;
import com.zq.dynamicphoto.fragment.HomePageFragment;
import com.zq.dynamicphoto.fragment.LiveFragment;
import com.zq.dynamicphoto.fragment.MineFragment;
import com.zq.dynamicphoto.presenter.DynamicUploadPresenter;
import com.zq.dynamicphoto.ui.widge.ScrollViewPager;
import com.zq.dynamicphoto.utils.CDNUrl;
import com.zq.dynamicphoto.utils.CompressVideoUtils;
import com.zq.dynamicphoto.utils.CosUtils;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.SaveTasks;
import com.zq.dynamicphoto.utils.ShareUtils;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.VideoUtils;
import com.zq.dynamicphoto.view.CompressView;
import com.zq.dynamicphoto.view.IUploadDynamicView;
import com.zq.dynamicphoto.view.UploadView;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileBatchCallback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity<IUploadDynamicView,
        DynamicUploadPresenter<IUploadDynamicView>> implements IUploadDynamicView,
        UploadView,CompressView {
    private static final String TAG = "HomeActivity";
    @BindView(R.id.view_pager)
    ScrollViewPager viewPager;
    @BindView(R.id.rb_tab_dynamic)
    RadioButton rbTabDynamic;
    @BindView(R.id.rb_tab_friend_circle)
    RadioButton rbTabFriendCircle;
    @BindView(R.id.rb_tab_live)
    RadioButton rbTabLive;
    @BindView(R.id.rb_tab_mine)
    RadioButton rbTabMine;
    @BindView(R.id.rg_tab)
    RadioGroup rgTab;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    ViewPagerAdapter mViewPagerAdapter;
    private long exitTime = 0;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTitleStrs = new ArrayList<>();
    private HomePageFragment dynamicFragment;
    private AddWaterFragment friendCircleFragment;
    private LiveFragment liveFragment;
    private MineFragment mineFragment;
    private int mCurrentTabPos = 0;

    private Boolean isContinue = false;

    private Boolean getContinue() {
        return isContinue;
    }

    private void setContinue(Boolean aContinue) {
        isContinue = aContinue;
    }

    @Override
    protected int getLayoutId() {
        //取消严格模式  FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy( builder.build() );
        }
        return R.layout.activity_home;
    }

    public ArrayList<Fragment> getmFragments() {
        return mFragments;
    }

    @Override
    protected void initView() {
        initFragments();
        EventBus.getDefault().register(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent != null){
            DynamicBean dynamic = messageEvent.getDynamicBean();
            if (dynamic != null){
                if (dynamic.getIsShare() == 1){
                    share(dynamic,1);
                }else if (dynamic.getIsShare() == 2){
                    share(dynamic,2);
                }
                SaveTasks.getInstance().getList().add(dynamic);
                startUpload();
            }else {
                startUpload();
            }
        }
    }

    private void share(DynamicBean dynamicBean, int flag) {
        Dynamic dynamic = new Dynamic();
        dynamic.setContent(dynamicBean.getContent());
        dynamic.setDynamicType(dynamicBean.getPicType());
        if (dynamicBean.getPicType() == PictureConfig.TYPE_VIDEO){
            ArrayList<DynamicVideo>videos = new ArrayList<>();
            for (String url:dynamicBean.getmSelectedImages()) {
                DynamicVideo video = new DynamicVideo();
                video.setVideoURL(url);
                videos.add(video);
            }
            dynamic.setDynamicVideos(videos);
            if (videos.get(0).getVideoURL().startsWith("http")){
                ShareUtils.getInstance().shareFriend(dynamic,flag,this);
            }else {
                showLoading();
            }
        }else {
            ArrayList<DynamicPhoto>photos = new ArrayList<>();
            for (String url:dynamicBean.getmSelectedImages()) {
                DynamicPhoto photo = new DynamicPhoto();
                photo.setThumbnailURL(url);
                photos.add(photo);
            }
            dynamic.setDynamicPhotos(photos);
            ShareUtils.getInstance().shareFriend(dynamic,flag,this);
        }
    }

    private void startUpload() {
        ArrayList<DynamicBean> list = SaveTasks.getInstance().getList();
        if (list.size() > 0) {
            if (list.size() == 1 || getContinue()) {
                setContinue(false);
                DynamicBean dynamicBean = list.get(0);
                if (dynamicBean.getRequestType() == Constans.ADD_DYNAMIC) {
                    addDynamic(dynamicBean);
                } else if (dynamicBean.getRequestType() == Constans.EDIT_DYNAMIC) {
                    editDynamic(dynamicBean);
                } else if (dynamicBean.getRequestType() == Constans.REPEAT_DYNAMIC) {
                    repeatDynamic(dynamicBean);
                }
            }
        }
    }

    /**
     * 转发动态
     * @param dynamicBean
     */
    private void repeatDynamic(DynamicBean dynamicBean) {
        if (dynamicBean.getmSelectedImages().size() != 0) {
            int pictureType = dynamicBean.getPicType();
            if (pictureType == PictureConfig.TYPE_VIDEO){//视频
                repeatDynamicVideo(dynamicBean);
            }else {//图片
                repeatDynamicImages(dynamicBean);
            }
        }else {
            uploadPhotoDynamic(Constans.REPEAT_DYNAMIC,PictureConfig.TYPE_IMAGE,null);
        }
    }

    private void repeatDynamicVideo(DynamicBean dynamicBean) {
        if (dynamicBean.getmSelectedImages().get(0).startsWith("http")){//网络视频
            uploadPhotoDynamic(Constans.REPEAT_DYNAMIC,PictureConfig.TYPE_VIDEO,dynamicBean.getmSelectedImages());
        }else {//本地视频
            dynamicVideo(dynamicBean);
        }
    }

    private void repeatDynamicImages(DynamicBean dynamicBean) {
        if (dynamicBean.getmSelectedImages().size() != 0){
            ArrayList<String> images = new ArrayList<>();
            for (String url:dynamicBean.getmSelectedImages()) {
                if (url.startsWith("http")){
                    SaveTasks.getInstance().getList().get(0).getSelectUrl().add(url);
                }else {
                    images.add(url);
                }
            }
            if (images.size() > 0){
                compressImage(images,PictureConfig.TYPE_IMAGE);
            }else {
                uploadPhotoDynamic(Constans.REPEAT_DYNAMIC,PictureConfig.TYPE_IMAGE,SaveTasks.getInstance().getList().get(0).getSelectUrl());
            }
        }else {
            uploadPhotoDynamic(Constans.REPEAT_DYNAMIC,PictureConfig.TYPE_IMAGE,null);
        }
    }

    /**
     * 编辑动态
     * @param dynamicBean
     */
    private void editDynamic(DynamicBean dynamicBean) {
        if (dynamicBean.getmSelectedImages().size() != 0) {
            int pictureType = dynamicBean.getPicType();
            if (pictureType == PictureConfig.TYPE_VIDEO){//视频
                editDynamicVideo(dynamicBean);
            }else {//图片
                editDynamicImages(dynamicBean);
            }
        }else {
            uploadPhotoDynamic(Constans.EDIT_DYNAMIC,PictureConfig.TYPE_IMAGE,null);
        }
    }

    private void editDynamicImages(DynamicBean dynamicBean) {
        if (dynamicBean.getmSelectedImages().size() != 0){
            ArrayList<String> images = new ArrayList<>();
            for (String url:dynamicBean.getmSelectedImages()) {
                if (url.startsWith("http")){
                    SaveTasks.getInstance().getList().get(0).getSelectUrl().add(url);
                }else {
                    images.add(url);
                }
            }
            if (images.size() > 0){
                compressImage(images,PictureConfig.TYPE_IMAGE);
            }else {
                uploadPhotoDynamic(Constans.EDIT_DYNAMIC,PictureConfig.TYPE_IMAGE,SaveTasks.getInstance().getList().get(0).getSelectUrl());
            }
        }else {
            uploadPhotoDynamic(Constans.EDIT_DYNAMIC,PictureConfig.TYPE_IMAGE,null);
        }
    }

    private void editDynamicVideo(DynamicBean dynamicBean) {
        if (dynamicBean.getmSelectedImages().get(0).startsWith("http")){//网络视频
            uploadPhotoDynamic(Constans.EDIT_DYNAMIC,PictureConfig.TYPE_VIDEO,dynamicBean.getmSelectedImages());
        }else {//本地视频
            dynamicVideo(dynamicBean);
        }
    }

    /**
     * 新增动态
     * @param dynamicBean
     */
    private void addDynamic(DynamicBean dynamicBean) {
        if (dynamicBean.getmSelectedImages().size() != 0) {
            int pictureType = dynamicBean.getPicType();
            if (pictureType == PictureConfig.TYPE_VIDEO){//视频
                dynamicVideo(dynamicBean);
            }else {//图片
                addDynamicImages(dynamicBean);
            }
        }else {
            uploadPhotoDynamic(Constans.ADD_DYNAMIC,PictureConfig.TYPE_IMAGE,null);
        }
    }


    private void uploadPhotoDynamic(int type,int flag,ArrayList<String>selectUrl) {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        Dynamic dynamic = new Dynamic();
        dynamic.setContent(SaveTasks.getInstance().getList().get(0).getContent());
        dynamic.setUserId(userId);
        dynamic.setDynamicLabels(SaveTasks.getInstance().getList().get(0).getDynamicLabels());
        List<DynamicPhoto> dynamicPhotos = new ArrayList<>();
        List<DynamicVideo> dynamicVideos = new ArrayList<>();
        dynamic.setDynamicType(flag);
        if (flag == PictureConfig.TYPE_IMAGE){
            if (selectUrl != null) {
                for (String url : selectUrl) {
                    DynamicPhoto dynamicPhoto = new DynamicPhoto();
                    dynamicPhoto.setThumbnailURL(url);
                    dynamicPhotos.add(dynamicPhoto);
                }
                selectUrl.clear();
            }
        }else if (flag == PictureConfig.TYPE_VIDEO){
            DynamicVideo dynamicVideo = new DynamicVideo();
            for (String url : selectUrl) {
                if (url.endsWith(".mp4")) {
                    dynamicVideo.setVideoURL(url);
                } else {
                    dynamicVideo.setVideoCover(url);
                }
            }
            dynamicVideos.add(dynamicVideo);
            selectUrl.clear();
        }
        if (MyApplication.getAppContext().getResources().getString(R.string.one_can_see).equals(SaveTasks.getInstance().getList().get(0).getPermission())) {
            Log.i(TAG, "私密");
            dynamic.setIsOpen(2);
        } else {
            Log.i(TAG, "公开");
            dynamic.setIsOpen(1);
        }
        dynamic.setHeight(SaveTasks.getInstance().getList().get(0).getHeight());
        dynamic.setWidth(SaveTasks.getInstance().getList().get(0).getWidth());
        dynamic.setDynamicPhotos(dynamicPhotos);
        dynamic.setDynamicVideos(dynamicVideos);
        if (SaveTasks.getInstance().getList().get(0).getDynamicId() != null){
            dynamic.setId(SaveTasks.getInstance().getList().get(0).getDynamicId());
        }
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setDynamic(dynamic);
        if (mPresenter != null) {
            if (type == Constans.ADD_DYNAMIC) {
                mPresenter.dynamicUpload(netRequestBean);
            } else if (type == Constans.EDIT_DYNAMIC) {
                mPresenter.dynamicEdit(netRequestBean);
            } else {
                if (SaveTasks.getInstance().getList().get(0).getDynamicForward() != null){
                    netRequestBean.setDynamicForward(SaveTasks.getInstance().getList().get(0).getDynamicForward());
                }
                mPresenter.dynamicRepeat(netRequestBean);
            }
        }
    }

    /**
     * 压缩并上传图片
     *
     * @param
     */
    private void compressImage(final ArrayList<String> imageUrl, final int flag) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Tiny.FileCompressOptions tiny = new Tiny.FileCompressOptions();
                Tiny.getInstance().source(imageUrl.toArray(new String[imageUrl.size()]))
                        .batchAsFile().withOptions(tiny).batchCompress(new FileBatchCallback() {
                    @Override
                    public void callback(boolean isSuccess, String[] outfile, Throwable t) {
                        for (String s : outfile) {
                            CosUtils.getInstance(HomeActivity.this).uploadToCos(s, flag);
                        }
                    }
                });
            }
        }).start();
    }

    private void dynamicVideo(DynamicBean dynamicBean) {
        Bitmap videoThumbnail = VideoUtils.getInstance().getVideoThumbnail(dynamicBean.getmSelectedImages().get(0));
        if (videoThumbnail != null) {
            String thumbPath = VideoUtils.getInstance().saveImage(videoThumbnail);
            VideoUtils.getInstance().insertImageToSystemGallery(thumbPath,videoThumbnail);
            File file = new File(Environment.getExternalStorageDirectory(), thumbPath);
            VideoUtils.getInstance().getPicHightAndWidth(file.getPath());
            ArrayList<String> thumb = new ArrayList<>();
            thumb.add(file.getPath());
            compressImage(thumb, PictureConfig.TYPE_IMAGE);
            CompressVideoUtils.getInstance().
                    compressVideoResouce(this,dynamicBean.getmSelectedImages()
                            .get(0),this);
        }
    }

    private void addDynamicImages(DynamicBean dynamicBean) {
        compressImage(dynamicBean.getmSelectedImages(), PictureConfig.TYPE_IMAGE);
    }

    @Override
    public void onUploadProcess(int percent) {

    }

    @Override
    public void onUploadResult(int code, String url) {
        Log.i(TAG, "url = " + url);
        if (code == Constans.REQUEST_OK){
            ArrayList<DynamicBean> list = SaveTasks.getInstance().getList();
            ArrayList<String> selectUrl = SaveTasks.getInstance().getList().get(0).getSelectUrl();
            selectUrl.add(url);
            if (list.get(0).getPicType() == PictureConfig.TYPE_VIDEO){
                //当前任务传的是视频
                if (selectUrl.size() == 2) {
                    if (SaveTasks.getInstance().getList().get(0).getIsShare() == 2){
                        String videoUrl = "";
                        for (String video:selectUrl) {
                            if (video.endsWith(".mp4")){
                                videoUrl = video;
                                break;
                            }
                        }
                        hideLoading();
                        if (!TextUtils.isEmpty(videoUrl)){
                            Dynamic dynamic = new Dynamic();
                            dynamic.setDynamicType(PictureConfig.TYPE_VIDEO);
                            dynamic.setContent(SaveTasks.getInstance().getList().get(0).getContent());
                            ArrayList<DynamicVideo>videos = new ArrayList<>();
                            DynamicVideo video = new DynamicVideo();
                            video.setVideoURL(CDNUrl.toCNDURL(videoUrl));
                            videos.add(video);
                            dynamic.setDynamicVideos(videos);
                            ShareUtils.getInstance().shareFriend(dynamic,2,this);
                        }
                    }
                    if (list.get(0).getRequestType() == 1) {//新增
                        uploadPhotoDynamic(Constans.ADD_DYNAMIC,PictureConfig.TYPE_VIDEO,selectUrl);
                    }else if (list.get(0).getRequestType() == 2) {//编辑
                        uploadPhotoDynamic(Constans.EDIT_DYNAMIC,PictureConfig.TYPE_VIDEO,selectUrl);
                    }else if (list.get(0).getRequestType() == 3){//转发
                        uploadPhotoDynamic(Constans.REPEAT_DYNAMIC,PictureConfig.TYPE_VIDEO,selectUrl);
                    }
                }
            }else {//当前任务传的是图片
                if (list.get(0).getRequestType() == 1) {//新增
                    if (selectUrl.size() == list.get(0).getmSelectedImages().size()){
                        uploadPhotoDynamic(Constans.ADD_DYNAMIC,PictureConfig.TYPE_IMAGE,selectUrl);
                    }
                }else if (list.get(0).getRequestType() == 2) {//编辑
                    if (selectUrl.size() == list.get(0).getmSelectedImages().size()){
                        uploadPhotoDynamic(Constans.EDIT_DYNAMIC,PictureConfig.TYPE_IMAGE,selectUrl);
                    }
                }else if (list.get(0).getRequestType() == 3){//转发
                    if (selectUrl.size() == list.get(0).getmSelectedImages().size()){
                        uploadPhotoDynamic(Constans.REPEAT_DYNAMIC,PictureConfig.TYPE_IMAGE,selectUrl);
                    }
                }
            }
        }
    }

    private void clearUtils() {
        if (SaveTasks.getInstance().getList().size() != 0) {
            SaveTasks.getInstance().getList().remove(0);
            if (SaveTasks.getInstance().getList().size() > 0) {
                MessageEvent messageEvent = new MessageEvent();
                setContinue(true);
                EventBus.getDefault().post(messageEvent);
            }
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initFragments() {
        dynamicFragment = new HomePageFragment();
        friendCircleFragment = new AddWaterFragment();
        liveFragment = new LiveFragment();
        mineFragment = new MineFragment();

        mFragments.add(dynamicFragment);
        mFragments.add(friendCircleFragment);
        mFragments.add(liveFragment);
        mFragments.add(mineFragment);

        mTitleStrs.add(getResources().getString(R.string.tv_photo_dynamic));
        mTitleStrs.add(getResources().getString(R.string.tv_add_water));
        mTitleStrs.add(getResources().getString(R.string.tv_live));
        mTitleStrs.add(getResources().getString(R.string.tv_mine));
    }

    @Override
    protected void initData() {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mViewPagerAdapter);
        viewPager.setCurrentItem(mCurrentTabPos);
        initListener();
    }

    private void initListener() {
        rgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int pos = 0;
                switch (checkedId) {
                    case R.id.rb_tab_dynamic:
                        pos = 0;
                        break;
                    case R.id.rb_tab_friend_circle:
                        pos = 1;
                        break;
                    case R.id.rb_tab_live:
                        pos = 2;
                        break;
                    case R.id.rb_tab_mine:
                        pos = 3;
                        break;
                    default:
                        break;
                }
                viewPager.setCurrentItem(pos);
                tvTitle.setText(mTitleStrs.get(pos));
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tvTitle.setText(mTitleStrs.get(position));
                switch (position) {
                    case 0:
                        rbTabDynamic.setChecked(true);
                        break;
                    case 1:
                        rbTabFriendCircle.setChecked(true);
                        break;
                    case 2:
                        rbTabLive.setChecked(true);
                        break;
                    case 3:
                        rbTabMine.setChecked(true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected DynamicUploadPresenter<IUploadDynamicView> createPresenter() {
        return new DynamicUploadPresenter<>();
    }

    @OnClick(R.id.layout_finish)
    public void onClicked() {
        MFGT.gotoAddPicActivity(this);
    }

    @Override
    public void showUploadDynamicResult(Result result) {
        clearUtils();
    }

    @Override
    public void showEditDynamicResult(Result result) {
        clearUtils();
    }

    @Override
    public void showRepeatDynamicResult(Result result) {
        clearUtils();
    }

    @Override
    public void onCompressResult(int code, String url) {
        Log.i(TAG,"videoUrl = "+url);
        if (code == Constans.REQUEST_OK) {
            CosUtils.getInstance(this).uploadToCos(url, 2);
        }else {
            ToastUtils.showShort(url);
        }
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 1000) {
            ToastUtils.showShort(R.string.exit_program);
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
