package com.github.tvbox.osc.ui.adapter;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.tvbox.osc.R;
import com.github.tvbox.osc.bean.VodInfo;
import com.github.tvbox.osc.beanry.InitBean;
import com.github.tvbox.osc.util.MMkvUtils;
import com.github.tvbox.osc.util.ToolUtils;

import java.util.ArrayList;

/**
 * @author pj567
 * @date :2020/12/22
 * @description:
 */
public class SeriesFlagAdapter extends BaseQuickAdapter<VodInfo.VodSeriesFlag, BaseViewHolder> {
    public SeriesFlagAdapter() {
        super(R.layout.item_series_flag, new ArrayList<>());
    }

    private String setParseName(String flag){ //qq、qiyi
        //qq=腾讯视频,qiyi=爱奇艺,youku=优酷视频
        InitBean initBean = MMkvUtils.loadInitBean("");
        if (initBean != null && ToolUtils.getIsEmpty(initBean.msg.uiParseName)){
            if (initBean.msg.uiParseName.contains(flag)){ //是否为要替换的flag
                if (initBean.msg.uiParseName.contains("|")){ //是否存在多个
                    String[] s = initBean.msg.uiParseName.split("\\|");
                    for(String each : s) {
                        if (each.contains(flag)){
                            String[] a = each.split("=>");
                            return a[1];
                        }
                    }
                }else{
                    String[] a = initBean.msg.uiParseName.split("=>");
                    return a[1];
                }
            }
        }
        return flag;
    }

    @Override
    protected void convert(BaseViewHolder helper, VodInfo.VodSeriesFlag item) {
        TextView tvSeries = helper.getView(R.id.tvSeriesFlag);
        View select = helper.getView(R.id.tvSeriesFlagSelect);
        if (item.selected) {
            select.setVisibility(View.VISIBLE);
        } else {
            select.setVisibility(View.GONE);
        }
        Log.d(TAG, "convert: "+item.name);
        helper.setText(R.id.tvSeriesFlag, setParseName(item.name));
    }

    public int getCurrentPosition(){
        int position = 0;
        for(int i = 0; i < getData().size(); i++){
            if(getData().get(i).selected){
                position = i;
                break;
            }
        }
        return position;
    }
}