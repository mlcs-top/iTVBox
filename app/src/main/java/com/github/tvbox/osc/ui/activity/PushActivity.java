package com.github.tvbox.osc.ui.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.base.BaseActivity;
import com.github.tvbox.osc.beanry.ReLevelBean;
import com.github.tvbox.osc.server.ControlManager;
import com.github.tvbox.osc.ui.tv.QRCodeGen;
import com.github.tvbox.osc.util.MMkvUtils;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class PushActivity extends BaseActivity {
    private ImageView ivQRCode;
    private TextView tvAddress;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_push;
    }

    @Override
    protected void init() {
        initView();
        initData();
    }

    private void initView() {
        ivQRCode = findViewById(R.id.ivQRCode);
        tvAddress = findViewById(R.id.tvAddress);
        refreshQRCode();
        findViewById(R.id.pushLocal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ClipboardManager manager = (ClipboardManager) PushActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                    if (manager != null) {
                        if (manager.hasPrimaryClip() && manager.getPrimaryClip() != null && manager.getPrimaryClip().getItemCount() > 0) {
                            ClipData.Item addedText = manager.getPrimaryClip().getItemAt(0);
                            Intent newIntent = new Intent(mContext, DetailActivity.class);
                            newIntent.putExtra("id", addedText.getText().toString().trim());
                            newIntent.putExtra("sourceKey", "push_agent");
                            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            PushActivity.this.startActivity(newIntent);
                        }
                    }
                } catch (Throwable th) {

                }
            }
        });
    }

    private void refreshQRCode() {
        String address = ControlManager.get().getAddress(false);
        tvAddress.setText(String.format("手机/电脑扫描上方二维码或者直接浏览器访问地址\n%s", address));
        ivQRCode.setImageBitmap(QRCodeGen.generateBitmap(address, AutoSizeUtils.mm2px(this, 300), AutoSizeUtils.mm2px(this, 300), 4));
    }

    private void initData() {

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