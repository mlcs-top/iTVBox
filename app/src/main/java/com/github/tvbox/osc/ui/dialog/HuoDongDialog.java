package com.github.tvbox.osc.ui.dialog;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.beanry.InitBean;
import com.github.tvbox.osc.ui.tv.QRCodeGen;
import com.github.tvbox.osc.util.BaseR;
import com.github.tvbox.osc.util.HawkConfig;
import com.github.tvbox.osc.util.MMkvUtils;
import com.github.tvbox.osc.util.ToolUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;

import org.jetbrains.annotations.NotNull;

public class HuoDongDialog extends BaseDialog {

    public HuoDongDialog(@NonNull @NotNull Context context) {
        super(context);
        setContentView(R.layout.dialog_about);
        InitBean initBean = MMkvUtils.loadInitBean("");
        if (initBean != null && ToolUtils.getIsEmpty(initBean.msg.appHuoDong)) {
            ImageView ivQRCode = findViewById(R.id.ivQRCode);
            ivQRCode.setImageBitmap(QRCodeGen.generateBitmap(initBean.msg.appHuoDong, 200, 200));
        }
    }
}