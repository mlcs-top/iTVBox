package com.github.tvbox.osc.beanxg;

import com.google.gson.annotations.SerializedName;

public class XgOrderStatus {

    /**
     * code : 1
     * msg : 获取成功
     * info : {"order_id":1176,"user_id":19,"order_status":0,"order_code":"19-1659082005","order_price":"10.00","order_time":1659082005,"order_points":10,"order_pay_type":"codepay","order_pay_time":0,"order_remarks":"APP充值"}
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("info")
    public InfoDTO info;

    public static class InfoDTO {
        /**
         * order_id : 1176
         * user_id : 19
         * order_status : 0
         * order_code : 19-1659082005
         * order_price : 10.00
         * order_time : 1659082005
         * order_points : 10
         * order_pay_type : codepay
         * order_pay_time : 0
         * order_remarks : APP充值
         */

        @SerializedName("order_id")
        public Integer orderId;
        @SerializedName("user_id")
        public Integer userId;
        @SerializedName("order_status")
        public Integer orderStatus;
        @SerializedName("order_code")
        public String orderCode;
        @SerializedName("order_price")
        public String orderPrice;
        @SerializedName("order_time")
        public Integer orderTime;
        @SerializedName("order_points")
        public Integer orderPoints;
        @SerializedName("order_pay_type")
        public String orderPayType;
        @SerializedName("order_pay_time")
        public Integer orderPayTime;
        @SerializedName("order_remarks")
        public String orderRemarks;
    }
}
