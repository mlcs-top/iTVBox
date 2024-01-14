package com.github.tvbox.osc.beanxg;

import com.google.gson.annotations.SerializedName;

public class XgAppVersionBean {

    /**
     * code : 1
     * msg : 最新版本
     * data : {"id":1,"title":"tvbox","desc":" 1、播放时无操作隐藏底部TAB\n 2、新增会员系统\n 3、自动注册、登录(取设备MAC地址)\n 4、新增卡密充值\n 5、新增码支付在线购买套餐\n 6、搜索页默认缩略图\n 7、新增开屏广告(OK键可跳过)\n 8、新增首页公告\n 9、无操作进入屏保(全屏幻灯)\n 10、非会员试看6分钟\n 11、解析背景图及解析时动画\n 12、对接聚合API接口同时调用自有苹果CMS数据\n 13、安卓、神马、数据影视及会员互通\n 14、用户在线购买会员时可用积分抵扣金额\n 15、新增版本更新\n PS：新增后台管理，以上功能均可在后台配置","code":"1.0.1","type":"tvbox","url":"http://oss0static.test.upcdn.net/upgrade/TVbox_1.0.1_101_jiagu_sign.apk","force":0}
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public DataDTO data;

    public static class DataDTO {
        /**
         * id : 1
         * title : tvbox
         * desc :  更新说明
         * code : 1.0.1
         * type : tvbox
         * url : http://oss0static.test.upcdn.net/upgrade/TVbox_1.0.1_101_jiagu_sign.apk
         * force : 0
         */

        @SerializedName("id")
        public Integer id;
        @SerializedName("title")
        public String title;
        @SerializedName("desc")
        public String desc;
        @SerializedName("code")
        public String code;
        @SerializedName("type")
        public String type;
        @SerializedName("url")
        public String url;
        @SerializedName("force")
        public Integer force;
    }
}
