package com.example.asus.lantalk.utils;

import android.util.Log;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static com.example.asus.lantalk.Constant.Constant.sLOCAL_HOST_IP;

/**
 * Created by asus on 18-5-8.
 */

public class ScanDeviceUtil {
    private static final String TAG = "ScanDeviceUtil";

    public static String getIPAddress(){
        String IPAddress = null;
        try{
            Enumeration enumeration = NetworkInterface.getNetworkInterfaces();
            InetAddress inetAddress = null;
            while (enumeration.hasMoreElements()){
                NetworkInterface networkInterface = (NetworkInterface) enumeration.nextElement();
                Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
                while (inetAddressEnumeration.hasMoreElements()){
                    inetAddress = inetAddressEnumeration.nextElement();
                    if (inetAddress instanceof Inet6Address){
                        continue;
                    }
                    String ip = inetAddress.getHostAddress();
                    if (!sLOCAL_HOST_IP.equals(ip)){
                        IPAddress = inetAddress.getHostAddress();
                        break;
                    }


                }
            }
        }catch (SocketException e){
            e.printStackTrace();
        }
        Log.e(TAG, "getIPAddress: "+IPAddress );
        return IPAddress;
    }

    public static String getLocalIPAddress(){
        String localIPAddress = "";
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaceEnumeration.hasMoreElements()){
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
                Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
                while (inetAddressEnumeration.hasMoreElements()){
                    InetAddress address = inetAddressEnumeration.nextElement();
                    if (!address.isLoopbackAddress()&&(address instanceof  InetAddress)){
                        localIPAddress = address.getHostAddress();
                    }
                }
            }
        }catch (SocketException e){
            e.printStackTrace();
        }
        Log.e(TAG, "getLocalIPAddress: "+localIPAddress );
        return localIPAddress;
    }
}
