package com.example.asus.lantalk.utils;


import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;

import java.net.NetworkInterface;

import java.net.SocketException;

import java.util.Enumeration;


/**
 * Created by asus on 18-5-8.
 */

public class NetIPUtil {
    /**
     * 获取广播地址
     *
     * @return
     */
    public static String getBroadcastIPAddress() {

        return getLocAddrIndex(getLocAddress()) + 255;

    }

    /**
     * TODO<获取本地ip地址>
     *
     * @return String
     */
    public static String getLocAddress() {
        String ipaddress = "";

        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface networks = en.nextElement();
                // 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> address = networks.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (address.hasMoreElements()) {
                    InetAddress ip = address.nextElement();
                    if (!ip.isLoopbackAddress()
                            && (ip instanceof Inet4Address)) {
                        ipaddress = ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("", "获取本地ip地址失败");
            e.printStackTrace();
        }
        return ipaddress;
    }

    /**
     * TODO<获取本机IP前缀>
     *
     * @param devAddress // 本机IP地址
     * @return String
     */
    private static String getLocAddrIndex(String devAddress) {
        if (!devAddress.equals("")) {
            return devAddress.substring(0, devAddress.lastIndexOf(".") + 1);
        }
        return null;
    }

}