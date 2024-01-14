package com.github.tvbox.osc.util;

import java.io.FileInputStream;
import java.util.Properties;

//零熙QQ：1007713299
public class PropertiesUtils {
    private static Properties properties = new Properties();

    public static void load(String filePath){
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            properties.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperties(String key){
       return properties.getProperty(key,"");
    }
}
//零熙QQ：1007713299