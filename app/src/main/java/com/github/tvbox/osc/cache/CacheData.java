package com.github.tvbox.osc.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.tvbox.osc.base.App;

import java.util.Map;

public class CacheData {
    public static final SharedPreferences preferences= App.getAppContext().getSharedPreferences("DxianluAdapter", Context.MODE_PRIVATE);
    public static final SharedPreferences.Editor editor=preferences.edit();

    public static boolean CacheString(String key,String value){
        try {
            editor.putString(key,value);
            editor.commit();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean CacheBoolean(String key,boolean value){
        try {
            editor.putBoolean(key, value);
            editor.commit();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean CacheFloat(String key,float value){
        try {
            editor.putFloat(key, value);
            editor.commit();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean CacheInt(String key,int value){
        try {
            editor.putInt(key, value);
            editor.commit();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean CacheLong(String key,long value){
        try {
            editor.putLong(key, value);
            editor.commit();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static String getCacheString(String key){
        try {
            return preferences.getString(key, null);
        }catch (Exception e){
            return null;
        }
    }

    public static Boolean getCacheBoolean(String key){
        try {
            return preferences.getBoolean(key,false);
        }catch (Exception e){
            return null;
        }
    }

    public static float getCacheFloat(String key){
        try {
            return preferences.getFloat(key,0.0f);
        }catch (Exception e){
            return 0.0f;
        }
    }

    public static int getCacheInt(String key){
        try {
            return preferences.getInt(key,0);
        }catch (Exception e){
            return 0;
        }
    }

    public static long getCacheLong(String key){
        try {
            return preferences.getLong(key,0);
        }catch (Exception e){
            return 0;
        }
    }

    public static Map<String, ?> getCacheAll(){
        try {
            return preferences.getAll();
        }catch (Exception e){
            return null;
        }
    }
}
