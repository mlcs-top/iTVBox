package com.github.tvbox.osc.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Objects;

public class MyService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(
                () -> {
                    while (true) {
                        /**
                         * 检测是否正在使用代理，如果在使用返回true,反之返回flase
                         */
                        if (isProxyNet(this)) {
                            Toast.makeText(this, "请关闭代理或者VPN,APP已自动退出", Toast.LENGTH_LONG).show();
                            System.exit(0);
                            onDestroy();
                        }
                        /**
                         * 检测是否正在使用Vpn，如果在使用返回true,反之返回flase
                         */
                        try {
                            Enumeration niList = NetworkInterface.getNetworkInterfaces();
                            if (niList != null) {
                                for (Object f : Collections.list(niList)) {
                                    NetworkInterface intf = (NetworkInterface) f;
                                    if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
//                        Toast.makeText(this, "正常---", Toast.LENGTH_LONG).show();
                                        continue;
                                    }
                                   // Log.d("-----", "isVpnUsed() NetworkInterface Name: " + intf.getName());
                                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())) {
//                        return true; // The VPN is up
                                        Toast.makeText(this, "检测到Vpn或代理工具，APP已自动退出", Toast.LENGTH_LONG).show();
                                        System.exit(0);
                                        onDestroy();
                                    }
                                }
                            }

                        } catch (Exception e) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                            System.exit(0);
                            onDestroy();
                        }
                    }
                }
        ).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //防抓包
    private Boolean isProxyNet(Context context) {
        String proxyHost = null;
        int proxyPort = -1;
        // 获取host和端口号，并对其进行判定
        proxyHost = System.getProperty("http.proxyHost", null);
        proxyPort = Integer.parseInt(Objects.requireNonNull(System.getProperty("http.proxyPort", "-1")));
        return proxyHost != null && !proxyHost.isEmpty() && proxyPort != -1;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
