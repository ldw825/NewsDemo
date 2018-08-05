package com.kent.newsdemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * author Kent
 * date 2018/7/31 031
 * version 1.0
 */
public class NewsDetailActivity extends AppCompatActivity {

    private WebView mWebView;
    private View mLoading;

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mLoading.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mLoading.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        String url = getIntent().getStringExtra("url");
        String channel = getIntent().getStringExtra("channel");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(channel);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mLoading = findViewById(R.id.loading);
        mWebView = findViewById(R.id.webview);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
//        settings.setLoadWithOverviewMode(true);
//        settings.setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewParent parent = mWebView.getParent();
        if (parent != null) {
            ((ViewGroup)parent).removeView(mWebView);
        }
        mWebView.stopLoading();
        mWebView.loadUrl("about:blank");
        mWebView.getSettings().setJavaScriptEnabled(false);
        mWebView.removeAllViews();
        mWebView.clearHistory();
        mWebView.destroy();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
