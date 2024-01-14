package com.github.tvbox.osc.beanry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReJieXiBean {

    /**
     * code : 200
     * msg : [{"name":"绿豆解析","type":1,"url":"http://124.222.84.216:8033/api.php?url=","ext":"{\"flag\":[\"youku\",\"优酷\",\"mgtv\",\"芒果\",\"qq\",\"腾讯\",\"qiyi\",\"爱奇艺\",\"qq\",\"letv\",\"奇艺\",\"ddzy\",\"人人\"]}"},{"name":"绿豆的啊","type":1,"url":"http://124.222.84.216:8033/?url=","ext":"{\"flag\":[\"youku\",\"优酷\",\"mgtv\",\"芒果\",\"qq\",\"腾讯\",\"qiyi\",\"爱奇艺\",\"qq\",\"letv\",\"奇艺\",\"ddzy\",\"人人\"]}"}]
     * time : 1661270280
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("time")
    public Integer time;
    @SerializedName("msg")
    public List<MsgDTO> msg;

    public static class MsgDTO {
        /**
         * name : 绿豆解析
         * type : 1
         * url : http://124.222.84.216:8033/api.php?url=
         * ext : {"flag":["youku","优酷","mgtv","芒果","qq","腾讯","qiyi","爱奇艺","qq","letv","奇艺","ddzy","人人"]}
         */

        @SerializedName("name")
        public String name;
        @SerializedName("type")
        public Integer type;
        @SerializedName("url")
        public String url;
        @SerializedName("ext")
        public String ext;
    }
}
