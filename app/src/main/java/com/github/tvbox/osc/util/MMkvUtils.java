package com.github.tvbox.osc.util;

import com.blankj.utilcode.util.GsonUtils;
import com.github.tvbox.osc.beanry.InitBean;
import com.github.tvbox.osc.beanry.ReJieXiBean;
import com.github.tvbox.osc.beanry.ReLevelBean;
import com.github.tvbox.osc.beanry.ReUserBean;
import com.github.tvbox.osc.beanry.SiteBean;
import com.tencent.mmkv.MMKV;

import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

public class MMkvUtils {

    //如意配置
    private static final String USER = "user";
    private static final String PASSWD = "passwd";
    private static final String INIT_BEAN = "InitBean";
    private static final String RE_USER_BEAN = "ReUserBean";
    private static final String SITE_BEAN = "SiteBean";
    private static final String RE_JIE_XI_BEAN = "ReJieXiBean";
    private static final String RE_LEVEL_BEAN = "ReLevelBean";


    private static final MMKV kv = MMKV.defaultMMKV();

    public static void saveUser(String user){
        kv.encode(USER, user);
    }

    public static String loadUser(){
        String var10000 = kv.decodeString(USER);
        if (var10000 == null){
            return "";
        }
        return var10000;
    }

    public static void savePasswd(String passwd){
        kv.encode(PASSWD, passwd);
    }

    public static String loadPasswd(){
        String var10000 = kv.decodeString(PASSWD);
        if (var10000 == null){
            return "";
        }
        return var10000;
    }

    public static void saveInitBean(@Nullable InitBean value){
        if (value == null){
            kv.encode(INIT_BEAN, "");
        }else{
            String msg = GsonUtils.toJson(value);
            kv.encode(INIT_BEAN, msg);
        }
    }

    public static InitBean loadInitBean(@Nullable String defaultValue){
        Intrinsics.checkParameterIsNotNull(defaultValue, "defaultValue");
        String msg = kv.decodeString(INIT_BEAN, defaultValue);
        return msg == null || ((CharSequence)msg).length() == 0 ? null : GsonUtils.fromJson(msg, InitBean.class);
    }

    public static void saveReUserBean(@Nullable ReUserBean value){
        if (value == null){
            kv.encode(RE_USER_BEAN, "");
        }else{
            String msg = GsonUtils.toJson(value);
            kv.encode(RE_USER_BEAN, msg);
        }
    }

    public static ReUserBean loadReUserBean(@Nullable String defaultValue){
        Intrinsics.checkParameterIsNotNull(defaultValue, "defaultValue");
        String msg = kv.decodeString(RE_USER_BEAN, defaultValue);
        return msg == null || ((CharSequence)msg).length() == 0 ? null : GsonUtils.fromJson(msg, ReUserBean.class);
    }

    public static void saveSiteBean(@Nullable SiteBean value){
        if (value == null){
            kv.encode(SITE_BEAN, "");
        }else{
            String msg = GsonUtils.toJson(value);
            kv.encode(SITE_BEAN, msg);
        }
    }

    public static SiteBean loadSiteBean(@Nullable String defaultValue){
        Intrinsics.checkParameterIsNotNull(defaultValue, "defaultValue");
        String msg = kv.decodeString(SITE_BEAN, defaultValue);
        return msg == null || ((CharSequence)msg).length() == 0 ? null : GsonUtils.fromJson(msg, SiteBean.class);
    }

    public static void saveReJieXiBean(@Nullable ReJieXiBean value){
        if (value == null){
            kv.encode(RE_JIE_XI_BEAN, "");
        }else{
            String msg = GsonUtils.toJson(value);
            kv.encode(RE_JIE_XI_BEAN, msg);
        }
    }

    public static ReJieXiBean loadReJieXiBean(@Nullable String defaultValue){
        Intrinsics.checkParameterIsNotNull(defaultValue, "defaultValue");
        String msg = kv.decodeString(RE_JIE_XI_BEAN, defaultValue);
        return msg == null || ((CharSequence)msg).length() == 0 ? null : GsonUtils.fromJson(msg, ReJieXiBean.class);
    }

    public static void saveReLevelBean(@Nullable ReLevelBean value){
        if (value == null){
            kv.encode(RE_LEVEL_BEAN, "");
        }else{
            String msg = GsonUtils.toJson(value);
            kv.encode(RE_LEVEL_BEAN, msg);
        }
    }


    public static ReLevelBean loadReLevelBean(@Nullable String defaultValue){
        Intrinsics.checkParameterIsNotNull(defaultValue, "defaultValue");
        String msg = kv.decodeString(RE_LEVEL_BEAN, defaultValue);
        return msg == null || ((CharSequence)msg).length() == 0 ? null : GsonUtils.fromJson(msg, ReLevelBean.class);
    }



}


