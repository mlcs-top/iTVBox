package com.github.tvbox.osc.beanry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReLevelBean {

    /**
     * code : 200
     * msg : [{"gname":"斗罗大陆","extend":"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Finews.gtimg.com%2Fnewsapp_match%2F0%2F12913276164%2F0&refer=http%3A%2F%2Finews.gtimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1664020017&t=5a8c52047a392231513cc2f6540f13c3","searchable":"1"},{"gname":"拿一座城市下酒","extend":"https://puui.qpic.cn/media_img/lena/PIChxhpn4_580_1680/0","searchable":"1"},{"gname":"广告【微信扫码关注公众号】","extend":"https://img.zcool.cn/community/0100fa57feef3aa84a0d304f5bf46b.jpg","searchable":"0"}]
     * time : 1661924106
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("time")
    public Integer time;
    @SerializedName("msg")
    public List<MsgDTO> msg;

    public static class MsgDTO {
        /**
         * gname : 斗罗大陆
         * extend : https://gimg2.baidu.com/image_search/src=http%3A%2F%2Finews.gtimg.com%2Fnewsapp_match%2F0%2F12913276164%2F0&refer=http%3A%2F%2Finews.gtimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1664020017&t=5a8c52047a392231513cc2f6540f13c3
         * searchable : 1
         */

        @SerializedName("name")
        public String name;
        @SerializedName("extend")
        public String extend;
        @SerializedName("searchable")
        public Integer searchable;
    }
}
