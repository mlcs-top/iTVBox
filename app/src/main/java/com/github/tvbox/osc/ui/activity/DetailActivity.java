package com.github.tvbox.osc.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ClipboardManager;
import android.content.ClipData;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.tvbox.osc.R;
import com.github.tvbox.osc.api.ApiConfig;
import com.github.tvbox.osc.base.BaseActivity;
import com.github.tvbox.osc.bean.AbsXml;
import com.github.tvbox.osc.bean.Movie;
import com.github.tvbox.osc.bean.SourceBean;
import com.github.tvbox.osc.bean.VodInfo;
import com.github.tvbox.osc.beanry.InitBean;
import com.github.tvbox.osc.beanry.NoticeBean;
import com.github.tvbox.osc.cache.RoomDataManger;
import com.github.tvbox.osc.event.RefreshEvent;
import com.github.tvbox.osc.picasso.RoundTransformation;
import com.github.tvbox.osc.ui.adapter.DetailSearchAdapter;
import com.github.tvbox.osc.ui.adapter.SeriesAdapter;
import com.github.tvbox.osc.ui.adapter.SeriesFlagAdapter;
import com.github.tvbox.osc.ui.dialog.DetailDialog;
import com.github.tvbox.osc.ui.dialog.QuickSearchDialog;
import com.github.tvbox.osc.ui.fragment.PlayFragment;
import com.github.tvbox.osc.ui.tv.widget.AlwaysMarqueeTextView;
import com.github.tvbox.osc.util.BaseR;
import com.github.tvbox.osc.util.DefaultConfig;
import com.github.tvbox.osc.util.FastClickCheckUtil;
import com.github.tvbox.osc.util.HawkConfig;
import com.github.tvbox.osc.util.MD5;
import com.github.tvbox.osc.util.MMkvUtils;
import com.github.tvbox.osc.util.MyHandler;
import com.github.tvbox.osc.util.PreferencesUtils;
import com.github.tvbox.osc.util.SharePreferencesUtils;
import com.github.tvbox.osc.util.ToolUtils;
import com.github.tvbox.osc.viewmodel.SourceViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.loading.dialog.AndroidLoadingDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.hawk.Hawk;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7GridLayoutManager;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import me.jessyan.autosize.utils.AutoSizeUtils;

import android.graphics.Paint;

/**
 * @author pj567
 * @date :2020/12/22
 * @description:
 */

public class DetailActivity extends BaseActivity implements MyHandler.IHandler {
    private LinearLayout llLayout;
    private FragmentContainerView llPlayerFragmentContainer;
    private View llPlayerFragmentContainerBlock;
    private View llPlayerPlace;
    private PlayFragment playFragment = null;
    private ImageView ivThumb;
    private TextView tvName;
    private TextView tvYear;
    private TextView tvSite;
    private TextView tvArea;
    private TextView tvLang;
    private TextView tvType;
    private TextView tvActor;
    private TextView tvDirector;
    private TextView tvNote;
    private TextView tvDes;
    private TextView tvPlay;
    private TextView tvLoadNext;
    private TextView tvDate;
    private TextView llSearch;
    private TextView tvUserHome;
    private TextView tv_finishHome;
    private TextView tvSort;
    private TextView tvQuickSearch;
    private TextView tvCollect, tvGoVIP;
    private TvRecyclerView mGridViewFlag;
    private TvRecyclerView mGridView;
    private TvRecyclerView mSeriesGroupView;
    private LinearLayout mEmptyPlayList;
    private LinearLayout mllGongGao;
    private LinearLayout ll_detail;
    private AlwaysMarqueeTextView gongGao;
    private SourceViewModel sourceViewModel;
    private Movie.Video mVideo;
    private VodInfo vodInfo;
    private SeriesFlagAdapter seriesFlagAdapter;
    private SeriesAdapter seriesAdapter;
    public String vodId;
    public String sourceKey;
    boolean seriesSelect = false;
    private View seriesFlagFocus = null;
    private boolean isReverse;
    private String preFlag="";
    private boolean firstReverse;
    private V7GridLayoutManager mGridViewLayoutMgr = null;
    private final Handler mHandler = new Handler();
    private HashMap<String, String> mCheckSources = null;
    private final ArrayList<String> seriesGroupOptions = new ArrayList<>();
    private View currentSeriesGroupView;

    private TvRecyclerView mGridViewSearch;
    private DetailSearchAdapter searchAdapter;

    private MyHandler myHandler;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_detail;
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Date date = new Date();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat timeFormat = new SimpleDateFormat(getString(R.string.hm_date1) + " |" + getString(R.string.hm_date2));
            tvDate.setText(timeFormat.format(date));
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        initView();
        initViewModel();
        initData();
        getNotice();

        myHandler = MyHandler.getHandler(this);
        myHandler.setActivityReference(this);

        mHandler.post(mRunnable);

