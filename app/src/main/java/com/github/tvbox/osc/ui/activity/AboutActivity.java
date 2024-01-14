package com.github.tvbox.osc.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.tvbox.osc.R;
import com.github.tvbox.osc.base.BaseActivity;
import com.github.tvbox.osc.beanry.InitBean;
import com.github.tvbox.osc.beanry.ReLevelBean;
import com.github.tvbox.osc.util.BaseR;
import com.github.tvbox.osc.util.HawkConfig;
import com.github.tvbox.osc.util.MMkvUtils;
import com.github.tvbox.osc.util.MacUtils;
import com.github.tvbox.osc.util.ToolUtils;
import com.github.tvbox.osc.view.HomeDialog;

import java.io.InputStream;
import java.net.URL;

public class AboutActivity extends BaseActivity {

    private TextView activity_about_text;
    private TextView activity_about_version;
    private TextView activity_about_check;
    private TextView activity_about_mac;
    private TextView user_fan_kui, activity_about_auth;
    private ImageView iv_AboutActivity_logo;
    private Bitmap bitmap;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_about;
    }

    @Override
    protected void init() {
        initBean = MMkvUtils.loadInitBean("");
        initView();
        initData();
        setListener();
        getAppVersion(false);
    }

    private void initView() {
        activity_about_text = findViewById(R.id.activity_about_text);
        activity_about_version = findViewById(R.id.activity_about_version);
        activity_about_check = findViewById(R.id.activity_about_check);
        activity_about_mac = findViewById(R.id.activity_about_mac);
        user_fan_kui = findViewById(R.id.user_fan_kui);
        iv_AboutActivity_logo = findViewById(R.id.iv_AboutActivity_logo);
        activity_about_auth = findViewById(R.id.activity_about_auth);
    }

    private void getAppVersion(boolean go) {

        if (initBean != null && ToolUtils.getIsEmpty(initBean.msg.appBb)) {
            String dVersion = initBean.msg.appBb; //最新版本
            String bVersion = ToolUtils.getVersion(mContext); //本地版本

            bVersion = bVersion.toLowerCase();
            dVersion = dVersion.toLowerCase();

            if(dVersion.compareTo(bVersion) > 0) {
                activity_about_check.setText("是");
                if (go) {
 //                   Screensaver.removeMessages(1);
                    showUpdateDialog(initBean.msg.appNshow, initBean.msg.appNurl, 1);
                }
            }else{
                activity_about_check.setText("否");
                if (go) {
                    ToolUtils.showToast(this, "已是最新版本", R.drawable.toast_smile);
                }
            }
            if (ToolUtils.getIsEmpty(initBean.msg.uiMode)){
                if (initBean.msg.uiMode.equals("y")){
                    activity_about_auth.setText("是");
                }else{
                    activity_about_auth.setText("否");
                }
            }
        }
    }

    /**
     * 显示升级提示的对话框
     */
    private void showUpdateDialog(String remark, final String updateurl, int isRequired) {
        HomeDialog.Builder builder = new HomeDialog.Builder(mContext);
        builder.setTitle("版本升级");
        String[] remarks = remark.split(";");
        String str = "";
        for (int i = 0; i < remarks.length; i++) {
            if (i == remarks.length - 1) {
                str = str + remarks[i];
                continue;
            } else {
                str = str + remarks[i] + "\n";
            }
        }
        Log.d("TAG", "msg=" + str);
        builder.setMessage(str);
        if (isRequired == 1) {
            builder.setPositiveButton("等不及了，立即更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ToolUtils.showToast(mContext, "正在后台下载,请稍后", R.drawable.toast_smile);
                    ToolUtils.startDownloadApk(mContext, updateurl, homeHandler);
                    dialog.dismiss();
                }
            });
        } else {
            builder.setPositiveButton("等不及了，立即更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ToolUtils.showToast(mContext, "正在后台下载,请稍后", R.drawable.toast_smile);
                    ToolUtils.startDownloadApk(mContext, updateurl, homeHandler);
                    dialog.dismiss();
                }
            });
            builder.setNeutralButton("先看片呢，稍后提醒", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    @SuppressLint("HandlerLeak")
    private Handler homeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            onMessage(msg);
        }
    };

    /**
     * @brief 窗口消息处理函数。
     * @author joychang
     * @param[in] msg 窗口消息。
     */
    @SuppressLint("WrongConstant")
    private void onMessage(final Message msg) {
        if (msg != null) {
            switch (msg.what) {
                case AboutActivity.WindowMessageID.ERROR:
                    Toast.makeText(getApplicationContext(), "服务器内部异常", 1).show();
                    break;
                case AboutActivity.WindowMessageID.DOWNLOAD_ERROR:
                    Toast.makeText(getApplicationContext(), "下载失败", 1).show();
                    break;
                case 1001://软件更新  总大小
                    countSize = (Float) msg.obj;
                    break;
                case 1002://软件更新  当前下载大小
                    currentSize = (Float) msg.obj;
                    activity_about_check.setText("正在下载更新 " + (int) (currentSize / countSize * 100) + "%");
                    break;
            }
        }
    }

    private float countSize;    //软件更新总大小
    private float currentSize;    //软件更新当前下载进度

    /**
     * @author joychang
     * @class WindowMessageID
     * @brief 内部消息ID定义类。
     */
    private static class WindowMessageID {
        /**
         * @brief 请求出错。
         */
        public final static int ERROR = 0x00000004;
        // 版本更新的消息
        private final static int DOWNLOAD_ERROR = 0x000000010;
    }

    private InitBean initBean;

    @SuppressLint("SetTextI18n")
    private void initData() {
        String str = "<font color='#ff9b26'><big>" + getResources().getString(R.string.app_name) + "</big></font>" + "<font><samll>是xxx旗下互联网电视平台，独家提供xxx所有栏目以及自制高清、超清视频点播和直播内容，并为用户提供各类热门电影、电视剧、综艺、动漫等内容。<samll></font>";
        if (initBean != null) {
            if (ToolUtils.getIsEmpty(initBean.msg.appAbout)){
                if (initBean.msg.appAbout.contains("|")){
                    String[] aboutData = initBean.msg.appAbout.split("\\|");
                    str = "<font color='#ff9b26'><big>" + aboutData[0] + "</big></font>" + "<font><samll>" + aboutData[1] + "<samll></font>";
                }else{
                    str = "<font color='#ff9b26'><big>" + getResources().getString(R.string.app_name) + "</big></font>" + "<font><samll>" + initBean.msg.appAbout + "<samll></font>";
                }
            }
            if (ToolUtils.getIsEmpty(initBean.msg.uiGroup)){
                user_fan_kui.setText(initBean.msg.uiGroup);
            }
            if (ToolUtils.getIsEmpty(initBean.msg.uiLogo)){
                Glide.with(this)
                        .load(initBean.msg.uiLogo)
                        .error(R.drawable.sm_logo)
                        .into(iv_AboutActivity_logo);
            }
        } else {
            user_fan_kui.setText("暂无");
        }
        activity_about_text.setText(Html.fromHtml(str));
        activity_about_version.setText(ToolUtils.getVersion(mContext) + ".22.08.05.DBEL_TVAPP.0.0.Release");
        String Mac = MacUtils.getMac(true);
        if (Mac == null || Mac.contains("00:00")) {
            Mac = ToolUtils.getAndroidId(AboutActivity.this);
        }
        activity_about_mac.setText(Mac);
    }

    private void setListener() {
        findViewById(R.id.ll_Update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAppVersion(true);
            }
        });
    }

 /*   private boolean KOJAK = true;
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
        Screensaver.removeCallbacksAndMessages(null);
    }*/
}
