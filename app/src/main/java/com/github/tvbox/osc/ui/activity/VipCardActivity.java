package com.github.tvbox.osc.ui.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.base.BaseActivity;
import com.github.tvbox.osc.beanry.InitBean;
import com.github.tvbox.osc.beanry.ReLevelBean;
import com.github.tvbox.osc.beanry.ReUserBean;
import com.github.tvbox.osc.ui.tv.QRCodeGen;
import com.github.tvbox.osc.util.BaseR;
import com.github.tvbox.osc.util.HawkConfig;
import com.github.tvbox.osc.util.MMkvUtils;
import com.github.tvbox.osc.util.ToolUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class VipCardActivity extends BaseActivity {

    private EditText activity_vip_card_editText;
    private TextView activity_vip_card_send;
    private TextView activity_vip_card_delete;
    private final StringBuilder stringBuilder = new StringBuilder();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_vip_card;
    }

    @Override
    protected void init() {
        initView();
        setListener();
        LinearLayout iv_Cerd = findViewById(R.id.iv_Cerd);
        ImageView iv_CerdUrl = findViewById(R.id.iv_CerdUrl);
        InitBean initBean = MMkvUtils.loadInitBean("");
        if (initBean != null && ToolUtils.getIsEmpty(initBean.msg.kamiUrl)) {
            iv_Cerd.setVisibility(View.VISIBLE);
            iv_CerdUrl.setImageBitmap(QRCodeGen.generateBitmap(initBean.msg.kamiUrl, 200, 200));
        } else {
            iv_Cerd.setVisibility(View.GONE);
        }
    }

    private void initView() {
        activity_vip_card_editText = findViewById(R.id.activity_vip_card_editText);
        activity_vip_card_send = findViewById(R.id.activity_vip_card_send);
        activity_vip_card_delete = findViewById(R.id.activity_vip_card_delete);
    }

    private void setListener() {
        activity_vip_card_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exchangeCard();
            }
        });

        activity_vip_card_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
                readyExchange();
            }
        });
    }

    public void vipNumberClick(View view) {
        stringBuilder.append(view.getTag());
        readyExchange();
    }

    private void readyExchange() {
        String str = stringBuilder.toString();
        activity_vip_card_editText.setText(str);
    }

    private void exchangeCard() {
        final String key = activity_vip_card_editText.getText().toString();
        if (key.isEmpty()) {
            ToolUtils.showToast(mContext, "输入的兑换码有误", R.drawable.toast_err);
        } else {
            ReUserBean userBean = MMkvUtils.loadReUserBean("");
            if (userBean != null && ToolUtils.getIsEmpty(userBean.msg.token)) {
                recHarGe(key, userBean.msg.token);
            } else {
                ToolUtils.showToast(mContext, "TOKEN过期！请重启应用", R.drawable.toast_err);
            }
        }
    }

    private void recHarGe(String key, String token) {
        Log.d("token", "recHarGe: " + token);
        new Thread(() -> {
            OkGo.<String>post(ToolUtils.setApi("card"))
                    .params("token", token)
                    .params("kami", key)
                    .params("t", System.currentTimeMillis() / 1000)
                    .params("sign", ToolUtils.setSign("token=" + token + "&kami=" + key))
                    .execute(new AbsCallback<String>() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                JSONObject jo = new JSONObject(BaseR.decry_R(response.body()));
                                if (jo.getInt("code") == 200) {
                                    ToolUtils.showToast(mContext, jo.getString("msg"), R.drawable.toast_smile);
                                }else{
                                    ToolUtils.showToast(mContext, jo.getString("msg"), R.drawable.toast_err);
                                }
                            } catch (JSONException e) {
                                ToolUtils.showToast(mContext, e.toString(), R.drawable.toast_err);
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Response<String> error) {
                            ToolUtils.showToast(mContext, BaseR.decry_R(error.body()), R.drawable.toast_err);
                        }

                        @Override
                        public String convertResponse(okhttp3.Response response) throws Throwable {
                            assert response.body() != null;
                            return response.body().string();
                        }
                    });
        }).start();
    }

    private String errorString(byte[] error) {
        String string = new String(error);
        String[] strings = string.split(",");
        String[] strings1 = strings[1].split(":");
        String errorString = strings1[1].replace("\"", "");
        return errorString;
    }

  /*  private boolean KOJAK = true;
    @SuppressLint("HandlerLeak")
    private Handler Screensaver = new Handler() { //处理消息
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (KOJAK) {
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
        Screensaver.sendEmptyMessageDelayed(1, 60000);
    }

    @Override
    protected void onResume() { //恢复
        super.onResume();
        KOJAK = true;
        Screensaver.sendEmptyMessageDelayed(1, 60000);
    }

    @Override
    protected void onPause() { //历来
        super.onPause();
        HawkConfig.FORCE_pause = false; //取消强制暂停
        Screensaver.removeCallbacksAndMessages(null);
    }*/
}

