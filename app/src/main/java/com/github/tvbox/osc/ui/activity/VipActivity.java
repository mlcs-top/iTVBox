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
import com.github.tvbox.osc.beanry.ReUserBean;
import com.github.tvbox.osc.beanry.UserInfoBean;
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
import java.util.Date;


public class VipActivity extends BaseActivity implements VipGoodsAdapter.OnClickListener {

    private static final String TAG = "VipActivity";
    private TextView activity_vip_user_name, activity_vip_user_time, activity_vip_qrcode_text, tv_payType, activity_tv_user_price;
    private LinearLayout ll_payType_wx, ll_payType_aliB, activity_vip_jump_card;
    private ImageView activity_vip_icon, activity_vip_avatar;
    private ImageView activity_vip_qrcode_img;
    private RecyclerView activity_vip_list;
    private ArrayList<VipGoodsData> vipGoodsDataList = new ArrayList<>();
    private VipGoodsAdapter vipGoodsAdapter;
    private String payOrder;

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
    protected void onResume() {
        super.onResume();
        if (payOrder != null) {
            orderStatus(payOrder);
            payOrder = null;
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_vip;
    }

    @Override
    protected void init() {
        initView();
        initData();
        initClick();
        getVipListBean();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initView() {
        findViewById(R.id.tv_______).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_payType_tt).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_payType_wx).setVisibility(View.VISIBLE);
        activity_vip_avatar = findViewById(R.id.activity_vip_avatar);
        ll_payType_wx = findViewById(R.id.ll_payType_wx);
        ll_payType_aliB = findViewById(R.id.ll_payType_aliB);
        activity_vip_jump_card = findViewById(R.id.activity_vip_jump_card);
        activity_vip_user_name = findViewById(R.id.activity_vip_user_name);
        activity_vip_user_time = findViewById(R.id.activity_vip_user_time);
        activity_vip_qrcode_text = findViewById(R.id.activity_vip_qrcode_text);
        activity_vip_qrcode_img = findViewById(R.id.activity_vip_qrcode_img);
        activity_tv_user_price = findViewById(R.id.activity_tv_user_price);
        activity_vip_icon = findViewById(R.id.activity_vip_icon);
        tv_payType = findViewById(R.id.tv_payType);
        activity_vip_list = findViewById(R.id.activity_vip_list);
        activity_vip_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activity_vip_list.addItemDecoration(new SpaceItemDecoration(10));
        InitBean initBean = MMkvUtils.loadInitBean("");
        if (initBean != null && ToolUtils.getIsEmpty(initBean.msg.uiKefu)) {
            activity_vip_qrcode_img.setImageBitmap(QRCodeGen.generateBitmap(initBean.msg.uiKefu, 300, 300));
        } else {
            activity_vip_qrcode_img.setImageDrawable(mContext.getApplicationContext().getResources().getDrawable(R.drawable.app_icon));
        }
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        int points = 0;
        String uName = "00:00:00:00:00:00";
        String uTime = "您还不是影视会员，马上开通会员吧";
        userBean = MMkvUtils.loadReUserBean("");
        activity_vip_icon.setBackgroundResource(R.drawable.channel_vip_ic_1);
        if (userBean != null && ToolUtils.getIsEmpty(userBean.msg.token)) {
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

            points = userBean.msg.info.fen;

            if (!uName.isEmpty()) {
                activity_vip_user_name.setText(uName);
            }
            if (!uTime.isEmpty()) {
                activity_vip_user_time.setText(uTime);
            }

            activity_tv_user_price.setText("选中套餐按(OK)键");

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
                    .params("markcode", ToolUtils.getAndroidId(VipActivity.this))
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
    private String payType = "wx";

    private void initClick() {
        
        findViewById(R.id.ll_user_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserDialog();
            }
        });

        ll_payType_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = "wx";
                activity_vip_qrcode_text.setText("微信");
            }
        });

        ll_payType_aliB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = "ali";
                activity_vip_qrcode_text.setText("支付宝");
            }
        });

        activity_vip_jump_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpActivity(VipCardActivity.class);
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

    private GoodsBean goodsData;

    /**
     * 获取商品列表
     */
    private void getVipListBean() {
        new Thread(() -> {
            OkGo.<String>post(ToolUtils.setApi("goods"))
                    .params("t", System.currentTimeMillis() / 1000)
                    .params("sign", ToolUtils.setSign("null"))
                    .execute(new AbsCallback<String>() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            findViewById(R.id.iv_anim_loading).setVisibility(View.GONE);
                            if (ToolUtils.iniData(response, mContext)) {
                                goodsData = new Gson().fromJson(BaseR.decry_R(response.body()), GoodsBean.class);
                                if (goodsData != null && goodsData.code == 200 && goodsData.msg.size() > 0) {
                                    findViewById(R.id.iv_anim_null).setVisibility(View.GONE);
                                    for (int i = 0; i < goodsData.msg.size(); i++) {
                                        VipGoodsData vipGoodsData = new VipGoodsData();
                                        vipGoodsData.setDes(goodsData.msg.get(i).gname);
                                        vipGoodsData.setPrice(goodsData.msg.get(i).gmoney);
                                        vipGoodsData.setDesS(goodsData.msg.get(i).cv);
                                        vipGoodsDataList.add(vipGoodsData);
                                        vipGoodsAdapter = new VipGoodsAdapter(VipActivity.this, VipActivity.this, vipGoodsDataList);
                                        activity_vip_list.setAdapter(vipGoodsAdapter);
                                    }
                                }else{
                                    findViewById(R.id.iv_anim_null).setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        @Override
                        public String convertResponse(okhttp3.Response response) throws Throwable {
                            assert response.body() != null;
                            return response.body().string();
                        }
                    });
        }).start();
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
        if (goodsData != null && userBean != null) {
            int userId = userBean.msg.info.id;
            payOrder = getOrderNum(userId);
            String payUrl = ToolUtils.setApi("pay") + "&order=" + payOrder + "&token=" + userBean.msg.token + "&way=" + payType + "&gid=" + goodsData.msg.get(position).gid + "&ua=0" + "&t=";
            Intent newIntent = new Intent(mContext, WebViewActivity.class);
            Log.d(TAG, "onGoodsItemClick: "+payUrl);
            newIntent.putExtra("newUrl", payUrl);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            VipActivity.this.startActivity(newIntent);
        } else {
            Toast.makeText(this, "您未登录", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 生成订单号（25位）：时间（精确到毫秒）+3位随机数+5位用户id
     */
    public static synchronized String getOrderNum(int userId) {
        Log.d(TAG, "getOrderNum: 111111111");
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String localDate = dateFormat.format(date);
        //3位随机数
        int i = (int) (Math.random() * 900 + 100);
        String randomNumeric = Integer.toString(i);
        String orderNum = localDate + randomNumeric + userId;
        Log.d("订单号:{}", orderNum);
        return orderNum;
    }

    /**
     * 查询订单状态
     */
    private void orderStatus(String out_trade_no) {
        new Thread(() -> {
            OkGo.<String>post(ToolUtils.setApi("pay_res"))
                    .params("oid", out_trade_no)
                    .params("t", System.currentTimeMillis() / 1000)
                    .params("sign", ToolUtils.setSign("oid=" + out_trade_no))
                    .execute(new AbsCallback<String>() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            if (ToolUtils.iniData(response, mContext)) {
                                ToolUtils.showToast(VipActivity.this, "会员开通成功", R.drawable.toast_smile);
                            }
                            getUserInfo(userBean.msg.token);
                        }

                        @Override
                        public void onError(Response<String> error) {
                            ToolUtils.showToast(VipActivity.this, BaseR.decry_R(error.toString()), R.drawable.toast_err);
                        }

                        @Override
                        public String convertResponse(okhttp3.Response response) throws Throwable {
                            assert response.body() != null;
                            return response.body().string();
                        }
                    });
        }).start();
    }

    @SuppressLint("SetTextI18n")
    private void getUserInfo(String token) {
        OkGo.<String>post(ToolUtils.setApi("get_info"))
                .params("token", token)
                .params("t", System.currentTimeMillis() / 1000)
                .params("sign", ToolUtils.setSign("token=" + token))
                .execute(new AbsCallback<String>() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        runOnUiThread(() -> {
                            try {
                                JSONObject jo = new JSONObject(BaseR.decry_R(response.body()));
                                if (jo.getInt("code") == 200) {
                                    UserInfoBean userInfoData = new Gson().fromJson(BaseR.decry_R(response.body()), UserInfoBean.class);
                                    userBean.msg.info.vip = userInfoData.msg.vip;
                                    userBean.msg.info.fen = userInfoData.msg.fen;
                                    userBean.msg.info.name = userInfoData.msg.name;
                                    MMkvUtils.saveReUserBean(userBean);
                                    initData();
                                    return;
                                } else {
                                    MMkvUtils.saveReUserBean(null);
                                    Toast.makeText(VipActivity.this, "您的账号在其他设备登录！您已被迫下线", Toast.LENGTH_SHORT).show();
                                }
                                initData();
                            } catch (JSONException e) {
                                MMkvUtils.saveReUserBean(null);
                                Toast.makeText(VipActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void onError(Response<String> error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(VipActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onPause() { //历来
        super.onPause();
        HawkConfig.FORCE_pause = false; //取消强制暂停
    }
}
