package com.javen205.jpay.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.javen205.jpay.R;


public class WebViewActivity extends AppCompatActivity {
    private final  static String KEY_CONTENT="CONTENT";
    private final  static String KEY_ISURL="ISURL";
    private WebView webView;
    private ProgressBar bar;
    private String content;
    private boolean isUrl;

    public static void jumpTo(Activity activity,boolean isUrl,String content) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_ISURL,isUrl);
        bundle.putString(KEY_CONTENT,content);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_web);
        webView = (WebView) findViewById(R.id.web_wv);
        bar=(ProgressBar) findViewById(R.id.web_pb);
        initView();
    }
    private void initView() {
        Bundle extras = null;
        try {
            extras = getIntent().getExtras();
        } catch (Exception e) {
            finish();
            return;
        }
        if (extras == null) {
            finish();
            return;
        }
        try {
            isUrl = extras.getBoolean(KEY_ISURL);
            content = extras.getString(KEY_CONTENT);
        } catch (Exception e) {
            finish();
            return;
        }
        if (TextUtils.isEmpty(content)) {
            // 测试H5支付，必须设置要打开的url网站
            new AlertDialog.Builder(this).setTitle("警告")
                    .setMessage("必须配置content")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    }).show();

        }

        setSettings(webView.getSettings());
        if (isUrl){
            webView.loadUrl(content);
        }else {
            webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
        }

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
                if (!(url.startsWith("http") || url.startsWith("https"))) {
                    return true;
                }

                final PayTask task = new PayTask(WebViewActivity.this);
                final String ex = task.fetchOrderInfoFromH5PayUrl(url);
                if (!TextUtils.isEmpty(ex)) {
                    System.out.println("paytask:::::" + url);
                    new Thread(new Runnable() {
                        public void run() {
                            System.out.println("payTask:::" + ex);
                            final H5PayResultModel result = task.h5Pay(ex, true);
                            if (!TextUtils.isEmpty(result.getReturnUrl())) {
                                WebViewActivity.this.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        view.loadUrl(result.getReturnUrl());
                                    }
                                });
                            }
                        }
                    }).start();
                } else {
                    view.loadUrl(url);
                }
                return true;
            }


        });

        /*webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("WebViewActivity", "访问的url地址：" + url);
                //唤起支付宝
                if(parseScheme(url)){
                    return true;
                }
                view.loadUrl(url);
                return true;
        }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });*/
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
//                toolbar.setTitle(title);
//                setSupportActionBar(toolbar);
//                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    bar.setVisibility(View.GONE);
                } else {
                    if (View.GONE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    private void setSettings(WebSettings setting) {
        setting.setDefaultTextEncodingName("utf-8");
        setting.setJavaScriptEnabled(true);

        setting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        setting.setSupportMultipleWindows(true);
        setting.setJavaScriptEnabled(true);
        setting.setSavePassword(false);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setMinimumFontSize(setting.getMinimumFontSize() + 8);
        setting.setAllowFileAccess(false);
        setting.setTextSize(WebSettings.TextSize.NORMAL);
        webView.setVerticalScrollbarOverlay(true);
    }

    public boolean parseScheme(String url) {
        if (url.contains("platformapi/startApp")) {
            // 如果没有安装支付宝会崩溃掉
            try{
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                startActivity(intent);
            }catch (Exception e){

            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            finish();
        }
    }


    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();//返回上一页面
                return true;
            } else {
                finish();//退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }*/
}
