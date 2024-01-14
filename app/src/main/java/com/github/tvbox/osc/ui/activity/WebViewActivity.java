package com.github.tvbox.osc.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebViewActivity extends BaseActivity {
    private static final String TAG = "WebViewActivity";
    private WebView wv;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_web_view;
    }

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void init() {
        wv = findViewById(R.id.wv);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new MyClient());
        wv.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            loadDetail(bundle.getString("newUrl", ""));
        }
    }

    private void loadDetail(String newUrl) {
        if (newUrl != null) {
            wv.loadUrl(newUrl);
        }
    }

    class MyClient extends WebViewClient{
        //监听到页面发生跳转的情况，默认打开web浏览器
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        //页面开始加载
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (url.contains("TRADE_SUCCESS")){
                finish();
            }
        }
        //页面加载完成的回调方法
        @Override
        public void onPageFinished(WebView view, String url) {
//            view.loadUrl("javascript:window.local_obj.showSource('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            super.onPageFinished(view, url);
        }
    }

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
//            Log.e("html源码打印", html);
//            Log.d("IMGbi", "showSource: "+getImageUrl(html));
//            Log.d("支付地址", "showSource: "+getImageSrc(getImageUrl(html)));
        }
    }

    // 获取img标签正则
    private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
    // 获取src路径的正则
    private static final String IMGSRC_REG = "http\"?(.*?)(\"|>|\\s+)";

    /***
     * 获取ImageSrc地址
     * @param listImageUrl
     * @return
     */

    private List<String> getImageSrc(List<String> listImageUrl) {
        List<String> listImgSrc = new ArrayList<String>();
        for (String image : listImageUrl) {
            Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(image);
            while (matcher.find()) {
                listImgSrc.add(matcher.group().substring(0, matcher.group().length() - 1));
            }
        }
        return listImgSrc;
    }

    /***
     * 获取ImageUrl地址
     *
     * @param HTML
     * @return
     */

    private List<String> getImageUrl(String HTML) {
        Matcher matcher = Pattern.compile(IMGURL_REG).matcher(HTML);
        List<String> listImgUrl = new ArrayList<String>();
        while (matcher.find()) {
            listImgUrl.add(matcher.group());
        }
        return listImgUrl;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        //如果用户按的是返回键 并且WebView页面可以返回
        if (keyCode == KeyEvent.KEYCODE_BACK && wv.canGoBack()){
            wv.goBack();
            return true;
        }
        //如果WebView不能返回 则调用默认的onKeyDown方法 退出Activity
        return super.onKeyDown(keyCode, event);
    }
}
