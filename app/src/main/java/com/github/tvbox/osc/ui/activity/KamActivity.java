package com.github.tvbox.osc.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.tvbox.osc.R;
import com.github.tvbox.osc.base.BaseActivity;
import com.github.tvbox.osc.bean.VipGoodsData;
import com.github.tvbox.osc.beanry.GoodsBean;
import com.github.tvbox.osc.beanry.InitBean;
import com.github.tvbox.osc.beanry.KamInfoBean;
import com.github.tvbox.osc.beanry.ReUserBean;
import com.github.tvbox.osc.beanry.UserInfoBean;
import com.github.tvbox.osc.ui.dialog.HuoDongDialog;
import com.github.tvbox.osc.ui.dialog.WeiXinDialog;
import com.github.tvbox.osc.ui.tv.QRCodeGen;
import com.github.tvbox.osc.util.BaseR;
import com.github.tvbox.osc.util.HawkConfig;
import com.github.tvbox.osc.util.MMkvUtils;
import com.github.tvbox.osc.util.ToolUtils;
import com.github.tvbox.osc.util.WiFiDialog;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


public class KamActivity extends BaseActivity implements VipGoodsAdapter.OnClickListener {

    private static final String TAG = "VipActivity";
    private TextView activity_vip_user_name, activity_vip_user_time, activity_vip_qrcode_text, tv_payType, activity_tv_user_price, activity_vip_pay;
    private LinearLayout ll_payType_aliB, activity_vip_jump_card;
    private ImageView activity_vip_icon, activity_vip_avatar;
    private ImageView activity_vip_qrcode_img;
    private RecyclerView activity_vip_list;
    private ArrayList<VipGoodsData> vipGoodsDataList = new ArrayList<>();
    private VipGoodsAdapter vipGoodsAdapter;
    private TextView ll_payType_tt, ll_payType_ttt;

