package com.github.tvbox.osc.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.tvbox.osc.R;
import com.github.tvbox.osc.api.ApiConfig;
import com.github.tvbox.osc.base.BaseActivity;
import com.github.tvbox.osc.beanry.NoticeBean;
import com.github.tvbox.osc.beanry.ReLevelBean;
import com.github.tvbox.osc.cache.RoomDataManger;
import com.github.tvbox.osc.cache.VodCollect;
import com.github.tvbox.osc.event.RefreshEvent;
import com.github.tvbox.osc.ui.adapter.CollectAdapter;
import com.github.tvbox.osc.ui.tv.widget.AlwaysMarqueeTextView;
import com.github.tvbox.osc.util.BaseR;
import com.github.tvbox.osc.util.FastClickCheckUtil;
import com.github.tvbox.osc.util.HawkConfig;
import com.github.tvbox.osc.util.MMkvUtils;
import com.github.tvbox.osc.util.ToolUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7GridLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
/**
 * @茶茶QQ205888578
 * @date :2023/10/5
 * 我的收藏界面
 */
public class CollectActivity extends BaseActivity {
    private TextView tvDel;
    private TextView tvDelTip;
    private TvRecyclerView mGridView;
    private CollectAdapter collectAdapter;
    private TextView sc_finishHome;//返回
    private TextView scSearch;//搜索
    private TextView scUserHome;//我的
    private LinearLayout mllGongGao;
    private AlwaysMarqueeTextView gongGao;
    private boolean delMode = false;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_collect;
    }

    @Override
    protected void init() {
        initView();
        initData();
    }

    private void toggleDelMode() {
        HawkConfig.hotVodDelete = !HawkConfig.hotVodDelete;
        collectAdapter.notifyDataSetChanged();
        delMode = !delMode;
        tvDelTip.setVisibility(delMode ? View.VISIBLE : View.GONE);
    }

    private void initView() {
        EventBus.getDefault().register(this);
        tvDel = findViewById(R.id.tvDel);
        tvDelTip = findViewById(R.id.tvDelTip);
        mGridView = findViewById(R.id.mGridView);
        mGridView.setHasFixedSize(true);
        mGridView.setLayoutManager(new V7GridLayoutManager(this.mContext, isBaseOnWidth() ? 5 : 6));
        collectAdapter = new CollectAdapter();
        mGridView.setAdapter(collectAdapter);
        sc_finishHome = findViewById(R.id.sc_finishHome);//返回
        scSearch = findViewById(R.id.scSearch);//搜索
        scUserHome = findViewById(R.id.scUserHome);//我的
        gongGao = findViewById(R.id.scgonggao);
        mllGongGao = findViewById(R.id.gongGao_user);

        getNotice();

        sc_finishHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //判断删除按钮如果可见设置不可见
                if (tvDelTip.getVisibility() == View.VISIBLE) {
                    HawkConfig.hotVodDelete = !HawkConfig.hotVodDelete;
                    collectAdapter.notifyDataSetChanged();
                    delMode = !delMode;
                    tvDelTip.setVisibility(View.GONE);
                }

                finish(); // 关闭当前界面
            }
        });
        //搜索
        scSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpActivity(SearchActivity.class);
            }
        });
        //我的
        scUserHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {jumpActivity(UserActivity.class);
            }
        });



        tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDelMode();
            }
        });
        mGridView.setOnInBorderKeyEventListener(new TvRecyclerView.OnInBorderKeyEventListener() {
            @Override
            public boolean onInBorderKeyEvent(int direction, View focused) {
                if (direction == View.FOCUS_UP) {
                    tvDel.setFocusable(true);
                    tvDel.requestFocus();
                }
                return false;
            }
        });
        mGridView.setOnItemListener(new TvRecyclerView.OnItemListener() {
            @Override
            public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
                itemView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300).setInterpolator(new BounceInterpolator()).start();
            }

            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                itemView.animate().scaleX(1.05f).scaleY(1.05f).setDuration(300).setInterpolator(new BounceInterpolator()).start();
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {

            }
        });
        collectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FastClickCheckUtil.check(view);
                VodCollect vodInfo = collectAdapter.getData().get(position);
                if (vodInfo != null) {
                    if (delMode) {
                        collectAdapter.remove(position);
                        RoomDataManger.deleteVodCollect(vodInfo.getId());
                    } else {
                        if (ApiConfig.get().getSource(vodInfo.sourceKey) != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString("id", vodInfo.vodId);
                            bundle.putString("sourceKey", vodInfo.sourceKey);
                            jumpActivity(DetailActivity.class, bundle);
                        } else {
                            //Intent newIntent = new Intent(mContext, SearchActivity.class);
                            //聚合搜索收藏
                            Intent newIntent = new Intent(mContext, FastSearchActivity.class);
                            newIntent.putExtra("title", vodInfo.name);
                            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(newIntent);
                        }
                    }
                }
            }
        });
    }


    private boolean isNotice = false;
    //公告
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



    private void initData() {
        List<VodCollect> allVodRecord = RoomDataManger.getAllVodCollect();
        List<VodCollect> vodInfoList = new ArrayList<>();
        for (VodCollect vodInfo : allVodRecord) {
            vodInfoList.add(vodInfo);
        }
        collectAdapter.setNewData(vodInfoList);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent event) {
        if (event.type == RefreshEvent.TYPE_HISTORY_REFRESH) {
            initData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (delMode) {
            toggleDelMode();
            return;
        }
        super.onBackPressed();
    }

 /*   private boolean KOJAK = true;
    @SuppressLint("HandlerLeak")
    private Handler Screensaver = new Handler(){ //处理消息
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (KOJAK){
                KOJAK = false;
                ReLevelBean LevelBean = MMkvUtils.loadReLevelBean("");
                if (LevelBean != null && LevelBean.msg.size() > 0){
                    jumpActivity(MyBanner.class);
                }
            }
            Screensaver.removeMessages(1);
        }
    };

    @Override
    public void onUserInteraction() { //有操作
        super.onUserInteraction();
        Screensaver.removeMessages(1);
        Screensaver.sendEmptyMessageDelayed(1,60000);
    }

    @Override
    protected void onResume() { //恢复
        super.onResume();
        KOJAK = true;
        Screensaver.sendEmptyMessageDelayed(1,60000);
    }

    @Override
    protected void onPause() { //历来
        super.onPause();
        Screensaver.removeCallbacksAndMessages(null);
    }*/
}