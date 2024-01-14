package com.github.tvbox.osc.beanry;

import com.google.gson.annotations.SerializedName;

public class ReUserBean {

    /**
     * code : 200
     * msg : {"token":"ec7f0e4c29cc91a523608ebafc7eef97","info":{"id":1,"pic":"http://17.u.com/data/pic/0.png","name":"这个人没有名字!","vip":1582112376,"fen":100}}
     * time : 1582107375
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("msg")
    public MsgDTO msg;
    @SerializedName("time")
    public Integer time;

    public static class MsgDTO {
        /**
         * token : ec7f0e4c29cc91a523608ebafc7eef97
         * info : {"id":1,"pic":"http://17.u.com/data/pic/0.png","name":"这个人没有名字!","vip":1582112376,"fen":100}
         */

        @SerializedName("token")
        public String token;
        @SerializedName("info")
        public InfoDTO info;

        public static class InfoDTO {
            /**
             * id : 1
             * pic : http://17.u.com/data/pic/0.png
             * name : 这个人没有名字!
             * vip : 1582112376
             * fen : 100
             */

            @SerializedName("id")
            public Integer id;
            @SerializedName("pic")
            public String pic;
            @SerializedName("name")
            public String name;
            @SerializedName("vip")
            public Integer vip;
            @SerializedName("fen")
            public Integer fen;
        }
    }
}
