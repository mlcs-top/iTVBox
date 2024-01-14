package com.github.tvbox.osc.ui.activity;

import static com.github.tvbox.osc.server.ControlManager.mContext;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.github.tvbox.osc.beanry.NoticeBean;
import com.github.tvbox.osc.beanry.ReLevelBean;
import com.github.tvbox.osc.beanry.ReUserBean;
import com.github.tvbox.osc.beanry.InitBean;
import com.github.tvbox.osc.beanry.UserInfoBean;
import com.github.tvbox.osc.ui.dialog.AboutDialog;
import com.github.tvbox.osc.ui.dialog.AboutDialog2;
import com.github.tvbox.osc.ui.dialog.WeiXinDialog;
import com.github.tvbox.osc.ui.dialog.WeiXinDialog2;
import com.github.tvbox.osc.ui.tv.widget.AlwaysMarqueeTextView;
import com.github.tvbox.osc.util.BaseR;
import com.github.tvbox.osc.util.FastClickCheckUtil;
import com.github.tvbox.osc.util.MMkvUtils;
import com.github.tvbox.osc.util.ToolUtils;
import com.github.tvbox.osc.util.WiFiDialog;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * @茶茶QQ205888578
 * @date :2023/9/25
 * 我的界面
 */
public class UserActivity extends BaseActivity {
    private static final String TAG = "UserActivity";
    private TextView tvUserMac, tvUserPoints, tvUserEndTime;
    private LinearLayout llUserRefresh;

    private ImageView lvUserRefresh, ll_User_ads,ll_User_ads2,ll_User_ads3, user_activity_pic;


    private ImageView vipimg;
    private ImageView signImg;
    private TextView tv_Button_name,tv_Button_name2, lluserRefreshtext;
    private TextView user_finishHome;//返回
    private TextView userSearch;//搜索
    private TextView Usersc;//收藏

    private LinearLayout mllGongGao;
    private AlwaysMarqueeTextView gongGao;
    private boolean KOJAK = true;



    @SuppressLint("HandlerLeak")
    private Handler Screensaver = new Handler() {
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
    public void onUserInteraction() {
        super.onUserInteraction();
        Screensaver.removeMessages(1);
        Screensaver.sendEmptyMessageDelayed(1, 60000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Screensaver.removeCallbacksAndMessages(null);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
       // initData();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void init() {
        KOJAK = true;
        Screensaver.sendEmptyMessageDelayed(1, 60000);
        tvUserMac = findViewById(R.id.llUserMac);
        tvUserPoints = findViewById(R.id.llUserPrice);
        tvUserEndTime = findViewById(R.id.llUserEndTime);
        lvUserRefresh = findViewById(R.id.lv_user_Refresh);
        llUserRefresh = findViewById(R.id.ll_user_Refresh);//刷新
        lluserRefreshtext = findViewById(R.id.ll_user_Refresh_text);
        tv_Button_name = findViewById(R.id.tv_Button_name);
        tv_Button_name2 = findViewById(R.id.tv_Button_name2);
        ll_User_ads = findViewById(R.id.ll_user_ads);
        ll_User_ads2 = findViewById(R.id.ll_user_ads2);
        ll_User_ads3 = findViewById(R.id.ll_user_ads3);
        user_activity_pic = findViewById(R.id.user_activity_pic);
        user_finishHome = findViewById(R.id.user_finishHome);//返回
        userSearch = findViewById(R.id.userSearch);//搜索
        Usersc = findViewById(R.id.Usersc);//收藏
        vipimg = findViewById(R.id.vipImg);
        signImg = findViewById(R.id.signImg);

        gongGao = findViewById(R.id.usergonggao);
        mllGongGao = findViewById(R.id.gongGao_user);




        initData();
        onClickM();
        getNotice();


        loadImageView(ll_User_ads, initData.msg.uiButton3backg);
        loadImageView(ll_User_ads2, initData.msg.uiButton3backg2);
        loadImageView(ll_User_ads3, initData.msg.uiButton3backg3);

      /*  //一图
            Glide.with(this)
                    .load(initData.msg.uiButton3backg)
                    .error(R.drawable.yujiazai)
                    .override(1920, 1080) // 设置加载图片的大小
                    .placeholder(R.drawable.yujiazai) // 设置预加载图片
                    .into(ll_User_ads);

        //二图
        Glide.with(this)
                .load(initData.msg.uiButton3backg2)
                .error(R.drawable.yujiazai)
                .override(1920, 1080) // 设置加载图片的大小
                .placeholder(R.drawable.yujiazai) // 设置预加载图片
                .into(ll_User_ads2);
        //三图
        Glide.with(this)
                .load(initData.msg.uiButton3backg3)
                .error(R.drawable.yujiazai)
                .override(1920, 1080) // 设置加载图片的大小
                .placeholder(R.drawable.yujiazai) // 设置预加载图片
                .into(ll_User_ads3);
*/


        vipCard();//判断是否vip显示图标

        //退出登录，检测是否登录
        ReUserBean userData = MMkvUtils.loadReUserBean("");
        TextView logoutText = findViewById(R.id.user_fragment_Logout_text);
        if (userData != null) {
            // 已登录
            logoutText.setText("退出登录");
        } else {
            // 未登录
            logoutText.setText("您未登录");
        }



        //返回
        user_finishHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // 关闭当前界面
            }

        });
        //搜索
        userSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpActivity(SearchActivity.class);
            }
        });
        //收藏
        Usersc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {jumpActivity(CollectActivity.class);
            }
        });

    }

