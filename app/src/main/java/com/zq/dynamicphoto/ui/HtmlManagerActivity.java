package com.zq.dynamicphoto.ui;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import com.tencent.mm.opensdk.utils.Log;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.utils.ShareCodeUtils;
import com.zq.dynamicphoto.utils.TitleUtils;
import butterknife.BindView;
import butterknife.OnClick;

public class HtmlManagerActivity extends BaseActivity {
    private static final String TAG = "HtmlManagerActivity";
    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.html_webView)
    WebView htmlWebView;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    int flag = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_html_manager;
    }

    @Override
    protected void initView() {
        flag = getIntent().getIntExtra(Constans.FLAG,0);
        String title = (String) getIntent().getSerializableExtra(Constans.HTML_TITLE);
        if (flag == 1) {
            layoutBack.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
            ivCamera.setImageDrawable(getResources().getDrawable(R.drawable.icon_up));
        } else {
            TitleUtils.setTitleBar(title, tvTitle, layoutBack, layoutFinish);
        }
    }

    @Override
    protected void initData() {
        loadHtml();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    private void loadHtml() {
        String html = (String) getIntent().getSerializableExtra(Constans.HTML);
        String url = Constans.HTML_Url + html;
        htmlWebView.getSettings().setJavaScriptEnabled(true);
        htmlWebView.loadUrl(url);
        Log.i(TAG, url);
        htmlWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showLoading();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                hideLoading();
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                hideLoading();
                super.onReceivedError(view, request, error);
            }
        });
    }

    @OnClick({R.id.layout_back, R.id.layout_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_finish:
                if (flag == 1){//表示二维码界面
                    ShareCodeUtils.shareToWx(this,htmlWebView);
                }
                break;
        }
    }
}
