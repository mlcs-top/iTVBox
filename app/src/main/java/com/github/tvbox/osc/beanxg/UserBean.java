package com.github.tvbox.osc.beanxg;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserBean implements Serializable {

    /**
     * code : 1
     * msg : 登录成功
     * data : {"user_id":9,"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0aW1lIjoxNjU4NzcxNjE5LCJ1aWQiOjl9.a4zuNVxgx0jj9vE1ZgQUsuS_qSivnDMiiIzS8cJfpi0","user_name":"sg-app1","user_email":"","user_points":10,"user_end_time":"1970.01.01到期","user_type":1}
     */

    @SerializedName("code")
    public Integer code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public DataDTO data;

    public static class DataDTO implements Serializable {
        /**
         * user_id : 9
         * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0aW1lIjoxNjU4NzcxNjE5LCJ1aWQiOjl9.a4zuNVxgx0jj9vE1ZgQUsuS_qSivnDMiiIzS8cJfpi0
         * user_name : sg-app1
         * user_email :
         * user_points : 10
         * user_end_time : 1970.01.01到期
         * user_type : 1
         */

        @SerializedName("user_id")
        public Integer userId;
        @SerializedName("token")
        public String token;
        @SerializedName("user_name")
        public String userName;
        @SerializedName("user_email")
        public String userEmail;
        @SerializedName("user_points")
        public int userPoints;
        @SerializedName("user_end_time")
        public String userEndTime;
        @SerializedName("user_type")
        public Integer userType;
    }
}