//加载三图片
    private void loadImageView(ImageView imageView, String url) {
        Glide.with(this)
                .load(url)
                .error(R.drawable.yujiazai)
                .override(1920, 1080)
                .placeholder(R.drawable.yujiazai)
                .into(imageView);
    }

    private InitBean initData;
    private ReUserBean userData;

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


    @SuppressLint("SetTextI18n")
    private void initData() {
        initData = MMkvUtils.loadInitBean("");
        if (initData != null) {
            if (ToolUtils.getIsEmpty(initData.msg.uiCommunity)) {
                if (initData.msg.uiCommunity.contains("|")) {
                    String[] buttonTitle = initData.msg.uiCommunity.split("\\|");
                    tv_Button_name.setText(buttonTitle[0]);
                } else {
                    tv_Button_name.setText(initData.msg.uiCommunity);
                }
            } else {
                tv_Button_name.setVisibility(View.GONE);
            }

            if (ToolUtils.getIsEmpty(initData.msg.uiKefu2)) {
                if (initData.msg.uiKefu2.contains("|")) {
                    String[] buttonTitle = initData.msg.uiKefu2.split("\\|");
                    tv_Button_name2.setText(buttonTitle[0]);
                } else {
                    tv_Button_name2.setText(initData.msg.uiKefu2);
                }
            }else {
                tv_Button_name2.setVisibility(View.GONE);
            }

        }


        userData = MMkvUtils.loadReUserBean("");
        if (userData != null && ToolUtils.getIsEmpty(userData.msg.token)) {
            lluserRefreshtext.setText("刷新");
            // lvUserRefresh.setVisibility(View.VISIBLE);
            findViewById(R.id.llUserEndTime).setVisibility(View.VISIBLE);
            // findViewById(R.id.ll_user_pic).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_user_login_text).setVisibility(View.GONE);
            if (ToolUtils.getIsEmpty(MMkvUtils.loadUser())) {
                tvUserMac.setText("用户：" + MMkvUtils.loadUser());
            }else{
                tvUserMac.setText("用户：" + userData.msg.info.name);
            }
            tvUserPoints.setText("积分：" + userData.msg.info.fen);
            tvUserEndTime.setText("SVIP：" + ToolUtils.stampToDate(userData.msg.info.vip));
            getUserInfo(userData.msg.token, false);

            Glide.with(this)
                    .load(userData.msg.info.pic)
                    .error(R.drawable.channel_user_avatar_default)
                    .into(user_activity_pic);
        } else {
            findViewById(R.id.llUserEndTime).setVisibility(View.GONE);
            //  findViewById(R.id.ll_user_pic).setVisibility(View.GONE);
            findViewById(R.id.ll_user_login_text).setVisibility(View.VISIBLE);
            // findViewById(R.id.user_fragment_Logout).setVisibility(View.GONE);
            lluserRefreshtext.setText("立即登录");
        }
    }

    //结束

    @SuppressLint("SetTextI18n")
    private void onClickM() {
        findViewById(R.id.ll_user_login_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserDialog();
            }
        });
        findViewById(R.id.ll_user_Refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lluserRefreshtext.getText().toString().equals("刷新")) {
                    if (userData !=null && ToolUtils.getIsEmpty(userData.msg.token)){
                        //  lvUserRefresh.setVisibility(View.VISIBLE);
                        getUserInfo(userData.msg.token, true);
                    }else{
                        ToolUtils.showToast(mContext, "已是最新数据", R.drawable.toast_smile);
                    }
                } else {
                    showUserDialog();
                }
            }
        });
        findViewById(R.id.cashCoupon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpActivity(KamActivity.class);
            }
        });
        findViewById(R.id.pointsMall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastClickCheckUtil.check(v);
                jumpActivity(ExchangeActivity.class);
            }
        });
        findViewById(R.id.ll_user_openVip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastClickCheckUtil.check(v);
                if (initData != null) {
                    if (initData.msg.pay.state.equals("y")) {
                        jumpActivity(VipActivity.class);
                    } else {
                        jumpActivity(VipCardActivity.class);
                    }
                } else {
                    ToolUtils.showToast(mContext, "暂未开放", R.drawable.toast_err);
                }
            }
        });
        ll_User_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastClickCheckUtil.check(v);
                if (initData != null && ToolUtils.getIsEmpty(initData.msg.uiButtonadimg)) {
                    WeiXinDialog dialog = new WeiXinDialog(mContext);
                    dialog.show();
                }else{
                    ToolUtils.showToast(mContext, "敬请期待", R.drawable.toast_err);
                }
            }
        });
        //签到
        ll_User_ads2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始
                exchangeCard();

                //结束
            }
        });
        ll_User_ads3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastClickCheckUtil.check(v);
                if (initData != null && ToolUtils.getIsEmpty(initData.msg.uiButtonadimg3)) {
                    WeiXinDialog2 dialog = new WeiXinDialog2(mContext);
                    dialog.show();
                }else{
                    ToolUtils.showToast(mContext, "敬请期待", R.drawable.toast_err);
                }
            }
        });
        findViewById(R.id.user_System_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastClickCheckUtil.check(v);
                jumpActivity(SettingActivity.class);
            }
        });
        findViewById(R.id.user_fragment_Feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastClickCheckUtil.check(v);
                AboutDialog dialog = new AboutDialog(mContext);
                dialog.show();
            }
        });
        findViewById(R.id.user_fragment_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastClickCheckUtil.check(v);
                jumpActivity(AboutActivity.class);
            }
        });
        //我的消息
        findViewById(R.id.fl_Message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FastClickCheckUtil.check(v);
                jumpActivity(MessageActivity.class);
            }
        });
        //公众号
        findViewById(R.id.wechat_official_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastClickCheckUtil.check(v);
                AboutDialog2 dialog = new AboutDialog2(mContext);
                dialog.show();
            }
        });
        //退出登录
        findViewById(R.id.user_fragment_Logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastClickCheckUtil.check(v);
                if (userData != null && ToolUtils.getIsEmpty(userData.msg.token)) {
                    findViewById(R.id.lv_user_Refresh).setVisibility(View.VISIBLE);
                    MMkvUtils.saveUser(null);
                    MMkvUtils.savePasswd(null);
                    MMkvUtils.saveReUserBean(null);
                    Toast.makeText(UserActivity.this, "退出登录", Toast.LENGTH_SHORT).show();
                    jumpActivity(HomeActivity.class);
                } else {
                    //登录
                    showUserDialog();
                }
            }
        });
    }




    /**
     *签到
     */

    private void exchangeCard() {
        ReUserBean userBean = MMkvUtils.loadReUserBean("");

        TextView logoutText = findViewById(R.id.user_fragment_Logout_text);
        if (userData != null) {
            //已登录
            if (userBean != null && ToolUtils.getIsEmpty(userBean.msg.token)) {
                recHarGe(userBean.msg.token);
            } else {
                ToolUtils.showToast(mContext, "TOKEN过期！请重启应用", R.drawable.toast_err);
            }

        } else {
            // 未登录
            ToolUtils.showToast(mContext, "您还没有登录呢，没法签到哦", R.drawable.toast_err);
        }
    }



    private void recHarGe(String token) {
        Log.d("token", "recHarGe: " + token);
        new Thread(() -> {
            OkGo.<String>post(ToolUtils.setApi("clock"))
                    .params("token", token)
                    .params("t", "")  //
                    .params("sign", ToolUtils.setSign("token=" + token + "&clock" ))
                    .execute(new AbsCallback<String>() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                JSONObject jo = new JSONObject(BaseR.decry_R(response.body()));
                                if (jo.getInt("code") == 200) {
                                    // ToolUtils.showToast(mContext, "签到成功，已获得奖励。", R.drawable.toast_smile);
                                    ToolUtils.showToast(mContext,  jo.getString("msg"), R.drawable.toast_err);
                                    //刷新
                                    if (userData != null && ToolUtils.getIsEmpty(userData.msg.token)) {
                                        getUserInfo(userData.msg.token, false);
                                    }

                                }
                                else {
                                    ToolUtils.showToast(mContext,  jo.getString("msg"), R.drawable.toast_err);
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



    //判断是否会员
    private void vipCard() {
        ReUserBean userBean = MMkvUtils.loadReUserBean("");

        TextView logoutText = findViewById(R.id.user_fragment_Logout_text);
        if (userData != null) {
            //已登录
            if (userBean != null && ToolUtils.getIsEmpty(userBean.msg.token)) {
                shifouvip(userBean.msg.token);
            } else {
                //token过期

            }

        } else {
            // 未登录

        }
    }

    private void shifouvip(String token) {
        Log.d("token", "recHarGe: " + token);
        new Thread(() -> {
            OkGo.<String>post(ToolUtils.setApi("get_vip"))
                    .params("token", token)
                    .params("t", "")  //
                    .params("sign", ToolUtils.setSign("token=" + token  ))
                    .execute(new AbsCallback<String>() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                JSONObject jo = new JSONObject(BaseR.decry_R(response.body()));
                                if (jo.getInt("code") == 200) {

                                    vipimg.setImageResource(R.drawable.channel_vip_ic_2);
                                }
                                else {
                                    //不是vip
                                    vipimg.setImageResource(R.drawable.channel_vip_ic_1);
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
     * @param uNameET    用户名输入框
     * @param uPassET    密码输入框
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
                    .params("markcode", ToolUtils.getAndroidId(UserActivity.this))
                    .params("t", System.currentTimeMillis() / 1000)
                    .params("sign", ToolUtils.setSign("user="+user+"&account="+user+"&password="+passwd+"&markcode="+ToolUtils.getAndroidId(this)))
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
                                        ((TextView)findViewById(R.id.user_fragment_Logout_text)).setText("退出登录");
                                        ReUserBean userData = new Gson().fromJson(BaseR.decry_R(response.body()), ReUserBean.class);
                                        if (ToolUtils.getIsEmpty(MMkvUtils.loadUser())) {
                                            tvUserMac.setText("用户：" + MMkvUtils.loadUser());
                                        }else{
                                            tvUserMac.setText("用户：" + userData.msg.info.name);
                                        }
                                        tvUserPoints.setText("积分：" + userData.msg.info.fen);
                                        tvUserEndTime.setText("SVIP：" + ToolUtils.stampToDate(userData.msg.info.vip));
                                        MMkvUtils.saveReUserBean(userData);
                                        lluserRefreshtext.setText("刷新");
                                        findViewById(R.id.llUserEndTime).setVisibility(View.VISIBLE);
                                        //  findViewById(R.id.ll_user_pic).setVisibility(View.VISIBLE);
                                        findViewById(R.id.ll_user_login_text).setVisibility(View.GONE);
                                        //    findViewById(R.id.user_fragment_Logout).setVisibility(View.VISIBLE);


                                        //返回然后重新加载界面，达到刷新的效果
                                        Intent intent = new Intent(UserActivity.this, UserActivity.class);
                                        startActivity(intent);
                                        finish(); // 关闭当前界面


                                        if (mDialog != null && mDialog.isShowing()) {
                                            mDialog.dismiss();
                                        }
                                    } else {
                                        MMkvUtils.saveReUserBean(null);
                                        LoginRegs("user_logon", user, passwd);
                                    }
                                    //  findViewById(R.id.lv_user_Refresh).setVisibility(View.GONE);
                                } else {
                                    MMkvUtils.saveReUserBean(null);
                                    ToolUtils.showToast(mContext, jo.getString("msg"), R.drawable.toast_err);
                                }


                            } catch (JSONException e) {
                                MMkvUtils.saveReUserBean(null);
                                e.printStackTrace();
                            }
                            ToolUtils.loadingClose_Tv();
                        }


                        public void onError(Response<String> error) {
                            ToolUtils.loadingClose_Tv();
                            ToolUtils.showToast(mContext, "未知错误", R.drawable.toast_err);
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

    @SuppressLint("SetTextI18n")
    private void getUserInfo(String token, boolean i) {
        OkGo.<String>post(ToolUtils.setApi("get_info"))
                .params("token", token)
                .params("t", System.currentTimeMillis() / 1000)
                .params("sign", ToolUtils.setSign("token="+token))
                .execute(new AbsCallback<String>() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jo = new JSONObject(BaseR.decry_R(response.body()));
                                    if (jo.getInt("code") == 200){
                                        UserInfoBean userInfoData = new Gson().fromJson(BaseR.decry_R(response.body()), UserInfoBean.class);
                                        userData.msg.info.vip = userInfoData.msg.vip;
                                        userData.msg.info.fen = userInfoData.msg.fen;
                                        userData.msg.info.name = userInfoData.msg.name;
                                        if (ToolUtils.getIsEmpty(MMkvUtils.loadUser())) {
                                            tvUserMac.setText("用户：" + MMkvUtils.loadUser());
                                        }else{
                                            tvUserMac.setText("用户：" + userData.msg.info.name);
                                        }
                                        tvUserPoints.setText("积分：" + userInfoData.msg.fen);
                                        tvUserEndTime.setText("SVIP：" + ToolUtils.stampToDate(userInfoData.msg.vip));
                                        MMkvUtils.saveReUserBean(userData);

                                        //   findViewById(R.id.user_fragment_Logout).setVisibility(View.VISIBLE);
                                        //        if (i){
                                        // Toast.makeText(UserActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                                        //        Toast.makeText(null, "", Toast.LENGTH_SHORT).show();
                                        //    }
                                        //  findViewById(R.id.lv_user_Refresh).setVisibility(View.GONE);
                                    }else{
                                        if (i){
                                            Toast.makeText(UserActivity.this, "您的账号在其他设备登录！您已被迫下线", Toast.LENGTH_SHORT).show();
                                        }
                                        lluserRefreshtext.setText("登录");
                                        MMkvUtils.saveReUserBean(null);
                                        findViewById(R.id.llUserEndTime).setVisibility(View.GONE);
                                        //    findViewById(R.id.ll_user_pic).setVisibility(View.GONE);
                                        findViewById(R.id.ll_user_login_text).setVisibility(View.VISIBLE);
                                        //     findViewById(R.id.user_fragment_Logout).setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    if (i){
                                        Toast.makeText(UserActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
                                    }
                                    MMkvUtils.saveReUserBean(null);
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Response<String> error) {
                        Toast.makeText(UserActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        assert response.body() != null;
                        return response.body().string();
                    }
                });


    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_user;
    }
}
