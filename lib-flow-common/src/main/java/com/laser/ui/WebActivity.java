package com.laser.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.laser.flowcommon.R;
import com.laser.tools.StatusBarUtil;
import com.laser.tools.UiUtil;

import java.io.File;

/**
 * @author Administrator
 * @description
 * @data 2017/6/14
 */

public class WebActivity extends AppCompatActivity {

    private static final String APP_CACAHE_DIRNAME = "webcache";

    public static final String BUNDLE_URL = "bundle_url";

    private WebView mWebView;

    private String mUrl;

    private String mCurrentUrl;
    /**
     * 是否有加载失败的标记
     */
    private View mLoadingView;
    private View mErroView;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow_activity_web);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);

        Intent intent = getIntent();
        if (intent == null) {
            handleErroUrl();
            return;
        }
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            handleErroUrl();
            return;
        }
        mUrl = bundle.getString(BUNDLE_URL);
        initView();
    }

    private void handleErroUrl() {
        finish();
    }


    protected void initView() {
        initToolbar();
        initErroView();
        initProgressBar();
        mWebView = (WebView) findViewById(R.id.webView);
        mErroView.setVisibility(View.GONE);
        mWebView.setVisibility(View.VISIBLE);
        if (mWebView != null) {
            WebSettings settings = mWebView.getSettings();
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            //setting
            settings.setPluginState(WebSettings.PluginState.ON);
            settings.supportMultipleWindows();  //多窗口
            settings.setJavaScriptEnabled(true);//js交互
            settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
            settings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
            settings.setBuiltInZoomControls(true);//设置支持缩放
            settings.setSupportZoom(true);//支持缩放
            settings.setDatabaseEnabled(true);
            settings.setAllowFileAccess(true);  //设置可以访问文件
            settings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
            settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
            settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
            settings.setLoadsImagesAutomatically(true);  //支持自动加载图片
            //开启缓存
            settings.setDomStorageEnabled(true);
            settings.setAppCacheMaxSize(1024 * 1024 * 8);
            String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath() + File.separator + APP_CACAHE_DIRNAME;
            settings.setAppCachePath(appCachePath);
            settings.setAllowFileAccess(true);
            settings.setAppCacheEnabled(true);
        }
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url) && url.length() > 4) {
                    String httpHead = url.substring(0, 5);
                    if (httpHead.equals("http:") || httpHead.equals("https")) {
                        return false;
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
                mCurrentUrl = url;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
            }


            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (!TextUtils.equals(mCurrentUrl, failingUrl)) {
                    return;
                }
                if (errorCode == WebViewClient.ERROR_CONNECT
                        || errorCode == WebViewClient.ERROR_TIMEOUT
                        || errorCode == WebViewClient.ERROR_HOST_LOOKUP) {
                    showErroView();
                }
            }


            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                int errorCode = 0;
                String failingUrl = "";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    errorCode = error.getErrorCode();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    failingUrl = request.getUrl().toString();
                }
                if (!TextUtils.equals(mCurrentUrl, failingUrl)) {
                    return;
                }
                if (errorCode == WebViewClient.ERROR_CONNECT
                        || errorCode == WebViewClient.ERROR_TIMEOUT
                        || errorCode == WebViewClient.ERROR_HOST_LOOKUP) {
                    showErroView();
                }
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {


            @Override
            public void onReceivedTitle(WebView view, String title) {

            }


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
            }
        });
        mWebView.loadUrl(mUrl);
    }

    private void initProgressBar() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void showErroView() {
        mProgressBar.setProgress(View.GONE);
        mErroView.setVisibility(View.VISIBLE);
        mWebView.setVisibility(View.GONE);
    }

    private void showSuccessView() {
        mErroView.setVisibility(View.GONE);
        mWebView.setVisibility(View.VISIBLE);
    }

    private void initErroView() {
        mErroView = findViewById(R.id.erroView);
        findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSuccessView();

                mWebView.clearView();//删除显示加载错误的View

                mWebView.loadUrl(mCurrentUrl);
            }
        });
    }

    private void initToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.setVisibility(View.GONE);
            long timeout = ViewConfiguration.getZoomControlsTimeout();
            UiUtil.postUiThreadDelayed(new Runnable() {
                @Override
                public void run() {
                    ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
                    if (viewGroup != null) {
                        viewGroup.removeAllViews();
                    }
                    mWebView.destroy();
                }
            }, timeout + 4);
        }
    }
}
