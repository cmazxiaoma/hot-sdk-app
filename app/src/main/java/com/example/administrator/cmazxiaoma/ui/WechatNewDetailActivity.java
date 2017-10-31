package com.example.administrator.cmazxiaoma.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;

/**
 * Created by Administrator on 2017/2/20.
 */

public class WechatNewDetailActivity extends BaseActivity {
    private WebView webview;
    private ProgressBar progressbar;
    private  String url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wechat_news_detail);
        initView();
    }

    private void initView() {
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        url=intent.getStringExtra("url");
        UtilsLog.i("title="+title+",url="+url);
        getSupportActionBar().setTitle(title);
        webview= (WebView) findViewById(R.id.wechat__news_detail_webview);
        progressbar= (ProgressBar) findViewById(R.id.wechat_news_detail_progressbar);
        //开启javaScript调用
        webview.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        webview.getSettings().setSupportZoom(true);
        //缩放控制
        webview.getSettings().setBuiltInZoomControls(true);
        //接口回调
        webview.setWebChromeClient(new WebChromeClient());
        //加载网页
        webview.loadUrl(url);
        //本地加载
        webview.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                //接受这个事件，不会扩散.也就是不会跳到浏览器，而是让webview去处理
                return  true;
            }
        });
    }

     //进度条变化的监听
    public class WebChromeClient extends android.webkit.WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(newProgress==100){
                progressbar.setVisibility(View.GONE);
            }
        }
    }
}
