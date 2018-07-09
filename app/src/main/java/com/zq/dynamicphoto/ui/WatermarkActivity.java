package com.zq.dynamicphoto.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.Image;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.fragment.PictureSlideFragment;
import com.zq.dynamicphoto.ui.widge.SwitchButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 图片编辑水印界面
 */
public class WatermarkActivity extends AppCompatActivity {


    @BindView(R.id.btn_switchbutton)
    SwitchButton btnSwitchbutton;
    @BindView(R.id.imgs_viewpager)
    ViewPager viewPager;
    ArrayList<Image> imgs;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.iv_top_pic)
    ImageView ivTopPic;
    @BindView(R.id.iv_next_pic)
    ImageView ivNextPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watermark);
        ButterKnife.bind(this);
        btnSwitchbutton.setHydropowerListener(hydropowerListener);
        btnSwitchbutton.setSoftFloorListener(softFloorListener);
        imgs = (ArrayList<Image>) getIntent().getSerializableExtra(Constans.SELECT_LIST);

        viewPager.setAdapter(new PictureSlidePagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    @OnClick({R.id.layout_back, R.id.iv_top_pic, R.id.iv_next_pic})
    public void onClicked(View view) {
        int currentItem = viewPager.getCurrentItem();
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.iv_top_pic:
                viewPager.setCurrentItem(currentItem - 1);
                break;
            case R.id.iv_next_pic:
                viewPager.setCurrentItem(currentItem + 1);
                break;
        }
    }

    private class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {

        private PictureSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PictureSlideFragment.newInstance(imgs.get(position).getPath());
        }

        @Override
        public int getCount() {
            return imgs.size();
        }
    }
}
