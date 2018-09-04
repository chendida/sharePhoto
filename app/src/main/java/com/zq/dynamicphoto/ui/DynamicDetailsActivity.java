package com.zq.dynamicphoto.ui;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.utils.Log;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.ui.widge.ShareDialog;
import com.zq.dynamicphoto.utils.ImageSaveUtils;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.ShareUtils;
import com.zq.dynamicphoto.utils.TitleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DynamicDetailsActivity extends BaseActivity implements ImageSaveUtils.DownLoadListener {
    private static final String TAG = "DynamicDetailsActivity";
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
    private ShareDialog shareDialog;
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

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 步骤2：根据协议的参数，判断是否是所需要的url
                // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
                //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）
                Log.i(TAG, "htmlUrl = " + url);
                Uri uri = Uri.parse(url);
                // 如果url的协议 = 预先约定的 js 协议
                // 就解析往下解析参数
                if (uri.getScheme().equals("js")) {
                    Log.i(TAG, "htmlUrl = " + "js调用了Android的方法1");
                    // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                    // 所以拦截url,下面JS开始调用Android需要的方法
                    if (uri.getAuthority().equals("Android")) {
                        //  步骤3：
                        // 执行JS所需要调用的逻辑
                        //System.out.println("js调用了Android的方法");
                        Log.i(TAG, "htmlUrl = " + "js调用了Android的方法");
                        // 可以在协议上带有参数并传递到Android上
                        String share = uri.getQueryParameter("share");
                        String xedit = uri.getQueryParameter("xedit");
                        if (!TextUtils.isEmpty(share)) {
                            Dynamic dynamic = new Gson().fromJson(share, Dynamic.class);
                            if (dynamic != null) {
                                showShareDialog(dynamic);
                            }
                        } else if (!TextUtils.isEmpty(xedit)) {
                            Dynamic dynamic = new Gson().fromJson(xedit, Dynamic.class);
                            if (dynamic != null) {
                                Log.i(TAG, dynamic.getId() + "id");
                                MFGT.gotoEditDynamicActivity(DynamicDetailsActivity.this, dynamic);
                            }
                        }
                    }
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    private void showShareDialog(final Dynamic dynamic) {
        shareDialog = new ShareDialog(this, R.style.dialog, new ShareDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                switch (position) {
                    case 1://分享给好友
                        ShareUtils.getInstance().shareFriend(dynamic,1,DynamicDetailsActivity.this);
                        break;
                    case 2://分享给微信朋友圈
                        ShareUtils.getInstance().shareFriend(dynamic,2,DynamicDetailsActivity.this);
                        break;
                    case 3://批量保存
                        if (dynamic != null){
                            showLoading();
                            ImageSaveUtils.getInstance(DynamicDetailsActivity.this).saveAll(dynamic);
                        }
                        break;
                }
            }
        });
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            ToastUtils.showShort(getResources().getString(R.string.have_no_wx));
        }else {
            shareDialog.show();
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @OnClick(R.id.layout_back)
    public void onClicked() {
        finish();
    }

    @Override
    public void callBack(int code, String msg) {
        hideLoading();
        ToastUtils.showShort(msg);
    }
}
