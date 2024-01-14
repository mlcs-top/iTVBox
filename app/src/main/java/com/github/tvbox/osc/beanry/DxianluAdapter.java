package com.github.tvbox.osc.beanry;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.cache.CacheData;
import com.github.tvbox.osc.event.RefreshEvent;
import com.github.tvbox.osc.ui.activity.DxianluActivity;
import com.github.tvbox.osc.ui.activity.UserActivity;
import com.github.tvbox.osc.ui.adapter.SearchSubtitleAdapter;
import com.github.tvbox.osc.util.FastClickCheckUtil;
import com.github.tvbox.osc.util.HawkConfig;
import com.github.tvbox.osc.util.MMkvUtils;
import com.github.tvbox.osc.util.ToolUtils;
import com.github.tvbox.osc.viewmodel.SubtitleViewModel;
import com.orhanobut.hawk.Hawk;
import com.github.tvbox.osc.api.ApiConfig;
import com.github.tvbox.osc.ui.activity.StartActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DxianluAdapter extends RecyclerView.Adapter<DxianluAdapter.ViewHolder> {


    private List<DxianluBean.MsgDTO> storeHouse;




    public DxianluAdapter(List<DxianluBean.MsgDTO> storeHouse){
        this.storeHouse = storeHouse;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dxianlu,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,@SuppressLint("RecyclerView") int position) {
        String sourceName = storeHouse.get(position).sourceName;
        String selectName=CacheData.getCacheString("sourceName");//获取选择的线路名称
        if(Objects.equals(sourceName, selectName)){
            sourceName+=" (正在使用)";
        }
        holder.textView.setText(sourceName);


        holder.group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //多线路点击事件
                FastClickCheckUtil.check(v);
              //  Hawk.put(HawkConfig.HOME_API, "");
                String inputApi = storeHouse.get(position).sourceUrl;
                CacheData.CacheString("sourceUrl",inputApi);//存储线路URL
                CacheData.CacheString("sourceName",storeHouse.get(position).sourceName);//存储线路名称
                Hawk.put(HawkConfig.API_URL2, inputApi);
               // Hawk.put(HawkConfig.API_URL, apiJsonc);
                Log.d("TAG", "loadConfig2: "+Hawk.get(HawkConfig.API_URL2, ""));//移植代码

                //弹窗提示
                //Toast.makeText(v.getContext(), storeHouse.get(position).sourceUrl, Toast.LENGTH_SHORT).show();


                // 重启应用
                Intent intent = v.getContext().getPackageManager().getLaunchIntentForPackage(v.getContext().getPackageName());
                if (intent != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                    // 销毁当前的Activity
                    ((DxianluActivity) v.getContext()).finish();
                    // 杀死当前进程
                    System.exit(0);
                }


            }

        });

    }

    @Override
    public int getItemCount() {
        return storeHouse.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        LinearLayout group;

        TextView tv_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            group = itemView.findViewById(R.id.item_dxianlu);
            textView = itemView.findViewById(R.id.item_dxianlu_text);

            tv_status=itemView.findViewById(R.id.tv_status);



        }
    }

}
