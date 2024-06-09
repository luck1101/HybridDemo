package mhwang.com.hybriddemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.util.HashMap;

public class MainActivity extends Activity implements View.OnClickListener {
    WebView wv_test;
    Button btnBack,btnPojie;

    private static final String JSOBJECT = "MyJS";
    private String curUrl = "https://www.iqiyi.com/";
    private static final String POJIEURL = "https://jx.xmflv.cc/?url=";
    private static final String AGENT = "HuaweiBrowser/12.0.5.302 Mobile Safari/537.36";
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initWebView();
        // 加载网页,若非本地页面,则把下面的加载地址换在页面url
        wv_test.loadUrl(curUrl);
    }

    private void initWebView(){
        // 设置webview属性
        WebSettings settings = wv_test.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        wv_test.setWebChromeClient(new WebChromeClient());
        // 这里需要注意,JSObject里面提供的方法需要添加@JavascriptInterface注释,
        // 否则会报"they will not be visible in API 17"错误
        wv_test.addJavascriptInterface(new JSObject(this), JSOBJECT);
        wv_test.setWebViewClient(new WebViewClient(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String redirectUrl = request.getUrl().toString();
                if (redirectUrl.startsWith("http:") || redirectUrl.startsWith("https:")) {
                    wv_test.loadUrl(redirectUrl);
                    curUrl = redirectUrl;
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

        });
    }

    private void initView(){
        wv_test = findViewById(R.id.wv_test);
        btnBack = findViewById(R.id.btn_back);
        btnPojie = findViewById(R.id.btn_pojie);

        btnPojie.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back){
            wv_test.goBack();
        }else if(view.getId() == R.id.btn_pojie){
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(POJIEURL+curUrl);
            intent.setData(content_url);
            startActivity(intent);

        }
    }
}
