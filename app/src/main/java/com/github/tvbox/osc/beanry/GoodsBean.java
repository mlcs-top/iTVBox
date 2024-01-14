package com.github.tvbox.osc.beanry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoodsBean {

    /**
     * code : 200
     * msg : [{"gid":"1","gname":"会员一天","gmoney":"1.00","gtype":"vip","obtain":"1","cv":""},{"gid":"2","gname":"积分一百","gmoney":"10.00","gtype":"fen","obtain":"100","cv":""}]
     * time : 1582360629
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("time")
    public Integer time;
    @SerializedName("msg")
    public List<MsgDTO> msg;

    public static class MsgDTO {
        /**
         * gid : 1
         * gname : 会员一天
         * gmoney : 1.00
         * gtype : vip
         * obtain : 1
         * cv :
         */

        @SerializedName("gid")
        public Integer gid;
        @SerializedName("gname")
        public String gname;
        @SerializedName("gmoney")
        public Integer gmoney;
        @SerializedName("gtype")
        public String gtype;
        @SerializedName("obtain")
        public String obtain;
        @SerializedName("cv")
        public String cv;
    }
}
