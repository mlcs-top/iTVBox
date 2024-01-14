package com.github.tvbox.osc.beanry;

import java.util.List;

public class AdvBean {
    public int code;
    public List<MsgDTO> msg;
    public int time;

    public static class MsgDTO {
        public String name;
        public String extend;
        public String searchable;
    }
//
//    public int code;
//    public List<MsgDTO> msg;
//    public int time;
//
//    public static class MsgDTO {
//        public String name;
//        public String extend;
//        public String searchable;
//    }


}
