package com.github.tvbox.osc.ui.activity;

import static com.github.tvbox.osc.server.ControlManager.mContext;
import com.github.tvbox.osc.util.DownloadUtil;
import com.github.tvbox.osc.util.PropertiesUtils;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.tvbox.osc.R;
import com.github.tvbox.osc.beanry.InitBean;
import com.github.tvbox.osc.beanry.ReJieXiBean;
import com.github.tvbox.osc.beanry.ReLevelBean;
import com.github.tvbox.osc.beanry.ReUserBean;
import com.github.tvbox.osc.beanry.SiteBean;
import com.github.tvbox.osc.util.BaseR;
import com.github.tvbox.osc.util.HawkConfig;
import com.github.tvbox.osc.util.MMkvUtils;
import com.github.tvbox.osc.util.MacUtils;
import com.github.tvbox.osc.util.ToolUtils;
import com.github.tvbox.osc.services.MyService;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;
import com.tendcloud.tenddata.TalkingDataSDK;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Objects;

public class StartActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private LinearLayout ll_ok_tiao;

    private String Mac;
    private int start_time = 5;
    private boolean isLogin = true;
    private boolean isClosed = false;
    private boolean isCloseds = false;
    private final Handler handler = new Handler();
    private static final String TAG = "StartActivity";

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            setTime(start_time);
            start_time -= 1;
            if (start_time >= 0 && !isClosed) {
                handler.postDelayed(runnable, 1000);
            } else {
                goMain();
            }
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            goMain();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //app统计TalkingData
        TalkingDataSDK.init(this.getApplicationContext(), HawkConfig.Your_app_id, HawkConfig.Your_channel_id, "");
        TalkingDataSDK.setReportUncaughtExceptions(true);

        //加入检测抓包代码
        if(HawkConfig.zb_vpn.equals("0")) {

        if(isUseProxy()||isUseVpn()){
            this.finish();
            return;
        }
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        }
        //加入检测抓包代码结束


        if (HawkConfig.APP_ID.contains("demo")){
            HawkConfig.APP_ID = HawkConfig.APP_ID.replaceAll("demo","");
        }

        if (Hawk.get(HawkConfig.HOME_REC, 8) == 8){ //是否设置过首页数据
            Hawk.put(HawkConfig.HOME_REC, 0); //设置首页默认数据
        }
        Log.d(TAG, "HawkConfig.APP_ID: "+HawkConfig.APP_ID);
        textView = findViewById(R.id.tv_start);
        imageView = findViewById(R.id.iv_image);
        ll_ok_tiao = findViewById(R.id.ll_ok_tiao);

        Mac = MacUtils.getMac(true);
        if (Build.VERSION.SDK_INT >= 26 || Mac == null || Mac.contains("00:00")) {
            Mac = ToolUtils.getAndroidId(StartActivity.this);
        }

        findViewById(R.id.tv_TTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMain();
            }
        });




        DownloadUtil.get().download(HawkConfig.CONFIG_URL, "download", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String path) {
                PropertiesUtils.load(path);
                String url = PropertiesUtils.getProperties("url");
                HawkConfig.MMM_MMM = url;
                Log.d(TAG, "onDownloadSuccess: "+url);
                getSite();
                getJieXi();
                getAppIni();
                getMyBanner();
            }
            @Override
            public void onDownloading(int progress) {
            }
            @Override
            public void onDownloadFailed() {

            }
        });

    }