    private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View itemView, boolean hasFocus) {
            if (null != itemView && itemView != activity_vip_list) {
                itemView.setSelected(hasFocus);
                Log.d(TAG, "onFocusChange: 来了老弟");
            }
        }
    };

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_vip;
    }

    @Override
    protected void init() {
        initView();
        initData();
        initClick();
    }

    private InitBean initBean;
    @SuppressLint("UseCompatLoadingForDrawables")
    private void initView() {
        activity_vip_avatar = findViewById(R.id.activity_vip_avatar);
        ll_payType_aliB = findViewById(R.id.ll_payType_aliB);
        activity_vip_jump_card = findViewById(R.id.activity_vip_jump_card);
        activity_vip_user_name = findViewById(R.id.activity_vip_user_name);
        activity_vip_user_time = findViewById(R.id.activity_vip_user_time);
        activity_vip_qrcode_text = findViewById(R.id.activity_vip_qrcode_text);
        activity_vip_qrcode_img = findViewById(R.id.activity_vip_qrcode_img);
        activity_tv_user_price = findViewById(R.id.activity_tv_user_price);
        activity_vip_icon = findViewById(R.id.activity_vip_icon);
        activity_vip_pay = findViewById(R.id.activity_vip_pay);
        ll_payType_tt = findViewById(R.id.ll_payType_tt);
        ll_payType_ttt = findViewById(R.id.ll_payType_ttt);
        tv_payType = findViewById(R.id.tv_payType);
        activity_vip_list = findViewById(R.id.activity_vip_list);
        activity_vip_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activity_vip_list.addItemDecoration(new SpaceItemDecoration(10));
        initBean = MMkvUtils.loadInitBean("");
        if (initBean != null && ToolUtils.getIsEmpty(initBean.msg.uiKefu)) {
            activity_vip_qrcode_img.setImageBitmap(QRCodeGen.generateBitmap(initBean.msg.uiKefu, 300, 300));
        } else {
            activity_vip_qrcode_img.setImageDrawable(mContext.getApplicationContext().getResources().getDrawable(R.drawable.app_icon));
        }
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        String uName = "00:00:00:00:00:00";
        String uTime = "您还不是影视会员，马上开通会员吧";
        userBean = MMkvUtils.loadReUserBean("");
        activity_vip_icon.setBackgroundResource(R.drawable.channel_vip_ic_1);
        if (userBean != null && ToolUtils.getIsEmpty(userBean.msg.token)) {
            getUserInfo(userBean.msg.token);
            findViewById(R.id.ll_user_login).setVisibility(View.GONE);
            findViewById(R.id.ll_vip_user).setVisibility(View.VISIBLE);
            findViewById(R.id.iv_anim_loading).setVisibility(View.VISIBLE);
            if (ToolUtils.getIsEmpty(MMkvUtils.loadUser())) {
                uName = MMkvUtils.loadUser();
            }else{
                if (ToolUtils.getIsEmpty(userBean.msg.info.name))
                    uName = userBean.msg.info.name;
            }
            if (ToolUtils.getIsEmpty(ToolUtils.stampToDate(userBean.msg.info.vip))) {
                long curRentTimes = System.currentTimeMillis() / 1000;
                int userEndTimes = userBean.msg.info.vip; //会员到期时间
                if (userEndTimes > curRentTimes) {
                    uTime = ToolUtils.stampToDate(userEndTimes);
                    activity_vip_icon.setBackgroundResource(R.drawable.channel_vip_ic_2);
                }
            }

            if (!uName.isEmpty()) {
                activity_vip_user_name.setText(uName);
            }

            if (!uTime.isEmpty()) {
                activity_vip_user_time.setText(uTime);
            }

            Glide.with(this)
                    .load(userBean.msg.info.pic)
                    .error(R.drawable.channel_user_avatar_default)
                    .centerCrop()
                    .override(0, 0) //默认淡入淡出动画
                    .transition(DrawableTransitionOptions.withCrossFade()) //缓存策略,跳过内存缓存【此处应该设置为false，否则列表刷新时会闪一下】
                    .skipMemoryCache(false) //缓存策略,硬盘缓存-仅仅缓存最终的图像，即降低分辨率后的（或者是转换后的）
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //设置图片加载的优先级
                    .priority(Priority.HIGH)
                    .into(activity_vip_avatar);
        } else {
            findViewById(R.id.ll_vip_user).setVisibility(View.GONE);
            findViewById(R.id.iv_anim_loading).setVisibility(View.GONE);
        }

        activity_tv_user_price.setText("选中列表按(OK)键");
        activity_vip_pay.setText("套餐购买");
        ll_payType_tt.setText("往期活动");
        ll_payType_ttt.setText("最新活动");
    }

    /**
     * 显示用户登录注册的dialog
     */
    private void showUserDialog() {
        WiFiDialog.Builder builder = new WiFiDialog.Builder(mContext);
        View mView = View.inflate(mContext, R.layout.user_form, null);
        final EditText user_name_et = (EditText) mView.findViewById(R.id.user_name_et);
        final EditText user_pass_et = (EditText) mView.findViewById(R.id.user_pass_et);
        builder.setContentView(mView);

        builder.setPositiveButton("登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestServer("user_logon", user_name_et, user_pass_et);
            }
        });

        builder.setNeutralButton("注册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestServer("user_reg", user_name_et, user_pass_et);
            }
        });
        mDialog = builder.create();
        mDialog.show();
    }

    /**
     * 请求服务器
     *
     * @param uNameET 用户名输入框
     * @param uPassET 密码输入框
     */
    private void requestServer(String act, EditText uNameET, EditText uPassET) {
        //获取数据
        final String userName = uNameET.getText().toString().trim();
        final String userPassWord = uPassET.getText().toString().trim();
        //非空判断
        if (TextUtils.isEmpty(userName)) {
            ToolUtils.showToast(mContext, "您还没输入账号", R.drawable.toast_err);
            return;
        }
        if (userName.length() < 5) {
            ToolUtils.showToast(mContext, "输入的账号小于6位", R.drawable.toast_err);
            return;
        }
        if (TextUtils.isEmpty(userPassWord)) {
            ToolUtils.showToast(mContext, "您还没输入密码", R.drawable.toast_err);
            return;
        }
        if (act.equals("user_reg")) {
            ToolUtils.loadingShow_tv(mContext, R.string.is_registing);
        } else {
            ToolUtils.loadingShow_tv(mContext, R.string.is_loading);
        }
        LoginRegs(act, userName, userPassWord);
    }

    private void LoginRegs(String act, String user, String passwd) { //注册登录
        new Thread(() -> {
            OkGo.<String>post(ToolUtils.setApi(act))
                    .params("user", user)
                    .params("account", user)
                    .params("password", passwd)
                    .params("markcode", ToolUtils.getAndroidId(KamActivity.this))
                    .params("t", System.currentTimeMillis() / 1000)
                    .params("sign", ToolUtils.setSign("user=" + user + "&account=" + user + "&password=" + passwd + "&markcode=" + ToolUtils.getAndroidId(this)))
                    .execute(new AbsCallback<String>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                JSONObject jo = new JSONObject(BaseR.decry_R(response.body()));
                                if (jo.getInt("code") == 200) { //成功
                                    if (act.equals("user_logon")) {
                                        MMkvUtils.saveUser(user);
                                        MMkvUtils.savePasswd(passwd);
                                        ToolUtils.showToast(mContext, "登录成功", R.drawable.toast_smile);
                                        userBean = new Gson().fromJson(BaseR.decry_R(response.body()), ReUserBean.class);
                                        MMkvUtils.saveReUserBean(userBean);
                                        findViewById(R.id.ll_user_login).setVisibility(View.GONE);
                                        findViewById(R.id.ll_vip_user).setVisibility(View.VISIBLE);
                                        initData();
                                        if (mDialog != null && mDialog.isShowing()) {
                                            mDialog.dismiss();
                                        }
                                    } else {
                                        MMkvUtils.saveReUserBean(null);
                                        LoginRegs("user_logon", user, passwd);
                                    }
                                } else {
                                    MMkvUtils.saveReUserBean(null);
                                    ToolUtils.showToast(mContext, jo.getString("msg"), R.drawable.toast_err);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ToolUtils.loadingClose_Tv();
                        }

                        public void onError(Response<String> error) {
                            ToolUtils.loadingClose_Tv();
                            ToolUtils.showToast(mContext, error.body(), R.drawable.toast_err);
                        }

                        @Override
                        public String convertResponse(okhttp3.Response response) throws Throwable {
                            assert response.body() != null;
                            return response.body().string();
                        }
                    });
        }).start();
    }

    private Dialog mDialog;

    private void initClick() {
        findViewById(R.id.ll_user_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserDialog();
            }
        });

        ll_payType_aliB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (initBean != null && ToolUtils.getIsEmpty(initBean.msg.appHuoDong)) {
                    HuoDongDialog dialog = new HuoDongDialog(mContext);
                    dialog.show();
                }else{
                    ToolUtils.showToast(mContext, "暂未开放", R.drawable.toast_err);
                }
            }
        });

        activity_vip_jump_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpActivity(VipActivity.class);
            }
        });

        activity_vip_list.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View child) {
                if (child.isFocusable() && null == child.getOnFocusChangeListener()) {
                    child.setOnFocusChangeListener(focusChangeListener);
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
            }
        });
    }

    private UserInfoBean userInfoData;
    private String[] kamData;

    @SuppressLint("SetTextI18n")
    private void getUserInfo(String token) { //获取用户信息，包含卡密
        OkGo.<String>post(ToolUtils.setApi("get_info"))
                .params("token", token)
                .params("t", System.currentTimeMillis() / 1000)
                .params("sign", ToolUtils.setSign("token=" + token))
                .execute(new AbsCallback<String>() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        findViewById(R.id.iv_anim_loading).setVisibility(View.GONE);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jo = new JSONObject(BaseR.decry_R(response.body()));
                                    if (jo.getInt("code") == 200) {
                                        userInfoData = new Gson().fromJson(BaseR.decry_R(response.body()), UserInfoBean.class);
                                        if (ToolUtils.getIsEmpty(userInfoData.msg.kam)) {
                                            findViewById(R.id.iv_anim_null).setVisibility(View.GONE);
                                            if (userInfoData.msg.kam.contains(",")) {
                                                kamData = userInfoData.msg.kam.split(",");
                                                for (String kamDatum : kamData) {
                                                    getKamInfo(kamDatum);
                                                }
                                            } else {
                                                getKamInfo(userInfoData.msg.kam);
                                            }
                                        }else{
                                            findViewById(R.id.iv_anim_null).setVisibility(View.VISIBLE);
                                        }
                                        userBean.msg.info.vip = userInfoData.msg.vip;
                                        userBean.msg.info.fen = userInfoData.msg.fen;
                                        userBean.msg.info.name = userInfoData.msg.name;
                                        MMkvUtils.saveReUserBean(userBean);
                                    } else {
                                        MMkvUtils.saveReUserBean(null);
                                        Toast.makeText(KamActivity.this, "您的账号在其他设备登录！您已被迫下线", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    MMkvUtils.saveReUserBean(null);
                                    Toast.makeText(KamActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        assert response.body() != null;
                        return response.body().string();
                    }
                });
    }

    public void getKamInfo(String text) { //查询卡密信息
        OkGo.<String>post(ToolUtils.setApi("km_statey"))
                .params("kami", text)
                .params("t", System.currentTimeMillis() / 1000)
                .params("sign", ToolUtils.setSign("kami=" + text))
                .execute(new AbsCallback<String>() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jo = new JSONObject(BaseR.decry_R(response.body()));
                                    if (jo.getInt("code") == 200) { //获取成功
                                        KamInfoBean kamInfo = new Gson().fromJson(BaseR.decry_R(response.body()), KamInfoBean.class);
                                        VipGoodsData vipGoodsData = new VipGoodsData();
                                        if (kamInfo.msg.useTime.equals("0")) {
                                            vipGoodsData.setDes("未使用");
                                        }else{
                                            vipGoodsData.setDes("已使用");
                                        }
                                        if (kamInfo.msg.type.equals("vip")){
                                            vipGoodsData.setDesSS("天");
                                        }else{
                                            vipGoodsData.setDesSS("积分");
                                        }
                                        vipGoodsData.setPrice(Integer.parseInt(kamInfo.msg.amount));
                                        vipGoodsData.setDesS(kamInfo.msg.kami);
                                        vipGoodsDataList.add(vipGoodsData);
                                        vipGoodsAdapter = new VipGoodsAdapter(KamActivity.this, KamActivity.this, vipGoodsDataList);
                                        activity_vip_list.setAdapter(vipGoodsAdapter);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        assert response.body() != null;
                        return response.body().string();
                    }
                });
    }

    public static class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private final int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            //不是第一个的格子都设一个左边和底部的间
            outRect.bottom = space;
            outRect.right = space;
        }
    }

    private ReUserBean userBean;

    @SuppressLint("SetTextI18n")
    @Override
    public void onGoodsItemClick(int position) {
        if (userInfoData != null && userBean != null) {
            if (userInfoData.msg.kam.contains(",")){
                exchangeCard(kamData[position]);
            }else{
                exchangeCard(userInfoData.msg.kam);
            }
        } else {
            Toast.makeText(this, "您未登录", Toast.LENGTH_SHORT).show();
        }
    }

    private void exchangeCard(String key) {
        if (key.isEmpty()) {
            ToolUtils.showToast(mContext, "输入的兑换码有误", R.drawable.toast_err);
        } else {
            if (userBean != null && ToolUtils.getIsEmpty(userBean.msg.token)) {
                recHarGe(key, userBean.msg.token);
            } else {
                ToolUtils.showToast(mContext, "TOKEN有误！请重启应用", R.drawable.toast_err);
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
                                } else {
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

    @Override
    protected void onPause() { //历来
        super.onPause();
        HawkConfig.FORCE_pause = false; //取消强制暂停
    }
}
