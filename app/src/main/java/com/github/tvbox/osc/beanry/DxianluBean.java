package com.github.tvbox.osc.beanry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DxianluBean {

    /**
     "storeHouse":
     */
    @SerializedName("storeHouse")
    public List<MsgDTO> storeHouse;

    public static class MsgDTO {
        /**
         "sourceName":"💚茶茶优选仓",
         "sourceUrl":"https://jihulab.com/duomv/apps/-/raw/main/a.txt"

         */

        @SerializedName("sourceName")
        public String sourceName;
        @SerializedName("sourceUrl")
        public String sourceUrl;
    }
}
