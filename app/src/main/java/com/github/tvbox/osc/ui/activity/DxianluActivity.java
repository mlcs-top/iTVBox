package com.github.tvbox.osc.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.base.BaseActivity;
import com.github.tvbox.osc.beanry.DxianluAdapter;
import com.github.tvbox.osc.beanry.DxianluBean;
import com.github.tvbox.osc.cache.CacheData;
import com.github.tvbox.osc.ui.tv.widget.AlwaysMarqueeTextView;
import com.github.tvbox.osc.util.BaseR;
import com.github.tvbox.osc.util.FastClickCheckUtil;
import com.github.tvbox.osc.util.ToolUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.github.tvbox.osc.util.HawkConfig;
import com.orhanobut.hawk.Hawk;


/**
 * @茶茶QQ205888578
 * @date :2023/9/25
 * 我的消息界面
 */
public class DxianluActivity extends BaseActivity {


    // private AlwaysMarqueeTextView gongGao;

    private DxianluAdapter DxianluAdapter;
    private RecyclerView message_list;

    private TextView moren;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_dxianlu;
    }

    @Override
    protected void init() {


        // gongGao = findViewById(R.id.wogonggao);
        message_list = findViewById(R.id.dxianlu_list);
        moren = findViewById(R.id.tv_resetting);

        //默认点击事件
        moren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Hawk.put(HawkConfig.API_URL2,"");

                CacheData.CacheString("sourceUrl","");//存储线路URL
                CacheData.CacheString("sourceName","");//存储线路名称

                // 重启应用
                Intent intent = v.getContext().getPackageManager().getLaunchIntentForPackage(v.getContext().getPackageName());
                if (intent != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                    // 销毁当前的Activity
                    ((DxianluActivity) v.getContext()).finish();
                    // 杀死当前进程
                    System.exit(0);
                }


            }
        });


        getDxianlu();

    }



    private boolean isNotice = false;
    //公告
    private void getDxianlu() {
        Log.d("tang", "getdxianlu");
        Log.d("tang", "HawkConfig.JSON_URL2");
        OkGo.<String>get(Hawk.get(HawkConfig.JSON_URL2))
                .execute(new AbsCallback<String>() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (ToolUtils.iniData2(response, mContext)) {
                            String decryptedResponse = BaseR.decry_R2(response.body());
                            DxianluBean noticeData = new Gson().fromJson(decryptedResponse, DxianluBean.class);
                            if (noticeData != null && noticeData.storeHouse.size() > 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DxianluAdapter dxianluAdapter = new DxianluAdapter(noticeData.storeHouse);
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(DxianluActivity.this);
                                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        message_list.setLayoutManager(layoutManager);
                                        message_list.setAdapter(dxianluAdapter);
                                    }
                                });

                                String lastSourceName = noticeData.storeHouse.get(noticeData.storeHouse.size() - 1).sourceName;
                                if (ToolUtils.getIsEmpty(lastSourceName) && isNotice) {
                                    ToolUtils.HomeDialog(mContext, lastSourceName);
                                }
                            }
                        }
                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        assert response.body() != null;
                        return response.body().string();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        // 请求失败的处理逻辑
                        // 可以在这里进行提示或其他操作
                        super.onError(response);
                    }
                });
    }




}