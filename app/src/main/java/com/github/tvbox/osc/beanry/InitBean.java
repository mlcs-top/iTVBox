package com.github.tvbox.osc.beanry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InitBean {

    /**
     * code : 200
     * msg : {"app_bb":"6.01","app_nshow":"测试更新","app_nurl":"http://oss0static.test.upcdn.net/static/TVBoxOS/release/android_1.0.22.apk","ui_mode":"y","ui_state":"y","ui_logo":"http://oss0static.test.upcdn.net/ads/sm_logo.png","ui_startad":"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fww1.sinaimg.cn%2Fmw690%2F647fb278ly8h45jg16nukj21hc0u0qb8.jpg&refer=http%3A%2F%2Fwww.sina.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661600801&t=313c3ae9218927ee17d8302160c44eae","kami_url":"http://124.222.84.216:8033/notify.php","app_json":"https://oss.lvdoui.net/static/TVBoxOS/static/api.json","app_jsonb":"https://oss.lvdoui.net/static/TVBoxOS/static/api.json","logon_way":"2","ui_paybackg":"http://124.222.84.216:8086/upload/app/20220808-1/66a979c9669f30b3625d32b09df3d6b7.jpg","ui_kefu":"http://wpa.qq.com/msgrd?v=3&uin=592805093&site=qq&menu=yes","ui_group":"888888888","ui_button3backg":"https://oss.lvdoui.net/ads/button3.png","ui_buttonadimg":"http://oss0static.test.upcdn.net/ads/weixin2.png","ui_community":"社区留言|http://lvdoui.net","ui_removersc":"","ui_remove_parses":"故人|Web解析|rr解析","ui_remove_class":"川菜|粤菜","ui_parse_name":"qq=>腾讯视频|qiyi=>爱奇艺|youku=>优酷视频|ppayun=>七七|wasu=>华数|hnm3u8=>红牛","app_about":"iTVBox|是TVBox二开版本，新增会员系统及调整部分UI，感谢TVBox作者开放此项目用于学习交流！本软件仅供学习参考,请于安装后24小时内删除。TVBox仓库：https://github.com/CatVodTVOfficial/TVBoxOSC","exten":[{"id":"4","name":"乐看_level9","data":"战狼|https://puui.qpic.cn/media_img/lena/PIChxhpn4_580_1680/0","appid":"10000"},{"id":"1","name":"itvbox_level9","data":"战狼|https://puui.qpic.cn/media_img/lena/PIChxhpn4_580_1680/0","appid":"10000"}],"pay":{"state":"y","url":"http://epay.lvdoui.net/","appid":"1000","appkey":"eo0f6XE1XXM2fgLMMEQePxl7ZNqX0097","ali":"y","wx":"y","qq":"y"}}
     * time : 1661273290
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("msg")
    public MsgDTO msg;
    @SerializedName("time")
    public Integer time;

    public static class MsgDTO {
        /**
         * app_bb : 6.01
         * app_nshow : 测试更新
         * app_nurl : http://oss0static.test.upcdn.net/static/TVBoxOS/release/android_1.0.22.apk
         * ui_mode : y
         * ui_state : y
         * ui_logo : http://oss0static.test.upcdn.net/ads/sm_logo.png
         * ui_startad : https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fww1.sinaimg.cn%2Fmw690%2F647fb278ly8h45jg16nukj21hc0u0qb8.jpg&refer=http%3A%2F%2Fwww.sina.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661600801&t=313c3ae9218927ee17d8302160c44eae
         * kami_url : http://124.222.84.216:8033/notify.php
         * app_json : https://oss.lvdoui.net/static/TVBoxOS/static/api.json
         * app_jsonb : https://oss.lvdoui.net/static/TVBoxOS/static/api.json
         * logon_way : 2
         * ui_paybackg : http://124.222.84.216:8086/upload/app/20220808-1/66a979c9669f30b3625d32b09df3d6b7.jpg
         * ui_kefu : http://wpa.qq.com/msgrd?v=3&uin=592805093&site=qq&menu=yes
         * ui_group : 888888888
         * ui_button3backg : https://oss.lvdoui.net/ads/button3.png
         * ui_buttonadimg : http://oss0static.test.upcdn.net/ads/weixin2.png
         * ui_community : 社区留言|http://lvdoui.net
         * ui_removersc :
         * ui_remove_parses : 故人|Web解析|rr解析
         * ui_remove_class : 川菜|粤菜
         * ui_parse_name : qq=>腾讯视频|qiyi=>爱奇艺|youku=>优酷视频|ppayun=>七七|wasu=>华数|hnm3u8=>红牛
         * app_about : iTVBox|是TVBox二开版本，新增会员系统及调整部分UI，感谢TVBox作者开放此项目用于学习交流！本软件仅供学习参考,请于安装后24小时内删除。TVBox仓库：https://github.com/CatVodTVOfficial/TVBoxOSC
         * exten : [{"id":"4","name":"乐看_level9","data":"战狼|https://puui.qpic.cn/media_img/lena/PIChxhpn4_580_1680/0","appid":"10000"},{"id":"1","name":"itvbox_level9","data":"战狼|https://puui.qpic.cn/media_img/lena/PIChxhpn4_580_1680/0","appid":"10000"}]
         * pay : {"state":"y","url":"http://epay.lvdoui.net/","appid":"1000","appkey":"eo0f6XE1XXM2fgLMMEQePxl7ZNqX0097","ali":"y","wx":"y","qq":"y"}
         */

        @SerializedName("app_bb")
        public String appBb;
        @SerializedName("app_nshow")
        public String appNshow;
        @SerializedName("app_nurl")
        public String appNurl;
        @SerializedName("mode")
        public String uiMode;
        @SerializedName("ui_state")
        public String uiState;
        @SerializedName("ui_logo")
        public String uiLogo;
        @SerializedName("ui_startad")
        public String uiStartad;
        @SerializedName("kami_url")
        public String kamiUrl;
        @SerializedName("app_json")
        public String appJson;
        @SerializedName("app_jsonb")
        public String appJsonb;
        @SerializedName("app_jsonc")
        public String appJsonc;
        @SerializedName("app_huodong")
        public String appHuoDong;
        @SerializedName("logon_way")
        public String logonWay;
        @SerializedName("ui_paybackg")
        public String uiPaybackg;
        @SerializedName("ui_kefu")
        public String uiKefu;
        @SerializedName("ui_group")
        public String uiGroup;
        @SerializedName("ui_kefu2")
        public String uiKefu2;
        @SerializedName("ui_button3backg")
        public String uiButton3backg;
        @SerializedName("ui_buttonadimg")
        public String uiButtonadimg;
        @SerializedName("ui_button3backg2")
        public String uiButton3backg2;
        @SerializedName("ui_button3backg3")
        public String uiButton3backg3;
        @SerializedName("ui_community")
        public String uiCommunity;
        @SerializedName("ui_buttonadimg3")
        public String uiButtonadimg3;
        @SerializedName("ui_removersc")
        public String uiRemoversc;
        @SerializedName("ui_remove_parses")
        public String uiRemoveParses;
        @SerializedName("ui_remove_class")
        public String uiRemoveClass;
        @SerializedName("ui_parse_name")
        public String uiParseName;
        @SerializedName("app_about")
        public String appAbout;
        @SerializedName("pay")
        public PayDTO pay;
        @SerializedName("exten")
        public List<ExtenDTO> exten;

        public static class PayDTO {
            /**
             * state : y
             * url : http://epay.lvdoui.net/
             * appid : 1000
             * appkey : eo0f6XE1XXM2fgLMMEQePxl7ZNqX0097
             * ali : y
             * wx : y
             * qq : y
             */

            @SerializedName("state")
            public String state;
            @SerializedName("url")
            public String url;
            @SerializedName("appid")
            public String appid;
            @SerializedName("appkey")
            public String appkey;
            @SerializedName("ali")
            public String ali;
            @SerializedName("wx")
            public String wx;
            @SerializedName("qq")
            public String qq;
        }

        public static class ExtenDTO {
            /**
             * id : 4
             * name : 乐看
             * data : https://puui.qpic.cn/media_img/lena/PIChxhpn4_580_1680/0
             * appid : 10000
             */

            @SerializedName("id")
            public String id;
            @SerializedName("name")
            public String name;
            @SerializedName("data")
            public String data;
            @SerializedName("appid")
            public String appid;
        }
    }
}
