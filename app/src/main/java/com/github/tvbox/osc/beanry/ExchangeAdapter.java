
package com.github.tvbox.osc.beanry;

import static com.github.tvbox.osc.server.ControlManager.mContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.ui.activity.FastSearchActivity;
import com.github.tvbox.osc.util.BaseR;
import com.github.tvbox.osc.util.MMkvUtils;
import com.github.tvbox.osc.util.ToolUtils;
import com.github.tvbox.quickjs.JSObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.animation.BounceInterpolator;


public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.ViewHolder> {

    private List<ExchangeBean.MsgDTO> msg;


    public ExchangeAdapter(List<ExchangeBean.MsgDTO> msg) {
        this.msg = msg;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_shop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textView.setText(msg.get(position).name);
        //holder.fenNumTextView.setText(msg.get(position).fen_num + " 积分");//积分
        holder.fenNumTextView.setText(Math.abs(Integer.parseInt(msg.get(position).fen_num)) + " 积分");
        //holder.jifensj.setText(msg.get(position).vip_num);//增加会员时间
        holder.group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //商品列表点击事件
              //  Toast.makeText(v.getContext(), "兑换功能暂未开放", Toast.LENGTH_SHORT).show();

                ReUserBean userBean = MMkvUtils.loadReUserBean("");
                if (userBean != null && ToolUtils.getIsEmpty(userBean.msg.token)) {
                   // ToolUtils.HomeDialog(v.getContext(), userBean.msg.token);
                      recHarGe(userBean.msg.token,msg.get(position).id);
                } else {
                    Toast.makeText(v.getContext(), "请先登录哦", Toast.LENGTH_SHORT).show();
                }


            }

        });
    }

    @Override
    public int getItemCount() {
        return msg.size();
    }

    public List<ExchangeBean.MsgDTO> getMsg() {
        return msg;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView fenNumTextView;
        TextView jifensj;

        TextView jifenid;

        TextView jfappid;
        LinearLayout group;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            group = itemView.findViewById(R.id.item_user_shop_group);
            textView = itemView.findViewById(R.id.tv_shop_title);//名字
            fenNumTextView = itemView.findViewById(R.id.tv_shop_price);//积分

            // 控件获取焦点缩放特效
            group.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    AnimatorSet animatorSet = new AnimatorSet();
                    if (hasFocus) {
                        // 缩放动画
                        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(group, "scaleX", 1.0f, 1.05f);
                        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(group, "scaleY", 1.0f, 1.05f);
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
                        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(group, "scaleX", 1.05f, 1.0f);
                        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(group, "scaleY", 1.05f, 1.0f);
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



 //积分兑换茶茶QQ205888578
    private void recHarGe(String token, String id) {
        Log.d("token", "recHarGe: " + token);
        Log.d("id", "recHarGe: " + id);
        new Thread(() -> {
            OkGo.<String>post(ToolUtils.setApi("get_fen"))
                    .params("token", token)
                    .params("fid", id)  // 将fid修改为传入的id值
                    .params("t", "")  //
                    .params("sign", ToolUtils.setSign("token=" + token + "&fid"+id ))
                    .execute(new AbsCallback<String>() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                JSONObject jo = new JSONObject(BaseR.decry_R(response.body()));
                                if (jo.getInt("code") == 200) {
                                    ToolUtils.showToast(mContext, "兑换成功", R.drawable.toast_smile);
                                }else{
                                    ToolUtils.showToast(mContext,  jo.getString("msg"), R.drawable.toast_err);
                                }


                            } catch (JSONException e) {
                               // ToolUtils.showToast(mContext, e.toString(), R.drawable.toast_err);
                                e.printStackTrace();
                            }

                        }


                        @Override
                        public void onError(Response<String> error) {
                           // ToolUtils.showToast(mContext, BaseR.decry_R(error.body()), R.drawable.toast_err);
                        }

                        @Override
                        public String convertResponse(okhttp3.Response response) throws Throwable {
                            assert response.body() != null;
                            return response.body().string();
                        }
                    });
        }).start();
    }



}