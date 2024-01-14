package com.github.tvbox.osc.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeBanner {

    /**
     * code : 1
     * msg : 轮播图
     * list : [{"vod_id":147513,"vod_name":"片段六：抖音942149579","vod_pic":"http://124.222.84.216:8086/upload/vod/20220715-1/f9cac58c6843434826ba44d3b4e790ea.jpeg","vod_pic_slide":"http://124.222.84.216:8086/upload/vod/20220715-1/f9cac58c6843434826ba44d3b4e790ea.jpeg","vod_score":"0.0","type_id":1,"vod_en":"SPJS","vod_time_add":"2022-07-15 16:03:44","vod_remarks":"全3集"},{"vod_id":147512,"vod_name":"片段五：抖音942149579","vod_pic":"http://124.222.84.216:8086/upload/vod/20220715-1/cc920475b52e414c42da20c174c02929.jpeg","vod_pic_slide":"http://124.222.84.216:8086/upload/vod/20220715-1/cc920475b52e414c42da20c174c02929.jpeg","vod_score":"0.0","type_id":1,"vod_en":"SPJS","vod_time_add":"2022-07-15 16:03:28","vod_remarks":"全3集"},{"vod_id":147511,"vod_name":"片段四：抖音942149579","vod_pic":"http://124.222.84.216:8086/upload/vod/20220715-1/8cc1e02c074cdaabb92464c8a27d472d.jpeg","vod_pic_slide":"http://124.222.84.216:8086/upload/vod/20220715-1/8cc1e02c074cdaabb92464c8a27d472d.jpeg","vod_score":"0.0","type_id":1,"vod_en":"SPJS","vod_time_add":"2022-07-15 16:03:14","vod_remarks":"全3集"},{"vod_id":147507,"vod_name":"片段三：抖音942149579","vod_pic":"http://124.222.84.216:8086/upload/vod/20220715-1/e082196e65bc642fc3d3780ea4f42c39.jpeg","vod_pic_slide":"http://124.222.84.216:8086/upload/vod/20220715-1/e082196e65bc642fc3d3780ea4f42c39.jpeg","vod_score":"0.0","type_id":1,"vod_en":"PDS","vod_time_add":"2022-07-15 15:55:57","vod_remarks":"全3集"},{"vod_id":147464,"vod_name":"片段二：抖音942149579","vod_pic":"http://124.222.84.216:8086/upload/vod/20220715-1/50c5ad1f09273e4a65ec2eb5d29a646b.jpeg","vod_pic_slide":"http://124.222.84.216:8086/upload/vod/20220715-1/50c5ad1f09273e4a65ec2eb5d29a646b.jpeg","vod_score":"0.0","type_id":1,"vod_en":"SPJSPD","vod_time_add":"2022-07-15 14:41:12","vod_remarks":"全3集"},{"vod_id":147506,"vod_name":"片段一：抖音942149579","vod_pic":"http://124.222.84.216:8086/upload/vod/20220715-1/5fdea038db78d14d2cc37c1ad33f1693.jpeg","vod_pic_slide":"http://124.222.84.216:8086/upload/vod/20220715-1/5fdea038db78d14d2cc37c1ad33f1693.jpeg","vod_score":"0.0","type_id":1,"vod_en":"SPJS","vod_time_add":"2022-07-15 15:49:17","vod_remarks":"全3集"}]
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("list")
    public List<ListDTO> list;

    public static class ListDTO {
        /**
         * vod_id : 147513
         * vod_name : 片段六：抖音942149579
         * vod_pic : http://124.222.84.216:8086/upload/vod/20220715-1/f9cac58c6843434826ba44d3b4e790ea.jpeg
         * vod_pic_slide : http://124.222.84.216:8086/upload/vod/20220715-1/f9cac58c6843434826ba44d3b4e790ea.jpeg
         * vod_score : 0.0
         * type_id : 1
         * vod_en : SPJS
         * vod_time_add : 2022-07-15 16:03:44
         * vod_remarks : 全3集
         */

        @SerializedName("vod_id")
        public Integer vodId;
        @SerializedName("vod_name")
        public String vodName;
        @SerializedName("vod_pic")
        public String vodPic;
        @SerializedName("vod_pic_slide")
        public String vodPicSlide;
        @SerializedName("vod_score")
        public String vodScore;
        @SerializedName("type_id")
        public Integer typeId;
        @SerializedName("vod_en")
        public String vodEn;
        @SerializedName("vod_time_add")
        public String vodTimeAdd;
        @SerializedName("vod_remarks")
        public String vodRemarks;
    }
}
