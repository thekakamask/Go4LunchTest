package com.example.go4lunch.activities.ui;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/*public class WebViewActivity extends AppCompatActivity {
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        //String url = getIntent().getStringExtra("website");
        WebResourceRequest request = getIntent().getStringExtra("website");
        MyWebViewClient myWebViewClient = new MyWebViewClient();
        //myWebViewClient.shouldOverrideUrlLoading(mWebView, url);
        myWebViewClient.shouldOverrideUrlLoading(mWebView, request);
        //PROGRESS BAR
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                mProgressBar.setProgress(progress);
                if(progress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });
        //HIDING ACTION BAR
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.mWebView.destroy();
    }
    *//*private class MyWebViewClient extends WebViewClient {
        //@SuppressLint("SetJavaScriptEnabled")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if( url != null) {
                view.loadUrl(url);
                WebSettings webSettings = mWebView.getSettings();
                // AVOID BUG IN THE WEBVIEW ACTIVITY WITH ACCESSING TO DOM
                webSettings.setDomStorageEnabled(true);
                // JAVASCRIPT ENABLED
                webSettings.setJavaScriptEnabled(true);
                // FORCE LINKS AND REDIRECTS TO OPEN IN THE WEBVIEW AND NOT IN A BROWSER
                mWebView.setWebViewClient(new WebViewClient());
            }
            return true;
        }
    }*//*
    private class MyWebViewClient extends WebViewClient {
        //@SuppressLint("SetJavaScriptEnabled")
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if( request != null) {
                //view.loadUrl(String.valueOf(request));
                view.loadUrl(request.getUrl().getPath());
                WebSettings webSettings = mWebView.getSettings();
                // AVOID BUG IN THE WEBVIEW ACTIVITY WITH ACCESSING TO DOM
                webSettings.setDomStorageEnabled(true);
                // JAVASCRIPT ENABLED
                webSettings.setJavaScriptEnabled(true);
                // FORCE LINKS AND REDIRECTS TO OPEN IN THE WEBVIEW AND NOT IN A BROWSER
                mWebView.setWebViewClient(new WebViewClient());
            }
            return true;
        }
    }
}*/
