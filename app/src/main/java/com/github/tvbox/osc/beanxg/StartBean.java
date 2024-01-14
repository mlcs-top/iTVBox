package com.github.tvbox.osc.beanxg;

import com.google.gson.annotations.SerializedName;

public class StartBean {

    /**
     * code : 1
     * msg : app配置
     * data : {"video_jiexi":{"id":"itvbox_play","app_is_show":"1","parse_api":"https://www.x-n.cc/api.php?url=,https://svip.spchat.top/api/?key=dXX9y27Q5ODKjVQbFM&url=","user_agent":"","headers":"","link_features":"","un_link_features":""},"Manager_config":"4f1a854bcc8be4cf6e4ae7847875a41f","app_config":{"apijson":"https://json.fxkan.net/api.json","jxbgd":"upload/app/20220806-1/47d155e449c8b433d5cbfe571023cd71.png","kefu_qecode_url":"http://wpa.qq.com/msgrd?v=3&uin=qq号码&site=qq&menu=yes","Button_three_a":"http://oss0static.test.upcdn.net/ads/loadsir_bg.jpga","Button_three_ad":"http://oss0static.test.upcdn.net/ads/loadsir_bg.jpgad","user_qun":"暂无","logo_img_url":"全局LOGO","about_text":"iTVBox|软件仅供学习参考, 请于安装后24小时内删除。打包分发请保留出处https://github.com/CatVodTVOfficial/TVBoxOSC","feedback_url":"百度一下|http://baidu.com"}}
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public DataDTO data;

    public static class DataDTO {
        /**
         * video_jiexi : {"id":"itvbox_play","app_is_show":"1","parse_api":"https://www.x-n.cc/api.php?url=,https://svip.spchat.top/api/?key=dXX9y27Q5ODKjVQbFM&url=","user_agent":"","headers":"","link_features":"","un_link_features":""}
         * Manager_config : 4f1a854bcc8be4cf6e4ae7847875a41f
         * app_config : {"apijson":"https://json.fxkan.net/api.json","jxbgd":"upload/app/20220806-1/47d155e449c8b433d5cbfe571023cd71.png","kefu_qecode_url":"http://wpa.qq.com/msgrd?v=3&uin=qq号码&site=qq&menu=yes","Button_three_a":"http://oss0static.test.upcdn.net/ads/loadsir_bg.jpga","Button_three_ad":"http://oss0static.test.upcdn.net/ads/loadsir_bg.jpgad","user_qun":"暂无","logo_img_url":"全局LOGO","about_text":"iTVBox|软件仅供学习参考, 请于安装后24小时内删除。打包分发请保留出处https://github.com/CatVodTVOfficial/TVBoxOSC","feedback_url":"百度一下|http://baidu.com"}
         */

        @SerializedName("video_jiexi")
        public VideoJiexiDTO videoJiexi;
        @SerializedName("Manager_config")
        public String ManagerConfig;
        @SerializedName("app_config")
        public AppConfigDTO appConfig;

        public static class VideoJiexiDTO {
            /**
             * id : itvbox_play
             * app_is_show : 1
             * parse_api : https://www.x-n.cc/api.php?url=,https://svip.spchat.top/api/?key=dXX9y27Q5ODKjVQbFM&url=
             * user_agent :
             * headers :
             * link_features :
             * un_link_features :
             */

            @SerializedName("id")
            public String id;
            @SerializedName("app_is_show")
            public String appIsShow;
            @SerializedName("parse_api")
            public String parseApi;
            @SerializedName("user_agent")
            public String userAgent;
            @SerializedName("headers")
            public String headers;
            @SerializedName("link_features")
            public String linkFeatures;
            @SerializedName("un_link_features")
            public String unLinkFeatures;
        }

        public static class AppConfigDTO {
            /**
             * apijson : https://json.fxkan.net/api.json
             * jxbgd : upload/app/20220806-1/47d155e449c8b433d5cbfe571023cd71.png
             * kefu_qecode_url : http://wpa.qq.com/msgrd?v=3&uin=qq号码&site=qq&menu=yes
             * Button_three_a : http://oss0static.test.upcdn.net/ads/loadsir_bg.jpga
             * Button_three_ad : http://oss0static.test.upcdn.net/ads/loadsir_bg.jpgad
             * user_qun : 暂无
             * logo_img_url : 全局LOGO
             * about_text : iTVBox|软件仅供学习参考, 请于安装后24小时内删除。打包分发请保留出处https://github.com/CatVodTVOfficial/TVBoxOSC
             * feedback_url : 百度一下|http://baidu.com
             * home_data:
             * auth_type:
             * parse_name:
             */

            @SerializedName("apijson")
            public String apijson;
            @SerializedName("apijson2")
            public String apijson2;
            @SerializedName("jxbgd")
            public String jxbgd;
            @SerializedName("kefu_qecode_url")
            public String kefuQecodeUrl;
            @SerializedName("Button_three_a")
            public String ButtonThreeA;
            @SerializedName("Button_three_ad")
            public String ButtonThreeAd;
            @SerializedName("user_qun")
            public String userQun;
            @SerializedName("logo_img_url")
            public String logoImgUrl;
            @SerializedName("about_text")
            public String aboutText;
            @SerializedName("feedback_url")
            public String feedbackUrl;
            @SerializedName("home_data")
            public int homeData;
            @SerializedName("auth_type")
            public int authType;
            @SerializedName("parse_name")
            public String parseName;
            @SerializedName("class_hidden")
            public String classHidden;
            @SerializedName("piay_source")
            public String playSource;
            @SerializedName("online_payment")
            public int OnlinePayment;
            @SerializedName("card_url")
            public String CardUrl;
            @SerializedName("remove_parses")
            public String removeParses;
            @SerializedName("remove_vodapi")
            public String removeVodApi;
        }
    }
}