//零熙QQ：1007713299

    //防抓包
    private Boolean isProxyNet(Context context) {
        String proxyHost = System.getProperty("http.proxyHost");
        int proxyPort = Integer.parseInt(System.getProperty("http.proxyPort", "-1"));

        return proxyHost != null && !proxyHost.isEmpty() && proxyPort != -1;
    }

    private boolean isUseProxy() {
        /**
         * 检测是否正在使用代理，如果在使用返回true,反之返回flase
         */
        if (isProxyNet(this)) {
            Toast.makeText(this, "请关闭代理或者VPN,APP已自动退出", Toast.LENGTH_LONG).show();
            return true;
        } else {
//            Toast.makeText(StartActivity.this, "正常", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    public static boolean isUseVpn() {
        try {
            Enumeration niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null) {
                for (Object f : Collections.list(niList)) {
                    NetworkInterface intf = (NetworkInterface) f;
                    if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    Log.d("-----", "isVpnUsed() NetworkInterface Name: " + intf.getName());
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())) {
                        Toast.makeText(mContext, "检测到Vpn或代理工具，APP已自动退出", Toast.LENGTH_LONG).show();
                        return true; // The VPN is up
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }


//防抓包结束



    private void goMain() {
        start_time = 0;
        isClosed = true;
        handler.removeCallbacks(runnable);
        if (!isCloseds) {
            isCloseds = true;
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
//零熙QQ：1007713299

    private void setTime(int i) {
        if (ll_ok_tiao.getVisibility() == View.GONE) {
            ll_ok_tiao.setVisibility(View.VISIBLE);
        }
        runOnUiThread(() -> {
            if (textView != null) {
                textView.setText(StringUtils.getString(R.string.skip, i));
            }
        });
    }

    private void getSite() { //获取自定义站点
        new Thread(() -> {
            OkGo.<String>post(ToolUtils.setApi("site"))
                    .params("t", System.currentTimeMillis() / 1000)
                    .params("sign", ToolUtils.setSign("null"))
                    .execute(new AbsCallback<String>() {
                        @Override
                        public void onStart(Request<String, ? extends Request> request) {
                            Log.d(TAG, "onStart: " + request.getCacheKey());
                        }

                        @Override
                        public void onSuccess(Response<String> response) {
                            if (ToolUtils.iniData(response, StartActivity.this)) {
                                SiteBean siteDta = new Gson().fromJson(BaseR.decry_R(response.body()), SiteBean.class);
                                MMkvUtils.saveSiteBean(siteDta);
                            }
                        }

                        @Override
                        public String convertResponse(okhttp3.Response response) throws Throwable {
                            assert response.body() != null;
                            return response.body().string();
                        }
                    });
        }).start();
    }

    private void getJieXi() { //获取解析接口
        new Thread(() -> {
            OkGo.<String>post(ToolUtils.setApi("exten"))
                    .params("t", System.currentTimeMillis() / 1000)
                    .params("sign", ToolUtils.setSign("null"))
                    .execute(new AbsCallback<String>() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            if (ToolUtils.iniData(response, StartActivity.this)) {
                                ReJieXiBean reJieXiBean = new Gson().fromJson(BaseR.decry_R(response.body()), ReJieXiBean.class);
                                MMkvUtils.saveReJieXiBean(reJieXiBean);
                            }
                        }

                        @Override
                        public String convertResponse(okhttp3.Response response) throws Throwable {
                            assert response.body() != null;
                            return response.body().string();
                        }
                    });
        }).start();
    }
    //零熙QQ：1007713299
    private void getMyBanner() {
        new Thread(() -> {
            OkGo.<String>post(ToolUtils.setApi("level"))
                    .params("t", System.currentTimeMillis() / 1000)
                    .params("sign", ToolUtils.setSign("null"))
                    .execute(new AbsCallback<String>() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            if (ToolUtils.iniData(response, StartActivity.this)) {
                                ReLevelBean LevelBean = new Gson().fromJson(BaseR.decry_R(response.body()), ReLevelBean.class);
                                MMkvUtils.saveReLevelBean(LevelBean);
                            }
                        }

                        @Override
                        public String convertResponse(okhttp3.Response response) throws Throwable {
                            assert response.body() != null;
                            return response.body().string();
                        }
                    });
        }).start();
    }

    private void getAppIni() { //获取应用配置
        new Thread(() -> {
            OkGo.<String>post(ToolUtils.setApi("ini") + "&pay")
                    .params("t", System.currentTimeMillis() / 1000)
                    .params("sign", ToolUtils.setSign("pay"))
                    .execute(new AbsCallback<String>() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            if (ToolUtils.iniData(response, StartActivity.this)) {
                                InitBean initData = new Gson().fromJson(BaseR.decry_R(response.body()), InitBean.class);
                                if (initData.code == 200) {
                                    String apiJson = initData.msg.appJson;
                                    if (!ToolUtils.getIsEmpty(apiJson) && ToolUtils.getIsEmpty(initData.msg.appJsonb)) {
                                        apiJson = initData.msg.appJsonb;
                                    }
                                    Hawk.put(HawkConfig.JSON_URL, apiJson); //保存聚合接口

                                    String apiJsonc = initData.msg.appJsonc;
                                    Hawk.put(HawkConfig.JSON_URL2, apiJsonc); //保存多线接口

                                    String startupAd = initData.msg.uiStartad;
                                    if (ToolUtils.getIsEmpty(startupAd)) {
                                        setQiDong(startupAd);
                                    }

                                    if (ToolUtils.getIsEmpty(initData.msg.logonWay)) {
                                        switch (initData.msg.logonWay) {
                                            case "0":
                                                if (!MMkvUtils.loadUser().equals("") && !MMkvUtils.loadPasswd().equals("")) {
                                                    reLoginReg(MMkvUtils.loadUser(), MMkvUtils.loadPasswd());
                                                }
                                                break;
                                            case "1":
                                                Log.d(TAG, "卡密登录");
                                                break;
                                            case "2":
                                                if (!MMkvUtils.loadUser().equals("") && !MMkvUtils.loadPasswd().equals("")) {
                                                    reLoginReg(MMkvUtils.loadUser(), MMkvUtils.loadPasswd());
                                                } else {
                                                    reLoginReg(Mac, "12345678");
                                                }
                                                break;
                                        }
                                    }

                                    MMkvUtils.saveInitBean(initData);
                                    handler.postDelayed(runnable, 1000);
                                }
                            }
                        }

                        @Override
                        public void onError(final Response<String> error) {
                            handler.postDelayed(runnable, 1000);
                        }

                        @Override
                        public String convertResponse(okhttp3.Response response) throws Throwable {
                            assert response.body() != null;
                            return response.body().string();
                        }
                    });
        }).start();
    }

    private void reLoginReg(String user, String passwd) { //登录注册
        new Thread(() -> {
            String act;
            if (!isLogin) {
                act = "user_reg";
            } else {
                act = "user_logon";
            }
            Log.d(TAG, "reLoginReg: " + act);
            OkGo.<String>post(ToolUtils.setApi(act))
                    .params("user", user)
                    .params("account", user)
                    .params("password", passwd)
                    .params("markcode", ToolUtils.getAndroidId(StartActivity.this))
                    .params("t", System.currentTimeMillis() / 1000)
                    .params("sign", ToolUtils.setSign("user=" + user + "&account=" + user + "&password=" + passwd + "&markcode=" + ToolUtils.getAndroidId(StartActivity.this)))
                    .execute(new AbsCallback<String>() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                JSONObject jo = new JSONObject(BaseR.decry_R(response.body()));
                                if (jo.getInt("code") == 200) { //成功
                                    if (isLogin) { //登录成功
                                        MMkvUtils.saveUser(user);
                                        MMkvUtils.savePasswd(passwd);
                                        Log.d(TAG, "reLoginReg: " + BaseR.decry_R(response.body()));
                                        ReUserBean userData = new Gson().fromJson(BaseR.decry_R(response.body()), ReUserBean.class);
                                        MMkvUtils.saveReUserBean(userData);
                                    } else { //注册成功
                                        isLogin = true;
                                        reLoginReg(user, passwd);
                                        MMkvUtils.saveReUserBean(null);
                                        Log.d(TAG, "reLoginReg: " + BaseR.decry_R(response.body()));
                                    }
                                } else {
                                    if (isLogin) { //登录失败
                                        if (jo.getInt("code") == 114){ //账号被禁用
                                            Toast.makeText(StartActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                        }else{ //账号或密码错误，去注册
                                            isLogin = false;
                                            reLoginReg(user, passwd);
                                        }
                                    } else { //注册失败
                                        Log.d(TAG, "reLoginReg: " + BaseR.decry_R(response.body()));
                                        Toast.makeText(StartActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public String convertResponse(okhttp3.Response response) throws Throwable {
                            assert response.body() != null;
                            return response.body().string();
                        }
                    });
        }).start();
    }

    private void setQiDong(String imgUrl) {
        imageView.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(imgUrl)
                .centerCrop()
                .override(0, 0) //默认淡入淡出动画
                .transition(DrawableTransitionOptions.withCrossFade()) //缓存策略,跳过内存缓存【此处应该设置为false，否则列表刷新时会闪一下】
                .skipMemoryCache(false) //缓存策略,硬盘缓存-仅仅缓存最终的图像，即降低分辨率后的（或者是转换后的）
                .diskCacheStrategy(DiskCacheStrategy.ALL) //设置图片加载的优先级
                .priority(Priority.HIGH)
                .into(imageView);
    }
}

