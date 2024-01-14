package com.github.tvbox.osc.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.tvbox.osc.R;
import com.github.tvbox.osc.beanry.InitBean;
import com.github.tvbox.osc.util.MMkvUtils;
import com.github.tvbox.osc.util.ToolUtils;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.net.URL;

public class WeiXinDialog extends BaseDialog {

    private ImageView ivWeiXi;
    @SuppressLint("UseCompatLoadingForDrawables")
    public WeiXinDialog(@NonNull @NotNull Context context) {
        super(context);
        setContentView(R.layout.weixin_about);
        ivWeiXi = findViewById(R.id.iv_WeiXi);
        InitBean initBean = MMkvUtils.loadInitBean("");
        if (initBean != null && ToolUtils.getIsEmpty(initBean.msg.uiButtonadimg)) {
            setNetworkBitmap(initBean.msg.uiButtonadimg, context);
        }else{
            ToolUtils.showToast(context, "暂未开放", R.drawable.toast_smile);
        }
    }

    public void setNetworkBitmap(String url,Context context) {
        Glide.with(context)
                .load(url)
                .error(R.drawable.button3)
                .centerCrop()
                .override(0, 0) //默认淡入淡出动画
                .transition(DrawableTransitionOptions.withCrossFade()) //缓存策略,跳过内存缓存【此处应该设置为false，否则列表刷新时会闪一下】
                .skipMemoryCache(false) //缓存策略,硬盘缓存-仅仅缓存最终的图像，即降低分辨率后的（或者是转换后的）
                .diskCacheStrategy(DiskCacheStrategy.ALL) //设置图片加载的优先级
                .priority(Priority.HIGH)
                .into(ivWeiXi);
    }
}