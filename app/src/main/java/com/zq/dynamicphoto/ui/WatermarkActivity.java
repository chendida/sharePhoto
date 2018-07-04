package com.zq.dynamicphoto.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 图片编辑水印界面
 */
public class WatermarkActivity extends AppCompatActivity {

    @BindView(R.id.iv_picture)
    ImageView ivPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watermark);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_camera)
    public void onClicked() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == 1){
                if (data != null){
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        ImageLoaderUtils.displayImg(ivPicture, bitmap);
                    }
                }
            }
        }
    }
}
