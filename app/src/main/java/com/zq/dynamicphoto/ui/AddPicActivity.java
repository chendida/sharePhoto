package com.zq.dynamicphoto.ui;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.blankj.utilcode.util.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.PicAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.ui.widge.SelectPicDialog;
import com.zq.dynamicphoto.utils.PicSelectUtils;
import com.zq.dynamicphoto.utils.TitleUtils;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class AddPicActivity extends BaseActivity implements PicAdapter.AddPicListener {

    @BindView(R.id.et_description_content)
    EditText etDescriptionContent;
    @BindView(R.id.layout_article)
    AutoRelativeLayout layoutArticle;
    @BindView(R.id.id_grid_view_commit_answers)
    GridView mGridView;
    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    private PicAdapter mAdapter;
    SelectPicDialog selectPicDialog;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_pic;
    }

    @Override
    protected void initView() {
        TitleUtils.setTitleBar(getResources().getString(R.string.publish_image_and_text), tvTitle, layoutBack, layoutFinish);
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

    /**
     * 添加图片回调
     *
     * @param view
     * @param i
     */
    @Override
    public void onAddButtonClick(View view, int i) {
        switch (view.getId()) {
            case R.id.id_item_add_pic:
                List<LocalMedia> localMedia = mAdapter.getmList();
                if (localMedia != null){
                    if (localMedia.size() != 0){
                        int pictureType = PictureMimeType.isPictureType(localMedia.get(0).getPictureType());
                        if (pictureType == PictureConfig.TYPE_VIDEO){//选的视频
                            PicSelectUtils.getInstance().intoVideoSelect(this);
                        }else {//选的图片
                            PicSelectUtils.getInstance().intoPicSelect(this);
                        }
                    }else {
                        showSelectPicDialog();
                    }
                }else {
                    showSelectPicDialog();
                }
                break;
            case R.id.iv_item_image_view:

                break;
        }
    }


    private void showSelectPicDialog() {
        if (selectPicDialog == null){
            selectPicDialog = new SelectPicDialog(this, R.style.dialog, new SelectPicDialog.OnItemClickListener() {
                @Override
                public void onClick(Dialog dialog, int position) {
                    dialog.dismiss();
                    switch (position) {
                        case 1://选择图片
                            PicSelectUtils.getInstance().intoPicSelect(AddPicActivity.this);
                            break;
                        case 2://选择视频
                            PicSelectUtils.getInstance().intoVideoSelect(AddPicActivity.this);
                            break;
                    }
                }
            });
        }
        selectPicDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        selectPicDialog = null;
    }
}
