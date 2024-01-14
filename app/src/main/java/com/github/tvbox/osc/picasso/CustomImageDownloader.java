package com.github.tvbox.osc.picasso;

import com.github.tvbox.osc.base.App;
import com.github.tvbox.osc.util.SSL.SSLSocketFactoryCompat;
import com.github.tvbox.osc.util.UA;
import com.squareup.picasso.Downloader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

public class CustomImageDownloader implements Downloader {
    private OkHttpClient client;

    public CustomImageDownloader() {
        // 自定义一个信任所有证书的TrustManager，添加SSLSocketFactory的时候要用到
        final X509TrustManager trustAllCert = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        };
        String pathName = App.getInstance().getApplicationContext().getCacheDir().getAbsolutePath() + "/pic/";
        File file = new File(pathName);
        // 创建一个OkHttpClient实例
        client = new OkHttpClient.Builder()
                .cache(new okhttp3.Cache(file, 100 * 1024 * 1024)) // 设置缓存大小为 100 MB
                .sslSocketFactory(new SSLSocketFactoryCompat(trustAllCert), trustAllCert)
                .hostnameVerifier((hostname, session) -> true)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public Response load(Request request) throws IOException {
        String url = request.url().toString();
        Request.Builder builder = request.newBuilder();
        // 这里参考了 FongMi 的 TV 项目部分代码
        if (url.contains("@Cookie=")) builder.addHeader("Cookie", url.split("@Cookie=")[1].split("@")[0]);
        if (url.contains("@Referer=")) builder.addHeader("Referer", url.split("@Referer=")[1].split("@")[0]);
        if (url.contains("@User-Agent=")) {
            builder.addHeader("User-Agent", url.split("@User-Agent=")[1].split("@")[0]);
        } else {
            String userAgent = UA.getSystemWebviewUserAgent();
            builder.addHeader("User-Agent", userAgent);
        }
        url = url.split("@")[0];
        // 根据请求的URL创建一个新的Request对象
        Request newRequest = builder.url(url).build();
        // 发送请求并获取Response
        Response response = client.newCall(newRequest).execute();
        return response;
    }

    @Override
    public void shutdown() {
        // 关闭OkHttpClient
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
    }
}