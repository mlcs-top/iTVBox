package com.github.tvbox.osc.ui.activity;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.base.BaseActivity;
import com.github.tvbox.osc.beanry.ExchangeAdapter;
import com.github.tvbox.osc.beanry.ExchangeBean;
import com.github.tvbox.osc.ui.tv.widget.AlwaysMarqueeTextView;
import com.github.tvbox.osc.util.BaseR;
import com.github.tvbox.osc.util.ToolUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;


/**
 * @茶茶QQ205888578
 * @date :2023/9/25
 * 积分商城界面
 */
public class ExchangeActivity extends BaseActivity {

    // private AlwaysMarqueeTextView gongGao;

    private ExchangeAdapter ExchangeAdapter;
    private RecyclerView message_list;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_exchange;
    }

    @Override
    protected void init() {
        // gongGao = findViewById(R.id.wogonggao);
        message_list = findViewById(R.id.tvShopList);
        getNotice();
    }

    private boolean isNotice = false;
    //公告
    private void getNotice() {
        Log.d("tang", "getNotice");
        OkGo.<String>post(ToolUtils.setApi("get_fenAll"))
                .params("t", System.currentTimeMillis() / 1000)
                .params("sign", ToolUtils.setSign("null"))
                .execute(new AbsCallback<String>() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (ToolUtils.iniData(response, mContext)) {
                            ExchangeBean noticeData = new Gson().fromJson(BaseR.decry_R(response.body()), ExchangeBean.class);
                            if (noticeData != null && noticeData.msg.size() > 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ExchangeAdapter = new ExchangeAdapter(noticeData.msg);

                                        // 修改布局为横向布局
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(ExchangeActivity.this);
                                        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                        message_list.setLayoutManager(layoutManager);

                                        message_list.setAdapter(ExchangeAdapter);
                                    }
                                });
                                if (ToolUtils.getIsEmpty(noticeData.msg.get(noticeData.msg.size() - 1).name)) {
                                    if (isNotice)
                                        ToolUtils.HomeDialog(mContext, noticeData.msg.get(noticeData.msg.size() - 1).name);
                                    //ToolUtils.HomeDialog(mContext, noticeData.msg.get(noticeData.msg.size() - 1).name);
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



}