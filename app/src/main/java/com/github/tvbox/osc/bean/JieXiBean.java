package com.github.tvbox.osc.bean;

import com.google.gson.annotations.SerializedName;

public class JieXiBean {

    /**
     * code : 1
     * msg : 解析接口
     * data : {"xg_app_player":{"parse_api":"https://www.x-n.cc/api.php?url=,https://www.x-n.cc/api.php?url=","link_features":".mp4,.m3u8,.flv,.avi,.mov,.rmvb,alizyw.com","un_link_features":"","id":"xg_app_player","app_is_show":"1","user_agent":"","headers":""},"hnm3u8":{"id":"hnm3u8","app_is_show":"1","parse_api":"","user_agent":"","headers":"","link_features":"","un_link_features":""},"qiyi":{"id":"qiyi","app_is_show":"1","parse_api":"","user_agent":"","headers":"","link_features":"","un_link_features":""},"youku":{"id":"youku","app_is_show":"1","parse_api":"","user_agent":"","headers":"","link_features":"","un_link_features":""},"qq":{"id":"qq","app_is_show":"1","parse_api":"","user_agent":"","headers":"","link_features":"","un_link_features":""}}
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public DataDTO data;

    public static class DataDTO {
        /**
         * xg_app_player : {"parse_api":"https://www.x-n.cc/api.php?url=,https://www.x-n.cc/api.php?url=","link_features":".mp4,.m3u8,.flv,.avi,.mov,.rmvb,alizyw.com","un_link_features":"","id":"xg_app_player","app_is_show":"1","user_agent":"","headers":""}
         * hnm3u8 : {"id":"hnm3u8","app_is_show":"1","parse_api":"","user_agent":"","headers":"","link_features":"","un_link_features":""}
         * qiyi : {"id":"qiyi","app_is_show":"1","parse_api":"","user_agent":"","headers":"","link_features":"","un_link_features":""}
         * youku : {"id":"youku","app_is_show":"1","parse_api":"","user_agent":"","headers":"","link_features":"","un_link_features":""}
         * qq : {"id":"qq","app_is_show":"1","parse_api":"","user_agent":"","headers":"","link_features":"","un_link_features":""}
         */

        @SerializedName("xg_app_player")
        public XgAppPlayerDTO xgAppPlayer;

        public static class XgAppPlayerDTO {
            /**
             * parse_api : https://www.x-n.cc/api.php?url=,https://www.x-n.cc/api.php?url=
             * link_features : .mp4,.m3u8,.flv,.avi,.mov,.rmvb,alizyw.com
             * un_link_features :
             * id : xg_app_player
             * app_is_show : 1
             * user_agent :
             * headers :
             */

            @SerializedName("parse_api")
            public String parseApi;
            @SerializedName("link_features")
            public String linkFeatures;
            @SerializedName("un_link_features")
            public String unLinkFeatures;
            @SerializedName("id")
            public String id;
            @SerializedName("app_is_show")
            public String appIsShow;
            @SerializedName("user_agent")
            public String userAgent;
            @SerializedName("headers")
            public String headers;
        }
    }
}
