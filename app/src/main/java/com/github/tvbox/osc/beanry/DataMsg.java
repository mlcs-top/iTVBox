package com.github.tvbox.osc.beanry;

import com.google.gson.annotations.SerializedName;

public class DataMsg {

    /**
     * code : 200
     * msg  : 加密数据
     * time : 1661416326
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("time")
    public Integer time;
}
