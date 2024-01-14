
package com.github.tvbox.osc.beanry;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.tvbox.osc.R;

import java.util.List;
import com.bumptech.glide.Glide;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.animation.BounceInterpolator;

import com.github.tvbox.osc.ui.activity.FastSearchActivity;


public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {

    private List<AdvBean.MsgDTO> msg;

    public BannerAdapter(List<AdvBean.MsgDTO> msg) {
        this.msg = msg;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_recommend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textView.setText(msg.get(position).name);

        // 加载图片到ivThumb控件
        if (msg.get(position).extend != null) {
        Glide.with(holder.itemView.getContext())
                .load(msg.get(position).extend)
                .override(1920, 1080) // 设置加载图片的大小
                .into(holder.ivThumb);
        } else {
            // 如果没有图片，则隐藏ivExtend控件
            holder.ivThumb.setVisibility(View.GONE);
        }

        // 根据位置设置tvRate的文字
        if (position == 2) {
            holder.tvRate.setText("点击快速搜索");
        } else if (position == 4) {
            holder.tvRate.setText("可右滑出更多");
        } else if (position == 6) {
            holder.tvRate.setText("点击搜索");
        } else  if (position == 8) {
            holder.tvRate.setText("可右滑出更多");
        }else  if (position == 10) {
            holder.tvRate.setText("点击进行搜索");
        }else {
            // 其他位置的文字可以根据需求进行设置
        }
        // 商品列表点击事件
        holder.group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(v.getContext(), "暂未开放", Toast.LENGTH_SHORT).show();
                //聚合搜索首页图
                Intent newIntent = new Intent( v.getContext(), FastSearchActivity.class);
                newIntent.putExtra("title", msg.get(position).name);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                v.getContext().startActivity(newIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return msg.size();
    }

    public List<AdvBean.MsgDTO> getMsg() {
        return msg;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout group;

        ImageView ivThumb;

        TextView tvRate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            group = itemView.findViewById(R.id.item_lbt);
            textView = itemView.findViewById(R.id.tvName);//名字
            ivThumb = itemView.findViewById(R.id.ivThumb);
            tvRate = itemView.findViewById(R.id.tvRate);


            // 控件获取焦点缩放特效
            group.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    AnimatorSet animatorSet = new AnimatorSet();
                    if (hasFocus) {
                        // 缩放动画
                        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(group, "scaleX", 1.0f, 1.04f);
                        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(group, "scaleY", 1.0f, 1.04f);
                        // 设置动画持续时间为300毫秒
                        scaleXAnimator.setDuration(300);
                        scaleYAnimator.setDuration(300);
                        // 设置弹跳效果的插值器
                        scaleXAnimator.setInterpolator(new BounceInterpolator());
                        scaleYAnimator.setInterpolator(new BounceInterpolator());
                        // 添加动画到动画集合中
                        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
                    } else {
                        // 缩放动画
                        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(group, "scaleX", 1.04f, 1.0f);
                        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(group, "scaleY", 1.04f, 1.0f);
                        // 设置动画持续时间为300毫秒
                        scaleXAnimator.setDuration(300);
                        scaleYAnimator.setDuration(300);
                        // 设置弹跳效果的插值器
                        scaleXAnimator.setInterpolator(new BounceInterpolator());
                        scaleYAnimator.setInterpolator(new BounceInterpolator());
                        // 添加动画到动画集合中
                        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
                    }
                    // 启动动画
                    animatorSet.start();
                }
            });


        }
    }


}