        //加入检测抓包代码
        if(HawkConfig.zb_vpn.equals("0")) {

            if(isUseProxy()||isUseVpn()){
                Intent intent=new Intent(this, StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

        }

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

    private DetailDialog dialog;
    private String lastVodId = "";

    private void showDialog(String title, String str) {
        dialog = new DetailDialog(mContext, title, str,  new DetailDialog.OnListener() {
            @Override
            public void left() {
                dialog.hide();
            }

            @Override
            public void cancel() {
                dialog.hide();
            }
        });
        if (!dialog.isShowing())
            dialog.show();
    }

    private void initView() {
        llLayout = findViewById(R.id.llLayout);
        llPlayerPlace = findViewById(R.id.previewPlayerPlace);
        llPlayerFragmentContainer = findViewById(R.id.previewPlayer);
        llPlayerFragmentContainerBlock = findViewById(R.id.previewPlayerBlock);
        ivThumb = findViewById(R.id.ivThumb);
        llPlayerPlace.setVisibility(showPreview ? View.VISIBLE : View.GONE);
        ivThumb.setVisibility(!showPreview ? View.VISIBLE : View.GONE);
        tvName = findViewById(R.id.tvName);
        tvYear = findViewById(R.id.tvYear);
        tvSite = findViewById(R.id.tvSite);
        tvArea = findViewById(R.id.tvArea);
        tvLang = findViewById(R.id.tvLang);
        tvType = findViewById(R.id.tvType);
        tvActor = findViewById(R.id.tvActor);
        tvDirector = findViewById(R.id.tvDirector);
        tvDes = findViewById(R.id.tvDes);
        tvPlay = findViewById(R.id.tvPlay);
        tvSort = findViewById(R.id.tvSort);
        tvCollect = findViewById(R.id.tvCollect);
        tvQuickSearch = findViewById(R.id.tvQuickSearch);
        mEmptyPlayList = findViewById(R.id.mEmptyPlaylist);
        mGridView = findViewById(R.id.mGridView);
        gongGao = findViewById(R.id.gonggao);
        mllGongGao = findViewById(R.id.gongGao_root);
        tvDate = findViewById(R.id.tvDate);
        llSearch = findViewById(R.id.llSearch);
        tvUserHome = findViewById(R.id.tvUserHome);
        tv_finishHome = findViewById(R.id.tv_finishHome);
        ll_detail = findViewById(R.id.ll_detail);
        tvNote= findViewById(R.id.tvNote);
        tvGoVIP = findViewById(R.id.tvGoVIP);

        mGridView.setHasFixedSize(true);
        mGridView.setHasFixedSize(false);
        this.mGridViewLayoutMgr = new V7GridLayoutManager(this.mContext, 6);
        mGridView.setLayoutManager(this.mGridViewLayoutMgr);
        seriesAdapter = new SeriesAdapter();
        mGridView.setAdapter(seriesAdapter);
        mGridViewFlag = findViewById(R.id.mGridViewFlag);
        mGridViewFlag.setHasFixedSize(true);
        mGridViewFlag.setLayoutManager(new V7LinearLayoutManager(this.mContext, 0, false));
        seriesFlagAdapter = new SeriesFlagAdapter();
        mGridViewFlag.setAdapter(seriesFlagAdapter);


        mGridViewSearch = findViewById(R.id.mGridSearch);
        mGridViewSearch.setVisibility(View.GONE);
//        mGridViewSearch.setHasFixedSize(true);
//        mGridViewSearch.setLayoutManager(new V7LinearLayoutManager(this.mContext, 0, false));
//        searchAdapter = new DetailSearchAdapter();
//        mGridViewSearch.setAdapter(searchAdapter);

        tvLoadNext = findViewById(R.id.tv_load_next);
        tvLoadNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNext(-1);
            }
        });
        tvLoadNext.setSelected(true);
        isReverse = false;
        firstReverse = false;
        preFlag = "";
        if (showPreview) {
            playFragment = new PlayFragment();

            playFragment.setPlayErrorListener(new PlayFragment.PlayErrorListener() {
                @Override
                public void onError() {
                    loadNextIntercepter();
                }
            });

            getSupportFragmentManager().beginTransaction().add(R.id.previewPlayer, playFragment).commit();
            getSupportFragmentManager().beginTransaction().show(playFragment).commitAllowingStateLoss();
            tvPlay.setText("全屏播放");
        }
        findViewById(R.id.autoChange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isAuto = SharePreferencesUtils.getBoolean(DetailActivity.this, "demo", "isAuto", true);
                if (!isAuto) {
                    ((TextView) findViewById(R.id.autoChange)).setText("已开启");
                } else {
                    ((TextView) findViewById(R.id.autoChange)).setText("自动换源");
                }
                SharePreferencesUtils.saveBoolean(DetailActivity.this, "demo", "isAuto", !isAuto);
            }
        });

        mSeriesGroupView = findViewById(R.id.mSeriesGroupView);
        mSeriesGroupView.setHasFixedSize(true);
        mSeriesGroupView.setLayoutManager(new V7LinearLayoutManager(this.mContext, 0, false));


        llPlayerFragmentContainerBlock.setOnClickListener((view -> toggleFullPreview()));

        tv_finishHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed ();
            }
        });
        tvUserHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpActivity(UserActivity.class);
            }
        });
        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpActivity(SearchActivity.class);
            }
        });
        tvSort.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                if (vodInfo != null && vodInfo.seriesMap.size() > 0) {
                    vodInfo.reverseSort = !vodInfo.reverseSort;
                    isReverse = !isReverse;
                    vodInfo.reverse();
                    vodInfo.playIndex=(vodInfo.seriesMap.get(vodInfo.playFlag).size()-1)-vodInfo.playIndex;
//                    insertVod(sourceKey, vodInfo);
                    firstReverse = true;
                    mSeriesGroupView.setVisibility(View.GONE);
                    seriesAdapter.notifyDataSetChanged();
                }
            }
        });
        tvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastClickCheckUtil.check(v);
                if (showPreview) {
                    toggleFullPreview();
                    if(firstReverse){
                        jumpToPlay();
                        firstReverse=false;
                    }
                } else {
                    jumpToPlay();
                }
            }
        });

        tvQuickSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickQuickSearch();
            }
        });
        tvCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = tvCollect.getText().toString();
                if ("加入收藏".equals(text)) {
                    RoomDataManger.insertVodCollect(sourceKey, vodInfo);
                    Toast.makeText(DetailActivity.this, "已加入收藏夹", Toast.LENGTH_SHORT).show();
                    tvCollect.setText("取消收藏");
                } else {
                    RoomDataManger.deleteVodCollect(sourceKey, vodInfo);
                    Toast.makeText(DetailActivity.this, "已移除收藏夹", Toast.LENGTH_SHORT).show();
                    tvCollect.setText("加入收藏");
                }
            }
        });
        tvGoVIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitBean initBean = MMkvUtils.loadInitBean("");
                if (initBean != null) {
                    if (initBean.msg.pay.state.equals("y")) {
                        jumpActivity(VipActivity.class);
                    } else if (ToolUtils.getIsEmpty(initBean.msg.kamiUrl)) {
                        jumpActivity(VipCardActivity.class);
                    } else {
                        ToolUtils.showToast(mContext, "暂未开放", R.drawable.toast_err);
                    }
                } else {
                    ToolUtils.showToast(mContext, "暂未开放", R.drawable.toast_err);
                }
            }
        });
        ll_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder builder = new StringBuilder();
                builder.append("来源："+(ApiConfig.get().getSource(mVideo.sourceKey).getName())+ "\n");
                builder.append("状态："+mVideo.note + "\n");
                builder.append(mVideo.year == 0 ? "" : String.valueOf("年份："+mVideo.year) + "\n");
                builder.append("地区："+mVideo.area + "\n");
                builder.append("演员："+mVideo.actor + "\n");
                builder.append("导演："+mVideo.director + "\n");
                builder.append(removeHtmlTag("简介："+mVideo.des) + "\n");
                showDialog(mVideo.name, builder.toString());
            }

        });

        mGridView.setOnItemListener(new TvRecyclerView.OnItemListener() {
            @Override
            public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
                seriesSelect = false;
            }

            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                seriesSelect = true;
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {

            }
        });


        mGridViewSearch.setOnItemListener(new TvRecyclerView.OnItemListener() {
            @Override
            public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {

            }

            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {

            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                loadNext(position);
            }
        });
        mGridViewFlag.setOnItemListener(new TvRecyclerView.OnItemListener() {
            private void refresh(View itemView, int position) {
                SeriesFlagAdapter adapter = (SeriesFlagAdapter) mGridViewFlag.getAdapter();
                List<VodInfo.VodSeriesFlag> data = adapter.getData();
                for (int index = 0; index < data.size(); index++) {
                    data.get(index).selected = index == position;
                }
                changeInsideSource(itemView, position, false);
            }

            @Override
            public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
//                seriesSelect = false;
            }

            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                refresh(itemView, position);
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                refresh(itemView, position);
            }
        });
        seriesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FastClickCheckUtil.check(view);
                if (vodInfo != null && vodInfo.seriesMap.get(vodInfo.playFlag).size() > 0) {
                    boolean reload = false;
                    for (int j = 0; j < vodInfo.seriesMap.get(vodInfo.playFlag).size(); j++) {
                        seriesAdapter.getData().get(j).selected = false;
                        seriesAdapter.notifyItemChanged(j);
                    }
                    //解决倒叙不刷新
                    if (vodInfo.playIndex != position) {
                        seriesAdapter.getData().get(position).selected = true;
                        seriesAdapter.notifyItemChanged(position);
                        vodInfo.playIndex = position;

                        reload = true;
                    }
                    //解决当前集不刷新的BUG
                    if (!preFlag.isEmpty() && !vodInfo.playFlag.equals(preFlag)) {
                        reload = true;
                    }

                    seriesAdapter.getData().get(vodInfo.playIndex).selected = true;
                    seriesAdapter.notifyItemChanged(vodInfo.playIndex);
                    //选集全屏 想选集不全屏的注释下面一行
                    if (showPreview && !fullWindows) toggleFullPreview();
                    if (!showPreview || reload) {
                        jumpToPlay();
                        firstReverse=false;
                    }
                }
            }
        });

        mGridView.setOnFocusChangeListener((view, b) -> onGridViewFocusChange(view, b));

        boolean isAuto = SharePreferencesUtils.getBoolean(DetailActivity.this, "demo", "isAuto", true);

        Log.e("DetailActivity", "isAuto : " + isAuto);
        if (!isAuto) {
            ((TextView) findViewById(R.id.autoChange)).setText("自动换源");
        } else {
            ((TextView) findViewById(R.id.autoChange)).setText("已开启");
        }

        setLoadSir(llLayout);
    }


    private void clickQuickSearch(){
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               startQuickSearch();
               QuickSearchDialog quickSearchDialog = new QuickSearchDialog(DetailActivity.this);
               EventBus.getDefault().post(new RefreshEvent(RefreshEvent.TYPE_QUICK_SEARCH, quickSearchData));
               EventBus.getDefault().post(new RefreshEvent(RefreshEvent.TYPE_QUICK_SEARCH_WORD, quickSearchWord));
               quickSearchDialog.show();
               if (pauseRunnable != null && pauseRunnable.size() > 0) {
                   searchExecutorService = Executors.newFixedThreadPool(5);
                   for (Runnable runnable : pauseRunnable) {
                       searchExecutorService.execute(runnable);
                   }
                   pauseRunnable.clear();
                   pauseRunnable = null;
               }
               quickSearchDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                   @Override
                   public void onDismiss(DialogInterface dialog) {
                       try {
                           if (searchExecutorService != null) {
                               pauseRunnable = searchExecutorService.shutdownNow();
                               searchExecutorService = null;
                           }
                       } catch (Throwable th) {
                           th.printStackTrace();
                       }
                   }
               });
           }
       });
    }

    private void changeInsideSource(View itemView, int position, boolean fromChangeSource) {
        Log.e("DetailActivity", "changeInsideSource position : " + position);
        try {
            String newFlag = seriesFlagAdapter.getData().get(position).name;

            ToastUtils.showShort("已切换线路为 : " + newFlag);

            if (vodInfo != null && !vodInfo.playFlag.equals(newFlag)) {
                for (int i = 0; i < vodInfo.seriesFlags.size(); i++) {
                    VodInfo.VodSeriesFlag flag = vodInfo.seriesFlags.get(i);
                    if (flag.name.equals(vodInfo.playFlag)) {
                        flag.selected = false;
                        seriesFlagAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                VodInfo.VodSeriesFlag flag = vodInfo.seriesFlags.get(position);
                flag.selected = true;
                // clean pre flag select status
                if (vodInfo.seriesMap.get(vodInfo.playFlag).size() > vodInfo.playIndex) {
                    vodInfo.seriesMap.get(vodInfo.playFlag).get(vodInfo.playIndex).selected = false;
                }
                vodInfo.playFlag = newFlag;
                seriesFlagAdapter.notifyItemChanged(position);
                refreshList(fromChangeSource);


            } else {
                Log.e("DetailActivity", "no change : " + position);
            }
            seriesFlagFocus = itemView;
        } catch (Exception e) {
           showEmpty();
        }
    }

    private void onGridViewFocusChange(View view, boolean hasFocus) {
        if (llPlayerFragmentContainerBlock.getVisibility() != View.VISIBLE) return;
        llPlayerFragmentContainerBlock.setFocusable(!hasFocus);
    }

    private List<Runnable> pauseRunnable = null;

    private boolean isNotice = false;

    private void getNotice() {
        Log.d("tang", "getNotice");
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
                                    if (isNotice)
                                        ToolUtils.HomeDialog(mContext, noticeData.msg.get(noticeData.msg.size() - 1).content);
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


    //换源
    private boolean isNeedJumpToPlay = false;

    private boolean isChangeFirst = false;

    private ConcurrentHashMap<String, Movie.Video> sourceData = new ConcurrentHashMap<>();

    private  void loadNext(int position) {

//        if (!isLoadForSource) {
//            getAllSource();
//            isLoadForSource = true;
//        } else {
//
//        }

        try {
            if (position == -1) {
                int currentPosition = ((SeriesFlagAdapter) mGridViewFlag.getAdapter()).getCurrentPosition();
                if (currentPosition == ((SeriesFlagAdapter) mGridViewFlag.getAdapter()).getData().size() - 1) {
//                    lastVodId += "," + mVideo.id + mVideo.sourceKey;
//                    boolean isFind = false;
//                    String[] split = lastVodId.split(",");
//
//                    if (sourceData.size() > 0) {
//                        for (int i = 0; i < sourceData.size(); i++) {
//                            Movie.Video video = sourceData.get(i + "");
//                            String id = video.id;
//
//                            String chooseId = "";
//
//                            for (int index = 0; index < split.length - 1; index++) {
//                                if ((split[index]).contains(id) && split[index].contains(video.sourceKey)) {
//                                    chooseId = id;
//                                    break;
//                                }
//                            }
//
//                            if (chooseId.isEmpty() && !video.sourceKey.equals(mVideo.sourceKey)) {
//                                mVideo.id = video.id;
//                                mVideo.sourceKey = video.sourceKey;
//                                isFind = true;
//                                isNeedJumpToPlay = true;
//                                isChangeFirst = true;
//                                isLoadDetailFinish = false;
//
//                                checkDetail = false;
//                                checkDetailStart = false;
//                                getDetailCountDownTimer.cancel();
//
//                                loadDetail(video.id, video.sourceKey, true);
//
//                                break;
//                            }
//                        }
//
//                        try {
//                            if (!isFind) {
//                                if (sourceData.size() > 1) {
//                                    lastVodId = "";
//                                    loadNext(-1);
//                                } else {
//                                    ToastUtils.showShort("暂无其他源数据，请稍后再试");
//                                }
//                            }
//                        } catch (Exception e) {
//
//                        }
//                    } else {
//                        ToastUtils.showShort("暂无源数据");
//                    }

//                    int nextPosition = 0;
////                    if (isChangeFirst) {
////                        isChangeFirst = false;
////                        nextPosition = 0;
////                    }
//                    SeriesFlagAdapter adapter = (SeriesFlagAdapter) mGridViewFlag.getAdapter();
//                    List<VodInfo.VodSeriesFlag> data = adapter.getData();
//                    for (int index = 0; index < data.size(); index++) {
//                        data.get(index).selected = index == nextPosition;
//                    }
//                    adapter.setNewData(data);
//                    adapter.notifyDataSetChanged();
//
//                    try {
//                        seriesFlagAdapter.bindToRecyclerView(mGridViewFlag);
//                    } catch (Exception e) {
//
//                    }
//
//                    View viewByPosition = seriesFlagAdapter.getViewByPosition(nextPosition, R.id.ll_root);
//                    changeInsideSource(viewByPosition, nextPosition, true);



                   clickQuickSearch();


                } else {
                    if (isLoadDetailFinish) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int nextPosition = currentPosition + 1;
                                if (isChangeFirst) {
                                    isChangeFirst = false;
                                    nextPosition = 0;
                                }
                                SeriesFlagAdapter adapter = (SeriesFlagAdapter) mGridViewFlag.getAdapter();
                                List<VodInfo.VodSeriesFlag> data = adapter.getData();
                                for (int index = 0; index < data.size(); index++) {
                                    data.get(index).selected = index == nextPosition;
                                }
                                adapter.setNewData(data);
                                adapter.notifyDataSetChanged();

                                try {
                                    seriesFlagAdapter.bindToRecyclerView(mGridViewFlag);
                                } catch (Exception e) {

                                }

                                View viewByPosition = seriesFlagAdapter.getViewByPosition(nextPosition, R.id.ll_root);
                                changeInsideSource(viewByPosition, nextPosition, true);
                            }
                        });

                    } else {
                        showEmpty();
                        ToastUtils.showShort("视频详情加载失败");
                    }

                }


            } else {
                Movie.Video video = sourceData.get(position + "");
                Log.e("DetailActivity", "loadDetail 1-2");
                mVideo.id = video.id;
                mVideo.sourceKey = video.sourceKey;
                isNeedJumpToPlay = true;
                loadDetail(video.id, video.sourceKey, true);
            }
        } catch (Exception e) {
            Log.e("DetailActivity", "load next error is " + e);
            if (mVideo != null) {
//                if (sourceData.size() > 1) {
//                    lastVodId = "";
//                //    loadNext(-1);
//                }
//               if( ((SeriesFlagAdapter) mGridViewFlag.getAdapter()).getData().size() > 0){
//                   loadNext(-1);
//               }

            } else {
                Log.e("PlayFragment", "showEmpty");
                showEmpty();
            }
        }
    }


    private boolean isFirstJump = false;

    private void jumpToPlay() {
        Log.e("DetailActivity", "jumpToPlay  ");
        if (vodInfo != null && vodInfo.seriesMap.get(vodInfo.playFlag).size() > 0) {

            long jumpDelay = 1000;
            if (!isFirstJump) {
                isFirstJump = true;
                jumpDelay = delayTime;
            }

            Message message = new Message();
            message.what = 2;
            Log.e("DetailActivity", "send  message.what = 2 ");
            myHandler.getHandler().sendMessageDelayed(message, jumpDelay);

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    void refreshList(boolean fromChangeSource) {

        if (vodInfo.seriesMap.get(vodInfo.playFlag).size() <= vodInfo.playIndex) {
            vodInfo.playIndex = 0;
        }
        Log.e("DetailActivity","refreshList fromChangeSource : " + (vodInfo.seriesMap.get(vodInfo.playFlag) != null));
        if (vodInfo.seriesMap.get(vodInfo.playFlag) != null) {
            boolean canSelect = true;
            for (int j = 0; j < vodInfo.seriesMap.get(vodInfo.playFlag).size(); j++) {
                if (vodInfo.seriesMap.get(vodInfo.playFlag).get(j).selected) {
                    canSelect = false;
                    break;
                }
            }
            if (canSelect)
                vodInfo.seriesMap.get(vodInfo.playFlag).get(vodInfo.playIndex).selected = true;
        }

        Paint pFont = new Paint();
        Rect rect = new Rect();

        List<VodInfo.VodSeries> list = vodInfo.seriesMap.get(vodInfo.playFlag);
        int listSize = list.size();
        int w = 1;
        for(int i =0; i < listSize; ++i){
            String name = list.get(i).name;
            pFont.getTextBounds(name, 0, name.length(), rect);
            if(w < rect.width()){
                w = rect.width();
            }
        }
        w += 32;
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth()/3;
        int offset = screenWidth/w;
        if(offset <=2) offset =2;
        if(offset > 6) offset =6;
        mGridViewLayoutMgr.setSpanCount(offset);
        seriesAdapter.setNewData(vodInfo.seriesMap.get(vodInfo.playFlag));

//        setSeriesGroupOptions();
        mSeriesGroupView.setVisibility(View.GONE);

        mGridView.postDelayed(new Runnable() {
            @Override
            public void run() {

                mGridView.smoothScrollToPosition(vodInfo.playIndex);

                try {
                    int playPosition = seriesAdapter.getCurrentPosition();
                    if (vodInfo != null && vodInfo.seriesMap.get(vodInfo.playFlag).size() > 0) {
                        boolean reload = false;
                        for (int j = 0; j < vodInfo.seriesMap.get(vodInfo.playFlag).size(); j++) {
                            seriesAdapter.getData().get(j).selected = false;
                            seriesAdapter.notifyItemChanged(j);
                        }
                        //解决倒叙不刷新
                        if (vodInfo.playIndex != playPosition) {
                            seriesAdapter.getData().get(playPosition).selected = true;
                            seriesAdapter.notifyItemChanged(playPosition);
                            vodInfo.playIndex = playPosition;

                            reload = true;
                        }
                        //解决当前集不刷新的BUG
                        if (!preFlag.isEmpty() && !vodInfo.playFlag.equals(preFlag)) {
                            reload = true;
                        }

                        seriesAdapter.getData().get(vodInfo.playIndex).selected = true;
                        seriesAdapter.notifyItemChanged(vodInfo.playIndex);
                        //选集全屏 想选集不全屏的注释下面一行
//                            if (showPreview && !fullWindows) {
//                                toggleFullPreview();
//                            }
                        if (!showPreview || reload) {
                            jumpToPlay();
                            firstReverse = false;
                        }
                    }
                } catch (Exception e) {

                }

            }
        }, 100);
    }

    private void setTextShow(TextView view, String tag, String info) {
        if (info == null || info.trim().isEmpty()) {
            view.setVisibility(View.GONE);
            return;
        }
        view.setVisibility(View.VISIBLE);
        view.setText(Html.fromHtml(getHtml(tag, info)));
    }

    private String removeHtmlTag(String info) {
        if (info == null)
            return "";
        return info.replaceAll("\\<.*?\\>", "").replaceAll("\\s", "");
    }

    private boolean isLoadForSource = false;

    private boolean isFirstInit = false;
    private Observer<AbsXml> observer = new Observer<AbsXml>() {
        @Override
        public void onChanged(AbsXml absXml) {


            Log.e("DetailActivity", "detail onChanged");
            isLoadDetailFinish = true;
            if (isFirstInit) {
                Message message = new Message();
                message.what = 1;
                myHandler.getHandler().sendMessageDelayed(message, 0);
            } else {
                isFirstInit = true;
            }


            if (absXml != null && absXml.movie != null && absXml.movie.videoList != null && absXml.movie.videoList.size() > 0) {
                Log.e("DetailActivity", "detail have data");
                checkDetail = false;
                showSuccess();
                mVideo = absXml.movie.videoList.get(0);
                vodInfo = new VodInfo();
                vodInfo.setVideo(mVideo);
                vodInfo.sourceKey = mVideo.sourceKey;


                tvName.setText(mVideo.name);

                searchAllTitle = mVideo.name;


                setTextShow(tvNote, "", mVideo.note);
                setTextShow(tvActor, "", "演员：" + mVideo.actor);
                setTextShow(tvDes, "", removeHtmlTag("简介：" + mVideo.des));
                if (!TextUtils.isEmpty(mVideo.pic)) {
                    Picasso.get()
                            .load(DefaultConfig.checkReplaceProxy(mVideo.pic))
                            .transform(new RoundTransformation(MD5.string2MD5(mVideo.pic + mVideo.name))
                                    .centerCorp(true)
                                    .override(AutoSizeUtils.mm2px(mContext, 300), AutoSizeUtils.mm2px(mContext, 400))
                                    .roundRadius(AutoSizeUtils.mm2px(mContext, 10), RoundTransformation.RoundType.ALL))
                            .placeholder(R.drawable.img_loading_placeholder)
                            .error(R.drawable.img_loading_placeholder)
                            .into(ivThumb);
                } else {
                    ivThumb.setImageResource(R.drawable.img_loading_placeholder);
                }


                boolean isAuto = SharePreferencesUtils.getBoolean(DetailActivity.this, "demo", "isAuto", true);
                if (!isAuto) {
                    ((TextView) findViewById(R.id.autoChange)).setText("自动换源");
                } else {
                    ((TextView) findViewById(R.id.autoChange)).setText("已开启");
                }

                if (vodInfo.seriesMap != null && vodInfo.seriesMap.size() > 0) {
                    mGridViewFlag.setVisibility(View.VISIBLE);
                    mGridView.setVisibility(View.VISIBLE);
                    tvPlay.setVisibility(View.VISIBLE);
                    mEmptyPlayList.setVisibility(View.GONE);

                    VodInfo vodInfoRecord = RoomDataManger.getVodInfo(sourceKey, vodId);
                    // 读取历史记录
                    if (vodInfoRecord != null) {
                        vodInfo.playIndex = Math.max(vodInfoRecord.playIndex, 0);
                        vodInfo.playFlag = vodInfoRecord.playFlag;
                        vodInfo.playerCfg = vodInfoRecord.playerCfg;
                        vodInfo.reverseSort = vodInfoRecord.reverseSort;
                    } else {
                        vodInfo.playIndex = 0;
                        vodInfo.playFlag = null;
                        vodInfo.playerCfg = "";
                        vodInfo.reverseSort = false;
                    }

                    if (vodInfo.reverseSort) {
                        vodInfo.reverse();
                    }

                    if (vodInfo.playFlag == null || !vodInfo.seriesMap.containsKey(vodInfo.playFlag))
                        vodInfo.playFlag = (String) vodInfo.seriesMap.keySet().toArray()[0];

                    int flagScrollTo = 0;
                    for (int j = 0; j < vodInfo.seriesFlags.size(); j++) {
                        VodInfo.VodSeriesFlag flag = vodInfo.seriesFlags.get(j);
                        if (flag.name.equals(vodInfo.playFlag)) {
                            flagScrollTo = j;
                            flag.selected = true;
                        } else
                            flag.selected = false;
                    }
                    //设置播放地址
                    seriesFlagAdapter.setNewData(vodInfo.seriesFlags);
                    mGridViewFlag.scrollToPosition(flagScrollTo);

                    refreshList(false);
                    if (showPreview) {
                        Log.e("DetailActivity", "detail have data play");
                        jumpToPlay();
                        llPlayerFragmentContainer.setVisibility(View.VISIBLE);
                        llPlayerFragmentContainerBlock.setVisibility(View.VISIBLE);
                    } else {
                        if (isNeedJumpToPlay) {
                            isNeedJumpToPlay = false;
                            jumpToPlay();
                        }
                    }

                    if(lastFromNext){
                        clickQuickSearch();
                    }

                    Log.e("DetailActivity", "detail have data finish");
                    // startQuickSearch();
                } else {
                    mGridViewFlag.setVisibility(View.GONE);
                    mGridView.setVisibility(View.GONE);
                    mSeriesGroupView.setVisibility(View.GONE);
                    tvPlay.setVisibility(View.GONE);
                    mEmptyPlayList.setVisibility(View.VISIBLE);
                }
            } else {
                //      showEmpty();
                loadNextIntercepter();

                llPlayerFragmentContainer.setVisibility(View.GONE);
                llPlayerFragmentContainerBlock.setVisibility(View.GONE);
            }

        }
    };

    private void initViewModel() {

        sourceViewModel = new ViewModelProvider(this).get(SourceViewModel.class);
        sourceViewModel.detailResult.observe(this, observer);
    }

    private String getHtml(String label, String content) {
        if (content == null) {
            content = "";
        }
        return label + "<font color=\"#FFFFFF\">" + content + "</font>";
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            loadDetail(bundle.getString("id", null), bundle.getString("sourceKey", ""), false);
        }
    }


    private CountDownTimer getDetailCountDownTimer;
    private boolean checkDetailStart = false;
    private boolean checkDetail = false;

    private boolean lastFromNext  = false;

    private void loadDetail(String vid, String key, boolean fromLoadNext) {


        try {
            if (fromLoadNext) {
                try {
                    iosLoadingDialog.show(getFragmentManager(), System.currentTimeMillis() + 1000 + "");
                } catch (Exception e) {

                }

                initViewModel();
            } else {
                showLoading();
            }

            String string = SharePreferencesUtils.getString(this, "demo", "timeoutS", "10");

            checkDetailStart = true;
            int outTime = Integer.parseInt(string) * 1000;
            if (getDetailCountDownTimer != null) {
                checkDetailStart = false;
                getDetailCountDownTimer.onFinish();
            }


            getDetailCountDownTimer = new CountDownTimer(outTime, 1000) {
                @Override
                public void onTick(long l) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        Log.e("DetailActivity", "activity is alive : " + DetailActivity.this.isDestroyed() + " isPause : " + isPause);
                    }
                }

                @Override
                public void onFinish() {
                    Log.e("DetailActivity", "onFinish checkDetailStart : " + checkDetailStart + " checkDetail : " + checkDetail);
                    if (checkDetailStart) {
                        if (checkDetail) {
                            ToastUtils.showShort("视频详情加载失败");
                            Log.e("DetailActivity", "onFinish");
                            loadNextIntercepter();


                            isLoadDetailFinish = false;
                            if (!fromLoadNext) {
                                Log.e("DetailActivity", "showEmpty CountDownTimer");
                                // showEmpty();
                            } else {
                                Message message = new Message();
                                message.what = 1;
                                myHandler.getHandler().sendMessageDelayed(message, 0);
                            }
                        }
                    }
                }
            };
            checkDetail = true;
            checkDetailStart = true;
            getDetailCountDownTimer.start();

            lastFromNext = fromLoadNext;
            if (fromLoadNext) {
                ToastUtils.showShort("已换源为 : " + ApiConfig.get().getSource(key).getName());
            }
        } catch (Exception e) {
            Log.e("DetailActivity", "load Next key error is: " + e);
        }


        if (vid != null) {
            vodId = vid;
            sourceKey = key;
            Log.e("DetailActivity", "sourceKey : " + sourceKey + " vodId : " + vodId);
            sourceViewModel.getDetail(sourceKey, vodId);
            boolean isVodCollect = RoomDataManger.isVodCollect(sourceKey, vodId);
            if (isVodCollect) {
                tvCollect.setText("取消收藏");
            } else {
                tvCollect.setText("加入收藏");
            }
        }
    }

    private ExecutorService searchAllExecutorService = null;
    private AtomicInteger allRunCount = new AtomicInteger(0);
    private String searchAllTitle = "";

    private long delayTime = 2000;

    private boolean isLoadDetailFinish = false;

    private AndroidLoadingDialog iosLoadingDialog;

    private void getAllSource() {
        iosLoadingDialog = new AndroidLoadingDialog().setOnTouchOutside(true);
        iosLoadingDialog.setOnTouchOutside(false);
        iosLoadingDialog.show(getFragmentManager(), "iosLoadingDialog");
        try {
            if (searchAllExecutorService != null) {
                searchAllExecutorService.shutdownNow();
                searchAllExecutorService = null;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        } finally {
            sourceData.clear();
            allRunCount.set(0);
        }
        searchAllExecutorService = Executors.newFixedThreadPool(5);
        List<SourceBean> searchRequestList = new ArrayList<>();
        searchRequestList.addAll(ApiConfig.get().getSourceBeanList());
        SourceBean home = ApiConfig.get().getHomeSourceBean();
        searchRequestList.remove(home);
        searchRequestList.add(0, home);

        ArrayList<String> siteKey = new ArrayList<>();
        for (SourceBean bean : searchRequestList) {
            if (!bean.isSearchable()) {
                continue;
            }
            siteKey.add(bean.getKey());
            allRunCount.incrementAndGet();
        }


        for (String key : siteKey) {
            Message message = new Message();
            message.what = 0;
            message.obj = key;
            myHandler.getHandler().sendMessage(message);
        }


        Log.e("DetailActivity", "siteKey.size() : " + siteKey.size());
        delayTime = siteKey.size() * 50;
        if (delayTime > 10 * 1000) {
            delayTime = 10000;
        }
        Message message = new Message();
        message.what = 199;
        myHandler.getHandler().sendMessageDelayed(message, delayTime);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent event) {
        if (event.type == RefreshEvent.TYPE_REFRESH) {
            if (event.obj != null) {
                if (event.obj instanceof Integer) {
                    int index = (int) event.obj;
                    for (int j = 0; j < vodInfo.seriesMap.get(vodInfo.playFlag).size(); j++) {
                        seriesAdapter.getData().get(j).selected = false;
                        seriesAdapter.notifyItemChanged(j);
                    }
                    seriesAdapter.getData().get(index).selected = true;
                    seriesAdapter.notifyItemChanged(index);
                    mGridView.setSelection(index);
                    vodInfo.playIndex = index;
                    //保存历史
                    insertVod(sourceKey, vodInfo);
                } else if (event.obj instanceof JSONObject) {
                    vodInfo.playerCfg = ((JSONObject) event.obj).toString();
                    //保存历史
                    insertVod(sourceKey, vodInfo);
                }

            }
        } else if (event.type == RefreshEvent.TYPE_QUICK_SEARCH_SELECT) {
            if (event.obj != null) {
                isClickBySearch = true;
                Movie.Video video = (Movie.Video) event.obj;
                loadDetail(video.id, video.sourceKey, false);
            }
        } else if (event.type == RefreshEvent.TYPE_QUICK_SEARCH_WORD_CHANGE) {
            if (event.obj != null) {
                String word = (String) event.obj;
                switchSearchWord(word);
            }
        } else if (event.type == RefreshEvent.TYPE_QUICK_SEARCH_RESULT) {
            try {
                searchData(event.obj == null ? null : (AbsXml) event.obj);
            } catch (Exception e) {
                searchData(null);
            }
        } else if (event.type == RefreshEvent.TYPE_SEARCH_RESULT) {
            try {
                updateSourceData(event.obj == null ? null : (AbsXml) event.obj);
            } catch (Exception e) {
                updateSourceData(null);
            }
        }
    }

    private void updateSourceData(AbsXml absXml) {
        if (absXml != null && absXml.movie != null && absXml.movie.videoList != null && absXml.movie.videoList.size() > 0) {
            for (Movie.Video video : absXml.movie.videoList) {
                if (video.name.equals(mVideo.name)) {
                    String replace = video.sourceKey.trim().replace(" ", "");
                    boolean isExist = checkIsExist(replace);
                    if (!isExist) {
                        if (mVideo.id.equals(video.id) && mVideo.sourceKey.equals(video.sourceKey)) {
                            video.selected = true;
                        }
                        sourceData.put(sourceData.size() + "", video);
                        //  searchAdapter.addData(video);
                        //  searchAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private boolean checkIsExist(String s) {
        boolean isExist = false;
        for (int index = 0; index < sourceData.size(); index++) {
            if ((sourceData.get(index + "").sourceKey.trim().replace(" ", "")).equals(s)) {
                isExist = true;
                break;
            }
        }

        return isExist;
    }

    private String searchTitle = "";
    private boolean hadQuickStart = false;
    private final List<Movie.Video> quickSearchData = new ArrayList<>();
    private final List<String> quickSearchWord = new ArrayList<>();
    private ExecutorService searchExecutorService = null;

    private void switchSearchWord(String word) {
        OkGo.getInstance().cancelTag("quick_search");
        quickSearchData.clear();
        searchTitle = word;
        searchResult();
    }

    private void startQuickSearch() {
        if (hadQuickStart)
            return;
        hadQuickStart = true;
        OkGo.getInstance().cancelTag("quick_search");
        quickSearchWord.clear();
        searchTitle = mVideo.name;
        quickSearchData.clear();
        quickSearchWord.add(searchTitle);
        // 分词
        OkGo.<String>get("http://api.pullword.com/get.php?source=" + URLEncoder.encode(searchTitle) + "&param1=0&param2=0&json=1")
                .tag("fenci")
                .execute(new AbsCallback<String>() {
                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        if (response.body() != null) {
                            return response.body().string();
                        } else {
                            throw new IllegalStateException("网络请求错误");
                        }
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String json = response.body();
                        try {
                            for (JsonElement je : new Gson().fromJson(json, JsonArray.class)) {
                                quickSearchWord.add(je.getAsJsonObject().get("t").getAsString());
                            }
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                        List<String> words = new ArrayList<>(new HashSet<>(quickSearchWord));
                        EventBus.getDefault().post(new RefreshEvent(RefreshEvent.TYPE_QUICK_SEARCH_WORD, words));
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });

        searchResult();
    }

    private void searchResult() {
        try {
            if (searchExecutorService != null) {
                searchExecutorService.shutdownNow();
                searchExecutorService = null;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        searchExecutorService = Executors.newFixedThreadPool(5);
        List<SourceBean> searchRequestList = new ArrayList<>();
        searchRequestList.addAll(ApiConfig.get().getSourceBeanList());
        SourceBean home = ApiConfig.get().getHomeSourceBean();
        searchRequestList.remove(home);
        searchRequestList.add(0, home);

        ArrayList<String> siteKey = new ArrayList<>();
        for (SourceBean bean : searchRequestList) {
            if (!bean.isSearchable() || !bean.isQuickSearch()) {
                continue;
            }
            if (mCheckSources != null && !mCheckSources.containsKey(bean.getKey())) {
                continue;
            }
            siteKey.add(bean.getKey());
        }
        for (String key : siteKey) {
            searchExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    sourceViewModel.getQuickSearch(key, searchTitle);
                }
            });
        }
    }

    private void searchData(AbsXml absXml) {
        if (absXml != null && absXml.movie != null && absXml.movie.videoList != null && absXml.movie.videoList.size() > 0) {
            List<Movie.Video> data = new ArrayList<>();
            for (Movie.Video video : absXml.movie.videoList) {
                if (video.sourceKey.equals(sourceKey) && video.id.equals(vodId))
                    continue;
                data.add(video);
            }
            quickSearchData.addAll(data);
            EventBus.getDefault().post(new RefreshEvent(RefreshEvent.TYPE_QUICK_SEARCH, data));
        }
    }

    private void insertVod(String sourceKey, VodInfo vodInfo) {
        try {
            vodInfo.playNote = vodInfo.seriesMap.get(vodInfo.playFlag).get(vodInfo.playIndex).name;
        } catch (Throwable th) {
            vodInfo.playNote = "";
        }
        PreferencesUtils.putString(this, "last_tv_key", sourceKey);//首页继续观看读取记录
        PreferencesUtils.putString(this, "last_tv_id", vodInfo.id);//首页继续观看读取记录
        RoomDataManger.insertVodRecord(sourceKey, vodInfo);
        EventBus.getDefault().post(new RefreshEvent(RefreshEvent.TYPE_HISTORY_REFRESH));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (searchExecutorService != null) {
                searchExecutorService.shutdownNow();
                searchExecutorService = null;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        playFragment.stopCheckThread();
        myHandler.setActivityReference(null);
        MyHandler.destroyHandler();
        myHandler = null;
        checkDetail = false;
        checkDetailStart = false;
        OkGo.getInstance().cancelTag("fenci");
        OkGo.getInstance().cancelTag("detail");
        OkGo.getInstance().cancelTag("quick_search");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (fullWindows) {
            if (playFragment.onBackPressed())
                return;
            toggleFullPreview();
            mGridView.requestFocus();
            List<VodInfo.VodSeries> list = vodInfo.seriesMap.get(vodInfo.playFlag);
//            mSeriesGroupView.setVisibility(list.size()>GroupCount ? View.VISIBLE : View.GONE);
            return;
        }
        if (seriesSelect) {
            if (seriesFlagFocus != null && !seriesFlagFocus.isFocused()) {
                seriesFlagFocus.requestFocus();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event != null && playFragment != null && fullWindows) {
            if (playFragment.dispatchKeyEvent(event)) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    // preview
    VodInfo previewVodInfo = null;
    boolean showPreview = Hawk.get(HawkConfig.SHOW_PREVIEW, true);
    ; // true 开启 false 关闭
    boolean fullWindows = false;
    ViewGroup.LayoutParams windowsPreview = null;
    ViewGroup.LayoutParams windowsFull = null;

    void toggleFullPreview() {
        if (windowsPreview == null) {
            windowsPreview = llPlayerFragmentContainer.getLayoutParams();
        }
        if (windowsFull == null) {
            windowsFull = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        fullWindows = !fullWindows;
        llPlayerFragmentContainer.setLayoutParams(fullWindows ? windowsFull : windowsPreview);
        llPlayerFragmentContainerBlock.setVisibility(fullWindows ? View.GONE : View.VISIBLE);
        mGridView.setVisibility(fullWindows ? View.GONE : View.VISIBLE);
        mGridViewFlag.setVisibility(fullWindows ? View.GONE : View.VISIBLE);
        mSeriesGroupView.setVisibility(fullWindows ? View.GONE : View.VISIBLE);
        //      mGridViewSearch.setVisibility(fullWindows ? View.GONE : View.VISIBLE);

        //全屏下禁用详情页几个按键的焦点 防止上键跑过来
        tvPlay.setFocusable(!fullWindows);
        tvLoadNext.setFocusable(!fullWindows);
        tvSort.setFocusable(!fullWindows);
        tvCollect.setFocusable(!fullWindows);
        tvQuickSearch.setFocusable(!fullWindows);
        tvGoVIP.setFocusable(!fullWindows);
        ll_detail.setFocusable(!fullWindows);
        tv_finishHome.setFocusable(!fullWindows);
        llSearch.setFocusable(!fullWindows);
        tvUserHome.setFocusable(!fullWindows);
        mGridViewSearch.setFocusable(!fullWindows);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadNextIntercepter();
        }
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case 0: {
                String key = (String) msg.obj;
                searchAllExecutorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        sourceViewModel.getSearch(key, searchAllTitle);
                    }
                });
                break;
            }

            case 1: {
                try {
                    iosLoadingDialog.dismiss();
                } catch (Exception e) {

                }

                break;
            }
            case 199: {
                try {
                    iosLoadingDialog.dismiss();
                    loadNext(-1);
                } catch (Exception e) {

                }

                break;
            }

            case 2: {
                try {
                    preFlag = vodInfo.playFlag;
                    //更新播放地址
                    Bundle bundle = new Bundle();
                    //保存历史
                    insertVod(sourceKey, vodInfo);
                    bundle.putString("sourceKey", sourceKey);
                    bundle.putSerializable("VodInfo", vodInfo);
                    if (showPreview) {
                        if (previewVodInfo == null) {
                            try {
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                ObjectOutputStream oos = new ObjectOutputStream(bos);
                                oos.writeObject(vodInfo);
                                oos.flush();
                                oos.close();
                                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
                                previewVodInfo = (VodInfo) ois.readObject();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (previewVodInfo != null) {
                            previewVodInfo.playerCfg = vodInfo.playerCfg;
                            previewVodInfo.playFlag = vodInfo.playFlag;
                            previewVodInfo.playIndex = vodInfo.playIndex;
                            previewVodInfo.seriesMap = vodInfo.seriesMap;
                            bundle.putSerializable("VodInfo", previewVodInfo);
                        }
                        Log.e("DetailActivity", "playFragment.setDataForInsideChange");
                        playFragment.setDataForInsideChange(bundle);
                    } else {
                        Intent intent = new Intent(mContext, PlayActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 100);
                    }
                } catch (Exception e) {

                }
                break;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
    }

    boolean isPause = false;

    @Override
    protected void onPause() {
        isPause = true;
        if (iosLoadingDialog != null) {
            iosLoadingDialog.dismiss();
        }
        super.onPause();
    }


    private boolean isClickBySearch = false;

    private void loadNextIntercepter(){
//        boolean isAuto = SharePreferencesUtils.getBoolean(DetailActivity.this, "demo", "isAuto", true);
//        if (isAuto && !isClickBySearch) {
//            Log.e("DetailActivity", "loadNext 1- 4");
//            loadNext(-1);
//        }
        loadNext(-1);
    }


}
