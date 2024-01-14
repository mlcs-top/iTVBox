package com.github.tvbox.osc.beanxg;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class XgAdBean {

    /**
     * code : 1
     * msg : 广告列表
     * data : [{"id":2,"name":"启动配置","content":"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fww1.sinaimg.cn%2Fmw690%2F647fb278ly8h45jg16nukj21hc0u0qb8.jpg&refer=http%3A%2F%2Fwww.sina.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661600801&t=313c3ae9218927ee17d8302160c44eae","req_type":2,"req_content":"https://json.fxkan.net/api.json","headers":"","time":5,"skip_time":0}]
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public List<DataDTO> data;

    public static class DataDTO {
        /**
         * id : 2
         * name : 启动配置
         * content : https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fww1.sinaimg.cn%2Fmw690%2F647fb278ly8h45jg16nukj21hc0u0qb8.jpg&refer=http%3A%2F%2Fwww.sina.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661600801&t=313c3ae9218927ee17d8302160c44eae
         * req_type : 2
         * req_content : https://json.fxkan.net/api.json
         * headers :
         * time : 5
         * skip_time : 0
         */

        @SerializedName("id")
        public Integer id;
        @SerializedName("name")
        public String name;
        @SerializedName("content")
        public String content;
        @SerializedName("req_type")
        public Integer reqType;
        @SerializedName("req_content")
        public String reqContent;
        @SerializedName("headers")
        public String headers;
        @SerializedName("time")
        public Integer time;
        @SerializedName("skip_time")
        public Integer skipTime;
    }
}
