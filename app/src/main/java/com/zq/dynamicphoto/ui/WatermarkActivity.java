package com.zq.dynamicphoto.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.photoview.PhotoViewAttacher;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.Image;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.fragment.PictureSlideFragment;
import com.zq.dynamicphoto.ui.widge.NoPreloadViewPager;
import com.zq.dynamicphoto.ui.widge.SwitchButton;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.waterutil.EffectUtil;
import com.zq.dynamicphoto.waterutil.customview.MyHighlightView;
import com.zq.dynamicphoto.waterutil.customview.MyImageViewDrawableOverlay;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 图片编辑水印界面
 */
public class WatermarkActivity extends AppCompatActivity {
    private static final String TAG = "WatermarkActivity";

    @BindView(R.id.btn_switchbutton)
    SwitchButton btnSwitchbutton;
    @BindView(R.id.imgs_viewpager)
    NoPreloadViewPager viewPager;
    ArrayList<Image> imgs;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.iv_top_pic)
    ImageView ivTopPic;
    @BindView(R.id.iv_next_pic)
    ImageView ivNextPic;
    private PictureSlidePagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watermark);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        EffectUtil.clear();
        btnSwitchbutton.setHydropowerListener(hydropowerListener);
        btnSwitchbutton.setSoftFloorListener(softFloorListener);
        imgs = (ArrayList<Image>) getIntent().getSerializableExtra(Constans.SELECT_LIST);
        mAdapter = new PictureSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setOnPageChangeListener(new NoPreloadViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (imgs.size() == 1) {
                    ivNextPic.setVisibility(View.GONE);
                    ivTopPic.setVisibility(View.GONE);
                } else {
                    if ((position + 1) == imgs.size()) {//最后一张
                        ivNextPic.setVisibility(View.GONE);
                    } else {
                        ivNextPic.setVisibility(View.VISIBLE);
                    }
                    if (position == 0) {
                        ivTopPic.setVisibility(View.GONE);
                    } else {
                        ivTopPic.setVisibility(View.VISIBLE);
                    }
                }
                tvNum.setText(String.valueOf(position + 1) + "/" + imgs.size());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }




    SwitchButton.HydropowerListener hydropowerListener = new SwitchButton.HydropowerListener() {
        @Override
        public void hydropower() {
            ToastUtils.showShort("单张");
        }
    };
    SwitchButton.SoftFloorListener softFloorListener = new SwitchButton.SoftFloorListener() {
        @Override
        public void softFloor() {
            ToastUtils.showShort("批量");
        }
    };

    @OnClick({R.id.layout_back, R.id.iv_top_pic, R.id.iv_next_pic,
        R.id.layout_save})
    public void onClicked(View view) {
        int currentItem = viewPager.getCurrentItem();
        switch (view.getId()) {
            case R.id.layout_back:
                EventBus.getDefault().post(imgs.get(0));
                //finish();
                break;
            case R.id.iv_top_pic:
                viewPager.setCurrentItem(currentItem - 1);
                break;
            case R.id.iv_next_pic:
                viewPager.setCurrentItem(currentItem + 1);
                break;
            case R.id.layout_save:
                Log.i("PictureSlideFragment","save");
                Image image = new Image(null);
                EventBus.getDefault().post(image);
                break;
        }
    }


    private class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {
        private PictureSlideFragment fragment;
        private PictureSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public PictureSlideFragment getItem(int position) {
            fragment = PictureSlideFragment.newInstance(imgs,imgs.get(position));
            return fragment;
        }

        @Override
        public int getCount() {
            return imgs.size();
        }
    }

    /*private class PictureSlidePagerAdapter extends PagerAdapter {
        private Context mContext;
        private PhotoViewAttacher mAttacher;
        private MyImageViewDrawableOverlay mImageView;
        private AutoRelativeLayout drawArea;
        private int position;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        private PictureSlidePagerAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return imgs == null ? 0 :imgs.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return (view == object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            if (object != null && imgs != null) {
                String resId = (String) ((AutoRelativeLayout)object).getTag();
                if (resId != null) {
                    for (int i = 0; i < imgs.size(); i++) {
                        if (resId.equals(imgs.get(i).getPath())) {
                            return i;
                        }
                    }
                }
            }
            return ViewPager.SCROLLBAR_POSITION_DEFAULT;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            if (imgs != null && position < imgs.size()) {
                setPosition(position);
                String resId = imgs.get(position).getPath();
                if (resId != null) {
                        View itemView = LayoutInflater.from(mContext).inflate(R.layout.fragment_picture_slide, null, false);
                        //此处假设所有的照片都不同，用resId唯一标识一个itemView；也可用其它Object来标识，只要保证唯一即可
                        ImageView imageView = itemView.findViewById(R.id.iv_main_pic);
                        drawArea = itemView.findViewById(R.id.drawing_view_container);
                        mAttacher = new PhotoViewAttacher(imageView);
                        if (mContext != null) {
                            Glide.with(mContext).load(imgs.get(position).getPath()).into(imageView);
                            mAttacher.update();
                        }
                        itemView.setTag(resId);
                        ((ViewPager) container).addView(itemView);
                        //initCanvasWater();
                        return itemView;
                }
            }
            return null;
        }


        private void getPicHeightAndWidth(String url,int position) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            *//**
             * 最关键在此，把options.inJustDecodeBounds = true;
             * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
             *//*
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(url, options); // 此时返回的bitmap为null
            *//**
             *options.outHeight为原始图片的高
             *//*
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = width * options.outHeight / options.outWidth;
            initView(width,height,position);
        }

        private void initView(int width,int height,int position) {
            if (position == viewPager.getCurrentItem()) {
                AutoRelativeLayout.LayoutParams rlp = new AutoRelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                int heightPixels = getResources().getDisplayMetrics().heightPixels;//屏幕高度
                float scale = heightPixels / 1080f;//倍数
                rlp.topMargin = (int) ((heightPixels - (196 * scale + height)) / 2);
                rlp.bottomMargin = rlp.topMargin;
                //添加贴纸水印的画布
                View overlay = LayoutInflater.from(mContext).inflate(
                        R.layout.view_drawable_overlay, null);
                mImageView = overlay.findViewById(R.id.drawable_overlay);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
                mImageView.setLayoutParams(params);
                overlay.setLayoutParams(params);
                drawArea.addView(overlay, rlp);
            }
        }

        //初始化贴图
        private void initStickerToolBar(Image image) {
            if (mImageView == null) {
                Log.i(TAG, "mImageView == null");
                return;
            }
            Log.i(TAG, "mImageView != null");
            EffectUtil.addStickerImage(mImageView, image,
                    new EffectUtil.StickerCallback() {
                        @Override
                        public void onRemoveSticker(Image sticker) {
                        }
                    });
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //注意：此处position是ViewPager中所有要显示的页面的position，与Adapter mDrawableResIdList并不是一一对应的。
            //因为mDrawableResIdList有可能被修改删除某一个item，在调用notifyDataSetChanged()的时候，ViewPager中的页面
            //数量并没有改变，只有当ViewPager遍历完自己所有的页面，并将不存在的页面删除后，二者才能对应起来
            if (object != null) {
                ViewGroup viewPager = ((ViewGroup) container);
                int count = viewPager.getChildCount();
                for (int i = 0; i < count; i++) {
                    View childView = viewPager.getChildAt(i);
                    if (childView == object) {
                        viewPager.removeView(childView);
                        break;
                    }
                }
            }
        }

        private void initCanvasWater(){
            if (mImageView == null){
                Log.i(TAG, "mImageView1 == null");
                return;
            }
            Log.i(TAG, "mImageView1 != null");
            mImageView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (EffectUtil.getHightlistViews() != null){
                                Log.i(TAG,"EffectUtil.getHightlistViews().size() = "
                                        + EffectUtil.getHightlistViews().size());
                                if (EffectUtil.getHightlistViews().size() != 0){
                                    for (MyHighlightView view :EffectUtil.getHightlistViews()) {
                                        mImageView.addHighlightView(mImageView.getWidth(),
                                                        mImageView.getHeight(),view,0);
                                    }
                                }
                            }
                            mImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    });
        }
    }*/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(Image image) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
