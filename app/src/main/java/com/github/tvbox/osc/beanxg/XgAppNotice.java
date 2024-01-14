package com.github.tvbox.osc.beanxg;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class XgAppNotice implements Serializable {

    /**
     * code : 1
     * msg : 公告列表
     * data : [{"id":3,"title":"首页公告","intro":"ITVBox是tvbox开源版二开而来，在关于软件处可获得tvbox开源版","create_time":"2022-08-06 14:26","is_top":0,"content":"","detail_create_time":"2022-08-06 14:26:06","content_empty":0}]
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public List<DataDTO> data;

    public static class DataDTO {
        /**
         * id : 3
         * title : 首页公告
         * intro : ITVBox是tvbox开源版二开而来，在关于软件处可获得tvbox开源版
         * create_time : 2022-08-06 14:26
         * is_top : 0
         * content :
         * detail_create_time : 2022-08-06 14:26:06
         * content_empty : 0
         */

        @SerializedName("id")
        public Integer id;
        @SerializedName("title")
        public String title;
        @SerializedName("intro")
        public String intro;
        @SerializedName("create_time")
        public String createTime;
        @SerializedName("is_top")
        public Integer isTop;
        @SerializedName("content")
        public String content;
        @SerializedName("detail_create_time")
        public String detailCreateTime;
        @SerializedName("content_empty")
        public Integer contentEmpty;
    }
}
