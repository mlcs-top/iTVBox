package com.github.tvbox.osc.beanry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SiteBean {

    /**
     * code : 200
     * msg : [{"gid":"1","gname":"绿豆视频","gtype":"Spider","gapiname":"csp_AppYsV2","obtain":"http://124.222.84.216:8033/1","parse":"http://124.222.84.216:8033/?url=","searchable":"1","quicksearch":"1","filterable":"1"}]
     * time : 1661187327
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
         * gname : 绿豆视频
         * gtype : Spider
         * gapiname : csp_AppYsV2
         * obtain : http://124.222.84.216:8033/1
         * parse : http://124.222.84.216:8033/?url=
         * searchable : 1
         * quicksearch : 1
         * filterable : 1
         */

        @SerializedName("gid")
        public String gid;
        @SerializedName("gname")
        public String gname;
        @SerializedName("gtype")
        public int type;
        @SerializedName("gapiname")
        public String gapiname;
        @SerializedName("extend")
        public String extend;
        @SerializedName("parse")
        public String parse;
        @SerializedName("searchable")
        public int searchable;
        @SerializedName("quicksearch")
        public int quicksearch;
        @SerializedName("filterable")
        public int filterable;
    }
}
