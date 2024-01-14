package com.github.tvbox.osc.beanxg;

import com.google.gson.annotations.SerializedName;

public class XgPayBean {

    /**
     * code : 1
     * msg : 提交成功
     * data : {"url":"https://996pay.net/mapi.php?money=10&name=11&notify_url=http%3A%2F%2F124.222.84.216%3A8086%2Fapi.php%2Fapp%2Fcodepay_notify&out_trade_no=3-1658921702&pid=1000&return_url=http%3A%2F%2F124.222.84.216%3A8086%2Findex.php%2Fpayment%2Fnotify%2Fpay_type%2Fepay&type=alipay&sign=8855882a27cfe69f5dccbb28f110f9eb","order_code":"3-1658921702"}
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public DataDTO data;

    public static class DataDTO {
        /**
         * url : https://996pay.net/mapi.php?money=10&name=11&notify_url=http%3A%2F%2F124.222.84.216%3A8086%2Fapi.php%2Fapp%2Fcodepay_notify&out_trade_no=3-1658921702&pid=1000&return_url=http%3A%2F%2F124.222.84.216%3A8086%2Findex.php%2Fpayment%2Fnotify%2Fpay_type%2Fepay&type=alipay&sign=8855882a27cfe69f5dccbb28f110f9eb
         * order_code : 3-1658921702
         */

        @SerializedName("url")
        public String url;
        @SerializedName("order_code")
        public String orderCode;
    }
}
