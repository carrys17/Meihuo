package com.example.shang.meihuo.module.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shang.meihuo.R;
import com.example.shang.meihuo.base.BaseActivity;

import butterknife.BindView;

public class WebViewActivity extends BaseActivity implements WebContract.IWebView {

    public static final String GANK_URL = "com.nanchen.aiyagirl.module.web.WebViewActivity.gank_url";
    public static final String GANK_TITLE = "com.nanchen.aiyagirl.module.web.WebViewActivity.gank_title";
    @BindView(R.id.web_title)
    TextView mWebTitle;
    @BindView(R.id.web_toolbar)
    Toolbar mWebToolbar;
    @BindView(R.id.web_progressBar)
    ProgressBar mWebProgressBar;
    @BindView(R.id.web_appbar)
    AppBarLayout mWebAppbar;
    @BindView(R.id.web_view)
    WebView mWebView;

    private WebContract.IWebPresenter mWebPresenter;

    public static void start(Context context,String url,String title){
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(WebViewActivity.GANK_TITLE,title);
        intent.putExtra(WebViewActivity.GANK_URL,url);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        mWebPresenter = new WebPresenter(this);

        mWebToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mWebPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebPresenter.unSubscribe();
    }

    @Override
    public Activity getWebViewContext() {
        return this;
    }

    @Override
    public void setGankTitle(String title) {
        mWebTitle.setText(title);
    }

    @Override
    public void loadGankUrl(String url) {
        mWebView.loadUrl(url);
    }

    @Override
    public void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        settings.setJavaScriptEnabled(true);    //支持js
        settings.setAppCacheEnabled(true);
        //  这个属性可以让webview只显示一列，也就是自适应页面大小,不能左右滑动
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);  //支持缩放 

        //使用WebChromeClient 可以操作Javascript dialogs（js脚本对话框）, favicons（添加收藏的标志），
        //  titles（标题）, 和 progress（进度条）.
        //  简单的说，如果除了加载HTML的话，只需要用WebViewClient即可，但是在进行兼容互联网上
        // 附加javascript的页面的时候和调用javascript对话框的时候，或者功能较为复杂的内嵌操作的时候，
        // 建议使用WebChromeClient
        mWebView.setWebChromeClient(new MyWebChrome());
        mWebView.setWebViewClient(new MyWebClient());
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            //进不来- -！马丹
         //   Log.i("xyz","进来了这个方法");
            mWebView.goBack();
        } else {
            finish();
        }
    }

    private class MyWebChrome extends WebChromeClient {
        //   加载过程中
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mWebProgressBar.setVisibility(View.VISIBLE);
            mWebProgressBar.setProgress(newProgress);
        }
    }

    private class MyWebClient extends WebViewClient {
        //  加载完成后
        @Override
        public void onPageFinished(WebView view, String url) {
            mWebProgressBar.setVisibility(View.GONE);
        }
    }

}
