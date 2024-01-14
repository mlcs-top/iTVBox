package com.github.tvbox.osc.ui.dialog;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.beanry.InitBean;
import com.github.tvbox.osc.ui.tv.QRCodeGen;
import com.github.tvbox.osc.util.MMkvUtils;
import com.github.tvbox.osc.util.ToolUtils;

import org.jetbrains.annotations.NotNull;

public class AboutDialog extends BaseDialog {

    public AboutDialog(@NonNull @NotNull Context context) {
        super(context);
        setContentView(R.layout.dialog_about);
        InitBean initBean = MMkvUtils.loadInitBean("");
        if (initBean != null && ToolUtils.getIsEmpty(initBean.msg.uiCommunity)) {
            if (initBean.msg.uiCommunity.contains("|")) {
                String[] ButtonTitle = initBean.msg.uiCommunity.split("\\|");
                ImageView ivQRCode = findViewById(R.id.ivQRCode);
                ivQRCode.setImageBitmap(QRCodeGen.generateBitmap(ButtonTitle[1], 200, 200));
            }else{
                ToolUtils.showToast(context, "暂未开放", R.drawable.toast_err);
            }
        }
    }
}