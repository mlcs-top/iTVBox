package com.github.tvbox.osc.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PermissionChecker;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.callback.EmptyCallback;
import com.github.tvbox.osc.callback.LoadingCallback;
import com.github.tvbox.osc.ui.activity.StartActivity;
import com.github.tvbox.osc.util.AppManager;
import com.github.tvbox.osc.util.HawkConfig;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.orhanobut.hawk.Hawk;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Objects;

import me.jessyan.autosize.AutoSizeCompat;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * @author pj567
 * @date :2020/12/17
 * @description:
 */
public abstract class BaseActivity extends AppCompatActivity implements CustomAdapt {
    protected static Context mContext;
    private LoadService mLoadService;

    private static float screenRatio = -100.0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            if (screenRatio < 0) {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int screenWidth = dm.widthPixels;
                int screenHeight = dm.heightPixels;
                screenRatio = (float) Math.max(screenWidth, screenHeight) / (float) Math.min(screenWidth, screenHeight);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        // 设置主题颜色
        if (Hawk.get(HawkConfig.THEME_SELECT, 0) == 0) {
            setTheme(R.style.NetfxTheme);
        } else if (Hawk.get(HawkConfig.THEME_SELECT, 0) == 1) {
            setTheme(R.style.DoraeTheme);
        } else if (Hawk.get(HawkConfig.THEME_SELECT, 0) == 2) {
            setTheme(R.style.PepsiTheme);
        } else if (Hawk.get(HawkConfig.THEME_SELECT, 0) == 3) {
            setTheme(R.style.NarutoTheme);
        } else if (Hawk.get(HawkConfig.THEME_SELECT, 0) == 4) {
            setTheme(R.style.MinionTheme);
        } else if (Hawk.get(HawkConfig.THEME_SELECT, 0) == 5) {
            setTheme(R.style.YagamiTheme);
        } else {
            setTheme(R.style.SakuraTheme);
        }


        //加入检测抓包代码
        if(HawkConfig.zb_vpn.equals("0")) {

        if(isUseProxy()||isUseVpn()){
            Intent intent=new Intent(this, StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        }


        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        mContext = this;
        AppManager.getInstance().addActivity(this);
        init();
    }


    //防抓包
    private Boolean isProxyNet(Context context) {
        String proxyHost = null;
        int proxyPort = -1;
        // 获取host和端口号，并对其进行判定
        proxyHost = System.getProperty("http.proxyHost", null);
        proxyPort = Integer.parseInt(Objects.requireNonNull(System.getProperty("http.proxyPort", "-1")));
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
                   // Log.d("-----", "isVpnUsed() NetworkInterface Name: " + intf.getName());
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


    @Override
    protected void onResume() {
        super.onResume();
        hideSysBar();
        changeWallpaper(false);
    }

    public void hideSysBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
            uiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            uiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
    }

    public void hideSystemUI(boolean shownavbar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            uiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiVisibility |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
            uiVisibility |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            uiVisibility |= View.SYSTEM_UI_FLAG_IMMERSIVE;
            uiVisibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            if (!shownavbar) {
                uiVisibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                uiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            }
            getWindow().getDecorView().setSystemUiVisibility(uiVisibility);
            // set content behind navigation bar
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    @Override
    public Resources getResources() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            AutoSizeCompat.autoConvertDensityOfCustomAdapt(super.getResources(), this);
        }
        return super.getResources();
    }

    public boolean hasPermission(String permission) {
        boolean has = true;
        try {
            has = PermissionChecker.checkSelfPermission(this, permission) == PermissionChecker.PERMISSION_GRANTED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return has;
    }

    protected abstract int getLayoutResID();

    protected abstract void init();

    protected void setLoadSir(View view) {
        if (mLoadService == null) {
            mLoadService = LoadSir.getDefault().register(view, new Callback.OnReloadListener() {
                @Override
                public void onReload(View v) {
                }
            });
        }
    }

    protected void showLoading() {
        if (mLoadService != null) {
            mLoadService.showCallback(LoadingCallback.class);
        }
    }

    protected void showEmpty() {
        if (null != mLoadService) {
            mLoadService.showCallback(EmptyCallback.class);
        }
    }

    protected void showSuccess() {
        if (null != mLoadService) {
            mLoadService.showSuccess();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().finishActivity(this);
    }

    public void jumpActivity(Class<? extends BaseActivity> clazz) {
        Intent intent = new Intent(mContext, clazz);
        startActivity(intent);
    }

    public void jumpActivity(Class<? extends BaseActivity> clazz, Bundle bundle) {
        Intent intent = new Intent(mContext, clazz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected String getAssetText(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assets = getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assets.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public float getSizeInDp() {
        return isBaseOnWidth() ? 1280 : 720;
    }


    public boolean supportsPiPMode() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    @Override
    public boolean isBaseOnWidth() {
        return !(screenRatio >= 4.0f);
    }


    // 获取主题颜色
/*    public void setScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void setScreenOff() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }*/

    // takagen99: Added Theme Color
    public int getThemeColor() {
        TypedArray a = mContext.obtainStyledAttributes(R.styleable.themeColor);
        int themeColor = a.getColor(R.styleable.themeColor_color_theme, 0);
        return themeColor;
    }


    protected static BitmapDrawable globalWp = null;

    public void changeWallpaper(boolean force) {
        if (!force && globalWp != null)
            getWindow().setBackgroundDrawable(globalWp);
        try {
            File wp = new File(getFilesDir().getAbsolutePath() + "/wp");
            if (wp.exists()) {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(wp.getAbsolutePath(), opts);
                // 从Options中获取图片的分辨率
                int imageHeight = opts.outHeight;
                int imageWidth = opts.outWidth;
                int picHeight = 720;
                int picWidth = 1080;
                int scaleX = imageWidth / picWidth;
                int scaleY = imageHeight / picHeight;
                int scale = 1;
                if (scaleX > scaleY && scaleY >= 1) {
                    scale = scaleX;
                }
                if (scaleX < scaleY && scaleX >= 1) {
                    scale = scaleY;
                }
                opts.inJustDecodeBounds = false;
                // 采样率
                opts.inSampleSize = scale;
                globalWp = new BitmapDrawable(BitmapFactory.decodeFile(wp.getAbsolutePath(), opts));
            } else {
                globalWp = null;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            globalWp = null;
        }

        if (globalWp != null) {
            getWindow().setBackgroundDrawable(globalWp);
        } else {
            getWindow().setBackgroundDrawableResource(R.drawable.app_bg);
        }
    }
}