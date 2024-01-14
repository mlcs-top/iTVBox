package com.github.tvbox.osc.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * SharePreferences 工具类
 * 如：保存 int、 String、 boolean、 float、long、 Set < String >，
 * 获取int、 String、 boolean、 float、long、set，
 * 清除当前xml 某一个key对应的值、所有值
 *
 */
public class SharePreferencesUtils {

    /**
     * 保存 int
     *
     * @param context 上下文环境
     * @param xmlName xml文件名
     * @param key     要保存的 key
     * @param value   要保存的 value
     */
    public static void saveInt(Context context, String xmlName, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 保存 String
     *
     * @param context 上下文环境
     * @param xmlName xml文件名
     * @param key     要保存的 key
     * @param value   要保存的 value
     */
    public static void saveString(Context context, String xmlName, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 保存 boolean
     *
     * @param context 上下文环境
     * @param xmlName xml文件名
     * @param key     要保存的 key
     * @param value   要保存的 value
     */
    public static void saveBoolean(Context context, String xmlName, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 保存 float
     *
     * @param context 上下文环境
     * @param xmlName xml文件名
     * @param key     要保存的 key
     * @param value   要保存的 value
     */
    public static void saveFloat(Context context, String xmlName, String key, float value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    /**
     * 保存 long
     *
     * @param context 上下文环境
     * @param xmlName xml文件名
     * @param key     要保存的 key
     * @param value   要保存的 value
     */
    public static void saveLong(Context context, String xmlName, String key, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * 保存 Set < String >
     *
     * @param context 上下文环境
     * @param xmlName xml文件名
     * @param key     要保存的 key
     * @param value   要保存的 value
     */
    public static void saveSet(Context context, String xmlName, String key, Set<String> value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    /**
     * 获取set
     *
     * @param context 上下文环境
     * @param xmlName xml文件名
     * @param key     要获取的 key
     */
    public static Set<String> getSet(Context context, String xmlName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(key, new HashSet<String>());
    }

    /**
     * 清除当前xml 某一个key对应的值
     *
     * @param context 上下文环境
     * @param xmlName xml文件名
     * @param key     要清除的key
     */
    public static void clearOne(Context context, String xmlName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 清除xml中所有的值
     *
     * @param context 上下文环境
     * @param xmlName xml文件名
     */
    public static void clearAll(Context context, String xmlName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 获取int 值
     *
     * @param context      上下文环境
     * @param xmlName      xml文件名
     * @param key          要获取的key
     * @param defaultValue 默认值
     */
    public static int getInt(Context context, String xmlName, String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        int value = sharedPreferences.getInt(key, defaultValue);
        return value;
    }

    /**
     * 获取 String 值
     *
     * @param context      上下文环境
     * @param xmlName      xml文件名
     * @param key          要获取的key
     * @param defaultValue 默认值
     */
    public static String getString(Context context, String xmlName, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * 获取Boolean 值
     *
     * @param context      上下文环境
     * @param xmlName      xml文件名
     * @param key          要获取的key
     * @param defaultValue 默认值
     * @return
     */
    public static boolean getBoolean(Context context, String xmlName, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * 获取long 值
     *
     * @param context      上下文环境
     * @param xmlName      xml文件名
     * @param key          要获取的key
     * @param defaultValue 默认值
     */
    public static long getLong(Context context, String xmlName, String key, long defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, defaultValue);
    }

    /**
     * 获取float 值
     *
     * @param context      上下文环境
     * @param xmlName      xml文件名
     * @param key          要获取的key
     * @param defaultValue 默认值
     */
    public static float getFloat(Context context, String xmlName, String key, float defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, defaultValue);
    }
}
