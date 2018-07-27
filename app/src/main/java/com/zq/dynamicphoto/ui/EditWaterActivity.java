package com.zq.dynamicphoto.ui;

import android.view.View;
import android.widget.ImageView;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.ui.widge.WholeColorEditDialog;
import butterknife.BindView;
import butterknife.OnClick;

public class EditWaterActivity extends BaseActivity {
    private static final String TAG = "EditWaterActivity";
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.layout_whole_water_bg)
    AutoRelativeLayout layoutWholeWaterBg;
    private WholeColorEditDialog colorEditDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_water;
    }

    @Override
    protected void initView() {
        initDialog();
    }

    private void initDialog() {
        if (colorEditDialog == null) {
            colorEditDialog = new WholeColorEditDialog(this, R.style.dialog);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @OnClick({R.id.layout_bg, R.id.layout_whole_color})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_bg:
                int color = getResources().getColor(R.color.red_color);
                ivHead.setColorFilter(color);
                break;
            case R.id.layout_whole_color:
                /*int width = layoutWholeWaterBg.getLayoutParams().width;
                int height = layoutWholeWaterBg.getLayoutParams().height;
                Log.i(TAG,"width = " + width+",height = " + height);
                int color1 = getResources().getColor(R.color.btn_login_bg_color);
                ivHead.setColorFilter(color1);*/
                colorEditDialog.show();
                break;
        }
    }
}
