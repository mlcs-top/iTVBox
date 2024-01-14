package com.github.tvbox.osc.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.tvbox.osc.R;
import com.github.tvbox.osc.base.BaseActivity;
import com.github.tvbox.osc.beanry.InitBean;
import com.github.tvbox.osc.util.BaseR;
import com.github.tvbox.osc.util.HawkConfig;
import com.github.tvbox.osc.util.MMkvUtils;
import com.github.tvbox.osc.util.ToolUtils;

public class ScreensaverActivity extends BaseActivity {

    private String vodTitle;
    private InitBean initBean;
    private int currentItem = 0;
    private ImageView ivScreensaver;
    private TextView ad_name;
    private LinearLayout ok_go_ss;
    private final Handler mHandler = new Handler();
    private final Runnable mRunnable = new Runnable() {
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void run() {
            if (currentItem < initBean.msg.exten.size() - 1) {
                currentItem++;
            } else {
                currentItem = 0;
            }
            addScreensaver(currentItem);
            mHandler.postDelayed(this, 8000);
        }
    };

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_screensaver;
    }

    @Override
    protected void onResume() { //恢复
        super.onResume();
        initBean = MMkvUtils.loadInitBean("");
        mHandler.postDelayed(mRunnable, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void init() {
        ivScreensaver = findViewById(R.id.iv_screensaver);
        ad_name = findViewById(R.id.ad_name);
        ok_go_ss = findViewById(R.id.ok_go_ss);
        findViewById(R.id.lv_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSearch();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            goSearch();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goSearch() {
        if (vodTitle.contains("广告")){
            Toast.makeText(ScreensaverActivity.this, "可按返回键键退出噢~~~", Toast.LENGTH_SHORT).show();
        }else{
            // Intent newIntent = new Intent(mContext, SearchActivity.class);
            //聚合搜索广告
            Intent newIntent = new Intent(mContext, FastSearchActivity.class);
            newIntent.putExtra("title", vodTitle);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(newIntent);
        }
    }

    private void addScreensaver(int i) {
        if (ToolUtils.getIsEmpty(initBean.msg.exten.get(i).name) && ToolUtils.getIsEmpty(initBean.msg.exten.get(i).data)) {
            vodTitle = initBean.msg.exten.get(i).name;
            if (vodTitle.contains("广告")){
                ad_name.setText(vodTitle);
                ok_go_ss.setVisibility(View.GONE);
            }else{
                ad_name.setText("按");
                ok_go_ss.setVisibility(View.VISIBLE);
            }
            Glide.with(mContext)
                    .load(initBean.msg.exten.get(i).data)
                    .centerCrop()
                    .override(0, 0) //默认淡入淡出动画
                    .transition(DrawableTransitionOptions.withCrossFade()) //缓存策略,跳过内存缓存【此处应该设置为false，否则列表刷新时会闪一下】
                    .skipMemoryCache(false) //缓存策略,硬盘缓存-仅仅缓存最终的图像，即降低分辨率后的（或者是转换后的）
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //设置图片加载的优先级
                    .priority(Priority.HIGH)
                    .into(ivScreensaver);
        }
    }
}
