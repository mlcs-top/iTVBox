package com.github.tvbox.osc.beanry;

import com.google.gson.annotations.SerializedName;

public class KamInfoBean {

    /**
     * code : 200
     * msg : {"id":"5","appid":"10000","kami":"8469119959","type":"vip","amount":"1","note":"1","user":null,"use_time":"0","end_time":"0","new":"n","state":"y"}
     * time : 1662432676
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("msg")
    public MsgDTO msg;
    @SerializedName("time")
    public Integer time;

    public static class MsgDTO {
        /**
         * id : 5
         * appid : 10000
         * kami : 8469119959
         * type : vip
         * amount : 1
         * note : 1
         * user : null
         * use_time : 0
         * end_time : 0
         * new : n
         * state : y
         */

        @SerializedName("id")
        public String id;
        @SerializedName("appid")
        public String appid;
        @SerializedName("kami")
        public String kami;
        @SerializedName("type")
        public String type;
        @SerializedName("amount")
        public String amount;
        @SerializedName("note")
        public String note;
        @SerializedName("user")
        public String user;
        @SerializedName("use_time")
        public String useTime;
        @SerializedName("end_time")
        public String endTime;
        @SerializedName("new")
        public String newX;
        @SerializedName("state")
        public String state;
    }
}
