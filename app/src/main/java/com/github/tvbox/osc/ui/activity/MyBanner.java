package com.github.tvbox.osc.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.tvbox.osc.R;
import com.github.tvbox.osc.base.BaseActivity;
import com.github.tvbox.osc.beanry.ReLevelBean;
import com.github.tvbox.osc.util.MMkvUtils;
import com.github.tvbox.osc.util.ToolUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

/**
 *
 * com.youth.banner这个依赖好像没适配TV，只监听点击事件，TV需要的是实时反馈索引。先不研究他了
 *
 * **/
public class MyBanner extends BaseActivity implements OnBannerListener {

    private static final String TAG = "MyBanner";
    private Banner banner;
    private TextView BannerName;
    private ReLevelBean LevelBean;
    private LinearLayout ok_go_ss;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    private ProgressBar progressBar;
    private String vodTitle;
    private Integer searchable;
    private int currentItem = 0;
    private final int delayTime = 8000;
    private final Handler mHandler = new Handler();
    private final Runnable mRunnable = new Runnable() {
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void run() {
            if (LevelBean.msg.size() > 0) {
                bb = false;
                startProgressBar(100);
                vodTitle = LevelBean.msg.get(currentItem).name;
                searchable = LevelBean.msg.get(currentItem).searchable;
                if (searchable == 1) {
                    BannerName.setText("按");
                    ok_go_ss.setVisibility(View.VISIBLE);
                } else {
                    BannerName.setText(vodTitle);
                    ok_go_ss.setVisibility(View.GONE);
                }
                if (currentItem < LevelBean.msg.size() - 1) {
                    currentItem++;
                } else {
                    currentItem = 0;
                }
                mHandler.postDelayed(this, delayTime);
            }
        }
    };

    private boolean bb = false;

    public void startProgressBar(int b) {
        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 100; i >= 0; i--) {
                    if (bb) break;
                    setProgressBar(i);
                    try {
                        Thread.sleep(75);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(mRunnable);
        thread.start();
    }

    public void setProgressBar(int progress) {
        progressBar.setProgress(progress);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_banner;
    }

    @Override
    protected void init() {
        initView();
    }

    @Override
    protected void onResume() { //恢复
        super.onResume();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bb = true;
        currentItem = 0;
        setProgressBar(100);
        banner.stopAutoPlay();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bb = true;
        currentItem = 0;
        setProgressBar(100);
        banner.stopAutoPlay();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        bb = true;
        currentItem = 0;
        setProgressBar(100);
        banner.stopAutoPlay();
        mHandler.removeCallbacksAndMessages(null);
    }

    private void initView() {
        banner = (Banner) findViewById(R.id.banner);
        ok_go_ss = findViewById(R.id.ok_go_ss);
        BannerName = (TextView) findViewById(R.id.ad_name);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        list_path = new ArrayList<>();
        list_title = new ArrayList<>();
        LevelBean = MMkvUtils.loadReLevelBean("");
        if (LevelBean != null && LevelBean.msg.size() > 0) {
            for (int i = 0; i < LevelBean.msg.size(); i++) {
                if (ToolUtils.getIsEmpty(LevelBean.msg.get(i).extend)) {
                    list_path.add(LevelBean.msg.get(i).extend);
                    Log.d(TAG, "initView: " + LevelBean.msg.get(i).extend);
                    list_title.add(LevelBean.msg.get(i).name);
                }
            }
        }

        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR)
                //设置图片加载器，图片加载器在下方
                .setImageLoader(new MyLoader())
                //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
                .setBannerAnimation(Transformer.Accordion)
                //设置轮播图的标题集合
                .setBannerTitles(list_title)
                //设置图片网址或地址的集合
                .setImages(list_path)
                //设置轮播间隔时间
                .setDelayTime(delayTime)
                //设置是否为自动轮播，默认是“是”。
                .isAutoPlay(true)
                //设置指示器的位置，小点点，左中右。
                .setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(MyBanner.this)
                //必须最后调用的方法，启动轮播图。
                .start();
        mHandler.postDelayed(mRunnable, 1);
    }

    //按下OK键时获取当前幻灯片位置
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            goSearch();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goSearch() {
        if (searchable != 1) {
            finish();
        } else {
            //Intent newIntent = new Intent(mContext, SearchActivity.class);
            //聚合搜索不知道哪里
            Intent newIntent = new Intent(mContext, FastSearchActivity.class);
            newIntent.putExtra("title", vodTitle);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(newIntent);
        }
    }

    @Override
    public void OnBannerClick(int position) {
        goSearch();
        Log.d(TAG, "OnBannerClick: " + position);
    }

    //自定义的图片加载器
    private static class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Log.d(TAG, "OnBannerClick: ");
            Glide.with(context).load((String) path).into(imageView);
        }
    }
}