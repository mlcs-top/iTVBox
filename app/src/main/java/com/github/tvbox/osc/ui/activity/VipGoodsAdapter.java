package com.github.tvbox.osc.ui.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.github.tvbox.osc.R;
import com.github.tvbox.osc.bean.VipGoodsData;
import com.github.tvbox.osc.util.ToolUtils;

import java.util.List;

public class VipGoodsAdapter extends RecyclerView.Adapter<VipGoodsAdapter.ViewHolder>{

    private Context context;
    private OnClickListener clickListener;
    private List<VipGoodsData> vipGoodsData;

    public VipGoodsAdapter(Context context, OnClickListener clickListener, List<VipGoodsData> vipGoodsData){
        this.context = context;
        this.clickListener = clickListener;
        this.vipGoodsData = vipGoodsData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vip_goods_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.vip_goods_des.setText(vipGoodsData.get(position).getDes());
        holder.vip_goods_des_coen.setText(vipGoodsData.get(position).getDesS());
        holder.vip_goods_price.setText(String.valueOf(vipGoodsData.get(position).getPrice()));
        if (ToolUtils.getIsEmpty(vipGoodsData.get(position).getDesSS())){
            holder.vip_goods_price_company.setText(vipGoodsData.get(position).getDesSS());
        }else{
            holder.vip_goods_price_company.setText("元");
        }
    }

    @Override
    public int getItemCount() {
        return vipGoodsData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView vip_goods_price,vip_goods_price_company;
        TextView vip_goods_des, vip_goods_des_coen;
        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onGoodsItemClick(getAdapterPosition());
            }
        };
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vip_goods_des = itemView.findViewById(R.id.vip_goods_des);
            vip_goods_price = itemView.findViewById(R.id.vip_goods_price);
            vip_goods_des_coen = itemView.findViewById(R.id.vip_goods_des_coen);
            vip_goods_price_company = itemView.findViewById(R.id.vip_goods_price_company);
            itemView.setOnClickListener(onClickListener);
        }
    }

    public interface OnClickListener {
        void onGoodsItemClick(int position);
    }
}
