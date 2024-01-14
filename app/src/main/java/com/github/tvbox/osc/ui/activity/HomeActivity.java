package com.github.tvbox.osc.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;//加
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;//加
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;//加
import android.provider.Settings;
import android.util.Log;//加
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;//加
import androidx.core.content.ContextCompat;//加
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.viewpager.widget.ViewPager;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.api.ApiConfig;
import com.github.tvbox.osc.base.BaseActivity;
import com.github.tvbox.osc.base.BaseLazyFragment;
import com.github.tvbox.osc.bean.AbsSortXml;
import com.github.tvbox.osc.bean.MovieSort;
import com.github.tvbox.osc.bean.SourceBean;
import com.github.tvbox.osc.beanry.InitBean;//加
import com.github.tvbox.osc.beanry.NoticeBean;//加
import com.github.tvbox.osc.beanry.ReLevelBean;
import com.github.tvbox.osc.event.RefreshEvent;
import com.github.tvbox.osc.server.ControlManager;
import com.github.tvbox.osc.ui.adapter.HomePageAdapter;
import com.github.tvbox.osc.ui.adapter.SelectDialogAdapter;
import com.github.tvbox.osc.ui.adapter.SortAdapter;
import com.github.tvbox.osc.ui.dialog.SelectDialog;
import com.github.tvbox.osc.ui.dialog.TipDialog;
import com.github.tvbox.osc.ui.fragment.GridFragment;
import com.github.tvbox.osc.ui.fragment.UserFragment;
import com.github.tvbox.osc.ui.tv.widget.AlwaysMarqueeTextView;//加
import com.github.tvbox.osc.ui.tv.widget.DefaultTransformer;
import com.github.tvbox.osc.ui.tv.widget.FixedSpeedScroller;
import com.github.tvbox.osc.ui.tv.widget.NoScrollViewPager;
import com.github.tvbox.osc.ui.tv.widget.ViewObj;
import com.github.tvbox.osc.util.AppManager;
import com.github.tvbox.osc.util.BaseR;//加
import com.github.tvbox.osc.util.DefaultConfig;
import com.github.tvbox.osc.util.FileUtils;
import com.github.tvbox.osc.util.HawkConfig;
import com.github.tvbox.osc.util.LOG;
import com.github.tvbox.osc.viewmodel.SourceViewModel;
import com.github.tvbox.osc.util.MMkvUtils;//加
import com.github.tvbox.osc.util.ToolUtils;//加
import com.github.tvbox.osc.view.HomeDialog;//加
import com.google.gson.Gson;//加
import com.lzy.okgo.OkGo;//加
import com.lzy.okgo.callback.AbsCallback;//加
import com.lzy.okgo.model.Response;//加
import com.orhanobut.hawk.Hawk;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7GridLayoutManager;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class HomeActivity extends BaseActivity {

    private static Resources res;

    private View currentView;
    private LinearLayout topLayout,contentLayout,mllGongGao;
    private AlwaysMarqueeTextView gongGao;//
    private TextView tvName,tvDate,mUpdateMsg;
    private ImageView tvWifi,tvFind,llHistory,tvStyle,tvDraw,tvMenu,tvxianlu;
    private TvRecyclerView mGridView;
    private NoScrollViewPager mViewPager;
    private SourceViewModel sourceViewModel;
    private SortAdapter sortAdapter;
    private HomePageAdapter pageAdapter;
    private final List<BaseLazyFragment> fragments = new ArrayList<>();
    private boolean isDownOrUp = false;
    private boolean sortChange = false;
    private int currentSelected = 0;
    private int sortFocused = 0;
    public View sortFocusView = null;
    private final Handler mHandler = new Handler();
    private long mExitTime = 0;
    private float countSize;    //软件更新总大小
    private float currentSize;    //软件更新当前下载进度

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Date date = new Date();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat timeFormat = new SimpleDateFormat(getString(R.string.hm_date2) + " " + getString(R.string.hm_date1));
            tvDate.setText(timeFormat.format(date));
            mHandler.postDelayed(this, 1000);
        }
    };

    private boolean KOJAK = true;
    @SuppressLint("HandlerLeak")
    private Handler Screensaver = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (KOJAK) {
                KOJAK = false;
                ReLevelBean LevelBean = MMkvUtils.loadReLevelBean("");
                if (LevelBean != null && LevelBean.msg.size() > 0) {
                    jumpActivity(MyBanner.class);
                }
            }
            Screensaver.removeMessages(1);
        }
    };

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
    }//加

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_home;
    }

    boolean useCacheConfig = false;
    private InitBean initData;

    @Override
    protected void init() {

        //加入检测抓包代码
        if(HawkConfig.zb_vpn.equals("0")) {

            if(isUseProxy()||isUseVpn()){
                Intent intent=new Intent(this, StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }
        //加入检测抓包代码结束

        res = getResources();
        EventBus.getDefault().register(this);
        ControlManager.get().startServer();
        initView();
        initViewModel();
        useCacheConfig = false;
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            useCacheConfig = bundle.getBoolean("useCache", false);
        }
        initData=MMkvUtils.loadInitBean("");
        initData();
        getAppVersion();
        getNotice();
    }

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

    public static Resources getRes() {
        return res;
    }

    private void initView() {
        this.gongGao = findViewById(R.id.gonggao);
        this.mllGongGao = findViewById(R.id.gongGao_root);
        this.topLayout = findViewById(R.id.topLayout);
        this.tvName = findViewById(R.id.tvName);
        this.tvWifi = findViewById(R.id.tvWifi);
        this.tvDraw = findViewById(R.id.tvDrawer);
        this.tvxianlu = findViewById(R.id.tvxianlu);
        this.tvFind = findViewById(R.id.tvFind);
        this.llHistory = findViewById(R.id.llHistory);
        this.tvMenu = findViewById(R.id.tvMenu);
        this.tvDate = findViewById(R.id.tvDate);
        this.contentLayout = findViewById(R.id.contentLayout);
        this.mGridView = findViewById(R.id.mGridViewCategory);
        this.mViewPager = findViewById(R.id.mViewPager);
        this.sortAdapter = new SortAdapter();
        this.mGridView.setLayoutManager(new V7LinearLayoutManager(this.mContext, 0, false));
        this.mGridView.setSpacingWithMargins(0, AutoSizeUtils.dp2px(this.mContext, 10.0f));
        this.mGridView.setAdapter(this.sortAdapter);
        this.mGridView.setOnItemListener(new TvRecyclerView.OnItemListener() {
            public void onItemPreSelected(TvRecyclerView tvRecyclerView, View view, int position) {
                if (view != null && !HomeActivity.this.isDownOrUp) {
                    view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(250).start();
                    TextView textView = view.findViewById(R.id.tvTitle);
                    textView.getPaint().setFakeBoldText(false);
                    textView.setTextColor(HomeActivity.this.getResources().getColor(R.color.color_FFFFFF_70));
                    textView.invalidate();
                    view.findViewById(R.id.tvFilter).setVisibility(View.GONE);
                }
            }

            public void onItemSelected(TvRecyclerView tvRecyclerView, View view, int position) {
                if (view != null) {
                    HomeActivity.this.isDownOrUp = false;
                    HomeActivity.this.sortChange = true;
                    view.animate().scaleX(1.1f).scaleY(1.1f).setInterpolator(new BounceInterpolator()).setDuration(300).start();
                    TextView textView = view.findViewById(R.id.tvTitle);
                    textView.getPaint().setFakeBoldText(true);
                    textView.setTextColor(HomeActivity.this.getResources().getColor(R.color.color_FFFFFF));
                    textView.invalidate();
                    if (!sortAdapter.getItem(position).filters.isEmpty())
                        view.findViewById(R.id.tvFilter).setVisibility(View.VISIBLE);
                    HomeActivity.this.sortFocusView = view;
                    HomeActivity.this.sortFocused = position;
                    mHandler.removeCallbacks(mDataRunnable);
                    mHandler.postDelayed(mDataRunnable, 200);
                }
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                if (itemView != null && currentSelected == position) {
                    BaseLazyFragment baseLazyFragment = fragments.get(currentSelected);
                    if ((baseLazyFragment instanceof GridFragment) && !sortAdapter.getItem(position).filters.isEmpty()) {// 弹出筛选
                        ((GridFragment) baseLazyFragment).showFilter();
                    } else if (baseLazyFragment instanceof UserFragment) {
                        showSiteSwitch();
                    }
                }
            }
        });

        // 消息
        mllGongGao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                jumpActivity(MessageActivity.class);
            }
        });

        // 我的应用
        tvDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpActivity(AppsActivity.class);
            }
        });

        // 多线路
        tvxianlu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                jumpActivity(DxianluActivity.class);
            }
        });

        this.mGridView.setOnInBorderKeyEventListener(new TvRecyclerView.OnInBorderKeyEventListener() {
            public boolean onInBorderKeyEvent(int direction, View view) {
                if (direction == View.FOCUS_UP) {
                    BaseLazyFragment baseLazyFragment = fragments.get(sortFocused);
                    if ((baseLazyFragment instanceof GridFragment)) {// 弹出筛选
                        ((GridFragment) baseLazyFragment).forceRefresh();
                    }
                }
                if (direction != View.FOCUS_DOWN) {
                    return false;
                }
                BaseLazyFragment baseLazyFragment = fragments.get(sortFocused);
                if (!(baseLazyFragment instanceof GridFragment)) {
                    return false;
                }
                return !((GridFragment) baseLazyFragment).isLoad();
            }
        });
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File dir = mContext.getCacheDir();
                FileUtils.recursiveDelete(dir);
                Toast.makeText(HomeActivity.this, getString(R.string.hm_cache_del), Toast.LENGTH_SHORT).show();
            }
        });
        tvName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                reloadHome();
                return true;
            }
        });
        tvWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        tvFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpActivity(SearchActivity.class);
            }
        });
        llHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { jumpActivity(HistoryActivity.class);}
        });
        tvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpActivity(UserActivity.class);
            }
        });
        tvMenu.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null)));
                return true;
            }
        });
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));
            }
        });
        setLoadSir(this.contentLayout);
    }

    private void initViewModel() {
        sourceViewModel = new ViewModelProvider(this).get(SourceViewModel.class);
        sourceViewModel.sortResult.observe(this, new Observer<AbsSortXml>() {
            @Override
            public void onChanged(AbsSortXml absXml) {
                showSuccess();
                if (absXml != null && absXml.classes != null && absXml.classes.sortList != null) {// 这里代表是否获取到的值
                    sortAdapter.setNewData(DefaultConfig.adjustSort(ApiConfig.get().getHomeSourceBean().getKey(), absXml.classes.sortList, true));
                } else {
                    sortAdapter.setNewData(DefaultConfig.adjustSort(ApiConfig.get().getHomeSourceBean().getKey(), new ArrayList<>(), true));
                }
                initViewPager(absXml);
            }
        });
    }

    private boolean dataInitOk = false;
    private boolean jarInitOk = false;
    private boolean bApiJson = false;

    boolean HomeShow = Hawk.get(HawkConfig.HOME_SHOW_SOURCE, false);

    boolean isNetworkAvailable() {
        ConnectivityManager cm
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void initData() {

        SourceBean home = ApiConfig.get().getHomeSourceBean();
        if (HomeShow) {
            if (home != null && home.getName() != null && !home.getName().isEmpty())
                tvName.setText(home.getName());
        }

        if (isNetworkAvailable()) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI) {
                tvWifi.setImageDrawable(res.getDrawable(R.drawable.hm_wifi));
            } else if (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE) {
                tvWifi.setImageDrawable(res.getDrawable(R.drawable.hm_mobile));
            } else if (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_ETHERNET) {
                tvWifi.setImageDrawable(res.getDrawable(R.drawable.hm_lan));
            }
        }

        mGridView.requestFocus();

        if (dataInitOk && jarInitOk) {
            showLoading();
            sourceViewModel.getSort(ApiConfig.get().getHomeSourceBean().getKey());
            if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                LOG.e("有");
            } else {
                LOG.e("无");
            }
            return;
        }
        showLoading();
        if (dataInitOk && !jarInitOk) {
            if (!ApiConfig.get().getSpider().isEmpty()) {
                ApiConfig.get().loadJar(useCacheConfig, ApiConfig.get().getSpider(), new ApiConfig.LoadConfigCallback() {
                    @Override
                    public void success() {
                        jarInitOk = true;
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!useCacheConfig)
                                    Toast.makeText(HomeActivity.this, getString(R.string.hm_ok), Toast.LENGTH_SHORT).show();
                                initData();
                            }
                        }, 50);
                    }

                    @Override
                    public void retry() {

                    }

                    @Override
                    public void error(String msg) {
                        jarInitOk = true;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(HomeActivity.this, getString(R.string.hm_notok), Toast.LENGTH_SHORT).show();
                                initData();
                            }
                        });
                    }
                });
            }
            return;
        }
        ApiConfig.get().loadConfig(useCacheConfig, new ApiConfig.LoadConfigCallback() {
            TipDialog dialog = null;

            @Override
            public void retry() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                    }
                });
            }

            @Override
            public void success() {
                dataInitOk = true;
                if (ApiConfig.get().getSpider().isEmpty()) {
                    jarInitOk = true;
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                    }
                }, 50);
            }

            @Override
            public void error(String msg) {
                if (msg.equalsIgnoreCase("-1")) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dataInitOk = true;
                            jarInitOk = true;
                            initData();
                        }
                    });
                    return;
                }

                if (initData != null && ToolUtils.getIsEmpty(initData.msg.appJsonb) && !bApiJson) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Hawk.put(HawkConfig.JSON_URL, initData.msg.appJsonb); //保存远端接口
                            bApiJson = true;
                            initData();
                        }
                    });
                    return;
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog == null)
                            dialog = new TipDialog(HomeActivity.this, msg, getString(R.string.hm_retry), getString(R.string.hm_cancel), new TipDialog.OnListener() {
                                @Override
                                public void left() {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            initData();
                                            dialog.hide();
                                        }
                                    });
                                }

                                @Override
                                public void right() {
                                    dataInitOk = true;
                                    jarInitOk = true;
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            initData();
                                            dialog.hide();
                                        }
                                    });
                                }

                                @Override
                                public void cancel() {
                                    dataInitOk = true;
                                    jarInitOk = true;
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            initData();
                                            dialog.hide();
                                        }
                                    });
                                }
                            });
                        if (!dialog.isShowing())
                            dialog.show();
                    }
                });
            }
        }, this);

    }

    private void initViewPager(AbsSortXml absXml) {
        if (sortAdapter.getData().size() > 0) {
            for (MovieSort.SortData data : sortAdapter.getData()) {
                if (data.id.equals("my0")) {
                    if (Hawk.get(HawkConfig.HOME_REC, 0) == 1 && absXml != null && absXml.videoList != null && absXml.videoList.size() > 0) {
                        fragments.add(UserFragment.newInstance(absXml.videoList));
                    } else {
                        fragments.add(UserFragment.newInstance(null));
                    }
                } else {
                    fragments.add(GridFragment.newInstance(data));
                }
            }
            pageAdapter = new HomePageAdapter(getSupportFragmentManager(), fragments);
            try {
                Field field = ViewPager.class.getDeclaredField("mScroller");
                field.setAccessible(true);
                FixedSpeedScroller scroller = new FixedSpeedScroller(mContext, new AccelerateInterpolator());
                field.set(mViewPager, scroller);
                scroller.setmDuration(300);
            } catch (Exception e) {
            }
            mViewPager.setPageTransformer(true, new DefaultTransformer());
            mViewPager.setAdapter(pageAdapter);
            mViewPager.setCurrentItem(currentSelected, false);
        }
    }

    @Override
    public void onBackPressed() {
        int i;
        if (this.fragments.size() <= 0 || this.sortFocused >= this.fragments.size() || (i = this.sortFocused) < 0) {
            exit();
            return;
        }
        BaseLazyFragment baseLazyFragment = this.fragments.get(i);
        if (baseLazyFragment instanceof GridFragment) {
            View view = this.sortFocusView;
            GridFragment grid = (GridFragment)baseLazyFragment;
            if(grid.restoreView() ){ return; }// 还原上次保存的UI内容
            if (view != null && !view.isFocused()) {
                this.sortFocusView.requestFocus();
            } else if (this.sortFocused != 0) {
                this.mGridView.setSelection(0);
            } else {
                exit();
            }
        } else {
            exit();
        }
    }

    private void exit() {
        if (System.currentTimeMillis() - mExitTime < 2000) {
            super.onBackPressed();
        } else {
            mExitTime = System.currentTimeMillis();
            Toast.makeText(mContext, "再按一次返回键退出应用", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 显示茶茶影视或者数据源名称茶茶QQ205888578
        SourceBean home = ApiConfig.get().getHomeSourceBean();
        if (Hawk.get(HawkConfig.HOME_SHOW_SOURCE, false)) {
            if (home != null && home.getName() != null && !home.getName().isEmpty()) {
                tvName.setText(home.getName());
            }
        } else {
            tvName.setText(R.string.app_name);
        }


        KOJAK = true;
        mHandler.post(mRunnable);
        Screensaver.sendEmptyMessageDelayed(1, 60000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent event) {
        if (event.type == RefreshEvent.TYPE_PUSH_URL) {
            if (ApiConfig.get().getSource("push_agent") != null) {
                Intent newIntent = new Intent(mContext, DetailActivity.class);
                newIntent.putExtra("id", (String) event.obj);
                newIntent.putExtra("sourceKey", "push_agent");
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                HomeActivity.this.startActivity(newIntent);
            }
        }
    }

    private final Runnable mDataRunnable = new Runnable() {
        @Override
        public void run() {
            if (sortChange) {
                sortChange = false;
                if (sortFocused != currentSelected) {
                    currentSelected = sortFocused;
                    mViewPager.setCurrentItem(sortFocused, false);
                    changeTop(sortFocused != 0);
                }
            }
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (topHide < 0)
            return false;
        int keyCode = event.getKeyCode();
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                showSiteSwitch();
            }

        } else if (event.getAction() == KeyEvent.ACTION_UP) {

        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        AppManager.getInstance().appExit(0);
        ControlManager.get().stopServer();
    }
    private void getAppVersion() {
        if (initData != null && ToolUtils.getIsEmpty(initData.msg.appBb)) {
            String dVersion = initData.msg.appBb; //最新版本
            String bVersion = ToolUtils.getVersion(mContext); //本地版本
            bVersion = bVersion.toLowerCase();
            dVersion = dVersion.toLowerCase();
            if (dVersion.compareTo(bVersion) > 0) {
 //               Screensaver.removeMessages(1);
                showUpdateDialog(initData.msg.appNshow, initData.msg.appNurl, 1);
            } else {
                isNotice = true;
            }
        }
    }

    private boolean isNotice = false;

    private void getNotice() {
        Log.d("tang","getNotice");
        OkGo.<String>post(ToolUtils.setApi("notice"))
                .params("t", System.currentTimeMillis() / 1000)
                .params("sign", ToolUtils.setSign("null"))
                .execute(new AbsCallback<String>() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (ToolUtils.iniData(response, mContext)) {
                            NoticeBean noticeData = new Gson().fromJson(BaseR.decry_R(response.body()), NoticeBean.class);
                            if (noticeData != null && noticeData.msg.size() > 0) {
                                if (ToolUtils.getIsEmpty(noticeData.msg.get(noticeData.msg.size() - 1).content)) {
                                    gongGao.setText(noticeData.msg.get(noticeData.msg.size() - 1).content);
                                    gongGao.startScroll();
 //                                   if (isNotice)
   //                                     ToolUtils.HomeDialog(mContext, noticeData.msg.get(noticeData.msg.size() - 1).content);
                                }
                            }
                        }
                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        assert response.body() != null;
                        return response.body().string();
                    }
                });
    }

    @SuppressLint("HandlerLeak")
    private Handler homeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            onMessage(msg);
        }
    };

    /**
     * @brief 窗口消息处理函数。
     * @author joychang
     * @param[in] msg 窗口消息。
     */
    @SuppressLint("WrongConstant")
    private void onMessage(final Message msg) {
        if (msg != null) {
            switch (msg.what) {
                case WindowMessageID.ERROR:
                    Toast.makeText(getApplicationContext(), "服务器内部异常", 1).show();
                    break;
                case WindowMessageID.DOWNLOAD_ERROR:
                    Toast.makeText(getApplicationContext(), "下载失败", 1).show();
                    break;
                case 1001://软件更新  总大小
                    countSize = (Float) msg.obj;
                    break;
                case 1002://软件更新  当前下载大小
                    currentSize = (Float) msg.obj;
                    tvDate.setText("正在下载安装包 " + (int) (currentSize / countSize * 100) + "%");
                    if (currentSize >= countSize) {
                        mHandler.post(mRunnable);
                    }
                    break;
            }
        }
    }

    /**
     * 权限的验证及处理，相关方法
     */
    private void getReadPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//是否请求过该权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 10001);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10001);
                }
            } else { //如果已经获取到了权限则直接进行下一步操作
                goUpdate();
            }
        } else { //小于VERSION_CODES.M直接进行下一步操作
            goUpdate();
        }
    }

    /**
     * 一个或多个权限请求结果回调
     * 当点击了不在询问，但是想要实现某个功能，必须要用到权限，可以提示用户，引导用户去设置
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10001:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //判断是否勾选禁止后不再询问
                        boolean flag = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                        if (flag) {
                            getReadPermissions();
                        } else { // 勾选不再询问，并拒绝
                            Toast.makeText(this, "请到设置中打开权限", Toast.LENGTH_LONG).show();
                        }
                        return;//用户权限是一个一个的请求的，只要有拒绝，剩下的请求就可以停止，再次请求打开权限了
                    }
                }
                goUpdate();
                break;
            default:
                break;
        }
    }

    private void goUpdate() {
        mHandler.removeCallbacksAndMessages(null);
        ToolUtils.showToast(this, "正在后台下载,请稍后", R.drawable.toast_smile);
        ToolUtils.startDownloadApk(mContext, newApkUrl, homeHandler);
    }

    private String newApkUrl;

    /**
     * 显示升级提示的对话框
     */
    private void showUpdateDialog(String text, final String apkUrl, int isRequired) {
        HomeDialog.Builder builder = new HomeDialog.Builder(mContext);
        builder.setTitle("发现新版本");
        String[] remarks = text.split(";");
        String str = "";
        for (int i = 0; i < remarks.length; i++) {
            if (i == remarks.length - 1) {
                str = str + remarks[i];
            } else {
                str = str + remarks[i] + "\n";
            }
        }
        builder.setMessage(str);
        if (isRequired == 1) {
            builder.setPositiveButton("等不及了，立即更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    newApkUrl = apkUrl;
                    dialog.dismiss();
                    getReadPermissions();
                }
            });
        } else {
            builder.setPositiveButton("等不及了，立即更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    newApkUrl = apkUrl;
                    dialog.dismiss();
                    getReadPermissions();
                }
            });
            builder.setNeutralButton("先看片呢，稍后提醒", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Screensaver.removeMessages(1);
                    Screensaver.sendEmptyMessageDelayed(1, 10000);
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }



    /**
     * @author joychang
     * @class WindowMessageID
     * @brief 内部消息ID定义类。
     */
    private class WindowMessageID {
        /**
         * @brief 请求出错。
         */
        public final static int ERROR = 0x00000004;
        // 版本更新的消息
        private final static int DOWNLOAD_ERROR = 0x000000010;
    }


    byte topHide = 0;

    private void changeTop(boolean hide) {
        ViewObj viewObj = new ViewObj(topLayout, (ViewGroup.MarginLayoutParams) topLayout.getLayoutParams());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                topHide = (byte) (hide ? 1 : 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
//        if (hide && topHide == 0) {
//            animatorSet.playTogether(new Animator[]{
//                    ObjectAnimator.ofObject(viewObj, "marginTop", new IntEvaluator(),
//                            new Object[]{
//                                    Integer.valueOf(AutoSizeUtils.mm2px(this.mContext, 10.0f)),
//                                    Integer.valueOf(AutoSizeUtils.mm2px(this.mContext, 0.0f))
//                            }),
//                    ObjectAnimator.ofObject(viewObj, "height", new IntEvaluator(),
//                            new Object[]{
//                                    Integer.valueOf(AutoSizeUtils.mm2px(this.mContext, 50.0f)),
//                                    Integer.valueOf(AutoSizeUtils.mm2px(this.mContext, 1.0f))
//                            }),
//                    ObjectAnimator.ofFloat(this.topLayout, "alpha", new float[]{1.0f, 0.0f})});
//            animatorSet.setDuration(200);
//            animatorSet.start();
//            return;
//        }

        if (!hide && topHide == 1) {
            animatorSet.playTogether(new Animator[]{
                    ObjectAnimator.ofObject(viewObj, "marginTop", new IntEvaluator(),
                            new Object[]{
                                    Integer.valueOf(AutoSizeUtils.mm2px(this.mContext, 0.0f)),
                                    Integer.valueOf(AutoSizeUtils.mm2px(this.mContext, 10.0f))
                            }),
                    ObjectAnimator.ofObject(viewObj, "height", new IntEvaluator(),
                            new Object[]{
                                    Integer.valueOf(AutoSizeUtils.mm2px(this.mContext, 1.0f)),
                                    Integer.valueOf(AutoSizeUtils.mm2px(this.mContext, 50.0f))
                            }),
                    ObjectAnimator.ofFloat(this.topLayout, "alpha", new float[]{0.0f, 1.0f})});
            animatorSet.setDuration(200);
            animatorSet.start();
            return;
        }
    }

    //选择首页数据源多列
    void showSiteSwitch() {
        List<SourceBean> sites = new ArrayList<>();
        //List<SourceBean>bean=ApiConfig.get().getSourceBeanList();

        for (SourceBean sb : ApiConfig.get().getSourceBeanList()) {
            Log.e("beanList","bean key "+sb.getKey()+" name :"+sb.getName());
            if (sb.getHide() == 0) sites.add(sb);
        }
        if (sites.size() > 0) {
            SelectDialog<SourceBean> dialog = new SelectDialog<>(HomeActivity.this);

            // Multi Column Selection
            int spanCount = (int) Math.floor(sites.size() / 10);
            if (spanCount <= 1) spanCount = 1;
            if (spanCount >= 3) spanCount = 3;

            TvRecyclerView tvRecyclerView = dialog.findViewById(R.id.list);
            tvRecyclerView.setLayoutManager(new V7GridLayoutManager(dialog.getContext(), spanCount));
            LinearLayout cl_root = dialog.findViewById(R.id.cl_root);
            ViewGroup.LayoutParams clp = cl_root.getLayoutParams();
            if (spanCount != 1) {
                clp.width = AutoSizeUtils.mm2px(dialog.getContext(), 400 + 260 * (spanCount - 1));
            }

            dialog.setTip(getString(R.string.dia_source));
            dialog.setAdapter(new SelectDialogAdapter.SelectDialogInterface<SourceBean>() {
                @Override
                public void click(SourceBean value, int pos) {
                    Log.e("beanClick","click value : "+value.getName());
                    ApiConfig.get().setSourceBean(value);
                    reloadHome();
                    //强制清空
                  //  ApiConfig.get().cleanSourceBeanList();
                }

                @Override
                public String getDisplay(SourceBean val) {
                    return val.getName();
                }
            }, new DiffUtil.ItemCallback<SourceBean>() {
                @Override
                public boolean areItemsTheSame(@NonNull @NotNull SourceBean oldItem, @NonNull @NotNull SourceBean newItem) {
                    return oldItem == newItem;
                }

                @Override
                public boolean areContentsTheSame(@NonNull @NotNull SourceBean oldItem, @NonNull @NotNull SourceBean newItem) {
                    return oldItem.getKey().equals(newItem.getKey());
                }
            }, sites, sites.indexOf(ApiConfig.get().getHomeSourceBean()));
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
            dialog.show();
        }
    }

    //首页重新加载
    void reloadHome() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle bundle = new Bundle();
        bundle.putBoolean("useCache", true);
        intent.putExtras(bundle);
        HomeActivity.this.startActivity(intent);
    }

}