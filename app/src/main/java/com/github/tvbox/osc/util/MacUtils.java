package com.github.tvbox.osc.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class MacUtils {

    private static final String TAG = "App";

    /**
     * 获取mac地址,首先获取有线mac,没有则获取无线mac
     * @param needColon 是否需要带上冒号
     * true 需要携带冒号 - 11:22:33:44:55:66
     * false 不需要携带冒号 - 112233445566
     */
    public static String getMac(boolean needColon) {
        String stbMac = getSTBMac(needColon);
        if (null != stbMac) {
            return stbMac;
        } else {
            return getWifiMac(needColon);
        }
    }

    /**
     * Get the STB MacAddress
     * @param needColon
     */
    public static String getSTBMac(boolean needColon) {
        try {
            String filePath = "/sys/class/net/eth0/address";
            StringBuffer sb = new StringBuffer(100);
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            char[] buf = new char[1024];
            int len = 0;
            while ((len = br.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, len);
                sb.append(readData);
            }
            br.close();
            if (needColon){
                return sb.toString().trim();
            }
            return sb.toString().trim().replace(":", "");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the wifi MacAddress
     * @param needColon
     */
    public static String getWifiMac(boolean needColon) {
        try {
            Process p = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            LineNumberReader input = new LineNumberReader(isr);
            String str = "";
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    if (needColon){
                        return str.trim();
                    }
                    return str.trim().replace(":", "");
                }
            }
            input.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDeviceSN(){
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            java.lang.reflect.Method get =c.getMethod("get", String.class);
            serial = (String)get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }

    /**
     */
    public static String getMac2() {
        String stbMac = macAddress();
        if (null != stbMac) {
            return stbMac;
        } else {
            return getMacAddress();
        }
    }
    public static String macAddress() {
        try {
            String address = null;
            // 把当前机器上访问网络的接口存入 Enumeration集合中
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            Log.d("TEST_BUG", " interfaceName = " + interfaces );
            while (interfaces.hasMoreElements()) {
                NetworkInterface netWork = interfaces.nextElement();
                // 如果存在硬件地址并可以使用给定的当前权限访问，则返回该硬件地址（通常是 MAC）。
                byte[] by = netWork.getHardwareAddress();
                if (by == null || by.length == 0) {
                    continue;
                }
                StringBuilder builder = new StringBuilder();
                for (byte b : by) {
                    builder.append(String.format("%02X:", b));
                }
                if (builder.length() > 0) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                String mac = builder.toString();
                Log.d("TEST_BUG", " interfaceName ="+netWork.getName()+", mac="+mac);
                // 从路由器上在线设备的MAC地址列表，可以印证设备Wifi的 name 是 wlan0
                if (netWork.getName().equals("wlan0")) {
                    address = mac;
                    Log.d("TEST_BUG", " interfaceName ="+netWork.getName()+", address="+address);
                }
            }
            return address;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取MAC地址
     */
    public static String getMacAddress() {
        try {
            // 把当前机器上访问网络的接口存入 List集合中
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!"wlan0".equalsIgnoreCase(nif.getName())) {
                    continue;
                }
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null || macBytes.length == 0) {
                    continue;
                }
                StringBuilder result = new StringBuilder();
                for (byte b : macBytes) {
                    //每隔两个字符加一个:
                    result.append(String.format("%02X:", b));
                }
                if (result.length() > 0) {
                    //删除最后一个:
                    result.deleteCharAt(result.length() - 1);
                }
                return result.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown";
    }
}