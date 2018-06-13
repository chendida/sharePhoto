package com.zq.dynamicphoto.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.PicAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPicActivity extends BaseActivity implements PicAdapter.AddPicListener{

    @BindView(R.id.et_description_content)
    EditText etDescriptionContent;
    @BindView(R.id.layout_article)
    AutoRelativeLayout layoutArticle;
    @BindView(R.id.id_grid_view_commit_answers)
    GridView mGridView;
    private PicAdapter mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_pic;
    }

    @Override
    protected void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ArrayList<String> mSelectedImages = new ArrayList<>();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /**
     * 添加图片回调
     * @param view
     * @param i
     */
    @Override
    public void onAddButtonClick(View view, int i) {
        switch (view.getId()){
            case R.id.id_item_add_pic:
                ToastUtils.showShort("添加");
                break;
            case R.id.iv_item_image_view:

                break;
        }
    }
}
