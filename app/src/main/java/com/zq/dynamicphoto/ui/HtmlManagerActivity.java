package com.zq.dynamicphoto.ui;

import android.graphics.Bitmap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import com.tencent.mm.opensdk.utils.Log;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.common.Constans;
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_html_manager;
    }

    @Override
    protected void initView() {
        String title = (String) getIntent().getSerializableExtra(Constans.HTML_TITLE);
        TitleUtils.setTitleBar(title, tvTitle, layoutBack, layoutFinish);
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

    @OnClick(R.id.layout_back)
    public void onViewClicked() {
        finish();
    }
}
