package com.github.tvbox.osc.beanxg;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VodApiJson {

    /**
     * code : 1
     * msg : 公告列表
     * data : [{"id":1,"title":"测试站点","intro":"csp_AppYsV2","sort":3,"status":1,"filterable":1,"searchable":1,"quickSearch":1,"playerurl":"","vodapi":"http://124.222.84.216:8086/api.php/app/"},{"id":8,"title":"绿豆视频","intro":"csp_AppYsV2","sort":3,"status":1,"filterable":1,"searchable":1,"quickSearch":1,"playerurl":"","vodapi":"http://124.222.84.216:8086/kofhwysmno.php/admin/xgapp"}]
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public List<DataDTO> data;

    public static class DataDTO {
        /**
         * id : 1
         * title : 测试站点
         * intro : csp_AppYsV2
         * sort : 3
         * status : 1
         * filterable : 1
         * searchable : 1
         * quickSearch : 1
         * playerurl :
         * vodapi : http://124.222.84.216:8086/api.php/app/
         */

        @SerializedName("id")
        public Integer id;
        @SerializedName("title")
        public String title;
        @SerializedName("intro")
        public String intro;
        @SerializedName("sort")
        public Integer sort;
        @SerializedName("status")
        public Integer status;
        @SerializedName("filterable")
        public Integer filterable;
        @SerializedName("searchable")
        public Integer searchable;
        @SerializedName("quickSearch")
        public Integer quickSearch;
        @SerializedName("playerurl")
        public String playerurl;
        @SerializedName("vodapi")
        public String vodapi;
    }
}
