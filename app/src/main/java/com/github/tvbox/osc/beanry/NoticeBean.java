package com.github.tvbox.osc.beanry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NoticeBean {

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
         * content : 为了更好了为大家提供服务，APP将于2020-3-3 23:40进行维护升级，维护时间预计4小时
         * date : 2020-03-03 16:05:21
         * name : 管理员
         */

        @SerializedName("content")
        public String content;
        @SerializedName("date")
        public String date;
        @SerializedName("name")
        public String name;
    }
}
