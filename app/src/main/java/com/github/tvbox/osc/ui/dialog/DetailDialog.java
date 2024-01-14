package com.github.tvbox.osc.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.tvbox.osc.R;

import org.jetbrains.annotations.NotNull;

public class DetailDialog extends BaseDialog {

    public DetailDialog(@NonNull @NotNull Context context, String name, String tip, OnListener listener) {
        super(context);

        setContentView(R.layout.dialog_delete);
        setCanceledOnTouchOutside(false);
        TextView tipInfo = findViewById(R.id.tipInfo);
        TextView title = findViewById(R.id.tv_title);
        title.setText(name);
        tipInfo.setText(tip);

        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                listener.cancel();
            }
        });
    }

    public interface OnListener {
        void left();
        void cancel();
    }
}
