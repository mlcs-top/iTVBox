package com.github.tvbox.osc.beanry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExchangeBean {

    /**
     * code : 200
     * msg : [{"content":"为了更好了为大家提供服务，APP将于2020-3-3 23:40进行维护升级，维护时间预计4小时","date":"2020-03-03 16:05:21","name":"管理员"},{"content":"请大家严格遵守APP协议，严禁发布广告、低俗、歧视、政治等信息。举报QQ：51154393","date":"2020-03-01 12:23:05","name":"管理员"}]
     * time : 1582107375
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("time")
    public Integer time;
    @SerializedName("msg")
    public List<MsgDTO> msg;

    public static class MsgDTO {
        /**
         * "id": "1",
         * "appid": "10000",
         * "name": "VIP天卡",
         * "fen_num": "-50",
         * "vip_num": "24",
         * "state": "y"
         */

        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;

        @SerializedName("appid")
        public String appid;
        @SerializedName("fen_num")
        public String fen_num;

        @SerializedName("vip_num")
        public String vip_num;

        @SerializedName("state")
        public String state;
    }
}
