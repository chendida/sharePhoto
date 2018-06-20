package com.zq.dynamicphoto.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.PicAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.DynamicLabel;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.PicSelectUtils;
import com.zq.dynamicphoto.utils.SaveLabelUtils;
import com.zq.dynamicphoto.utils.SoftUtils;
import com.zq.dynamicphoto.utils.TitleUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddPicActivity extends BaseActivity implements PicAdapter.AddPicListener {
    private static final String TAG = "AddPicActivity";
    @BindView(R.id.layout_article)
    AutoRelativeLayout layoutArticle;
    @BindView(R.id.id_grid_view_commit_answers)
    GridView mGridView;
    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.et_description_content)
    EditText etDescriptionContent;
    @BindView(R.id.tv_label)
    TextView tvLabel;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.check_clause)
    CheckBox checkClause;
    private PicAdapter mAdapter;
    private final int MIN_DELAY_TIME = 1000;  // 两次点击间隔不能少于500ms
    private long lastClickTime;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_pic;
    }

    @Override
    protected void initView() {
        TitleUtils.setTitleBar(getResources().getString(R.string.publish_image_and_text), tvTitle, layoutBack, ivCamera, tvFinish);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ArrayList<LocalMedia> mSelectedImages = new ArrayList<>();
        mSelectedImages.clear();
        mAdapter = new PicAdapter(AddPicActivity.this, mSelectedImages, this);
        mGridView.setAdapter(mAdapter);

        initListener();
    }

    private void initListener() {
        layoutArticle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!TextUtils.isEmpty(etDescriptionContent.getText().toString())) {
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(etDescriptionContent.getText());
                    Toast.makeText(AddPicActivity.this, "已复制到粘贴板", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 接收图片选择器返回结果，更新所选图片集合
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    PicSelectUtils.getInstance().clear();
                    // 图片选择结果回调
                    ArrayList<LocalMedia> newFiles = (ArrayList<LocalMedia>) PictureSelector.obtainMultipleResult(data);
                    mAdapter.initDynamicList(newFiles);
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<DynamicLabel> dynamicLabels = SaveLabelUtils.getInstance().getDynamicLabels();
        if (dynamicLabels != null) {
            Log.i(TAG, " dynamicLabels = " + dynamicLabels.size());
            if (dynamicLabels.size() != 0) {
                String str = "";
                for (DynamicLabel d : dynamicLabels) {
                    str = str + d.getLabeltext() + ",";
                }
                if (!TextUtils.isEmpty(str)) {
                    tvLabel.setText(str.substring(0, str.length() - 1));
                }
            } else {
                tvLabel.setText(getResources().getString(R.string.by_label_category));
            }
        }
    }

    /**
     * 添加图片回调
     *
     * @param view
     * @param i
     */
    @Override
    public void onAddButtonClick(View view, int i) {
        ArrayList<LocalMedia> localMedia = mAdapter.getmList();
        switch (view.getId()) {
            case R.id.id_item_add_pic:
                PicSelectUtils.getInstance().gotoSelectPicOrVideo(localMedia, this);
                break;
            case R.id.iv_item_image_view://预览
                Log.i(TAG, "position = " + i);
                PicSelectUtils.getInstance().preview(i, localMedia, this);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SaveLabelUtils.getInstance().getDynamicLabels().clear();
    }

    public boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    @OnClick({R.id.layout_back, R.id.layout_article, R.id.layout_label, R.id.layout_who_can_see,
            R.id.check_clause, R.id.tv_about_clause, R.id.btn_one_key_share
            , R.id.layout_finish})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_finish:
                if (checkClause.isChecked()) {
                    toUpload();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.please_read_and_agree_clause), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_article:
                SoftUtils.softShow(this);
                break;
            case R.id.layout_label:
                MFGT.gotoAddLabelActivity(this);
                break;
            case R.id.layout_who_can_see:
                break;
            case R.id.check_clause:
                break;
            case R.id.tv_about_clause:
                break;
            case R.id.btn_one_key_share:
                break;
        }
    }

    private void toUpload() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
