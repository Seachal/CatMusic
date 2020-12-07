package com.cat.music.ui.base;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.cat.music.R;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

public abstract class BaseWebAppActivity extends AppCompatActivity {

    private BridgeWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 去除actionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_web_app);
        initView();
    }

    public abstract String getWebUrl();

    public void initView() {
        mWebView = findViewById(R.id.webView);
        registerHandler(mWebView);
        callHandler(mWebView);
        mWebView.loadUrl(getWebUrl());
    }

    public void callHandler(BridgeWebView webView) {
        webView.callHandler("functionInJs", "android 1发过来的", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {

            }
        });
    }

    public void registerHandler(BridgeWebView webView) {
        webView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i("TAG", "handler = submitFromWeb, data from web = " + data);
                function.onCallBack("submitFromWeb exe, response data from Java");
            }
        });
    }

}