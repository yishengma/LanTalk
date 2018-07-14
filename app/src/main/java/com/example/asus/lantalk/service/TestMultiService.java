package com.example.asus.lantalk.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by asus on 18-7-14.
 */

public class TestMultiService extends Service {
    public static final String BASE_IP_ADDRESS = "10.21.20.255";
    public static final int PORT = 3333;
    private MulticastSocket mSocket;
    private InetAddress mAddress;
    private static final String TAG = "TestMultiService";

    @Override
    public void onCreate() {
        try {
            mAddress = InetAddress.getByName(BASE_IP_ADDRESS);
//            if (!mAddress.isMulticastAddress()) {
//                throw new Exception("NoMulticast");
//            }

            mSocket = new MulticastSocket(PORT);
            mSocket.setTimeToLive(255);
//            mSocket.joinGroup(mAddress);
            new WorkThread().start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onCreate();
    }
    private class WorkThread extends Thread {

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, 1024);
            while (true) {
                try {
                    mSocket.receive(datagramPacket);
                    String result = new String(buffer,0,datagramPacket.getLength());
                    Log.e(TAG, "run: "+result );
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
