package com.github.tvbox.osc.beanxg;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VipListBean {

    /**
     * code : 1
     * msg : vip价目表
     * data : [{"type":1,"name":"日卡","price":10,"remark":"300元/月"},{"type":2,"name":"周卡","price":20,"remark":"80元/月"},{"type":3,"name":"月卡","price":30,"remark":"30元/月"},{"type":4,"name":"年卡","price":40,"remark":"3元/月"}]
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public List<DataDTO> data;

    public static class DataDTO {
        /**
         * type : 1
         * name : 日卡
         * price : 10
         * remark : 300元/月
         */

        @SerializedName("type")
        public Integer type;
        @SerializedName("name")
        public String name;
        @SerializedName("price")
        public Integer price;
        @SerializedName("remark")
        public String remark;
    }
}
