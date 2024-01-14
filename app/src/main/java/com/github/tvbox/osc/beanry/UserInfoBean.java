package com.github.tvbox.osc.beanry;

import com.google.gson.annotations.SerializedName;

public class UserInfoBean {

    /**
     * code : 200
     * msg : {"id":"10","pic":"http://124.222.84.216:8033/data/pic/0.png","user":"00:db:9a:23:07:c5","email":null,"phone":null,"name":"这个人没有名字!","vip":"1663925532","fen":"0","inv":"0","diary":"y","openid_wx":null,"openid_qq":null}
     * time : 1661248999
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("msg")
    public MsgDTO msg;
    @SerializedName("time")
    public Integer time;

    public static class MsgDTO {
        /**
         * id : 10
         * pic : http://124.222.84.216:8033/data/pic/0.png
         * user : 00:db:9a:23:07:c5
         * email : null
         * phone : null
         * name : 这个人没有名字!
         * vip : 1663925532
         * fen : 0
         * inv : 0
         * diary : y
         * openid_wx : null
         * openid_qq : null
         */

        @SerializedName("id")
        public String id;
        @SerializedName("pic")
        public String pic;
        @SerializedName("user")
        public String user;
        @SerializedName("email")
        public Object email;
        @SerializedName("phone")
        public Object phone;
        @SerializedName("name")
        public String name;
        @SerializedName("vip")
        public Integer vip;
        @SerializedName("fen")
        public Integer fen;
        @SerializedName("kam")
        public String kam;
        @SerializedName("inv")
        public String inv;
        @SerializedName("diary")
        public String diary;
        @SerializedName("openid_wx")
        public Object openidWx;
        @SerializedName("openid_qq")
        public Object openidQq;
    }
}
